package com.gravity.innovations.tasks.done;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimeReciever extends BroadcastReceiver {
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		((Common.Callbacks.TimeCallBack)arg0).onTimeReceive(arg0, arg1);
	}
}