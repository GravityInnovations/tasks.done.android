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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//This class will include common keys used in application

//default decleration as below
//public static final int MY_KEY_SOME = 0;

//default usuage outside as below
//PublicKeys.MY_KEY_SOME
//pass it to switch, bundle extras, shared prefs etc
public class Common {
	private static final String prefix = "com.gravity.innovations.tasks.done";
	public static final String USER_EMAIL = prefix+"UserEmail";//used in shared prefs for io of user email address
	public static final int SPLASH_TIME_OUT = 3000;
	public static final String ACCOUNT_TYPE = "com.google";
	public static final String AUTH_TOKEN = prefix+"AuthToken";
	public static final String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile https://www.google.com/m8/feeds/ https://www.googleapis.com/auth/drive.appdata https://www.googleapis.com/auth/tasks";
	public static final String HAS_INTERNET = prefix+"HasInternet";
	public static final String EXCEPTION = prefix+"Exception";
	public static final String USER_UNAPPROVE = prefix+"Unapproved";
	public static final String NETWORK_ERROR = "NetworkError";
	//Activity Names
	public static final String AUTH_ACTIVITY = "AuthenticationActivity";
	//request codes
	public class RequestCodes{
		public static final int SPLASH_AUTH = 999;
	}
	public static class Callbacks{
		public interface SplashCallback{
	        public void pushSuccess(String AuthToken, String Email);
	        public void pushFalure(String Error, String Email);
	    }
		
	}

	public static boolean hasInternet(Activity mActivity)
	{
		
		ConnectivityManager cm =
		        (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
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
