package ink.qtum.org.views.fragments.base;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.ButterKnife;
import ink.qtum.org.inkqtum.R;

public abstract class BaseFragment extends Fragment {

    protected View root;
    protected ProgressDialog progressDialog;
    protected boolean isFragmentVisible = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, root);
        isFragmentVisible = true;
        initDefault();
        init();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragmentVisible = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentVisible = false;
    }

    private void initDefault() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.dialog_msg_please_wait));
        progressDialog.setCancelable(false);
    }

    abstract protected
    @LayoutRes
    int getLayout();

    abstract protected void init();

    public void showProgress(String msg) {
        if (!progressDialog.isShowing()) {
            progressDialog.setMessage(msg);
            showProgress();
        }
    }

    public void showProgress() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void changeProgressText(String msg){
        progressDialog.setMessage(msg);
    }

    public void closeProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showError(EditText editText, String error) {

        if (editText.getParent().getParent() instanceof TextInputLayout){
            ((TextInputLayout)editText.getParent().getParent()).setError(error);
            ((TextInputLayout)editText.getParent().getParent()).setErrorEnabled(true);
        }
    }

    public void hideError(EditText editText){
        if (editText.getParent().getParent() instanceof TextInputLayout){
            ((TextInputLayout)editText.getParent().getParent()).setErrorEnabled(false);
        }
    }

    @Override
    public void onDestroy() {
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        super.onDestroy();
    }
}
