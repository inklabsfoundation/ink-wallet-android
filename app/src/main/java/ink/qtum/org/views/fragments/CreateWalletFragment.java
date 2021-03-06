package ink.qtum.org.views.fragments;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.bitcoinj.wallet.Wallet;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.DialogManager;
import ink.qtum.org.managers.WalletCreationCallback;
import ink.qtum.org.managers.WalletManager;
import ink.qtum.org.utils.ClipboardUtils;
import ink.qtum.org.views.fragments.base.BaseFragment;


@AutoInjector(QtumApp.class)
public class CreateWalletFragment extends BaseFragment {

    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_mnemonics)
    TextView tvMnemonics;
    @BindView(R.id.ib_close)
    ImageView ivClose;


    @Inject
    WalletManager walletManager;

    private OnWalletFragmentInteractionListener mListener;

    public CreateWalletFragment() {
        // Required empty public constructor
    }

    public static CreateWalletFragment newInstance() {
        return new CreateWalletFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_create_wallet;
    }

    @Override
    protected void init() {
        QtumApp.getAppComponent().inject(this);
        initViews();
        generateWallet();
    }

    private void initViews() {
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    private void generateWallet() {
        showProgress(getString(R.string.generating_wallet));
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                walletManager.createWallet(new WalletCreationCallback() {
                    @Override
                    public void onWalletCreated() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvAddress.setText(walletManager.getWalletFriendlyAddress());
                                tvMnemonics.setText(walletManager.getMnemonic());
                                closeProgress();
                            }
                        });
                    }
                });
            }
        }).start();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWalletFragmentInteractionListener) {
            mListener = (OnWalletFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSeedFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.btn_copy_mnemonics)
    void onNextClick() {

    }


    public interface OnWalletFragmentInteractionListener {
        void onFinishCreateWallet();
    }

    @OnClick(R.id.btn_copy_mnemonics)
    public void copyMnemonics() {
        DialogManager.showCopyMnemonicsDialog(getContext(), new DialogManager.DialogListener() {
            @Override
            public void onNegativeButtonClick() {
                super.onNegativeButtonClick();
                ClipboardUtils.copyToClipBoard(QtumApp.getAppContext(), tvMnemonics.getText().toString());
            }

        });
    }

    @OnClick(R.id.tv_start_wallet)
    public void goToMainActivity() {
        mListener.onFinishCreateWallet();
    }
}
