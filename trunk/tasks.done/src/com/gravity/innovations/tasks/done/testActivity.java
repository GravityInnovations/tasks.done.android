package com.gravity.innovations.tasks.done;


import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;


public class testActivity extends Activity {
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//
		setContentView(R.layout.activity_splash);
		try{
		
		Intent i = getIntent();
		Bundle b = i.getBundleExtra("service");
		
		//AppHandlerService.MyBinder mb= (AppHandlerService.MyBinder)b.getBinder("mybinder");
		
		//AppHandlerService s = mb.getService();
		//s.sendNotification("t", "m", 222);
		//Toast.makeText(getApplicationContext(),x.flattenToString(), 
			//	Toast.LENGTH_LONG).show();
		

		
		
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(),"Error: "+ex.toString(), 
					Toast.LENGTH_LONG).show();
		}
		//
	}
	
	
	
}
