package ink.qtum.org.views.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.bitcoinj.wallet.Wallet;

import javax.inject.Inject;

import autodagger.AutoInjector;
import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.WalletCreationCallback;
import ink.qtum.org.managers.WalletManager;
import ink.qtum.org.views.activities.base.AToolbarActivity;
import ink.qtum.org.views.fragments.InputMnemonicFragment;


@AutoInjector(QtumApp.class)
public class RestoreWalletActivity extends AToolbarActivity implements InputMnemonicFragment.OnMnemonicFragmentInteractionListener {

    @Inject
    WalletManager walletManager;

    @Override
    protected void init(Bundle savedInstanceState) {
        QtumApp.getAppComponent().inject(this);
        setToolBarTitle(getString(R.string.restore_ink_wallet));
        showMnemonicFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_restore;
    }

    @Override
    public void onMnemonicsEntered(String mnemonics) {
        restoreWallet(mnemonics);
    }

    private void restoreWallet(String mnemonics) {
        walletManager.restoreWallet(mnemonics, new WalletCreationCallback() {
            @Override
            public void onWalletCreated(Wallet wallet) {
                openMainActivity();
            }
        });
    }

    private void showMnemonicFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_container, InputMnemonicFragment.newInstance())
                .commit();
    }

}
