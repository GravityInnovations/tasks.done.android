package com.gravity.innovations.tasks.done;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

public class AlarmBroadcastReciever extends WakefulBroadcastReceiver {
	// The app's AlarmManager, which provides access to the system alarm
	// services.
	private AlarmManager alarmMgr;
	// The pending intent that is triggered when the alarm fires.
	private PendingIntent alarmIntent;

	@Override
	public void onReceive(Context context, Intent intent) {
		String alarm_id = intent.getAction();
		Intent service = new Intent(context, AlarmSchedulingService.class);
		service.putExtra(Common.KEY_EXTRAS_ALARM_ID, Integer.parseInt(alarm_id));
		// Start the service, keeping the device awake while it is launching.
		startWakefulService(context, service);

	}

	@SuppressLint("NewApi")
	public void setAlarm(TaskModel task, Context _context) {

		for (TaskNotificationsModel model : task.notifications) {
			alarmMgr = (AlarmManager) _context
					.getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(_context, AlarmBroadcastReciever.class);
			int alarm_id = model._id;
			intent.setAction(String.valueOf(alarm_id));

			alarmIntent = PendingIntent.getBroadcast(_context, alarm_id,
					intent, PendingIntent.FLAG_UPDATE_CURRENT
							| Intent.FILL_IN_DATA);

			alarmMgr = (AlarmManager) _context
					.getSystemService(Context.ALARM_SERVICE);
			// notification classification by type
			Long adjustableTime = (long) 0;
			Long notificationTime = (long) 0;
			notificationTime = Long.valueOf(task.startDateTime);
			if (model.interval_type != 0) {
				if (task.allDay == 0) {
					if (model.interval_type == 1) {
						// mins
						adjustableTime = TimeUnit.MINUTES.toMillis(Integer
								.valueOf(model.interval));
						notificationTime = notificationTime - adjustableTime;
					} else if (model.interval_type == 2) {
						// hrs
						adjustableTime = TimeUnit.HOURS.toMillis(Integer
								.valueOf(model.interval));
						notificationTime = notificationTime - adjustableTime;
					} else if (model.interval_type == 3) {
						// days

						adjustableTime = TimeUnit.DAYS.toMillis(Integer
								.valueOf(model.interval));
						notificationTime = notificationTime - adjustableTime;
					} else if (model.interval_type == 4) {
						// weeks
						adjustableTime = ((Long.valueOf(model.interval)) * 604800000);
						// 604800000 = week in millis
						notificationTime = notificationTime - adjustableTime;
					}
				} else if (task.allDay == 1) {

					Calendar dateCal = Calendar.getInstance();
					dateCal.setTimeInMillis(Long
							.valueOf(task.startDateTime));
					Calendar timeCal = Calendar.getInstance();
					timeCal.setTimeInMillis(Long
							.valueOf(model.interval_expiration));

					Calendar newDateTimecal = Common.datetimeHelper
							.mergeCalendars(dateCal, timeCal);
					notificationTime = newDateTimecal.getTimeInMillis();

					if (model.interval_type == 3) {
						// days
						adjustableTime = TimeUnit.DAYS.toMillis(Integer
								.valueOf(model.interval));
						notificationTime = notificationTime - adjustableTime;

					} else if (model.interval_type == 4) {
						// weeks
						adjustableTime = ((Long.valueOf(model.interval)) * 604800000);
						// 604800000 = week in millis
						notificationTime = notificationTime - adjustableTime;

					} else if (model.interval_type == 6) {
						notificationTime = notificationTime;
					}
				}

			}
			Calendar newD = Calendar.getInstance();
			newD.setTimeInMillis(notificationTime);
			Date date = newD.getTime();
			// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// alarmMgr.setExact(AlarmManager.RTC_WAKEUP, notificationTime,
			// alarmIntent);
			// } else {
			alarmMgr.set(AlarmManager.RTC_WAKEUP, notificationTime, alarmIntent);
			// }

			ComponentName receiver = new ComponentName(_context,
					BootBroadcastReceiver.class);
			PackageManager pm = _context.getPackageManager();

			pm.setComponentEnabledSetting(receiver,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);

		}
	}

