package com.travis.awesome.two_good_things.adapters;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.travis.awesome.two_good_things.R;
import com.travis.awesome.two_good_things.objects.GoodThingObject;

public class GoodThingsAdapter extends BaseAdapter{
	
	private Activity activity;
	private ArrayList<GoodThingObject> good_things;
	private static LayoutInflater inflater=null;


	public GoodThingsAdapter(Activity a, ArrayList<GoodThingObject> good_things) {

		this.activity = a;
		this.good_things = good_things;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		//Log.d("GoodThings", "Made it here. size: " + good_things.size());
	}



	public int getCount() {
		return good_things.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder{
		public TextView txt_text;
		public TextView txt_time;
		public RelativeLayout layout_top_choice_holder;
		public ImageView img_bronze;
		public ImageView img_silver;
		public ImageView img_gold;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View vi=convertView;
        ViewHolder holder;
            vi = inflater.inflate(R.layout.listview_item, null);
            holder = new ViewHolder();
            
            
            holder.txt_text = (TextView) vi.findViewById(R.id.txt_text);
            holder.txt_time = (TextView) vi.findViewById(R.id.txt_time);
            holder.layout_top_choice_holder = (RelativeLayout) vi.findViewById(R.id.layout_top_choice_holder);
            holder.img_bronze = (ImageView) vi.findViewById(R.id.img_bronze);
            holder.img_silver = (ImageView) vi.findViewById(R.id.img_silver);
            holder.img_gold = (ImageView) vi.findViewById(R.id.img_gold);
            

        String text = good_things.get(position).getText();
        String time = good_things.get(position).getTimestamp();
        boolean bronze = good_things.get(position).isWeek();
        boolean silver = good_things.get(position).isMonth();
        boolean gold = good_things.get(position).isYear();
        holder.txt_text.setText(text);
        holder.txt_time.setText(formatTime(time));

        if (bronze){
        	holder.img_bronze.setImageResource(R.drawable.bronze_star);
        } else {
        	holder.img_bronze.setImageResource(R.drawable.empty_star);        	
        }
        	
        
        if (silver){
        	holder.img_silver.setImageResource(R.drawable.silver_star);
        } else {
        	holder.img_silver.setImageResource(R.drawable.empty_star);        	
        	
        }

        if (gold){
        	holder.img_gold.setImageResource(R.drawable.gold_star);
        } else {
        	holder.img_gold.setImageResource(R.drawable.empty_star);        	        	
        }
        
        return vi;
	}
	
	
	private String formatTime(String time) {

		Long milli = Long.decode(time);
		Date date = new Date(milli);
		
		return date.toString().substring(0,date.toString().length()-18);
	}
	
	


}











