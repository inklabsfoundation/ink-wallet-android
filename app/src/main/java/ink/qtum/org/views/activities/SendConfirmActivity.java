package ink.qtum.org.views.activities;


import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.DialogManager;
import ink.qtum.org.views.activities.base.AToolbarActivity;

public class SendConfirmActivity extends AToolbarActivity {

    @BindView(R.id.tv_receiver_address)
    TextView tvReceiverAddress;
    @BindView(R.id.tv_sending_fees_sum)
    TextView tvFeesSum;
    @BindView(R.id.tv_sending_description)
    TextView tvDescription;
    @BindView(R.id.tv_sending_amount)
    TextView tvSendingAmount;

    @Override
    protected void init(Bundle savedInstanceState) {
        setToolBarTitle(getString(R.string.toolbar_title_send));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_send_confirm;
    }

    @OnClick(R.id.btn_confirm_sending_tx)
    public void sendTransaction() {
        Random rand = new Random();
        if (rand.nextBoolean()) {
            DialogManager.showSucceedDialog(this);
        } else {
            DialogManager.showTransferFailDialog(this);
        }

    }
}
