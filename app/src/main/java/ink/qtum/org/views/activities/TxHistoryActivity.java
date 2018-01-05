package ink.qtum.org.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.QtumApp;
import ink.qtum.org.adapter.TxHistoryAdapter;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.WalletManager;
import ink.qtum.org.models.TransactionHistory;
import ink.qtum.org.models.response.TransactionsListResponse;
import ink.qtum.org.rest.ApiMethods;
import ink.qtum.org.rest.Requestor;
import ink.qtum.org.utils.QtumTransactionHistoryConverter;
import ink.qtum.org.views.activities.base.AToolbarActivity;

import static ink.qtum.org.models.Extras.COIN_BALANCE_EXTRA;
import static ink.qtum.org.models.Extras.COIN_ID_EXTRA;
import static ink.qtum.org.models.Extras.INK_ID;

/**
 * Created by v_alekseev on 28.12.17.
 */
@AutoInjector(QtumApp.class)
public class TxHistoryActivity extends AToolbarActivity {

    @BindView(R.id.rvTxHistory)
    RecyclerView mRecycler;
    @BindView(R.id.iv_currency_logo)
    ImageView mCurrencyLogo;
    @BindView(R.id.tv_balance)
    TextView mBalance;
    @BindView(R.id.tv_currency_name)
    TextView mCurrencyName;

    @Inject
    WalletManager walletManager;

    private TxHistoryAdapter adapter;
    private String currentCoinId;
    private String currentCoinBalance;

    @Override
    protected void init(Bundle savedInstanceState) {
        QtumApp.getAppComponent().inject(this);
        setToolBarTitle(getString(R.string.toolbar_title_details));
        if (getIntent() != null){
            currentCoinId = getIntent().getStringExtra(COIN_ID_EXTRA);
            currentCoinBalance = getIntent().getStringExtra(COIN_BALANCE_EXTRA);
        }
        if (!TextUtils.isEmpty(currentCoinId)){
            initTxList(currentCoinId);
        }


        if (!TextUtils.isEmpty(currentCoinBalance)){
            mBalance.setText(currentCoinBalance);
        }
    }

    private void initTxList(final String currentCoinId) {
        mCurrencyName.setText(currentCoinId);
        showProgress("Loading transactions");
        Requestor.getTransactions(walletManager.getWalletFriendlyAddress(), new ApiMethods.RequestListener() {
            @Override
            public void onSuccess(Object response) {
                closeProgress();
                TransactionsListResponse txList = (TransactionsListResponse)response;
                Log.d("svcom", "tx count = " + txList.getTxs().size());
                List<TransactionHistory> transactions = QtumTransactionHistoryConverter.convertToFriendlyList(txList, walletManager.getWalletFriendlyAddress());
                for (TransactionHistory transaction : transactions) {
                    Log.d("svcom", transaction.toString());
                }
                showTxList(transactions);
            }

            @Override
            public void onFailure(String msg) {
                closeProgress();
                Log.d("svcom", "failure - " + msg);
                Toast.makeText(TxHistoryActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void showTxList(List<TransactionHistory> transactions){
        adapter = new TxHistoryAdapter(transactions);
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
                adapter.clearFilter();
                break;
            case R.id.tvFilterReceive:
                adapter.filterReceived();
                break;
            case R.id.tvFilterSend:
                adapter.filterSent();
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
