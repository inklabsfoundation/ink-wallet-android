package ink.qtum.org.views.fragments;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.fragments.base.BaseFragment;


public class InputMnemonicFragment extends BaseFragment {

    @BindView(R.id.et_seed)
    EditText etMnemonics;

    @BindView(R.id.btn_next)
    AppCompatButton btnNext;

    @BindView(R.id.ib_close)
    ImageView ivClose;

    private OnMnemonicFragmentInteractionListener mListener;

    public InputMnemonicFragment() {
        // Required empty public constructor
    }

    public static InputMnemonicFragment newInstance() {
        return new InputMnemonicFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_input_mnemonic;
    }

    @Override
    protected void init() {
        initViews();
    }

    private void initViews() {
        btnNext.setEnabled(false);
        etMnemonics.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(etMnemonics.getText().toString())){
                    btnNext.setEnabled(true);
                } else {
                    btnNext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        if (context instanceof OnMnemonicFragmentInteractionListener) {
            mListener = (OnMnemonicFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMnemonicFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.btn_next)
    void onNextClick(){
        String mnemonic = etMnemonics.getText().toString();
        if (!TextUtils.isEmpty(mnemonic)){
            mListener.onMnemonicsEntered(mnemonic);
        }
    }


    public interface OnMnemonicFragmentInteractionListener {
        void onMnemonicsEntered(String seed);
    }
}
