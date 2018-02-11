package ink.qtum.org.views.activities;

import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.DialogManager;
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
        tvMnemonics.setText(walletManager.getMnemonic());
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_backup;
    }

    @OnClick(R.id.btn_copy_mnemonics)
    public void copyMnemonic() {
        DialogManager.showCopyMnemonicsDialog(this, new DialogManager.DialogListener() {
            @Override
            public void onNegativeButtonClick() {
                super.onNegativeButtonClick();
                ClipboardUtils.copyToClipBoard(QtumApp.getAppContext(), tvMnemonics.getText().toString());
            }
        });
    }


}
