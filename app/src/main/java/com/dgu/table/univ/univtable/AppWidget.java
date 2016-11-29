package com.dgu.table.univ.univtable;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

public class AppWidget extends AppWidgetProvider {

    public static String WIDGET_BUTTON = "UPDATE_BUTTON_UNIVTABLE";

    @Override
    public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            Intent svcIntent = new Intent(ctxt, WidgetService.class);
            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews widget = new RemoteViews(ctxt.getPackageName(), R.layout.app_widget);
            widget.setRemoteAdapter(appWidgetIds[i], R.id.words, svcIntent);
            widget.setEmptyView(R.id.words, R.id.emptyview);

            Intent intentSync = new Intent(WIDGET_BUTTON);
            PendingIntent pendingSync = PendingIntent.getBroadcast(ctxt,0, intentSync, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar cal = Calendar.getInstance();
            String day = "";
            switch(cal.get(Calendar.DAY_OF_WEEK)){
                case 1: day = "일요일"; break;
                case 2: day = "월요일"; break;
                case 3: day = "화요일"; break;
                case 4: day = "수요일"; break;
                case 5: day = "목요일"; break;
                case 6: day = "금요일"; break;
                case 7: day = "토요일"; break;
                default: day = "오늘의 시간표"; break;
            }
            widget.setTextViewText(R.id.widget_title, day);
            widget.setOnClickPendingIntent(R.id.imageButtonSync, pendingSync);
            appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
        }

        super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(WIDGET_BUTTON.equals(intent.getAction())){
            Log.e("UPDATE", "CALLED");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), AppWidget.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.words);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

