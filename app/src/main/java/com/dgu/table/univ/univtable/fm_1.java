package com.dgu.table.univ.univtable;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import crawl.ClassInfo;
import crawl.Crawler;
import crawl.DonggukCrawler;
import crawl.KookminCrawler;
import crawl.SogangCrawler;
import util.WeatherParser;
import weekview.WeekView;
import weekview.WeekViewEvent;

public class fm_1 extends Fragment implements View.OnClickListener{

    private static final int DUMMY_COUNT = 1;

    private boolean isAlreadyDid = false;
    private WeekView mWeekView;
    private SQLiteDatabase database;
    private Handler h = new Handler();

    private String dropTable =
            "drop table if exists CLASSES;";

    private String dbName = "UNIVTABLE_DB";
    private String createTable =
            "create table if not exists CLASSES(" +
                    "`id` integer primary key autoincrement, " +
                    "`subject` text, " +
                    "`location` text, " +
                    "`rawtime` text, " +
                    "`wday` integer);";


    private Crawler Tcrawler;

    private ProgressBar pbar;

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private RecyclerView mRecyclerView;
    private RecyclerViewMaterialAdapter recyclerViewMaterialAdapter;
    private ListViewAdapter testAdapter;

    public boolean isUnique(String subject, String location, String rawtime, int wday){
        SQLiteStatement s = database.compileStatement( "select count(*) from CLASSES where `wday`=" + wday + " and `subject`='" + subject + "' and `rawtime`='" + rawtime + "' and `location`='" + location + "'; " );
        long count = s.simpleQueryForLong();
        if(count <= 0) return true;
        else return false;
    }

    private void insertData(String subject, String location, String rawtime, int wday){
        if(!isUnique(subject, location, rawtime, wday)) return;
        Log.e("Inserted", subject + " / " + location + " / " + rawtime + " / " + wday);
        database.beginTransaction();
        try{
            String sql = "insert into CLASSES(`subject`, `location`, `rawtime`, `wday`) values ('" + subject + "', '" + location + "', '" + rawtime + "', " + wday + ");";
            database.execSQL(sql);
            database.setTransactionSuccessful();
        }catch(Exception e){
            Log.e("Insertion Failed", "###############################################");
        }finally{
            database.endTransaction();
        }
    }

    public void createDatabase(){
        database = getActivity().openOrCreateDatabase(dbName, android.content.Context.MODE_PRIVATE, null);
    }

    public void dropTable(){
        try{
            database.execSQL(dropTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable(){
        try{
            database.execSQL(createTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fm_1, container, false);

        createDatabase();

        pbar = (ProgressBar)rootView.findViewById(R.id.pbar);

        pref = getActivity().getSharedPreferences("Univtable", getActivity().MODE_PRIVATE);
        prefEditor = pref.edit();

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        testAdapter = new ListViewAdapter(rootView.getContext(), R.layout.listview_bid);
        recyclerViewMaterialAdapter = new RecyclerViewMaterialAdapter(testAdapter);
        mRecyclerView.setAdapter(recyclerViewMaterialAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);

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

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        WeatherParser.getWeather(pref.getString("geocode", Crawler.GEO_SEOUL), new Handler(){
            @Override
            public void handleMessage(Message msg){
                dataChanged();
            }
        });

        loadList();
    }

    public void dataChanged(){
        recyclerViewMaterialAdapter.notifyDataSetChanged();
        testAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
    }

    public void loadList(){
        pbar.setVisibility(View.VISIBLE);
        // Crawling Routine Begin

        Tcrawler.getTimetable(new Handler(){
            @Override
            public void handleMessage(Message msg){
                dropTable();
                createTable();
                testAdapter.mListData.clear();
                for(int i = 0; i < DUMMY_COUNT; i++){
                    testAdapter.addItem(new ClassInfo());
                }
                Calendar calendar = Calendar.getInstance( );
                final int wday = calendar.get(Calendar.DAY_OF_WEEK);
                for(ClassInfo c : Tcrawler.getClassList()) {
                    String sH = c.startHour < 10 ? "0" + c.startHour : "" + c.startHour;
                    String sM = c.startMin < 10 ? "0" + c.startMin : "" + c.startMin;
                    String eH = c.endHour < 10 ? "0" + c.endHour : "" + c.endHour;
                    String eM = c.endMin < 10 ? "0" + c.endMin : "" + c.endMin;
                    c.rawtime = sH + "시 " + sM + "분 - " + eH + "시 " + eM + "분";
                    if(wday - 1 == c.weekDay) testAdapter.addItem(c);
                    insertData(c.title, c.location, c.rawtime, c.weekDay + 1);
                }
                dataChanged();
                pbar.setVisibility(View.INVISIBLE);
            }
        });

    }
}
