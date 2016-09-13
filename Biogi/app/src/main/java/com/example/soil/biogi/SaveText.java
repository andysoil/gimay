package com.example.soil.biogi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.soil.biogi.healthCheck.healthModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by soil on 2016/4/29.
 */
public class SaveText extends SQLiteOpenHelper {
    private static final String TAG = SaveText.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "android_api";


    private static final String TABLE_USER = "user";
    private static final String TABLE_HEALTHCHECK = "healthcheck";
    private static final String TABLE_HEALTH_ITME_REPORT = "itemreport" ;
    private static final String TABLE_HEALTH_ITME_REPORT_INSPECT = "iteminspect" ;
    private static final String TABLE_FOLLOW_UP = "followup" ;
    private static final String TABLE_HEALTH_ITEMDEITAL = "itemdeital" ;

    private static final String KEY_ID = "id";
    private static final String KEY_CATEGORY_NAME = "category";
    private static final String KEY_NAME = "name";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_PASSWORD = "pas";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_SEX = "sex";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_CELLPHONE = "cellphone";
    private static final String KEY_MAIL ="mail" ;
    private static final String KEY_DATE ="date" ;
    private static final String KEY_SCORE ="score" ;
    private static final String KEY_COMMENT ="comment" ;
    private static final String KEY_NUM ="num" ;
    private static final String KEY_INID ="inid" ;
    private static final String KEY_CID ="cid" ;
    private static final String KEY_INRG ="inrg" ;
    private static final String KEY_MEMBERVALUE ="mv" ;
    private static final String KEY_CATEGORY_CHOOSE ="choose" ;
    private static final String KEY_OFTEN ="often" ;
    private static final String KEY_REASON ="reason" ;
    private static final String KEY_RESULT ="result" ;

