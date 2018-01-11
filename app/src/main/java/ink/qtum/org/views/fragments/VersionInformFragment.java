package ink.qtum.org.views.fragments;

import android.widget.TextView;

import butterknife.BindView;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.activities.MainActivity;
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
        tvVersionNum.setText(String.format("V%s", "0.1.0"));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setToolbarTitle(getString(R.string.toolbar_title_version_inform));
    }
}
