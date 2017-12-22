package ink.qtum.org.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ink.qtum.org.inkqtum.R;


public class CreateSeedFragment extends Fragment {

    @BindView(R.id.et_seed)
    EditText etSeed;

    @BindView(R.id.btn_next)
    AppCompatButton btnNext;

    private OnSeedFragmentInteractionListener mListener;

    public CreateSeedFragment() {
        // Required empty public constructor
    }

    public static CreateSeedFragment newInstance() {
        return new CreateSeedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_create_seed, container, false);
        ButterKnife.bind(this, fragmentView);
        initViews();
        return fragmentView;
    }

    private void initViews() {
        btnNext.setEnabled(false);
        etSeed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(etSeed.getText().toString())){
                    btnNext.setEnabled(true);
                } else {
                    btnNext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSeedFragmentInteractionListener) {
            mListener = (OnSeedFragmentInteractionListener) context;
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
        String seed = etSeed.getText().toString();
        if (!TextUtils.isEmpty(seed)){
            mListener.onSeedEntered(seed);
        }
    }


    public interface OnSeedFragmentInteractionListener {
        void onSeedEntered(String seed);
    }
}
