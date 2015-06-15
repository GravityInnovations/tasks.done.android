package com.gravity.innovations.tasks.done;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/*
 * this class gets referral intent at installation and also stores the referral in shared pref
 */

public class ReferralReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		try {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				bundle.containsKey(null);
			}
		} catch (final Exception e) {
			return;
		}
		Map<String, String> referralParams = new HashMap<String, String>();
		if (!intent.getAction().equals("com.android.vending.INSTALL_REFERRER")) { //$NON-NLS-1$
			return; // Return if this is not the right intent.
		}
		String referrer = intent.getStringExtra("referrer"); //$NON-NLS-1$
		if (referrer == null || referrer.length() == 0) {
			return; // Return if referre is empty/null
		}
		try { // Remove any url encoding
			referrer = URLDecoder.decode(referrer, "UTF-8");
			//wrong referrer = URLDecoder.decode(referrer, "x-www-form-urlencoded"); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			return;
		}

		// Parse the query string, extracting the relevant data
		String[] params = referrer.split("&"); // $NON-NLS-1$
		for (String param : params) {
			String[] pair = param.split("="); // $NON-NLS-1$
			referralParams.put(pair[0], pair[1]);
		}

		ReferralReceiver.storeReferralParams(context, referralParams);
	}

	private final static String[] EXPECTED_PARAMETERS = { "utm_source",
			"utm_medium", "utm_term", "utm_content", "utm_campaign" };

	private final static String PREFS_FILE_NAME = "ReferralParamsFile";

	public static void storeReferralParams(Context context,
			Map<String, String> params) {
		SharedPreferences storage = context.getSharedPreferences(
				ReferralReceiver.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = storage.edit();

		for (String key : ReferralReceiver.EXPECTED_PARAMETERS) {
			String value = params.get(key);
			if (value != null) {
				editor.putString(key, value);
			}
		}

		editor.commit();
	}

	public static Map<String, String> retrieveReferralParams(Context context) {
		HashMap<String, String> params = new HashMap<String, String>();
		SharedPreferences storage = context.getSharedPreferences(
				ReferralReceiver.PREFS_FILE_NAME, Context.MODE_PRIVATE);

		for (String key : ReferralReceiver.EXPECTED_PARAMETERS) {
			String value = storage.getString(key, null);
			if (value != null) {
				params.put(key, value);
			}
		}
		return params;
	}

}
