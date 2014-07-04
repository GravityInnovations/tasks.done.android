package com.gravity.innovations.tasks.done;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

//This class will include common keys used in application

//default decleration as below
//public static final int MY_KEY_SOME = 0;

//default usuage outside as below
//PublicKeys.MY_KEY_SOME
//pass it to switch, bundle extras, shared prefs etc 
public class Common {
	// Mushahid commented this to check the commit changes mmm kkk ;;;
	private static final String prefix = "com.gravity.innovations.tasks.done.";
	public static final String USER_EMAIL = prefix + "UserEmail";// used in
																	// shared
																	// prefs for
																	// io of
																	// user
																	// email
																	// address
	public static final String USER_DATA = prefix + "UserData";
	public static final String USER_IS_VERIFICATION_COMPLETE = prefix
			+ "GoogleVerificationComplete";
	public static final String USER_IS_SYNC_TYPE = prefix + "UserWillSync";
	public static final String USER_IS_REGISTERED = prefix + "UserRegistered";
	public static final String SHARED_PREF_KEY = prefix;
	public static final int SPLASH_TIME_OUT = 3000;
	public static final int SPLASH_TIME_OUT_SMALL = 3000;
	public static final String ACCOUNT_TYPE = "com.google";
	public static final String AUTH_TOKEN = prefix + "AuthToken";
	public static final String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.profile";
	public static final String EXTRA_MESSAGE = "message";
    public static final String GOOGLE_PROPERTY_REG_ID = "registration_id";
    public static final String GOOGLE_PROPERTY_APP_VERSION = "appVersion";
    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    public final static String GCM_SENDER_ID = "632707319263";

    
	public static final String HAS_INTERNET = prefix + "HasInternet";
	public static final String EXCEPTION = prefix + "Exception";
	public static final String USER_UNAPPROVE = prefix + "Unapproved";
	public static final String NETWORK_ERROR = prefix+"NetworkError";
	public static final int one = 2;

	public static final int HTTP_RESPONSE_OK = 0;
    public static final int HTTP_RESPONSE_ERROR = 1; 
	//commit update commit
	public static final int tv = 1;
	// commit update commit
	// commands - Splash
	public static final int CHECK_INTERNET = 1;
	public static final int LOAD_PREFS = 2;

	public static final int GOOGLE_AUTH = 3;
	public static final int LOAD_LOCAL_DB = 4;
	public static final int GRAVITY_REGISTER = 5;
	public static final int GO_TO_MAIN = 7;
	public static final int CONFIG_GCM = 6;
	// Activity Names
	public static final String AUTH_ACTIVITY = "AuthenticationActivity";
	public static final String SPLASH_ACTIVITY = "SplashActivity";
	//gravity urls
	public static final String GRAVITY_BASE_URL = "http://192.168.1.2/";
	public static final String GRAVITY_ACCOUNT_URL = GRAVITY_BASE_URL+"Account/";
	public static final String GRAVITY_GCM_URL = GRAVITY_BASE_URL+"GCM/";
	//google urls
	public static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";//require token
	// request codes
	public class RequestCodes {
		public static final int SPLASH_AUTH = 999;
		public static final int GRAVITY_REGISTER = 998;
		public static final int GRAVITY_SEND_GCM_CODE = 997;
	}

	public static class Callbacks {
		private interface GoogleAuthCallback {
			public void pushSuccess(String AuthToken, String Email);

			public void pushFailure(String Error, String Email);
		}
		public interface GCMCallback{
			public void displayMsg(String msg);
			public void storeRegisterationId(String regid, int appVersion);
		}
		public interface HttpCallback{
			public void httpResult(Object data, int RequestCode, int ResultCode);
		}
		public interface AuthActivityCallback extends GoogleAuthCallback {
		}

		public interface SplashActivityCallback extends GoogleAuthCallback, HttpCallback,GCMCallback{
			public void CheckInternet();

			public void LoadPreferences();

			public void GoogleAuth();

			public void LoadLocalDB();
			public void GravityRegister();
			public void GoToMain();
		}

	}

