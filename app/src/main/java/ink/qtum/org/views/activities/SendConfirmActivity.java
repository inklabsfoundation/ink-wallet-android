package ink.qtum.org.views.activities;


import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.bitcoinj.core.Coin;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.DialogManager;
import ink.qtum.org.managers.SharedManager;
import ink.qtum.org.managers.WalletManager;
import ink.qtum.org.models.contract.UnspentOutput;
import ink.qtum.org.models.response.SendTxResponse;
import ink.qtum.org.rest.ApiMethods;
import ink.qtum.org.rest.Requestor;
import ink.qtum.org.views.activities.base.AToolbarActivity;

import static ink.qtum.org.models.Constants.INK_CONTRACT_ADDRESS_HEX;
import static ink.qtum.org.models.Extras.AMOUNT_EXTRA;
import static ink.qtum.org.models.Extras.COIN_ID_EXTRA;
import static ink.qtum.org.models.Extras.FEE_EXTRA;
import static ink.qtum.org.models.Extras.INK_ID;
import static ink.qtum.org.models.Extras.QTUM_ID;
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
    @Inject
    SharedManager sharedManager;

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
        switch (coinId) {
            case QTUM_ID:
                generateQtumRawTx();
                break;
            case INK_ID:
                generateInkRawTx();
                break;
        }
    }

    private void generateQtumRawTx() {
        try {
            txHex = walletManager.generateQtumHexTx(address, amount, feePerKb);
            txSizeBytes = txHex.length() / 2;
            updateViews();
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void generateInkRawTx() {
        final String abiParams = walletManager.createAbiMethodParams(address, Long.toString(amount));
        final double feeBerKb = Double.parseDouble(Coin.valueOf(feePerKb).toPlainString());
        final String fee = walletManager.getValidatedFee(feeBerKb);
        Requestor.getUTXOListForToken(walletManager.getWalletFriendlyAddress(), new ApiMethods.RequestListener() {
            @Override
            public void onSuccess(Object response) {
                List<UnspentOutput> unspentOutputs = (List<UnspentOutput>) response;

                if (unspentOutputs != null && !unspentOutputs.isEmpty()) {
                    txHex = walletManager.createTokenHexTx(abiParams, INK_CONTRACT_ADDRESS_HEX, fee, BigDecimal.valueOf(feeBerKb), unspentOutputs);
                    txSizeBytes = txHex.length() / 2;
                    updateViews();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    private void updateViews() {
        tvReceiverAddress.setText(address);
        tvSendingAmount.setText(String.format("%s %s", Coin.valueOf(amount).toPlainString(), coinId));
        long txFee = feePerKb / 1024 * txSizeBytes;
        tvFeesSum.setText(String.format("%s %s", Coin.valueOf(txFee).toPlainString(), QTUM_ID));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_send_confirm;
    }

    @OnClick(R.id.btn_confirm_sending_tx)
    public void sendTxBtnClick() {
        DialogManager.showPinCodeDialog(this, sharedManager.getPinCode(), getString(R.string.confirm_your_pin),
                false, new DialogManager.DialogListener() {
                    @Override
                    public void onPositiveButtonClick() {
                        super.onPositiveButtonClick();
                        sendTransaction();
                    }
                });

    }

    private void sendTransaction(){
        walletManager.sendTx(txHex, new ApiMethods.RequestListener() {
            @Override
            public void onSuccess(Object response) {
                SendTxResponse txResponse = (SendTxResponse) response;
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
