package com.example.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class SplashActivity extends Activity{
	MediaPlayer countD;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		countD = MediaPlayer.create(SplashActivity.this, R.raw.starwars_countdown);
		SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext()); 
		boolean music = getPrefs.getBoolean("checkbox", true);
		if(music == true){
			countD.start();
		}
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(1000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent openMainActivity = new Intent ("com.splash.MENU");
					startActivity(openMainActivity);
				}
			}
		};
		timer.start();
	}

 
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		countD.release();
		finish();
	}
	

}
