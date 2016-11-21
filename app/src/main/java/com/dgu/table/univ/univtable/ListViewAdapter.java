package com.dgu.table.univ.univtable;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import crawl.ClassInfo;
import util.WeatherParser;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    public static final int HEADER = 3, DEFAULT = 0;
    public Context mContext = null;
    public List<ClassInfo> mListData = new ArrayList<>();
    public int item_layout;

    public ListViewAdapter(Context mContext, int item_layout) {
        super();
        this.mContext = mContext;
        this.item_layout = item_layout;
    }

    @Override
    public int getItemViewType(int position){
        if(position == 0) return HEADER;
        else return DEFAULT;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch(viewType){
            case DEFAULT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_bid, parent, false);
                break;
            case HEADER:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_header, parent, false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_bid, parent, false);
                break;
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(getItemViewType(position) == HEADER){
            Picasso.with(mContext).load(WeatherParser.getImageUrl()).into(holder._wIcon);
            holder._subject.setText(WeatherParser.getWeatherInfo());
        }else {
            ClassInfo mData = mListData.get(position);
            holder._subject.setText(mData.title);
            holder.cardview.setOnClickListener(new CardView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //final AssignInfo mData = staticInfo.mAdapter.mListData.get(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView _subject;
        public ImageView _wIcon;
        public CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            _wIcon = (ImageView) itemView.findViewById(R.id.wIcon);
            _subject = (TextView) itemView.findViewById(R.id.subject);
            cardview = (CardView) itemView.findViewById(R.id.cardview);

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(ClassInfo addInfo){
        mListData.add(addInfo);
    }
    public void dataChange(){
        this.notifyDataSetChanged();
    }

}