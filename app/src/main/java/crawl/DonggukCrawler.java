package crawl;

import util.SFCallback;

public class DonggukCrawler extends Crawler{
    /**
     * DONGGUK UNIVERSITY
     * */
    //ID = "";
    //PW = "";
    public DonggukCrawler(final String userId, final String userPw, SFCallback onStart, SFCallback onConnect, SFCallback onFinish){
        URL_AUTH = "https://eclass.dongguk.edu/User.do?cmd=loginUser";
        URL_TIME = "https://eclass.dongguk.edu/Schedule.do?cmd=viewLessonSchedule";
        URL_HOME = "https://eclass.dongguk.edu/Main.do?cmd=viewEclassMain";
        URL_HAND = "https://eclass.dongguk.edu/Report.do?cmd=viewMainReportListLearner";
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
