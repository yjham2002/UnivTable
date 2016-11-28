package com.dgu.table.univ.univtable;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static String batStatus = "Unknown";

    private SQLiteDatabase database;
    private String dbName = "UNIVTABLE_DB";
    private String createTable =
            "create table if not exists UNIVTABLE_CHAT(" +
                    "`id` integer primary key autoincrement, " +
                    "`from` integer, " +
                    "`to` integer, " +
                    "`msg` text, " +
                    "`date` datetime, " +
                    "`read` integer);";

    private void sendMessage(int from, int to) {
        Intent intent = new Intent("UNIVTABLE_CHAT_EVENT");
        intent.putExtra("froma", from);
        intent.putExtra("toa", to);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private static final String TAG = "MyFirebaseMsgService";

    public void createDatabase(){
        database = openOrCreateDatabase(dbName, android.content.Context.MODE_PRIVATE, null);
    }

    public void createTable(){
        try{
            database.execSQL(createTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertData(String from, String to, String msg){
        database.beginTransaction();
        try{
            String sql = "insert into UNIVTABLE_CHAT(`from`, `to`, `msg`, `date`, read) values (" + from + ", " + to + ", '" + msg + "', datetime('now', 'localtime') ," + 0 + ");";
            database.execSQL(sql);
            database.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            database.endTransaction();
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String from = remoteMessage.getFrom();
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String msg = data.get("message");

        Log.e(TAG, remoteMessage.getData().toString());

        if(title.equals("MSG_CALL")) {
            createDatabase();
            createTable();
            insertData(data.get("froma"), data.get("toa"), msg);
            if(ChatActivity.isRun != true) sendNotification("메세지가 도착했습니다");
            sendMessage(Integer.parseInt(data.get("froma").trim()), Integer.parseInt(data.get("toa").trim()));
        }else sendNotification(msg);

    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
