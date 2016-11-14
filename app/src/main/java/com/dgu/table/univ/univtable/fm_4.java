package com.dgu.table.univ.univtable;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import crawl.ClassInfo;

public class fm_4 extends Fragment implements View.OnClickListener{

    public RecyclerView mRecyclerView;
    public ListViewAdapter testAdapter;
    private FloatingActionButton _add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fm_4, container, false);

        _add = (FloatingActionButton)rootView.findViewById(R.id.addbutton);
        _add.setOnClickListener(this);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        testAdapter = new ListViewAdapter(rootView.getContext(), R.layout.listview_bid);

        mRecyclerView.setAdapter(new RecyclerViewMaterialAdapter(testAdapter));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);

        testAdapter.addItem(new ClassInfo());
        testAdapter.addItem(new ClassInfo());
        testAdapter.addItem(new ClassInfo());
        testAdapter.addItem(new ClassInfo());
        testAdapter.addItem(new ClassInfo());
        testAdapter.addItem(new ClassInfo());
        testAdapter.addItem(new ClassInfo());
        testAdapter.addItem(new ClassInfo());
        testAdapter.addItem(new ClassInfo());
        testAdapter.addItem(new ClassInfo());

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
}
