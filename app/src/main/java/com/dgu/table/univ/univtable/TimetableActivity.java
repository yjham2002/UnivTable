package com.dgu.table.univ.univtable;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import crawl.ClassInfo;
import crawl.Crawler;
import crawl.DonggukCrawler;
import crawl.KookminCrawler;
import crawl.SogangCrawler;
import weekview.MonthLoader;
import weekview.WeekView;
import weekview.WeekViewEvent;

public class TimetableActivity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, View.OnClickListener{

    private Crawler Tcrawler;

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private Button _exit;

    private ProgressBar pbar;

    private WeekView mWeekView;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bt_exit:
                finish();
                break;
            default: break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(getApplicationContext(), event.getRawTime(), Toast.LENGTH_LONG).show();
    }
    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
    }
    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> list = getEvents(newYear, newMonth);
        return list;
    }

    public List<WeekViewEvent> getEvents(int newYear, int newMonth){
        List<WeekViewEvent> events = new ArrayList<>();

        for(ClassInfo e : Tcrawler.getClassList()) {
            WeekViewEvent wvEvent = e.toWeekViewEvent(newYear, newMonth);
            wvEvent.setColor(getResources().getColor(R.color.darklime));
            events.add(wvEvent);
        }

        return events;
    }

    @Override
    public void onResume(){
        super.onResume();
        mWeekView.goToHour(9.0);
        Calendar fix = Calendar.getInstance();
        fix.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        mWeekView.goToDate(fix);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        pref = getSharedPreferences("Univtable", MODE_PRIVATE);
        prefEditor = pref.edit();

        pbar = (ProgressBar)findViewById(R.id.pbar);

        _exit = (Button)findViewById(R.id.bt_exit);
        _exit.setOnClickListener(this);

        mWeekView = (WeekView)findViewById(R.id.weekView);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setEventLongPressListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.goToHour(9.0);
        Calendar fix = Calendar.getInstance();
        fix.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        mWeekView.goToDate(fix);

        switch (pref.getInt("ucode", 0)){
            case Crawler.UCODE_DONGGUK: case Crawler.UCODE_DONGGUK_GY: case Crawler.UCODE_DONGGUK_IL:
                Tcrawler = new DonggukCrawler(pref.getString("id", "#"), pref.getString("pw", "#"));
                break;
            case Crawler.UCODE_KOOKMIN:
                Tcrawler = new KookminCrawler(pref.getString("id", "#"), pref.getString("pw", "#"));
                break;
            case Crawler.UCODE_SOGANG:
                Tcrawler = new SogangCrawler(pref.getString("id", "#"), pref.getString("pw", "#"));
                break;
            default:
                Tcrawler = null;
                break;
        }

        mWeekView.setVisibility(View.INVISIBLE);
        mWeekView.setAlpha(0.0f);
        mWeekView.setTranslationY(mWeekView.getHeight() / 3);

        if(pref.getInt("ucode", 0) != 0) Tcrawler.getTimetable(new Handler(){
            @Override
            public void handleMessage(Message msg){
                mWeekView.notifyDatasetChanged();
                mWeekView.setVisibility(View.VISIBLE);
                mWeekView.animate().alpha(1.0f).translationY(0);
                pbar.animate().alpha(0.0f);
                pbar.setVisibility(View.GONE);
            }
        });

    }
}
