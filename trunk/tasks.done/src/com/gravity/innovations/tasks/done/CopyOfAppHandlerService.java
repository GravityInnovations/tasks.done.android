package com.gravity.innovations.tasks.done;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


public class CopyOfAppHandlerService extends IntentService {

	private NotificationManager mNotificationManager;
	private Intent thisIntent;
	int x = 0;
	public CopyOfAppHandlerService(String name) {
		super(name);
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try{
			thisIntent = intent;
			if(intent.getAction() == Common.serviceActions.START_APP)
			{
				sendNotification("App started", "my message"+x, 1111);
			}
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(),"Error: "+ex.toString(), 
					Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		int a = super.onStartCommand(intent, flags, startId);
		thisIntent = intent;
		sendNotification("App started", "my message"+x, 1111);
		return a;
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		
		super.onRebind(intent);
	}

	private void sendNotification(String Title, String Message, int RequestCode) {
		thisIntent = new Intent(this, CopyOfAppHandlerService.class);
		//Intent intent = new Intent(this, MainActivity.class);
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		
		PendingIntent contentIntent = PendingIntent.getService(this,
				RequestCode, thisIntent, 0);
 				
				
				//PendingIntent.getActivity(this, RequestCode,
				//thisIntent, /*PendingIntent.FLAG_UPDATE_CURRENT|*/ Intent.FILL_IN_DATA|Intent.FLAG_ACTIVITY_NEW_TASK);
						//| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		
		// use the right class it should be called from the where alarms are set
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(Title)
				.setStyle(
						new NotificationCompat.BigTextStyle()
								.bigText(Message))
				.setDefaults(
						Notification.DEFAULT_SOUND
								| Notification.DEFAULT_LIGHTS
								| Notification.DEFAULT_VIBRATE
								| Notification.DEFAULT_ALL)
				.setContentText(Message); 
		//mBuilder.addAction(R.drawable.ic_action_cancel, "Dismiss", contentIntent);
		//mBuilder.addAction(R.drawable.ic_action_accept, "Done", contentIntent);
		x++;
		
		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(RequestCode, mBuilder.build());
	
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
