package com.travis.awesome.two_good_things.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.travis.awesome.two_good_things.R;


public class AlarmReciever extends BroadcastReceiver {
	
	private static final int NOTIFICATION_ID = 403948;
	private Context context;
	private int type;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		this.context = context;
		type = intent.getIntExtra("TYPE", 0);

		if (type == 0) {
			startActivity();
			
		}
	}
	
	
	private void startActivity() {
		
	}
	
	
	
}