	private TaskNotificationsModel getModel(TaskModel task, int alarmID) {
		TaskNotificationsModel model = null;
		for (TaskNotificationsModel temp : task.notifications) {
			if (temp._id == alarmID) {
				model = temp;
			}
		}
		return model;
	}

	@SuppressLint("NewApi")
	public void setNextAlarm(TaskModel task, Context _context, int alarmID) {

		TaskNotificationsModel model = getModel(task, alarmID);

		alarmMgr = (AlarmManager) _context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(_context, AlarmBroadcastReciever.class);
		int alarm_id = model._id;
		intent.setAction(String.valueOf(alarm_id));

		alarmIntent = PendingIntent.getBroadcast(_context, alarm_id, intent,
				PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

		alarmMgr = (AlarmManager) _context
				.getSystemService(Context.ALARM_SERVICE);
		// ////////////////////////////////////////////////////
		Long adjustableTime = (long) 0;
		Long notificationTime = (long) 0;
		notificationTime = Long.valueOf(task.startDateTime);
		if (model.interval_type != 0) {
			if (task.allDay == 0) {
				if (model.interval_type == 1) {
					// mins
					adjustableTime = TimeUnit.MINUTES.toMillis(Integer
							.valueOf(model.interval));
					notificationTime = notificationTime - adjustableTime;
				} else if (model.interval_type == 2) {
					// hrs
					adjustableTime = TimeUnit.HOURS.toMillis(Integer
							.valueOf(model.interval));
					notificationTime = notificationTime - adjustableTime;
				} else if (model.interval_type == 3) {
					// days

					adjustableTime = TimeUnit.DAYS.toMillis(Integer
							.valueOf(model.interval));
					notificationTime = notificationTime - adjustableTime;
				} else if (model.interval_type == 4) {
					// weeks
					adjustableTime = ((Long.valueOf(model.interval)) * 604800000);
					// 604800000 = week in millis
					notificationTime = notificationTime - adjustableTime;
				}
			} else if (task.allDay == 1) {

				Calendar dateCal = Calendar.getInstance();
				dateCal.setTimeInMillis(Long.valueOf(task.startDateTime));
				Calendar timeCal = Calendar.getInstance();
				timeCal.setTimeInMillis(Long.valueOf(model.interval_expiration));

				Calendar newDateTimecal = Common.datetimeHelper.mergeCalendars(
						dateCal, timeCal);
				notificationTime = newDateTimecal.getTimeInMillis();

				if (model.interval_type == 3) {
					// days
					adjustableTime = TimeUnit.DAYS.toMillis(Integer
							.valueOf(model.interval));
					notificationTime = notificationTime - adjustableTime;

				} else if (model.interval_type == 4) {
					// weeks
					adjustableTime = ((Long.valueOf(model.interval)) * 604800000);
					// 604800000 = week in millis
					notificationTime = notificationTime - adjustableTime;

				} else if (model.interval_type == 6) {
					notificationTime = notificationTime;
				}
			}

		}
		Calendar newD = Calendar.getInstance();
		newD.setTimeInMillis(notificationTime);
		Date date = newD.getTime();
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		// alarmMgr.setExact(AlarmManager.RTC_WAKEUP, notificationTime,
		// alarmIntent);
		// } else {
		alarmMgr.set(AlarmManager.RTC_WAKEUP, notificationTime, alarmIntent);
		// }

		ComponentName receiver = new ComponentName(_context,
				BootBroadcastReceiver.class);
		PackageManager pm = _context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);

	}

	/**
	 * Cancels the alarm.
	 */

	public void cancelAlarm(Context context, int id) {
		// If the alarm has been set, cancel it with assigned id.
		// Intent alarmIntent = new Intent(context, AlarmReciever.class);
		// alarmIntent.putExtra(IConstants.ALARM_REQUEST_CODE,alarm.getAlarmId());
		Intent intent = new Intent(context, AlarmBroadcastReciever.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id,
				intent, 0);

		if (alarmMgr != null) {
			// and then cancel alarm with that intent alram_id
			alarmMgr.cancel(pendingIntent);
		}
		// Disable {@code BootReceiver} so that it doesn't automatically
		// restart the
		// alarm when the device is rebooted.
		ComponentName receiver = new ComponentName(context,
				BootBroadcastReceiver.class);

		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
	}

}