package com.dgu.table.univ.univtable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import crawl.Crawler;
import util.Communicator;
import util.URL;

public class ChatListActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView _title;
    private Button _exit, _start, _refresh;

    private ImageView _iv;

    private int PartnerNumber = -1;
    private int PartnerUcode = -1;

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_refresh:
                refresh();
                break;
            case R.id.bt_exit:
                finish();
                break;
            case R.id.bt_start:
                if(PartnerNumber == pref.getInt("mid", -1)){
                    Toast.makeText(getApplicationContext(), "운이 좋으시군요! 본인과 매칭되었습니다!\n새로운 상대를 찾아주세요!", Toast.LENGTH_LONG).show();
                }else {
                    Intent i = new Intent(new Intent(this, ChatActivity.class));
                    i.putExtra("partner", PartnerNumber);
                    startActivity(i);
                }
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public void init(){
        _iv = (ImageView)findViewById(R.id.favicon);
        _title = (TextView)findViewById(R.id.title);
        _exit = (Button)findViewById(R.id.bt_exit);
        _exit.setOnClickListener(this);
        _start = (Button)findViewById(R.id.bt_start);
        _start.setOnClickListener(this);
        _refresh = (Button)findViewById(R.id.bt_refresh);
        _refresh.setOnClickListener(this);
        refresh();
    }

    public void refresh(){
        final ProgressDialog pdial = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        pdial.setMessage("상대방 정보를 불러오는 중...");
        pdial.setCancelable(false);
        pdial.show();
        _start.setEnabled(false);
        Communicator.getHttp(URL.MAIN + URL.REST_RANDOM, new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                try {
                    JSONArray json_arr = new JSONArray(jsonString);
                    JSONObject json_list = json_arr.getJSONObject(0);
                    PartnerNumber = json_list.getInt("id");
                    PartnerUcode = json_list.getInt("uid");
                    switch (PartnerUcode){
                        case -1:
                            _iv.setImageDrawable(getResources().getDrawable(R.drawable.smile));
                            break;
                        case Crawler.UCODE_DONGGUK: case Crawler.UCODE_DONGGUK_GY: case Crawler.UCODE_DONGGUK_IL:
                            _iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_dongguk));
                            break;
                        case Crawler.UCODE_KOOKMIN:
                            _iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_kookmin));
                            break;
                        case Crawler.UCODE_SOGANG:
                            _iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_sogang));
                            break;
                    }
                    _start.setEnabled(true);
                }catch (JSONException e){
                    _start.setEnabled(false);
                    e.printStackTrace();
                }finally {
                    pdial.dismiss();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        pref = getSharedPreferences("Univtable", MODE_PRIVATE);
        prefEditor = pref.edit();

        init();
    }

}
