package ink.qtum.org.views.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.ProgressBar;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.ButterKnife;
import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.SharedManager;
import ink.qtum.org.managers.WalletManager;
import ink.qtum.org.utils.Coders;
import ink.qtum.org.views.activities.base.AToolbarActivity;
import ink.qtum.org.views.fragments.CreateWalletConfirmPinFragment;
import ink.qtum.org.views.fragments.CreateWalletPinFragment;
import ink.qtum.org.views.fragments.CreateSeedFragment;
import ink.qtum.org.views.fragments.CreateWalletFragment;

/**
 * Created by SV on 18.12.2017.
 */

@AutoInjector(QtumApp.class)
public class CreateWalletActivity extends AToolbarActivity implements CreateSeedFragment.OnSeedFragmentInteractionListener,
        CreateWalletFragment.OnWalletFragmentInteractionListener,
        CreateWalletPinFragment.OnPinFragmentInteractionListener,
        CreateWalletConfirmPinFragment.OnPinConfirmedListener {

    @BindView(R.id.pb_create_progress)
    ProgressBar pbCreateProgress;

    @Inject
    WalletManager walletManager;

    @Inject
    SharedManager sharedManager;

//    private String mSeed;

    @Override
    protected void init(Bundle savedInstanceState) {
        QtumApp.getAppComponent().inject(this);
        ButterKnife.bind(this);
        setToolBarTitle(getString(R.string.btn_create_ink_wallet));
//        showSeedFragment();
        showPinFragment();
    }

    private void setCreateProgress(int progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pbCreateProgress.setProgress(progress, true);
        } else {
            pbCreateProgress.setProgress(progress);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_create_new;
    }

//    private void showSeedFragment() {
//        setCreateProgress(1);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.fl_fragment_container, CreateSeedFragment.newInstance())
//                .commit();
//    }

    @Deprecated
    @Override
    public void onSeedEntered(String seed) {
//        mSeed = seed;
//        showPinFragment();
    }

    private void showPinFragment() {
        setCreateProgress(1);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_container, CreateWalletPinFragment.newInstance())
                .commit();
    }

    @Override
    public void onPinEntered(String pin) {
        showConfirmPinFragment(pin);
    }

    @Override
    public void onConfirmed(String pin) {
        sharedManager.setPinCode(Coders.getSha1Hex(pin));
        showWalletFragment();
    }

    private void showWalletFragment() {
        setCreateProgress(2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_container, CreateWalletFragment.newInstance())
                .commit();
    }

    private void showConfirmPinFragment(String pin) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_container, CreateWalletConfirmPinFragment.newInstance(pin))
                .commit();
    }

    @Override
    public void onFinishCreateWallet() {
        startActivity(new Intent(CreateWalletActivity.this, MainActivity.class));
    }
}
