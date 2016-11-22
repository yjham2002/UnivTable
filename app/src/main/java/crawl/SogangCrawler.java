package crawl;

import android.os.Handler;

import java.io.IOException;

public class SogangCrawler extends Crawler{

    /**
     * SOGANG UNIVERSITY
     * */
    //ID = "20131014";
    //PW = "wjsgytjd5+";
    public SogangCrawler(final String userId, final String userPw){
        classList.clear();
        URL_AUTH = "https://eclass.sogang.ac.kr/ilos/lo/login.acl";
        URL_TIME = "http://eclass.sogang.ac.kr/ilos/st/main/pop_academic_timetable_form.acl";
        URL_HOME = "http://eclass.sogang.ac.kr/ilos/main/main_form.acl";
        URL_HAND = "http://eclass.sogang.ac.kr/ilos/m/st/report_list_form.acl";
        FORM_ID = "usr_id";
        FORM_PW = "usr_pwd";
        ID = userId;
        PW = userPw;
        CSS = "strong[id=user]";
    }

    @Override
    public void getTimetable(final Handler mHandler) {

    }

}
