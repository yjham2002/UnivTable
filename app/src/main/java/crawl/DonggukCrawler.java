package crawl;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class DonggukCrawler extends Crawler {

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
        trashValue="시간/월화수목금토";
    }

    @Override
    public void getTimetable(final Handler mHandler){
        Thread t = new Thread() {
            public void run() {
                Connection.Response response = Jsoup.connect(URL_AUTH)
                        .followRedirects(true)
                        .data(FORM_ID, ID)
                        .data(FORM_PW, PW)
                        .method(Connection.Method.POST)
                        .timeout(TIMEOUT)
                        .execute();

                document = Jsoup.connect(URL_HOME)
                        .followRedirects(true)
                        .cookies(response.cookies())
                        .followRedirects(true)
                        .method(Connection.Method.POST)
                        .timeout(TIMEOUT)
                        .post();
                Elements name = document.select("span.user");
                userName = name.text();
                //System.out.println(userName);
                //사용자 이름 로그
                document = Jsoup.connect(URL_HAND)
                        .followRedirects(true)
                        .cookies(response.cookies())
                        .followRedirects(true)
                        .method(Connection.Method.POST)
                        .timeout(TIMEOUT)
                        .post();
                Elements hwTable = document.select("table.list-table");
                for (Element trTags : hwTable.select("tr")) {
                    handList.add(new HandInfo(trTags.child(0).text(), trTags.child(1).text(), trTags.child(2).text(),
                            trTags.child(3).text(), trTags.child(4).text(), trTags.child(5).text(), trTags.child(6).text()));
                }

                document = Jsoup.connect(URL_TIME)
                        .followRedirects(true)
                        .cookies(response.cookies())
                        .followRedirects(true)
                        .method(Connection.Method.POST)
                        .timeout(TIMEOUT)
                        .post();
                Elements table = document.select("table.bbs-table01");

                ClassInfo Clist[][] = new ClassInfo[32][6];
                String tmpString;
                int middleIndex = -1;
                int lineIndicator = -1;
                String startRawtime = "";
                String endRawtime = "";
                int swungDashIndicator;
                int ScolonIndicator = -1;
                int EcolonIndicator = -1;

                for (Element trTags : table.select("tr")) {
                    String rawtime = trTags.child(0).text();

                    if (!trashValue.contains(trTags.child(0).text())) {
                        //System.out.println("rawTime: "+rawtime);
                        //rawtime 로그
                        swungDashIndicator = rawtime.indexOf("~");
                        startRawtime = rawtime.substring(0, swungDashIndicator - 1);
                        endRawtime = rawtime.substring(swungDashIndicator + 2, rawtime.length());
                        ScolonIndicator = startRawtime.indexOf(":");
                        EcolonIndicator = endRawtime.indexOf(":");
                    }

                    for (int i = 1; i < 7; i++) {
                        if (!trashValue.contains(trTags.child(i).text())) {
                            tmpString = trTags.child(i).text();
                            //System.out.println("[tmpString:" + tmpString + "]");
                            middleIndex = tmpString.indexOf("(");
                            ClassInfo tmpClass = new ClassInfo();
                            tmpClass.startHour = Integer.parseInt(startRawtime.substring(0, ScolonIndicator));
                            tmpClass.startMin = Integer.parseInt(startRawtime.substring(ScolonIndicator + 1, startRawtime.length()));
                            tmpClass.endHour = Integer.parseInt(endRawtime.substring(0, EcolonIndicator));
                            tmpClass.endMin = Integer.parseInt(endRawtime.substring(EcolonIndicator + 1, endRawtime.length()));
                            //System.out.println("time: "+tmpClass.startHour+":"+tmpClass.startMin+"  "+tmpClass.endHour+":"+tmpClass.endMin);
                            //시간 결과값 로그
                            if (middleIndex != -1) {
                                tmpClass.title = tmpString.substring(0, middleIndex - 1);
                                tmpClass.location = tmpString.substring(middleIndex, tmpString.length());
                            }
                            switch (i) {
                                case 1:
                                    tmpClass.weekDay = 0;
                                    break;
                                case 2:
                                    tmpClass.weekDay = 1;
                                    break;
                                case 3:
                                    tmpClass.weekDay = 2;
                                    break;
                                case 4:
                                    tmpClass.weekDay = 3;
                                    break;
                                case 5:
                                    tmpClass.weekDay = 4;
                                    break;
                                case 6:
                                    tmpClass.weekDay = 5;
                                    break;
                            }
                            Clist[lineIndicator][i - 1] = tmpClass;
                        }
                    }
                    lineIndicator++;
                }

                String tmpTitle = "";
                int tmpStarthMin = 30;
                int tmpStartm = -1;
                int tmpEndhMax = -1;
                int tmpEndm = -1;

                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 32; j++) {
                        if (Clist[j][i].title != "" && !tmpTitle.equals(Clist[j][i].title)) {
                            tmpTitle = Clist[j][i].title;
                        }

                        if (Clist[j][i].title != "" && tmpTitle.equals(Clist[j][i].title)) {
                            if (tmpStarthMin > Clist[j][i].startHour) {
                                tmpStarthMin = Clist[j][i].startHour;
                                tmpStartm = Clist[j][i].startMin;
                            }
                            if (tmpEndhMax <= Clist[j][i].endHour) {
                                tmpEndhMax = Clist[j][i].endHour;
                                tmpEndm = Clist[j][i].endMin;
                            }
                        }

                        if (Clist[j][i].title != "" && !tmpTitle.equals(Clist[j + 1][i].title)) {
                            classList.add(new ClassInfo(Clist[j][i].title, Clist[j][i].location, Clist[j][i].rawtime, Clist[j][i].weekDay, tmpStarthMin, tmpStartm, tmpEndhMax, tmpEndm));
                            tmpStarthMin = 30;
                            tmpStartm = -1;
                            tmpEndhMax = -1;
                            tmpEndm = -1;
                        }
                    }
                    tmpStarthMin = 30;
                    tmpStartm = -1;
                    tmpEndhMax = -1;
                    tmpEndm = -1;
                }

                //classList 로그
                for (int i = 0; i < classList.size(); i++) {
                    System.out.println(" [" + classList.get(i).title + "] [" + classList.get(i).location + "] [" + classList.get(i).weekDay + "] [" + classList.get(i).startHour + ":" + classList.get(i).startMin + "] [" + classList.get(i).endHour + ":" + classList.get(i).endMin + "]");
                }

                Message msg = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        };
        t.start();
    }


}
