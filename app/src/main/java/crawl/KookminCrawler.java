package crawl;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class KookminCrawler extends Crawler{

    /**
     * KOOKMIN UNIVERSITY
     * */
    //ID = "20145160";
    //PW = "qkrtkddl2#";
    public KookminCrawler(final String userId, final String userPw){
        classList.clear();
        URL_AUTH = "http://ktis.kookmin.ac.kr/kmu/com.Login.do?";
        URL_TIME = "http://yjham2002.woobi.co.kr/test.html";
        //URL_TIME = "http://ktis.kookmin.ac.kr/kmu/ucb.Ucb0164rAGet01.do";
        URL_HOME = "http://ktis.kookmin.ac.kr/kmu/usa.Usa0209eFGet01.do";
        URL_HAND = "#";
        FORM_ID = "txt_user_id";
        FORM_PW = "txt_passwd";
        ID = userId;
        PW = userPw;
        CSS = "table>tbody>tr>td[colspan=2]";
    }

    @Override
    public void getTimetable(final Handler mHandler) {
        classList.clear();
        handList.clear();
        Thread t = new Thread() {
            public void run() {
                try {
                    Connection.Response response = Jsoup.connect(URL_AUTH)
                            .followRedirects(true)
                            .data(FORM_ID, ID)
                            .data(FORM_PW, PW)
                            .method(Connection.Method.POST)
                            .timeout(TIMEOUT)
                            .execute();

                    document = Jsoup.connect(URL_TIME)
                            .followRedirects(true)
                            //.cookies(response.cookies())
                            .method(Connection.Method.POST)
                            .timeout(TIMEOUT)
                            .post();

                    Elements table = document.select("table[width=100%]");
                    Log.e("KOOKMIN", table.html().toString());

                    //테이블 전체 내용 로그
                    ClassInfo ClistA[][] = new ClassInfo[15][7];      //ShortTime용 배열
                    ClassInfo ClistB[][] = new ClassInfo[11][7];      //LongTime용 배열

                    int lineIndicator = 0;

                    for (Element trTags : table.select("tr")) {        //tr 단위로 읽는다
                        int actualLine = -1;
                        String rawtimeShort = "";
                        String rawtimeLong = "";
                        String startRawtimeShort = "";
                        String startRawtimeLong = "";
                        String endRawtimeShort = "";
                        String endRawtimeLong = "";

                        int swungDashIndicatorShort = -1;
                        int swungDashIndicatorLong = -1;

                        if (trTags.child(0).text().contains("학년도")) {    //같은 클래스명 가진 테이블 건너뛰기
                            continue;
                        }
                        if (trTags.attr("class").equals("table_header_center")) {     //요일 행 건너뛰기
                            continue;
                        }
                        if (lineIndicator == 0) {               //first line: 안읽도록 처리하기로 함
                            lineIndicator++;
                            continue;
                        } else if (lineIndicator % 6 == 1 || lineIndicator == 25) {        //type 1: tr에 short, long 둘 다 있는 타입
                            rawtimeShort = trTags.child(1).text();
                            rawtimeLong = trTags.child(2).text();
                            swungDashIndicatorShort = rawtimeShort.indexOf("~");
                            swungDashIndicatorLong = rawtimeLong.indexOf("~");
                            startRawtimeShort = rawtimeShort.substring(swungDashIndicatorShort - 5, swungDashIndicatorShort);
                            startRawtimeLong = rawtimeLong.substring(swungDashIndicatorLong - 5, swungDashIndicatorLong);
                            endRawtimeShort = rawtimeShort.substring(swungDashIndicatorShort + 1, rawtimeShort.length());
                            endRawtimeLong = rawtimeLong.substring(swungDashIndicatorLong + 1, rawtimeLong.length());

                            for (int i = 3; i <= 14; i++) {     //필요한 모든 td 순회
                                if (!trTags.child(i).text().equals(" ") && trTags.child(i).text().length() > 1) {
                                    ClassInfo tmpClass = new ClassInfo();     // 하나씩 삽입될 개체
                                    if (i % 2 == 1) {     //rawtimeShort 사용
                                        //actualLine=Integer.parseInt(rawtimeShort.substring(1, 3));
                                        String tmpline = rawtimeShort.substring(1, 3);
                                        if (tmpline.contains(" ")) actualLine = Integer.parseInt(tmpline.substring(0, 1));
                                        else actualLine = Integer.parseInt(tmpline);
                                        //System.out.println("actual Line:"+actualLine);        //실제 저장될 배열 줄 위치
                                        tmpClass.weekDay = i / 2;
                                        tmpClass.categorizeKookminClass(trTags.child(i).text());
                                        ClistA[actualLine][i / 2] = tmpClass;
                                        tmpClass.insertTime(startRawtimeShort, endRawtimeShort);

                                    } else if (i % 2 == 0) {      //rawtimdLong 사용
                                        String tmpline = rawtimeLong.substring(1, 2);
                                        String orderTable = "ABCDEFGHI";
                                        actualLine = orderTable.indexOf(tmpline) + 1;

                                        tmpClass.weekDay = i / 2 - 1;
                                        tmpClass.categorizeKookminClass(trTags.child(i).text());
                                        ClistB[actualLine][i / 2 - 1] = tmpClass;
                                        tmpClass.insertTime(startRawtimeLong, endRawtimeLong);

                                        System.out.println("ClistB[" + (actualLine) + "][" + (i / 2) + "] : " +
                                                "[" + ClistB[actualLine][i / 2].title + ClistB[actualLine][i / 2].location + ClistB[actualLine][i / 2].weekDay + "]");
                                        //배열 삽입시 로그
                                    }
                                }
                                //System.out.println("skip");
                            }

                        } else if (lineIndicator % 2 == 1) { //left type
                            rawtimeShort = trTags.child(1).text();
                            swungDashIndicatorShort = rawtimeShort.indexOf("~");
                            startRawtimeShort = rawtimeShort.substring(swungDashIndicatorShort - 5, swungDashIndicatorShort);
                            endRawtimeShort = rawtimeShort.substring(swungDashIndicatorShort + 1, rawtimeShort.length());
                            String tmpline = rawtimeShort.substring(1, 3);
                            if (tmpline.contains(" "))
                                actualLine = Integer.parseInt(tmpline.substring(0, 1));
                            else actualLine = Integer.parseInt(tmpline);
                            for (int i = 2; i <= 7; i++) {
                                if (trTags.child(i).text().length() > 1 && !trTags.child(i).text().equals(" ")) {
                                    ClassInfo tmpClass = new ClassInfo();
                                    tmpClass.weekDay = i - 1;
                                    tmpClass.categorizeKookminClass(trTags.child(i).text());
                                    ClistA[actualLine][i - 1] = tmpClass;
                                    tmpClass.insertTime(startRawtimeShort, endRawtimeShort);
                                }
                            }
                        } else if (lineIndicator == 28) {     //맨 마지막시간 처리(필요 없음)
                            System.out.println("do nothing");
                        } else if (lineIndicator % 6 == 4 || lineIndicator == 27) {        //right type
                            rawtimeLong = trTags.child(1).text();
                            swungDashIndicatorLong = rawtimeLong.indexOf("~");
                            startRawtimeLong = rawtimeLong.substring(swungDashIndicatorLong - 5, swungDashIndicatorLong);
                            endRawtimeLong = rawtimeLong.substring(swungDashIndicatorLong + 1, rawtimeLong.length());
                            //System.out.println(startRawtimeLong+endRawtimeLong);
                            String tmpline = rawtimeLong.substring(1, 2);
                            String orderTable = "ABCDEFGHI";
                            actualLine = orderTable.indexOf(tmpline) + 1;
                            for (int i = 2; i <= 7; i++) {
                                if (trTags.child(i).text().length() > 1 && !trTags.child(i).text().equals(" ")) {
                                    ClassInfo tmpClass = new ClassInfo();
                                    tmpClass.weekDay = i - 1;
                                    tmpClass.categorizeKookminClass(trTags.child(i).text());
                                    ClistB[actualLine][i - 1] = tmpClass;
                                    tmpClass.insertTime(startRawtimeLong, endRawtimeLong);
                                }
                            }
                        } else {                               //none type

                        }
                        lineIndicator++;
                    }
                    //배열 로그
                    for (int i = 0; i < ClistA[0].length; i++)
                        for (int j = 0; j < ClistA.length; j++) if (ClistA[j][i] != null) System.out.println("[" + j + "," + i + ":" + ClistA[j][i].title + "]");

                    categorizeTimeTable(ClistA);
                    categorizeTimeTable(ClistB);

                    for(ClassInfo c : classList){
                        Log.e("classList", c.title + " / " + classList.size());
                    }

                    Message msg = mHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }catch (IOException e){}
            }
        };
        t.start();
    }

    private void categorizeTimeTable(ClassInfo Clist[][]){
        String tmpTitle="";
        int tmpStarthMin = 30;
        int tmpStartm = -1;
        int tmpEndhMax = -1;
        int tmpEndm = -1;
        System.out.println("Clist[0].length: "+Clist[0].length);
        System.out.println("Clist.length: "+Clist.length);

        for(int i = 1; i < Clist[0].length; i++){
            for(int j = 0; j < Clist.length; j++){
                if(Clist[j][i]!=null){
                    if(!tmpTitle.equals(Clist[j][i].title)){
                        tmpTitle=Clist[j][i].title;
                        System.out.println("class:"+tmpTitle);
                        System.out.println("["+j+","+i+"]");
                    }

                    if(tmpTitle.equals(Clist[j][i].title)){
                        System.out.println(tmpStarthMin + "," + tmpStartm + "," + tmpEndhMax + "," + tmpEndm);
                        System.out.println("Clis: "+Clist[j][i].startHour+","+Clist[j][i].startMin+","+Clist[j][i].endHour+","+Clist[j][i].endMin);
                        if(tmpStarthMin>Clist[j][i].startHour){
                            tmpStarthMin=Clist[j][i].startHour;
                            tmpStartm=Clist[j][i].startMin;
                        }

                        if(tmpEndhMax<=Clist[j][i].endHour){
                            tmpEndhMax=Clist[j][i].endHour;
                            tmpEndm=Clist[j][i].endMin;
                        }
                        System.out.println("["+j+","+i+"]"+"1");
                        System.out.println(tmpStarthMin+","+tmpStartm+","+tmpEndhMax+","+tmpEndm);
                    }

                    if(Clist[j+1][i] == null || !tmpTitle.equals(Clist[j+1][i].title)){
                        classList.add(new ClassInfo(Clist[j][i].title, Clist[j][i].location, Clist[j][i].rawtime,Clist[j][i].weekDay, tmpStarthMin, tmpStartm, tmpEndhMax, tmpEndm));
                        tmpStarthMin = 30;
                        tmpStartm=-1;
                        tmpEndhMax=-1;
                        tmpEndm=-1;
                        // System.out.println("["+j+","+i+"]");
                    }

                    if(j == Clist.length - 1 && tmpTitle.equals(Clist[j][i])){
                        classList.add(new ClassInfo(Clist[j][i].title, Clist[j][i].location, Clist[j][i].rawtime,Clist[j][i].weekDay, tmpStarthMin, tmpStartm, tmpEndhMax, tmpEndm));
                        //System.out.println("["+j+","+i+"]");
                        tmpStarthMin = 30;
                        tmpStartm=-1;
                        tmpEndhMax=-1;
                        tmpEndm=-1;
                    }

                }
            }
        }
        //classList 로그
        for(int i=0;i<classList.size();i++){
            Log.e("KOOKMIN"," ["+classList.get(i).title+"] ["+classList.get(i).location+"] ["+classList.get(i).weekDay+"] ["+classList.get(i).startHour+":"+classList.get(i).startMin+"] ["+classList.get(i).endHour+":"+classList.get(i).endMin+"]");
        }
    }

}
