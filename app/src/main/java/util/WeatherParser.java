package util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class WeatherParser{

    private static String URL = "http://www.weatheri.co.kr/forecast/forecast01.php?rid=1301040101&k=10&a_name=";
    private static String geocode = "서울";
    private static String IURL = "http://cse.dongguk.edu";
    private static String weatherInfo = "";
    private static final int THRESHOLD = 233;
    private static boolean isFailed = false;

    public static String getImageUrl(){
        return IURL;
    }

    public static String getWeatherInfo(){ return weatherInfo; }

    public WeatherParser(){}

    public static void getWeather(final String geocode, final Handler mHandler) {
        Thread t = new Thread() {
            public void run() {
                try {
                    Document document = Jsoup.connect("http://www.weatheri.co.kr/forecast/forecast01.php?rid=1301040101&k=10&a_name=" + geocode).get();
                    Element weather = document.select("tr>td").get(THRESHOLD);
                    Element imageUrl = document.select("tbody>tr>td[rowspan=4]>img").first();
                    IURL = imageUrl.attr("src");
                    IURL = "http://www.weatheri.co.kr" + IURL.substring(2, IURL.length());
                    weatherInfo = weather.text().toString();
                    Log.e("Weather", weatherInfo);
                    Log.e("Weather", IURL);
                }catch (IOException e){
                    isFailed = true;
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
