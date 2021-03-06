package ink.qtum.org.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.widget.TextView;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.BuildConfig;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.SharedManager;
import ink.qtum.org.views.activities.base.BaseActivity;

import static ink.qtum.org.models.Extras.ACTION_RESTORE_SAVED;

/**
 * Created by SV on 18.12.2017.
 */

@AutoInjector(QtumApp.class)
public class LoginActivity extends BaseActivity {

    @BindView(R.id.btn_restore)
    AppCompatButton btnRestore;
    @BindView(R.id.tv_create_new)
    TextView btnCreateNew;
    @BindView(R.id.tv_version_number)
    TextView tvVersionNumber;

    @Inject
    SharedManager sharedManager;

    @Override
    protected void init(Bundle savedInstanceState) {
        QtumApp.getAppComponent().inject(this);
        tvVersionNumber.setText(String.format("v %s", BuildConfig.VERSION_NAME));
        if (!TextUtils.isEmpty(sharedManager.getLastSyncedBlock())) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction(ACTION_RESTORE_SAVED);
            startActivity(intent);
        }

    }


    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.btn_restore)
    void openRestoreActivity() {
        Intent intent = new Intent(this, RestoreWalletActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_create_new)
    void openCreateActivity() {
        Intent intent = new Intent(this, CreateWalletActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_term_of_use_link)
    public void showTermOfUse(){
        startActivity(new Intent(this, TermsOfUsageActivity.class));
    }
}
