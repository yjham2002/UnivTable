package com.dgu.table.univ.univtable;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import crawl.Crawler;
import util.GPSTracker;
import util.LatLng;
import util.MapUtils;

public class AlarmReceiver extends BroadcastReceiver{

    private SharedPreferences pref;
    private SharedPreferences prefSet;

    public static final double THR = 500.0;
    private GPSTracker mGPS;
    private SQLiteDatabase database;

    private String dbName = "UNIVTABLE_DB";
    private String createTable =
            "create table if not exists CLASSES(" +
                    "`id` integer primary key autoincrement, " +
                    "`subject` text, " +
                    "`location` text, " +
                    "`rawtime` text, " +
                    "`wday` integer);";
    private String attendTable =
            "create table if not exists ATTEND(" +
                    "`id` integer primary key autoincrement, " +
                    "`subject` text, " +
                    "`date` text);";

    private Context context = null;

    private SimpleDateFormat inputParser = new SimpleDateFormat("HH:mm");

    private boolean compareDates(String compare1, String compare2){
        Date date, dateCompareOne, dateCompareTwo;
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        date = parseDate(hour + ":" + minute);
        dateCompareOne = parseDate(compare1);
        dateCompareTwo = parseDate(compare2);
        if ( dateCompareOne.before(date) && dateCompareTwo.after(date)) {
            return true;
        }else return false;
    }

    private Date parseDate(String date) {
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

    public void createDatabase(){
        database = context.openOrCreateDatabase(dbName, android.content.Context.MODE_PRIVATE, null);
    }

    public void createTable(){
        try{
            database.execSQL(createTable);
            database.execSQL(attendTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String currentClass = "", currentTime = "";

    public boolean isClasstime(){
        Calendar cal = Calendar.getInstance();
        String sql = "select distinct `subject`, `location`, `rawtime` from CLASSES where `wday` = " + cal.get(Calendar.DAY_OF_WEEK) + " order by `id` asc";
        Cursor result = database.rawQuery(sql, null);
        result.moveToFirst();
        while(!result.isAfterLast()){
            if(compareDates(result.getString(2).substring(0, 2).trim() + ":" + result.getString(2).substring(4, 6).trim(), result.getString(2).substring(10, 12).trim() + ":" + result.getString(2).substring(14, 16).trim())){
                Log.e("It is classtime", "##########################");
                currentClass = result.getString(0);
                currentTime = cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH);
                result.close();
                return true;
            }
            result.moveToNext();
        }
        result.close();
        return false;
    }

    public boolean isUnique(String subject, String date){
        Log.e("DGU_ALARM", "IS_UNIQUE");
        SQLiteStatement s = database.compileStatement( "select count(*) from ATTEND where `subject`='" + subject + "' and `date`='" + date + "'; " );
        long count = s.simpleQueryForLong();
        if(count <= 0) return true;
        else return false;
    }

    public void insertAbsent(){
        Log.e("DGU_ALARM", "INSERT_ABSENT");
        if(!isUnique(currentClass, currentTime)) {
            Log.e("Unique Fail", currentClass + "/" + currentTime);
            return;
        }
        notification("유니테이블 출결 알림", currentClass + " 강의에 결석 혹은 지각했나요?", context);
        Log.e("Unique Success", currentClass + "/" + currentTime);
        database.beginTransaction();
        try{
            String sql = "insert into ATTEND(`subject`, `date`) values ('" + currentClass + "', '" + currentTime + "');";
            database.execSQL(sql);
            database.setTransactionSuccessful();
        }catch(Exception e){
            Log.e("ATTEND-Insertion Failed", "###############################################");
        }finally{
            database.endTransaction();
        }
    }

    public void checkAttendance(Context context){
        Log.e("DGU_ALARM", "CHECK_ATTENDANCE");
        pref = context.getSharedPreferences("Univtable", context.MODE_PRIVATE);
        createDatabase();
        createTable();
        if(Looper.myLooper()==null) Looper.prepare();
        GPSTracker loc = new GPSTracker(context);
        LatLng latLng = null;
        switch (pref.getInt("ucode", -1)){
            case Crawler.UCODE_DONGGUK:
                latLng = Crawler.GEO_DONGGUK_SEOUL;
                break;
            case Crawler.UCODE_DONGGUK_GY:
                latLng = Crawler.GEO_DONGGUK_GYEONGJU;
                break;
            case Crawler.UCODE_DONGGUK_IL:
                latLng = Crawler.GEO_DONGGUK_ILSAN;
                break;
            case Crawler.UCODE_KOOKMIN:
                latLng = Crawler.GEO_DONGGUK_KOOKMIN;
                break;
            case Crawler.UCODE_SOGANG:
                latLng = Crawler.GEO_DONGGUK_SOGANG;
                break;
            default:
                latLng = Crawler.GEO_DONGGUK_SEOUL;
                break;
        }
        Log.e("DGU_ALARM", MapUtils.distance(latLng, loc.getLatitude(), loc.getLongitude()) + " Meters Long");
        if(MapUtils.distance(latLng, loc.getLatitude(), loc.getLongitude()) > THR && isClasstime()){
            insertAbsent();
        }
    }

    public void notification(String title, String msg, Context context){
        Log.e("DGU_ALARM", "NOTIFICATION");
        prefSet = PreferenceManager.getDefaultSharedPreferences(context);
        if(!prefSet.getBoolean("keyword_push_univ", true)) return;
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, IntroActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher).setTicker("HETT").setWhen(System.currentTimeMillis())
                .setNumber(1).setContentTitle(title).setContentText(msg)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);
        notificationmanager.notify(1, builder.build());
    }

    public boolean isPermissionGranted(Context context){
        Log.e("DGU_ALARM", "PERMISSION CHECK");
        return !( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("DGU_ALARM", "SAVED");
        this.context = context;
        if(isPermissionGranted(context)) checkAttendance(context);
    }
}
