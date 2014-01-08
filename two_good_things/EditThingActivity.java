package com.travis.awesome.two_good_things;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.travis.awesome.two_good_things.objects.GoodThingObject;
import com.travis.awesome.two_good_things.util.DatabaseManager;

public class EditThingActivity extends Activity implements OnClickListener {
	
	private Intent intent;
	private DatabaseManager db_manager;
	
	private TextView txt_date;
	private EditText edittxt_thing1;
	private CheckBox chckbx_week;
	private CheckBox chckbx_month;
	private CheckBox chckbx_year;
	private Button btn_ok;
	private Button btn_remove;
	

	private int good_thing_position;
	private String good_thing_id;
	private String timestamp;
	private ArrayList<GoodThingObject> good_things = new ArrayList<GoodThingObject>();
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_thing_activity);

        init();
        getExtras();
        loadEntry();
        populateActivity();
	}
	

	
	private void init(){
		txt_date = (TextView) findViewById(R.id.txt_date);
		edittxt_thing1 = (EditText) findViewById(R.id.edittxt_thing1);
		chckbx_week = (CheckBox) findViewById(R.id.chckbx_week);
		chckbx_month = (CheckBox) findViewById(R.id.chckbx_month);
		chckbx_year = (CheckBox) findViewById(R.id.chckbx_year);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_remove = (Button) findViewById(R.id.btn_remove);

		btn_ok.setOnClickListener(this);
		btn_remove.setOnClickListener(this);
	}
	
	private void getExtras(){
	
		intent = getIntent();
		good_thing_id = intent.getStringExtra("ID");
		good_thing_position = intent.getIntExtra("POSITION", -1);
	}
	
	private void loadEntry(){
		
		db_manager = new DatabaseManager(getApplicationContext());
		db_manager.open();
		good_things = db_manager.getAllEntries();
		db_manager.close();
	}

	private void populateActivity(){
		
		if(good_thing_position == -1 || (good_things.size()-1)<good_thing_position){
			finish();
		}
		
		timestamp = good_things.get(good_thing_position).getTimestamp();
		txt_date.setText(formatTime(timestamp));
		edittxt_thing1.setText(good_things.get(good_thing_position).getText());
		chckbx_week.setChecked(good_things.get(good_thing_position).isWeek());
		chckbx_month.setChecked(good_things.get(good_thing_position).isMonth());
		chckbx_year.setChecked(good_things.get(good_thing_position).isYear());
	}

	private void insertDatabase(String input){
		if (input =="")
			return;
		db_manager.open();
		db_manager.insertEntry(input, timestamp, chckbx_week.isChecked(), chckbx_month.isChecked(), chckbx_year.isChecked());
		db_manager.close();
	}
	
	private void removeEntryFromDatabase(String id){
		if (id=="")
		{
			Log.d("2GT", "The id was null. Could not remove from database.");
			return;
		}
		
		db_manager.open();
		db_manager.removeEntry(Integer.parseInt(id));
		db_manager.close();
	}

	

	public void onClick(View view) {
		switch(view.getId()){
		case(R.id.btn_ok):
			insertDatabase(edittxt_thing1.getText().toString());
			removeEntryFromDatabase(good_thing_id);
			finish();
			break;
		case(R.id.btn_remove):
			removeEntryFromDatabase(good_thing_id);
			Log.d("2GT", "Removing: " + good_thing_id + " from database");
			finish();
			break;
		}
	}
	private String formatTime(String time) {

		Long milli = Long.decode(time);
		Date date = new Date(milli);
		
		return date.toString().substring(0,date.toString().length()-18);
	}

}
