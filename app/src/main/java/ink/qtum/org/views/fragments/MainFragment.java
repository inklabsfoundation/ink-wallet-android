package ink.qtum.org.views.fragments;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

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
import ink.qtum.org.views.activities.BackupActivity;
import ink.qtum.org.views.fragments.base.BaseFragment;

@AutoInjector(QtumApp.class)
public class MainFragment extends BaseFragment {

    @BindView(R.id.rv_tokens_list)
    RecyclerView mRecycler;
    @BindView(R.id.tv_wallet_address)
    TextView mAddress;

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

        adapter = new TokensAdapter(getDemoList());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(adapter);
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
        Toast.makeText(getContext(), "Receive", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.rl_send)
    public void onSendClick() {
        Toast.makeText(getContext(), "Send", Toast.LENGTH_SHORT).show();
    }
}
