package ink.qtum.org.managers;

import android.content.Context;
import android.util.Log;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.UTXO;
import org.bitcoinj.core.UTXOProvider;
import org.bitcoinj.core.UTXOProviderException;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.params.QtumMainNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.KeyChain;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.spongycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import autodagger.AutoInjector;
import ink.qtum.org.QtumApp;
import ink.qtum.org.models.contract.ContractMethodParameter;
import ink.qtum.org.models.contract.UnspentOutput;
import ink.qtum.org.models.response.UtxoItemResponse;
import ink.qtum.org.rest.ApiMethods;
import ink.qtum.org.rest.Requestor;
import ink.qtum.org.utils.ContractBuilder;
import ink.qtum.org.utils.CryptoUtils;
import ink.qtum.org.utils.FileUtils;

import static ink.qtum.org.models.Constants.BIP_39_WORDLIST_ASSET;

/**
 * Created by SV on 21.12.2017.
 */

@AutoInjector(QtumApp.class)
public class WalletManager {

    @Inject
    SharedManager sharedManager;

    private Wallet wallet;
    private String walletFriendlyAddress;
    private Address walletAddress;
    private Coin walletQtumBalance;
    private Context context;
    private static NetworkParameters params = QtumMainNetParams.get();
    private String mnemonicKey;
    private HashSet<String> mBip39Words;

    public WalletManager(Context context) {
        QtumApp.getAppComponent().inject(this);
        this.context = context;
        mBip39Words = FileUtils.readToSet(context, BIP_39_WORDLIST_ASSET);
    }

    public void createWallet(WalletCreationCallback callback) {

        wallet = new Wallet(params);
        DeterministicSeed seed = wallet.getKeyChainSeed();

        mnemonicKey = Joiner.on(" ").join(seed.getMnemonicCode());
        sharedManager.setLastSyncedBlock(CryptoUtils.encodeBase64(mnemonicKey));
        updateMainAddress();
        callback.onWalletCreated();

    }

    public String getMnemonic() {
        return mnemonicKey;
    }

    public String getWalletFriendlyAddress() {
        return walletFriendlyAddress;
    }

    public void restoreWallet(String mnemonicCode, WalletCreationCallback callback) {
        if (mnemonicCode.charAt(mnemonicCode.length() - 1) == ' ') {
            mnemonicCode = mnemonicCode.substring(0, mnemonicCode.length() - 1);
        }

        DeterministicSeed seed = new DeterministicSeed(Splitter.on(' ').splitToList(mnemonicCode), null, "", 0);

        wallet = Wallet.fromSeed(params, seed);
        mnemonicKey = Joiner.on(" ").join(seed.getMnemonicCode());
        sharedManager.setLastSyncedBlock(CryptoUtils.encodeBase64(mnemonicKey));

        updateMainAddress();
        callback.onWalletCreated();
        updateWallet();
    }

    private void updateMainAddress(){
        DeterministicKey key = wallet.getActiveKeyChain().getKey(KeyChain.KeyPurpose.RECEIVE_FUNDS);
        walletAddress = key.toAddress(params);
        walletFriendlyAddress = walletAddress.toBase58();
    }

