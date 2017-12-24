package ink.qtum.org.views.activities.base;


import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ink.qtum.org.inkqtum.R;


public abstract class AToolbarActivity extends BaseActivity {

    protected TextView title;
    protected Toolbar toolBar;


    @Override
    protected void createLayout() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.activity_base, null, false);
        View contentView = inflater.inflate(getLayout(), mainView, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW, R.id.toolbar_main);
        contentView.setLayoutParams(params);
        mainView.addView(contentView, 0);

        setContentView(mainView);

    }

    public void enableBackButton(){
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null){
            toolbar.setDisplayHomeAsUpEnabled(true);
            toolbar.setHomeButtonEnabled(true);
        }

    }

    @Override
    protected void initToolbar() {
        toolBar = (Toolbar) findViewById(R.id.toolbar_main);
        if (toolBar != null) {
//            title = (TextView) findViewById(R.id.tv_toolbar_title);

            setSupportActionBar(toolBar);

            initBackButton();

        }
    }

    private void initBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_vec);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setToolBarTitle(int titleId) {
        toolBar.setTitle(titleId);
        toolBar.setTitleTextColor(getResources().getColor(R.color.toolbar_text_color));
//        title.setText(resId);
    }

//    protected void setToolBarTitle(CharSequence title) {
//        if (this.title != null) {
//            this.title.setText(title);
//        } else {
//            setTitle(getTitle().toString() + " " + title);
//        }
//    }

}
