package com.dgu.table.univ.univtable;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import crawl.ClassInfo;

public class WidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private static List<ClassInfo> items = new ArrayList<>();
    private SQLiteDatabase database;
    private String dbName = "UNIVTABLE_DB";
    private String createTable =
            "create table if not exists CLASSES(" +
                    "`id` integer primary key autoincrement, " +
                    "`subject` text, " +
                    "`location` text, " +
                    "`rawtime` text, " +
                    "`wday` integer);";

    private Context context = null;

    private int appWidgetId;

    public WidgetViewFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void createDatabase(){
        database = context.openOrCreateDatabase(dbName, android.content.Context.MODE_PRIVATE, null);
    }

    public void createTable(){
        try{
            database.execSQL(createTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectData(){
        Calendar cal = Calendar.getInstance();
        String sql = "select distinct `subject`, `location`, `rawtime` from CLASSES where `wday` = " + cal.get(Calendar.DAY_OF_WEEK) + " order by `id` asc";
        Cursor result = database.rawQuery(sql, null);
        result.moveToFirst();
        items.clear();
        int a = 0;
        while(!result.isAfterLast()){
            Log.e("SELECT", a++ + " times");
            items.add(new ClassInfo(result.getString(0), result.getString(1), result.getString(2)));
            result.moveToNext();
        }
        result.close();
    }

    @Override
    public void onCreate() {
        createDatabase();
        createTable();
        selectData();
    }

    @Override
    public void onDestroy() {
        // no-op
    }

    @Override
    public int getCount() {
        return( items.size() );
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.widget_row);
        row.setTextViewText(R.id.subject, items.get(position).title);
        row.setTextViewText(R.id.location, items.get(position).location);
        row.setTextViewText(R.id.rawtime, items.get(position).rawtime);
        return(row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return(null);
    }

    @Override
    public int getViewTypeCount() {
        return(1);
    }

    @Override
    public long getItemId(int position) {
        return(position);
    }

    @Override
    public boolean hasStableIds() {
        return(true);
    }

    @Override
    public void onDataSetChanged() {
        createDatabase();
        createTable();
        selectData();
    }
}