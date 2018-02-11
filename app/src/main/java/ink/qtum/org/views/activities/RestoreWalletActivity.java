package ink.qtum.org.views.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.bitcoinj.wallet.Wallet;

import javax.inject.Inject;

import autodagger.AutoInjector;
import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.SharedManager;
import ink.qtum.org.managers.WalletCreationCallback;
import ink.qtum.org.managers.WalletManager;
import ink.qtum.org.utils.Coders;
import ink.qtum.org.views.activities.base.AToolbarActivity;
import ink.qtum.org.views.fragments.InputMnemonicFragment;
import ink.qtum.org.views.fragments.RestoreWalletConfirmPinFragment;
import ink.qtum.org.views.fragments.RestoreWalletPinFragment;


@AutoInjector(QtumApp.class)
public class RestoreWalletActivity extends AToolbarActivity implements InputMnemonicFragment.OnMnemonicFragmentInteractionListener,
        RestoreWalletPinFragment.OnPinFragmentInteractionListener,
        RestoreWalletConfirmPinFragment.OnPinConfirmedListener {

    @Inject
    WalletManager walletManager;

    @Inject
    SharedManager sharedManager;

    private String mnemonic;

    @Override
    protected void init(Bundle savedInstanceState) {
        QtumApp.getAppComponent().inject(this);
        setToolBarTitle(getString(R.string.btn_restore_ink_wallet));
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
        mnemonic = mnemonics;
        showPinFragment();
    }

    @Override
    public void onPinEntered(String pin) {
        showConfirmPinFragment(pin);
    }

    @Override
    public void onConfirmed(String pin) {
        showProgress();
        sharedManager.setPinCode(Coders.getSha1Hex(pin));
        walletManager.restoreWallet(mnemonic, new WalletCreationCallback() {
            @Override
            public void onWalletCreated(Wallet wallet) {
                closeProgress();
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

    private void showPinFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_container, RestoreWalletPinFragment.newInstance())
                .commit();
    }

    private void showConfirmPinFragment(String pin) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_container, RestoreWalletConfirmPinFragment.newInstance(pin))
                .commit();
    }
}
