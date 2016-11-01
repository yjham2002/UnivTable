package com.dgu.table.univ.univtable;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import weekview.MonthLoader;
import weekview.WeekView;
import weekview.WeekViewEvent;

public class TimetableActivity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, View.OnClickListener{

    private WeekView mWeekView;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            default: break;
        }
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

        mWeekView = (WeekView)findViewById(R.id.weekView);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setEventLongPressListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.goToHour(9.0);
        Calendar fix = Calendar.getInstance();
        fix.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        mWeekView.goToDate(fix);

    }
}
