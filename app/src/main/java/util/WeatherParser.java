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

import crawl.Crawler;

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
        switch (geocode){
            case Crawler.GEO_SEOUL:
                URL = "http://www.weatheri.co.kr/forecast/forecast01.php?rid=0101010000&k=1&a_name=%BC%AD%BF%EF";
                break;
            case Crawler.GEO_ILSAN:
                URL = "http://www.weatheri.co.kr/forecast/forecast01.php?rid=0202020102&k=1&a_name=%B0%ED%BE%E7";
                break;
            case Crawler.GEO_GYEONGJU:
                URL = "http://www.weatheri.co.kr/forecast/forecast01.php?rid=1002030102&k=8&a_name=%B0%E6%C1%D6";
                break;
            default:
                URL = "http://www.weatheri.co.kr/forecast/forecast01.php?rid=0101010000&k=1&a_name=%BC%AD%BF%EF";
                break;
        }
        Thread t = new Thread() {
            public void run() {
                try {
                    Document document = Jsoup.connect(URL).get();
                    Element weather = document.select("tr>td").get(THRESHOLD);
                    Element imageUrl = document.select("tbody>tr>td[rowspan=4]>img").first();
                    IURL = imageUrl.attr("src");
                    IURL = "http://www.weatheri.co.kr" + IURL.substring(2, IURL.length());
                    weatherInfo = weather.text().toString();
                    Log.e("Weather", weatherInfo);
                    Log.e("Weather", IURL);
                }catch (IOException e){
                    weatherInfo = "날씨 정보를 불러오는 중 오류가 발생했습니다";
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
