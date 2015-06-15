package com.gravity.innovations.tasks.done;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends ActionBarActivity {
	Context mContext;
	private static final String PREF_USER_ACTION_BAR_COLOR = "actionbar_color";
	// orange else 1-5 for others
	private int mUserActionBarColor;
	Switch sharingSwitch, googleSyncSwitch, serverSyncSwitch;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		setContentView(R.layout.activity_settings);
	
		// declaration SharedPreferences
		final SharedPreferences settings = getSharedPreferences(
				"action_bar_color_settings", Context.MODE_PRIVATE);

		final SharedPreferences sync_settings = getSharedPreferences(
				"application_sync_settings", Context.MODE_PRIVATE);

		// set Action bar color
		mUserActionBarColor = settings.getInt(PREF_USER_ACTION_BAR_COLOR, 0);
		if (mUserActionBarColor == 1) {
			android.app.ActionBar mActionBar = getActionBar();
			mActionBar.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#4072b4")));// blue
		} else if (mUserActionBarColor == 2) {
			android.app.ActionBar mActionBar = getActionBar();
			mActionBar.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#1abc9c")));// torquise
		} else if (mUserActionBarColor == 3) {
			android.app.ActionBar mActionBar = getActionBar();
			mActionBar.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#f39c12")));// orange
		} else if (mUserActionBarColor == 4) {
			android.app.ActionBar mActionBar = getActionBar();
			mActionBar.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#000000")));// black
		} else if (mUserActionBarColor == 5) {
			android.app.ActionBar mActionBar = getActionBar();
			mActionBar.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#cecece")));// grey
		}

		// initializations
		sharingSwitch = (Switch) findViewById(R.id.Sharing);
		googleSyncSwitch = (Switch) findViewById(R.id.Google_Sync);
		serverSyncSwitch = (Switch) findViewById(R.id.Server_Sync);
		// theme selection and preferences setting
//		blue.setOnClickListener(new OnClickListener() {
//			@SuppressLint("NewApi")
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Log.e("BLUE circle", "pressed");
//				Toast.makeText(mContext, "Blue Theme Selected",
//						Toast.LENGTH_SHORT).show();
//				// storing preferences
//				SharedPreferences.Editor editor = settings.edit();
//				editor.putInt(PREF_USER_ACTION_BAR_COLOR, 1);
//				editor.commit();
//				// restart application
//				Intent i = getBaseContext().getPackageManager()
//						.getLaunchIntentForPackage(
//								getBaseContext().getPackageName());
//				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(i);
//
//			}
//		});


	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//overridePendingTransition( R.anim.slide_bottom_to_top, R.anim.slide_bottom_to_top);

	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
	}

}
