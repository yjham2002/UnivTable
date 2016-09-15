package com.dgu.table.univ.univtable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

public class fm_1 extends Fragment implements View.OnClickListener{

    public RecyclerView mRecyclerView;
    public ListViewAdapter testAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fm_1, container, false);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);
        testAdapter = new ListViewAdapter(rootView.getContext(), R.layout.listview_bid);

        mRecyclerView.setAdapter(new RecyclerViewMaterialAdapter(testAdapter));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView);

        testAdapter.addItem(new ClassInfo("헤더"));
        testAdapter.addItem(new ClassInfo("디폴트"));
        testAdapter.addItem(new ClassInfo("디폴트"));
        testAdapter.addItem(new ClassInfo("디폴트"));
        testAdapter.addItem(new ClassInfo("디폴트"));
        testAdapter.addItem(new ClassInfo("디폴트"));
        testAdapter.addItem(new ClassInfo("디폴트"));
        testAdapter.addItem(new ClassInfo("디폴트"));
        testAdapter.addItem(new ClassInfo("디폴트"));
        testAdapter.addItem(new ClassInfo("디폴트"));
        testAdapter.addItem(new ClassInfo("디폴트"));
        testAdapter.addItem(new ClassInfo("디폴트"));
        testAdapter.addItem(new ClassInfo("디폴트"));
        testAdapter.addItem(new ClassInfo("디폴트"));
        testAdapter.addItem(new ClassInfo("디폴트"));
        testAdapter.addItem(new ClassInfo("디폴트"));

        testAdapter.dataChange();
        return rootView;
    }

    public void onClick(View v) {
    }
}