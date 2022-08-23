package com.mahyco.time.timemanagement;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
/*import com.google.android.gms.common.SignInButton;*/
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {
    private static final String TAG ="login" ;
    private TextView txtView,lbltype,txtRegister,txtUpdate,txtForget;
    private EditText txtentermobile,txtEnterotp;
    private CardView btnLogin,btnRegistr,btnUpdate;
    String type=null;
    public String userCode;
    databaseHelper databaseHelper1;
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;
    //private SignInButton signInButton;

    ProgressDialog dialog;

    public String  langcode="";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog progressdialog;
    private ProgressDialog mProgressDialog;
    SharedPreferences locdata;
    public Messageclass msclass;
    private Boolean exit = false;
    public CommonExecution cx;
    Config config;
    Prefs mPref;
    private static final int RC_SIGN_IN = 1000;
    private static UsernameValidator usernameValidator;
    public static final int Progress_Dialog_Progress = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mPref=Prefs.with(this);

        usernameValidator = new UsernameValidator();
       // signInButton = findViewById(R.id.sign_in_button);

        btnLogin= (CardView)findViewById(R.id.btnLogin);

        txtRegister=(TextView)findViewById(R.id.txtRegister);
        txtUpdate=(TextView)findViewById(R.id.txtUpdate);
        txtForget=(TextView)findViewById(R.id.txtForget);
        databaseHelper1 = new databaseHelper(this);
        txtentermobile=(EditText)findViewById(R.id.txtentermobile);
        txtEnterotp=(EditText)findViewById(R.id.txtEnterotp);

        msclass=new Messageclass(this);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        cx=new CommonExecution(this);
        config = new Config(this); //Here the context is passing
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        String userid = Preferences.get(this, Constants.UserID);
        try {
            userCode = Utils.decrypt("Mahyco123",
                    Base64.decode(userid.getBytes("UTF-16LE"), Base64.DEFAULT));
        } catch (Exception e)
        {

        }
       Cursor data =  databaseHelper1.fetchusercodeNew(); // Remark 16th jan 2022  databaseHelper1.fetchusercode(userCode);


        if (data.getCount()==0)
        {

        }else {
            data.moveToFirst();
            if(data!=null)
            {
                do
                {
                    userCode=data.getString((data.getColumnIndex("user_code")));
                }while(data.moveToNext());

            }data.close();
            if (userCode!=null) {
                txtentermobile.setText(userCode);

                /*Remark 16th jan 2022 Redirect to Punch in/out as not need to login again*/

                Intent intent = new Intent(getApplicationContext(), punch.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

        }
        checkAndRequestPermissions();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRequest();
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenRegister();
            }
        });
        txtForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenForget();
            }
        });

        //For Update App
        txtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent promptInstall = new Intent(android.content.Intent.ACTION_VIEW);
                // promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 // startActivity(promptInstall);


                Intent promptInstall = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.mahyco.time.timemanagement"));
                startActivity(promptInstall);
            }
        });
        //
        //showLocationDialog();

    }

   /* protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    } */
   /*private void showLocationDialog() {
       String isLocationBoxShown = mPref.getString(AppConstant.IS_LOCATION_BOX_SHOWN, "0");
       Log.d("isLocationBoxShown","READ isLocationBoxShown value :: "+isLocationBoxShown);
       if (isLocationBoxShown.equalsIgnoreCase("0")) {
           AlertDialog.Builder locationDlg = new AlertDialog.Builder(login.this);
           locationDlg.setTitle("Use Your Location");
           ImageView showImage = new ImageView(login.this);
           showImage.setImageResource(R.drawable.location_msg);
           locationDlg.setView(showImage);
           locationDlg.setCancelable(false);
           locationDlg.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface arg0, int arg1) {
                   mPref.save(AppConstant.IS_LOCATION_BOX_SHOWN, "1");
                   Log.d("isLocationBoxShown","SAVE isLocationBoxShown value :: "+mPref.getString(AppConstant.IS_LOCATION_BOX_SHOWN, "0"));
                   //requestLocationAccess();
                   checkAndRequestPermissions();
               }
           });
           locationDlg.show();
       }
   }*/
    public void OpenRegister(){
        Intent openIntent=new Intent(this,register_user.class);
        startActivity(openIntent);
    }

    public void OpenForget(){

        Intent openIntent=new Intent(this,forget_password.class);
        startActivity(openIntent);

    }
    public void LoginRequest() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String username = txtentermobile.getText().toString().trim();
        String pass = txtEnterotp.getText().toString().trim();
        // String lang = spnlanguage.getSelectedItem().toString().trim();
        logincheck(username.trim(),pass.trim());
        // new LoginReq().execute(SERVER,username,pass);

    }


    private void signIn() {
        //Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private  void logincheck(String username,String pass)
    {
        try {

            if(username.length()==0)
            {
                msclass.showMessage("Invalid credentials");
                return ;
            }
            if (txtEnterotp.getText().length() == 0) {
                msclass.showMessage("Invalid credentials");
                return ;
            }
            if(isValidUsernamePass(username,pass))
            {
                dialog.setMessage("Loading. Please wait...");
                dialog.show();

                /*Remark 16 Jan*/
                String searchQuery = "select  *  from UserMaster where User_pwd='" + pass.trim() + "' and user_code='" + username.toUpperCase().trim() + "'";
                SQLiteDatabase database = new databaseHelper(this).getReadableDatabase();
                Cursor cursor = database.rawQuery(searchQuery, null);

                //Cursor cursor = mDatabase.getReadableDatabase().rawQuery(searchQuery, null);
                // String searchQuery = "SELECT  *  FROM UserMaster  ";
                int count = cursor.getCount();
                count = 1;
                if (count > 0)
                {
                    // msclass.showMessage(cursor.getString(1));
                    cursor.moveToFirst();
                    while (cursor.isAfterLast() == false) {
                        editor.putString("Displayname", cursor.getString(3));
                        editor.commit();
                        cursor.moveToNext();
                    }
                    cursor.close();
                    txtEnterotp.setText("");
                    txtentermobile.setText("");
                    String encData = Utils.encrypt("Mahyco123".getBytes("UTF-16LE"),
                            (username.toUpperCase().trim()).getBytes("UTF-16LE"));
                    Log.d(TAG, "usercode encrypted in register : " + encData);
                    Preferences.save(this, Constants.UserID, encData);

                    //Star
                    if (config.NetworkConnection())
                    {
                        boolean b=HttpUtils.authkey(username, pass, mPref, cx,this);
                       // Toast.makeText(this, ""+b, Toast.LENGTH_SHORT).show();
                        if(b)
                        {
                            dialog.dismiss();
                            Intent intent = new Intent(login.this, punch.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            login.this.finish();


                        }
                        else
                        {   dialog.dismiss();
                            msclass.showMessage("something went wrong, please try again later.(token) .");
                        }
                    }
                    else
                    {  dialog.dismiss();
                        msclass.showMessage("Internet connection not available.");
                    }
                    //end

                  /*
                    Intent intent = new Intent(login.this, punch.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    dialog.dismiss();
                    */



                } else
                    {
                        // Call online login Check  if (local DB )
                    msclass.showMessage("Invalid credentials");
                    //msclass.showMessage("User name and password not correct ,please try again");
                    dialog.dismiss();
                }
            }
            else

            {
                msclass.showMessage("Invalid credentials");
               // Toast.makeText(login.this,"Invalid credentials",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex) {
           // Toast.makeText(login.this,"Please try again",Toast.LENGTH_LONG).show();
            msclass.showMessage("Please try again");
        }
    }

    private boolean isValidUsernamePass(String name, String password){
        boolean result= false;
        String expressionUserName = "^[a-zA-Z0-9]{2,25}$";

                //"([a-zA-Z0-9]$)"; --^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\s).*$
       // String expressionPassord = "(?=.*[!@#$%^&*-])(?=.*[0-9])(?=.*[A-Z]).{8,20}$";
        String expressionPassord = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

        String inputStr = name;
        String  inputPass = password;
        Pattern pattern = Pattern.compile(expressionUserName);//, Pattern.CASE_INSENSITIVE);
        Pattern patterninputPass = Pattern.compile(expressionPassord);//, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        Matcher matcherPass = patterninputPass.matcher(inputPass);
        if (matcher.matches())
        //if (usernameValidator.validate(name))
        {
            if(matcherPass.matches()){
                result = true;
            }
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
           // handleSignInResult(task);
        }
    }
/* No need single sign
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                // Preferences.saveBool(getApplicationContext(), Preferences.KEY_IS_LOGGED_IN, true);


                Map<String, Object> user = new HashMap<>();
                user.put("email", account.getEmail());
                user.put("familyName", account.getFamilyName());
                user.put("fullName", account.getDisplayName());
                user.put("givenName", account.getGivenName());
                //user.put("loginTime", getDate());


                // txtData.setText(account.getDisplayName() + "::::" + account.getId() + "::::" + account.getIdToken() + "::::" + account.getEmail());
                // Toast.makeText(this, "acc::"+account.toString(), Toast.LENGTH_SHORT).show();
            }
            Log.d("rht", "handleSignInResult: " + account.toString());


            if (!(account.toString().isEmpty())) {

                Log.d("rht", "mailUser: " + account.getEmail().toString());
                Log.d("rht", "Error: " + account.getServerAuthCode());

                // String email="rahul.dhande@mahyco.com";
                // UserRegisterGoogle(email);
                UserRegisterGoogle(account.getEmail().toString());

            } else {
                msclass.showMessage("something went wrong, please try again later");
            }
            // Signed in successfully, show authenticated UI.
            // updateUI(account);
        } catch (ApiException e) {
            e.getMessage();
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("rht", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
            Log.d("rht", "handleSignInResult: " +    e.getMessage());
        }
    }

*/
    private  boolean UserRegisterGoogle(String userMail)
    {
        if(config.NetworkConnection()) {
            dialog.setMessage("Loading....");
            dialog.show();
            String str= null;
            boolean fl=false;
            try {
                str = "";

                if(str.contains("False"))
                {
                    msclass.showMessage("You are not authorised. Kindly register");
                    dialog.dismiss();
                }


                else {
                    JSONObject object = new JSONObject(str);
                    Log.d("","DataWe"+object);
                    JSONArray jArray = object.getJSONArray("Table");
                    Log.d("","DataWe"+jArray);

                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject jObject = jArray.getJSONObject(0);

                        Log.d("","DataWe"+jObject);
//                        if(jObject.getString("IMEI")!=(imeiNumber)) {
//
//                            msclass.showMessage("Already Registered on other device");//show specific response msg from api
//                            dialog.dismiss();
//
//                        }
                        databaseHelper1.deleledata("UserMaster", "");
                        fl = databaseHelper1.InsertUserRegistrationGoogle(jObject.getString("usercode").toString().toUpperCase(), jObject.getString("username").toString(), jObject.getString("user_pwd").toString(),jObject.getString("user_imei").toString(),userMail);
                        editor.putString("username", jObject.getString("username").toString());
                       // editor.putString("user_imei",jObject.getString("user_imei"));


                        //}
                        //else
                        // {
                        //    msclass.showMessage("This user mobile device IMEI No and User id not match .please check IMEI_no");
                        //    dialog.dismiss();
                        ////    return false;
                        //}


                    }
                   // editor.putString("USER_CODE", txtentermobile.getText().toString());
                   // editor.putString("USER_EMAIL", userMail.toString());
                    editor.commit();

                    if (fl == true) {
                        //    msclass.showMessage("User Registration successfully");
                        dialog.dismiss();
                        txtEnterotp.setText("");
                        txtentermobile.setText("");
                        Intent intent= new Intent(getApplicationContext(),punch.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                        // finish();

                    } else {
                        msclass.showMessage("Registration  not done");
                        return false;
                    }


                }
            }
            catch (Exception e) {


            }

        }
        else
        {
            msclass.showMessage("Internet network not available.");
            dialog.dismiss();
            return false;
        }
        return true;
    }

    private boolean checkAndRequestPermissions() {
        int permissionCAMERA = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA);

        int storagePermission = ContextCompat.checkSelfPermission(this,

                android.Manifest.permission.READ_EXTERNAL_STORAGE);



        int READ_PHONE_STATE = ContextCompat.checkSelfPermission(this,

                android.Manifest.permission.READ_PHONE_STATE);
        int WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this,

                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(this,

                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(this,

                android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int USE_FINGERPRINT = ContextCompat.checkSelfPermission(this,

                android.Manifest.permission.USE_FINGERPRINT);
        int INTERNET = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET);

     /*   int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS);

        int receiveSMS = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_SMS);

      */

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (INTERNET != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.INTERNET);
        }
        if (READ_PHONE_STATE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (WRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ACCESS_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (USE_FINGERPRINT != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.USE_FINGERPRINT);
        }

        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
      /*  if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.SEND_SMS);
        }
       if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECEIVE_SMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
        }*/
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        try
        {

            if (new RootedDeviceUtils().isDeviceRooted()) {
                 Toast.makeText(login.this, "Device rooted", Toast.LENGTH_LONG).show();
                 showAlertDialogAndExitApp("This device is rooted. You can't use this app.");
            } else {
               // Toast.makeText(login.this, "Device NOT rooted", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex)
        {
        }
    }
    public void showAlertDialogAndExitApp(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(login.this).create();
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



}
