package ink.qtum.org.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ink.qtum.org.inkqtum.R;


public class CreatePinFragment extends Fragment {


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_create_pin, container, false);
        ButterKnife.bind(this, fragmentView);
        initViews();
        return fragmentView;
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
