package ink.qtum.org.views.activities;

import android.os.Bundle;

import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.activities.base.AToolbarActivity;

/**
 * Created by v_alekseev on 28.12.17.
 */

public class TxDetailsActivity extends AToolbarActivity {

    @Override
    protected void init(Bundle savedInstanceState) {
        setToolBarTitle(getString(R.string.toolbar_title_details));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_tx_details;
    }
}
