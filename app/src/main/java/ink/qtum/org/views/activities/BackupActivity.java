package ink.qtum.org.views.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.WalletManager;
import ink.qtum.org.utils.ClipboardUtils;
import ink.qtum.org.views.activities.base.AToolbarActivity;


@AutoInjector(QtumApp.class)
public class BackupActivity extends AToolbarActivity {

    @BindView(R.id.tv_mnemonics)
    TextView tvMnemonics;

    @Inject
    WalletManager walletManager;

    @Override
    protected void init(Bundle savedInstanceState) {
        QtumApp.getAppComponent().inject(this);
        setToolBarTitle(getString(R.string.title_backup));
        tvMnemonics.setText(walletManager.getPrivateKey());
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_backup;
    }

    @OnClick(R.id.btn_copy_mnemonics)
    public void copyMnemonic() {
        showAttentionDialog();
    }

    private void showAttentionDialog(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(R.string.copy_mnemonics_attention_content)
                .positiveText(R.string.btn_still_copy)
                .negativeText(R.string.btn_give_up)
                .positiveColor(getResources().getColor(R.color.btnBlueTextColor))
                .negativeColor(getResources().getColor(R.color.lightGrayTextColor))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        ClipboardUtils.copyToClipBoard(getApplicationContext(), tvMnemonics.getText().toString());
                        super.onPositive(dialog);
                    }
                });
        MaterialDialog dialog = builder.build();
        dialog.getContentView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        dialog.show();

    }
}
