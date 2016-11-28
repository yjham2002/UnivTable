package com.dgu.table.univ.univtable;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    public Context mContext = null;
    public ArrayList<ChatItems> mListData = new ArrayList<>();

    public ChatAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }
    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(int id, int from, int to, String msg, String date, int read){
        ChatItems addInfo = new ChatItems();
        addInfo.id = id;
        addInfo.from = from;
        addInfo.to = to;
        addInfo.msg = msg;
        addInfo.date = date;
        addInfo.read = read;
        mListData.add(addInfo);
    }

    public void dataChange(){
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        pref = mContext.getSharedPreferences("Univtable", mContext.MODE_PRIVATE);
        prefEditor = pref.edit();

        final ChatItems mData = mListData.get(position);
        ViewHolder holder;

        holder = new ViewHolder();

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(mData.from == pref.getInt("mid", 0)) convertView = inflater.inflate(R.layout.chatbox_me, null);
        else convertView = inflater.inflate(R.layout.chatbox_you, null);

        holder.mText = (TextView) convertView.findViewById(R.id.mText);
        holder.mDate = (TextView) convertView.findViewById(R.id.mDate);
        convertView.setTag(holder);
        holder.mText.setText(mData.msg);
        holder.mDate.setText(mData.date);

        return convertView;
    }

    private class ViewHolder {
        public TextView mText;
        public TextView mDate;
    }

}
