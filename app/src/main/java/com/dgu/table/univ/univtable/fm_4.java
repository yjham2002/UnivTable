package com.dgu.table.univ.univtable;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import crawl.ClassInfo;
import util.Communicator;
import util.URL;

public class fm_4 extends Fragment implements View.OnClickListener{

    private ProgressBar pbar;

    public RecyclerView mRecyclerView;
    public ArticleAdapter testAdapter;
    private RecyclerViewMaterialAdapter recyclerViewMaterialAdapter;

    private FloatingActionButton _add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fm_4, container, false);

        pbar = (ProgressBar)rootView.findViewById(R.id.pbar);

        _add = (FloatingActionButton)rootView.findViewById(R.id.addbutton);
        _add.setOnClickListener(this);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        testAdapter = new ArticleAdapter(rootView.getContext(), R.layout.listview_bid);

        recyclerViewMaterialAdapter = new RecyclerViewMaterialAdapter(testAdapter);

        mRecyclerView.setAdapter(recyclerViewMaterialAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);

        testAdapter.dataChange();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addbutton:
                Intent i = new Intent(getActivity(), WriteActivity.class);
                startActivity(i);
                break;
            default: break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        loadList();
    }

    public void dataChanged(){
        recyclerViewMaterialAdapter.notifyDataSetChanged();
        testAdapter.notifyDataSetChanged();
    }

    public void loadList(){
        Communicator.getHttp(URL.MAIN + URL.REST_BOARD_ALL, new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                testAdapter.mListData.clear();
                try {
                    JSONArray json_arr = new JSONArray(jsonString);
                    for(int i = 0; i < json_arr.length(); i++){
                        JSONObject json_list = json_arr.getJSONObject(i);
                        Log.e("Article", json_list.toString());
                        ArticleItem item = new ArticleItem(
                                json_list.getInt("id"),
                                json_list.getInt("mid"),
                                json_list.getString("content"),
                                json_list.getInt("flag"),
                                json_list.getInt("hit"),
                                json_list.getInt("ucode"),
                                json_list.getString("name"),
                                json_list.getString("date"));
                        testAdapter.addItem(item);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
                dataChanged();
                pbar.setVisibility(View.INVISIBLE);
            }
        });
    }

}
