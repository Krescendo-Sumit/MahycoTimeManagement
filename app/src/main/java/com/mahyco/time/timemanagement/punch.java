package com.mahyco.time.timemanagement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;

import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.mahyco.feedbacklib.view.DialogFeedback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class punch extends AppCompatActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback {
    private CardView btnClick, btnPunchOUT;
    RelativeLayout mainlayout;
    TextView txtText, txtname, txtVNameVCode;
    TextView txtCordinate;
    TextView txtLocation;
    TextView txtTime;
    TextView txtDate;
    TextView txtDay;
    TextView txtAccuracy;
    TextView txtAccuracyTxt;
    TextView txtFlag1, txtTime1, txtDate1;
    public Messageclass msclass;
    public CommonExecution cx;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Config config;
    private static final String TAG = "LocationActivity";
    //    private static final long INTERVAL = 1000 * 2;
//    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static final long INTERVAL = 1000;
    private static final long FASTEST_INTERVAL = 1000;
    String address;
    LocationRequest mLocationRequest;
    LocationCallback mlocationCallback;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    ImageView gpsBtn;
    String mDate, mDay;
    databaseHelper databaseHelper1;
    public String userCode;
    ProgressDialog dialog;
    Object[] objectArray;
    String currentVersion, latestVersion;
    Dialog dialog1;
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private CardView btnclose;
    Spinner spLocation;
    ImageView btnlogout;
    protected static final String TAG1 = "LocationOnOff";
    int REQUEST_CHECK_SETTINGS = 100;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    LinearLayout worklinerId;
    Dialog dialogpopup;
    Map<String, String> hashMap = new HashMap<>();
    String formattedDate1;
    Prefs mPref;
    int PUNCH_STATUS = 0; /*0:No Status, 1:PUNCHED IN, 2:PUNCHED OUT*/
    MediaPlayer MP;
    String qq = "select usercode,cordinates || ' ' || location || '_BULK_|vname_" + BuildConfig.VERSION_NAME + "_vcode_" + BuildConfig.VERSION_CODE + "' as location,cordinates,att_flag,punchdate as punch_date,punchtime as punch_time,flag from punchdata";
    JSONArray jsonArray_bulUplaod ;
    TextView tv_syncdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        spLocation = (Spinner) findViewById(R.id.spLocation);
        msclass = new Messageclass(this);
        mPref = Prefs.with(this);
        btnClick = (CardView) findViewById(R.id.btnPunch);
        btnPunchOUT = (CardView) findViewById(R.id.btnPunchOUT);
        mainlayout = (RelativeLayout) findViewById(R.id.mainlayout);
        txtText = (TextView) findViewById(R.id.txtText);
        tv_syncdata = (TextView) findViewById(R.id.tv_syncdata);
        txtCordinate = (TextView) findViewById(R.id.txtCordinate);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtDay = (TextView) findViewById(R.id.txtDay);
        txtFlag1 = (TextView) findViewById(R.id.txtFlag1);
        txtTime1 = (TextView) findViewById(R.id.txtTime1);
        txtname = (TextView) findViewById(R.id.txtname);
        txtVNameVCode = (TextView) findViewById(R.id.tv_vcode_vname);
        txtDate1 = (TextView) findViewById(R.id.txtDate1);
        txtAccuracy = (TextView) findViewById(R.id.txtAccuracy);
        txtAccuracyTxt = (TextView) findViewById(R.id.txtAccuracyTxt);
        gpsBtn = (ImageView) findViewById(R.id.gpsBtn);
        btnlogout = (ImageView) findViewById(R.id.imglogout);
        btnclose = (CardView) findViewById(R.id.btnclose);

        databaseHelper1 = new databaseHelper(this);
        worklinerId = (LinearLayout) findViewById(R.id.worklinerId);
        msclass = new Messageclass(this);
        cx = new CommonExecution(this);
        config = new Config(this); //Here the context is passing
        dialog = new ProgressDialog(this);
        final MediaPlayer MP = MediaPlayer.create(this, R.raw.mahyco);
        Cursor data1 = null;
        punch.setBlinkingTextview(gpsBtn, 700, 10);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(punch.this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        this.context = this;
        String empCode_encr = Preferences.get(this, Constants.UserID);
        try {
            userCode = Utils.decrypt("Mahyco123",
                    Base64.decode(empCode_encr.getBytes("UTF-16LE"), Base64.DEFAULT));

            data1 = databaseHelper1.fetchusercode(userCode);
        } catch (Exception e) {
            //msclass.showMessage("something went wrong, please try again later.. ");
        }

//Adding this Code to Show Local Database in Application  Code is Written By Sumit on 20-04-2022

        txtVNameVCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(punch.this, AndroidDatabaseManager.class);
                startActivity(intent);
                return false;
            }
        });

        // Version Checking Code


        try {
            new CheckVersion().execute(Constants.BASE_URL + "/api/hrbreaderdata/checkAppVersion");
        } catch (Exception e) {

        }

        // Sumit's Added Code is End Here.


        if (data1 != null && data1.getCount() == 0) {
        } else {
            data1.moveToFirst();
            if (data1 != null) {
                do {
                    userCode = data1.getString((data1.getColumnIndex("user_code")));
                    String userName = data1.getString((data1.getColumnIndex("username")));

                    txtname.setText("Welcome :" + userName);//.gputString("Displayname", cursor.getString(3));


                } while (data1.moveToNext());
            }
            data1.close();
        }

        txtVNameVCode.setText("" + cx.getVersionNameANDCode());

        // 22-05-2020
        if (userCode.length() == 0) {
            //userCode=pref.getString("UserID", null);
        }
        isTimeAutomatic(punch.this);
        boolean value = isTimeAutomatic(this);
        if (value != true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(punch.this);
            builder.setTitle("Mahyco");
            // builder.setMessage("Observation Saved");
            builder.setMessage("Please Enable Automatic Date and Time. \nWant To Enable It?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        } else {
            //
        }
        //final MediaPlayer mp = MediaPlayer.create(this, R.raw.gtm_analytics);
        Log.d(TAG, "onCreate ..............................." + isTimeAutomatic(punch.this));
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        ///Insert Punch In
        showdata();

        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocationUpdates();
                updateUI();
            }
        });


        tv_syncdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //finish();

                ExportData();


            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(punch.this);

                builder.setTitle("Mahyco");
                // builder.setMessage("Observation Saved");
                builder.setMessage("Do you want to sing out?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences pref;
                        SharedPreferences.Editor editor;
                        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                        editor = pref.edit();
                        //editor.clear();
                        //editor.commit();
                        //Preferences.save(context, Constants.UserID, "encData");
                        Intent openIntent = new Intent(context, login.class);
                        openIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(openIntent);
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mp.start();
                if (config.NetworkConnection()) {
                    if (validation() == true) {

                        GeneralMaster pp = (GeneralMaster) spLocation.getSelectedItem();

                        if (pp.Code().contains("0")) {
                            msclass.showMessage("Please select work location");
                            return;
                        }
                        try {
                            AlertDialog.Builder builder = new AlertDialog.Builder(punch.this);

                            builder.setTitle("Mahyco");
                            // builder.setMessage("Observation Saved");
                            builder.setMessage("Do you want to save record?");

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                @SuppressLint("WrongConstant")
                                public void onClick(DialogInterface dialog, int which) {

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                    // formatTime(new Date(), new Locale("es", "ES")
                                    String entrydate = sdf.format(new Date());

                                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss", Locale.US);
                                    String entrytime = sdf1.format(new Date());

                                    String input = txtDate.getText().toString();
                                    String Time = txtTime.getText().toString();
                                    String cordinate = txtCordinate.getText().toString();


                                    String[] split = input.split("Date: ");
                                    String Date = split[1];

                                    String[] split1 = Time.split("Time: ");
                                    String TimeSplit = split1[1];

                                    String[] cordi = cordinate.split("Cordinates: ");
                                    String cordinates = cordi[1];

                                    boolean result = databaseHelper1.InsertPunchData(userCode, txtLocation.getText().toString(), cordinates,
                                            "IN", Date, entrytime, entrydate);
                                    if (result) {
                                        /*msclass.showMessage("Punch IN data save successfully");
                                        mainlayout.setBackgroundColor(Color.rgb(255, 76, 76));
                                        btnPunchOUT.setVisibility(View.VISIBLE);
                                        btnClick.setVisibility(View.GONE);
                                        worklinerId.setVisibility(View.GONE);
                                        updateUI();
                                        dialog.dismiss();
                                        showdata();*/

                                        if (config.NetworkConnection())//returns true if internet available
                                        {
                                            //   MP.start();
                                            PUNCH_STATUS = 1;
                                            HR_Data_Upload("HR_Data_UploadNew");

                                            //do something. loadwebview.
                                        } else {
                                            msclass.showNoInternet();
                                            // Toast.makeText(punch.this,"No Internet Connection",1000).show();
                                        }

                                    }
                                }
                            });

                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Do nothing
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();
                        } catch (Exception e) {
                            msclass.showMessage("something went wrong, please try again later");
                        }
                    }
                } else {
                    msclass.showNoInternet();
                }
            }
        });

        ///Insert Punch OUT
        btnPunchOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mp.start();
                hashMap.put("alt_flag", "OUT");
                hashMap.put("date", mDate);
                String status = "OUT";
                Preferences.save(context, Preferences.USER_STATUS, status);
                objectArray = hashMap.entrySet().toArray();
                if (config.NetworkConnection()) {
                    if (validation() == true) {

                        try {
                            AlertDialog.Builder builder = new AlertDialog.Builder(punch.this);

                            builder.setTitle("Mahyco");
                            // builder.setMessage("Observation Saved");
                            builder.setMessage("Do you want to save record?");

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String entrydate = sdf.format(new Date());

                                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
                                    String entrytime = sdf1.format(new Date());

                                    String input = txtDate.getText().toString();
                                    String Time = txtTime.getText().toString();
                                    String cordinate = txtCordinate.getText().toString();

                                    String[] split = input.split("Date: ");
                                    String Date = split[1];

                                    String[] split1 = Time.split("Time: ");
                                    String TimeSplit = split1[1];

                                    String[] cordi = cordinate.split("Cordinates: ");
                                    String cordinates = cordi[1];

                                    boolean result = databaseHelper1.InsertPunchData(userCode, txtLocation.getText().toString(), cordinates,
                                            "OUT", Date, entrytime, entrydate);
                                    if (result) {
                                        /*msclass.showMessage("Punch OUT data save successfully");
                                        mainlayout.setBackgroundColor(Color.rgb(76, 166, 76));
                                        btnPunchOUT.setVisibility(View.GONE);
                                        btnClick.setVisibility(View.VISIBLE);
                                        spLocation.setSelection(0);
                                        updateUI();
                                        dialog.dismiss();
                                        showdata();*/
                                        Log.d("Punch_out1", "" + PUNCH_STATUS);
                                        if (config.NetworkConnection()) //returns true if internet available
                                        {
                                            PUNCH_STATUS = 2;
                                            Log.d("Punch_out2", "" + PUNCH_STATUS);
                                            //   MP.start();
                                            HR_Data_Upload("HR_Data_UploadNew");

                                            //do something. loadwebview.
                                        } else {
                                            // Toast.makeText(punch.this,"No Internet Connection",1000).show();

                                            msclass.showNoInternet();
                                        }
                                    }
                                }
                            });

                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Do nothing
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();
                        } catch (Exception e) {
                            msclass.showMessage("something went wrong, please try again later ");
                        }
                    }
                } else {
                    msclass.showNoInternet();
                }
            }
        });

        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GeneralMaster gm = (GeneralMaster) parent.getSelectedItem();
                try {


                   /* JSONObject object = new JSONObject();
                    String searchQuery2 = "select * from MDO_tagRetailerList where mobileno ='"+mobileno+"' ";
                    object.put("Table1", mDatabase.getResults( searchQuery2));
                    JSONArray jArray = object.getJSONArray("Table1");//new JSONArray(result);*/

                    if (gm.Code().contains("2")) {

                        if (config.NetworkConnection()) {
                            callHealthPOP();
                        } else {
                            msclass.showNoInternet();
                            spLocation.setSelection(0);
                        }


                        /*
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(punch.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("TIME MANAGEMENT");
                        // Setting Dialog Message
                        alertDialog.setMessage("Health Declaration Form . ");
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {




                            }


                        });


                        AlertDialog alert = alertDialog.create();
                        alert.show();
                        final Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
                        positiveButtonLL.weight = 10;
                        positiveButtonLL.gravity = Gravity.CENTER;
                        positiveButton.setLayoutParams(positiveButtonLL);
                        */
                        //end


                    }
                } catch (Exception e) {
                    msclass.showMessage("something went wrong, please try again later.SL ");
                }

                {


                    // BindRetailer(taluka);


                }

                // Toast.makeText(MobileVerify.this, "Country ID: "+gm.Code()+",  Country Name : "+gm.Desc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //  Toast.makeText(MobileVerify.this,"gngvnv", Toast.LENGTH_LONG).show();
            }
        });

        BindIntialData();
        //testCrash();
    }

    void ExportData() {
        try {

            qq = "select usercode,'Cordinates:' || cordinates || ' ' || location || '_BULK_|vname_" + BuildConfig.VERSION_NAME + "_vcode_" + BuildConfig.VERSION_CODE + "' as location,cordinates,att_flag,punchdate as punch_date,punchtime as punch_time,flag from punchdata where flag=0";
            jsonArray_bulUplaod = databaseHelper1.getResults(qq);
            if(jsonArray_bulUplaod.length()>0) {
                if (config.NetworkConnection()) {
                    new ExportData().execute();
                }
            }else
            {
                Toast.makeText(context, "No Data Found.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }


    }

    void testCrash() {
        Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }
    /*private void showUserFeedbackScreen(String userId){
        //Prefs mPref = Prefs.with(punch.this);
       boolean isFeedDone = mPref.getBoolean(AppConstant.LOCAL_CHECK_ISFEED_GIVEN, false);
        Log.d("TDMS_NXG", "GET PREF SAVED AS : " + mPref.getBoolean(AppConstant.LOCAL_CHECK_ISFEED_GIVEN, false));

        if (isFeedDone == false)
        {


            // userfeedback
            String json = "";
            String packageName = "com.mahyco.time.timemanagement";
            Config config = new Config(punch.this); //Here the context is passing
            try {
                if (config.NetworkConnection()) {
                    if (userId != null && !userId.isEmpty() && !userId.equals("")) {
                        CommonExecution cxx = new CommonExecution(this);
                        json = cxx.new BreederMasterDataIsFeedGiven(1, userId, packageName).execute().get();
                        Log.d("IsFeed", "User data str :" + json);
                        JSONObject obj = new JSONObject(json);
                        String IsFeedbackGiven = obj.getString("IsFeedbackGiven");

                        if (IsFeedbackGiven.equalsIgnoreCase("False")) {
                            showFeedbackScreen(userId);
                        }
                        else{
                            saveFeedStatusLocally();
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("IsFeed", "Msg: " + e.getMessage());
            }
        }
    }

    private void saveFeedStatusLocally(){
        Prefs mPref = Prefs.with(punch.this);
        mPref.saveBoolean(AppConstant.LOCAL_CHECK_ISFEED_GIVEN, true);
        Log.d("TDMS_NXG", "PREF SAVED AS : " + mPref.getBoolean(AppConstant.LOCAL_CHECK_ISFEED_GIVEN, false));
    }

    private void showFeedbackScreen(String userCode)
    {
        String packageName = "com.mahyco.time.timemanagement"; //BuildConfig.APPLICATION_ID;
        DialogFeedback feedbackDialog = new DialogFeedback(punch.this, packageName, userCode);
        feedbackDialog.showFeedbackDialog();
      //Register receiver for callback
        IntentFilter filter = new IntentFilter();
        filter.addAction("FeedbackResponse");
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent != null) {
                    boolean isFeedbackGiven = intent.getBooleanExtra("IS_FEEDBACK_GIVEN", false);
                    if (isFeedbackGiven == true)
                    {  saveFeedStatusLocally();
                        //mDatabase.insertUserFeedback(userId, "1");
                        //Toast.makeText(UserHome.this, "IS_FEEDBACK_GIVEN: " + isFeedbackGiven, Toast.LENGTH_LONG).show();
                        Log.d("MAA","IS_FEEDBACK_GIVEN : "+isFeedbackGiven);
                    }
                }
            }

            catch (Exception ex)
            {
                Log.d("MAA", "MAA 1 DEVICE_ID : " + ex.toString());
            }
        }
    };*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*try {
            if (receiver != null) {
                unregisterReceiver(receiver);
            }
        }
        catch (Exception ex){
            Log.d("MAA", "MAA 1 DEVICE_ID : " + ex.toString());
        }*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != dialogpopup)
                dialogpopup.dismiss();
            spLocation.setSelection(0);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void bindSP(Spinner sp) {

        try {
            sp.setAdapter(null);
            //List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0", "CHOOSE"));
            Croplist.add(new GeneralMaster("1", "YES"));
            Croplist.add(new GeneralMaster("2", "NO"));


            // Initializing an ArrayAdapter
            //  ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
            //        this,R.layout.spinner_item,adapter
            // );
            // spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            sp.setAdapter(adapter);
            sp.setSelection(1);


        } catch (Exception ex) {
            msclass.showMessage("something went wrong, please try again later ");
        }


    }

    public void callHealthPOP() {
        try {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = df.format(c);
            boolean flag = false;

            // final Dialog
            dialogpopup = new Dialog(context);

            dialogpopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogpopup.setContentView(R.layout.healthform);

            dialogpopup.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialogpopup.setOnKeyListener(new Dialog.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        spLocation.setSelection(0);
                        //finish();
                    }
                    return true;
                }
            });

           /* final Dialog dialogpopup = new Dialog(context, android.R.style.Theme_Light);
            dialogpopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
           // dialogpopup=new Dialog(punch.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialogpopup.setContentView(R.layout.healthform);
            */
            dialogpopup.show();


            // final Dialog dialog = new Dialog(context);
            /// dialog.setContentView(R.layout.healthform);


             /* s.postDelayed(new Runnable() {
                public void run() {
                    s.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                }
            }, 100L); */
            //  final LinearLayout tblLayout = (LinearLayout)dialog.findViewById(R.id.li1);
            // TableRow row0 = (TableRow)tblLayout.getChildAt(0);
            //TableRow row = (TableRow)tblLayout.getChildAt(1);
            final Spinner sp1 = (Spinner) dialogpopup.findViewById(R.id.sp1);
            final Spinner sp2 = (Spinner) dialogpopup.findViewById(R.id.sp2);
            final Spinner sp3 = (Spinner) dialogpopup.findViewById(R.id.sp3);
            final Spinner sp4 = (Spinner) dialogpopup.findViewById(R.id.sp4);
            final Spinner sp5 = (Spinner) dialogpopup.findViewById(R.id.sp5);
            final Spinner sp6 = (Spinner) dialogpopup.findViewById(R.id.sp6);
            final Spinner sp7 = (Spinner) dialogpopup.findViewById(R.id.sp7);
            final CheckBox chk = (CheckBox) dialogpopup.findViewById(R.id.chk);

            bindSP(sp1);
            bindSP(sp2);
            bindSP(sp3);
            bindSP(sp4);
            bindSP(sp5);
            bindSP(sp6);
            bindSP(sp7);
            Button btnPunchIn = (Button) dialogpopup.findViewById(R.id.btnPunchIn);
            EditText txt8 = (EditText) dialogpopup.findViewById(R.id.txt8);

            txt8.setText(formattedDate);
            txt8.setEnabled(false);
            ImageView imgclose = (ImageView) dialogpopup.findViewById(R.id.imgclose);
            imgclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spLocation.setSelection(0);
                    dialogpopup.dismiss();
                }
            });
            String[] array = null;
            array = new String[1];
            array[0] = "SELECT PRODUCT";
            //  mobileno =txtretailermobileno.getText().toString().trim();


            btnPunchIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (config.NetworkConnection()) {
                        String status = "IN";
                        Preferences.save(context, Preferences.USER_STATUS, status);
                        hashMap.put("date", mDate);
                        objectArray = hashMap.entrySet().toArray();
                        try {
                            GeneralMaster s1 = (GeneralMaster) sp1.getSelectedItem();
                            GeneralMaster s2 = (GeneralMaster) sp2.getSelectedItem();
                            GeneralMaster s3 = (GeneralMaster) sp3.getSelectedItem();
                            GeneralMaster s4 = (GeneralMaster) sp4.getSelectedItem();
                            GeneralMaster s5 = (GeneralMaster) sp5.getSelectedItem();
                            GeneralMaster s6 = (GeneralMaster) sp6.getSelectedItem();
                            GeneralMaster s7 = (GeneralMaster) sp7.getSelectedItem();
                            if (s1.Code().contains("0")) {
                                ((TextView) sp1.getSelectedView()).setError("Please select answer");
                                // msclass.showMessage("Please select option of Question 1");
                                // return;
                            }
                            if (s2.Code().contains("0")) {
                                ((TextView) sp2.getSelectedView()).setError("Please select answer");
                                // msclass.showMessage("Please select option of Question 1");
                                // return;
                            }

                            if (s3.Code().contains("0")) {
                                ((TextView) sp3.getSelectedView()).setError("Please select answer");
                                // msclass.showMessage("Please select option of Question 1");
                                // return;
                            }
                            if (s4.Code().contains("0")) {
                                ((TextView) sp4.getSelectedView()).setError("Please select answer");
                                // msclass.showMessage("Please select option of Question 1");
                                // return;
                            }

                            if (s5.Code().contains("0")) {
                                ((TextView) sp5.getSelectedView()).setError("Please select answer");
                                // msclass.showMessage("Please select option of Question 1");
                                // return;
                            }

                            if (s1.Code().contains("0")) {
                                ((TextView) sp1.getSelectedView()).setError("Please select answer");
                                // msclass.showMessage("Please select option of Question 1");
                                // return;
                            }

                            if (s6.Code().contains("0")) {
                                ((TextView) sp6.getSelectedView()).setError("Please select answer");
                                // msclass.showMessage("Please select option of Question 1");
                                // return;
                            }
                            if (s7.Code().contains("0")) {
                                ((TextView) sp7.getSelectedView()).setError("Please select answer");
                                // msclass.showMessage("Please select option of Question 1");
                                //return;
                            }

                            if (s1.Code().contains("0") || s2.Code().contains("0") ||
                                    s3.Code().contains("0") || s4.Code().contains("0") ||
                                    s5.Code().contains("0") || s6.Code().contains("0") ||
                                    s7.Code().contains("0")) {
                                msclass.showMessage("Please select  answer .");
                                return;
                            }
                            if (!chk.isChecked()) {
                                //chk.setButtonDrawable(getResources().getDrawable(
                                //   R.drawable.border_style));
                                msclass.showMessage("Please select  declaration check box .");
                                // msclass.showMessage("Please select option of Question 1");
                                return;
                            }

                            savePUNCHINdata(s1.Code().toString(), s2.Code().toString(), s3.Code().toString(),
                                    s4.Code().toString(), s5.Code().toString(), s6.Code().toString(), s7.Code().toString());

                        } catch (Exception ex) {

                            msclass.showMessage("something went wrong, please try again later");

                        }
                    } else {
                        msclass.showNoInternet();
                    }
                }
            });

            dialogpopup.show();
            dialogpopup.setCancelable(true);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialogpopup.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialogpopup.getWindow().setAttributes(lp);
            //Window window = dialog.getWindow();
            //window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);


        } catch (Exception ex) {
            msclass.showMessage("something went wrong, please try again later ");


        }


    }

    public void savePUNCHINdata(final String Q1, final String Q2, final String Q3, final String Q4, final String Q5, final String Q6, final String Q7) {
        MP = MediaPlayer.create(this, R.raw.mahyco);
        if (validation() == true) {

            GeneralMaster pp = (GeneralMaster) spLocation.getSelectedItem();

            if (pp.Code() == "0") {
                msclass.showMessage("Please select work location");
                return;
            }
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(punch.this);

                builder.setTitle("Mahyco");
                // builder.setMessage("Observation Saved");
                builder.setMessage("Do you want to save record?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @SuppressLint("WrongConstant")
                    public void onClick(DialogInterface dialog, int which) {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String entrydate = sdf.format(new Date());

                        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
                        String entrytime = sdf1.format(new Date());

                        String input = txtDate.getText().toString();
                        String Time = txtTime.getText().toString();
                        String cordinate = txtCordinate.getText().toString();

                        String[] split = input.split("Date: ");
                        String Date = split[1];

                        String[] split1 = Time.split("Time: ");
                        String TimeSplit = split1[1];

                        String[] cordi = cordinate.split("Cordinates: ");
                        String cordinates = cordi[1];

                        boolean result = databaseHelper1.InsertPunchData(userCode, txtLocation.getText().toString(), cordinates,
                                "IN", Date, entrytime, entrydate);
                        databaseHelper1.InserthealthData(userCode, Q1, Q2, Q3, Q4, Q5, Q6, Q7, "0", Date);
                        if (result) {
                            /*msclass.showMessage("Punch IN data save successfully");
                            mainlayout.setBackgroundColor(Color.rgb(255, 76, 76));
                            btnPunchOUT.setVisibility(View.VISIBLE);
                            btnClick.setVisibility(View.GONE);
                            worklinerId.setVisibility(View.GONE);
                            dialogpopup.dismiss();
                            updateUI();
                            dialog.dismiss();
                            showdata();*/

                            if (config.NetworkConnection()) //returns true if internet available
                            {
                                // MP.start();
                                PUNCH_STATUS = 1;
                                HR_Data_Upload("HR_Data_UploadNew");

                                //do something. loadwebview.
                            } else {
                                // Toast.makeText(punch.this,"No Internet Connection",1000).show();
                            }

                        }
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            } catch (Exception e) {
                msclass.showMessage("something went wrong, please try again later ");
            }
        }
    }

    private void BindIntialData() {

        try {
            spLocation.setAdapter(null);
            //List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            List<GeneralMaster> Croplist = new ArrayList<GeneralMaster>();
            Croplist.add(new GeneralMaster("0", "SELECT WORK LOCATION"));
//            Croplist.add(new GeneralMaster("1", "WORK FROM HOME"));
            Croplist.add(new GeneralMaster("2", "WORK FROM OFFICE/FIELD/PLANT/DEPOT"));

            ArrayAdapter<GeneralMaster> adapter = new ArrayAdapter<GeneralMaster>
                    (this, android.R.layout.simple_spinner_dropdown_item, Croplist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spLocation.setAdapter(adapter);


        } catch (Exception ex) {
            msclass.showMessage("something went wrong, please try again later ");

            //  Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block

        }
        currentVersion = pInfo.versionName;

        new GetLatestVersion().execute();
    }


    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
//It retrieves the latest version by scraping the content of current version from play store at runtime
                String urlOfAppFromPlayStore = "https://play.google.com/store/apps/details?id=com.mahyco.time.timemanagement";
                Document doc = Jsoup.connect(urlOfAppFromPlayStore).get();
                latestVersion = doc.getElementsByClass("htlgb").get(6).text();

            } catch (Exception e) {
                //msclass.showMessage("something went wrong, please try again later");

            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    if (!isFinishing()) { //This would help to prevent Error : BinderProxy@45d459c0 is not valid; is your activity running? error
                        showUpdateDialog();
                    } else {
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                dowork();
//                                startapp();
//                                finish();
//                            }
//                        }).start();

                    }
                }
            } else
                // background.start();
                super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A New Update is Available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("https://play.google.com/store/apps/details?id=com.mahyco.time.timemanagement")));
                dialog.dismiss();
            }
        });

        /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //background.start();
            }
        });*/

        builder.setCancelable(false); //Update 17 Jan. 2022
        dialog1 = builder.show();
    }

    public static void setBlinkingTextview(ImageView tv, long milliseconds, long offset) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(milliseconds); //You can manage the blinking time with this parameter
        anim.setStartOffset(offset);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        tv.startAnimation(anim);
    }

    public void showdata() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String formattedDate = df.format(c);
        Cursor data = databaseHelper1.Data();
        worklinerId.setVisibility(View.VISIBLE);
        if (data.getCount() == 0) {
            // msclass.showMessage("No Data Available... ");
        } else {
            data.moveToFirst();
            if (data != null) {
                try {


                    do {
                        txtDate1.setText("Date   :  " + (data.getString(data.getColumnIndex("punchdate"))));
                        txtTime1.setText("Time   :  " + (data.getString(data.getColumnIndex("punchtime"))));
                        txtFlag1.setText("In/Out :  " + (data.getString(data.getColumnIndex("att_flag"))) + "\n");

                        String lastdate = (data.getString(data.getColumnIndex("punchdate")));

                        if ((data.getString(data.getColumnIndex("att_flag"))).equals("IN")) {
                            PUNCH_STATUS = 1;
                            mainlayout.setBackgroundColor(Color.rgb(255, 76, 76));
                            btnPunchOUT.setVisibility(View.VISIBLE);
                            btnClick.setVisibility(View.GONE);
                            worklinerId.setVisibility(View.GONE);
                            updateUI();
                        } else if ((data.getString(data.getColumnIndex("att_flag"))).equals("OUT")) {
                            PUNCH_STATUS = 2;
                            mainlayout.setBackgroundColor(Color.rgb(76, 166, 76));
                            btnPunchOUT.setVisibility(View.GONE);
                            btnClick.setVisibility(View.VISIBLE);
                            worklinerId.setVisibility(View.VISIBLE);
                            updateUI();
                        } else {
                            //
                        }

                        if (lastdate.equals(formattedDate)) {
                            // worklinerId.setVisibility(View.GONE);
                            //New Code  add
                            if ((data.getString(data.getColumnIndex("att_flag"))).equals("IN")) {
                                PUNCH_STATUS = 1;
                                mainlayout.setBackgroundColor(Color.rgb(255, 76, 76));
                                btnPunchOUT.setVisibility(View.VISIBLE);
                                btnClick.setVisibility(View.GONE);
                                worklinerId.setVisibility(View.GONE);
                                updateUI();
                            } else if ((data.getString(data.getColumnIndex("att_flag"))).equals("OUT")) {
                                PUNCH_STATUS = 2;
                                mainlayout.setBackgroundColor(Color.rgb(76, 166, 76));
                                btnPunchOUT.setVisibility(View.GONE);
                                btnClick.setVisibility(View.VISIBLE);
                                worklinerId.setVisibility(View.VISIBLE);
                                updateUI();
                            } else {
                                //
                            }
                            //new code end


                        } else {
                            mainlayout.setBackgroundColor(Color.rgb(76, 166, 76));
                            btnPunchOUT.setVisibility(View.GONE);
                            btnClick.setVisibility(View.VISIBLE);
                            worklinerId.setVisibility(View.VISIBLE);
                            updateUI();
                        }
                    } while (data.moveToNext());

                } catch (Exception e) {
                    // msclass.showMessage("something went wrong, please try again later ");
                }

            }
            data.close();
        }
    }

    private boolean validation() {
        boolean flag = true;
        if (txtCordinate.getText().equals("")) {
            msclass.showMessage("Poor GPS connectivity\nTry again later!");
            return false;
        }
        /*if (txtLocation.getText().equals("")) {
            msclass.showMessage("Poor GPS connectivity\nTry again later!");
            return false;
        }*/

        return true;
    }

    @SuppressLint("RestrictedApi")
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setSmallestDisplacement(10);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    @Override
    public void onConnected(Bundle bundle) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);

        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    @Override
    public void onResult(@NonNull Result result) {
        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                // NO need to show the dialog;

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(punch.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }
    // @Override
    // public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

    //}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KeyEvent.KEYCODE_BACK) {
            if (null != dialogpopup)
                dialogpopup.dismiss();
            spLocation.setSelection(0);
        }

        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {

                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }

        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        //Task<Void> pendingResult = fusedLocationClient.requestLocationUpdates(mLocationRequest,mlocationCallback, null);
        Log.d(TAG, "Location update started ..............: ");

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        try {


            Log.d(TAG, "Firing onLocationChanged..............................................");
            mCurrentLocation = location;

//
            SimpleDateFormat timeWithoutAmPm = new SimpleDateFormat("HH:mm:ss", Locale.US);
            mLastUpdateTime = timeWithoutAmPm.format(new Date());
            // mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());


            float accuracy = mCurrentLocation.getAccuracy();

            if (accuracy == 0.0f) {


                txtAccuracy.setText(String.valueOf(accuracy));
                txtAccuracy.setVisibility(View.INVISIBLE);
                txtAccuracyTxt.setVisibility(View.INVISIBLE);


            } else {


                txtAccuracy.setText(accuracy + "m");
                txtAccuracy.setVisibility(View.VISIBLE);
                txtAccuracyTxt.setVisibility(View.VISIBLE);
            }


            // mDate=DateFormat.getDateInstance().format(new Date());
            // mDate = getDateFromTimeStamp(location.getTime());
            Date c = Calendar.getInstance().getTime();
            // System.out.println("Current time => " + c);
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            formattedDate1 = df1.format(c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            String formattedDate = df.format(c);

            SimpleDateFormat sdfm = new SimpleDateFormat("EEEE");

            Date d = new Date();
            String day = sdfm.format(d);
            mDay = day;
            mDate = formattedDate;
            updateUI();
        } catch (Exception ex) {
            //   msclass.showMessage("something went wrong, please try again later.Punch -onLocationChanged .");

        }
    }


    private void updateUI() {
        try {
            Log.d(TAG, "UI update initiated .............");
            if (null != mCurrentLocation) {
                String lat = String.valueOf(mCurrentLocation.getLatitude());
                String lng = String.valueOf(mCurrentLocation.getLongitude());
                txtDate.setText("Date: " + mDate);
                txtDay.setText("Day: " + mDay);
                txtTime.setText("Time: " + mLastUpdateTime);
                txtCordinate.setText("Cordinates: " + lat + "," + lng);
                String cordin = lat + "," + lng;
                Preferences.save(context, Preferences.COORDINATES, cordin);


                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
                    txtLocation.setText("Address: " + addresses.get(0).getAddressLine(0) + "");
                    String loc = "Cordinates: " + lat + "," + lng + "-" + addresses.get(0).getAddressLine(0) + "" + cx.getVersionNameANDCode();
                    Preferences.save(context, Preferences.KEY_ADDRESS_LOCATION, loc);


                } catch (Exception e) {
                    // msclass.showMessage("something went wrong, please try again later ");
                }
            } else {
                Log.d(TAG, "location is null ...............");
            }
        } catch (Exception ex) {
            // msclass.showMessage("something went wrong, please try again later.Punch -updateUI .");

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
            Log.d(TAG, "Location update stopped .......................");
        } catch (Exception ex) {
            // msclass.showMessage("something went wrong, please try again later.Punch -stopLocationUpdates .");

        }
    }


    private boolean isGooglePlayServicesAvailable() {

        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Please Enable GPS", Toast.LENGTH_SHORT).show();
        final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    // To Upload Obseervation On server
    public void HR_Data_Upload(String HR_Data_UploadNew) {
        if (config.NetworkConnection()) {
            dialog.setMessage("Loading....");
            dialog.show();
            String str = null;
            // str = cx.new MDOMasterData(1, txtUsername.getText().toString(), txtPassword.getText().toString()).execute().get();
            // Old logic
            // String searchQuery = "select  *  from punchdata where flag='0' LIMIT 1";
            //  String searchQuery2 = "select  *  from healthTable where att_flag='0' LIMIT 1 ";
            // New logic 20-09-2021
            String searchQuery = "select  *  from punchdata where flag='0' ";
            String searchQuery2 = "select  *  from healthTable where att_flag='0'  ";

            Cursor cursor = databaseHelper1.getReadableDatabase().rawQuery(searchQuery, null);
            int count = cursor.getCount();
            cursor.close();
//            if (count > 0) {
            String location = Preferences.get(context, Preferences.KEY_ADDRESS_LOCATION);
            String cdnts = Preferences.get(context, Preferences.COORDINATES);
            String stat = Preferences.get(context, Preferences.USER_STATUS);

            try {
                byte[] objAsBytes = null;//new byte[10000];
                JSONObject object = new JSONObject();
                JSONArray dataValues = new JSONArray();
                JSONArray dataValues2 = new JSONArray();

                try {

                    //Get latest date time
                    SimpleDateFormat timeData = new SimpleDateFormat("HH:mm:ss", Locale.US);
                    String latestTime = timeData.format(new Date());

                    JSONObject json = new JSONObject();
                    json.put("usercode", userCode);
                    json.put("location", location);
                    json.put("Cordinates", txtCordinate.getText().toString());
                    json.put("att_flag", stat);
                    json.put("punchdate", mDate);
                    json.put("punchtime", latestTime);
                    json.put("flag", "0");
                    json.put("date", formattedDate1);
                    dataValues.put(json);

                    //object.put("Table1", databaseHelper1.getResults(searchQuery));
                    //object.put("Table2", databaseHelper1.getResults(searchQuery2));
                    object.put("Table1", dataValues);
                    dataValues2 = databaseHelper1.getResults(searchQuery2);
                    object.put("Table2", dataValues2);

                } catch (JSONException e) {
                    msclass.showMessage("something went wrong, please try again later ,Uploading");
                }
                try {
                    objAsBytes = object.toString().getBytes("UTF-8");
                    Log.d(TAG, "HR_Data_Upload object : " + object.toString());
//                        Toast.makeText(getApplicationContext(), "HR_Data_Upload object : "+object.toString(), Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {
                    msclass.showMessage("something went wrong, please try again later,Uploading");
                }
                dialog.setMessage("Loading. Please wait...");
                dialog.setCancelable(false);
                dialog.show();
                // str= new UploadDataServer(HR_Data_Upload,objAsBytes).execute(SERVER).get();
                new UploadDataServer(HR_Data_UploadNew, objAsBytes).execute();
                //End
                // cursor.close();
                //End
                   /* if(str.contains("True")) {

                        dialog.dismiss();
                       // msclass.showMessage("Records Uploaded successfully");
                        String searchQuery1 = "update punchdata set flag = '1' where flag='0'";


                        databaseHelper1.runQuery(searchQuery1);
                    }
                    else
                    {
                        msclass.showMessage(str);
                        dialog.dismiss();
                    }*/

            } catch (Exception ex) {
                dialog.dismiss();
                // msclass.showMessage("something went wrong, please try again later ");


            }
//            }
//            else
//            {   dialog.dismiss();
//                //msclass.showMessage("Data not available for Uploading ");
//                dialog.dismiss();
//
//            }

        } else {
            msclass.showMessage("Internet network not available.");
            dialog.dismiss();
        }
        //dialog.dismiss();
    }

    //
    public synchronized String sycuploadHrData(JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.HR_Data_Upload, jsonObject,
                mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
    }

    public String uploadHrData(String encodeImage) {
        String str = "";
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectreq = new JSONObject();
        try {
            // jsonObjectreq.put("encodeImage",encodeImage);
            jsonObjectreq.put("EmpCode", userCode);
            jsonObjectreq.put("encodedData", encodeImage);
            jsonObject.put("Table", jsonObjectreq);

            // HR_Data_UploadNew
            str = sycuploadHrData(jsonObject);
        } catch (JSONException e) {
            //  msclass.showMessage("something went wrong, please try again later");
        }

        return str;

    }

    public class UploadDataServer extends AsyncTask<String, String, String> {

        byte[] objAsBytes;
        String Imagestring1;
        String Imagestring2;
        String ImageName;
        String Funname;

        public UploadDataServer(String Funname, byte[] objAsBytes) {

            //this.IssueID=IssueID;
            this.objAsBytes = objAsBytes;
            this.Imagestring1 = Imagestring1;
            this.Imagestring2 = Imagestring2;
            this.ImageName = ImageName;
            this.Funname = Funname;

        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {

            String encodeImage = Base64.encodeToString(objAsBytes, Base64.DEFAULT);
            return uploadHrData(encodeImage);
            // encode image to base64 so that it can be picked by saveImage.php file


        }

        protected void onPostExecute(String result) {
            String weatherInfo = "Weather Report  is: \n";
            try {
                dialog.dismiss();
                String resultout = result.trim();
                Log.i("Result is12  ", result);
                //Check token validation
                if (result.toLowerCase().contains("authorization")) {
                    redirecttoRegisterActivity(result);
                } else {
                    //redirecttoRegisterActivity(result);
                    if (result.contains("True") || result.contains("true")) {
                        if (MP == null)
                            MP = MediaPlayer.create(context, R.raw.mahyco);

                        MP.start();

                        if (PUNCH_STATUS == 1) {
                            msclass.showMessage("Punch IN Data Uploaded Successfully.");
                            mainlayout.setBackgroundColor(Color.rgb(255, 76, 76));
                            btnPunchOUT.setVisibility(View.VISIBLE);
                            btnClick.setVisibility(View.GONE);
                            worklinerId.setVisibility(View.GONE);
                            if (dialogpopup.isShowing()) {
                                dialogpopup.dismiss();
                            }
                            updateUI();
                            dialog.dismiss();
                            showdata();
                        } else if (PUNCH_STATUS == 2) {
                            msclass.showMessage("Punch OUT Data Uploaded Successfully");
                            mainlayout.setBackgroundColor(Color.rgb(76, 166, 76));
                            btnPunchOUT.setVisibility(View.GONE);
                            btnClick.setVisibility(View.VISIBLE);
                            spLocation.setSelection(0);
                            updateUI();
                            dialog.dismiss();
                            showdata();
                        }
                        /*mainlayout.setBackgroundColor(Color.rgb(255, 76, 76));
                        btnPunchOUT.setVisibility(View.VISIBLE);
                        btnClick.setVisibility(View.GONE);
                        worklinerId.setVisibility(View.GONE);
                        updateUI();
                        dialog.dismiss();
                        showdata();*/

                        if (Funname.equals("HR_Data_UploadNew")) {

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date d = new Date();
                            String strdate = dateFormat.format(d);

                            dialog.dismiss();
                            // msclass.showMessage("Records Uploaded successfully");
                            String searchQuery1 = "update punchdata set flag = '1' where flag='0'";
                            String searchQuery2 = "update healthTable set att_flag = '1' where att_flag='0'";

                            //Remark 16th Jan 2022 DELETE OLD DATA
                           /* String searchQuery1 = "delete from punchdata where flag='0'";
                            String searchQuery2 = "delete from healthTable where att_flag='0'";*/
                            databaseHelper1.runQuery(searchQuery1);
                            databaseHelper1.runQuery(searchQuery2);
                        }

                    } else {
                        //  msclass.showMessage("something went wrong, please try again later ");
                    }
                }
                // dialog.dismiss();


            } catch (Exception e) {

                // msclass.showMessage("something went wrong, please try again later "+e.getMessage());
                dialog.dismiss();
            }

        }
    }

    public void redirecttoRegisterActivity(String result) {
        if (result.toLowerCase().contains("authorization")) {

            if (result.toLowerCase().contains("authorization")) {
                if (HttpUtils.authkey(userCode, Preferences.get(this, Constants.Pass).toString(), mPref, cx, this)) {
                    //msclass.showMessage(mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
                    // Config.refreshActivity(punch.this);

                }
            }


          /*  AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Time Managment");
            builder.setMessage("Your login session is  expired, please register user again. ");
            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    editor.putString("UserID", null);
                    editor.commit();

                    Intent intent1 = new Intent(context.getApplicationContext(), login.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent1);




                }
            });
            AlertDialog alert = builder.create();
            alert.show(); */
        }
    }

    @Override
    protected void onResume() {

        if (config.NetworkConnection()) {
            /*TODO Uncomment when have to release Feedback Module*/
            //showUserFeedbackScreen(userCode);
            getCurrentVersion();
        } else {
            dialog.dismiss();
        }

        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
        if (new RootedDeviceUtils().isDeviceRooted()) {
            Toast.makeText(punch.this, "Device rooted", Toast.LENGTH_LONG).show();
            showAlertDialogAndExitApp("This device is rooted. You can't use this app.");
        } else {
            // Toast.makeText(punch.this,"Device NOT rooted",Toast.LENGTH_LONG).show();
        }
    }

    public void showAlertDialogAndExitApp(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(punch.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialog.show();
    }


    private class CheckVersion extends AsyncTask<String, Void, Void> {

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(context);


        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //UI Element
            //   uiUpdate.setText("Output : ");
            //  Dialog.setMessage("Please Wait..");
            // Dialog.show();
            //pb.setVisibility(View.VISIBLE);
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            try {

                Log.d("Url", urls[0]);
                Log.d("Url", urls[0]);
                // Call long running operations here (perform background computation)
                // NOTE: Don't call UI Element here.

                // Server url call by GET method
                HttpPost httpget = new HttpPost(urls[0]);
                httpget.setHeader("Authorization", "Bearer " + mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = Client.execute(httpget, responseHandler);

            } catch (ClientProtocolException e) {
                Error = e.getMessage();
                cancel(true);
            } catch (IOException e) {
                Error = e.getMessage();
                cancel(true);
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            //Dialog.dismiss();

            if (Error != null) {

                //  uiUpdate.setText("Output : "+Error);

            } else {
                //pb.setVisibility(View.GONE);
                //   uiUpdate.setText("Output : "+Content);
                // loadFromServer(Content.toString().trim());
                Log.i("Details", "" + Content);
                //   Toast.makeText(getApplicationContext(), ""+Content, Toast.LENGTH_SHORT).show();

                try {

                    JSONObject jsonObject = new JSONObject(Content.trim());

                    JSONObject jsonVersionDetails = jsonObject.getJSONObject("JsonObj");
                    int vcode = BuildConfig.VERSION_CODE;
                    if (jsonObject.getBoolean("success")) {
                        if (vcode != jsonVersionDetails.getInt("VersionCode")) {
                            showUpdateDialog();
                        }
                        if (jsonVersionDetails.getBoolean("NotificationStatus") != true) {
                            Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                            dialog.setContentView(R.layout.dialog_notification);
                            WebView web = dialog.findViewById(R.id.web);
                            Button btn_close = dialog.findViewById(R.id.btn_close);
                            btn_close.setEnabled(false);
                            btn_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            web.getSettings().setJavaScriptEnabled(true);
                            web.getSettings().setBuiltInZoomControls(true);
                            web.getSettings().setDisplayZoomControls(false);
                            web.setWebChromeClient(new WebChromeClient());
                            web.loadUrl(jsonVersionDetails.getString("Notification"));

                            web.setWebChromeClient(new WebChromeClient() {
                                public void onProgressChanged(WebView view, int progress) {
                                    //Make the bar disappear after URL is loaded, and changes string to Loading...
                                    setTitle("Loading...");
                                    setProgress(progress * 100); //Make the bar disappear after URL is loaded

                                    // Return the app name after finish loading
                                    if (progress == 100)
                                        setTitle("Mahyco");
                                }
                            });

                            web.setWebViewClient(new WebViewClient() {
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    // do your handling codes here, which url is the requested url
                                    // probably you need to open that url rather than redirect:
                                    view.loadUrl(url);
                                    return false; // then it is not handled by default action
                                }
                            });

                            web.setOnKeyListener(new View.OnKeyListener() {
                                @Override
                                public boolean onKey(View v, int keyCode, KeyEvent event) {
                                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                                        WebView webView = (WebView) v;

                                        switch (keyCode) {
                                            case KeyEvent.KEYCODE_BACK:
                                                if (webView.canGoBack()) {
                                                    webView.goBack();
                                                    return true;
                                                }
                                                break;
                                        }
                                    }

                                    return false;
                                }
                            });

                            new CountDownTimer(10000, 1000) {
                                int count = 1;

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    // TODO Auto-generated method stub
                                    btn_close.setText("" + millisUntilFinished / 1000);
                                    count++;
                                }

                                @Override
                                public void onFinish() {
                                    // TODO Auto-generated method stub
                                    btn_close.setText("Close");
                                    btn_close.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_close_24, 0);
                                    btn_close.setEnabled(true);
                                    dialog.dismiss();
                                }
                            }.start();


                            dialog.show();
                        }
                    } else  //  Coming False from the Version API
                    {
                        Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.i("Error is ", e.getMessage());
                }
            }
        }

    }


    private class ExportData extends AsyncTask<String, Void, Void> {

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(punch.this);


        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //UI Element
            //   uiUpdate.setText("Output : ");
            Dialog.setMessage("Please Wait..");
            Dialog.show();
            //pb.setVisibility(View.VISIBLE);
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            StringBuilder sb = new StringBuilder();

            String http = Constants.BASE_URL + "/api/hrbreaderdata/hR_ListPunchData";

            Log.i("Host is::", "" + http);
            HttpURLConnection urlConnection = null;
            try {
                Log.i("pass", "2");
                URL url = new URL(http);
                urlConnection = (HttpURLConnection) url.openConnection();
                //     urlConnection.addRequestProperty("Authorization", "Bearer "+mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
                Log.i("pass", "" + "Bearer " + mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Authorization", "Bearer " + mPref.getString(AppConstant.ACCESS_TOKEN_TAG, ""));
                urlConnection.setUseCaches(false);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                Log.i("pass", "3");
                urlConnection.setRequestProperty("Host", "timemanagementapi.mahyco.com");

                urlConnection.connect();
                //  String qq="select '0' as tr_id,usercode,cordinates || ' ' || location || '|vname_"+BuildConfig.VERSION_NAME+"_vcode_"+BuildConfig.VERSION_CODE+ "' as location,cordinates,att_flag,punchdate as punch_date,punchtime as punch_time,flag, DATETIme('now')  as upload_date from punchdata";


                String str = "{\"hR_Punch_Data\":" + jsonArray_bulUplaod.toString() + " }";
                Log.i("JSON DATA", str);
                JSONObject jsonParam = new JSONObject(str);

                DataOutputStream printout;
                Log.i("pass", "5");
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonParam.toString());
                Log.i("content:", jsonParam.toString());
                //Toast.makeText(getApplicationContext(),jsonParam.toString(),Toast.LENGTH_LONG).show();
                out.close();
                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    Log.i("content:", sb.toString());
                    //    Toast.makeText(getApplicationContext(), "" + sb.toString(), Toast.LENGTH_SHORT).show();
                    Content = sb.toString();
                } else {
                    Log.i("ResponseMsg:", urlConnection.getResponseMessage());
                }
            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if (Error != null) {

                //  uiUpdate.setText("Output : "+Error);

            } else {
                //pb.setVisibility(View.GONE);
                //   uiUpdate.setText("Output : "+Content);
                // loadFromServer(Content.toString().trim());
                Log.i("Details", "" + Content);

                try {

                    JSONObject jsonObject = new JSONObject(Content.trim());
                    if (jsonObject.getBoolean("success")) {
                        if(msclass!=null)
                            msclass.showMessage("SUCCESS: "+jsonObject.getString("message"));
                        Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                          databaseHelper1.updateBulkExport("UPDATE punchdata set flag=1 where flag=0");
                          databaseHelper1.updateBulkExport("delete from punchdata where flag=1");

                    }else {
                        Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        if(msclass!=null)
                        msclass.showMessage("ERROR: "+jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }


}
