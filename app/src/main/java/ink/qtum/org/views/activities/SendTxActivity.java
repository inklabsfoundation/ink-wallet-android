package ink.qtum.org.views.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.activities.base.AToolbarActivity;

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

    private final float maxFeesValue = 2f; // todo set correct value of max fee

    @Override
    protected void init(Bundle savedInstanceState) {
        setToolBarTitle(getString(R.string.toolbar_title_send));

        setSeekBarListener();
    }

    private void setSeekBarListener() {

        sbTxSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBar.getProgress();
                float value = (maxFeesValue * seekBar.getProgress()) / 100;
                tvTxSpeedValue.setText(String.format("%s INK", String.valueOf(value)));
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
                break;
            case R.id.cb_tx_fees_custom:
                cbCustom.setChecked(true);
                break;
        }
    }

    @OnClick(R.id.btn_next_step_of_send)
    public void goToSendConfirm() {
        startActivity(new Intent(this, SendConfirmActivity.class));
    }
}
