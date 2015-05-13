///////////////////////////////////////////////////////////////////////////////////////
//package com.gravity.innovations.tasks.done;
//
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.concurrent.TimeUnit;
//
//import android.annotation.SuppressLint;
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.support.v4.content.WakefulBroadcastReceiver;
//import android.text.format.Time;
//import android.util.Log;
//
//public class AlarmBroadcastReciever extends WakefulBroadcastReceiver {
//	// The app's AlarmManager, which provides access to the system alarm
//	// services.
//	// isNothing
//	private AlarmManager alarmMgr;
//	// The pending intent that is triggered when the alarm fires.
//	private PendingIntent alarmIntent;
//
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		String alarm_id = intent.getAction();
//		Intent service = new Intent(context, AlarmSchedulingService.class);
//		// service.setData(Uri.parse("custom://" + alarm_id));
//		service.setAction(String.valueOf(alarm_id));
//		service.putExtra("alarmMgr_id", alarm_id);
//		// Start the service, keeping the device awake while it is launching.
//		startWakefulService(context, service);
//	}
//
//	public void setAlarm(TaskModel task, Context _context) {
//
//		for (TaskNotificationsModel model : task.notifications) {
//
//			alarmMgr = (AlarmManager) _context
//					.getSystemService(Context.ALARM_SERVICE);
//			Intent intent = new Intent(_context, AlarmBroadcastReciever.class);
//			int alarm_id = model._id;
//			intent.setAction(String.valueOf(alarm_id));
//
//			alarmIntent = PendingIntent.getBroadcast(_context, alarm_id,
//					intent, PendingIntent.FLAG_UPDATE_CURRENT
//							| Intent.FILL_IN_DATA);
//
//			alarmMgr = (AlarmManager) _context
//					.getSystemService(Context.ALARM_SERVICE);
//
//			if (model.interval_type != 0) {
//
//				Long adjustableTime = (long) 0;
//				Long notificationTime = (long) 0;
//
//				if (model.interval_type == 1) {
//					// mins
//
//					if (model.interval == 0) {
//						// at the time of the event
//						notificationTime = Long.valueOf(task.rep_startDateTime);
//					} else {
//						// set interval
//						adjustableTime = TimeUnit.MINUTES.toMillis(Integer
//								.valueOf(model.interval));
//						notificationTime = notificationTime - adjustableTime;
//					}
//
//				} else if (model.interval_type == 2) {
//					// hrs
//					adjustableTime = TimeUnit.HOURS.toMillis(Integer
//							.valueOf(model.interval));
//					notificationTime = notificationTime - adjustableTime;
//				} else if (model.interval_type == 3) {
//					// days
//
//					if (task.rep_allDay == 0) {
//						if (model.interval_expiration != null
//								|| !model.interval_expiration.isEmpty()) {
//
//							adjustableTime = TimeUnit.DAYS.toMillis(Integer
//									.valueOf(model.interval));
//							notificationTime = notificationTime
//									- adjustableTime;
//
//						}
//					} else if (task.rep_allDay == 1) {
//						if (model.interval_expiration != null
//								|| !model.interval_expiration.isEmpty()) {
//
//							Calendar date_calendar = Calendar.getInstance();
//							date_calendar.setTimeInMillis(Long
//									.valueOf(task.rep_startDateTime));
//							Calendar time_calCalendar = Calendar.getInstance();
//							time_calCalendar.setTimeInMillis(Long
//									.valueOf(model.interval_expiration));
//							Calendar merged_calender = Calendar.getInstance();
//							merged_calender.set(Calendar.YEAR,
//									date_calendar.get(Calendar.YEAR));
//							merged_calender.set(Calendar.DAY_OF_MONTH,
//									date_calendar.get(Calendar.DAY_OF_MONTH));
//							merged_calender.set(Calendar.DATE,
//									date_calendar.get(Calendar.DATE));
//							merged_calender.set(Calendar.HOUR_OF_DAY,
//									date_calendar.get(Calendar.HOUR_OF_DAY));
//							merged_calender.set(Calendar.MINUTE,
//									date_calendar.get(Calendar.MINUTE));
//
//							adjustableTime = TimeUnit.DAYS.toMillis(Integer
//									.valueOf(model.interval));
//							notificationTime = merged_calender
//									.getTimeInMillis();
//							notificationTime = notificationTime
//									- adjustableTime;
//
//						}
//					}
//
//				} else if (model.interval_type == 4) {
//					// weeks
//					if (task.rep_allDay == 0) {
//						adjustableTime = ((Long.valueOf(model.interval)) * 604800000);
//						// 604800000 = week in millis
//						notificationTime = notificationTime - adjustableTime;
//
//					} else if (task.rep_allDay == 1) {
//						if (model.interval_expiration != null
//								|| !model.interval_expiration.isEmpty()) {
//
//							Calendar date_calendar = Calendar.getInstance();
//							date_calendar.setTimeInMillis(Long
//									.valueOf(task.rep_startDateTime));
//							Calendar time_calCalendar = Calendar.getInstance();
//							time_calCalendar.setTimeInMillis(Long
//									.valueOf(model.interval_expiration));
//							Calendar merged_calender = Calendar.getInstance();
//							merged_calender.set(Calendar.YEAR,
//									date_calendar.get(Calendar.YEAR));
//							merged_calender.set(Calendar.DAY_OF_MONTH,
//									date_calendar.get(Calendar.DAY_OF_MONTH));
//							merged_calender.set(Calendar.DATE,
//									date_calendar.get(Calendar.DATE));
//							merged_calender.set(Calendar.HOUR_OF_DAY,
//									date_calendar.get(Calendar.HOUR_OF_DAY));
//							merged_calender.set(Calendar.MINUTE,
//									date_calendar.get(Calendar.MINUTE));
//
//							adjustableTime = ((Long.valueOf(model.interval)) * 604800000);
//							// 604800000 = week in millis
//							notificationTime = merged_calender
//									.getTimeInMillis();
//							notificationTime = notificationTime
//									- adjustableTime;
//
//						}
//
//					}
//				}
//				alarmMgr.set(AlarmManager.RTC_WAKEUP, notificationTime,
//						alarmIntent);
//				ComponentName receiver = new ComponentName(_context,
//						BootBroadcastReceiver.class);
//				PackageManager pm = _context.getPackageManager();
//
//				pm.setComponentEnabledSetting(receiver,
//						PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//						PackageManager.DONT_KILL_APP);
//			}
//		}// foreach
//
//	}
//
//	/**
//	 * Cancels the alarm.
//	 */
//
//	public void cancelAlarm(Context context, int id) {
//		// If the alarm has been set, cancel it with assigned id.
//		// Intent alarmIntent = new Intent(context, AlarmReciever.class);
//		// alarmIntent.putExtra(IConstants.ALARM_REQUEST_CODE,alarm.getAlarmId());
//		Intent intent = new Intent(context, AlarmBroadcastReciever.class);
//
//		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id,
//				intent, 0);
//
//		if (alarmMgr != null) {
//			// and then cancel alarm with that intent alram_id
//			alarmMgr.cancel(pendingIntent);
//		}
//		// Disable {@code BootReceiver} so that it doesn't automatically
//		// restart the
//		// alarm when the device is rebooted.
//		ComponentName receiver = new ComponentName(context,
//				BootBroadcastReceiver.class);
//
//		PackageManager pm = context.getPackageManager();
//
//		pm.setComponentEnabledSetting(receiver,
//				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//				PackageManager.DONT_KILL_APP);
//	}
//
//}
