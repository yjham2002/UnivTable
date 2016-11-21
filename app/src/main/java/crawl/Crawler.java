package crawl;

import android.os.AsyncTask;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

import util.SFCallback;

public abstract class Crawler extends AsyncTask<Void, Void, String> {

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

    protected SFCallback onStart, onConnect, onFinish, onFail;

    protected String URL_AUTH;
    protected String URL_TIME;
    protected String URL_HOME;
    protected String URL_HAND;
    protected String FORM_ID;
    protected String FORM_PW;
    protected String ID;
    protected String PW;

    protected static final int TIMEOUT = 15000;

}
