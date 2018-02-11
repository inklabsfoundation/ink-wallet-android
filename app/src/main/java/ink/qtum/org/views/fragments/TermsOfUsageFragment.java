package ink.qtum.org.views.fragments;

import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import butterknife.BindView;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.fragments.base.BaseFragment;

public class TermsOfUsageFragment extends BaseFragment {

    @BindView(R.id.tv_term_of_usage_text)
    TextView tvTermOfUsage;

    @Override
    protected int getLayout() {
        return R.layout.fragment_terms_of_usage;
    }

    @Override
    protected void init() {
        setHasOptionsMenu(false);
        Spanned htmlAsSpanned = Html.fromHtml(getString(R.string.terms_of_usage_text));
        tvTermOfUsage.setText(htmlAsSpanned);
    }

}
