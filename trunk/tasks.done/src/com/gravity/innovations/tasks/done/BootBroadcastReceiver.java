package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.Currency;
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
	// ZZ**ZZ String remindAt
	String remindAt;
	// ZZ**ZZ String remindAt
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
		ArrayList<TaskModel> tasks = new ArrayList<TaskModel>();
		tasks = db.tasks.Get();
		for (TaskModel temp : tasks) {
			if (System.currentTimeMillis() < Long
					.valueOf(temp.rep_startDateTime)) {

				try {
					mAlarmBroadcastReciever.setAlarm(temp, context);

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
