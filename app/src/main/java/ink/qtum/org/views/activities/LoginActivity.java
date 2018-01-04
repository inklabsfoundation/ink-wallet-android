package ink.qtum.org.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.activities.base.BaseActivity;

/**
 * Created by SV on 18.12.2017.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.btn_restore)
    AppCompatButton btnRestore;

    @BindView(R.id.tv_create_new)
    TextView btnCreateNew;

    @Override
    protected void init(Bundle savedInstanceState) {
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

}
