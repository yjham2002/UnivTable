package crawl;

import android.os.AsyncTask;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

import util.SFCallback;

public abstract class Crawler extends AsyncTask<Void, Void, String> {

    public static final int UCODE_DONGGUK = 0;
    public static final int UCODE_KOOKMIN = 1;
    public static final int UCODE_SOGANG = 2;
    public static final int UCODE_DONGGUK_IL = 3;
    public static final int UCODE_DONGGUK_GY = 4;

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
