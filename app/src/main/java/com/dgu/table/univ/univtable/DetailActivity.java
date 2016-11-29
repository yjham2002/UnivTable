package com.dgu.table.univ.univtable;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import crawl.Crawler;
import util.TimeCalculator;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private int articleNumber = -1;

    private Button _remove;
    private Button _exit;
    private Button _send;
    private ImageView _favicon;
    private TextView _content;
    private TextView _name;
    private TextView _date;
    private EditText _comment;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.detail_submit:
                break;
            case R.id.detail_remove:
                break;
            case R.id.detail_exit:
                finish();
                break;
            default: break;
        }
    }

    public void init(){
        pref = getSharedPreferences("Univtable", MODE_PRIVATE);
        prefEditor = pref.edit();

        _favicon = (ImageView)findViewById(R.id.detail_favicon);
        _remove = (Button)findViewById(R.id.detail_remove);
        _remove.setOnClickListener(this);
        _exit = (Button)findViewById(R.id.detail_exit);
        _exit.setOnClickListener(this);
        _send = (Button)findViewById(R.id.detail_submit);
        _send.setOnClickListener(this);
        _date = (TextView)findViewById(R.id.detail_date);
        _name = (TextView)findViewById(R.id.detail_name);
        _content = (TextView)findViewById(R.id.detail_content);
        _comment = (EditText)findViewById(R.id.detail_comment);
        setView();
    }

    public void setView(){
        try {
            Bundle bundle = getIntent().getExtras();
            _content.setText(bundle.getString("content", "불러오는 중 오류가 발생하였습니다."));
            _name.setText(bundle.getString("name", "Unknown"));
            _date.setText(bundle.getString("date", "0000-00-00"));
            articleNumber = bundle.getInt("id", -1);
            if(bundle.getInt("mid", -1) != pref.getInt("mid", -2)) _remove.setVisibility(View.GONE);
            switch (bundle.getInt("ucode", -1)){
                case -1:
                    _favicon.setImageDrawable(getResources().getDrawable(R.drawable.smile));
                    break;
                case Crawler.UCODE_DONGGUK: case Crawler.UCODE_DONGGUK_GY: case Crawler.UCODE_DONGGUK_IL:
                    _favicon.setImageDrawable(getResources().getDrawable(R.drawable.icon_dongguk));
                    break;
                case Crawler.UCODE_KOOKMIN:
                    _favicon.setImageDrawable(getResources().getDrawable(R.drawable.icon_kookmin));
                    break;
                case Crawler.UCODE_SOGANG:
                    _favicon.setImageDrawable(getResources().getDrawable(R.drawable.icon_sogang));
                    break;
            }
        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(), "게시글을 불러올 수 없습니다", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
        loadComment();
    }

    public void loadComment(){

    }

}
