package com.dgu.table.univ.univtable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import crawl.Crawler;
import util.Communicator;
import util.TimeCalculator;
import util.URL;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    private ProgressDialog progressDialog;

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private int articleNumber = -1;

    private RecyclerView mRecyclerView;
    private CommentAdapter commentAdapter;

    private Button _remove;
    private Button _exit;
    private Button _send;
    private ImageView _favicon;
    private TextView _content;
    private TextView _name;
    private TextView _date;
    private EditText _comment;

    private int mToken = -1;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.detail_submit:
                onSend(_comment.getText().toString());
                break;
            case R.id.detail_remove:
                onRemove();
                break;
            case R.id.detail_exit:
                finish();
                break;
            default: break;
        }
    }

    public void sendPush(String title, String message){
        if(mToken == -1) return;
        HashMap<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("message", message);
        Log.e("FCM Sent", util.URL.MAIN + util.URL.REST_FCM_ONE + mToken);
        new Communicator().postHttp(util.URL.MAIN + util.URL.REST_FCM_ONE + mToken, data, new Handler());
    }

    public void init(){
        pref = getSharedPreferences("Univtable", MODE_PRIVATE);
        prefEditor = pref.edit();

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        commentAdapter = new CommentAdapter(this, R.layout.listview_comment);
        mRecyclerView.setAdapter(commentAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

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
            mToken = bundle.getInt("mid", -1);
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
    }

    @Override
    public void onResume(){
        super.onResume();
        loadComment();
    }

    public void loadComment(){
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("댓글 목록을 불러오는 중...");
        progressDialog.show();

        commentAdapter.mListData.clear();
        Communicator.getHttp(URL.MAIN + URL.REST_BOARD_COMMENT + articleNumber, new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                try {
                    JSONArray json_arr = new JSONArray(jsonString);
                    for(int i = 0; i < json_arr.length(); i++){
                        JSONObject json_list = json_arr.getJSONObject(i);
                        commentAdapter.addItem(new CommentData(json_list.getInt("id"), json_list.getInt("mid"), json_list.getInt("aid"), json_list.getString("content"), json_list.getString("date"), json_list.getString("userName")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    progressDialog.dismiss();
                    commentAdapter.dataChange();
                }

            }
        });
    }

    public void onSend(String msg){
        if(msg.length() < 5){
            Toast.makeText(getApplicationContext(), "5자 이상 입력하세요", Toast.LENGTH_LONG).show();
            return;
        }
        HashMap<String, String> dataSet = new HashMap<>();
        dataSet.put("mid", Integer.toString(pref.getInt("mid", -1)));
        dataSet.put("aid", Integer.toString(articleNumber));
        dataSet.put("content", msg);
        new Communicator().postHttp(URL.MAIN + URL.REST_COMMENT_NEW, dataSet, new Handler(){
            @Override
            public void handleMessage(Message msg){
                _comment.setText("");
                loadComment();
                sendPush(getResources().getString(R.string.app_name), pref.getString("name", "#") + "님이 댓글을 남겼습니다");
            }
        });
    }

    public void onRemove(){
        new Communicator().postHttp(URL.MAIN + URL.REST_REMOVE_BOARD + articleNumber, new HashMap<String, String>(), new Handler(){
            @Override
            public void handleMessage(Message msg){
                Toast.makeText(getApplicationContext(), "삭제되었습니다", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

}
