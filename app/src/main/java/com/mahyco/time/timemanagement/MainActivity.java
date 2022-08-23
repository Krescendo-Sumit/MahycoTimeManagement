package com.mahyco.time.timemanagement;

import static com.mahyco.time.timemanagement.RootedDeviceUtils.isDeviceRooted;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
//import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.function.LongFunction;

public class MainActivity extends AppCompatActivity {
    databaseHelper databaseHelper1;
    public android.widget.ProgressBar mprogresss;
    Context context = MainActivity.this;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        if (isDeviceRooted()) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("You are using Rooted device ! you can't use this application");
            builder1.setCancelable(false);


            builder1.setPositiveButton(
                    "Close Application",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            System.exit(0);
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            alert11.setCanceledOnTouchOutside(false);
        } else {
            String userid = Preferences.get(this, Constants.UserID);
            try {
                userCode = Utils.decrypt("Mahyco123",
                        Base64.decode(userid.getBytes("UTF-16LE"), Base64.DEFAULT));
            } catch (Exception e) {

            }


            databaseHelper1 = new databaseHelper(this);
            Cursor data1 = databaseHelper1.fetchusercodeNew(); /*Remark 16th Jan 2022*/
            if (data1.getCount() == 0) {
            } else {
                data1.moveToFirst();
                if (data1 != null) {
                    do {
                        userCode = data1.getString((data1.getColumnIndex("user_code")));
                    } while (data1.moveToNext());
                }
                data1.close();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dowork();
                    startapp();
                    finish();
                }
            }).start();
        }
    }

    private void dowork() {

        for (int progress = 0; progress < 100; progress += 10) {
            try {
                Thread.sleep(100);//300
                mprogresss.setProgress(progress);
            } catch (Exception e) {

            }

        }
    }

    private void startapp() {
        if (userCode != null && userCode.length() != 0) {
            /*Intent intent;
            intent = new Intent(this, punch.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/

            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        }
    }
}
