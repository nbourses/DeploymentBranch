package com.nbourses.oyeok.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhinandan on 11/26/2015.
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final String DB_NAME="mobileData.db";
    public static final String TABLE_SHAREDPREFERENCE_NAME="sharedPreference";
    public static final String KEY_COLUMN_NAME="key";

    public static final String VALUE_COLUMN_NAME="value";

    private static final String Create_Table_SharedPreference = "CREATE TABLE IF NOT EXISTS "+TABLE_SHAREDPREFERENCE_NAME+"(Key TEXT PRIMARY KEY,Value Text)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_Table_SharedPreference);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+TABLE_SHAREDPREFERENCE_NAME);
        onCreate(db);
    }

    /* Basic Database Functionalities */

    public long save(String key,String value) {
        if(ifexists(key)==0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_COLUMN_NAME, key);
            values.put(VALUE_COLUMN_NAME, value);
            // insert row
            long entry_id = db.insert(TABLE_SHAREDPREFERENCE_NAME, null, values);
            return entry_id;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN_NAME,key);
        values.put(VALUE_COLUMN_NAME,value);
        long entry_id=  db.update(TABLE_SHAREDPREFERENCE_NAME,values,KEY_COLUMN_NAME+"= '"+key+"'",null);
        return entry_id;
    }
    public int ifexists(String key){
        String s="null";
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "select count(*) from " + TABLE_SHAREDPREFERENCE_NAME + " where "
                + KEY_COLUMN_NAME + " = " +"'" + key + "'";

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
            int exist = c.getInt(0);
            return exist;
        }
        else
            return 0;
    }

    public String getValue(String key) {
        String s="null";
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("DBHelper","The key is "+key);

        String selectQuery = "SELECT  * FROM " + TABLE_SHAREDPREFERENCE_NAME + " WHERE "
                + KEY_COLUMN_NAME + " = " +"'" + key + "'";

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        if(c.getCount()!=0)
            s = c.getString(1);

        return s;
    }

    public List<KeyValuePair> getAllKeyValuePair() {
        List<KeyValuePair> keyValuePairList=new ArrayList<KeyValuePair>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_SHAREDPREFERENCE_NAME +"";

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                KeyValuePair kvp = new KeyValuePair();
                kvp.setKey(c.getString(0));
                kvp.setValue(c.getString(1));
                keyValuePairList.add(kvp);
            } while (c.moveToNext());
        }

        return keyValuePairList;
    }
}
