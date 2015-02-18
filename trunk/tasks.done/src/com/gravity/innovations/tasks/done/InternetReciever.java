package com.gravity.innovations.tasks.done;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class InternetReciever extends BroadcastReceiver{

	private AppHandlerService mService;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		try{
			mService = ((TheApp) context.getApplicationContext()).getService();//.startService((Common.Callbacks.ServiceCallback)context);
			if(mService!=null)
			{
				if(Common.hasInternet(context))
					mService.hasInternet = true;
				else{
					mService.hasInternet = false;
//					if(mService.AppStateClassName == SplashActivity.class.getName()){
//						//mService.stopSelf();
//						boolean b = ((TheApp) context.getApplicationContext()).stopService();
//						//Toast.makeText(context, b+"",Toast.LENGTH_LONG).show(); 
//					}
				}
			}
			
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getMessage(),Toast.LENGTH_LONG).show(); 
		}
//		if(Common.hasInternet(context))
//		 {
//		   Toast.makeText(context, "Network Available",Toast.LENGTH_LONG).show(); 
//		 }
//		else
//			Toast.makeText(context, "No Network",Toast.LENGTH_LONG).show();

	}

}
