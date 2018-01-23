package ink.qtum.org.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import butterknife.OnClick;
import ink.qtum.org.QtumApp;
import ink.qtum.org.inkqtum.R;
import ink.qtum.org.managers.DialogManager;
import ink.qtum.org.managers.SharedManager;
import ink.qtum.org.models.Extras;
import ink.qtum.org.models.RequestCode;
import ink.qtum.org.views.activities.base.BaseActivity;
import ink.qtum.org.views.fragments.FeedbackFragment;
import ink.qtum.org.views.fragments.LanguageFragment;
import ink.qtum.org.views.fragments.MainFragment;
import ink.qtum.org.views.fragments.QandAFragment;
import ink.qtum.org.views.fragments.TermsOfUsageFragment;
import ink.qtum.org.views.fragments.VersionInformFragment;

import static ink.qtum.org.models.Extras.ACTION_RESTORE_SAVED;

@AutoInjector(QtumApp.class)
public class MainActivity extends BaseActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btn_log_out)
    Button btnLogOut;

    @Inject
    SharedManager sharedManager;

    @Override
    protected void init(Bundle savedInstanceState) {
        QtumApp.getAppComponent().inject(this);
        setupSideMenu();
        initToolbar();
        String action = getIntent().getAction();
        if (action != null && action.equals(ACTION_RESTORE_SAVED)) {
            MainFragment mainFragment = new MainFragment();
            Bundle args = new Bundle();
            args.putBoolean(ACTION_RESTORE_SAVED, true);
            mainFragment.setArguments(args);
            navigateToFragment(mainFragment);

        } else {
            navigateToFragment(new MainFragment());
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(sharedManager.getPinCode())) {
            logOut();
        }

        if (!QtumApp.isIsAccessAllowed()) {
            DialogManager.showPinCodeDialog(this, sharedManager.getPinCode(), getString(R.string.input_pin_to_unlock),
                    false, new DialogManager.DialogListener() {
                        @Override
                        public void onPositiveButtonClick() {
                            super.onPositiveButtonClick();
                            QtumApp.setIsAccessAllowed(true);
                        }

                        @Override
                        public void onNegativeButtonClick() {
                            super.onNegativeButtonClick();
                            finishAffinity();
                        }
                    });
        }
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

    public void setToolbarTitle(String text) {
        mToolbar.setTitle(text);
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
                mDrawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.toolbar_menu_qr_scan:
                Intent intent = new Intent(this, QrCodeScanActivity.class);
                startActivityForResult(intent, RequestCode.QR_CODE_REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_log_out)
    public void logOutBtnClick() {
        DialogManager.showLogOutDialog(this, new DialogManager.DialogListener() {
            @Override
            public void onPositiveButtonClick() {
                super.onPositiveButtonClick();
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(MainActivity.this, BackupActivity.class));
            }

            @Override
            public void onNegativeButtonClick() {
                super.onNegativeButtonClick();
                logOut();
            }
        });
    }

    public void logOut() {
        sharedManager.clearLastSyncedBlock();
        sharedManager.clearPinCode();
        openLoginActivity();
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RequestCode.QR_CODE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra(Extras.QR_CODE_RESULT);
                Intent intent = new Intent(this, SendTxActivity.class);
                intent.putExtra(Extras.WALLET_NUMBER_EXTRA, result);
                startActivity(intent);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
