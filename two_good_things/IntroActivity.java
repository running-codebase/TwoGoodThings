package com.travis.awesome.two_good_things;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class IntroActivity extends Activity implements OnClickListener {
    
	private RelativeLayout layout_browse;
	private RelativeLayout layout_prefs;

	Intent intent;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        init();
	}
    
	private void init() {
		
		layout_browse = (RelativeLayout) findViewById(R.id.layout_browse);
		layout_prefs = (RelativeLayout) findViewById(R.id.layout_prefs);
		layout_browse.setOnClickListener(this);
		layout_prefs.setOnClickListener(this);
		
	}

	public void onClick(View view){
		switch (view.getId()){
		case(R.id.layout_browse):
			intent = new Intent(IntroActivity.this, BrowseActivity.class);
			startActivity(intent);
			break;
		case(R.id.layout_prefs):
			intent = new Intent(IntroActivity.this, PrefsActivity.class);
			startActivity(intent);
			break;
		
		}
	}
}