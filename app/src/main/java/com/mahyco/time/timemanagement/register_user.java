package com.mahyco.time.timemanagement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register_user extends AppCompatActivity {
    private static final String TAG ="" ;
    public EditText txtEnterName, txtEnterEmpCode, txtIMEI, txtEnterotp, txtEnterEmpMobile;
    public CardView btnLogin;
    public TextView txtTerms, txtotp1;
    public Messageclass msclass;
    public CommonExecution cx;
    public CheckBox chk;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Config config;
    databaseHelper databaseHelper1;
    private boolean isCheckedValue;
    //public Button btnLogin;
    ProgressDialog dialog;
    TelephonyManager tel;
    String  imei="";
    String  mobilenumber="";
    String usercode="";
    String  username="";
    String password="";
   // SharedPreferences pref;
    //SharedPreferences.Editor editor;
    Prefs mPref;
    Context context;
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        databaseHelper1 = new databaseHelper(this);
        msclass = new Messageclass(this);
        cx = new CommonExecution(this);
        mPref=Prefs.with(this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        context = this;
        config = new Config(this); //Here the context is passing
        dialog = new ProgressDialog(this);
        btnLogin = (CardView) findViewById(R.id.btnLogin);
        txtEnterName = (EditText) findViewById(R.id.txtEnterName);
        txtEnterEmpCode = (EditText) findViewById(R.id.txtEnterEmpCode);
        txtIMEI = (EditText) findViewById(R.id.txtIMEI);
        txtEnterotp = (EditText) findViewById(R.id.txtEnterotp);
        txtEnterEmpMobile = (EditText) findViewById(R.id.txtEnterEmpMobile);
        txtTerms = (TextView) findViewById(R.id.txtTerms);
        chk = (CheckBox) findViewById(R.id.chk);
        txtotp1 = (TextView) findViewById(R.id.txtotp1);

        btnLogin.setEnabled(false);

        pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        txtTerms.setText(Html.fromHtml("<u>Terms and Conditions</u>"));
        btnLogin.setCardBackgroundColor(Color.rgb(153, 214, 184));
        tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String imei = tel.getImei();
//            txtIMEI.setText(imei);
//
//        } else {
//            String imei = tel.getDeviceId();
//            txtIMEI.setText(imei);
//
//        }


        getUUID();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation() == true) {
                    //dialog.setMessage("Loading. Please wait...");
                    //dialog.show();

                    //String searchQuery1 = "delete from UserMaster ";
                   // databaseHelper1.runQuery(searchQuery1);
                    UserRegisteration();
                }
            }
        });

        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    btnLogin.setEnabled(true);
                    txtotp1.setEnabled(true);
                    btnLogin.setCardBackgroundColor(Color.rgb(0, 154, 78));
                } else {
                    btnLogin.setEnabled(false);
                    txtotp1.setEnabled(false);
                    btnLogin.setCardBackgroundColor(Color.rgb(153, 214, 184));
                }
            }
        });

        txtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(register_user.this);
                LayoutInflater inflater = register_user.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setPositiveButton("OK", null);

                AlertDialog alertDialog = dialogBuilder.create();

                alertDialog.show();
            }
        });
    }

    public void getUUID() {


       /* String uuid = pref.getString("uuid", "");

        String deviceId;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_PHONE_STATE}, 101);
            return;
        }
        if (tel.getDeviceId() != null) {
            deviceId = tel.getDeviceId();
            txtIMEI.setText(deviceId);
            //editor.putString("uuid", deviceId);
            editor.apply();

        } else {


            if (uuid.isEmpty()) {
                deviceId = UUID.randomUUID().toString();
               // editor.putString("uuid", deviceId);
                editor.commit();
                txtIMEI.setText(deviceId);
            } else {
                txtIMEI.setText(uuid);
            }


        }*/
        try {
            txtIMEI.setText(Utils.getDeviceIMEI(this.context));
        }
        catch (Exception ex)
        {
            txtIMEI.setText(UUID.randomUUID().toString());
        }

    }

    private boolean validation() {
        boolean flag = true;
        if (txtEnterName.getText().length() == 0) {
            msclass.showMessage("Please Enter User Name ");
            return false;

        }
        if (txtEnterEmpCode.getText().length() == 0) {
            msclass.showMessage("Please Enter Valid Emp Code");
            return false;
        }

        if (txtIMEI.getText().length() == 0) {
            msclass.showMessage("IMEI number can't be blank");
            return false;
        }
        if(txtEnterEmpMobile.getText().length()!=10)
        {
            msclass.showMessage(("Please Enter valid Mobile Number"));
            return false;
        }
        if (txtEnterotp.getText().length() == 0) {
            msclass.showMessage("Please Enter Password To Set");
            return false;
        }
        if(isValidUsernamePass(txtEnterotp.getText().toString())==false)
        {
           // msclass.showMessage("Please set  secure password");
            msclass.showMessage("Please set secure password \n" +
                    "1) Your password must be between 8 and 30 characters. \n" +
                    "2) Your password must contain at least one uppercase, or capital, letter (ex: A, B, etc.)\n" +
                    "3) Your password must contain at least one lowercase letter. \n " +
                    "4) Your password must contain at least one special character(ex:$, #, @, !,%,^,&,*,(,) -) \n " +
                    "5) Your password must contain at least one number digit (ex: 0, 1, 2, 3, etc.) \n ");
            // Toast.makeText(login.this,"Invalid credentials",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private boolean isValidUsernamePass( String password){
        boolean result= false;
        String expressionUserName = "^[a-zA-Z0-9]{2,25}$";

        //"([a-zA-Z0-9]$)"; --^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\s).*$
        // String expressionPassord = "(?=.*[!@#$%^&*-])(?=.*[0-9])(?=.*[A-Z]).{8,20}$";
        String expressionPassord = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

        //String inputStr = name;
        String  inputPass = password;
        Pattern pattern = Pattern.compile(expressionUserName);//, Pattern.CASE_INSENSITIVE);
        Pattern patterninputPass = Pattern.compile(expressionPassord);//, Pattern.CASE_INSENSITIVE);
       // Matcher matcher = pattern.matcher(inputStr);
        Matcher matcherPass = patterninputPass.matcher(inputPass);

            if(matcherPass.matches()){
                result = true;
            }

        return result;
    }

    private boolean UserRegisteration() {
        if (config.NetworkConnection()) {
            //dialog.setMessage("Loading....");
            //dialog.show();
            String str = null;
            String strAuthToken= null;
            String token="";
            boolean fl = false;
            try {
                imei = txtIMEI.getText().toString();
                mobilenumber = txtEnterEmpMobile.getText().toString();
                username=txtEnterName.getText().toString();
                usercode=txtEnterEmpCode.getText().toString();
                password=txtEnterotp.getText().toString();
                try {
                    strAuthToken = new GetAuthToken(txtEnterEmpCode.getText().toString(), txtEnterotp.getText().toString()).execute().get();
                } catch (ExecutionException e) {
                    msclass.showMessage("Something is wrong ,Please try  to later again ");
                } catch (InterruptedException e) {
                    msclass.showMessage("Something is wrong ,Please try  to later again ");
                }
                Log.i("Response From Api",strAuthToken);
                if(isValidMobile(mobilenumber)) {
                    if(mobilenumber != null && mobilenumber.length() == 10 ) {

                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(strAuthToken);
                        Log.i("Data :",jsonObject.toString());
                        if (jsonObject.has("access_token")) {
                            token = jsonObject.get("access_token").toString();
                            if (token != null && token != "") {
                                try {
                                    mPref.save(AppConstant.ACCESS_TOKEN_TAG, token);

                                    str = new Userverify("Hr_veriftyuser", context).execute().get();
                                 //   Toast.makeText(context,str,Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    msclass.showMessage("Somthing is worng ,Please try  to later again ");
                                }

                                if (str.contains("False")) {
                                    msclass.showMessage("User already register");
                                    dialog.dismiss();
                                } else if (str.contains("NotAvailable")) {
                                    msclass.showMessage("User not available");
                                } else {
                                    // msclass.showMessage(str);
                                    JSONObject object = new JSONObject(str);
                                    JSONArray jArray = object.getJSONArray("Table");

                                    for (int i = 0; i < jArray.length(); i++) {

                                        JSONObject jObject = jArray.getJSONObject(0);
                                        //if(jObject.getString("IMEI").toString().equals(msclass.getDeviceIMEI())) {
                                        databaseHelper1.deleledata("UserMaster", "");
                                        fl = databaseHelper1.InsertUserRegistration(jObject.getString("usercode").toString().toUpperCase(),
                                                jObject.getString("username").toString(), jObject.getString("user_imei").toString(),
                                                jObject.getString("user_pwd").toString());
                                        //editor.putString("Displayname", jObject.getString("username").toString());
                                    }
                                    //  editor.putString("usercode", txtEnterEmpCode.getText().toString());
                                    //  editor.commit();

                                    if (fl == true) {
                                        try {
                                            String encData = Utils.encrypt("Mahyco123".getBytes("UTF-16LE"),
                                                    (usercode).getBytes("UTF-16LE"));
                                            Log.d(TAG, "usercode encrypted in register : " + encData);
                                            Preferences.save(this, Constants.UserID, encData);
                                            Preferences.save(this, Constants.Pass, password);

                                            msclass.showMessage("User registration successfully");

                                        } catch (Exception ex) {
                                            msclass.showMessage("Somthing is worng ,Please try  to later again ");
                                        }
                                        //msclass.showMessage("User registration successfully");
                                        dialog.dismiss();
                                        txtEnterotp.setText("");
                                        txtEnterEmpCode.setText("");
                                        Intent intent = new Intent(getApplicationContext(), login.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        return true;
                                        // finish();

                                    } else {
                                        msclass.showMessage("Registration  not done");
                                        return false;
                                    }
                                }

                            }

                        } else {
                            msclass.showMessage("something went wrong, please try again later. (Rg-UserRegisteration)");

                        }
                    }else {
                        msclass.showMessage("Mobile Number Can Not Be null");
                    }
                }else{
                        msclass.showMessage("Please Enter Valid Mobile number");
                }
            }
                   catch (Exception e) {
                       msclass.showMessage("something went wrong, please try again later. (Register_user-UserRegisteration)");
                }





        }

                else {
            msclass.showMessage("Internet network not available.");
            dialog.dismiss();
            return false;
        }
        return true;
    }
    /**
     * <p>Method to get the access token from API</p>
     */
    public synchronized String sycHrVerifyuser(JSONObject jsonObject) {

        return HttpUtils.POSTJSON(Constants.HR_VerifyUser,jsonObject,mPref.getString(AppConstant.ACCESS_TOKEN_TAG,""));
    }
    public class GetAuthToken extends AsyncTask<String, String, String>
    {
        private String username;
        private String password;

        public GetAuthToken(String username ,String password){
            this.username = username;
            this.password = password;
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("grant_type", "password"));
            postParameters.add(new BasicNameValuePair("username", username));
            postParameters.add(new BasicNameValuePair("password",password));
            postParameters.add(new BasicNameValuePair("imei",imei));
            postParameters.add(new BasicNameValuePair("mobilenumber",mobilenumber));

            return HttpUtils.POST(Constants.GetToken,postParameters);
        }


        protected void onPostExecute(String result) {

            Log.d("result", result);
        }

    }

     public  String  callHrVerify()
     {
         String str="";
         JSONObject jsonObject = new JSONObject();
         JSONObject jsonObjectreq = new JSONObject();
         try {
             jsonObjectreq.put("usercode",usercode);
             jsonObjectreq.put("username",username);
             jsonObjectreq.put("imei_number",imei);
             jsonObjectreq.put("mobile_number",mobilenumber);
             jsonObjectreq.put("user_pwd",password);
             jsonObject.put("Table",jsonObjectreq);
            str= sycHrVerifyuser(jsonObject);
         } catch (Exception e) {
             msclass.showMessage("Something is worng ,Please try  to later again _(Registration)");
         }

      return str;

     }

    public static boolean isValidMobile(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches());
    }
    //** call  Hr _user verify APi
    private class Userverify  extends AsyncTask<String, String, String> {
        private ProgressDialog progressDailog;

        public Userverify(String Funname, Context context) {
        }

        protected void onPreExecute() {
            progressDailog = new ProgressDialog(context);
            progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDailog.setCanceledOnTouchOutside(false);
            progressDailog.setCancelable(false);
            progressDailog.setMessage("Data Uploading");
            progressDailog.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            return callHrVerify();

        }

        protected void onPostExecute(String result) {

            if (progressDailog != null) {
                progressDailog.dismiss();
            }
            //redirecttoRegisterActivity(result);
            //Staet
            /*try {
                String resultout = result.trim();
                Log.d("Response", resultout);
                JSONObject jsonObject = new JSONObject(resultout);
                if (jsonObject.has("success")) {
                    if (Boolean.parseBoolean(jsonObject.get("success").toString())) {



                    } else {

                    }

                } else {

                }
            } catch (Exception e) {

            }
*/
            //end


        }

    }

}
