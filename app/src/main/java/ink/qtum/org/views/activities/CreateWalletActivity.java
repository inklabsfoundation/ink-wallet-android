package ink.qtum.org.views.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ProgressBar;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.ButterKnife;
import ink.qtum.org.QtumApp;
import ink.qtum.org.views.fragments.CreatePinFragment;
import ink.qtum.org.views.fragments.CreateSeedFragment;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.WalletManager;
import ink.qtum.org.views.activities.base.BaseActivity;
import ink.qtum.org.views.fragments.CreateWalletFragment;

/**
 * Created by SV on 18.12.2017.
 */

@AutoInjector(QtumApp.class)
public class CreateWalletActivity extends BaseActivity implements CreateSeedFragment.OnSeedFragmentInteractionListener,
        CreatePinFragment.OnPinFragmentInteractionListener,
        CreateWalletFragment.OnWalletFragmentInteractionListener{

    @BindView(R.id.pb_create_progress)
    ProgressBar pbCreateProgress;

    @Inject
    WalletManager walletManager;

    @Override
    protected void init(Bundle savedInstanceState) {
        QtumApp.getAppComponent().inject(this);
        ButterKnife.bind(this);
        enableBackButton();
        setTitle(getString(R.string.create_ink_wallet));
        showSeedFragment();
}

    private void setCreateProgress(int progress) {
        pbCreateProgress.setProgress(progress);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_create_new;
    }

    private void showSeedFragment(){
        setCreateProgress(1);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_container, CreateSeedFragment.newInstance())
                .commit();
    }

    @Override
    public void onSeedEntered(String seed) {
        showPinFragment();
    }

    private void showPinFragment() {
        setCreateProgress(2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_container, CreatePinFragment.newInstance())
                .commit();
    }

    @Override
    public void onPinEntered(String seed) {
        showWalletFragment();
    }

    private void showWalletFragment() {
        setCreateProgress(3);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_container, CreateWalletFragment.newInstance())
                .commit();
    }

    @Override
    public void onFinishCreateWallet(String seed) {

    }
}
