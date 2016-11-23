package crawl;

import android.os.Handler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        classList.clear();
        handList.clear();
        Thread t = new Thread() {
            public void run() {
                try {
                    Connection.Response response= Jsoup.connect(URL_AUTH)
                            .followRedirects(true)
                            .data(FORM_ID, ID)
                            .data(FORM_PW, PW)
                            .method(Connection.Method.POST)
                            .timeout(TIMEOUT)
                            .execute();

                    document= Jsoup.connect(URL_HOME)
                            .followRedirects(true)
                            .cookies(response.cookies())
                            .followRedirects(true)
                            .method(Connection.Method.POST)
                            .timeout(TIMEOUT)
                            .post();
                    Elements name=document.select("strong.site-font-color");
                    userName=name.text();
                    System.out.println("name: "+userName);

                    document=Jsoup.connect(URL_TIME)
                            .followRedirects(true)
                            .cookies(response.cookies())
                            .followRedirects(true)
                            .method(Connection.Method.POST)
                            .timeout(TIMEOUT)
                            .post();
                    Elements table=document.select("table.questionlist");
                    // System.out.println(table.text());

                    String Clist[][]=new String[56][8];
                    int lineIndicator=-1;

                    for(Element trTags: table.select("tr")){
                        if(lineIndicator%4 == 0) {
                            for (int i = 1; i < 8; i++) {
                                if (!trashValue.contains(trTags.child(i).text())) {
                                    // System.out.println("[" + trTags.child(i).text() + "]");
                                    Clist[lineIndicator][i-1] = trTags.child(i).text();
                                }
                            }
                        }
                        else{
                            for(int i = 0; i < 7; i++){
                                if (!trashValue.contains(trTags.child(i).text())) {
                                    Clist[lineIndicator][i] = trTags.child(i).text();
                                }
                            }
                        }

                        lineIndicator++;
                    }

                    //Clist 로그
                    for(int i=0;i<56;i++){
                        for(int j=0;j<7;j++){
                            System.out.printf("[%s]", Clist[i][j]);
                        }
                        System.out.println();
                    }

                    for(int i=0;i<7;i++){
                        for(int j=0;j<56;j++){
                            if(!Clist[j][i].equals(" ")){
                                System.out.println("title:"+Clist[j][i]);
                                ClassInfo tmpclass=new ClassInfo();
                                int TLindicator;
                                String tmpStime;
                                int timeIndicator;
                                int sIndicator;
                                String tmpEtime;
                                int eIndicator;
                                String tmpRawtime;
                                String tmpRawTL;

                                tmpclass.title=Clist[j][i];
                                tmpRawTL=Clist[j+2][i];
                                TLindicator=tmpRawTL.indexOf("(");
                                tmpRawtime=tmpRawTL.substring(0, TLindicator);
                                tmpclass.location=tmpRawTL.substring(TLindicator, tmpRawTL.length());
                                tmpclass.weekDay=i+1;
                                timeIndicator=tmpRawtime.indexOf("~");
                                tmpStime=tmpRawtime.substring(0, timeIndicator);
                                tmpEtime=tmpRawtime.substring(timeIndicator+1, tmpRawtime.length());
                                sIndicator=tmpStime.indexOf(":");
                                eIndicator=tmpEtime.indexOf(":");
                                tmpclass.startHour=Integer.parseInt(tmpStime.substring(0, sIndicator));
                                tmpclass.startMin=Integer.parseInt(tmpStime.substring(sIndicator+1, tmpStime.length()));
                                tmpclass.endHour=Integer.parseInt(tmpEtime.substring(0, eIndicator));
                                tmpclass.endMin=Integer.parseInt(tmpEtime.substring(eIndicator+1, tmpEtime.length()));
                                classList.add(tmpclass);
                                j+=2;
                            }
                        }
                    }

                    System.out.println("end of sogangCrawler");
                }catch (IOException e){}
            }
        };
        t.start();
    }

}
