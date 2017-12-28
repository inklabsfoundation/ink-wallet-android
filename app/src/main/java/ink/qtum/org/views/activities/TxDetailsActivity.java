package ink.qtum.org.views.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;

import butterknife.BindView;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.activities.base.AToolbarActivity;

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
    TextInputEditText mBlockHash;
    @BindView(R.id.et_time_stamp)
    TextInputEditText mTimeStamp;


    @Override
    protected void init(Bundle savedInstanceState) {
        setToolBarTitle(getString(R.string.toolbar_title_details));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_tx_details;
    }
}
