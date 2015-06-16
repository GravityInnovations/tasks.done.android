package com.gravity.innovations.tasks.done;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class NotificationsBroadcastReciever extends BroadcastReceiver {
	private String ACTION_CALL = "1";
	private String ACTION_SMS = "2";
	private String ACTION_EMAIL = "3";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (Common.KEY_INTENT_NOTIFICATION_SET_ACTION.equals(action)) {
			Bundle extras = intent.getExtras();
			String actionType = extras
					.getString(Common.KEY_EXTRAS_NOTIFICATION_ACTION_TYPE);
			if (actionType.equals(ACTION_CALL)) {
				dialNumber(
						extras.getString(Common.KEY_EXTRAS_NOTIFICATION_RECEPIENT),
						context);
			} else if (actionType.equals(ACTION_SMS)) {
				sendSms(extras
						.getString(Common.KEY_EXTRAS_NOTIFICATION_RECEPIENT),
						context);
			} else if (actionType.equals(ACTION_EMAIL)) {
				sendEmail(
						extras.getString(Common.KEY_EXTRAS_NOTIFICATION_RECEPIENT),
						context);
			}
		}
	}

	private void sendEmail(String recipent, Context context) {
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.setType("text/plain");
		// intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
		String emailString = "\n this email reminder was reminded to you by task.done https://play.google.com/store";
		intent.putExtra(Intent.EXTRA_TEXT, emailString);
		intent.setData(Uri.parse("mailto:" + recipent));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// this will make such that when user returns to your app, your app is
		// displayed, instead of the email app.
		context.startActivity(intent);

	}

	private void sendSms(String recipent, Context context) {

		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
				+ recipent));
		String smsString = "this sms reminder was reminded to you by task.done https://play.google.com/store";
		intent.putExtra("sms_body", smsString);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		// Uri.parse("smsto:03465368123") single
		// Uri.parse("smsto:5551212;5551212") multiple
	}

	private void dialNumber(String recipent, Context context) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ recipent));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		// Uri.parse("tel:03465368123")
	}
}