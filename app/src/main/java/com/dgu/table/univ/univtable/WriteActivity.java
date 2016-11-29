package com.dgu.table.univ.univtable;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.HashMap;

import util.Communicator;
import util.URL;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private Button _cancel, _confirm;
    private EditText _textbox;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_confirm:
                upload(pref.getInt("mid", 0), _textbox.getText().toString(), 0);
                break;
            case R.id.bt_cancel:
                finish();
                break;
            default: break;
        }
    }

    public void upload(final int mid, String msg, final int flag){
        if(msg.length() < 5){
            Toast.makeText(getApplicationContext(), "5자 이상 입력하세요", Toast.LENGTH_LONG).show();
            return;
        }
        HashMap<String, String> data = new HashMap<>();
        data.put("mid", Integer.toString(mid));
        data.put("content", msg);
        data.put("flag", Integer.toString(flag));
        new Communicator().postHttp(URL.MAIN + URL.REST_BOARD_NEW, data, new Handler(){
            @Override
            public void handleMessage(Message msg){
                finish();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public void setView(){
        _cancel = (Button)findViewById(R.id.bt_cancel);
        _cancel.setOnClickListener(this);
        _confirm = (Button)findViewById(R.id.bt_confirm);
        _confirm.setOnClickListener(this);
        _textbox = (EditText)findViewById(R.id.tbox);
        _textbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 300) {
                    Toast.makeText(getApplicationContext(), "300자 이내로 작성하십시오", Toast.LENGTH_LONG).show();
                    _textbox.setText(charSequence.subSequence(0, 300));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        pref = getSharedPreferences("Univtable", MODE_PRIVATE);
        prefEditor = pref.edit();

        setView();
    }
}
