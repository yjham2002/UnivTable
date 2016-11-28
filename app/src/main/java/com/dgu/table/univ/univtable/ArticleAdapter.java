package com.dgu.table.univ.univtable;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import crawl.AttendInfo;
import crawl.Crawler;
import util.TimeCalculator;

/**
 * Created by HP on 2016-11-14.
 */
public class ArticleAdapter  extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    public static final int HEADER = 3, DEFAULT = 0;
    public Context mContext = null;
    public List<ArticleItem> mListData = new ArrayList<>();
    public int item_layout;

    public ArticleAdapter(Context mContext, int item_layout) {
        super();
        this.mContext = mContext;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_article, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ArticleItem mData = mListData.get(position);
        String tempContent = "";
        if(mData.content.length() > 20) tempContent = mData.content.substring(0, 23) + "...";
        else tempContent = mData.content;
        holder._name.setText(mData.name);
        holder._content.setText(tempContent);
        holder._comment.setText(Integer.toString(mData.comment));
        holder._date.setText(TimeCalculator.formatTimeString(mData.date));
        switch (mData.ucode){
            case -1:
                holder._iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.smile));
                break;
            case Crawler.UCODE_DONGGUK: case Crawler.UCODE_DONGGUK_GY: case Crawler.UCODE_DONGGUK_IL:
                holder._iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_dongguk));
                break;
            case Crawler.UCODE_KOOKMIN:
                holder._iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_kookmin));
                break;
            case Crawler.UCODE_SOGANG:
                holder._iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_sogang));
                break;
        }

        holder._name.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(mContext, ChatActivity.class);
                i.putExtra("partner", mData.mid);
                mContext.startActivity(i);
            }
        });

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
        public TextView _name, _content, _date, _comment;
        public ImageView _iv;
        public CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            _comment = (TextView)itemView.findViewById(R.id.article_comment);
            _name = (TextView)itemView.findViewById(R.id.article_name);
            _content = (TextView)itemView.findViewById(R.id.article_content);
            _date = (TextView)itemView.findViewById(R.id.article_date);
            _iv = (ImageView)itemView.findViewById(R.id.favicon);
            cardview = (CardView)itemView.findViewById(R.id.cardview);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(ArticleItem addInfo){
        mListData.add(addInfo);
    }
    public void dataChange(){
        this.notifyDataSetChanged();
    }

}