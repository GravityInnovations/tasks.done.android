package com.gravity.innovations.tasks.done;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TimeReciever extends BroadcastReceiver {
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		try {
			((Common.Callbacks.TimeCallBack) arg0).onTimeReceive(arg0, arg1);
		} catch (Exception e) { 
			Log.e(this.toString()+": onRecieve, TimeRecever.Class", e.getLocalizedMessage());
		}
	}
}