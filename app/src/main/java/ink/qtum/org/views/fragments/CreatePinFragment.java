package ink.qtum.org.views.fragments;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;

import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.fragments.base.BaseFragment;


public class CreatePinFragment extends BaseFragment {


    @BindView(R.id.btn_next)
    AppCompatButton btnNext;

    private OnPinFragmentInteractionListener mListener;

    public CreatePinFragment() {
        // Required empty public constructor
    }

    public static CreatePinFragment newInstance() {
        return new CreatePinFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_create_pin;
    }

    @Override
    protected void init() {
        initViews();
    }

    private void initViews() {

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

    @OnClick(R.id.btn_next)
    void onNextClick(){
        mListener.onPinEntered("");
    }


    public interface OnPinFragmentInteractionListener {
        void onPinEntered(String pin);
    }
}
