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

import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.QtumApp;
import ink.qtum.org.adapter.TxHistoryAdapter;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.WalletManager;
import ink.qtum.org.models.Extras;
import ink.qtum.org.models.TransactionHistory;
import ink.qtum.org.models.response.TransactionsInkListResponse;
import ink.qtum.org.models.response.TransactionsQtumListResponse;
import ink.qtum.org.rest.ApiMethods;
import ink.qtum.org.rest.Requestor;
import ink.qtum.org.utils.TransactionHistoryConverter;
import ink.qtum.org.views.activities.base.AToolbarActivity;

import static ink.qtum.org.models.Extras.COIN_BALANCE_EXTRA;
import static ink.qtum.org.models.Extras.COIN_ID_EXTRA;
import static ink.qtum.org.models.Extras.INK_ID;
import static ink.qtum.org.models.Extras.QTUM_ID;
import static ink.qtum.org.models.Extras.TX_HISTORY_EXTRA;

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
            if (currentCoinId.equalsIgnoreCase(Extras.QTUM_ID)) {
                initQtumTxList(currentCoinId);
            } else if (currentCoinId.equalsIgnoreCase(Extras.INK_ID)){
                initInkTxList(currentCoinId);
            }

            showIcon(currentCoinId);
        }


        if (!TextUtils.isEmpty(currentCoinBalance)){
            mBalance.setText(currentCoinBalance);
        }
    }

    private void initQtumTxList(final String currentCoinId) {
        mCurrencyName.setText(currentCoinId);
        showProgress(getString(R.string.loading_transactions));
        Requestor.getTransactions(walletManager.getWalletFriendlyAddress(), new ApiMethods.RequestListener() {
            @Override
            public void onSuccess(Object response) {
                closeProgress();
                TransactionsQtumListResponse txList = (TransactionsQtumListResponse)response;
                List<TransactionHistory> transactions = TransactionHistoryConverter.convertQtumToFriendlyList(txList, walletManager.getWalletFriendlyAddress());
                showTxList(transactions);
            }

            @Override
            public void onFailure(String msg) {
                closeProgress();
                Toast.makeText(TxHistoryActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initInkTxList(String currentCoinId) {
        mCurrencyName.setText(currentCoinId);
        showProgress(getString(R.string.loading_transactions));
        Requestor.getInkTransactions(walletManager.getWalletFriendlyAddress(), new ApiMethods.RequestListener() {
            @Override
            public void onSuccess(Object response) {
                closeProgress();
                TransactionsInkListResponse txList = (TransactionsInkListResponse)response;
                List<TransactionHistory> transactions = TransactionHistoryConverter.convertInkToFriendlyList(txList, walletManager.getWalletFriendlyAddress());
                showTxList(transactions);
            }

            @Override
            public void onFailure(String msg) {
                closeProgress();
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
            public void OnItemClick(TransactionHistory item) {
                Intent intent = new Intent(TxHistoryActivity.this, TxDetailsActivity.class);
                intent.putExtra(TX_HISTORY_EXTRA, item);
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

    private void showIcon(String coinId) {
        switch (coinId){
            case QTUM_ID:
                mCurrencyLogo.setImageResource(R.drawable.vec_icon_qtum);
                break;
            case INK_ID:
                mCurrencyLogo.setImageResource(R.drawable.ic_icon_ink);
                break;
            default:
                mCurrencyLogo.setImageResource(R.drawable.ic_action_close);
        }
    }

}
