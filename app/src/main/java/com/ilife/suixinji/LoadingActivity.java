package com.ilife.suixinji;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;

public class LoadingActivity extends Activity implements Runnable{
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
		if (spf.getBoolean("firstRun", true)) {
			startActivity(new Intent(this, GuideActivity.class));
			finish();
			return;
		}

		setContentView(R.layout.activity_loading);
		mHandler.postDelayed(this, 3000);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.loading, menu);
		return true;
	}
	
	public void goMain(View v){
		mHandler.removeCallbacks(this);
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}

	@Override
	public void run() {
		goMain(null);
	}

}
