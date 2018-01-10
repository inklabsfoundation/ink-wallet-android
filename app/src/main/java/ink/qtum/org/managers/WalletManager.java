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
import org.bitcoinj.params.QtumMainNetParams;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.spongycastle.util.encoders.Hex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import ink.qtum.org.QtumApp;
import ink.qtum.org.models.response.UtxoItemResponse;
import ink.qtum.org.rest.ApiMethods;
import ink.qtum.org.rest.Requestor;
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
    private Coin myBalance;
    private Context context;
    private static NetworkParameters params = QtumMainNetParams.get();
    private String mnemonicKey;
    private HashSet<String> mBip39Words;

    public WalletManager(Context context) {
        QtumApp.getAppComponent().inject(this);
        this.context = context;
        mBip39Words = FileUtils.readToSet(context, BIP_39_WORDLIST_ASSET);
    }

    public void createWallet(String passphrase, WalletCreationCallback callback) {

        wallet = new Wallet(params);
        DeterministicSeed seed = wallet.getKeyChainSeed();

        mnemonicKey = Joiner.on(" ").join(seed.getMnemonicCode());
        sharedManager.setLastSyncedBlock(CryptoUtils.encodeBase64(mnemonicKey));
        walletFriendlyAddress = wallet.currentReceiveAddress().toString();
        callback.onWalletCreated(wallet);

    }

    public String getMnemonic() {
        return mnemonicKey;
    }

    public String getWalletFriendlyAddress() {
//        return "QPKacYwu6iXMytVVzwtZP7G4iKe9bmr4op";
        return wallet.currentReceiveAddress().toString();
    }

    public void restoreWallet(String mnemonicCode, WalletCreationCallback callback) {
        if (mnemonicCode.charAt(mnemonicCode.length() - 1) == ' ') {
            mnemonicCode = mnemonicCode.substring(0, mnemonicCode.length() - 1);
        }

        DeterministicSeed seed = new DeterministicSeed(Splitter.on(' ').splitToList(mnemonicCode), null, "", 0);

        wallet = Wallet.fromSeed(params, seed);
        mnemonicKey = Joiner.on(" ").join(seed.getMnemonicCode());
        sharedManager.setLastSyncedBlock(CryptoUtils.encodeBase64(mnemonicKey));
        walletFriendlyAddress = wallet.currentReceiveAddress().toString();

        callback.onWalletCreated(wallet);

        Requestor.getUTXOList(wallet.currentReceiveAddress().toString(), new ApiMethods.RequestListener() {
            @Override
            public void onSuccess(Object response) {
                setUTXO((List<UtxoItemResponse>)response);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    private void setUTXO(List<UtxoItemResponse> utxoList) {

        Address a = wallet.currentReceiveAddress();
        final List<UTXO> utxos = new ArrayList<>();

        Log.d("svcom", "resp size - " + utxoList.size());
        for (UtxoItemResponse utxo : utxoList) {
            Sha256Hash hash = Sha256Hash.wrap(utxo.getTxid());
            utxos.add(new UTXO(hash, utxo.getVout(), Coin.valueOf(utxo.getSatoshis()),
                    utxo.getHeight(), false, ScriptBuilder.createOutputScript(a)));
        }
        for (UTXO u : utxos) {
            Log.d("svcom", "u - " + u.getValue().toFriendlyString());
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
        wallet.setUTXOProvider(utxoProvider);
    }

    public static String SMALL_SENDING = "insufficientMoney";
    public static String NOT_ENOUGH_MONEY = "notEnough";

    public String generateQtumHexTx(String toAddress, long sumInSatoshi, long feeInSatoshi) {
        Address RECEIVER = Address.fromBase58(params, toAddress);

        Coin AMOUNT = Coin.valueOf(sumInSatoshi);
        Coin FEE = Coin.valueOf(feeInSatoshi);

        Log.d("svcom", "tx - amount = " + AMOUNT.toFriendlyString() + " fee = " + FEE.toFriendlyString());
        /**
         * available default fee
         * Transaction.REFERENCE_DEFAULT_MIN_TX_FEE;
         * Transaction.DEFAULT_TX_FEE;
         */
        SendRequest sendRequest = SendRequest.to(RECEIVER, AMOUNT);
        sendRequest.changeAddress = wallet.currentReceiveAddress();
        sendRequest.feePerKb = FEE;


        Transaction trx = null;
        String hex = "";
        try {
            trx = wallet.sendCoinsOffline(sendRequest);
            Log.d("svcom", "size = " + trx.bitcoinSerialize().length);
            hex = Hex.toHexString(trx.bitcoinSerialize());
            Log.d("svcom", "hex: " + hex);
        } catch (InsufficientMoneyException e) {
            e.printStackTrace();
            return NOT_ENOUGH_MONEY;
        } catch (Wallet.DustySendRequested e) {
            e.printStackTrace();
            return SMALL_SENDING;
        }
        return hex;
    }

    public void sendTx(String rawTx, ApiMethods.RequestListener listener){
        Requestor.sendRawTx(rawTx, listener);
    }

    public boolean isValidQtumAddress(String address){
        try {
            Address.fromBase58(params, address);
            return true;
        } catch (AddressFormatException e){
            return false;
        }
    }

}
