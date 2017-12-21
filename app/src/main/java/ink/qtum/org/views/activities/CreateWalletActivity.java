package ink.qtum.org.views.activities;

import android.os.Bundle;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.OnClick;
import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.WalletManager;
import ink.qtum.org.views.activities.base.BaseActivity;

/**
 * Created by SV on 18.12.2017.
 */

@AutoInjector(QtumApp.class)
public class CreateWalletActivity extends BaseActivity {

    @Inject
    WalletManager walletManager;

    @Override
    protected void init(Bundle savedInstanceState) {
        QtumApp.getAppComponent().inject(this);
        enableBackButton();
}

    @Override
    protected int getLayout() {
        return R.layout.activity_create_new;
    }

    @OnClick(R.id.button)
    void openMain(){
        openMainActivity();
    }

}
