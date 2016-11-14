package crawl;

import java.util.ArrayList;

import util.SFCallback;

public class DonggukCrawler extends Crawler {

    public ArrayList<ClassInfo> classList = new ArrayList<>();

    /**
     * DONGGUK UNIVERSITY
     */
    //ID = "";
    //PW = "";
    public DonggukCrawler(final String userId, final String userPw, SFCallback onStart, SFCallback onConnect, SFCallback onFinish) {
        classList.clear();
        URL_AUTH = "https://eclass.dongguk.edu/User.do?cmd=loginUser"; // Login Link
        URL_TIME = "https://eclass.dongguk.edu/Schedule.do?cmd=viewLessonSchedule"; // Timetable Link
        URL_HOME = "https://eclass.dongguk.edu/Main.do?cmd=viewEclassMain"; // Link for retrieving user Name
        URL_HAND = "https://eclass.dongguk.edu/Report.do?cmd=viewMainReportListLearner"; // Link for retrieving handin list
        FORM_ID = "userDTO.userId";
        FORM_PW = "userDTO.password";
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
        onFinish.callback();
    }

}
