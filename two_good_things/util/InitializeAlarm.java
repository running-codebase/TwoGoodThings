package com.travis.awesome.two_good_things.util;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class InitializeAlarm {

	public static final long  DAY_IN_MILLISECONDS =  86400000;
	private static final int UPDATE_ALARM_CODE = 403948;

	private Context context;
	Intent update_intent = new Intent(context, AlarmReciever.class);
	
	PendingIntent pending_update_intent = PendingIntent.getBroadcast(
			context, UPDATE_ALARM_CODE, update_intent, 
			PendingIntent.FLAG_UPDATE_CURRENT);
	AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	
	Calendar calendar;
	
	public InitializeAlarm(Context context, long time, int type) {
		this.context = context;
		
		update_intent.putExtra("TYPE", type);
		
		calendar = Calendar.getInstance();
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);

    	long next_alarm_time = 0;

    	
    	next_alarm_time = calendar.getTimeInMillis() + time;
    	
    	alarm.setRepeating(AlarmManager.RTC_WAKEUP,
    			next_alarm_time, 
    			DAY_IN_MILLISECONDS, pending_update_intent);		
	}
	
	
	
	
	
}
