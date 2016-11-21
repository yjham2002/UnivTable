package crawl;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public abstract class Crawler {

    public static final String GEO_SEOUL = "서울";
    public static final String GEO_ILSAN = "일산";
    public static final String GEO_GYEONGJU = "경주";

    public static final int UCODE_DONGGUK = 1;
    public static final int UCODE_KOOKMIN = 4;
    public static final int UCODE_SOGANG = 3;
    public static final int UCODE_DONGGUK_IL = 2;
    public static final int UCODE_DONGGUK_GY = 5;

    public static final int LENGTH_DONGGUK = 10;
    public static final int LENGTH_SOGANG = 8;
    public static final int LENGTH_KOOKMIN = 8;

    protected String userName = "";
    protected ArrayList<ClassInfo> classList = new ArrayList<>();
    protected ArrayList<HandInfo> handList = new ArrayList<>();

    protected Document document;

    protected String URL_AUTH;
    protected String URL_TIME;
    protected String URL_HOME;
    protected String URL_HAND;
    protected String FORM_ID;
    protected String FORM_PW;
    protected String CSS;
    protected String ID;
    protected String PW;

    public String getAuth(){
        return URL_AUTH;
    }

    public void verify(final Handler h){
        Thread t = new Thread() {
            public void run() {
                boolean result = false;

                try{
                    Connection.Response res = Jsoup.connect(URL_AUTH)
                            .followRedirects(true)
                            .data(FORM_ID, ID)
                            .data(FORM_PW, PW)
                            .method(Connection.Method.POST)
                            .timeout(TIMEOUT)
                            .execute();
                    document = Jsoup.connect(URL_HOME)
                            .followRedirects(true)
                            .cookies(res.cookies())
                            .timeout(TIMEOUT)
                            .get();
                    Element getUser = document.select(CSS).first();
                    if(getUser == null) {
                        result = false;
                    }else{
                        result = true;
                        userName = getUser.text().trim();
                    }
                }catch (IOException e){
                    result = false;
                }

                Message msg = h.obtainMessage();
                Bundle bundle = new Bundle();

                bundle.putBoolean("result", result);
                bundle.putString("id", ID);
                bundle.putString("pw", PW);
                bundle.putString("name", userName);

                msg.setData(bundle);
                h.sendMessage(msg);
            }
        };
        t.start();
    }

    public abstract void getDailyTime();
    public abstract void getTimetable();

    protected static final int TIMEOUT = 15000;

}
