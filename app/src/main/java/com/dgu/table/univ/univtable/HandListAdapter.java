package com.dgu.table.univ.univtable;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import crawl.ClassInfo;
import crawl.HandInfo;

public class HandListAdapter extends RecyclerView.Adapter<HandListAdapter.ViewHolder> {

    public Context mContext = null;
    public List<HandInfo> mListData = new ArrayList<>();
    public int item_layout;

    public HandListAdapter(Context mContext, int item_layout) {
        super();
        this.mContext = mContext;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_hand, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        HandInfo mData = mListData.get(position);
        holder._subject.setText(mData.subject);
        holder._num.setText(mData.score);
        holder._open.setText(mData.open);
        holder._time.setText(mData.date);
        holder._handin.setText(mData.handin);
        holder._name.setText(mData.name);
        holder.cardview.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final AssignInfo mData = staticInfo.mAdapter.mListData.get(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView _subject, _num, _open, _time, _handin, _name;
        public CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            _num = (TextView)itemView.findViewById(R.id.num);
            _open = (TextView)itemView.findViewById(R.id.open);
            _time = (TextView)itemView.findViewById(R.id.time);
            _handin = (TextView)itemView.findViewById(R.id.handin);
            _name = (TextView)itemView.findViewById(R.id.name);
            _subject = (TextView)itemView.findViewById(R.id.subject);
            cardview = (CardView)itemView.findViewById(R.id.cardview);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(HandInfo addInfo){
        mListData.add(addInfo.clone());
    }
    public void dataChange(){
        this.notifyDataSetChanged();
    }

}
