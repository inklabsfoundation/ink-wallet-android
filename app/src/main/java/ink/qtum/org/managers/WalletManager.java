package ink.qtum.org.managers;

import android.content.Context;

import com.google.common.base.Joiner;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.QtumMainNetParams;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;

import java.util.HashSet;

import ink.qtum.org.utils.FileUtils;

import static ink.qtum.org.models.Constants.BIP_39_WORDLIST_ASSET;

/**
 * Created by SV on 21.12.2017.
 */

public class WalletManager {

    private Wallet wallet;
    private String walletFriendlyAddress;
    private Coin myBalance;
    private Context context;
    private static NetworkParameters params = QtumMainNetParams.get();
    private String mnemonicKey;
    private HashSet<String> mBip39Words;

    public WalletManager(Context context) {
        this.context = context;
        mBip39Words = FileUtils.readToSet(context, BIP_39_WORDLIST_ASSET);
    }

    public void createWallet(String passphrase, WalletCreationCallback callback) {

        wallet = new Wallet(params);
        DeterministicSeed seed = wallet.getKeyChainSeed();

        mnemonicKey = Joiner.on(" ").join(seed.getMnemonicCode());

        walletFriendlyAddress = wallet.currentReceiveAddress().toString();
        callback.onWalletCreated(wallet);

    }

    public String getPrivateKey() {
        return mnemonicKey;
    }

    public String getWalletFriendlyAddress() {
        return wallet.currentReceiveAddress().toString();
    }
}
