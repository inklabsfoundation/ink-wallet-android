package ink.qtum.org.views.activities;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import butterknife.BindView;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.activities.base.AToolbarActivity;

public class TermsOfUsageActivity extends AToolbarActivity {

    @BindView(R.id.tv_term_of_usage_text)
    TextView tvTermOfUsage;

    @Override
    protected void init(Bundle savedInstanceState) {
        Spanned htmlAsSpanned = Html.fromHtml(getString(R.string.terms_of_usage_text));
        tvTermOfUsage.setText(htmlAsSpanned);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_terms_of_usage;
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolBarTitle(getString(R.string.toolbar_title_terms_of_usage));
    }
}
