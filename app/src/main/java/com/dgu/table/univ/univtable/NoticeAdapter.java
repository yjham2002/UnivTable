package com.dgu.table.univ.univtable;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    public Context mContext = null;
    public List<NoticeData> mListData = new ArrayList<>();
    public int item_layout;

    public NoticeAdapter(Context mContext, int item_layout) {
        super();
        this.mContext = mContext;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_notice, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        NoticeData mData = mListData.get(position);
        holder._title.setText(mData.Title);
        holder._date.setText(mData.Date);
        holder._content.setText(mData.Content);
        holder.cardview.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final NoticeData mData = mListData.get(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView _title;
        public TextView _date;
        public TextView _content;
        public CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            _title = (TextView)itemView.findViewById(R.id.title);
            _date = (TextView)itemView.findViewById(R.id.date);
            _content = (TextView)itemView.findViewById(R.id.content);
            cardview = (CardView)itemView.findViewById(R.id.cardview);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(NoticeData addInfo){
        NoticeData newAssign = new NoticeData(addInfo.id, addInfo.Title, addInfo.Content, addInfo.Date);
        mListData.add(newAssign);
    }

    public void dataChange(){
        this.notifyDataSetChanged();
    }

}
