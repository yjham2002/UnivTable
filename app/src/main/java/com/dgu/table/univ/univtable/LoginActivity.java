package com.dgu.table.univ.univtable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import crawl.Crawler;
import crawl.DonggukCrawler;
import crawl.KookminCrawler;
import crawl.SogangCrawler;
import util.Communicator;
import util.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Crawler crawler;
    private String geocode = "서울";

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

    public void signIn(final String id, final String pw){
        final ProgressDialog pdial = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        pdial.setMessage("로그인하는 중...");
        pdial.setCancelable(false);
        pdial.show();

        _login.setEnabled(false);

        final UnivItem mData = spinList.get(_spinner.getSelectedItemPosition());

        switch (mData.ucode){
            case Crawler.UCODE_DONGGUK:
                crawler = new DonggukCrawler(id, pw);
                geocode = Crawler.GEO_SEOUL;
                if(id.length() != Crawler.LENGTH_DONGGUK) {
                    showToast( mData.uname + "는 " + Crawler.LENGTH_DONGGUK + "자리 학번을 사용합니다");
                    _login.setEnabled(true);
                    pdial.dismiss();
                    return;
                }
                break;
            case Crawler.UCODE_DONGGUK_GY:
                crawler = new DonggukCrawler(id, pw);
                geocode = Crawler.GEO_GYEONGJU;
                if(id.length() != Crawler.LENGTH_DONGGUK) {
                    showToast( mData.uname + "는 " + Crawler.LENGTH_DONGGUK + "자리 학번을 사용합니다");
                    _login.setEnabled(true);
                    pdial.dismiss();
                    return;
                }
                break;
            case Crawler.UCODE_DONGGUK_IL:
                crawler = new DonggukCrawler(id, pw);
                geocode = Crawler.GEO_ILSAN;
                if(id.length() != Crawler.LENGTH_DONGGUK) {
                    showToast( mData.uname + "는 " + Crawler.LENGTH_DONGGUK + "자리 학번을 사용합니다");
                    _login.setEnabled(true);
                    pdial.dismiss();
                    return;
                }
                break;
            case Crawler.UCODE_SOGANG:
                geocode = Crawler.GEO_SEOUL;
                crawler = new SogangCrawler(id, pw);
                if(id.length() != Crawler.LENGTH_SOGANG) {
                    showToast( mData.uname + "는 " + Crawler.LENGTH_SOGANG + "자리 학번을 사용합니다");
                    _login.setEnabled(true);
                    pdial.dismiss();
                    return;
                }
                break;
            case Crawler.UCODE_KOOKMIN:
                crawler = new KookminCrawler(id, pw);
                geocode = Crawler.GEO_SEOUL;
                if(id.length() != Crawler.LENGTH_KOOKMIN) {
                    showToast( mData.uname + "는 " + Crawler.LENGTH_KOOKMIN + "자리 학번을 사용합니다");
                    _login.setEnabled(true);
                    pdial.dismiss();
                    return;
                }
                break;
            default:
                _login.setEnabled(true);
                pdial.dismiss();
                return;
        }
        if(pw.length() < 1){
            showToast("패스워드를 입력하세요");
            _login.setEnabled(true);
            pdial.dismiss();
            return;
        }

        crawler.verify(new Handler(){
            @Override
            public void handleMessage(Message msg){
                Log.e("Handle", msg.getData().toString());
                if(msg.getData().getBoolean("result")){
                    prefEditor.putBoolean("auto", true);
                    prefEditor.putString("geocode", geocode);
                    prefEditor.putInt("selection", _spinner.getSelectedItemPosition());
                    prefEditor.putString("id", msg.getData().getString("id"));
                    prefEditor.putString("pw", msg.getData().getString("pw"));
                    prefEditor.putString("name", msg.getData().getString("name"));
                    prefEditor.putInt("ucode", spinList.get(_spinner.getSelectedItemPosition()).ucode);
                    prefEditor.commit();
                    HashMap<String, String> dataSet = new HashMap<>();
                    dataSet.put("uid", Integer.toString(pref.getInt("ucode", 0)));
                    dataSet.put("stuid", msg.getData().getString("id"));
                    dataSet.put("Name", msg.getData().getString("name"));
                    new Communicator().postHttp(URL.MAIN + URL.REST_USER_NEW, dataSet, new Handler(){
                        @Override
                        public void handleMessage(Message msg){
                            String jsonString = msg.getData().getString("jsonString");
                            try {
                                JSONArray json_arr = new JSONArray(jsonString);
                                JSONObject json_list = json_arr.getJSONObject(0);
                                Log.e("JSON", msg.getData().getString("jsonString"));
                                prefEditor.putInt("mid", json_list.getInt("id"));
                                prefEditor.commit();
                            }catch (JSONException e){
                                prefEditor.putBoolean("auto", false);
                                prefEditor.putString("id", "#");
                                prefEditor.putString("pw", "#");
                                prefEditor.putString("name", "#");
                                prefEditor.commit();
                                showToast("로그인에 실패하였습니다");
                                _login.setEnabled(true);
                                pdial.dismiss();
                                e.printStackTrace();
                                return;
                            }
                            if(pref.getInt("mid", 0) == 0){
                                prefEditor.putBoolean("auto", false);
                                prefEditor.putString("id", "#");
                                prefEditor.putString("pw", "#");
                                prefEditor.putString("name", "#");
                                prefEditor.commit();
                                showToast("로그인에 실패하였습니다");
                                _login.setEnabled(true);
                                pdial.dismiss();
                                return;
                            }else{
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                pdial.dismiss();
                                _login.setEnabled(true);
                                finish();
                            }
                        }
                    });

                }else{
                    prefEditor.putBoolean("auto", false);
                    prefEditor.putString("id", "#");
                    prefEditor.putString("pw", "#");
                    prefEditor.putString("name", "#");
                    prefEditor.commit();
                    showToast("로그인에 실패하였습니다");
                    _login.setEnabled(true);
                    pdial.dismiss();
                    return;
                }
            }
        });

    }

    public void initView(){
        _login = (Button)findViewById(R.id.bt_signin);
        _id = (EditText)findViewById(R.id.idp);
        _pw = (EditText)findViewById(R.id.pwp);
        _pw.setTransformationMethod(new PasswordTransfromMethod());
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

        if(pref.getBoolean("auto", false)){
            _spinner.setSelection(pref.getInt("selection", 0));
            signIn(pref.getString("id", "#"), pref.getString("pw", "#"));
        }
    }

    public class PasswordTransfromMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source;
            }
            public char charAt(int index) {
                return '●';
            }
            public int length() {
                return mSource.length();
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };

}