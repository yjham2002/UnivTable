package com.dgu.table.univ.univtable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.List;

import crawl.Crawler;
import crawl.DonggukCrawler;
import crawl.SogangCrawler;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<UnivItem> spinList = new ArrayList<>();
    private SpinnerAdapter sAdapter;

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    private Spinner _spinner;
    private Button _login;
    private EditText _id, _pw;

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bt_signin:
                signIn(_id.getText().toString(), _pw.getText().toString());
                break;
            default: break;
        }
    }

    public void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void signIn(String id, String pw){
        final UnivItem mData = spinList.get(_spinner.getSelectedItemPosition());
        switch (mData.ucode){
            case Crawler.UCODE_DONGGUK: case Crawler.UCODE_DONGGUK_IL: case Crawler.UCODE_DONGGUK_GY:
                if(id.length() != Crawler.LENGTH_DONGGUK) {
                    showToast( mData.uname + "는 " + Crawler.LENGTH_DONGGUK + "자리 학번을 사용합니다");
                    return;
                }
                break;
            case Crawler.UCODE_SOGANG:
                if(id.length() != Crawler.LENGTH_SOGANG) {
                    showToast( mData.uname + "는 " + Crawler.LENGTH_SOGANG + "자리 학번을 사용합니다");
                    return;
                }
                break;
            case Crawler.UCODE_KOOKMIN:
                if(id.length() != Crawler.LENGTH_KOOKMIN) {
                    showToast( mData.uname + "는 " + Crawler.LENGTH_KOOKMIN + "자리 학번을 사용합니다");
                    return;
                }
                break;
            default: return;
        }
        if(pw.length() < 1){
            showToast("패스워드를 입력하세요");
            return;
        }
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
        prefEditor.putInt("ucode", spinList.get(_spinner.getSelectedItemPosition()).ucode);
        prefEditor.commit();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void initView(){
        _login = (Button)findViewById(R.id.bt_signin);
        _id = (EditText)findViewById(R.id.idp);
        _pw = (EditText)findViewById(R.id.pwp);
        _login.setOnClickListener(this);

        sAdapter = new SpinnerAdapter(this, spinList, R.layout.item_layout, R.layout.item_layout2);

        _spinner = (Spinner)findViewById(R.id.spinner);
        _spinner.setAdapter(sAdapter);

        spinList.clear();

        spinList.add(new UnivItem(Crawler.UCODE_KOOKMIN, "국민대학교(서울)"));
        spinList.add(new UnivItem(Crawler.UCODE_DONGGUK_GY, "동국대학교(경주)"));
        spinList.add(new UnivItem(Crawler.UCODE_DONGGUK, "동국대학교(서울)"));
        spinList.add(new UnivItem(Crawler.UCODE_DONGGUK_IL, "동국대학교(일산)"));
        spinList.add(new UnivItem(Crawler.UCODE_SOGANG, "서강대학교(서울)"));

        sAdapter.notifyDataSetChanged();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getSharedPreferences("Univtable", MODE_PRIVATE);
        prefEditor = pref.edit();

        initView();
    }

}