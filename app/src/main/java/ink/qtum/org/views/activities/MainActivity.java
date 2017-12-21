package ink.qtum.org.views.activities;

import android.os.Bundle;

import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.activities.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayout() {
        return 0;
    }
}
