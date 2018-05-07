package com.github.airsaid.androidwidget;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.airsaid.androidwidget.data.Item;


/**
 * Android 自定义控件集.
 *
 * @author airsaid
 */
public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnItemClickCallback,
        FragmentManager.OnBackStackChangedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setSubtitle(R.string.app_desc);
        setSupportActionBar(mToolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_view);

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        Log.d(TAG, "savedInstanceState: " + savedInstanceState);
        if(savedInstanceState == null){
            setMainFragment(-1);
        }
    }

    @Override
    public void onBackStackChanged() {
        int backStackEntryCount = getSupportFragmentManager()
                .getBackStackEntryCount();
        if(backStackEntryCount == 0){
            setTitle(getString(R.string.app_name), getString(R.string.app_desc));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            startActivity(new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse(
                            ((TextView) findViewById(R.id.textView)).getText().toString())));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        setMainFragment(item.getOrder());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setMainFragment(int type){
        // 如果 fragment 回退栈中有 fragment, 则退出, 避免返回时错乱
        FragmentManager fm = getSupportFragmentManager();
        while (fm.getBackStackEntryCount() > 0){
            fm.popBackStackImmediate();
        }

        Log.d(TAG, "MainFragment: " + mMainFragment);
        if(mMainFragment == null){
            mMainFragment = MainFragment.newInstance();
            mMainFragment.setOnItemClickCallback(this);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mMainFragment, MainFragment.class.getSimpleName())
                .commit();
        if(type != -1){
            mMainFragment.setType(type);
        }
    }

    @Override
    public void onItemClickListener(Item item) {
        switchPage(item);
    }

    /**
     * 切换到对应的页面.
     * @param menu 菜单条目.
     */
    private void switchPage(Item menu){
        try {
            Object obj = menu.getCls().newInstance();
            if(obj instanceof Fragment){
                // 设置对应标题
                setTitle(menu.getTitle(), menu.getDesc());
                // 切换页面
                Fragment fragment = (Fragment) obj;
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, fragment, menu.getTitle())
                        .addToBackStack(menu.getTitle())
                        .commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTitle(String title, String desc){
        mToolbar.setTitle(title);
        mToolbar.setSubtitle(desc);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
