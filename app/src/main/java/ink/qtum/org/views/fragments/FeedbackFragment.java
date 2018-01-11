package ink.qtum.org.views.fragments;

import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.activities.MainActivity;
import ink.qtum.org.views.fragments.base.BaseFragment;

public class FeedbackFragment extends BaseFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_feedback;
    }

    @Override
    protected void init() {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setToolbarTitle(getString(R.string.toolbar_title_feedback));
    }
}