    public SaveText(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables

    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE "
                +  TABLE_USER + "("
                +  KEY_ID + " TEXT,"
                +  KEY_NAME + " TEXT,"
                +  KEY_PASSWORD + " TEXT,"
                +  KEY_USERNAME + " TEXT,"
                +  KEY_SEX + " TEXT,"
                +  KEY_BIRTHDAY + " DATE,"
                +  KEY_PHONE + " TEXT,"
                +  KEY_CELLPHONE + " TEXT,"
                +  KEY_MAIL + " TEXT,"
                +  KEY_CREATED_AT + " DATE)" ;

        String CREATE_HEALTHCHECK_TABLE = "CREATE TABLE "
                +TABLE_HEALTHCHECK + "("
                +KEY_NUM+" TEXT PRIMARY KEY,"
                +KEY_ID + " TEXT,"
                +KEY_SEX + " TEXT,"
                +KEY_COMMENT + " TEXT,"
                +KEY_SCORE + " TEXT,"
                +KEY_DATE + " TEXT)" ;

        String CREATE_HEALTH_ITME_REPORT_TABLE  = "CREATE TABLE "
                +TABLE_HEALTH_ITME_REPORT + "("
                +KEY_NUM+" TEXT PRIMARY KEY,"
                +KEY_DATE +" TEXT,"
                +KEY_CATEGORY_NAME + " TEXT)";

        String CREATE_HEALTH_ITEM_REPORT_INSPECT_TABLE =  "CREATE TABLE "
                +TABLE_HEALTH_ITME_REPORT_INSPECT + "("
                +KEY_NUM+" TEXT PRIMARY KEY,"
                +KEY_CATEGORY_CHOOSE + " TEXT, "
                +KEY_NAME + " TEXT,"
                +KEY_INID + " TEXT,"
                +KEY_CID + " TEXT,"
                +KEY_INRG + " TEXT,"
                +KEY_MEMBERVALUE + " TEXT)";
        String CREATE_HEALTH_FOLLOWUP ="CREATE TABLE "
                +TABLE_FOLLOW_UP +"("
                +KEY_NUM+ " TEXT PRIMARY KEY,"
                +KEY_DATE+" TEXT,"
                +KEY_NAME + " TEXT,"
                +KEY_OFTEN + " TEXT,"
                +KEY_REASON + " TEXT)";
        String CREATE_HEALTH_ITEMDEITAL ="CREATE TABLE "
                +TABLE_HEALTH_ITEMDEITAL +"("
                +KEY_NUM+ " TEXT PRIMARY KEY,"
                +KEY_INID + " TEXT,"
                +KEY_DATE+" TEXT,"
                +KEY_RESULT + " TEXT,"
                +KEY_REASON + " TEXT)";
        db.execSQL(CREATE_HEALTHCHECK_TABLE);
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_HEALTH_ITME_REPORT_TABLE);
        db.execSQL(CREATE_HEALTH_ITEM_REPORT_INSPECT_TABLE);
        db.execSQL(CREATE_HEALTH_FOLLOWUP);
        db.execSQL(CREATE_HEALTH_ITEMDEITAL);
        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEALTHCHECK);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String _id,String name, String created_at, String password, String username, String sex, String birthday, String phone, String cellphone, String mail) {

        ContentValues values = new ContentValues();
        values.put(KEY_ID, _id); // Name
        values.put(KEY_NAME, name); // Name
        values.put(KEY_PASSWORD, password); // pas At
        values.put(KEY_USERNAME, username); // Name
        values.put(KEY_SEX, sex); // pas At
        values.put(KEY_BIRTHDAY, birthday); // Created At
        values.put(KEY_PHONE, phone); // Name
        values.put(KEY_CELLPHONE, cellphone); // pas At
        values.put(KEY_CREATED_AT, created_at); // Created At
        values.put(KEY_MAIL,mail) ;
        // Inserting Row
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into usersqlite: " + id+username);
    }
    public void addHealthCheck(String num,String id,String date,String sex,String comment,String score){
        ContentValues values = new ContentValues();
        values.put(KEY_NUM, num);
        values.put(KEY_ID, id);
        values.put(KEY_SEX, sex);
        values.put(KEY_COMMENT, comment);
        values.put(KEY_SCORE, score);
        values.put(KEY_DATE, date);

        SQLiteDatabase db = this.getWritableDatabase();
        long long_id = db.insertWithOnConflict(TABLE_HEALTHCHECK, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into healthsqlite: " + long_id + date+sex);
    }
    public void addHealthItem(String num,String category_name,String date){
        ContentValues values = new ContentValues();
        values.put(KEY_NUM, num);
        values.put(KEY_DATE, date) ;
        values.put(KEY_CATEGORY_NAME, category_name);
        SQLiteDatabase db = this.getWritableDatabase();
        long long_id = db.insertWithOnConflict(TABLE_HEALTH_ITME_REPORT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into addHealthItem: " + long_id);
    }
    public void addHealthInspect(String num,String inspect_name,String inspect_id,String category_id,String inrg,String member_value,String choose){
        ContentValues values = new ContentValues();
        values.put(KEY_NUM, num);
        values.put(KEY_CATEGORY_CHOOSE, choose) ;
        values.put(KEY_NAME, inspect_name) ;
        values.put(KEY_INID, inspect_id) ;
        values.put(KEY_CID, category_id) ;
        values.put(KEY_INRG,inrg);
        values.put(KEY_MEMBERVALUE, member_value);
        SQLiteDatabase db = this.getWritableDatabase();
        long long_id = db.insertWithOnConflict(TABLE_HEALTH_ITME_REPORT_INSPECT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection

        Log.d(TAG, "New user addHealthInspect into addHealthInspect: " + long_id);
    }
    public void addHealthFollow(String num,String date,String name,String often,String reason){

        ContentValues values = new ContentValues();
        values.put(KEY_NUM, num);
        values.put(KEY_DATE, date) ;
        values.put(KEY_NAME, name) ;
        values.put(KEY_OFTEN, often);
        values.put(KEY_REASON, reason) ;

        SQLiteDatabase db = this.getWritableDatabase();
        long long_id = db.insertWithOnConflict(TABLE_FOLLOW_UP, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into addHealthFollow: " + long_id);
    }
    public void addHealthItemDetial(String num,String date,String result,String reason,String inid){

        ContentValues values = new ContentValues();
        values.put(KEY_NUM, num);
        values.put(KEY_INID, inid);
        values.put(KEY_DATE, date) ;
        values.put(KEY_RESULT, result) ;
        values.put(KEY_REASON, reason) ;

        SQLiteDatabase db = this.getWritableDatabase();
        long long_id = db.insertWithOnConflict(TABLE_HEALTH_ITEMDEITAL, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection

        Log.d(TAG, "New user addHealthItemDetial into addHealthItemDetial: " + long_id);
    }
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("_id",cursor.getString(0));
            user.put("name", cursor.getString(1));
            user.put("password", cursor.getString(2));
            user.put("username", cursor.getString(3));
            user.put("sex", cursor.getString(4));
            user.put("birthday", cursor.getString(5));
            user.put("phone", cursor.getString(6));
            user.put("cellphone", cursor.getString(7));
            user.put("mail", cursor.getString(8));
            user.put("created_at", cursor.getString(9));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching getUserDetails from Sqlite: " + user.toString());

        return user;
    }
    public HashMap<String,String>getHealthDate(){
        HashMap<String,String> date =new HashMap<String,String>() ;
        String selectDate = " SELECT * FROM "+ TABLE_HEALTHCHECK ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =db.rawQuery(selectDate,null) ;
        cursor.moveToFirst() ;

        if (cursor.getCount() > 0) {
            date.put(KEY_ID,cursor.getString(0));
            date.put(KEY_SEX,  cursor.getString(1));
            date.put(KEY_COMMENT, cursor.getString(2));
            date.put(KEY_SCORE, cursor.getString(3));
            date.put(KEY_DATE, cursor.getString(4));
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Fetching getHealthDate from Sqlite get: " + date.toString());
        return date;
    }

    public List<String> getHealthDates(){
        List<String> labels = new ArrayList<String>();
        String selectDate ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        selectDate = " SELECT * FROM " + TABLE_HEALTHCHECK;
        cursor = db.rawQuery(selectDate, null);
        cursor.moveToFirst() ;
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(5));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.d(TAG, "Fetching getHealthDates from Sqlite get: " +labels);
        return labels;
    }

    public List<String> getHealthDatesDetial(String date){

        List<String> labelsDetial = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        String selectDate = " SELECT * FROM " + TABLE_HEALTHCHECK +" WHERE "
                + KEY_DATE +" = '"+ date+ "'" ;
        Log.d(TAG, "Fetching getHealthDatesDetial from Sqlite get: " +selectDate);
        cursor = db.rawQuery(selectDate, null);
        cursor.moveToFirst() ;
        if (cursor.moveToFirst()) {
            do {
                labelsDetial.add(cursor.getString(3));
                labelsDetial.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Fetching getHealthDatesDetial from Sqlite get: " +labelsDetial);
        return labelsDetial;
    }

    public List<String> getHealthItemReport(String date){
        List<String> labelsDetial = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        String selectDate = " SELECT * FROM " + TABLE_HEALTH_ITME_REPORT +" WHERE "
                + KEY_DATE +" = '"+ date+ "'" ;
        Log.d(TAG, "Fetching getHealthItemReport from Sqlite get: " +selectDate);
        cursor = db.rawQuery(selectDate, null);
        cursor.moveToFirst() ;
        if (cursor.moveToFirst()) {
            do {
                labelsDetial.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Fetching getHealthItemReport from Sqlite get: " +labelsDetial);
        return labelsDetial;
    }

    public List<String> getHealthItemInspect(String category){
        List<String> labelsDetial = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        String selectDate = " SELECT * FROM " + TABLE_HEALTH_ITME_REPORT_INSPECT +" WHERE "
                + KEY_CATEGORY_CHOOSE +" = '"+ category+ "'" ;
        Log.d(TAG, "Fetching getHealthItemReport from Sqlite get: " + selectDate);
        cursor = db.rawQuery(selectDate, null);
        cursor.moveToFirst() ;

        if (cursor.moveToFirst()) {
            do {
                labelsDetial.add(cursor.getString(2));
                labelsDetial.add(cursor.getString(3));
                labelsDetial.add(cursor.getString(5));
                labelsDetial.add(cursor.getString(6));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Fetching getHealthItemReport from Sqlite get: " +labelsDetial);
        return labelsDetial;
    }

    public List<String> getHealthFollow(String date){

        List<String> labelsDetial = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        String selectDate = " SELECT * FROM " + TABLE_FOLLOW_UP +" WHERE "
                + KEY_DATE +" = '"+ date+ "'" ;
        Log.d(TAG, "Fetching getHealthFollow from Sqlite get: " +selectDate);
        cursor = db.rawQuery(selectDate, null);
        cursor.moveToFirst() ;
        if (cursor.moveToFirst()) {
            do {
                labelsDetial.add(cursor.getString(2));
                labelsDetial.add(cursor.getString(3));
                labelsDetial.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.d(TAG, "Fetching getHealthFollow from Sqlite get: " +labelsDetial);
        return labelsDetial;
    }
    public List<String> getHealthItemDetial(String inid){

        List<String> labelsDetial = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        String selectDate = " SELECT * FROM " + TABLE_HEALTH_ITEMDEITAL +" WHERE "
                + KEY_INID +" = '"+ inid+ "'" ;

        Log.d(TAG, "Fetching getHealthItemDetial from Sqlite get: " +selectDate);
        cursor = db.rawQuery(selectDate, null);
        cursor.moveToFirst() ;
        if (cursor.moveToFirst()) {
            do {
                labelsDetial.add(cursor.getString(2));
                labelsDetial.add(cursor.getString(3));
                labelsDetial.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.d(TAG, "Fetching getHealthItemDetial from Sqlite get: " +labelsDetial);
        return labelsDetial;
    }
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.delete(TABLE_HEALTHCHECK, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
    public void updatepas(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PASSWORD, password); // pas At

        String where = KEY_NAME + "='" + email +"'";
        // Inserting Row

        db.update(TABLE_USER, values, where, null) ;
        db.close(); // Closing database connection

        Log.d(TAG, "update password update into sqlite: ");
    }
    public void updateMemberData( final String inname, final String insex, final String inbirthday,
                                  final String inphone, final String incellphone, final String inmail,final String usename) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, inname);
        values.put(KEY_SEX, insex);
        values.put(KEY_BIRTHDAY, inbirthday);
        values.put(KEY_PHONE, inphone);
        values.put(KEY_CELLPHONE, incellphone);
        values.put(KEY_MAIL, inmail);
        String where = KEY_NAME + "='" + usename +"'";
        // Inserting Row

        db.update(TABLE_USER, values,where, null) ;
        db.close(); // Closing database connection

        Log.d(TAG, "update date update into sqlite: ");
    }
    public void updateHealthDate(String num,String id,String date,String sex,String comment,String score) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_SEX, sex);
        values.put(KEY_COMMENT, comment);
        values.put(KEY_SCORE, score);
        values.put(KEY_DATE, date);

        String where = KEY_NUM+ "='" + num +"'";
        // Inserting Row

        db.update(TABLE_HEALTHCHECK, values, where, null) ;
        db.close(); // Closing database connection

        Log.d(TAG, "update updateHealthDate update into sqlite: "+date+values);
    }
    public void updateHealthItemReport(String num,String categoryName,String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE,date) ;
        values.put(KEY_NUM, num);
        values.put(KEY_CATEGORY_NAME, categoryName);

        String where = KEY_NUM+ "='" + num +"'";
        // Inserting Row

        db.update(TABLE_HEALTH_ITME_REPORT, values, where, null) ;
        db.close(); // Closing database connection

        Log.d(TAG, "update updateHealthDate update into sqlite: "+values);
    }
    public void updateFollowUp(String num,String date,String name,String often,String reason) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NUM, num);
        values.put(KEY_DATE, date) ;
        values.put(KEY_NAME, name) ;
        values.put(KEY_OFTEN, often);
        values.put(KEY_REASON, reason) ;

        String where = KEY_NUM+ "='" + num +"'";
        // Inserting Row

        db.update(TABLE_FOLLOW_UP, values, where, null) ;
        db.close(); // Closing database connection

        Log.d(TAG, "update updateFollowUp update into sqlite: "+values);
    }
    public void updateHealthItemReportInspect(String num,String inspect_name,String inspect_id,String category_id,String inrg,String member_value,String choose) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NUM, num);
        values.put(KEY_CATEGORY_CHOOSE, choose) ;
        values.put(KEY_NAME, inspect_name) ;
        values.put(KEY_INID, inspect_id) ;
        values.put(KEY_CID, category_id) ;
        values.put(KEY_INRG,inrg);
        values.put(KEY_MEMBERVALUE, member_value);

        String where = KEY_NUM+ "='" + num +"'";
        // Inserting Row

        db.update(TABLE_HEALTH_ITME_REPORT_INSPECT, values, where, null);
        db.close(); // Closing database connection

        Log.d(TAG, "update updateHealthItemReportInspect update into sqlite: " + values);
    }
    public void updateHealthItemDetial(String num,String date,String result,String reason,String inid) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NUM, num);
        values.put(KEY_INID, inid);
        values.put(KEY_DATE, date) ;
        values.put(KEY_RESULT, result) ;
        values.put(KEY_REASON, reason) ;

        String where = KEY_NUM+ "='" + num +"'";
        // Inserting Row
        db.update(TABLE_HEALTH_ITEMDEITAL, values, where, null);
        db.close(); // Closing database connection

        Log.d(TAG, "update updateHealthItemDetial update into sqlite: " + values);
    }
}
