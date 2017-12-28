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


public class CreateSeedFragment extends BaseFragment {

    @BindView(R.id.et_seed)
    EditText etSeed;

    @BindView(R.id.btn_next)
    AppCompatButton btnNext;

    @BindView(R.id.ib_close)
    ImageView ivClose;

    private OnSeedFragmentInteractionListener mListener;

    public CreateSeedFragment() {
        // Required empty public constructor
    }

    public static CreateSeedFragment newInstance() {
        return new CreateSeedFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_create_seed;
    }

    @Override
    protected void init() {
        initViews();
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
