package com.dgu.table.univ.univtable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button _login;
    private EditText _id, _pw;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bt_signin:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                //signIn();
                break;
            default: break;
        }
    }

    public void signIn(){
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
        _login.setEnabled(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _login = (Button)findViewById(R.id.bt_signin);
        _id = (EditText)findViewById(R.id.idp);
        _pw = (EditText)findViewById(R.id.pwp);
        _login.setOnClickListener(this);
    }

}