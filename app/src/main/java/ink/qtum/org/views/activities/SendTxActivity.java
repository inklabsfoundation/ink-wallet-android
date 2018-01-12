package ink.qtum.org.views.activities;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;

import java.io.IOException;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.BuildConfig;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.WalletManager;
import ink.qtum.org.models.Extras;
import ink.qtum.org.models.RequestCode;
import ink.qtum.org.rest.ApiMethods;
import ink.qtum.org.rest.Requestor;
import ink.qtum.org.views.activities.base.AToolbarActivity;
import okhttp3.ResponseBody;

import static ink.qtum.org.models.Extras.AMOUNT_EXTRA;
import static ink.qtum.org.models.Extras.COIN_ID_EXTRA;
import static ink.qtum.org.models.Extras.FEE_EXTRA;
import static ink.qtum.org.models.Extras.QTUM_ID;
import static ink.qtum.org.models.Extras.WALLET_NUMBER_EXTRA;

@AutoInjector(QtumApp.class)
public class SendTxActivity extends AToolbarActivity {

    @BindView(R.id.et_send_tx_address)
    EditText etAddress;
    @BindView(R.id.et_send_tx_amount)
    EditText etAmount;
    @BindView(R.id.et_tx_description)
    EditText etDescription;
    @BindView(R.id.cb_tx_fees_standard)
    CheckBox cbStandard;
    @BindView(R.id.cb_tx_fees_custom)
    CheckBox cbCustom;
    @BindView(R.id.sb_tx_speed)
    SeekBar sbTxSpeed;
    @BindView(R.id.tv_tx_speed_value)
    TextView tvTxSpeedValue;
    @BindView(R.id.btn_next_step_of_send)
    Button btNext;
    @BindView(R.id.tv_coin_id)
    TextView tvCoinId;

    @Inject
    WalletManager walletManager;

    private Coin currentBalance;
    private long currentAmont;
    private long currentFee;

    private final float maxFeesValue = 1f; // todo set correct value of max fee

    @Override
    protected void init(Bundle savedInstanceState) {
        QtumApp.getAppComponent().inject(this);
        setToolBarTitle(getString(R.string.toolbar_title_send));

        if (getIntent().getExtras() != null) {
            String walletNumber = getIntent().getExtras().getString(WALLET_NUMBER_EXTRA, "");
            etAddress.setText(walletNumber);
            checkAddress(walletNumber);
        }

        setSeekBarListener();
        btNext.setEnabled(false);
        etAmount.setEnabled(false);
        showBalance(QTUM_ID);
        initAddressField();
        initAmountField();
    }

    private void initAddressField() {
        etAddress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                if (clipboard != null && clipboard.hasPrimaryClip()) {

                        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                        String pasteData = item.getText().toString();
                        etAddress.setText(pasteData);
                        checkAddress(pasteData);

                }
                return false;
            }
        });

        etAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    etAddress.setError(null);
                } else {
                    String address = etAddress.getText().toString();
                    if (TextUtils.isEmpty(address)){
                        btNext.setEnabled(false);
                    } else {
                        checkAddress(address);
                    }
                }
            }
        });
    }

    private void checkAddress(String pasteData) {
        if (walletManager.isValidQtumAddress(pasteData)){
            btNext.setEnabled(true);
            etAddress.setError(null);
        } else {
            btNext.setEnabled(false);
            etAddress.setError(getString(R.string.wrong_qtum_address));
        }
    }

    private void initAmountField() {
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etAmount.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {

                    Coin amount = Coin.parseCoin(editable.toString());
                    long amountSatoshi = amount.getValue();
                    Log.d("svcom", "balance long - " + currentBalance.getValue());
                    Log.d("svcom", "amount long - " + amountSatoshi);
                    if (amountSatoshi < currentBalance.longValue()) {
                        currentAmont = amountSatoshi;
                        btNext.setEnabled(true);
                    } else {
                        etAmount.setError(getString(R.string.amout_cant_be_more_balance));
                        btNext.setEnabled(false);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showBalance(final String coinId) {
        tvCoinId.setHint(coinId);
        Requestor.getBalance(walletManager.getWalletFriendlyAddress(), new ApiMethods.RequestListener() {
            @Override
            public void onSuccess(Object response) {
                try {
                    Long balanceSatoshi = Long.parseLong(((ResponseBody) response).string());
                    currentBalance = Coin.valueOf(balanceSatoshi);
                    btNext.setEnabled(true);
                    etAmount.setEnabled(true);
                    String maxAmount = String.format("%s %s", getString(R.string.max_amount), currentBalance.toPlainString());
                    etAmount.setHint(maxAmount);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String msg) {
                Log.e(BuildConfig.APPLICATION_ID, msg);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_menu_qr_scan:
                Intent intent = new Intent(this, QrCodeScanActivity.class);
                startActivityForResult(intent, RequestCode.QR_CODE_REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSeekBarListener() {
        if (cbStandard.isChecked()){
            sbTxSpeed.setEnabled(false);
        } else {
            sbTxSpeed.setEnabled(true);
        }
        sbTxSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBar.getProgress();
                float value = (maxFeesValue * seekBar.getProgress()) / 100;
                currentFee = Coin.parseCoin(Float.toString(value)).getValue();
                tvTxSpeedValue.setText(String.format("%s %s", String.valueOf(value), QTUM_ID));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_send_tx;
    }

    @OnClick({R.id.cb_tx_fees_custom, R.id.cb_tx_fees_standard})
    public void checkFeesType(View v) {
        cbStandard.setChecked(false);
        cbCustom.setChecked(false);

        switch (v.getId()) {
            case R.id.cb_tx_fees_standard:
                cbStandard.setChecked(true);
                sbTxSpeed.setEnabled(false);
                break;
            case R.id.cb_tx_fees_custom:
                cbCustom.setChecked(true);
                sbTxSpeed.setEnabled(true);
                break;
        }
    }

    @OnClick(R.id.btn_next_step_of_send)
    public void goToSendConfirm() {
        Intent intent = new Intent(this, SendConfirmActivity.class);
        intent.putExtra(COIN_ID_EXTRA, QTUM_ID);
        intent.putExtra(WALLET_NUMBER_EXTRA, etAddress.getText().toString());
        intent.putExtra(AMOUNT_EXTRA, currentAmont);
        if (cbStandard.isChecked()) {
            intent.putExtra(FEE_EXTRA, Transaction.REFERENCE_DEFAULT_MIN_TX_FEE.getValue());
        } else {
            intent.putExtra(FEE_EXTRA, currentFee);
        }

        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RequestCode.QR_CODE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(Extras.QR_CODE_RESULT);
                etAddress.setText(result);
                checkAddress(result);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
