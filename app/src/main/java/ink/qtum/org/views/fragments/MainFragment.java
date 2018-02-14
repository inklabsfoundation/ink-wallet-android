package ink.qtum.org.views.fragments;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bitcoinj.core.Coin;

import java.io.IOException;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.QtumApp;
import ink.qtum.org.adapter.TokensAdapter;
import ink.qtum.org.inkqtum.BuildConfig;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.DialogManager;
import ink.qtum.org.managers.SharedManager;
import ink.qtum.org.managers.WalletCreationCallback;
import ink.qtum.org.managers.WalletManager;
import ink.qtum.org.models.RequestCode;
import ink.qtum.org.rest.ApiMethods;
import ink.qtum.org.rest.Requestor;
import ink.qtum.org.utils.CryptoUtils;
import ink.qtum.org.utils.TransactionHistoryConverter;
import ink.qtum.org.views.activities.BackupActivity;
import ink.qtum.org.views.activities.QrCodeScanActivity;
import ink.qtum.org.views.activities.ReceiveActivity;
import ink.qtum.org.views.activities.SendTxActivity;
import ink.qtum.org.views.activities.TxHistoryActivity;
import ink.qtum.org.views.fragments.base.BaseFragment;
import okhttp3.ResponseBody;

import static ink.qtum.org.models.Extras.ACTION_RESTORE_SAVED;
import static ink.qtum.org.models.Extras.COIN_BALANCE_EXTRA;
import static ink.qtum.org.models.Extras.COIN_ID_EXTRA;
import static ink.qtum.org.models.Extras.INK_ID;
import static ink.qtum.org.models.Extras.QTUM_ID;

@AutoInjector(QtumApp.class)
public class MainFragment extends BaseFragment {

    @BindView(R.id.rv_tokens_list)
    RecyclerView mRecycler;
    @BindView(R.id.tv_wallet_address)
    TextView mAddress;

    @BindView(R.id.tv_qtum_balance)
    TextView tvQtumBalance;
    @BindView(R.id.ll_qtum_balance_container)
    LinearLayout llQtumBalanceContainer;

    @BindView(R.id.tv_ink_balance)
    TextView tvInkBalance;
    @BindView(R.id.ll_ink_balance_container)
    LinearLayout llInkBalanceContainer;


    private TokensAdapter adapter;

    @Inject
    WalletManager walletManager;

    @Inject
    SharedManager sharedManager;

    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void init() {
        setHasOptionsMenu(true);
        QtumApp.getAppComponent().inject(this);
        if (getArguments() != null) {
            if (getArguments().getBoolean(ACTION_RESTORE_SAVED)
                    && !android.text.TextUtils.isEmpty(sharedManager.getLastSyncedBlock())) {
                restoreSavedWallet();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_menu_qr_scan:
                Intent intent = new Intent(getContext(), QrCodeScanActivity.class);
                startActivityForResult(intent, RequestCode.QR_CODE_REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restoreSavedWallet() {

        showProgress(getString(R.string.generating_wallet));
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                walletManager.restoreWallet(CryptoUtils.decodeBase64(sharedManager.getLastSyncedBlock()), new WalletCreationCallback() {
                    @Override
                    public void onWalletCreated() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                closeProgress();
                                initViews();
                            }
                        });
                    }
                });
            }
        }).start();

    }

    private void initViews() {
        if (!android.text.TextUtils.isEmpty(walletManager.getWalletFriendlyAddress())) {
            mAddress.setText(walletManager.getWalletFriendlyAddress());
            initQtum();
            initINK();
        }
    }

    private void initQtum() {
        getQtumBalance();
    }

    private void initINK() {
        getInkBalance();
    }

    private void getQtumBalance() {
        Requestor.getBalance(walletManager.getWalletFriendlyAddress(), new ApiMethods.RequestListener() {
            @Override
            public void onSuccess(Object response) {
                try {
                    Long balanceSatoshi = Long.parseLong(((ResponseBody) response).string());
                    final Coin balance = Coin.valueOf(balanceSatoshi);
                    showQtumBalance(balance.toPlainString());

                    llQtumBalanceContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), TxHistoryActivity.class);
                            intent.putExtra(COIN_ID_EXTRA, QTUM_ID);
                            intent.putExtra(COIN_BALANCE_EXTRA, balance.toPlainString());
                            startActivity(intent);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String msg) {
                Log.e(BuildConfig.APPLICATION_ID, msg);
            }
        });
    }

    private void getInkBalance() {
        Requestor.getInkBalance(walletManager.getWalletFriendlyAddress(), new ApiMethods.RequestListener() {
            @Override
            public void onSuccess(Object response) {
                try {
                    String inkBalanceStr = ((ResponseBody) response).string();
                    final String balance = TransactionHistoryConverter.getFriendlyValueInk(inkBalanceStr);
                    showInkBalance(balance);

                    llInkBalanceContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), TxHistoryActivity.class);
                            intent.putExtra(COIN_ID_EXTRA, INK_ID);
                            intent.putExtra(COIN_BALANCE_EXTRA, balance);
                            startActivity(intent);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String msg) {
                Log.e(BuildConfig.APPLICATION_ID, msg);
            }
        });
    }

    private void showQtumBalance(String balance) {
        tvQtumBalance.setText(balance);
    }

    private void showInkBalance(String balance) {
        tvInkBalance.setText(balance);
    }

    @OnClick(R.id.cl_backup)
    public void onBackupClick() {
        if (!TextUtils.isEmpty(sharedManager.getPinCode())) {
            DialogManager.showPinCodeDialog(getContext(), sharedManager.getPinCode(), getString(R.string.input_pin_to_backup),
                    true, new DialogManager.DialogListener() {
                        @Override
                        public void onPositiveButtonClick() {
                            super.onPositiveButtonClick();
                            startActivity(new Intent(getActivity(), BackupActivity.class));
                        }

                    });
        } else {
            startActivity(new Intent(getActivity(), BackupActivity.class));
        }
    }

    @OnClick(R.id.rl_receive)
    public void onReceiveClick() {
        startActivity(new Intent(getActivity(), ReceiveActivity.class));
    }

    @OnClick(R.id.rl_send)
    public void onSendClick() {
        startActivity(new Intent(getActivity(), SendTxActivity.class));
    }
}
