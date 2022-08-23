package com.mahyco.time.timemanagement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class CommonExecution {


    Context context;
    String returnstring;
    ProgressDialog dialog;
    public String Bredderurlpath;

    public CommonExecution(Context context) {
        this.context = context;
        dialog = new ProgressDialog(this.context);
        //dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

         Bredderurlpath = "";

    }

    public class BreederMasterData extends AsyncTask<String, String, String> {

        private String txtEnterEmpCode;
        private String txtIMEI;
        private String txtEnterotp;
        private String txtEnterName;
        private String txtEnterEmpMobile;


        public BreederMasterData(String txtEnterEmpCode, String txtIMEI, String txtEnterotp, String txtEnterEmpMobile) {

            this.txtEnterEmpCode = txtEnterEmpCode;
            this.txtIMEI = txtIMEI;
            this.txtEnterotp = txtEnterotp;
            this.txtEnterEmpMobile = txtEnterEmpMobile;
        }

        protected void onPreExecute() {
            dialog.setMessage("Loading....");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            //HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); // Timeout Limit

            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("from", "HR_VerifyUser"));
            // postParameters.add(new BasicNameValuePair("xmlString",""));
            String Urlpath1 = Bredderurlpath + "?usercode=" + txtEnterEmpCode + "&imei_number=" + txtIMEI + "&user_pwd=" + txtEnterotp + "&mobile_number=" + txtEnterEmpMobile +"";
            HttpPost httppost = new HttpPost(Urlpath1);

            // StringEntity entity;
            // entity = new StringEntity(request, HTTP.UTF_8);

            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            // httppost.setHeader("Content-Type","text/xml;charset=UTF-8");
            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(formEntity);

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }
                    returnstring = builder.toString();
                }
            } catch (UnsupportedEncodingException e) {

                returnstring = e.getMessage().toString();
                //dialog.dismiss();
            } catch (ClientProtocolException e) {

                returnstring = e.getMessage().toString();
                // dialog.dismiss();
            } catch (Exception e) {

                returnstring = e.getMessage().toString();
                //  dialog.dismiss();
            }

            // dialog.dismiss();
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    /**
     * <p>Method to get the access token from API</p>
     */
    public class GetAuthToken extends  AsyncTask<String, String, String>
    {
        private String username;
        private String password;
        private String imei;
        public GetAuthToken(String username ,String password,String imei){
            this.username = username;
            this.password = password;
            this.imei = imei;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                password = URLDecoder.decode(password, "UTF-8");
            } catch (UnsupportedEncodingException e){
            }
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);

            postParameters.add(new BasicNameValuePair("grant_type", "password"));
            postParameters.add(new BasicNameValuePair("username", username)); //97010412
            postParameters.add(new BasicNameValuePair("password",password));  //Mahyco@123123
            postParameters.add(new BasicNameValuePair("imei",imei));       //
            postParameters.add(new BasicNameValuePair("mobilenumber","1111"));


            return HttpUtils.POST(Constants.GetToken,postParameters);
        }


        protected void onPostExecute(String result) {

            Log.d("result", result);
        }

    }
    public class BreederMasterDataIsFeedGiven extends AsyncTask<String, String, String> {

        private int action;
        private String userCode;
        private String packageName;

        public BreederMasterDataIsFeedGiven(int action, String userCode,String packageName) {
            this.action = action;
            this.userCode = userCode;
            this.packageName = packageName;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>(2);
            postParameters.add(new BasicNameValuePair("from", "insertbreederData"));
            //String Urlpath1 = Constants.IS_FEEDBACK_GIVEN + "?UserCode=" + userCode;
            String Urlpath1 = Constants.IS_FEEDBACK_NEW + "?UserCode=" + userCode +"&packageName="+packageName;
            Log.d("Is FeedbackGiven","Urlpath1 :"+Urlpath1);

            HttpPost httppost = new HttpPost(Urlpath1);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            try {
                httppost.setEntity(new UrlEncodedFormEntity(postParameters));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                httppost.setEntity(formEntity);

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }
                    returnstring = builder.toString();
                }
            } catch (UnsupportedEncodingException e) {
                Log.d("MSG",e.getMessage());
                returnstring = e.getMessage().toString();
            } catch (ClientProtocolException e) {
                Log.d("MSG",e.getMessage());
                Log.d("MSG",e.getMessage());
            } catch (Exception e) {
                Log.d("MSG",e.getMessage());
                returnstring = e.getMessage().toString();
            }
            Log.d("Is FeedbackGiven","Return String :"+builder.toString());
            return builder.toString();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    public String getVersionNameANDCode() {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            String data = "|" + "vname_" + version + "_vcode_" + verCode;
            Log.d("DATA", "getVersionNameANDCode : " + data);
            return data;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("MSG", "getVersionNameANDCode MSG : " + e.getMessage());
        }
        return "";
    }
}
