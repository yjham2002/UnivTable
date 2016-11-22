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
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import crawl.ClassInfo;
import crawl.Crawler;
import crawl.DonggukCrawler;
import weekview.MonthLoader;
import weekview.WeekView;
import weekview.WeekViewEvent;

public class TimetableActivity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, View.OnClickListener{

    private Crawler Tcrawler;

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private WeekView mWeekView;

    @Override
    public void onClick(View v){
        switch(v.getId()){
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
            events.add(e.toWeekViewEvent(newYear, newMonth));
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

        mWeekView = (WeekView)findViewById(R.id.weekView);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setEventLongPressListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.goToHour(9.0);
        Calendar fix = Calendar.getInstance();
        fix.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        mWeekView.goToDate(fix);

        Tcrawler = new DonggukCrawler(pref.getString("id", "#"), pref.getString("pw", "#"));
        Tcrawler.getTimetable(new Handler(){
            @Override
            public void handleMessage(Message msg){
                mWeekView.notifyDatasetChanged();
            }
        });

    }
}
