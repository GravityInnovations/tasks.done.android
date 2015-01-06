package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * This BroadcastReceiver automatically (re)starts the alarm when the device is
 * rebooted. This receiver is set to be disabled (android:enabled="false") in
 * the application's manifest file. When the user sets the alarm, the receiver
 * is enabled. When the user cancels the alarm, the receiver is disabled, so
 * that rebooting the device will not trigger this receiver.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

	AlarmBroadcastReciever mAlarmBroadcastReciever = new AlarmBroadcastReciever();
	DatabaseHelper db;
	@Override
	public void onReceive(Context context, Intent arg1) {
		if (arg1.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			assignAlarms(context);
		}
	}
	public void assignAlarms(Context context) {
		/*
		 * Checking reboot-able alarm
		 */
		db = new DatabaseHelper(context);
		ArrayList<TaskModel> alarms = new ArrayList<TaskModel>();
		alarms = db.Alarm_List();
		for (TaskModel temp : alarms) {
			if (temp.remind_interval == 1) {// repeat once

				String remindAt = temp.remind_at;

				StringTokenizer tokens = new StringTokenizer(remindAt, "/");
				String year_string = tokens.nextToken();
				String month_string = tokens.nextToken();
				String date_string = tokens.nextToken();
				String hours_string = tokens.nextToken();
				String minute_string = tokens.nextToken();
				int year = /* 2014; */Integer.parseInt(year_string);
				int month = /* 10; */Integer.parseInt(month_string);
				int date = /* 12; */Integer.parseInt(date_string);
				int hours = /* 12; */Integer.parseInt(hours_string);
				int minutes = /* 10; */Integer.parseInt(minute_string);
				try {
					mAlarmBroadcastReciever.setAlarm_RepeatOnce(context, year,
							month, date, hours, minutes, temp);
				} catch (Exception e) {
					Log.e("Assigning Alarm", "Once:AlarmBootBroadcastReciever");
				}

			} else if (temp.remind_interval == 2) {// repeat Daily

				String remindAt = temp.remind_at;

				StringTokenizer tokens = new StringTokenizer(remindAt, "/");
				String year_string = tokens.nextToken();
				String month_string = tokens.nextToken();
				String date_string = tokens.nextToken();
				String hours_string = tokens.nextToken();
				String minute_string = tokens.nextToken();
				int year = Integer.parseInt(year_string);
				int month = Integer.parseInt(month_string);
				int date = Integer.parseInt(date_string);
				int hours = Integer.parseInt(hours_string);
				int minutes = Integer.parseInt(minute_string);
				try {
					mAlarmBroadcastReciever.setAlarm_repeatDaily(context, year,
							month, date, hours, minutes, temp);
				} catch (Exception e) {
					Log.e("Assigning Alarm", "Daily:AlarmBootBroadcastReciever");
				}

			} else if (temp.remind_interval == 3) {// repeat Weekly

				String remindAt = temp.remind_at;

				StringTokenizer tokens = new StringTokenizer(remindAt, "/");
				String year_string = tokens.nextToken();
				String month_string = tokens.nextToken();
				String date_string = tokens.nextToken();
				String hours_string = tokens.nextToken();
				String minute_string = tokens.nextToken();
				int year = Integer.parseInt(year_string);
				int month = Integer.parseInt(month_string);
				int date = Integer.parseInt(date_string);
				int hours = Integer.parseInt(hours_string);
				int minutes = Integer.parseInt(minute_string);
				try {
					mAlarmBroadcastReciever.setAlarm_RepeatWeekly(context,
							year, month, date, hours, minutes, temp);
				} catch (Exception e) {
					Log.e("Assigning Alarm",
							"Weekly:AlarmBootBroadcastReciever");
				}

			} else if (temp.remind_interval == 4) {// repeat Monthly

				String remindAt = temp.remind_at;

				StringTokenizer tokens = new StringTokenizer(remindAt, "/");
				String year_string = tokens.nextToken();
				String month_string = tokens.nextToken();
				String date_string = tokens.nextToken();
				String hours_string = tokens.nextToken();
				String minute_string = tokens.nextToken();
				int year = Integer.parseInt(year_string);
				int month = Integer.parseInt(month_string);
				int date = Integer.parseInt(date_string);
				int hours = Integer.parseInt(hours_string);
				int minutes = Integer.parseInt(minute_string);
				try {
					mAlarmBroadcastReciever.setAlarm_RepeatMonthly(context,
							year, month, date, hours, minutes, temp);
				} catch (Exception e) {
					Log.e("Assigning Alarm",
							"Monthly:AlarmBootBroadcastReciever");
				}

			} else if (temp.remind_interval == 5) {// repeat Yearly

				String remindAt = temp.remind_at;

				StringTokenizer tokens = new StringTokenizer(remindAt, "/");
				String year_string = tokens.nextToken();
				String month_string = tokens.nextToken();
				String date_string = tokens.nextToken();
				String hours_string = tokens.nextToken();
				String minute_string = tokens.nextToken();
				int year = Integer.parseInt(year_string);
				int month = Integer.parseInt(month_string);
				int date = Integer.parseInt(date_string);
				int hours = Integer.parseInt(hours_string);
				int minutes = Integer.parseInt(minute_string);
				try {
					mAlarmBroadcastReciever.setAlarm_RepeatYearly(context,
							year, month, date, hours, minutes, temp);
				} catch (Exception e) {
					Log.e("Assigning Alarm",
							"Yearly:AlarmBootBroadcastReciever");
				}

			}

		}

		/*
		 * Checking reboot-able alarm
		 */
	}
}
