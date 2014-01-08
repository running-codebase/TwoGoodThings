package com.travis.awesome.two_good_things;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.travis.awesome.two_good_things.util.DatabaseManager;

public class AddTwoGoodThingsActivity extends Activity implements OnClickListener {

	private static final int ONE_DAY_IN_SECONDS = 86400;  
	
	private Intent intent;
	private DatabaseManager db_manager; 	
	private Calendar calendar;
//	private DailyAlarm daily_alarm;
//	private SharedPreferences pref;

	private Button btn_add;
	private Button btn_nothing;
	private EditText edit_txt_thing1;
	private EditText edit_txt_thing2;
	
	private boolean alarm_spawned;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_two_things_activity);

        WakeLocker.acquire(this); 
        init();
        getExtras();
        initializeTimeStamp();
	}
	
	@Override
    public void onResume(){
		super.onResume();
	}

	@Override
    public void onPause(){
		super.onStop();
		WakeLocker.release();
	}
	
	private void init(){
	
		
		db_manager = new DatabaseManager(this);
//		pref = PreferenceManager.getDefaultSharedPreferences(this); 
		
		btn_add = (Button) findViewById(R.id.btn_add);
		btn_nothing = (Button) findViewById(R.id.btn_nothing);
		edit_txt_thing1 = (EditText) findViewById(R.id.edittxt_thing1);
		edit_txt_thing2 = (EditText) findViewById(R.id.edittxt_thing2);

		btn_add.setOnClickListener(this);
		btn_nothing.setOnClickListener(this);
	}

	private void getExtras(){
		
		intent = getIntent();
		alarm_spawned = intent.getBooleanExtra("ALARM_SPAWNED", false);

		/*
		if (alarm_spawned)//The alarm restarted this set alarm for tommorow
		{
			Log.d("2GT", "Alarm set for tomorrow");
			//daily_alarm = new DailyAlarm(this, helper_code.timeAfterMidnight() + 60);//For testing fires every minute);			
			daily_alarm = new DailyAlarm(this, pref.getInt("ALARM_TIME", 72000) + ONE_DAY_IN_SECONDS);//For realease once per day			


		}
		*/
	}
	
	private void initializeTimeStamp(){
		calendar = Calendar.getInstance();
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
	}
	
	private void insertDatabase(String input){
		if (input =="")
			return;
		db_manager.open();
		db_manager.insertEntry(input, String.valueOf(calendar.getTimeInMillis()), false, false, false);
		db_manager.close();
	}
	
	private void startBrowseActivity(){
		
		Intent browse_activity_intent = new Intent(this, BrowseActivity.class);
        this.startActivity(browse_activity_intent);
	}

	private void startChooseBestOfActivity(){

		if (!alarm_spawned){
			return;
		}
	
//		Date date = new Date(calendar.getTimeInMillis());
//		Log.d("2GT", "Date is: " +date.toString());
//		Log.d("2GT", "Last day is : " + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

		if (calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH) && calendar.get(Calendar.MONTH) == Calendar.DECEMBER){
			Log.d("2GT", "Today is the end of the year");
			Intent best_of_intent = new Intent (this, ChooseBestActivity.class);
			best_of_intent.putExtra("TYPE", 2); //Best of year
			startActivity(best_of_intent);
		}

		if (calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
			Log.d("2GT", "Today is the end of the month");
			Intent best_of_intent = new Intent (this, ChooseBestActivity.class);
			best_of_intent.putExtra("TYPE", 1); //Best of month
			startActivity(best_of_intent);
		}

		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			Log.d("2GT", "Today is sunday");
			Intent best_of_intent = new Intent (this, ChooseBestActivity.class);
			best_of_intent.putExtra("TYPE", 0); //Best of week
			startActivity(best_of_intent);
		}
	}
	
	public void onClick(View view) {
		switch(view.getId()){
		case(R.id.btn_add):
 			insertDatabase(edit_txt_thing1.getText().toString());
			insertDatabase(edit_txt_thing2.getText().toString());
			Toast.makeText(this, "Inserting", Toast.LENGTH_LONG).show();
			if (alarm_spawned)
			{
				startBrowseActivity();
			}
			startChooseBestOfActivity();
			finish();
			break;
		case(R.id.btn_nothing):
			if (alarm_spawned)
			{
				startBrowseActivity();
			}
			startChooseBestOfActivity();
			finish();
			break;
		}
	}
	
}
