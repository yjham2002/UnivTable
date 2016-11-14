package crawl;

import android.os.AsyncTask;

import com.dgu.table.univ.univtable.ClassInfo;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

import util.SFCallback;

public abstract class Crawler extends AsyncTask<Void, Void, String> {

    protected ArrayList<ClassInfo> classList = new ArrayList<>();

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
