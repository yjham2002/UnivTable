package com.dgu.table.univ.univtable;



import android.content.SharedPreferences;
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

import java.util.Calendar;

import crawl.ClassInfo;
import crawl.Crawler;
import crawl.DonggukCrawler;
import crawl.HandInfo;
import crawl.KookminCrawler;
import crawl.SogangCrawler;
import util.WeatherParser;

public class fm_2 extends Fragment implements View.OnClickListener{

    private static final int DUMMY_COUNT = 0;

    private ProgressBar pbar;

    private Crawler Tcrawler;

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    public RecyclerView mRecyclerView;
    public HandListAdapter testAdapter;

    private RecyclerViewMaterialAdapter recyclerViewMaterialAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fm_2, container, false);

        pbar = (ProgressBar)rootView.findViewById(R.id.pbar);

        pref = getActivity().getSharedPreferences("Univtable", getActivity().MODE_PRIVATE);
        prefEditor = pref.edit();

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        testAdapter = new HandListAdapter(rootView.getContext(), R.layout.listview_bid);
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

        testAdapter.dataChange();
        return rootView;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onResume(){
        super.onResume();
        loadList();
    }

    public void loadList(){
        pbar.setVisibility(View.VISIBLE);
        // Crawling Routine Begin

        Tcrawler.getTimetable(new Handler(){
            @Override
            public void handleMessage(Message msg){
                testAdapter.mListData.clear();
                testAdapter.mListData.addAll(Tcrawler.getHandList());
                dataChanged();
                pbar.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void dataChanged(){
        recyclerViewMaterialAdapter.notifyDataSetChanged();
        testAdapter.notifyDataSetChanged();
    }

}
