package com.gravity.innovations.tasks.done;

import java.io.IOException;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.gravity.innovations.tasks.done.Common.Callbacks.GCMCallback;

public class GCMController {
	public static boolean checkPlayServices(final Context mContext, final Activity mActivity) {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, mActivity,
	                    Common.PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            //Log.i(TAG, "This device is not supported.");
	            //finish();
	        	//((GCMCallback)mContext).displayMsg("Device unsupported");
	        	//addProgressTask("Device unsupported");
	        }
	        return false;
	    }
	    return true;
	}
	public static String getRegistrationId(Context context, Common.userData user_data) {
	    //final SharedPreferences prefs = getGCMPreferences(context);
	   // String registrationId = prefs.getString(Common.GOOGLE_PROPERTY_REG_ID, "");
	    if (user_data.google_reg_id.isEmpty()) {
	        //Log.i(TAG, "Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    //int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (user_data.google_regVer != currentVersion) {
	        //Log.i(TAG, "App version changed.");
	        return "";
	    }
	    return user_data.google_reg_id;
	}
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	private static class gcmBackgroundRegisteration extends AsyncTask<Void, Void, String>{
		private GoogleCloudMessaging gcm;
		private Context mContext;
		private Activity mActivity;
		private String regid;
		private int RequestCode;
		public gcmBackgroundRegisteration(Context mContext,
				GoogleCloudMessaging gcm) {
			// TODO Auto-generated constructor stub
			this.mContext = mContext;
			this.gcm = gcm;
			this.mActivity = mActivity;
			this.RequestCode = RequestCode;
		}
		protected String doInBackground(Void... params) {
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(mContext);
                }
                regid = gcm.register(Common.GCM_SENDER_ID);
                //msg = "Device registered, registration ID=" + regid;

                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.
                //GravityController.send_gcm_req_code(mActivity,RequestCode,regid);
                //sendRegistrationIdToBackend();

                // For this demo: we don't need to send it because the device
                // will send upstream messages to a server that echo back the
                // message using the 'from' address in the message.

                // Persist the regID - no need to register again.
                ((Common.Callbacks.GCMCallback)mContext).storeGCMRegisterationId(regid,getAppVersion(mContext));
                //storeRegistrationId(context, regid);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
           // mDisplay.append(msg + "\n");
        	if(false)
        	((Common.Callbacks.GCMCallback)mContext).displayMsg(msg);
        }
		
	}
	public static void registerInBackground(final Context mContext, 
			final GoogleCloudMessaging gcm) {
		gcmBackgroundRegisteration r = new gcmBackgroundRegisteration(mContext, gcm);
		r.execute(null,null,null);
	
	}
}
