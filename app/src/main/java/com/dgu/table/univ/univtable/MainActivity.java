package com.dgu.table.univ.univtable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.tsengvn.typekit.TypekitContextWrapper;

import crawl.Crawler;
import util.Additional_URL;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private TextView _id, _name, _notice;
    private Button _more, _time, _chat, _logout;
    private ImageView _setting, _favicon, _bg;
    private MaterialViewPager mViewPager;
    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.drawer_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.drawer_notice:
                startActivity(new Intent(this, NoticeActivity.class));
                break;
            case R.id.drawer_timetable:
                startActivity(new Intent(this, TimetableActivity.class));
                break;
            case R.id.drawer_chat:
                startActivity(new Intent(this, ChatActivity.class));
                break;
            case R.id.drawer_logout:
                onLogout();
                break;
        }
    }

    public void onLogout(){
        prefEditor.putBoolean("auto", false);
        prefEditor.putString("id", "#");
        prefEditor.putString("pw", "#");
        prefEditor.putString("name", "#");
        prefEditor.commit();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void initView(){
        setTitle("");

        _bg = (ImageView)findViewById(R.id.bg_image);

        _id = (TextView)findViewById(R.id.sID);
        _name = (TextView)findViewById(R.id.sName);
        _notice = (TextView)findViewById(R.id.notice_text);

        _id.setText(pref.getString("id", "Load Failed"));
        _name.setText(pref.getString("name", "Load Failed"));

        _setting = (ImageView)findViewById(R.id.drawer_setting);
        _setting.setOnClickListener(this);
        _more = (Button)findViewById(R.id.drawer_notice);
        _more.setOnClickListener(this);
        _time = (Button)findViewById(R.id.drawer_timetable);
        _time.setOnClickListener(this);
        _chat = (Button)findViewById(R.id.drawer_chat);
        _chat.setOnClickListener(this);
        _logout = (Button)findViewById(R.id.drawer_logout);
        _logout.setOnClickListener(this);

        _favicon = (ImageView)findViewById(R.id.favicon);

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        toolbar = mViewPager.getToolbar();

    }

    public void setUCODE(final Drawable bg, final Drawable icon){
        _bg.setImageDrawable(bg);
        _favicon.setImageDrawable(icon);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("Univtable", MODE_PRIVATE);
        prefEditor = pref.edit();

        initView();
        switch (pref.getInt("ucode", -1)){
            case -1:
                setUCODE(null, getResources().getDrawable(R.drawable.smile));
                break;
            case Crawler.UCODE_DONGGUK: case Crawler.UCODE_DONGGUK_GY: case Crawler.UCODE_DONGGUK_IL:
                setUCODE(getResources().getDrawable(R.drawable.bg_dongguk), getResources().getDrawable(R.drawable.icon_dongguk));
                break;
            case Crawler.UCODE_KOOKMIN:
                setUCODE(getResources().getDrawable(R.drawable.bg_kookmin), getResources().getDrawable(R.drawable.icon_kookmin));
                break;
            case Crawler.UCODE_SOGANG:
                setUCODE(getResources().getDrawable(R.drawable.bg_sogang), getResources().getDrawable(R.drawable.icon_sogang));
                break;
        }

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 4) {
                    case 0:
                        return new fm_1();
                    case 1:
                        return new fm_2();
                    case 2:
                        return new fm_3();
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
                        return "Today";
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
                    case 0: return HeaderDesign.fromColorResAndUrl(R.color.colorPrimaryDark, Additional_URL.ImgUrl_1);
                    case 1: return HeaderDesign.fromColorResAndUrl(R.color.colorPrimaryDark, Additional_URL.ImgUrl_2);
                    case 2: return HeaderDesign.fromColorResAndUrl(R.color.colorPrimaryDark, Additional_URL.ImgUrl_3);
                    case 3: return HeaderDesign.fromColorResAndUrl(R.color.colorPrimaryDark, Additional_URL.ImgUrl_4);
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
                    startActivity(new Intent(MainActivity.this, TimetableActivity.class));
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
    public void onResume(){
        super.onResume();
        mDrawerToggle.syncState();
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

    private boolean mFlag;
    private Handler mHandler = new Handler(){
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