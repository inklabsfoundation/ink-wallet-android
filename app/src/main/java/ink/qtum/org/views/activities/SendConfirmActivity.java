package ink.qtum.org.views.activities;


import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.bitcoinj.core.Coin;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.DialogManager;
import ink.qtum.org.managers.WalletManager;
import ink.qtum.org.models.response.SendTxResponse;
import ink.qtum.org.rest.ApiMethods;
import ink.qtum.org.views.activities.base.AToolbarActivity;

import static ink.qtum.org.models.Extras.AMOUNT_EXTRA;
import static ink.qtum.org.models.Extras.COIN_ID_EXTRA;
import static ink.qtum.org.models.Extras.FEE_EXTRA;
import static ink.qtum.org.models.Extras.WALLET_NUMBER_EXTRA;

@AutoInjector(QtumApp.class)
public class SendConfirmActivity extends AToolbarActivity {

    @BindView(R.id.tv_receiver_address)
    TextView tvReceiverAddress;
    @BindView(R.id.tv_sending_fees_sum)
    TextView tvFeesSum;
    @BindView(R.id.tv_sending_description)
    TextView tvDescription;
    @BindView(R.id.tv_sending_amount)
    TextView tvSendingAmount;

    @Inject
    WalletManager walletManager;

    private String address;
    private long amount;
    private long feePerKb;
    private String txHex;
    private String coinId;
    private int txSizeBytes;


    @Override
    protected void init(Bundle savedInstanceState) {
        QtumApp.getAppComponent().inject(this);
        setToolBarTitle(getString(R.string.toolbar_title_send));
        if (getIntent().getExtras() != null) {
            coinId = getIntent().getExtras().getString(COIN_ID_EXTRA);
            address = getIntent().getExtras().getString(WALLET_NUMBER_EXTRA, "");
            amount = getIntent().getExtras().getLong(AMOUNT_EXTRA);
            feePerKb = getIntent().getExtras().getLong(FEE_EXTRA);
        }

        generateRawTx();


    }

    private void generateRawTx() {
        txHex = walletManager.generateQtumHexTx(address, amount, feePerKb);
        txSizeBytes = txHex.length() / 2;
        Log.d("svcom", "lenght str - " + txSizeBytes);
        updateViews();
    }

    private void updateViews() {
        tvReceiverAddress.setText(address);
        tvSendingAmount.setText(String.format("%s %s", Coin.valueOf(amount).toPlainString(), coinId));
        long txFee = feePerKb / 1024 * txSizeBytes;
        tvFeesSum.setText(String.format("%s %s", Coin.valueOf(txFee).toPlainString(), coinId));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_send_confirm;
    }

    @OnClick(R.id.btn_confirm_sending_tx)
    public void sendTransaction() {

        walletManager.sendTx(txHex, new ApiMethods.RequestListener() {
            @Override
            public void onSuccess(Object response) {
                Log.d("svcom", "onSuccess");
                SendTxResponse txResponse = (SendTxResponse) response;
                Log.d("svcom", txResponse.getTxid());
                DialogManager.showSucceedDialog(SendConfirmActivity.this, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        openMainActivity();
                    }
                });

            }

            @Override
            public void onFailure(String msg) {
                DialogManager.showTransferFailDialog(SendConfirmActivity.this, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        openMainActivity();
                    }
                });
                Log.d("svcom", "onFailure " + msg);
            }
        });

    }
}
