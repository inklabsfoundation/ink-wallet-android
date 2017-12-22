package ink.qtum.org.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ink.qtum.org.inkqtum.R;


public class CreateWalletFragment extends Fragment {


    @BindView(R.id.btn_copy_mnemonics)
    AppCompatButton btnNext;

    private OnWalletFragmentInteractionListener mListener;

    public CreateWalletFragment() {
        // Required empty public constructor
    }

    public static CreateWalletFragment newInstance() {
        return new CreateWalletFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_create_wallet, container, false);
        ButterKnife.bind(this, fragmentView);
        initViews();
        return fragmentView;
    }

    private void initViews() {

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
    void onNextClick(){

    }


    public interface OnWalletFragmentInteractionListener {
        void onFinishCreateWallet(String seed);
    }
}
