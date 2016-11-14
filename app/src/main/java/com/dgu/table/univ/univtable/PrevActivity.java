package com.dgu.table.univ.univtable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PrevActivity extends AppCompatActivity implements View.OnClickListener{

    private Button _login;

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_signin:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }

    public void initView(){
        _login = (Button)findViewById(R.id.bt_signin);
        _login.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev);

        initView();
    }
}
