package com.dgu.table.univ.univtable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import crawl.ClassInfo;
import crawl.Crawler;
import util.WeatherParser;

public class fm_1 extends Fragment implements View.OnClickListener{

    private static final int DUMMY_COUNT = 1;

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private RecyclerView mRecyclerView;
    private RecyclerViewMaterialAdapter recyclerViewMaterialAdapter;
    private ListViewAdapter testAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fm_1, container, false);

        pref = getActivity().getSharedPreferences("Univtable", getActivity().MODE_PRIVATE);
        prefEditor = pref.edit();

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        testAdapter = new ListViewAdapter(rootView.getContext(), R.layout.listview_bid);
        recyclerViewMaterialAdapter = new RecyclerViewMaterialAdapter(testAdapter);
        mRecyclerView.setAdapter(recyclerViewMaterialAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);

        for(int i = 0; i < DUMMY_COUNT; i++){
            testAdapter.addItem(new ClassInfo());
        }

        loadList();

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

    }

    public void dataChanged(){
        recyclerViewMaterialAdapter.notifyDataSetChanged();
        testAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
    }

    public void loadList(){
        testAdapter.addItem(new ClassInfo());
        dataChanged();
    }
}
