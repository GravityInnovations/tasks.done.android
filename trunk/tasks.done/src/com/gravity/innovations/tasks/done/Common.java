package com.gravity.innovations.tasks.done;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.json.JSONObject;

import android.animation.AnimatorSet.Builder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

//This class will include common keys used in application

//default decleration as below
//public static final int MY_KEY_SOME = 0;

//default usuage outside as below
//PublicKeys.MY_KEY_SOME
//pass it to switch, bundle extras, shared prefs etc 
public class Common {
	private static final String prefix = "com.gravity.innovations.tasks.done.";
	public static final String USER_EMAIL = prefix + "UserEmail";// used in
																	// shared
																	// prefs for
																	// io of
																	// user
																	// email
																	// address
	public static final String USER_NAME = prefix + "Username";
	public static final String USER_IMAGE = prefix + "UserImage";
	public static final String USER_DATA = prefix + "UserData";
	public static final String USER_IS_VERIFICATION_COMPLETE = prefix
			+ "GoogleVerificationComplete";
	public static final String USER_IS_SYNC_TYPE = prefix + "UserWillSync";
	public static final String USER_IS_REGISTERED = prefix + "UserRegistered";
	public static final String USER_ID_GRAVITY = prefix + "gUserId";
	public static final String ALL_USERS_SYNCED = prefix + "allUsersSynced";
	public static final String USER_LOCAL_ID = prefix + "_id";
	public static final String SHARED_PREF_KEY = prefix;
	public static final int SPLASH_TIME_OUT = 3000;
	public static final int SPLASH_TIME_OUT_SMALL = 1000;
	public static final String ACCOUNT_TYPE = "com.google";
	public static final String AUTH_TOKEN = prefix + "AuthToken";
	public static final String AUTH_TOKEN_TYPE = "oauth2:profile https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.me";// https://www.googleapis.com/auth/userinfo.profile";// https://www.googleapis.com/auth/drive.file https://www.googleapis.com/auth/datastoremobile https://www.googleapis.com/auth/appstate";
	public static final String EXTRA_MESSAGE = "message";
	public static final String GOOGLE_PROPERTY_REG_ID = "registration_id";
	public static final String GOOGLE_IS_USER_SYNCED = prefix
			+ "ProfileDataSynced";
	public static final String GOOGLE_PROPERTY_APP_VERSION = "appVersion";
	public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public final static String HAS_EXCEPTION = prefix + "hasException";
	public final static String EXCEPTION_TYPE = prefix + "ExceptionType";
	public final static String MESSAGE = prefix + "Message";

	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	public final static String GCM_SENDER_ID = "632707319263";

	public static final String HAS_INTERNET = prefix + "HasInternet";
	public static final String EXCEPTION = prefix + "Exception";
	public static final String USER_UNAPPROVE = prefix + "Unapproved";
	public static final String NETWORK_ERROR = prefix + "NetworkError";
	public static final int one = 2;

	public static final int HTTP_RESPONSE_OK = 0;
	public static final int HTTP_RESPONSE_ERROR = 1;
	// commit update commit
	public static final int tv = 1;
	// commit update commit
	// commands - Splash
	public static final int CHECK_INTERNET = 1;
	public static final int LOAD_PREFS = 2;
	public static final int GET_ACCOUNT = 3;
	public static final int GOT_ACCOUNT = 12;
	public static final int GOOGLE_AUTH = 4;
	public static final int GOOGLE_USER_INFO = 5;
	public static final int LOAD_LOCAL_DB = 6;
	public static final int GRAVITY_REGISTER = 7;
	public static final int GO_TO_MAIN = 8;
	public static final int CONFIG_GCM = 9;
	public static final int GRAVITY_SYNC = 10;
	public static final int USERS_SYNC = 11;

	// Activity Names
	public static final String AUTH_ACTIVITY = "AuthenticationActivity";
	public static final String SPLASH_ACTIVITY = "SplashActivity";

	//gravity urls
	public static final String GRAVITY_BASE_URL = "http://192.168.1.5/";
	public static final String GRAVITY_ACCOUNT_URL = GRAVITY_BASE_URL+"Account/";
	public static final String GRAVITY_GCM_URL = GRAVITY_BASE_URL+"GCM/";
	public static final String GRAVITY_TASKLIST_URL = GRAVITY_BASE_URL+"TaskList/";
	public static final String GRAVITY_TASKLIST_SHARE_URL = GRAVITY_BASE_URL+"ShareTaskList/";
	public static final String GRAVITY_TASK_URL = GRAVITY_BASE_URL+"Task/";
	
