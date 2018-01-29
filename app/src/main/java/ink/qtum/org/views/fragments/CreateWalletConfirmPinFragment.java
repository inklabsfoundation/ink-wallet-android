package ink.qtum.org.views.fragments;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.custom.PinCodeLayout;
import ink.qtum.org.views.fragments.base.BaseFragment;


public class CreateWalletConfirmPinFragment extends BaseFragment {


    @BindView(R.id.ib_close)
    ImageView ivClose;
    @BindView(R.id.pin_code_layout)
    PinCodeLayout pinCode;
    private static String firstPin;
    private static String secondPin;

    private OnPinConfirmedListener mListener;

    public CreateWalletConfirmPinFragment() {
        // Required empty public constructor
    }

    public static CreateWalletConfirmPinFragment newInstance(String pin) {
        firstPin = pin;
        return new CreateWalletConfirmPinFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_create_wallet_confirm_pin;
    }

    @Override
    protected void init() {
        initViews();
        pinCode.setInputListener(new PinCodeLayout.OnPinCodeListener() {
            @Override
            public void pinCodeCreated(String pin) {
                secondPin = pin;
            }
        });
    }

    private void initViews() {
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
        if (context instanceof OnPinConfirmedListener) {
            mListener = (OnPinConfirmedListener) context;
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

    @OnClick(R.id.btn_next)
    void onNextClick() {
        if (firstPin.equalsIgnoreCase(secondPin)) {
            mListener.onConfirmed(secondPin);
        } else {
            Toast.makeText(getContext(), "Pin code is not match", Toast.LENGTH_LONG).show();
            pinCode.callError();
        }
    }


    public interface OnPinConfirmedListener {
        void onConfirmed(String pin);
    }
}
