package ink.qtum.org.datastorage;

/**
 * Created by sv on 25.01.18.
 */

import com.google.common.collect.ImmutableList;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class KeyStorage implements Serializable {

    private static KeyStorage sKeyStorage;
    private List<DeterministicKey> mDeterministicKeyList;
    private List<String> mAddressesList;
    private Wallet sWallet = null;
    private final int ADDRESSES_COUNT = 10;

    public static KeyStorage getInstance() {
        if (sKeyStorage == null) {
            sKeyStorage = new KeyStorage();
        }
        return sKeyStorage;
    }

    private KeyStorage() {
    }

    public void setWallet(Wallet wallet) {
        this.sWallet = wallet;
    }

    public List<DeterministicKey> getKeyList(NetworkParameters parameters) {
        if (mDeterministicKeyList == null || mDeterministicKeyList.isEmpty()) {
            mDeterministicKeyList = new ArrayList<>(ADDRESSES_COUNT);
            mAddressesList = new ArrayList<>();
            List<ChildNumber> pathParent = new ArrayList<>();
            pathParent.add(new ChildNumber(88, true));
            pathParent.add(new ChildNumber(0, true));
            for (int i = 0; i < ADDRESSES_COUNT; i++) {
                ImmutableList<ChildNumber> path = HDUtils.append(pathParent, new ChildNumber(i, true));
                DeterministicKey k = sWallet.getActiveKeyChain().getKeyByPath(path, true);
                mDeterministicKeyList.add(k);
                mAddressesList.add(k.toAddress(parameters).toString());
            }
        }
        return mDeterministicKeyList;
    }
}
