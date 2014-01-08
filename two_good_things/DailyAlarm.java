package com.travis.awesome.two_good_things;


import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

public class DailyAlarm extends BroadcastReceiver {

	int time_of_day;
	
    public DailyAlarm(){ }

    public DailyAlarm(Context context, int time_of_day)
    {
    	this.time_of_day = time_of_day;
    	AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    	Intent intent = new Intent(context, DailyAlarm.class);
    	PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    	
    	Calendar time = Calendar.getInstance();
    	long current_time_of_day = time.get(Calendar.HOUR_OF_DAY) * 3600 +  time.get(Calendar.MINUTE) * 60 + time.get(Calendar.SECOND);

    	time.setTimeInMillis(System.currentTimeMillis());
    	time.set(Calendar.HOUR_OF_DAY, 0);
    	time.set(Calendar.MINUTE, 0);
    	time.set(Calendar.SECOND, 0);     
    	time.add(Calendar.SECOND, time_of_day);

    	if (time_of_day < current_time_of_day){ //setting the alarm for tomorrow as time has already passed today
    		Log.d("2GT", "Time: " + time_of_day + " has passed. Setting for tomorrow");
    		time.add(Calendar.HOUR, 24); 
    	}
    	alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), 86400000, pendingIntent);//Should repeat everyday

    	Date date = new Date(time.getTimeInMillis());
    	Log.d("2GT", "Alarm was set for: " + date.toString());
    }

     @Override
     public void onReceive(Context context, Intent intent) {
    	  
    	 SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context); 

         Intent intent2 = new Intent(context, AddTwoGoodThingsActivity.class);
         intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         intent2.putExtra("ALARM_SPAWNED", true);
         context.startActivity(intent2);

			if (pref.getBoolean("ALARM_VIBRATION", true)){
				Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(500);				
			}
			
			if (pref.getBoolean("ALARM_SOUND", true)){
				MediaPlayer mp = MediaPlayer.create(context, R.raw.alert_sound);
				mp.setOnCompletionListener(new OnCompletionListener() {
				 public void onCompletion(MediaPlayer mp) {
	                 mp.release();
	             }
				});   
				mp.start();
			}
      }
      
}
