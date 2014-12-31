package com.gravity.innovations.tasks.done;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;


public class initHandlerActivity extends Activity {
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        try{
//    		Intent i = new Intent(this, AppHandlerService.class);
//    		i.setAction(Common.serviceActions.START_APP);
//    		
//    		ComponentName x = getApplicationContext().startService(i);
//    		bindService(i, new ServiceConnection() {
//    			
//    			@Override
//    			public void onServiceDisconnected(ComponentName name) {
//    				// TODO Auto-generated method stub
//    				
//    			}
//    			
//    			@Override
//    			public void onServiceConnected(ComponentName name, IBinder service) {
//    				// TODO Auto-generated method stub
//    				
//    			}
//    		}, Context.BIND_AUTO_CREATE);
    		//Toast.makeText(getApplicationContext(),x.flattenToString(), 
    			//	Toast.LENGTH_LONG).show();
    		

    		Intent AppStateIntent = new Intent(this, SplashActivity.class);
    		startActivity(AppStateIntent);
    		finish();
    		}
    		catch(Exception ex)
    		{
    			Toast.makeText(getApplicationContext(),"Error: "+ex.toString(), 
    					Toast.LENGTH_LONG).show();
    		}
        //bindService(intent, mConnection, 0);
      
        
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
	}
	
	
	
}
