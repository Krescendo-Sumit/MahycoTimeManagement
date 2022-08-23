package com.mahyco.time.timemanagement;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Config {
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
    static Context mContext;
    public Config(Context mContext) {
        this.mContext = mContext;
    }

    public static boolean  NetworkConnection() {

        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;


    }


    public static void save(Context context, String key,String value) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences("locdata", Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(key, value); //3
        editor.commit(); //4
    }

    public static String getValue(Context context,String key) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences("locdata", Context.MODE_PRIVATE); //1
        text = settings.getString(key, null); //2
        return text;
    }

}