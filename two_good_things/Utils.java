package com.travis.awesome.two_good_things;

import java.util.Calendar;

public class Utils {
	//General helper functions
	
	public static int timeAfterMidnight(){ 
    	Calendar total_time = Calendar.getInstance();
    	Calendar time_before_midnight = Calendar.getInstance();
		time_before_midnight.set(Calendar.HOUR_OF_DAY, 0);
		time_before_midnight.set(Calendar.MINUTE, 0);
		time_before_midnight.set(Calendar.SECOND, 0);     

		return (int)(total_time.getTimeInMillis()/1000 - time_before_midnight.getTimeInMillis()/1000);
		
	}
	
}
