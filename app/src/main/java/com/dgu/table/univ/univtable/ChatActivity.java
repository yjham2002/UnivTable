package com.dgu.table.univ.univtable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import util.Communicator;
import util.URL;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private int partnerKey = -2;

    public static boolean isRun = false;

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private EditText msg;
    private TextView title;
    private ListView chat;
    private Button exit, send;
    private ChatAdapter cAdapter;

    private SQLiteDatabase database;
    private String dbName = "UNIVTABLE_DB";
    private String createTable =
            "create table if not exists UNIVTABLE_CHAT(" +
                    "`id` integer primary key autoincrement, " +
                    "`from` integer, " +
                    "`to` integer, " +
                    "`msg` text, " +
                    "`date` datetime, " +
                    "`read` integer);";


    public void createDatabase(){
        database = openOrCreateDatabase(dbName, android.content.Context.MODE_PRIVATE, null);
    }

    public void createTable(){
        try{
            database.execSQL(createTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertData(String from, String to, String msg){
        database.beginTransaction();
        try{
            String sql = "insert into UNIVTABLE_CHAT(`from`, `to`, `msg`, `date`, read) values (" + from + ", " + to + ", '" + msg + "', datetime('now', 'localtime') ," + 0 + ");";
            database.execSQL(sql);
            database.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            database.endTransaction();
        }
    }

    public void selectData(){
        String sql = "select * from UNIVTABLE_CHAT where `from` = " + pref.getInt("mid", -1) + " OR `to` = "+ pref.getInt("mid", -1) +" order by `id` asc";
        Cursor result = database.rawQuery(sql, null);
        result.moveToFirst();
        cAdapter.mListData.clear();
        while(!result.isAfterLast()){
            cAdapter.addItem(result.getInt(0), result.getInt(1), result.getInt(2), result.getString(3), result.getString(4), result.getInt(5));
            result.moveToNext();
        }
        cAdapter.dataChange();
        result.close();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public void init(){
        createDatabase();
        createTable();

        msg = (EditText)findViewById(R.id.msg);
        title = (TextView)findViewById(R.id.title);
        chat = (ListView)findViewById(R.id.chat);
        exit = (Button)findViewById(R.id.bt_exit);
        send = (Button)findViewById(R.id.send);

        exit.setOnClickListener(this);
        send.setOnClickListener(this);

        cAdapter = new ChatAdapter(this);

        chat.setAdapter(cAdapter);

        selectData();

        title.setText("불러오는 중...");

        chat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int position = cAdapter.mListData.get(arg2).id;
                final String sql = "delete from UNIVTABLE_CHAT where `id` = " + position;
                Toast.makeText(getApplicationContext(), "메세지 삭제됨", Toast.LENGTH_LONG).show();
                database.execSQL(sql);
                selectData();
                return false;
            }
        });

        partnerKey = getIntent().getExtras().getInt("partner"); // NEED TO BE MODIFIED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        pref = getSharedPreferences("Univtable", MODE_PRIVATE);
        prefEditor = pref.edit();

        init();
    }

    public void onSend(){
        if(msg.getText().length() > 0) {
            insertData(Integer.toString(pref.getInt("mid", -1)), Integer.toString(partnerKey), msg.getText().toString());
            selectData();
            HashMap<String, String> data = new HashMap<>();
            data.put("title", "MSG_CALL");
            data.put("message", msg.getText().toString());
            data.put("froma", Integer.toString(pref.getInt("mid", -2)));
            data.put("toa", Integer.toString(partnerKey));
            Log.e("CHAT", data.toString());
            new Communicator().postHttp(URL.MAIN + URL.REST_CHAT + partnerKey, data, new Handler());
            msg.setText("");
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_exit:
              finish();
            break;
            case R.id.send:
                onSend();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isRun = true;
        Communicator.getHttp(URL.MAIN + URL.REST_USER_ONE + partnerKey, new Handler(){
            @Override
            public void handleMessage(Message msg){
                String jsonString = msg.getData().getString("jsonString");
                try {
                    JSONArray json_arr = new JSONArray(jsonString);
                    JSONObject json_list = json_arr.getJSONObject(0);
                    Log.e("JSON", msg.getData().getString("jsonString"));
                    title.setText(json_list.getString("Name").toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("UNIVTABLE_CHAT_EVENT"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            selectData();
        }
    };

    @Override
    public void onDestroy(){
        super.onDestroy();
        isRun = false;
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        isRun = false;
        super.onPause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            finish();
            overridePendingTransition(R.anim.push_in_r, R.anim.push_out_r);
        }
        return super.onKeyDown(keyCode, event);
    }

}
