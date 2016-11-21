package com.dgu.table.univ.univtable;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.Communicator;
import util.URL;

public class NoticeActivity extends AppCompatActivity implements View.OnClickListener{

    private Button _exit;
    private RecyclerView mRecyclerView;
    private NoticeAdapter noticeAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_exit:
                finish();
                break;
            default: break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        _exit = (Button)findViewById(R.id.bt_exit);
        _exit.setOnClickListener(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        noticeAdapter = new NoticeAdapter(this, R.layout.listview_notice);
        mRecyclerView.setAdapter(noticeAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Communicator.getHttp(URL.MAIN + URL.REST_NOTICE_ALL, new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                try {
                    noticeAdapter.mListData.clear();
                    JSONArray json_arr = new JSONArray(jsonString);
                    for(int i = 0; i < json_arr.length(); i++) {
                        JSONObject json_list = json_arr.getJSONObject(i);
                        noticeAdapter.addItem(new NoticeData(json_list.getInt("id"), json_list.getString("Title"), json_list.getString("Content"), json_list.getString("Date")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    noticeAdapter.dataChange();
                }

            }
        });

    }
}
