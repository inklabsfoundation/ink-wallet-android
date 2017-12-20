package ink.qtum.org.inkqtum.views.activities;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;

import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.inkqtum.views.activities.base.BaseActivity;

/**
 * Created by SV on 18.12.2017.
 */

public class RestoreWalletActivity extends BaseActivity {

    @Override
    protected void init(Bundle savedInstanceState) {
        enableBackButton();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_restore;
    }

    @OnClick(R.id.button)
    void openMain(){
        openMainActivity();
    }
}
