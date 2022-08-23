package com.mahyco.time.timemanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class databaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_FILE_PATH = "/sdcard";
    private static final String DATABASE_NAME = "mahycoattendance.db";
    private static final String COLUMN_ID = "_id";
    private static final String punchdata = "punchdata";
    private static final String UserMaster = "UserMaster";
    private static final String healthTable = "healthTable";
    static databaseHelper sInstance;
    public databaseHelper(Context context) {
        super(context, DATABASE_NAME,null , 5);

        SQLiteDatabase db = getWritableDatabase();
    }
    public static synchronized databaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new databaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL("CREATE TABLE " + punchdata + "(usercode text, location text, cordinates text,att_flag text,punchdate text,punchtime text, flag text, date text)");
       // db.execSQL("CREATE TABLE " + UserMaster + "(user_code TEXT,user_imei TEXT,User_pwd TEXT,username TEXT,user_email TEXT)");



        String CREATE_punchdata = "CREATE  TABLE IF NOT EXISTS " + punchdata + "(usercode text, location text, cordinates text,att_flag text,punchdate text,punchtime text, flag text, date text)";
        db.execSQL(CREATE_punchdata);
        String CREATE_UserMaster = "CREATE  TABLE IF NOT EXISTS " + UserMaster + "(user_code TEXT,user_imei TEXT,User_pwd TEXT,username TEXT,user_email TEXT)";
        db.execSQL(CREATE_UserMaster);

        String CREATE_TABLE_HEALTH = "CREATE  TABLE IF NOT EXISTS " + healthTable + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,userCode TEXT,Q1 TEXT,Q2 TEXT,Q3 TEXT,Q4 TEXT,Q5 TEXT," +
               "Q6 TEXT, Q7 TEXT,Q8 TEXT,att_flag TEXT,punchdate TEXT,EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(CREATE_TABLE_HEALTH);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + punchdata);
       // db.execSQL("DROP TABLE IF EXISTS " + UserMaster);
      //  onCreate(db);

        switch (oldVersion)
        {
            case 1:
                upgrate1(db);
                break;
            case 2:
                upgrate1(db);
                break;
            case 3:
                upgrate1(db);
                break;
            case 4:
                upgrate1(db);
                break;
            default:
                break;
        }
    }
    public void  upgrate1(SQLiteDatabase db) // mahendra
    {




        try {
            String CREATE_TABLE_HEALTH = "CREATE  TABLE IF NOT EXISTS " + healthTable + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,userCode TEXT,Q1 TEXT,Q2 TEXT,Q3 TEXT,Q4 TEXT,Q5 TEXT," +
                    "Q6 TEXT, Q7 TEXT,Q8 TEXT,att_flag TEXT,punchdate TEXT,EntryDt DATETIME DEFAULT (datetime('now','localtime')))";
            db.execSQL(CREATE_TABLE_HEALTH);


        } catch (Exception e) {
            Log.e(TAG, "code..." + e.getMessage());
        }



    }
    public boolean InserthealthData(String usercode ,String Q1 ,String Q2 ,String Q3,String Q4,String Q5,
                                    String Q6,String Q7,String att_flag ,String punchdate ) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userCode", usercode);
        contentValues.put("Q1", Q1);
        contentValues.put("Q2", Q2);
        contentValues.put("Q3", Q3);
        contentValues.put("Q4", Q4);
        contentValues.put("Q5", Q5);
        contentValues.put("Q6", Q6);
        contentValues.put("Q7", Q7);
        contentValues.put("att_flag", att_flag);
        contentValues.put("punchdate", punchdate);
        long result = db.insert(healthTable, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }
    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = getWritableDatabase();
        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);
            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            //sqlDB.close();
            return alc;
        }
    }
    public void runQuery(String runQuery) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        db.execSQL(runQuery);
        db.close();
    }
    public boolean InsertUserRegistration(String user_code,String username,String IMEI_No,String User_pwd) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_code", user_code);
        contentValues.put("user_imei", IMEI_No);
        contentValues.put("User_pwd", User_pwd);
        contentValues.put("username", username);
        db.insert("UserMaster", null, contentValues);
        db.close();
        return true;
    }

    public boolean InsertUserRegistrationGoogle(String UserCode, String username, String User_pwd, String IMEINo, String usermail) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("User_pwd", User_pwd);
        contentValues.put("user_code", UserCode);
        contentValues.put("user_imei", IMEINo);
        contentValues.put("username", username);
        contentValues.put("user_email", usermail);
        db.insert("UserMaster", null, contentValues);
        db.close();
        return true;
    }

    public boolean InsertPunchData(String usercode ,String location ,String cordinates ,String att_flag ,String punchdate,String Time,String date ) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("usercode", usercode);
        contentValues.put("location", location);
        contentValues.put("cordinates", cordinates);
        contentValues.put("att_flag", att_flag);
        contentValues.put("punchdate", punchdate);
        contentValues.put("punchtime", Time);
        contentValues.put("flag", 0);
        contentValues.put("date", date);
        long result = db.insert("punchdata", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor fetchusercode(String usercode) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM UserMaster where user_code='"+usercode+"'", null);
        return mCursor;
    }

    /*Remark 16 Jan 2022*/
    public Cursor fetchusercodeNew() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM UserMaster", null);
        return mCursor;
    }

    public Cursor Data() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM punchdata order by date desc LIMIT 1", null);
        return mCursor;
    }
    public void deleledata(String TABLE_NAME, String Where) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from  " + TABLE_NAME + "  " + Where);
    }

    public void updateBulkExport(String QUERY) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(QUERY);
    }


    public JSONArray getResults(String Query) {
        String myTable = "Table1";//Set name of your table
        String searchQuery = Query;
        Cursor cursor = getReadableDatabase().rawQuery(searchQuery, null);
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (i == 19 || i == 21) {
                            if (cursor.getBlob(i) != null) {
                                rowObject.put(cursor.getColumnName(i), "");

                            } else {
                                rowObject.put(cursor.getColumnName(i), "");

                            }
                        } else {
                            if (cursor.getString(i) != null) {


                                Log.d("TAG_NAME", cursor.getString(i));
                                rowObject.put(cursor.getColumnName(i), cursor.getString(i));

                            } else {
                                rowObject.put(cursor.getColumnName(i), "");
                            }
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        return resultSet;
    }
}
