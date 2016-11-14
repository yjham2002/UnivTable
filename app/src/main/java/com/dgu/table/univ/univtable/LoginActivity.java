package com.dgu.table.univ.univtable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import crawl.Crawler;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private Button _login;
    private EditText _id, _pw;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bt_signin:
                signIn();
                break;
            default: break;
        }
    }

    public void signIn(){
        /*
        _login.setEnabled(false);
        _id.setError(null);
        _pw.setError(null);
        if(_id.getText().length() <= 0 || _id.getText().length() > 20){
            _id.setError("유효한 정보를 입력하세요");
            return;
        }
        if(_pw.getText().length() < 5){
            _pw.setError("유효한 패스워드를 입력하세요");
            return;
        }
        final ProgressDialog pdial = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        pdial.setMessage("계정 정보를 불러오는 중...");
        pdial.setCancelable(false);
        pdial.show();
        _login.setEnabled(true);
        pdial.dismiss();
*/
        // on success
        prefEditor.putString("id", "2014112021");
        prefEditor.putString("name", "함의진");
        prefEditor.putInt("ucode", Crawler.UCODE_SOGANG);
        prefEditor.commit();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getSharedPreferences("Univtable", MODE_PRIVATE);
        prefEditor = pref.edit();

        _login = (Button)findViewById(R.id.bt_signin);
        _id = (EditText)findViewById(R.id.idp);
        _pw = (EditText)findViewById(R.id.pwp);
        _login.setOnClickListener(this);
    }

}