package com.dgu.table.univ.univtable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.Communicator;
import util.TimeCalculator;
import util.URL;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    public Context mContext = null;
    public List<CommentData> mListData = new ArrayList<>();
    public int item_layout;

    public CommentAdapter(Context mContext, int item_layout) {
        super();
        this.mContext = mContext;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_comment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        pref = mContext.getSharedPreferences("Univtable", mContext.MODE_PRIVATE);
        prefEditor = pref.edit();

        final CommentData mData = mListData.get(position);

        holder._title.setText(mData.userName);
        holder._title.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(mContext, ChatActivity.class);
                i.putExtra("partner", mData.mid);
                if(pref.getInt("mid", -1) != mData.mid) mContext.startActivity(i);
            }
        });
        holder._date.setText(TimeCalculator.formatTimeString(mData.date));
        holder._content.setText(mData.content);
        holder.cardview.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppTheme_Dark_Dialog);
                builder.setMessage("댓글을 삭제하시겠습니까?");
                builder.setCancelable(true);
                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                HashMap<String, String> data = new HashMap<>();
                                data.put("id", Integer.toString(mData.id));
                                new Communicator().postHttp(URL.MAIN + URL.REST_REMOVE_COMMENT, data, new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        if (mContext instanceof DetailActivity)
                                            ((DetailActivity) mContext).loadComment();
                                    }
                                });
                            }
                        });
                AlertDialog alert = builder.create();
                if (pref.getInt("mid", -1) == mData.mid) alert.show();
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
        public TextView _amount;
        public LinearLayout cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            _title = (TextView)itemView.findViewById(R.id.title);
            _date = (TextView)itemView.findViewById(R.id.date);
            _content = (TextView)itemView.findViewById(R.id.content);
            cardview = (LinearLayout) itemView.findViewById(R.id.cardview);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(CommentData addInfo){
        mListData.add(addInfo);
    }

    public void dataChange(){
        this.notifyDataSetChanged();
    }

}

