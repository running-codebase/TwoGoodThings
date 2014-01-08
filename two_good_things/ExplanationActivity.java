package com.travis.awesome.two_good_things;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ExplanationActivity extends Activity implements OnClickListener {
	
	Button btn_ok;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explanation_activity);

        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
	}

	public void onClick(View view) {
		if (view.getId() == R.id.btn_ok){
			Intent add_two_things_intent = new Intent(this, AddTwoGoodThingsActivity.class);
			startActivity(add_two_things_intent);
			finish();
		}
	}

}