	//google urls
	public static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=";//require token
// request codes

	public class RequestCodes {
		public static final int SPLASH_ACC = 999;
		public static final int SPLASH_AUTH = 996;
		public static final int GRAVITY_REGISTER = 998;
		public static final int GRAVITY_SEND_GCM_CODE = 997;
		public static final int GRAVITY_SEND_TASKLIST = 995;
		public static final int GRAVITY_GET_TASKLISTS = 994;
		public static final int GOOGLE_GET_USER_INFO = 993;
		public static final int GRAVITY_VALIDATE_USERS = 992;
		public static final int GRAVITY_GET_TASKS = 991;
	}

	public static class serviceActions {
		public static final String START_APP = "startapplication";
		public static final String RESTART_SERVICE = "restartservice";
		public static final String USER_START_APP = "userstartedapplication";
	}

	public class EXCEPTIONS {
		public static final int NoException = 0;
		public static final int UserRecoverableAuthException = 1;
	}

	public static class Callbacks {
		public interface ServiceCallback {
			public void onServiceBound(AppHandlerService handleService);

			void startResultActivity(Intent intent, int RequestCode);
		}

		public interface TimeCallBack {
			public void onTimeReceive(Context mContext, Intent intent);

		}

		public interface GoogleAuthCallback {
			public void AuthResult(Intent i);

			// public void pushFailure(String Error, String Email);
		}

		public interface GCMCallback {
			public void displayMsg(String msg);

			public void storeGCMRegisterationId(String regid, int appVersion);
		}

		public interface HttpCallback {
			public void httpResult(JSONObject data, int RequestCode,
					int ResultCode);
		}

		public interface AuthActivityCallback extends GoogleAuthCallback {
		}

		public interface SplashCallback {
			public void CheckInternet();

			public void LoadPreferences();// 1

			public void GetAccount();

			public void GoogleAuth();

			public void GetUserDataFromGoogle();

			public void LoadLocalDB();// 2

			public void GravityRegister();

			public void GoToMainThread();

			public void ConfigureGCM();

			public void SyncAppData();

			public void SyncUsers(boolean isFirstTime);

		}

		public interface SplashActivityCallback extends GoogleAuthCallback,
				HttpCallback, GCMCallback, ServiceCallback, SplashCallback {

		}

		public interface SplashServiceCallback extends GoogleAuthCallback,
				HttpCallback, GCMCallback, SplashCallback {
			public void onAccountProvided(String email, int ResultCode);
		}

	}

	public static class HttpMethod {
		public static final int HttpGet = 1;
		public static final int HttpPost = 2;
		public static final int HttpDelete = 3;
		public static final int HttpPut = 4;
	}

	public static boolean hasInternet(Context mContext) {

		ConnectivityManager cm = (ConnectivityManager) mContext
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
		public int _id;
		public String name;
		public Boolean is_sync_type;
		public Boolean gravity_is_registered;
		public String google_reg_id;
		public int google_regVer;
		public String google_AuthToken;
		public String gravity_user_id;
		public Boolean google_is_data_synced;
		public Boolean all_users_synced;
		public static Bitmap image;

		public userData() {

		}
	}

	public static class User {
		private String name;
		private String email;

		public User(String name, String email) {
			this.name = name;
			this.email = email;
		}
	}

	public static class Users {
		private ArrayList<Common.User> users;

		public Users() {
			users = new ArrayList<Common.User>();
			for (int i = 0; i < 20; i++)
				users.add(new Common.User("username" + i, "email" + i));
		}

		public ArrayList<User> getAppUsers() {
			return users;
		}

