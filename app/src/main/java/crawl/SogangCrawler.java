package crawl;

import com.dgu.table.univ.univtable.ClassInfo;

import java.util.ArrayList;

import util.SFCallback;

public class SogangCrawler extends Crawler{

    /**
     * SOGANG UNIVERSITY
     * */
    //ID = "20131014";
    //PW = "wjsgytjd5+";
    public SogangCrawler(final String userId, final String userPw, SFCallback onStart, SFCallback onConnect, SFCallback onFinish){
        classList.clear();
        URL_AUTH = "https://eclass.sogang.ac.kr/ilos/lo/login.acl";
        URL_TIME = "http://eclass.sogang.ac.kr/ilos/st/main/pop_academic_timetable_form.acl";
        URL_HOME = "http://eclass.dongduk.ac.kr/ilos/m/main/main_form.acl";
        URL_HAND = "http://eclass.sogang.ac.kr/ilos/m/st/report_list_form.acl";
        FORM_ID = "usr_id";
        FORM_PW = "usr_pwd";
        ID = userId;
        PW = userPw;
        this.onStart = onStart;
        this.onConnect = onConnect;
        this.onFinish = onFinish;
        this.onFail = onFail;
    }

    @Override
    protected String doInBackground(Void... params){
        return null;
    }
    @Override
    protected void onPostExecute(String result){

    }
}
