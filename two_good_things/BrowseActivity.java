
package com.travis.awesome.two_good_things;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.travis.awesome.two_good_things.adapters.GoodThingsAdapter;
import com.travis.awesome.two_good_things.objects.GoodThingObject;
import com.travis.awesome.two_good_things.util.DatabaseManager;

public class BrowseActivity extends Activity implements OnClickListener, OnItemClickListener  {

	private static final String FIRST_RUN = "FIRST_RUN";
	private static final String ALARM_TIME = "ALARM_TIME";
	private static final String ID = "ID";
	private static final String POSITION = "POSITION";
	private static final int SETTINGS_RESULTS = 1;
	private static final int EDIT_RESULTS = 2;
		
	
	private static final int FIRST_ALARM_IN_SECONDS = 72000; //3600 = 1hr   8pm = 72000
	private static final int ONE_DAY_IN_SECONDS = 86400; //set this to 
	
	private SharedPreferences pref;
	private Editor editor;
	private DatabaseManager db_manager; 	
	private DailyAlarm daily_alarm;
	
	private ListView listview_goodthings;
	private RelativeLayout layout_add_thing;
	private RelativeLayout layout_all;
	private RelativeLayout layout_week;
	private RelativeLayout layout_month;
	private RelativeLayout layout_year;
	
	private ArrayList<GoodThingObject> good_things = new ArrayList<GoodThingObject>();
	private GoodThingsAdapter adapter;

	public enum Filter {
		 ALL, WEEK, MONTH, YEAR;  
		}
	Filter filter = Filter.ALL;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse);

        init();
        firstRun(); 
	}

	@Override
    public void onResume(){
		super.onResume();
		showList();
	}

	@Override
    public void onPause(){
		super.onPause();
	}
	
	
	private void init() {

		pref = PreferenceManager.getDefaultSharedPreferences(this); // 0 - for private mode
		editor = pref.edit();
		listview_goodthings = (ListView) findViewById(R.id.listview_goodthings);
		
		layout_add_thing = (RelativeLayout) findViewById(R.id.layout_add_thing);
		layout_all = (RelativeLayout) findViewById(R.id.layout_all);
		layout_week = (RelativeLayout) findViewById(R.id.layout_week);
		layout_month = (RelativeLayout) findViewById(R.id.layout_month);
		layout_year = (RelativeLayout) findViewById(R.id.layout_year);
		
		layout_add_thing.setOnClickListener(this);
		layout_all.setOnClickListener(this);
		layout_week.setOnClickListener(this);
		layout_month.setOnClickListener(this);
		layout_year.setOnClickListener(this);
	}

	private void firstRun(){

		if (pref.getBoolean(FIRST_RUN, true))//Alarm has not been set. Set it for the first time
		{
	         Log.d("2GT", "Alarm set for first time");

			daily_alarm = new DailyAlarm(this, FIRST_ALARM_IN_SECONDS);
			
			editor.putInt(ALARM_TIME, FIRST_ALARM_IN_SECONDS);
			editor.putBoolean(FIRST_RUN, false);
			editor.commit();
			
			Intent explanation_intent = new Intent(this, ExplanationActivity.class);
			startActivity(explanation_intent);
		}
	}

	private void showList(){
		loadDatabase();
        displayGoodThings(filter);
	}
	
	private void loadDatabase() {

		db_manager = new DatabaseManager(getApplicationContext());
		db_manager.open();
		good_things.clear();
		good_things = db_manager.getAllEntries();
		db_manager.close();
	}

	private void displayGoodThings(Filter filter) {

		if (filter == Filter.ALL){
			
		} else if (filter == Filter.WEEK){
			for (int i = good_things.size()-1; i>=0; i--){
				if (!good_things.get(i).isWeek()){
					good_things.remove(i);
				}
			}
		} else if (filter == Filter.MONTH){
			for (int i = good_things.size()-1; i>=0; i--){
				if (!good_things.get(i).isMonth()){
					good_things.remove(i);
				}
			}
		} else if (filter == Filter.YEAR){
			for (int i = good_things.size()-1; i>=0; i--){
				if (!good_things.get(i).isYear()){
					good_things.remove(i);
				}
			}
		}
		
		adapter = new GoodThingsAdapter(this, good_things);
		listview_goodthings.setAdapter(adapter);
		listview_goodthings.setOnItemClickListener(this);
	}

	public void onClick(View view) {
		switch(view.getId()){
		case(R.id.layout_add_thing):
			Intent add_two_things_intent = new Intent(this, AddTwoGoodThingsActivity.class);
			startActivity(add_two_things_intent);
			break;
		case(R.id.layout_all):
			filter = Filter.ALL;
			layout_all.setBackgroundResource(R.drawable.button_gradient_down);
			layout_week.setBackgroundResource(R.drawable.button_gradient);
			layout_month.setBackgroundResource(R.drawable.button_gradient);
			layout_year.setBackgroundResource(R.drawable.button_gradient);
			showList();
			break;
		case(R.id.layout_week):
			filter = Filter.WEEK;
			layout_all.setBackgroundResource(R.drawable.button_gradient);
			layout_week.setBackgroundResource(R.drawable.button_gradient_down);
			layout_month.setBackgroundResource(R.drawable.button_gradient);
			layout_year.setBackgroundResource(R.drawable.button_gradient);
			showList();
			break;
		case(R.id.layout_month):
			filter = Filter.MONTH;
			layout_all.setBackgroundResource(R.drawable.button_gradient);
			layout_week.setBackgroundResource(R.drawable.button_gradient);
			layout_month.setBackgroundResource(R.drawable.button_gradient_down);
			layout_year.setBackgroundResource(R.drawable.button_gradient);
			showList();
			break;
		case(R.id.layout_year):			
			filter = Filter.YEAR;
			layout_all.setBackgroundResource(R.drawable.button_gradient);
			layout_week.setBackgroundResource(R.drawable.button_gradient);
			layout_month.setBackgroundResource(R.drawable.button_gradient);
			layout_year.setBackgroundResource(R.drawable.button_gradient_down);
			showList();
			break;
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{		
		Intent edit_intent = new Intent(this, EditThingActivity.class);

		edit_intent.putExtra(POSITION, position);
		edit_intent.putExtra(ID, good_things.get(position).getId());
		startActivityForResult(edit_intent, EDIT_RESULTS);
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		  MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.option_menu, menu);
		    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.settings:
	    	Intent settings_intent = new Intent(this, SettingsActivity.class);
	    	startActivityForResult(settings_intent, SETTINGS_RESULTS);
	    	break;
		}
		return true;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case SETTINGS_RESULTS:
        	updateAlarmDetails();
        	break;
        }
    }
		
	
	private void updateAlarmDetails(){
		
		//Load prefs
		boolean alarm_on = pref.getBoolean("DAILY_ALARM", true);

    	AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
    	Intent intent = new Intent(this, DailyAlarm.class);
    	PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    	try{
        	alarmMgr.cancel(pendingIntent);
    		Log.d("2GT", "Alarm Cancelled");
    	} catch(Exception e){
    		Log.d("2GT", "Error cancelling intent");
    	}
    	
		if (alarm_on){
			Calendar calendar = Calendar.getInstance();
			int alarm_time = pref.getInt("ALARM_TIME", 72000);
			daily_alarm = new DailyAlarm(this, alarm_time);
		}
	}
}
