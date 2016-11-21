package crawl;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class KookminCrawler extends Crawler{

    public ArrayList<ClassInfo> classList = new ArrayList<>();

    /**
     * KOOKMIN UNIVERSITY
     * */
    //ID = "20145160";
    //PW = "qkrtkddl2#";
    public KookminCrawler(final String userId, final String userPw){
        classList.clear();
        URL_AUTH = "http://ktis.kookmin.ac.kr/kmu/com.Login.do?";
        URL_TIME = "http://ktis.kookmin.ac.kr/kmu/ucb.Ucb0164rAGet01.do";
        URL_HOME = "http://ktis.kookmin.ac.kr/kmu/usa.Usa0209eFGet01.do";
        URL_HAND = "#";
        FORM_ID = "txt_user_id";
        FORM_PW = "txt_passwd";
        ID = userId;
        PW = userPw;
        CSS = "table>tbody>tr>td[colspan=2]";
    }

    @Override
    public void getDailyTime(){

    }
    @Override
    public void getTimetable(){

    }

}
