package ink.qtum.org.views.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.models.TransactionHistory;
import ink.qtum.org.views.activities.base.AToolbarActivity;

import static ink.qtum.org.models.Extras.INK_ID;
import static ink.qtum.org.models.Extras.QTUM_ID;
import static ink.qtum.org.models.Extras.TX_HISTORY_EXTRA;

/**
 * Created by v_alekseev on 28.12.17.
 */

public class TxDetailsActivity extends AToolbarActivity {

    @BindView(R.id.et_from_address)
    TextInputEditText mAddressFrom;
    @BindView(R.id.et_to_address)
    TextInputEditText mAddressTo;
    @BindView(R.id.et_fees)
    TextInputEditText mFees;
    @BindView(R.id.et_description)
    TextInputEditText mDescription;
    @BindView(R.id.et_tx_hash)
    TextInputEditText mTxHash;
    @BindView(R.id.et_block_height)
    TextInputEditText mBlockHeight;
    @BindView(R.id.et_time_stamp)
    TextInputEditText mTimeStamp;
    @BindView(R.id.iv_currency_logo)
    AppCompatImageView ivCoinLogo;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_currency_name)
    TextView mCoinName;

    private TransactionHistory txHistoryItem;


    @Override
    protected void init(Bundle savedInstanceState) {
        setToolBarTitle(getString(R.string.toolbar_title_details));
        txHistoryItem = (TransactionHistory)getIntent().getSerializableExtra(TX_HISTORY_EXTRA);
        if (txHistoryItem != null) {
            showTxInfo();
        }
    }

    private void showTxInfo() {
        tvBalance.setText(txHistoryItem.getFriendlyValue());
        mCoinName.setText(txHistoryItem.getCoinId());
        showIcon(txHistoryItem.getCoinId());
        mAddressFrom.setText(txHistoryItem.getFromAddress());
        mAddressTo.setText(txHistoryItem.getToAddress());
        mFees.setText(txHistoryItem.getFees().toPlainString());
        if (TextUtils.equals(txHistoryItem.getCoinId(), QTUM_ID) || TextUtils.equals(txHistoryItem.getCoinId(), INK_ID)){
            mDescription.setVisibility(View.INVISIBLE);
        }
        mTxHash.setText(txHistoryItem.getTxHash());
        mBlockHeight.setText(String.format("%s", txHistoryItem.getBlockHeight()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z", Locale.getDefault());
        mTimeStamp.setText(dateFormat.format(new Date(txHistoryItem.getTimestamp())));
    }

    private void showIcon(String coinId) {
        switch (coinId){
            case QTUM_ID:
                ivCoinLogo.setImageResource(R.drawable.vec_icon_qtum);
                break;
            case INK_ID:
                ivCoinLogo.setImageResource(R.drawable.ic_icon_ink);
                break;
            default:
                ivCoinLogo.setImageResource(R.drawable.ic_action_close);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_tx_details;
    }
}
