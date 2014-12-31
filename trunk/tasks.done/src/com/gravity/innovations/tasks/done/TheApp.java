package com.gravity.innovations.tasks.done;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NotificationCompat;

public class TheApp extends Application {
	private AppHandlerService HandleService;
	private NotificationManager mNM;
	@Override
	public void onCreate() {
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		// TODO Auto-generated method stub
		super.onCreate();
		//showNotification(1000, "App created", new Intent());
		
	}
	public void startService(final Common.Callbacks.ServiceCallback caller)
	{
		Intent i = new Intent(this, AppHandlerService.class);
		i.setAction(Common.serviceActions.START_APP);
		
		ComponentName x = getApplicationContext().startService(i);
		getApplicationContext().bindService(i, new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				HandleService = null;
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				
				HandleService = ((AppHandlerService.AppHandlerBinder)service).getService();
				caller.onServiceBound(HandleService);
				
			}

		}, Context.BIND_AUTO_CREATE);
	}
	public void setService(AppHandlerService service)
	{
		this.HandleService = service;
	}
	public AppHandlerService getService()
	{
		return this.HandleService;
	}
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		showNotification(2000, "Terminated", new Intent());
		super.onTerminate();
	}
	public void showNotification(int id, String Text, Intent i) {
		//if(isStatusChanged){
long[] a = {};
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				i,
				Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT |Notification.FLAG_ONLY_ALERT_ONCE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("task.done")
				.setStyle(
						new NotificationCompat.BigTextStyle()
								.bigText("task.done"))
				.setDefaults(
						
								 Notification.DEFAULT_LIGHTS
								|Notification.FLAG_ONLY_ALERT_ONCE
								| Notification.FLAG_NO_CLEAR
								| Notification.FLAG_ONGOING_EVENT)
				.setContentText(Text).setVibrate(a);
		mBuilder.setContentIntent(contentIntent);
		mBuilder.setOngoing(true);
		
		
		mNM.notify(id, mBuilder.build());
	//}
	}
	

}
