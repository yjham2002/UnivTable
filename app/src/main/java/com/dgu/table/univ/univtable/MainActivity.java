package com.dgu.table.univ.univtable;

import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import util.Additional_URL;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private Button _test;
    private MaterialViewPager mViewPager;
    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.menu01:
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        _test = (Button)findViewById(R.id.menu01);
        _test.setOnClickListener(this);
        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        toolbar = mViewPager.getToolbar();

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 4) {
                    case 0:
                        return new fm_1();
                    case 1:
                        return new fm_2();
                    case 2:
                        return new fm_2();
                    case 3:
                        return new fm_4();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 4) {
                    case 0:
                        return "시간표";
                    case 1:
                        return "과제목록";
                    case 2:
                        return "출결목록";
                    case 3:
                        return "커뮤니티";
                }
                return "";
            }
        });


        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.oil,
                                Additional_URL.ImgUrl_1);
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green_teal,
                                Additional_URL.ImgUrl_2);
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.purple,
                                Additional_URL.ImgUrl_3);
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                Additional_URL.ImgUrl_4);
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });
        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
        Log.e("Drawer", "onPostCreate Called");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }

    public boolean mFlag;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0){
                mFlag=false;
            }
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            if(!mFlag) {
                Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                mFlag = true;
                mHandler.sendEmptyMessageDelayed(0, 2000);
                return false;
            } else {
                finish();
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}