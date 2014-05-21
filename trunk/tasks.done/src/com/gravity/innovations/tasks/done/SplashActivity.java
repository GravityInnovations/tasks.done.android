package com.gravity.innovations.tasks.done;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends Activity {
	private ProgressBar progress1;
	private ProgressBar progress2;
	private ProgressBar progress3;
	private TextView status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		//load ui
		
		progress1 = (ProgressBar)findViewById(R.id.progressBar1);
		progress2 = (ProgressBar)findViewById(R.id.progressBar2);
		progress3 = (ProgressBar)findViewById(R.id.progressBar3);
		status = (TextView)findViewById(R.id.txt_status);
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
		/*
		 new Handler().postDelayed(new Runnable() {
			 
	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	            
	 
	            @Override
	            public void run() {
	                // This method will be executed once the timer is over
	                // Start your app main activity
	                Intent i = new Intent(SplashActivity.this, AuthenticationActivity.class);
	                startActivity(i);
	                //startActivityForResult(i, Common.RequestCodes.SPLASH_AUTH);
	                // close this activity
	                finish();
	            }
	        }, Common.SPLASH_TIME_OUT);
		 */ 
		 
		 //1 - Check Internet
		status.setText(R.string.check_internet);
		if(Common.hasInternet(this))
		{
			progress1.setProgress(40);
			
		}
		else
		{
			status.setText(R.string.require_internet);
			progress1.setProgress(30);
		}
		//2 - Load Prefs
		status.setText(R.string.load_sp);
		
		//3 - Connect with google - recheck internet within
		status.setText(R.string.get_google_acc);
		Intent i = new Intent(SplashActivity.this, AuthenticationActivity.class);
		
		startActivityForResult(i, Common.RequestCodes.SPLASH_AUTH);
		 //4 - Load Db data
		 //5 - Every thing else
	}
	@SuppressLint("NewApi")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	   Bundle mBundle = data.getExtras(); 
		switch(requestCode)
	     {
	     
	     	case  Common.RequestCodes.SPLASH_AUTH:
	     		if(mBundle.getString(Common.USER_EMAIL) != null
	     			&& resultCode == Activity.RESULT_OK)
	     		{
	     			//save to sp
	     			status.setText(mBundle.getString(Common.USER_EMAIL)+R.string.got_email_success_full);
	     		}
	     		else if(mBundle.getString(Common.USER_EMAIL) != null
		     			&& mBundle.getString(Common.EXCEPTION) != null)
		     		{
		     			
		     			status.setText(mBundle.getString(Common.USER_EMAIL)+R.string.got_email_success_half);
		     		}
	     		
		    	 if(resultCode == Activity.RESULT_CANCELED)
		    	 {
		    		 progress1.setAlpha((float) 0.4);
		    		 progress1.setProgress(80);
		    	 }
		    	 else if(resultCode == Activity.RESULT_OK)
		    	 progress1.setProgress(100);
	    	 break;
	     }
	}
}

