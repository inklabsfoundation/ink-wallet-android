package ink.qtum.org.views.activities;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.DialogManager;
import ink.qtum.org.managers.WalletManager;
import ink.qtum.org.utils.QrCodeUtils;
import ink.qtum.org.views.activities.base.AToolbarActivity;

@AutoInjector(QtumApp.class)
public class ReceiveActivity extends AToolbarActivity{

    @Inject
    WalletManager walletManager;

    @BindView(R.id.qr_image_view)
    ImageView mViewQr;
    @BindView(R.id.tv_wallet_address)
    TextView mWalletAddress;

    public final int QR_CODE_WIDTH = 500;

    @Override
    protected void init(Bundle savedInstanceState) {
        QtumApp.getAppComponent().inject(this);
        setToolBarTitle(getString(R.string.toolbar_title_receive));

        mWalletAddress.setText(walletManager.getWalletFriendlyAddress());
        mViewQr.setImageBitmap(QrCodeUtils.textToQrCode(walletManager.getWalletFriendlyAddress(), QR_CODE_WIDTH));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_receive;
    }

    @OnClick(R.id.btn_copy_address)
    public void copyAddress(){
//        ClipboardUtils.copyToClipBoard(QtumApp.getAppContext(), mWalletAddress.getText().toString());
        DialogManager.showCopySucceedDialog();
    }
}
