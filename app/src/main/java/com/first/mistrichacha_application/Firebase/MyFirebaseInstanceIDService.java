package com.first.mistrichacha_application.Firebase;

import android.util.Log;

import com.first.mistrichacha_application.Comman.SessionManagment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by Belal on 5/27/2016.
 */

//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    SessionManagment sd;
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        sd = new SessionManagment(getApplicationContext());
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sd.setFCM(refreshedToken);
        Log.d("FCM SESSION",sd.getFCM());
       }
  }