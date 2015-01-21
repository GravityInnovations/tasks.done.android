package com.gravity.innovations.tasks.done;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class AlarmSchedulingService extends IntentService {
	DatabaseHelper db;
	Context mContext;

	public AlarmSchedulingService() {
		super("SchedulingService");
	}

	public int NOTIFICATION_ID;
	private NotificationManager mNotificationManager;
	//NotificationCompat.Builder builder;
	// Bundle extras;
	private static final String PREF_TASKLIST_ID_PRESSED = "tasklist_pressed_id";
	private static final String PREF_TASK_ID_FOR_TASKLIST_PRESSED = "task_id";

	@SuppressLint("NewApi")
	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			//String alarm_id=intent.getAction();
			//int alarmID=Integer.parseInt(alarm_id);
			String alarm_id = intent.getAction();
			// Get passed values
			Bundle extras = intent.getExtras();
			// int uniqueID = extras.getInt("alarm_id");
			String uniqueIDDsS = extras.getString("alarmMgr_id");
			// String uniqueIDDS = extras.getString("alarm_id");
			// int alarmMgr_id = //extras.getInt("alarmMgr_id"); //success but
			// we need to check wake lock and parcelable or searializable data
			NOTIFICATION_ID = Integer.parseInt(uniqueIDDsS);// alarmMgr_id, alarmID;
			sendNotification(Integer.parseInt(uniqueIDDsS));
			// Release the wake lock provided by the BroadcastReceiver.
			AlarmBroadcastReciever.completeWakefulIntent(intent);
		} catch (Exception e) {
			String ex = e.getLocalizedMessage();
		}
	}

	// Post a notification
	public void sendNotification(int alarm_id) {
		mContext = getApplicationContext();
		db = new DatabaseHelper(mContext);
		TaskModel task = db.tasks.Get(alarm_id);//.Task_Single(alarm_id);
		String alarmTitle = task.title;
		String alarmMessage = task.details;

		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		int id = task.fk_tasklist_id;
		// will use this if later to open the tasklist containg notifation task

		// on notification press open task
		/*
		final SharedPreferences tasklistID = getSharedPreferences(
				PREF_TASKLIST_ID_PRESSED, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = tasklistID.edit();
		editor.putInt(PREF_TASKLIST_ID_PRESSED, id);
		editor.commit();
		final SharedPreferences taskID = getSharedPreferences(
				PREF_TASK_ID_FOR_TASKLIST_PRESSED, Context.MODE_PRIVATE);
		editor = taskID.edit();
		editor.putInt(PREF_TASK_ID_FOR_TASKLIST_PRESSED, task._id);
		editor.commit();
		 */
		// on notification press open task

		// Intent intent = new Intent(mContext, MainActivity.class);
		// //intent.putExtra("alarmMgr_id", alarm_id);
		// intent.putExtra("openTaskListWithID", id);//doing this to open the
		// very similar tasklist

//		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	//			new Intent(this, MainActivity.class),
		//		Intent.FLAG_ACTIVITY_NEW_TASK);
		
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("_task_list_id", task.fk_tasklist_id);
		intent.putExtra("_task_id", task._id);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, /*PendingIntent.FLAG_UPDATE_CURRENT|*/ Intent.FILL_IN_DATA|Intent.FLAG_ACTIVITY_NEW_TASK);
						//| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		
		// use the right class it should be called from the where alarms are set
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(alarmTitle)
				.setStyle(
						new NotificationCompat.BigTextStyle()
								.bigText(alarmMessage))
							
				.setDefaults(
						Notification.DEFAULT_SOUND
								| Notification.DEFAULT_LIGHTS 
								| Notification.FLAG_ONGOING_EVENT
								| Notification.FLAG_FOREGROUND_SERVICE
								)
								
				.setContentText(alarmMessage);
		//mBuilder.addAction(R.drawable.ic_action_cancel, "Dismiss", contentIntent);
		//mBuilder.addAction(R.drawable.ic_action_accept, "Done", contentIntent);
		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}
