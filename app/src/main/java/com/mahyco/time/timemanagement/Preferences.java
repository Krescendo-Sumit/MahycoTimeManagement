package com.mahyco.time.timemanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;



public class Preferences {




    public Preferences(Context context) {
    }

    public static final String APP_PREF = "MahycoRetailer";
    public static SharedPreferences sp;
    public static String KEY_LOGIN = "loginType";
    public static String KEY_EMP_CODE = "empCode";
    public static String KEY_OTP = "OTP";
    public static String KEY_IMEI_CODE = "ImeiCode";
    public static String KEY_RANDOMNO = "randomNo";
    public static String KEY_PASSWORD = "Password";
    public static String KEY_NAME = "name";
    public static String KEY_FINALMSG = "finalMessage";
    public static Boolean IS_LOGGED_IN = false;
    public static String KEY_UserID = "UserID";
    public static final String KEY_ADDRESS_LOCATION ="location";
    public static final String COORDINATES ="coordinates";
    public static final String USER_STATUS ="status" ;

    public static void save(Context context, String key, String value) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void saveBool(Context context, String key, boolean value) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static Boolean getBool(Context context, String key) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        boolean val = sp.getBoolean(key, false);
        return val;
    }

    public static int getInt(Context context, String key) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        int userId = sp.getInt(String.valueOf(key), 0);
        return userId;
    }

    public static void saveInt(Context context, String key, int value) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static String get(Context context, String key) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        String userId = sp.getString(key, "");
        return userId;
    }

    public static void clearPreference(Context context) {
        sp = context.getSharedPreferences(APP_PREF, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }

//    public static void logOut(Context context) {
//
//        clearPreference(context);
//        Intent intent = new Intent(context,
//                LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//        ((Activity) context).finish();
//    }

//    public static void saveArrayList(Context context, ArrayList<StudentList> list, String key) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = prefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        editor.putString(key, json);
//        editor.apply();     // This line is IMPORTANT !!!
//    }
//
//    public static ArrayList<StudentList> getArrayList(Context context, String key) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        Gson gson = new Gson();
//        String json = prefs.getString(key, null);
//        Type type = new TypeToken<ArrayList<StudentList>>() {
//        }.getType();
//        return gson.fromJson(json, type);
//    }
}
