package crawl;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class DonggukCrawler extends Crawler {

    public ArrayList<ClassInfo> classList = new ArrayList<>();

    /**
     * DONGGUK UNIVERSITY
     */
    //ID = "";
    //PW = "";
    public DonggukCrawler(final String userId, final String userPw) {
        classList.clear();
        URL_AUTH = "https://eclass.dongguk.edu/User.do?cmd=loginUser"; // Login Link
        URL_TIME = "https://eclass.dongguk.edu/Schedule.do?cmd=viewLessonSchedule"; // Timetable Link
        URL_HOME = "https://eclass.dongguk.edu/Main.do?cmd=viewEclassMain"; // Link for retrieving user Name
        URL_HAND = "https://eclass.dongguk.edu/Report.do?cmd=viewMainReportListLearner"; // Link for retrieving handin list
        FORM_ID = "userDTO.userId";
        FORM_PW = "userDTO.password";
        ID = userId;
        PW = userPw;
        CSS = "SPAN>strong";
    }

    @Override
    public void getDailyTime(){

    }
    @Override
    public void getTimetable(){

    }

}
