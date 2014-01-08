package com.travis.awesome.two_good_things.objects;

public class GoodThingObject {
	
	private String id;
	private String text;
	private String timestamp;
	private boolean  week = false;
	private boolean  month = false;
	private boolean  year = false;
	
	
	public GoodThingObject() {
		
	}
	
	public GoodThingObject(String text, String timestamp, boolean week, boolean month, boolean year) {
		
		this.text = text;
		this.timestamp = timestamp;
		this.week = week;
		this.month = month;
		this.year = year;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public boolean isWeek() {
		return week;
	}
	public void setWeek(boolean week) {
		this.week = week;
	}
	public boolean isMonth() {
		return month;
	}
	public void setMonth(boolean month) {
		this.month = month;
	}
	public boolean isYear() {
		return year;
	}
	public void setYear(boolean year) {
		this.year = year;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
