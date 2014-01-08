package com.travis.awesome.two_good_things;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.d("2GT", "The boot reciever was triggered");
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		int time_of_day = pref.getInt("ALARM_TIME", 72000);
		DailyAlarm daily_alarm = new DailyAlarm(context, time_of_day);
	}

}
