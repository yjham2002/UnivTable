package crawl;

import com.dgu.table.univ.univtable.ClassInfo;

import java.util.ArrayList;

import util.SFCallback;

public class KookminCrawler extends Crawler{

    public ArrayList<ClassInfo> classList = new ArrayList<>();

    /**
     * KOOKMIN UNIVERSITY
     * */
    //ID = "20145160";
    //PW = "qkrtkddl2#";
    public KookminCrawler(final String userId, final String userPw, SFCallback onStart, SFCallback onConnect, SFCallback onFinish, SFCallback onFail){
        classList.clear();
        URL_AUTH = "http://ktis.kookmin.ac.kr/kmu/com.Login.do?";
        URL_TIME = "http://ktis.kookmin.ac.kr/kmu/ucb.Ucb0164rAGet01.do";
        URL_HOME = "http://ktis.kookmin.ac.kr/kmu/usa.Usa0209eFGet01.do";
        URL_HAND = "#";
        FORM_ID = "txt_user_id";
        FORM_PW = "txt_passwd";
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
