package com.travis.awesome.two_good_things;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity   {
	
	private SharedPreferences pref;
	private Editor editor;
	
	private EditText edittxt_time;
	private CheckBox chkbx_daily_alarm;
	private CheckBox chkbx_vibration;
	private CheckBox chkbx_sound;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        loadPreferences();
        init();
        
	}

	public void loadPreferences() {
		pref = PreferenceManager.getDefaultSharedPreferences(this); 
		editor = pref.edit();
	}
	
	public void init() {
		edittxt_time = (EditText) findViewById(R.id.edittxt_time);
		chkbx_daily_alarm = (CheckBox) findViewById(R.id.chkbx_daily_alarm);
		chkbx_vibration = (CheckBox) findViewById(R.id.chkbx_vibration);
		chkbx_sound = (CheckBox) findViewById(R.id.chkbx_sound);
		
		String time = secondsToHHMM(pref.getInt("ALARM_TIME", 72000));
		
		edittxt_time.setText("" + (time));
		chkbx_daily_alarm.setChecked(pref.getBoolean("DAILY_ALARM", true));
		chkbx_vibration.setChecked(pref.getBoolean("ALARM_VIBRATION", true));
		chkbx_sound.setChecked(pref.getBoolean("ALARM_SOUND", true));
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("2GT", "OnPause called");
		
		//Save the preferences
		String time = edittxt_time.getText().toString();        		
		boolean valid_input = true;
		String error = ""; 
		
		if (time.length()!=4){
			valid_input = false;
			error = "Wrong amount of characters";
		}
		
		try{
			int value;
			value = Integer.parseInt(time);
		} catch(Exception e) {
			valid_input = false;
			error = "Not a number";
		}
		
		
		if (valid_input){
			int value;
			value = HHMMToSeconds(time);
			editor.putInt("ALARM_TIME", value);
		} else {
			Toast.makeText(this, "Incorrect time value. Value not updated", Toast.LENGTH_LONG).show();
		}
		
		
		editor.putBoolean("DAILY_ALARM", chkbx_daily_alarm.isChecked());
		editor.putBoolean("ALARM_VIBRATION", chkbx_vibration.isChecked());
		editor.putBoolean("ALARM_SOUND", chkbx_sound.isChecked());

		editor.commit();
	}
	
	private String secondsToHHMM(int input){
		int hours = input/3600;
		int mins = input%3600/60;
		String output;
		Log.d("2GT", "input: " + input + " hours: " + hours + " mins: " + mins);
	
		if (hours<10) {
			if (mins<10){
				output = "0" + hours + "0" + mins;
			} else {
				output = "0" + hours + mins;				
			}
		} else {
			if (mins<10){
				output = "" + hours + "0" + mins;
			} else {
				output = "" + hours + mins;
			}
		}
		
		return output;
	}

	private int HHMMToSeconds(String input){
	
		int hours = (Integer.parseInt(input.substring(0, 2)));
		int mins = (Integer.parseInt(input.substring(2)));
		Log.d("2GT", "HHMM to seconds= input: " + input + " hours: " + hours + " mins: " + mins);
		int output = hours*3600 + mins*60;

		if (output>(86400)){ //More then one day incorrect time
			return 86400;
		} else {
			return output;
		}
	}

}




