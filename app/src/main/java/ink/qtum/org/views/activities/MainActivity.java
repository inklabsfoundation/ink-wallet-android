package ink.qtum.org.views.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.views.activities.base.BaseActivity;
import ink.qtum.org.views.fragments.FeedbackFragment;
import ink.qtum.org.views.fragments.LanguageFragment;
import ink.qtum.org.views.fragments.MainFragment;
import ink.qtum.org.views.fragments.QandAFragment;
import ink.qtum.org.views.fragments.TermsOfUsageFragment;
import ink.qtum.org.views.fragments.VersionInformFragment;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @Override
    protected void init(Bundle savedInstanceState) {
        setupSideMenu();
        initToolbar();
        navigateToFragment(new MainFragment());
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    public void setupSideMenu() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                Fragment fragment = null;
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fl_main_root);
                switch (menuItem.getItemId()) {
                    case R.id.menu_main:
                        fragment = new MainFragment();
                        break;
                    case R.id.menu_lang:
                        fragment = new LanguageFragment();
                        break;
                    case R.id.menu_qa:
                        fragment = new QandAFragment();
                        break;
                    case R.id.menu_feedback:
                        fragment = new FeedbackFragment();
                        break;
                    case R.id.menu_terms_of_usage:
                        fragment = new TermsOfUsageFragment();
                        break;
                    case R.id.menu_version_inform:
                        fragment = new VersionInformFragment();
                        break;
                }
                navigateToFragment(fragment);
                return true;
            }
        });
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_main_root, fragment)
                .commit();
    }

    public void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_vec);
        mToolbar.setTitle(" ");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.toolbar_text_color));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.toolbar_menu_settings:
                Toast.makeText(getApplicationContext(), "Code scan", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