    public void updateWallet() {
        Requestor.getUTXOList(walletFriendlyAddress, new ApiMethods.RequestListener() {
            @Override
            public void onSuccess(Object response) {
                setUTXO((List<UtxoItemResponse>) response);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    private void setUTXO(List<UtxoItemResponse> utxoList) {

        Address a = walletAddress;
        final List<UTXO> utxos = new ArrayList<>();

        for (UtxoItemResponse utxo : utxoList) {
            Sha256Hash hash = Sha256Hash.wrap(utxo.getTxid());
            utxos.add(new UTXO(hash, utxo.getVout(), Coin.valueOf(utxo.getSatoshis()),
                    utxo.getHeight(), false, ScriptBuilder.createOutputScript(a)));
        }

        UTXOProvider utxoProvider = new UTXOProvider() {
            @Override
            public List<UTXO> getOpenTransactionOutputs(List<Address> addresses) throws UTXOProviderException {
                return utxos;
            }

            @Override
            public int getChainHeadHeight() throws UTXOProviderException {
                return Integer.MAX_VALUE;
            }

            @Override
            public NetworkParameters getParams() {
                return wallet.getParams();
            }
        };
        wallet.reset();
        wallet.setUTXOProvider(utxoProvider);
        walletQtumBalance = wallet.getBalance();
    }

    private final static String SMALL_SENDING = "insufficientMoney";
    private final static String NOT_ENOUGH_MONEY = "notEnough";

    public String generateQtumHexTx(String toAddress, long sumInSatoshi, long feeInSatoshi,
                                    String description) {

        Address RECEIVER = Address.fromBase58(params, toAddress);

        Coin AMOUNT = Coin.valueOf(sumInSatoshi);
        Coin FEE = Coin.valueOf(feeInSatoshi);
        /**
         * available default fee
         * Transaction.REFERENCE_DEFAULT_MIN_TX_FEE;
         * Transaction.DEFAULT_TX_FEE;
         */
        SendRequest sendRequest = SendRequest.to(RECEIVER, AMOUNT);
        sendRequest.shuffleOutputs = false;
        sendRequest.changeAddress = walletAddress;
        sendRequest.feePerKb = FEE;
        sendRequest.shuffleOutputs = false;

        if (sendRequest.tx != null && !android.text.TextUtils.isEmpty(description)){
            try {
                byte[] descriptionsBytes = description.getBytes("UTF-8");

                /*OP_RETURN with  message limit is 80 bytes*/
                if (descriptionsBytes.length <= 70) {
                    sendRequest.tx.addOutput(Coin.ZERO,
                            ScriptBuilder.createOpReturnScript(descriptionsBytes));
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        Transaction trx = null;
        String hex = "";
        try {
            trx = wallet.sendCoinsOffline(sendRequest);
            hex = Hex.toHexString(trx.bitcoinSerialize());
        } catch (InsufficientMoneyException e) {
            e.printStackTrace();
            return NOT_ENOUGH_MONEY;
        } catch (Wallet.DustySendRequested e) {
            e.printStackTrace();
            return SMALL_SENDING;
        }
        return hex;
    }

    public void sendTx(String rawTx, ApiMethods.RequestListener listener) {
        Requestor.sendRawTx(rawTx, listener);
    }

    public boolean isValidQtumAddress(String address) {
        try {
            Address.fromBase58(params, address);
            return true;
        } catch (AddressFormatException e) {
            return false;
        }
    }

    public Coin getQtumBalance() {
        return walletQtumBalance;
    }

    public String createTokenHexTx(String abiParams, String tokenAddress,
                                   String fee, BigDecimal feePerKb, List<UnspentOutput> unspentOutputs,
                                   String description) throws Exception {

        final int gasLimit = 300000;
        final int gasPrice = 40;

        ContractBuilder contractBuilder = new ContractBuilder();
        Script script = contractBuilder.createMethodScript(abiParams, gasLimit, gasPrice, tokenAddress);

        return contractBuilder.createTransactionHash(script, unspentOutputs, gasLimit, gasPrice,
                feePerKb, fee,
                context, params, walletAddress, wallet, description);
    }

    public String createAbiMethodParams(String address, String resultAmount) {
        ContractBuilder contractBuilder = new ContractBuilder();
        List<ContractMethodParameter> contractMethodParameterList = new ArrayList<>();
        ContractMethodParameter contractMethodParameterAddress = new ContractMethodParameter("_to", "address", address);

        ContractMethodParameter contractMethodParameterAmount = new ContractMethodParameter("_value", "uint256", resultAmount);
        contractMethodParameterList.add(contractMethodParameterAddress);
        contractMethodParameterList.add(contractMethodParameterAmount);
        return contractBuilder.createAbiMethodParams("transfer", contractMethodParameterList);
    }

    public String getValidatedFee(Double fee) {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        formatSymbols.setDecimalSeparator('.');
        String pattern = "##0.00000000";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, formatSymbols);
        return decimalFormat.format(fee);
    }

}
