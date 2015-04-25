package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
import android.util.Log;

public class AlarmSchedulingService extends IntentService {
	DatabaseHelper db;
	Context mContext;

	public AlarmSchedulingService() {
		super("SchedulingService");
	}

	public int NOTIFICATION_ID;
	private NotificationManager mNotificationManager;
	private AlarmBroadcastReciever mAlarmBroadcastReciever = new AlarmBroadcastReciever();
	// NotificationCompat.Builder builder;
	// Bundle extras;

	private static final String PREF_TASKLIST_ID_PRESSED = "tasklist_pressed_id";
	private static final String PREF_TASK_ID_FOR_TASKLIST_PRESSED = "task_id";

	@SuppressLint("NewApi")
	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			// Get passed values
			Bundle extras = intent.getExtras();
			// int uniqueID = extras.getInt("alarm_id");

			String uniqueIDDsS = extras.getString("alarmMgr_id");
			NOTIFICATION_ID = Integer.parseInt(uniqueIDDsS);// alarmMgr_id,
															// alarmID;
			sendNotification(Integer.parseInt(uniqueIDDsS));
			// Release the wake lock provided by the BroadcastReceiver.
			AlarmBroadcastReciever.completeWakefulIntent(intent);
		} catch (Exception e) {
			Log.e("AlarmShedullingSerivce:onHandleIntetnt",
					e.getLocalizedMessage());
		}
	}

	// Post a notification
	public void sendNotification(int alarm_id) {
		mContext = getApplicationContext();

		db = new DatabaseHelper(mContext);

		TaskNotificationsModel model = db.notification.Get(alarm_id);
		TaskModel task = db.tasks.Get(model.fk_task_id);
		String alarmTitle = task.title;
		String alarmMessage = task.details + "Notification ID: " + model._id
				+ " Interval: " + model.interval + "interval Type is: "
				+ model.interval_type;

		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		int id = task.fk_tasklist_id;

		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("_task_list_id", task.fk_tasklist_id);
		intent.putExtra("_task_id", task._id);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, /* PendingIntent.FLAG_UPDATE_CURRENT| */
				Intent.FILL_IN_DATA | Intent.FLAG_ACTIVITY_NEW_TASK);
		// | Intent.FLAG_ACTIVITY_SINGLE_TOP);

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
								| Notification.FLAG_FOREGROUND_SERVICE)

				.setContentText(alarmMessage);
		// mBuilder.addAction(R.drawable.ic_action_cancel, "Dismiss",
		// contentIntent);
		// mBuilder.addAction(R.drawable.ic_action_accept, "Done",
		// contentIntent);
		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		if (task.rep_intervalType != 0) {
			// is Repeating type Alarm
			int interval = task.rep_interval;
			if (task.rep_intervalType == 1) {
				// daily
				Long addTheseDaysToExisting = (Long.valueOf(interval) * 86400000L);// 1
																					// day
																					// in
																					// millis
																					// =
																					// 86400000L
				task.rep_startDateTime = String
						.valueOf((Long.valueOf(task.rep_startDateTime) + addTheseDaysToExisting));
				Long endDateTime = Long.valueOf(task.rep_endDateTime);
				if (endDateTime <= Long.valueOf(task.rep_startDateTime)) {
					// do nothing
					// cancel alarm
					mAlarmBroadcastReciever.cancelAlarm(mContext, alarm_id);
				} else if (endDateTime > Long.valueOf(task.rep_startDateTime)) {
					db.tasks.Edit(task);
					set_NextAlarm(db.tasks.Get(task._id), mContext);
				}

			} else if (task.rep_intervalType == 2) {
				// weekly
				Long addTheseWeeksToExisting = (Long.valueOf(interval) * 604800000L); // 1
																						// week
																						// in
																						// millis
																						// =
																						// 604800000L
				task.rep_startDateTime = String
						.valueOf((Long.valueOf(task.rep_startDateTime) + addTheseWeeksToExisting));
				Long endDateTime = Long.valueOf(task.rep_endDateTime);
				if (endDateTime <= Long.valueOf(task.rep_startDateTime)) {
					// do nothing
					// cancel alarm
					mAlarmBroadcastReciever.cancelAlarm(mContext, alarm_id);
				} else if (endDateTime > Long.valueOf(task.rep_startDateTime)) {
					db.tasks.Edit(task);
					set_NextAlarm(db.tasks.Get(task._id), mContext);
				}
			} else if (task.rep_intervalType == 3) {
				// monthly

				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(Long.valueOf(task.rep_startDateTime));

				// INTERVAL_DAY
				// * c.getActualMaximum(Calendar.DAY_OF_MONTH)

				Long addTheseMonthsToExisting = (Long.valueOf(interval)
						* c.getActualMaximum(Calendar.DAY_OF_MONTH) * 86400000L); // month
																					// max
																					// in
																					// millis
																					// *
																					// 1
																					// DAy
																					// in
																					// millis

				task.rep_startDateTime = String
						.valueOf((Long.valueOf(task.rep_startDateTime) + addTheseMonthsToExisting));
				Long endDateTime = Long.valueOf(task.rep_endDateTime);
				if (endDateTime <= Long.valueOf(task.rep_startDateTime)) {
					// do nothing
					// cancel alarm
					mAlarmBroadcastReciever.cancelAlarm(mContext, alarm_id);
				} else if (endDateTime > Long.valueOf(task.rep_startDateTime)) {
					db.tasks.Edit(task);
					set_NextAlarm(db.tasks.Get(task._id), mContext);
				}

			} else if (task.rep_intervalType == 4) {
				// yearly
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(Long.valueOf(task.rep_startDateTime));

				// INTERVAL_DAY
				// * c.getActualMaximum(Calendar.DAY_OF_MONTH)

				GregorianCalendar gCal = new GregorianCalendar();

				Long numberOfDays = 0L;

				if (gCal.isLeapYear(c.get(Calendar.YEAR))) {
					numberOfDays = 366L;
				} else {
					numberOfDays = 365L;
				}

				Long addTheseYearsToExisting = (Long.valueOf(interval)
						* numberOfDays * 86400000L); // month max in millis * 1
														// DAy in millis

				task.rep_startDateTime = String
						.valueOf((Long.valueOf(task.rep_startDateTime) + addTheseYearsToExisting));
				Long endDateTime = Long.valueOf(task.rep_endDateTime);
				if (endDateTime <= Long.valueOf(task.rep_startDateTime)) {
					// do nothing
					// cancel alarm
					mAlarmBroadcastReciever.cancelAlarm(mContext, alarm_id);
				} else if (endDateTime > Long.valueOf(task.rep_startDateTime)) {
					db.tasks.Edit(task);
					set_NextAlarm(db.tasks.Get(task._id), mContext);
				}

			}

		}
	}

	private void set_NextAlarm(TaskModel task, Context mContext) {
		try {
			mAlarmBroadcastReciever.setAlarm(task, mContext);
		} catch (Exception e) {
			Log.e("set Next Alarm", e.getLocalizedMessage());
		}

		// compare your time and notification calculation here
		// task.notifications.
		// setAlarm

		// ArrayList<TaskNotificationsModel> models = //=
		// db.notification.Get(alarm_id);
		// TaskModel task = db.tasks.Get(alarm_id);// .Task_Single(alarm_id);
	}

}
