package com.dgu.table.univ.univtable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import crawl.Crawler;
import crawl.DonggukCrawler;
import crawl.SogangCrawler;

public class SpinnerAdapter extends BaseAdapter{

    private Context context = null;
    private ArrayList<UnivItem> spinnerData = null;
    private LayoutInflater inflater;
    private int item_layout, item_layout2;

    public SpinnerAdapter (Context context, ArrayList<UnivItem> spinnerData, int item_layout, int item_layout2){
        this.context = context;
        this.spinnerData = spinnerData;
        this.item_layout = item_layout;
        this.item_layout2 = item_layout2;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setChangeData(ArrayList<UnivItem> spinnerData){
        this.spinnerData = spinnerData;
    }

    @Override
    public int getCount() {
        if(null != spinnerData) return spinnerData.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        if(null != spinnerData){
            return spinnerData.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UnivItem data = spinnerData.get(position);
        if(null != data){
            convertView = inflater.inflate(item_layout, parent, false);
            ImageView iv = (ImageView) convertView.findViewById(R.id.fav);
            TextView tv = (TextView) convertView.findViewById(R.id.subject);
            switch (data.ucode){
                case Crawler.UCODE_DONGGUK: case Crawler.UCODE_DONGGUK_GY: case Crawler.UCODE_DONGGUK_IL:
                    iv.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_dongguk));
                    break;
                case Crawler.UCODE_SOGANG:
                    iv.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_sogang));
                    break;
                case Crawler.UCODE_KOOKMIN:
                    iv.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_kookmin));
                    break;
                default:
                    iv.setImageDrawable(context.getResources().getDrawable(R.drawable.smile));
                    break;
            }
            tv.setText(data.uname);
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        UnivItem data = spinnerData.get(position);
        convertView = inflater.inflate(item_layout2, parent, false);
        ImageView iv = (ImageView) convertView.findViewById(R.id.fav);
        TextView tv = (TextView) convertView.findViewById(R.id.subject);
        switch (data.ucode){
            case Crawler.UCODE_DONGGUK: case Crawler.UCODE_DONGGUK_GY: case Crawler.UCODE_DONGGUK_IL:
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_dongguk));
                break;
            case Crawler.UCODE_SOGANG:
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_sogang));
                break;
            case Crawler.UCODE_KOOKMIN:
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_kookmin));
                break;
            default:
                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.smile));
                break;
        }
        tv.setText(data.uname);
        return convertView;
    }



}
