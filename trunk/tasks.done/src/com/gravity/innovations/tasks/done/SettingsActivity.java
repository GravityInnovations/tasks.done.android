package com.gravity.innovations.tasks.done;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends  ActionBarActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}
	@Override
    public void onBackPressed() {
           super.onBackPressed();
        
    }

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
	}
	
	

}
