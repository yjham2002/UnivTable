package com.dgu.table.univ.univtable;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import java.util.Calendar;

import crawl.AttendInfo;
import crawl.ClassInfo;

public class fm_3 extends Fragment implements View.OnClickListener{

    private SQLiteDatabase database;
    private String dbName = "UNIVTABLE_DB";
    private String attendTable =
            "create table if not exists ATTEND(" +
                    "`id` integer primary key autoincrement, " +
                    "`subject` text, " +
                    "`date` text);";

    private ProgressBar pbar;
    private RecyclerView mRecyclerView;
    public AttendListAdapter testAdapter;

    public void createDatabase(){
        database = getActivity().openOrCreateDatabase(dbName, android.content.Context.MODE_PRIVATE, null);
    }

    public void createTable(){
        try{
            database.execSQL(attendTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectData(){
        int counter = 0;
        pbar.setVisibility(View.VISIBLE);
        String sql = "select distinct `subject`, `date` from ATTEND order by `id` asc";
        Cursor result = database.rawQuery(sql, null);
        result.moveToFirst();
        testAdapter.mListData.clear();
        while(!result.isAfterLast()){
            testAdapter.mListData.add(new AttendInfo(result.getString(0), result.getString(1)));
            counter++;
            result.moveToNext();
        }
        if(counter == 0){
            testAdapter.mListData.add(new AttendInfo("유니테이블", "결석 혹은 지각한 강의가 없습니다."));
        }
        result.close();
        pbar.setVisibility(View.INVISIBLE);
        testAdapter.dataChange();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fm_3, container, false);

        pbar = (ProgressBar)rootView.findViewById(R.id.pbar);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        testAdapter = new AttendListAdapter(rootView.getContext(), R.layout.listview_bid);

        mRecyclerView.setAdapter(new RecyclerViewMaterialAdapter(testAdapter));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);

        createDatabase();
        createTable();
        selectData();

        return rootView;
    }

    @Override
    public void onClick(View v) {
    }
}
