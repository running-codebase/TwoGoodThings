package com.travis.awesome.two_good_things.util;


import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.travis.awesome.two_good_things.objects.GoodThingObject;

public class DatabaseManager {
	
	public static final String KEY_ROWID = "_id";
    public static final String KEY_TEXT = "Text";
    public static final String KEY_TIMESTAMP = "Timestamp";
    public static final String KEY_WEEK = "Week";
    public static final String KEY_MONTH = "Month";
    public static final String KEY_YEAR = "Year";
    
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "GoodThings";
    private static final String DATABASE_TABLE = "tblGoodThings";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
        "create table tblGoodThings (_id integer primary key autoincrement, "
        + "Text text not null, Timestamp text not null, Week integer, Month integer, Year integer);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

	
	
	
	
	public DatabaseManager(Context ctx) {

		this.context = ctx;
		DBHelper = new DatabaseHelper(context);

	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	   {
	       DatabaseHelper(Context context)
	       {
	           super(context, DATABASE_NAME, null, DATABASE_VERSION);
	       }

	       @Override
	       public void onCreate(SQLiteDatabase db)
	       {
	           db.execSQL(DATABASE_CREATE);
	       }

	       @Override
	       public void onUpgrade(SQLiteDatabase db, int oldVersion,
	                             int newVersion)
	       {
	           Log.w(TAG, "Upgrading database from version " + oldVersion
	                 + " to "
	                 + newVersion + ", which will destroy all old data");
	           db.execSQL("DROP TABLE IF EXISTS tblRandomQuotes");
	           onCreate(db);
	       }
	   }

	//---opens the database---
    public DatabaseManager open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    public long insertEntry(String text, String timestamp, Boolean week, Boolean month, Boolean year) {
    	
    	
    	int week_val, month_val, year_val;

    	if (week) {
    		week_val = 1;
    	} else {
    		week_val = 0;
    	}
    	if (month) {
    		month_val = 1;
    	} else {
    		month_val = 0;
    	}
    	if (year) {
    		year_val = 1;
    	} else {
    		year_val = 0;
    	}

    	ContentValues initialValues = new ContentValues();
    	
        initialValues.put(KEY_TEXT, text);
        initialValues.put(KEY_TIMESTAMP, timestamp);
        initialValues.put(KEY_WEEK, week_val);
        initialValues.put(KEY_MONTH, month_val);
        initialValues.put(KEY_YEAR, year_val);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
      		
 	
    
    }
    
    public Boolean removeEntry(int id) {
    	
    	return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + id, null) > 0;
        
    }
    
    public ArrayList<GoodThingObject> getAllEntries() {

    	 ArrayList<GoodThingObject> good_things = new ArrayList<GoodThingObject>();
    	 
    		Cursor cursor = db.query(DATABASE_TABLE, new String[] {
            		KEY_ROWID, 
            		KEY_TEXT,
            		KEY_TIMESTAMP,
            		KEY_WEEK,
            		KEY_MONTH,
            		KEY_YEAR}, 
                    null, 
                    null, 
                    null, 
                    null, 
                    null);
    		
    		cursor.moveToFirst();
        	while (cursor.isAfterLast() == false) {
        		GoodThingObject good_thing = new GoodThingObject();
        		good_thing.setId(cursor.getString(0));
        		good_thing.setText(cursor.getString(1));
        		good_thing.setTimestamp(cursor.getString(2));
        		if (cursor.getInt(3) == 1) {
        			good_thing.setWeek(true);
        		}
        		if (cursor.getInt(4) == 1) {
        			good_thing.setMonth(true);
        		}
        		if (cursor.getInt(5) == 1) {
        			good_thing.setYear(true);
        		}
        		good_things.add(good_thing);
        		cursor.moveToNext();
            }

        	cursor.close();
    	
        	return good_things;
    }
    
    

    //total entries in the database
    public int getEntryTotal()
    {
        Cursor cursor = db.rawQuery(
                    "SELECT COUNT(Text) FROM tblGoodThings", null);
                if(cursor.moveToFirst()) {
                    return cursor.getInt(0);
                }
                return cursor.getInt(0);
    }
}
