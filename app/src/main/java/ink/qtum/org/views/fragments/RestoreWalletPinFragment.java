package ink.qtum.org.views.fragments;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.custom.PinCodeLayout;
import ink.qtum.org.views.fragments.base.BaseFragment;


public class RestoreWalletPinFragment extends BaseFragment {

    @BindView(R.id.btn_restore_pin_next)
    Button btnPinNext;

    @BindView(R.id.ib_close)
    ImageView ivClose;
    @BindView(R.id.pin_code_layout)
    PinCodeLayout pinCode;

    private String pinStr;
    private OnPinFragmentInteractionListener mListener;

    public RestoreWalletPinFragment() {
        // Required empty public constructor
    }

    public static RestoreWalletPinFragment newInstance() {
        return new RestoreWalletPinFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_restore_wallet_pin;
    }

    @Override
    protected void init() {
        initViews();
        pinCode.setInputListener(new PinCodeLayout.OnPinCodeListener() {
            @Override
            public void pinCodeCreated(String pin) {
                btnPinNext.setEnabled(true);
                pinStr = pin;
            }
        });
    }

    private void initViews() {
        btnPinNext.setEnabled(false);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPinFragmentInteractionListener) {
            mListener = (OnPinFragmentInteractionListener) context;
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

    @OnClick(R.id.btn_restore_pin_next)
    void onNextClick() {
        if (!android.text.TextUtils.isEmpty(pinStr)) {
            mListener.onPinEntered(pinStr);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        pinCode.clearAll();
    }

    public interface OnPinFragmentInteractionListener {
        void onPinEntered(String pin);
    }
}
