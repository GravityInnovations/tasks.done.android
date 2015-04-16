package com.gravity.innovations.tasks.done;

import java.util.Calendar;
import java.util.GregorianCalendar;
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
	//isNothing
	private AlarmManager alarmMgr;
	// The pending intent that is triggered when the alarm fires.
	private PendingIntent alarmIntent;

	@Override
	public void onReceive(Context context, Intent intent) {
		String alarm_id = intent.getAction();
		Intent service = new Intent(context, AlarmSchedulingService.class);
		// service.setData(Uri.parse("custom://" + alarm_id));
		service.setAction(String.valueOf(alarm_id));
		service.putExtra("alarmMgr_id", alarm_id);
		// Start the service, keeping the device awake while it is launching.
		startWakefulService(context, service);
	}

	@SuppressLint("NewApi")
	public void setAlarm_RepeatOnce(Context context, int year, int month,
			int date, int hours, int minutes, /* int task_id */
			TaskModel currentTask) {
		try {
			alarmMgr = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);

			DatabaseHelper db = new DatabaseHelper(context);

			int alarm_id = currentTask._id;
			Intent intent = new Intent(context, AlarmBroadcastReciever.class);
			intent.setAction(String.valueOf(alarm_id)); 
			alarmIntent = PendingIntent.getBroadcast(context, alarm_id,
					intent, PendingIntent.FLAG_UPDATE_CURRENT
							| Intent.FILL_IN_DATA);

			// DatabaseHelper db = new DatabaseHelper(context);
			//ZZ**ZZ currentTask.alarm_id = alarm_id;// currentTask._id;;
			db.tasks.Edit(currentTask);//.Task_Edit(currentTask);

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.set(Calendar.DATE, date);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.HOUR_OF_DAY, hours);
			calendar.set(Calendar.MINUTE, minutes);

			alarmMgr = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
					alarmIntent);

			// Enable {@code AlarmBootBroadCastReceiver} to automatically
			// restart the alarm
			// when the
			// device is rebooted.
			ComponentName receiver = new ComponentName(context,
					BootBroadcastReceiver.class);
			PackageManager pm = context.getPackageManager();

			pm.setComponentEnabledSetting(receiver,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);
		} catch (Exception ex) {
			String e = ex.getLocalizedMessage();
		}
	}

	/*
	 * alarm for daily repetition, needs time
	 */

	public void setAlarm_repeatDaily(Context context, int year, int month,
			int date, int hours, int minutes, TaskModel currentTask) {
		alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		int alarm_id = currentTask._id;
		Intent intent = new Intent(context, AlarmBroadcastReciever.class);
		intent.setAction(String.valueOf(alarm_id));
		// alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		alarmIntent = PendingIntent.getBroadcast(context, alarm_id, intent,
				PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

		DatabaseHelper db = new DatabaseHelper(context);
		//ZZ**ZZ currentTask.alarm_id = alarm_id;// currentTask._id;;
		db.tasks.Edit(currentTask);//.Task_Edit(currentTask);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());

		calendar.set(Calendar.DATE, date);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);

		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);

		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 1,
				alarmIntent);

		// Enable {@code SampleBootReceiver} to automatically restart the alarm
		// when the device is rebooted.
		ComponentName receiver = new ComponentName(context,
				BootBroadcastReceiver.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}

	/*
	 * this will repeat at the weekdays and hours and minute
	 */

	public void setAlarm_RepeatWeekly(Context context, int year, int month,
			int date, int hours, int minutes, TaskModel currentTask) {
		alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		int alarm_id = currentTask._id;
		Intent intent = new Intent(context, AlarmBroadcastReciever.class);
		intent.setAction(String.valueOf(alarm_id));
		// alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		alarmIntent = PendingIntent.getBroadcast(context, alarm_id, intent,
				PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

		DatabaseHelper db = new DatabaseHelper(context);
		//ZZ**ZZ currentTask.alarm_id = alarm_id;// currentTask._id;;
		db.tasks.Edit(currentTask);//.Task_Edit(currentTask);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.DATE, date);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		// calender.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		// SimpleDateFormat d = new SimpleDateFormat();
		// d.
		// SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
		// String weekDay = dayFormat.format(calendar.getTime());

		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);

		alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7,
				alarmIntent);

		// Enable {@code SampleBootReceiver} to automatically restart the alarm
		// when the
		// device is rebooted.
		ComponentName receiver = new ComponentName(context,
				BootBroadcastReceiver.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}

	/*
	 * this will repeat on the same date of the month at specific time date
	 * value will range form 1-31
	 */

	@SuppressLint("NewApi")
	public void setAlarm_RepeatMonthly(Context context, int year,
			int currentMonth, int date, int hours, int minutes,
			TaskModel currentTask) {
		alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		int alarm_id = currentTask._id;
		Intent intent = new Intent(context, AlarmBroadcastReciever.class);
		intent.setAction(String.valueOf(alarm_id));
		// alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		alarmIntent = PendingIntent.getBroadcast(context, alarm_id, intent,
				PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

		DatabaseHelper db = new DatabaseHelper(context);
		//ZZ**ZZ currentTask.alarm_id = alarm_id;// currentTask._id;;
		db.tasks.Edit(currentTask);//.Task_Edit(currentTask);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());

		calendar.set(Calendar.DATE, date);
		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, currentMonth);
		if (currentMonth == 0/* Calendar.JANUARY */
				|| currentMonth == 2/* Calendar.MARCH */
				|| currentMonth == 4/* Calendar.MAY */
				|| currentMonth == 6/* Calendar.JULY */
				|| currentMonth == 7/* Calendar.AUGUST */
				|| currentMonth == 9/* Calendar.OCTOBER */
				|| currentMonth == 11/* Calendar.DECEMBER */) {
			alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 31,
					alarmIntent);
		}
		if (currentMonth == 3/* Calendar.APRIL */
				|| currentMonth == 5/* Calendar.JUNE */
				|| currentMonth == 8/* Calendar.SEPTEMBER */
				|| currentMonth == 10/* Calendar.NOVEMBER */) {
			alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 30,
					alarmIntent);
		}

		if (currentMonth == 1/* Calendar.FEBRUARY */) {// for feburary month)
			GregorianCalendar cal = (GregorianCalendar) GregorianCalendar
					.getInstance();
			if (cal.isLeapYear(year)) {// for leap year feburary month
				alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(),
						AlarmManager.INTERVAL_DAY * 29, alarmIntent);
			} else { // for non leap year feburary month
				alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(),
						AlarmManager.INTERVAL_DAY * 28, alarmIntent);
			}
		}

		// Enable {@code SampleBootReceiver} to automatically restart the
		// alarm when the device is rebooted.

		ComponentName receiver = new ComponentName(context,
				BootBroadcastReceiver.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);

	}

	/*
	 * this will repeat once a year date month hours and minutes
	 */

	public void setAlarm_RepeatYearly(Context context, int year, int month,
			int date, int hours, int minutes, TaskModel currentTask) {
		alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		int alarm_id = currentTask._id;
		Intent intent = new Intent(context, AlarmBroadcastReciever.class);
		intent.setAction(String.valueOf(alarm_id));

		alarmIntent = PendingIntent.getBroadcast(context, alarm_id, intent,
				PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

		DatabaseHelper db = new DatabaseHelper(context);
		//ZZ**ZZ currentTask.alarm_id = alarm_id;// currentTask._id;;
		db.tasks.Edit(currentTask);//.Task_Edit(currentTask);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		// Set the alarm's trigger time to 8:30 a.m.
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DATE, date);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);

		GregorianCalendar cal = (GregorianCalendar) GregorianCalendar
				.getInstance();
		if (cal.isLeapYear(year)) {
			alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), 60 * 60 * 24 * 366 * 1000,
					alarmIntent);
		} else {
			alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), 60 * 60 * 24 * 365 * 1000,
					alarmIntent);
		}

		// Enable {@code BootReceiver} to automatically restart the alarm
		// when the device is rebooted.

		ComponentName receiver = new ComponentName(context,
				BootBroadcastReceiver.class);
		PackageManager pm = context.getPackageManager();

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
