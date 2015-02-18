package com.gravity.innovations.tasks.done;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;

public class NotificationHandler {

	private NotificationManager mNM;
	private Context mContext;

	public NotificationHandler(Context mContext) {
		this.mContext = mContext;
		mNM = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void showNotification(int id, String Text, Intent i) {
		// if(isStatusChanged){
		int NOTIFICATION_ID = 123;
		long[] a = {};
		PendingIntent contentIntent = PendingIntent.getService(mContext, 0, i,
				Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT
						| Notification.FLAG_ONLY_ALERT_ONCE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mContext)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("task.done")
				.setStyle(
						new NotificationCompat.InboxStyle().setBigContentTitle("task.done")
						.setSummaryText(Text)
						)
						//.BigTextStyle()
								//.bigText(Text))
				.setDefaults(

						Notification.DEFAULT_LIGHTS
								| Notification.FLAG_ONLY_ALERT_ONCE
								| Notification.FLAG_NO_CLEAR
								| Notification.FLAG_ONGOING_EVENT)
				.setContentText(Text).setVibrate(null);
		
			mBuilder.setContentIntent(contentIntent);

			mBuilder.setOnlyAlertOnce(true);
		mBuilder.setOngoing(true);
		mBuilder.setContentInfo("5 pending tasks");
//		Bitmap b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_action_about);
//		mBuilder.setLargeIcon(b);
		mBuilder.setProgress(100,1, true);
		mBuilder.addAction(android.R.drawable.ic_menu_close_clear_cancel, "Force stop", contentIntent);
		mBuilder.addAction(android.R.drawable.ic_menu_send, "Launch App", contentIntent);
		
		mNM.notify(id, mBuilder.build());
		// }
	}
	public void showAlertNotification(int id, String Text, Intent i, Bitmap icon ) {
		// if(isStatusChanged){
		PendingIntent contentIntent = PendingIntent.getService(mContext, 0, i,
				Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT
						| Notification.FLAG_ONLY_ALERT_ONCE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mContext)
				.setSmallIcon(R.drawable.ic_launcher).setLargeIcon(ImageGridAdapter.getRoundedCornerBitmap(icon))
				.setContentTitle("task.done")
				.setStyle(
						new NotificationCompat.InboxStyle().setBigContentTitle("task.done")
						.setSummaryText(Text)
						)
						//.BigTextStyle()
								//.bigText(Text))
				.setDefaults(

						Notification.DEFAULT_LIGHTS
								| Notification.FLAG_ONLY_ALERT_ONCE
								| Notification.FLAG_NO_CLEAR
								| Notification.FLAG_ONGOING_EVENT)
				.setContentText(Text);
		
			mBuilder.setContentIntent(contentIntent);
		mBuilder.setAutoCancel(true);
//		Bitmap b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_action_about);
//		mBuilder.setLargeIcon(b);
		mNM.notify(id, mBuilder.build());
		// }
	}

}
