package com.gravity.innovations.tasks.done;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);
//		Thread timer = new Thread() {
//			public void run() {
//				try {
//					sleep(2000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} finally {
//					Intent openMainActivity = new Intent(SplashActivity.this, MainActivity.class);
//					startActivity(openMainActivity);
//				}
//			}
//		};
//		timer.start();
		 new Handler().postDelayed(new Runnable() {
			 
	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */
	 
	            @Override
	            public void run() {
	                // This method will be executed once the timer is over
	                // Start your app main activity
	                Intent i = new Intent(SplashActivity.this, MainActivity.class);
	                startActivity(i);
	 
	                // close this activity
	                finish();
	            }
	        }, Common.SPLASH_TIME_OUT);
	}
	@Override
	protected void onPause() {
	    super.onPause();
	    finish();
	}
}

