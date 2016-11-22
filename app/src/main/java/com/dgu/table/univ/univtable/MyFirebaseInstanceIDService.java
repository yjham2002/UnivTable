package com.dgu.table.univ.univtable;

import android.content.SharedPreferences;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        pref = getSharedPreferences("Univtable", MODE_PRIVATE);
        prefEditor = pref.edit();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("Refreshed token", refreshedToken);
        prefEditor.putString("Token" ,refreshedToken);
        prefEditor.commit();
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
}