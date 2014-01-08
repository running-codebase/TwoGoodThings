package com.travis.awesome.two_good_things;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.travis.awesome.two_good_things.adapters.GoodThingsAdapter;
import com.travis.awesome.two_good_things.objects.GoodThingObject;
import com.travis.awesome.two_good_things.util.DatabaseManager;

public class ChooseBestActivity extends Activity implements OnItemClickListener, OnClickListener {
	
	private ListView listview_goodthings;
	private Button btn_ok;
	private Button btn_cancel;

	private Intent intent;
	private DatabaseManager db_manager; 	
	private ArrayList<GoodThingObject> good_things = new ArrayList<GoodThingObject>();
	private GoodThingsAdapter adapter;
	private int best_of_type;
	private ArrayList<Integer> selected_values = new ArrayList<Integer>();
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_best_two_activity);

        init();
        getExtras();
        showList();
        showDialog();
	
	}

	
	public void init(){
		listview_goodthings = (ListView) findViewById(R.id.listview_goodthings_menu);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		
		listview_goodthings.setOnItemClickListener(this);
		btn_ok.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
	}

	public void getExtras(){

		intent = getIntent();
		best_of_type = intent.getIntExtra("TYPE", -1);		
	}

	private void showList(){
		loadDatabase();
        displayGoodThings();
	}
		
	private void loadDatabase() {

		db_manager = new DatabaseManager(getApplicationContext());
		db_manager.open();
		good_things.clear();
		good_things = db_manager.getAllEntries();
		db_manager.close();
		
		
	}

	private void displayGoodThings() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		
		if (best_of_type == -1 ){
			Log.d("2GT", "Choose best missing Intent Extras");
		} else if (best_of_type == 0 ){//week
			//show only this week
			//calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			calendar.add(Calendar.DAY_OF_YEAR, -7);
			
			for (int i = good_things.size()-1; i>=0; i--){
				if (Long.decode(good_things.get(i).getTimestamp()) < calendar.getTimeInMillis()){
					good_things.remove(i);
				}
			}
			
		} else if (best_of_type == 1 ){//month
			calendar.set(Calendar.DAY_OF_MONTH, 1);

			for (int i = good_things.size()-1; i>=0; i--){
				Log.d("2GT", "adding things from the last seven days");
					if (!good_things.get(i).isWeek() || Long.decode(good_things.get(i).getTimestamp()) < calendar.getTimeInMillis()){
					good_things.remove(i);					
				} 
			}
			
		} else if (best_of_type == 2 ){//year
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.MONTH, Calendar.JANUARY);

			for (int i = good_things.size()-1; i>=0; i--){
				if (!good_things.get(i).isWeek() || !good_things.get(i).isMonth() || Long.decode(good_things.get(i).getTimestamp()) < calendar.getTimeInMillis()) {
					good_things.remove(i);					
				}
			}
		} 
		
		adapter = new GoodThingsAdapter(this, good_things);
		listview_goodthings.setAdapter(adapter);
		listview_goodthings.setOnItemClickListener(this);
	}

	public void showDialog(){
		
		String message;
		if (best_of_type == 0){
			message = getString(R.string.choose_best_week);
		} else if (best_of_type == 1){
			message = getString(R.string.choose_best_month);
		} else if (best_of_type == 2){
			message = getString(R.string.choose_best_year);			
		} else {
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setMessage(message)
	       .setTitle(R.string.choose_best_title);
		
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               dialog.dismiss();
	           }
	       });
		
		builder.create().show();
	}
	
	public void onClick(View view) {
		
		switch(view.getId()){
			case(R.id.btn_ok):
				db_manager.open();
				for (int i = 0; i<selected_values.size(); i++){
					if (best_of_type == 0) {
						db_manager.insertEntry(good_things.get(selected_values.get(i)).getText(), good_things.get(selected_values.get(i)).getTimestamp(), true, good_things.get(selected_values.get(i)).isMonth(), good_things.get(selected_values.get(i)).isYear());
					} else if (best_of_type == 1) {
						db_manager.insertEntry(good_things.get(selected_values.get(i)).getText(), good_things.get(selected_values.get(i)).getTimestamp(), good_things.get(selected_values.get(i)).isWeek(), true, good_things.get(selected_values.get(i)).isYear());						
					} else if (best_of_type == 2) {
						db_manager.insertEntry(good_things.get(selected_values.get(i)).getText(), good_things.get(selected_values.get(i)).getTimestamp(), good_things.get(selected_values.get(i)).isWeek(), good_things.get(selected_values.get(i)).isMonth(), true);						
					} 
					db_manager.removeEntry(Integer.parseInt(good_things.get(selected_values.get(i)).getId()));
				}
				db_manager.close();
				finish();
				break;
			case(R.id.btn_cancel):
				finish();
				break;
		}
		
	}


	public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

		if (selected_values.contains(position)){
			view.setBackgroundColor(Color.BLACK);
			selected_values.remove(position);
		} else {
			view.setBackgroundColor(Color.YELLOW);
			selected_values.add(position);
		}
	}
}
