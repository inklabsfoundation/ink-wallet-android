package ink.qtum.org.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.adapter.TxHistoryAdapter;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.activities.base.AToolbarActivity;

/**
 * Created by v_alekseev on 28.12.17.
 */

public class TxHistoryActivity extends AToolbarActivity {

    @BindView(R.id.rvTxHistory)
    RecyclerView mRecycler;
    @BindView(R.id.iv_currency_logo)
    ImageView mCurrencyLogo;
    @BindView(R.id.tv_balance)
    TextView mBalance;
    @BindView(R.id.tv_currency_name)
    TextView mCurrencyName;

    private TxHistoryAdapter adapter;

    @Override
    protected void init(Bundle savedInstanceState) {
        setToolBarTitle(getString(R.string.toolbar_title_details));
        initTxList();
    }

    private void initTxList() {
        adapter = new TxHistoryAdapter(getDemoList());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        adapter.setItemClickListener(new TxHistoryAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(TxHistoryActivity.this, TxDetailsActivity.class);
                startActivity(intent);
            }
        });

        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(adapter);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_tx_history;
    }

    @OnClick({R.id.tvFilterAll, R.id.tvFilterReceive, R.id.tvFilterSend})
    public void onFilterClick(View view){
        switch (view.getId()){
            case R.id.tvFilterAll:

                break;
            case R.id.tvFilterReceive:

                break;
            case R.id.tvFilterSend:

                break;
        }
    }

    private List<Integer> getDemoList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        return list;
    }

}
