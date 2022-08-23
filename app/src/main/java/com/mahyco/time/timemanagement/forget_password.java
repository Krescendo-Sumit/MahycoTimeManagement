package com.mahyco.time.timemanagement;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class forget_password extends AppCompatActivity {
    databaseHelper databaseHelper1;
    Config config;
    public String userCode;
    private TextView txtView, lbltype, txtRegister, txtUpdate, txtForget;
    public EditText txtentermobile, txtEnterotp;
    private CardView btnLogin;
    public Messageclass msclass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        databaseHelper1 = new databaseHelper(this);
        msclass = new Messageclass(this);
        txtentermobile = (EditText) findViewById(R.id.txtentermobile);
        txtEnterotp = (EditText) findViewById(R.id.txtEnterotp);
        btnLogin = (CardView) findViewById(R.id.btnLogin);
        String userid = Preferences.get(this, Constants.UserID);
        try {
            userCode = Utils.decrypt("Mahyco123",
                    Base64.decode(userid.getBytes("UTF-16LE"), Base64.DEFAULT));
        } catch (Exception e) {

        }
        Cursor data = databaseHelper1.fetchusercode(userCode);
        if (data.getCount() == 0) {
            msclass.showMessage("Registration Not done properly,Please try to registration again!");
            return;
        } else {
            data.moveToFirst();
            if (data != null) {
                do {
                    userCode = data.getString((data.getColumnIndex("user_code")));
                } while (data.moveToNext());

            }
            data.close();
            txtentermobile.setText(userCode);

        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (txtentermobile.getText().length() == 0) {
                        msclass.showMessage("Invalid credentials");
                        return;
                    }
                    if (txtEnterotp.getText().length() == 0) {
                        msclass.showMessage("Invalid credentials");
                        return;
                    }
                    String username = txtentermobile.getText().toString().trim();
                    String pass = txtEnterotp.getText().toString().trim();

                    if (isValidUsernamePass(username, pass)) {
                        String InsertQuery = "update UserMaster set User_pwd = '" + txtEnterotp.getText().toString() + "' where user_code = '" + txtentermobile.getText().toString() + "' ";
                        databaseHelper1.runQuery(InsertQuery);
                        msclass.showMessage("Password update successfully");
                       //New
                        Intent intent = new Intent(getApplicationContext(), login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        //end

                        return;
                    } else {
                        msclass.showMessage("Please set secure password \n" +
                                "1) Your password must be between 8 and 30 characters. \n" +
                                "2) Your password must contain at least one uppercase, or capital, letter (ex: A, B, etc.)\n" +
                                "3) Your password must contain at least one lowercase letter. \n " +
                                "4) Your password must contain at least one special character(ex:$, #, @, !,%,^,&,*,(,) -) \n " +
                                "5) Your password must contain at least one number digit (ex: 0, 1, 2, 3, etc.) \n ");
                        // Toast.makeText(login.this,"Invalid credentials",Toast.LENGTH_LONG).show();

                        //msclass.showMessage("Invalid credentials");
                    }
                } catch (Exception ex) {

                    msclass.showMessage("Somthing is worng ,Please try  to later again ");

                   // Toast.makeText(forget_password.this, ex.toString() + "Exception", Toast.LENGTH_LONG).show();

                }
            }
        });

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

    }