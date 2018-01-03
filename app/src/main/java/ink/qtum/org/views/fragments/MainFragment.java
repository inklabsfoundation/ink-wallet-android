package ink.qtum.org.views.fragments;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.bitcoinj.core.Coin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.QtumApp;
import ink.qtum.org.adapter.TokensAdapter;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.WalletManager;
import ink.qtum.org.rest.ApiMethods;
import ink.qtum.org.rest.Requestor;
import ink.qtum.org.views.activities.BackupActivity;
import ink.qtum.org.views.activities.ReceiveActivity;
import ink.qtum.org.views.activities.TxHistoryActivity;
import ink.qtum.org.views.fragments.base.BaseFragment;
import okhttp3.ResponseBody;

@AutoInjector(QtumApp.class)
public class MainFragment extends BaseFragment {

    @BindView(R.id.rv_tokens_list)
    RecyclerView mRecycler;
    @BindView(R.id.tv_wallet_address)
    TextView mAddress;

    @BindView(R.id.tv_qtum_balance)
    TextView tvQtumBalance;

    private TokensAdapter adapter;

    @Inject
    WalletManager walletManager;

    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void init() {
        QtumApp.getAppComponent().inject(this);

        initViews();
    }

    private void initViews() {
        mAddress.setText(walletManager.getWalletFriendlyAddress());
        getBalance();
        initList();
    }

    private void initList() {
        adapter = new TokensAdapter(getDemoList());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        adapter.setItemClickListener(new TokensAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(getActivity(), TxHistoryActivity.class);

                startActivity(intent);
            }
        });

        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(adapter);
    }

    private void getBalance() {
        Requestor.getBalance(walletManager.getWalletFriendlyAddress(), new ApiMethods.RequestListener() {
            @Override
            public void onSuccess(Object response) {
                try {
                    Long balanceSatoshi = Long.parseLong(((ResponseBody) response).string());
                    Coin balance = Coin.valueOf(balanceSatoshi);
                    showQtumBalance(balance.toPlainString());
                    Log.d("svcom", "balance - " + balance.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String msg) {
                Log.d("svcom", "balance - " + msg);
            }
        });
    }

    private void showQtumBalance(String balance) {
        tvQtumBalance.setText(balance);
    }

    private List<Integer> getDemoList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        return list;
    }

    @OnClick(R.id.cl_backup)
    public void onBackupClick() {
        startActivity(new Intent(getActivity(), BackupActivity.class));
    }

    @OnClick(R.id.rl_receive)
    public void onReceiveClick() {
        startActivity(new Intent(getActivity(), ReceiveActivity.class));
    }

    @OnClick(R.id.rl_send)
    public void onSendClick() {
        Toast.makeText(getContext(), "Send", Toast.LENGTH_SHORT).show();
    }
}
