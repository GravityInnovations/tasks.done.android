package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {
	AlarmBroadcastReciever mAlarmBroadcastReciever = new AlarmBroadcastReciever();
	DatabaseHelper db;

	@Override
	public void onReceive(Context context, Intent arg1) {
		if (arg1.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			assignAlarms(context);
			// startApp(context);
		}

	}

	private void startApp(Context context) {
		try {
			Intent intent = new Intent(context, SplashActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			Log.e("startAppAfterLockScreenOpens", e.getLocalizedMessage());
		}
	}

	public void assignAlarms(Context context) {
		db = new DatabaseHelper(context);
		ArrayList<TaskModel> tasks = new ArrayList<TaskModel>();
		tasks = db.tasks.Get();
		for (TaskModel temp : tasks) {
			if (System.currentTimeMillis() < Long.valueOf(temp.startDateTime)) {

				try {
					mAlarmBroadcastReciever.setAlarm(temp, context);

				} catch (Exception e) {
					Log.e("Assigning Alarm",
							"Yearly:AlarmBootBroadcastReciever");
				}

			}
		}
	}
}
