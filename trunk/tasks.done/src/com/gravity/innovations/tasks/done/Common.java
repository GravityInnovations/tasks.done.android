package com.gravity.innovations.tasks.done;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

//This class will include common keys used in application

//default decleration as below
//public static final int MY_KEY_SOME = 0;

//default usuage outside as below
//PublicKeys.MY_KEY_SOME
//pass it to switch, bundle extras, shared prefs etc
public class Common {
	private static final String prefix = "com.gravity.innovations.tasks.done";
	public static final String USER_EMAIL = prefix + "UserEmail";// used in
																	// shared
																	// prefs for
																	// io of
																	// user
																	// email
																	// address
	public static final int SPLASH_TIME_OUT = 3000;
	public static final int SPLASH_TIME_OUT_SMALL = 1000;
	public static final String ACCOUNT_TYPE = "com.google";
	public static final String AUTH_TOKEN = prefix + "AuthToken";
	public static final String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile https://www.google.com/m8/feeds/ https://www.googleapis.com/auth/drive.appdata https://www.googleapis.com/auth/tasks";
	public static final String HAS_INTERNET = prefix + "HasInternet";
	public static final String EXCEPTION = prefix + "Exception";
	public static final String USER_UNAPPROVE = prefix + "Unapproved";
	public static final String NETWORK_ERROR = "NetworkError";
	// commands - Splash
	public static final int CHECK_INTERNET = 1;
	public static final int LOAD_PREFS = 2;

	public static final int GOOGLE_AUTH = 3;
	public static final int LOAD_LOCAL_DB = 4;

	// Activity Names
	public static final String AUTH_ACTIVITY = "AuthenticationActivity";
	public static final String SPLASH_ACTIVITY = "SplashActivity";

	
	
	
	// request codes
	public class RequestCodes {
		public static final int SPLASH_AUTH = 999;
	}

	public static class Callbacks {
		public interface AuthCallback {
			public void pushSuccess(String AuthToken, String Email);
			public void pushFalure(String Error, String Email);
		}

		public interface SplashCallback {
			public void CheckInternet();

			public void LoadPreferences();

			public void GoogleAuth();

			public void LoadLocalDB();
		}

	}
	//Start Database Variables and Queries M
	public static class QueryDB{
		
		
		public static final String KEY_ROWID = "_id";
		public static final String KEY_TASKTITLE = "task_title";
		public static final String KEY_TASKDETAILS = "task_todo";
		public static final String DATABASE_NAME = "Tasks";
		public static final String DATABASE_TABLE = "taskDetails";
		public static final int DATABASE_VERSION = 1;
		//Create Query for database  
		public static final String CREATE_QUERY = "CREATE TABLE " + Common.QueryDB.DATABASE_TABLE + " (" +
		Common.QueryDB.KEY_ROWID + " INTERGER PRIMARY KEY AUTOINCREMENT, " +
		Common.QueryDB.KEY_TASKTITLE + " TEXT NOT NULL, " +
		Common.QueryDB.KEY_TASKDETAILS + " TEXT NOT NULL);";
		//Create Query for database
	}
	//End Database Variables and Queries M
	
	public static class customPause {
		public customPause(final Activity mActivity, final int functionToken, int Time) {

			new Handler().postDelayed(new Runnable() {

				
				@Override
				public void run() {
					//if(mActivity.getClass().getSimpleName().toString().equals(Common.AUTH_ACTIVITY))
	    	        	switch(functionToken)
	    	        	{
		    	        	case CHECK_INTERNET:
		    	        		((SplashActivity)mActivity).CheckInternet();
		    	        		break;
		    	        	case LOAD_PREFS:
		    	        		((SplashActivity)mActivity).LoadPreferences();
		    	        		break;
		    	        	case GOOGLE_AUTH:
		    	        		((SplashActivity)mActivity).GoogleAuth();
		    	        		break;
		    	        	case LOAD_LOCAL_DB:
		    	        		((SplashActivity)mActivity).LoadLocalDB();
		    	        		break;
		    	        	default: break;
	    	        	}
						
	    				
				}
			}, Time);

		}
	}

	public static boolean hasInternet(Activity mActivity) {

		ConnectivityManager cm = (ConnectivityManager) mActivity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = cm.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;
		return true;

	}
}
