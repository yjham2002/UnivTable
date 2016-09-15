package com.dgu.table.univ.univtable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

public class fm_4 extends Fragment implements View.OnClickListener{

    ObservableScrollView mScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fm_4, container, false);

        mScrollView = (ObservableScrollView)rootView.findViewById(R.id.scrollView);
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);
        //MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, myObservableScrollViewCallbacks);
    }

    public void onClick(View v) {
    }
}