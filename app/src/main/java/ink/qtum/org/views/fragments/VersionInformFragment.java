package ink.qtum.org.views.fragments;

import android.widget.TextView;

import butterknife.BindView;
import ink.qtum.org.inkqtum.BuildConfig;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.fragments.base.BaseFragment;

public class VersionInformFragment extends BaseFragment {

    @BindView(R.id.tv_version_number)
    TextView tvVersionNum;

    @Override
    protected int getLayout() {
        return R.layout.fragment_version_inform;
    }

    @Override
    protected void init() {
        setHasOptionsMenu(false);
        tvVersionNum.setText(String.format("v %s", BuildConfig.VERSION_NAME));
    }

}
