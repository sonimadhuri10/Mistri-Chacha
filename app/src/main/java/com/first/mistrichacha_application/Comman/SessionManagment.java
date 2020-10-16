package com.first.mistrichacha_application.Comman;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagment {


    private static String TAG = SessionManagment.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;


    // Shared preferences file name
    private static final String PREF_NAME = "MistriChacha";
    private static final String KEY_ID = "id";
    private static final String LOGIN_STATUS = "false";
    private static final String KEY_FCM = "fcm";
    private static final String KEY_SOCIAL = "social";
    private static final String KEY_APITOKEN = "api_token";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_LATTITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LOCATION = "location";


    public SessionManagment(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setUSER_ID(String s) {
        editor.putString("KEY_ID", s);
        editor.commit();
    }

    public String getUSER_ID() {
        return pref.getString("KEY_ID", KEY_ID);
    }

    public void setKEY_APITOKEN(String s) {
        editor.putString("KEY_api_token", s);
        editor.commit();
    }

    public String getKEY_APITOKEN() {
        return pref.getString("KEY_api_token", KEY_APITOKEN);
    }


    public void setLOGIN_STATUS(String s) {
        editor.putString("LOGIN_STATUS", s);
        editor.commit();
    }

    public String getLOGIN_STATUS() {
        return pref.getString("LOGIN_STATUS", LOGIN_STATUS);
    }



    public String getFCM() {
        return pref.getString("KEY_FCM", KEY_FCM);
    }

    public void setFCM(String fcm) {
        editor.putString("KEY_FCM", fcm);
        editor.commit();
    }


    public String getSOCIAL_LOGIN() {
        return pref.getString("id", KEY_SOCIAL);
    }

    public void setSOCIAL_LOGIN(String social) {
        editor.putString("id", social);
        editor.commit();
    }

    public String getNAME() {
        return pref.getString("KEY_NAME", KEY_NAME);
    }

    public void setSETNAME(String name) {
        editor.putString("KEY_NAME", name);
        editor.commit();
    }

    public String getEMAIL() {
        return pref.getString("KEY_EMAIL", KEY_EMAIL);
    }

    public void setSETEMAIL(String email) {
        editor.putString("KEY_EMAIL", email);
        editor.commit();
    }

    public String getPHOTO() {
        return pref.getString("KEY_PHOTO", KEY_PHOTO);
    }

    public void setPHOTO(String photo) {
        editor.putString("KEY_PHOTO", photo);
        editor.commit();
    }

    public String getMobile() {
        return pref.getString("KEY_MOBILE", KEY_MOBILE);
    }

    public void setMobile(String mobile) {
        editor.putString("KEY_MOBILE", mobile);
        editor.commit();
    }

    public String getLatitude() {
        return pref.getString("KEY_LATTITUDE", KEY_LATTITUDE);
    }

    public void setLatitude(String latitide) {
        editor.putString("KEY_LATTITUDE", latitide);
        editor.commit();
    }

    public String getLongitude() {
        return pref.getString("KEY_LONGITUDE", KEY_LONGITUDE);
    }

    public void setLongitude(String longitude) {
        editor.putString("KEY_LONGITUDE", longitude);
        editor.commit();
    }

    public String getLocation() {
        return pref.getString("KEY_LOCATION", KEY_LOCATION);
    }

    public void setLocation(String location) {
        editor.putString("KEY_LOCATION", location);
        editor.commit();
    }
}