		public Cursor getAppUsersCursor(Context mContext) {
			ContentResolver cr = mContext.getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
					null, null, null);
			return cur;
		}
	}

	public static class CustomViewsData {
		public static class MultiSelectRowData {
			public String text1, text2;
			public byte[] iconRes;
			public int iconResId;
			// public int iconRes;
		}
	}

	// for dialog creation and handling
	public static class CustomDialog {
		public static final void CustomDialog(final Context context, View view,
				String dialogTitle) {

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle(dialogTitle);
			builder.setView(view);
			builder.create().show();
		}

		public static final AlertDialog.Builder CustomDialog(
				final Context context, View view) {

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setView(view);
			builder.create().show();
			builder.setCancelable(true);
			return builder;
		}

		public static final void CustomDialog(final Context context,
				int posText, int negText,
				DialogInterface.OnClickListener negListener,
				DialogInterface.OnClickListener posListener) {

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setTitle(R.string.delete);
			builder.setMessage(R.string.delete_message_confirm);
			// builder.setView(view);

			if (posListener != null) {
				builder.setPositiveButton(posText, posListener);
			}
			if (negListener != null) {
				builder.setNegativeButton(negText, negListener);
			}

			builder.create().show();
		}

		public static final void CustomDialog(final Context context, View view,
				DialogInterface.OnClickListener negListener,
				DialogInterface.OnClickListener posListener, int posText,
				int negText, String dialogTitle) {

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setView(view);
			builder.setTitle(dialogTitle);
			if (posListener != null) {
				builder.setPositiveButton(posText, posListener);
			}
			if (negListener != null) {
				builder.setNegativeButton(negText, negListener);
			}
			builder.create().show();
		}

		public static final void MultiChoiceDialog(final Context context,
				MultiSelectListAdapter adapter,
				OnItemClickListener onItemClickListener,
				DialogInterface.OnClickListener negListener,
				DialogInterface.OnClickListener posListener, int posText,
				int negText, String dialogTitle) {
			AlertDialog builder = null;
			if (posListener != null && negListener != null)
				builder = new AlertDialog.Builder(context)
						.setTitle(dialogTitle).setAdapter(adapter, null)
						.setPositiveButton(posText, posListener)
						.setNegativeButton(negText, negListener).create();
			else if (posListener == null && negListener == null)
				builder = new AlertDialog.Builder(context)
						.setTitle(dialogTitle).setAdapter(adapter, null)
						.create();
			else if (posListener == null)
				builder = new AlertDialog.Builder(context)
						.setTitle(dialogTitle).setAdapter(adapter, null)
						.setNegativeButton(negText, negListener).create();
			else if (negListener == null)
				builder = new AlertDialog.Builder(context)
						.setTitle(dialogTitle).setAdapter(adapter, null)
						.setPositiveButton(posText, posListener).create();

			// final AlertDialog dialog = new AlertDialog.Builder(context);
			// final AlertDialog.Builder builder = new
			// AlertDialog.Builder(context);

			// builder.setMultiChoiceItems(items, "checked", "email",
			// chooseItemListner);

			builder.getListView().setItemsCanFocus(false);
			builder.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			// builder.getListView().setOnItemSelectedListener(onItemSelectedListener);//.setOnItemClickListener(itemClickListener);
			builder.getListView().setOnItemClickListener(onItemClickListener);
			builder.show();
		}

		public static void CustomDialog(Context mContext, String title,
				String message, String posText, String negText,
				OnClickListener posListener, OnClickListener negListener) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setIcon(android.R.drawable.ic_popup_reminder);
			builder.setTitle(title);
			builder.setCancelable(false);
			builder.setMessage(message);
			// builder.setView(view);

			if (posListener != null) {
				builder.setPositiveButton(posText, posListener);
			}
			if (negListener != null) {
				builder.setNegativeButton(negText, negListener);
			}

			builder.create().show();

		}
		public static AlertDialog.Builder TextDialog(Context mContext,AlertData data) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setIcon(android.R.drawable.ic_popup_reminder);
			builder.setTitle(data.title);
			builder.setCancelable(false);
			builder.setMessage(data.message);
			builder.setIcon(data.icon);
			// builder.setView(view);

			if (data.posListener != null) {
				builder.setPositiveButton(data.posText, data.posListener);
			}
			if (data.negListener != null) {
				builder.setNegativeButton(data.negText, data.negListener);
			}
			return builder;
			//builder//.create().show();

		}
	}
	public static class AlertData implements Serializable{
		int icon; String title;
		String message; String posText; String negText;
		OnClickListener posListener; OnClickListener negListener;
		AlertData(int icon, String title,
				String message, String posText, String negText,
				OnClickListener posListener, OnClickListener negListener)
				{
			this.icon=icon;
			this.title=title;
			this.message= message; this.posText= posText;this.negText=negText;
			this.posListener=posListener; this.negListener=negListener;
				}
	}
	public static class FormateTimeStrings {
		// this class only return formated time String to the dialogs
		public static String getFormatedDateTimeString(String remindAt) {
			StringTokenizer tokens = new StringTokenizer(remindAt, "/");
			String year_string = tokens.nextToken();// year
			String month_string = tokens.nextToken();// month
			int month = Integer.parseInt(month_string);
			String date_string = tokens.nextToken();// date
			String hours_string = tokens.nextToken();// time
			String minute_string = tokens.nextToken();// minute
			String newFormatedDate = null;
			if (month == 0) {
				newFormatedDate = year_string + " Jan " + date_string + ", "
						+ hours_string + ":" + minute_string;
				// list_item[0] = "Repeat; Once at " + newFormatedDate;
			} else if (month == 1) {
				newFormatedDate = year_string + " Feb " + date_string + ", "
						+ hours_string + ":" + minute_string;
				// list_item[0] = "Repeat; Once at " + newFormatedDate;
			} else if (month == 2) {
				newFormatedDate = year_string + " Mar " + date_string + ", "
						+ hours_string + ":" + minute_string;
				// list_item[0] = "Repeat; Once at " + newFormatedDate;
			} else if (month == 3) {
				newFormatedDate = year_string + " Apr " + date_string + ", "
						+ hours_string + ":" + minute_string;
				// list_item[0] = "Repeat; Once at " + newFormatedDate;
			} else if (month == 4) {
				newFormatedDate = year_string + " May " + date_string + ", "
						+ hours_string + ":" + minute_string;
				// list_item[0] = "Repeat; Once at " + newFormatedDate;
			} else if (month == 5) {
				newFormatedDate = year_string + " Jun " + date_string + ", "
						+ hours_string + ":" + minute_string;
				// list_item[0] = "Repeat; Once at " + newFormatedDate;
			} else if (month == 6) {
				newFormatedDate = year_string + " Jul " + date_string + ", "
						+ hours_string + ":" + minute_string;
				// list_item[0] = "Repeat; Once at " + newFormatedDate;
			} else if (month == 7) {
				newFormatedDate = year_string + " Aug " + date_string + ", "
						+ hours_string + ":" + minute_string;
				// list_item[0] = "Repeat; Once at " + newFormatedDate;
			} else if (month == 8) {
				newFormatedDate = year_string + " Sept " + date_string + ", "
						+ hours_string + ":" + minute_string;
				// list_item[0] = "Repeat; Once at " + newFormatedDate;
			} else if (month == 9) {
				newFormatedDate = year_string + " Oct " + date_string + ", "
						+ hours_string + ":" + minute_string;
				// list_item[0] = "Repeat; Once at " + newFormatedDate;
			} else if (month == 10) {
				newFormatedDate = year_string + " Nov " + date_string + ", "
						+ hours_string + ":" + minute_string;
				// list_item[0] = "Repeat; Once at " + newFormatedDate;
			} else if (month == 11) {
				newFormatedDate = year_string + " Dec " + date_string + ", "
						+ hours_string + ":" + minute_string;
			}
			// list_item[0] = "Repeat; Once at " + newFormatedDate;
			// list_item[0] = "Repeat: Once at " + temp.remind_at + " "; //
			// String.valueOf(temp.remind_interval);
			return newFormatedDate;
		}

		public static String getFormatedTimeString(String remindAt) {

			StringTokenizer tokens = new StringTokenizer(remindAt, "/");
			String year_string = tokens.nextToken();// year
			String month_string = tokens.nextToken();// month
			String date_string = tokens.nextToken();// date
			String hours_string = tokens.nextToken();// time
			String minute_string = tokens.nextToken();// minute
			String newFormatedDate = hours_string + ":" + minute_string;
			return newFormatedDate;
		}

		public static String getFormatedWeeklyTimeString(int weekday,
				String remindAt) {
			StringTokenizer tokens = new StringTokenizer(remindAt, "/");
			String year_string = tokens.nextToken();// year
			String month_string = tokens.nextToken();// month
			String date_string = tokens.nextToken();// date
			String hours_string = tokens.nextToken();// time
			String minute_string = tokens.nextToken();// minute
			String newFormatedDate = null;
			if (weekday == 0) {
				newFormatedDate = " Sun, at " + hours_string + ":"
						+ minute_string;
			} else if (weekday == 1) {
				newFormatedDate = " Mon, at " + hours_string + ":"
						+ minute_string;
			} else if (weekday == 2) {
				newFormatedDate = " Tue, at " + hours_string + ":"
						+ minute_string;
			} else if (weekday == 3) {
				newFormatedDate = " Wed, at " + hours_string + ":"
						+ minute_string;
			} else if (weekday == 4) {
				newFormatedDate = " Thu, at " + hours_string + ":"
						+ minute_string;
			} else if (weekday == 5) {
				newFormatedDate = " Fri, at " + hours_string + ":"
						+ minute_string;
			} else if (weekday == 6) {
				newFormatedDate = " Sat, at " + hours_string + ":"
						+ minute_string;
			}
			return newFormatedDate;
		}

		public static String getFormatedMonthlyDateTimeString(String remindAt) {

			StringTokenizer tokens = new StringTokenizer(remindAt, "/");
			String year_string = tokens.nextToken();// year
			String month_string = tokens.nextToken();// month
			String date_string = tokens.nextToken();// date
			int date = Integer.parseInt(date_string);
			String hours_string = tokens.nextToken();// time
			String minute_string = tokens.nextToken();// minute
			String newFormatedDate = null;
			if (date == 1) {
				newFormatedDate = date_string + "st, At: " + hours_string + ":"
						+ minute_string;
				// list_item[0] = "Repeat; Once at " + newFormatedDate;
			} else {
				newFormatedDate = date_string + "nd, At: " + hours_string + ":"
						+ minute_string;
			}
			// list_item[0] = "Repeat; Once at " + newFormatedDate;
			// list_item[0] = "Repeat: Once at " + temp.remind_at + " "; //
			// String.valueOf(temp.remind_interval);
			return newFormatedDate;
		}

		public static String getFormatedYearlyDateTimeString(String remindAt) {

			StringTokenizer tokens = new StringTokenizer(remindAt, "/");
			String year_string = tokens.nextToken();// year
			String month_string = tokens.nextToken();// month
			int month = Integer.parseInt(month_string);
			String date_string = tokens.nextToken();// date
			String hours_string = tokens.nextToken();// time
			String minute_string = tokens.nextToken();// minute
			String newFormatedDate = null;
			if (month == 0) {
				newFormatedDate = "Jan " + date_string + ", " + hours_string
						+ ":" + minute_string;
			} else if (month == 1) {
				newFormatedDate = "Feb " + date_string + ", " + hours_string
						+ ":" + minute_string;
			} else if (month == 2) {
				newFormatedDate = "Mar " + date_string + ", " + hours_string
						+ ":" + minute_string;
			} else if (month == 3) {
				newFormatedDate = "Apr " + date_string + ", " + hours_string
						+ ":" + minute_string;
			} else if (month == 4) {
				newFormatedDate = "May " + date_string + ", " + hours_string
						+ ":" + minute_string;
			} else if (month == 5) {
				newFormatedDate = "Jun " + date_string + ", " + hours_string
						+ ":" + minute_string;
			} else if (month == 6) {
				newFormatedDate = "Jul " + date_string + ", " + hours_string
						+ ":" + minute_string;
			} else if (month == 7) {
				newFormatedDate = "Aug " + date_string + ", " + hours_string
						+ ":" + minute_string;
			} else if (month == 8) {
				newFormatedDate = "Sept " + date_string + ", " + hours_string
						+ ":" + minute_string;
			} else if (month == 9) {
				newFormatedDate = "Oct " + date_string + ", " + hours_string
						+ ":" + minute_string;
			} else if (month == 10) {
				newFormatedDate = "Nov " + date_string + ", " + hours_string
						+ ":" + minute_string;
			} else if (month == 11) {
				newFormatedDate = "Dec " + date_string + ", " + hours_string
						+ ":" + minute_string;
			}
			return newFormatedDate;
		}

		public static void getMillis(String date) {
			// gets dateTime string of format1 type
			SimpleDateFormat format1 = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
			format1.setTimeZone(TimeZone.getTimeZone("UTC"));
			SimpleDateFormat format2 = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss");
			format2.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date d1;
			try {
				d1 = format2.parse(date);
				Log.e("date", " ///" + String.valueOf(d1));
				Long timeInMillis = d1.getTime();
				Log.e("date in millis", " ///" + String.valueOf(timeInMillis));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// long CurrentDeviceTime = System.currentTimeMillis();
			// String dateString = format1.format(new Date(CurrentDeviceTime));
			// Log.e("CurrentDeviceTime", " ///" + dateString);
		}
	}
}