	public static class customPause {
		public customPause(final Activity mActivity, final int functionToken,
				int Time) {

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// if(mActivity.getClass().getSimpleName().toString().equals(Common.AUTH_ACTIVITY))
					switch (functionToken) {
					case CHECK_INTERNET:
						((SplashActivity) mActivity).CheckInternet();
						break;
					case LOAD_PREFS:
						((SplashActivity) mActivity).LoadPreferences();
						break;
					case GOOGLE_AUTH:
						((SplashActivity) mActivity).GoogleAuth();
						break;
					case LOAD_LOCAL_DB:
						((SplashActivity) mActivity).LoadLocalDB();
						break;
					case GRAVITY_REGISTER:
						((SplashActivity) mActivity).GravityRegister();
						break;
					case CONFIG_GCM:
						((SplashActivity) mActivity).ConfigureGCM();
						break;
					case GO_TO_MAIN:
						((SplashActivity) mActivity).GoToMain();
						break;
					default:
						break;
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

	public static class userData implements Serializable {
		public String email;
		public String name;
		public Boolean is_verification_complete;
		public Boolean is_sync_type;
		public Boolean is_registered;
		public String google_reg_id;
		public int google_regVer;
		public userData() {

		}
	}
	public static class User{
		private String name;
		private String email;
		public User(String name, String email)
		{
			this.name = name;
			this.email = email;
		}
	}
	public static class Users{
		private ArrayList<Common.User> users;
		public Users()
		{
			users = new ArrayList<Common.User>();
			for(int i=0;i<20;i++)
				users.add(new Common.User("username"+i, "email"+i));
		}
		public ArrayList<User> getAppUsers()
		{
			return users;
		}
		public Cursor getAppUsersCursor(Context mContext)
		{
			ContentResolver cr = mContext.getContentResolver();
	        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
	        return cur;
		}
	}
	public static class CustomViewsData
	{
		public static class MultiSelectRowData
		{
			public String text1, text2;
			public int iconRes;
		}
	}
	// for dialog creation and handling
	public static class CustomDialog{
		public static final void CustomDialog(final Context context, 
				int posText, int negText, 
				DialogInterface.OnClickListener negListener,
				DialogInterface.OnClickListener posListener) {

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle(R.string.delete);
			builder.setMessage(R.string.delete_message_confirm);
			 //builder.setView(view);
			
			if (posListener != null){
				builder.setPositiveButton(posText, posListener);
			}
			if (negListener != null){
				builder.setNegativeButton(negText, negListener);
			}

			builder.create().show();
		}

		public static final void CustomDialog(final Context context, View view,
				DialogInterface.OnClickListener negListener,
				DialogInterface.OnClickListener posListener, 
				int posText, int negText,
				String dialogTitle) {

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setView(view);
			builder.setTitle(dialogTitle);
			if (posListener != null){
				builder.setPositiveButton(posText, posListener);
			}
			if (negListener != null){
			builder.setNegativeButton(negText, negListener);
			}
			builder.create().show();
		}
		public static final void MultiChoiceDialog(final Context context, 
				MultiSelectListAdapter adapter,
				OnItemClickListener onItemClickListener,
				DialogInterface.OnClickListener negListener,
				DialogInterface.OnClickListener posListener, 
				int posText, int negText,
				String dialogTitle)
		{
			final AlertDialog builder = new AlertDialog.Builder(context)
		    .setTitle(dialogTitle)
		    .setAdapter(adapter, null)
		    .setPositiveButton(posText, posListener)
		    .setNegativeButton(negText, negListener)
		    .create();

			//final AlertDialog dialog = new AlertDialog.Builder(context);
			//final AlertDialog.Builder builder =  new AlertDialog.Builder(context);
			
			//builder.setMultiChoiceItems(items, "checked", "email", chooseItemListner);
			
			
			builder.getListView().setItemsCanFocus(false);
			builder.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			//builder.getListView().setOnItemSelectedListener(onItemSelectedListener);//.setOnItemClickListener(itemClickListener);
			builder.getListView().setOnItemClickListener(onItemClickListener);
			builder.show();
		}
	}
}
