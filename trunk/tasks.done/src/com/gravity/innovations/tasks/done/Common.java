package com.gravity.innovations.tasks.done;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//This class will include common keys used in application

//default decleration as below
//public static final int MY_KEY_SOME = 0;

//default usuage outside as below
//PublicKeys.MY_KEY_SOME
//pass it to switch, bundle extras, shared prefs etc 
public class Common {
	private static final String prefix = "com.gravity.innovations.tasks.done.";
	public static final String USER_EMAIL = prefix + "UserEmail";
	// used in shared prefs for io of user email address
	public static final String USER_NAME = prefix + "Username";
	public static final String DEVICE_MAC = prefix + "DeviceMAC";
	public static final String DEVICE_IMEI = prefix + "DeviceIMEI";
	public static final String DEVICE_TITLE = prefix + "DeviceTitle";
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

	// gravity urls
	public static final String GRAVITY_BASE_URL = "http://192.168.100.4/";
	public static final String GRAVITY_ACCOUNT_URL = GRAVITY_BASE_URL
			+ "Account/";
	public static final String GRAVITY_GCM_URL = GRAVITY_BASE_URL + "GCM/";
	public static final String GRAVITY_TASKLIST_URL = GRAVITY_BASE_URL
			+ "TaskList/";
	public static final String GRAVITY_TASKLIST_SHARE_URL = GRAVITY_BASE_URL
			+ "ShareTaskList/";
	public static final String GRAVITY_TASK_URL = GRAVITY_BASE_URL + "Task/";

	// google urls
	public static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=";// require
																														// token

	public static final String KEY_EXTRAS_NOTIFICATION_RECEPIENT = "_recipient_";
	// store acions call/sms/email
	public static final String KEY_EXTRAS_NOTIFICATION_ACTION_TYPE = "_actionType_";
	// store eithernumber/emailId
	public static final String KEY_INTENT_NOTIFICATION_SET_ACTION = "com.gravity.innovations.tasks.done.NOTIFICATION_INTENT";

	public static final String KEY_EXTRAS_TASK = "_task_";
	public static final String KEY_EXTRAS_LIST = "_list_";
	public static final String KEY_EXTRAS_TASK_ID = "_task_id";
	public static final String KEY_EXTRAS_LIST_ID = "_task_list_id";
	public static final String KEY_EXTRAS_NOTIFICATION_ID = "_notification_id";
	public static final String KEY_EXTRAS_NOTIFICATION_ID_COMPARING = "_comparing_";
	public static final String KEY_EXTRAS_ALARM_ID = "_alarm_id";

	// for canceling the notification

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
	public static String toDeviceTime(String serverTime) {
		try {

			// String ServerDate = data.optString("data");//
			// 2015-01-13T12:00:28.3367416Z
			// localtime on Desktop 5:02PM
			String DATEFORMAT_SERVER = "yyyy-M-dd'T'HH:mm:ss.SSSSSSS'Z'";
			SimpleDateFormat serverDateFormat = new SimpleDateFormat(
					DATEFORMAT_SERVER);

			String DATEFORMAT_DISPLAY = "yyyy-MM-dd HH:mm:ss.SSS";
			SimpleDateFormat displayFormat = new SimpleDateFormat(
					DATEFORMAT_DISPLAY);
			displayFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

			// returns formatted serverDateTime
			long serverTimeLong = serverDateFormat.parse(
					serverTime).getTime();
			// 1421135795416;
			serverTime = displayFormat.format(new Date(serverTimeLong));
			Log.e("ServerTime", serverTime);
			// returns formatted serverDateTime

			// takes cuurent devive time and convert it to display Format
			String currentDateTime = serverDateFormat.format(new Date());
			long deviceCurrentTimeLong = System.currentTimeMillis();
			String deviceTime = displayFormat.format(new Date(
					deviceCurrentTimeLong));
			
			return deviceTime;
			//Log.e("DeviceTime", deviceTime);
			// 1421216214567
			// takes cuurent devive time and convert it to display Format
		} catch (Exception e) {
			e.printStackTrace();
			return "";
			
		}
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
		public String imei;
		public String mac;
		public String device_title;
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

		public static final void set(final Context context, int posText,
				int negText, DialogInterface.OnClickListener negListener,
				DialogInterface.OnClickListener posListener) {

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(R.drawable.ic_warning_grey600_24dp);
			builder.setTitle(R.string.delete);
			builder.setMessage(R.string.delete_message_confirm);
			if (posListener != null) {
				builder.setPositiveButton(posText, posListener);
			}
			if (negListener != null) {
				builder.setNegativeButton(negText, negListener);
			}
			builder.create().show();
		}

		public static final void set(final Context context, View view,
				DialogInterface.OnClickListener posListener, int posText) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setView(view);
			if (posListener != null) {
				builder.setPositiveButton(posText, posListener);
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

		public static final void withRadioButton(final Context context,
				View view, DialogInterface.OnClickListener posListener,
				int posText) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setView(view);
			if (posListener != null) {
				builder.setPositiveButton(posText, posListener);
			}
			builder.create().show();
		}

		public static void set(Context mContext, String title, String message,
				String posText, String negText, OnClickListener posListener,
				OnClickListener negListener) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setIcon(android.R.drawable.ic_popup_reminder);
			builder.setTitle(title);
			builder.setCancelable(false);
			builder.setMessage(message);
			if (posListener != null) {
				builder.setPositiveButton(posText, posListener);
			}
			if (negListener != null) {
				builder.setNegativeButton(negText, negListener);
			}
			builder.create().show();
		}

		public static AlertDialog.Builder textDialog(Context mContext,
				AlertData data) {
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
		}

		// new dailogs for TaskReminderActivity
		public static final void calenderDialog(final Context context,
				View view, View headerView,
				DialogInterface.OnClickListener posListener, int posText) {
			// editText task edit Activity
			AlertDialog.Builder builder = new AlertDialog.Builder(context);

			builder.setView(view);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
					&& Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
				builder.setCustomTitle(headerView);
			}
			// builder.setTitle(dialogTitle);
			if (posListener != null) {
				builder.setPositiveButton(posText, posListener);
			}

			builder.create().show();

		}

		public static final void listDialog(Context context,
				String[] list_items, DialogInterface.OnClickListener listener) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setItems(list_items, listener);
			if (listener != null) {
			}
			builder.create().show();
		}

	}

	public static class AlertData implements Serializable {
		int icon;
		String title;
		String message;
		String posText;
		String negText;
		OnClickListener posListener;
		OnClickListener negListener;

		AlertData(int icon, String title, String message, String posText,
				String negText, OnClickListener posListener,
				OnClickListener negListener) {
			this.icon = icon;
			this.title = title;
			this.message = message;
			this.posText = posText;
			this.negText = negText;
			this.posListener = posListener;
			this.negListener = negListener;
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

	public static class CalenderToMillis {

		public static String getTimeInMillis(Calendar date, Calendar time) {
			int c_startDate = date.get(Calendar.DAY_OF_MONTH);
			int c_startMonth = date.get(Calendar.MONTH);
			int c_startYear = date.get(Calendar.YEAR);
			int c_startHour = time.get(Calendar.HOUR);
			int c_startMinute = time.get(Calendar.MINUTE);
			Calendar calendar_DateTime;
			calendar_DateTime = Calendar.getInstance();
			calendar_DateTime.set(c_startYear, c_startMonth, c_startDate,
					c_startHour, c_startMinute);
			long timeLong = calendar_DateTime.getTimeInMillis();
			return String.valueOf(timeLong);
		}

		public static String getTimeInMillis(Calendar calendar) {
			// test this one
			calendar.set(Calendar.HOUR, 00);
			calendar.set(Calendar.MINUTE, 00);
			long time = calendar.getTimeInMillis();
			return String.valueOf(time);
		}

	}

	public static class datetimeHelper {
		public static String getDate(Calendar calendar) {
			// this will run only first time
			String currentDate = null;
			SimpleDateFormat formatter = new SimpleDateFormat(
					"EEEE, MMM dd, yyyy");
			currentDate = formatter.format(calendar.getTimeInMillis());
			return currentDate;
		}

		public static String getDateInMs(Calendar calendar) {
			// this will run only first time
			return String.valueOf(calendar.getTimeInMillis());
		}

		public static Calendar mergeCalendars(Calendar date, Calendar time) {
			Calendar calendar_DateTime = Calendar.getInstance();
			calendar_DateTime.set(date.get(Calendar.YEAR),
					date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH),
					time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
			Long millis = calendar_DateTime.getTimeInMillis();
			return calendar_DateTime;
		}

		public static String getDate_repeat(Calendar calendar) {
			// this will run only first time
			String currentDate = null;
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			currentDate = formatter.format(calendar.getTimeInMillis());
			return currentDate;
		}

		// /for initial setup only
		public static Calendar setCalender_CurrentDate(Calendar cal) {
			// these are for initial setup only
			cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			return cal;
		}

		public static Calendar setCalender_CurrentTime(Calendar cal) {
			// these are for initial setup only
			// long currentTimeMillis = System.currentTimeMillis();
			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 0);
			cal.set(Calendar.DAY_OF_MONTH, 0);
			cal.set(Calendar.MONTH, 0);
			return cal;
		}

		public static Calendar setCalender_CurrentTimePlusOneHour(Calendar cal) {
			// these are for initial setup only
			long currentTimeMillis = System.currentTimeMillis();
			currentTimeMillis = currentTimeMillis + (3600000); // currentTime
																// plusOneHour
			Calendar today = Calendar.getInstance();
			today.setTimeInMillis(currentTimeMillis);
			cal.set(0, 0, 0, today.get(Calendar.HOUR_OF_DAY),
					today.get(Calendar.MINUTE), today.get(Calendar.SECOND));
			return cal;
		}

		public static String getTime(Calendar calendar/* , Boolean is24HourFormat */) {
			String currentDate = null, formateStyle = "hh:mm aa";
			// if (is24HourFormat) {
			// formateStyle = "HH:mm";// 24 hours
			// } else if (!is24HourFormat) {
			// formateStyle = "hh:mm aa";// 12 hours
			// }
			SimpleDateFormat formatter = new SimpleDateFormat(formateStyle);
			currentDate = formatter.format(calendar.getTimeInMillis());
			return currentDate;
		}

		public static Long calendar_ReturnDate(Calendar _calendar) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(_calendar.get(Calendar.YEAR),
					_calendar.get(Calendar.MONTH),
					_calendar.get(Calendar.DAY_OF_MONTH), 0, 0);
			return calendar.getTimeInMillis();

		}

		public static Long calendar_ReturnTime(Calendar _calendar) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(0, 0, 0, _calendar.get(Calendar.HOUR_OF_DAY),
					_calendar.get(Calendar.MINUTE));
			return calendar.getTimeInMillis();

		}
	}

	public static class SetAdapter {
		public static ArrayAdapter<CharSequence> simpleSpinner(Spinner spinner,
				int arrayResource, Context context) {
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(context, arrayResource,
							android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			return adapter;
		}
	}

	public static class RepeatModel {
		public static TaskModel DoesNotRepeat(TaskModel task) {
			task.rep_interval = 0;
			task.rep_intervalType = 0;
			task.rep_intervalExpiration = null;
			// task.rep_startDateTime = null;
			// task.rep_endDateTime = null;
			// task.rep_createdDateTime = null;
			task.rep_value = null;
			return task;
		}

		public static TaskModel RepeatEveryDay(TaskModel task) {
			task.rep_interval = 1;
			task.rep_intervalType = 1;
			task.rep_intervalExpiration = null;
			// task.rep_startDateTime = Notnull;
			// task.rep_endDateTime = Notnull;
			// task.rep_createdDateTime = Notnull;
			task.rep_value = null;
			return task;
		}

		public static TaskModel RepeatEveryWeek(TaskModel task) {
			task.rep_interval = 1;
			task.rep_intervalType = 2;
			task.rep_intervalExpiration = null;
			// task.rep_startDateTime = Notnull;
			// task.rep_endDateTime = Notnull;
			// task.rep_createdDateTime = Notnull;
			task.rep_value = null;
			return task;
		}

		public static TaskModel RepeatEveryMonth(TaskModel task) {
			task.rep_interval = 1;
			task.rep_intervalType = 3;
			task.rep_intervalExpiration = null;
			// task.rep_startDateTime = Notnull;
			// task.rep_endDateTime = Notnull;
			// task.rep_createdDateTime = Notnull;
			task.rep_value = null;
			return task;
		}

		public static TaskModel RepeatEveryYear(TaskModel task) {
			task.rep_interval = 1;
			task.rep_intervalType = 4;
			task.rep_intervalExpiration = null;
			// task.rep_startDateTime = Notnull;
			// task.rep_endDateTime = Notnull;
			// task.rep_createdDateTime = Notnull;
			task.rep_value = null;
			return task;
		}

	}

	public static class NotificaitonModel {

		public static TaskNotificationsModel deletable(
				TaskNotificationsModel model) {
			model.interval = 0; // 0
			model.interval_type = 0; // mins
			model.send_as = 0;
			return model;
		}
		
		public static TaskNotificationsModel atTimeOfEvent(
				TaskNotificationsModel model) {
			model.interval = 0; // 0
			model.interval_type = 1; // mins
			model.send_as = 0;
			return model;
		}

		public static TaskNotificationsModel ten_minsBefore(
				TaskNotificationsModel model) {
			model.interval = 10; // 10
			model.interval_type = 1; // mins
			model.send_as = 0;
			return model;
		}

		public static TaskNotificationsModel thirty_minsBefore(
				TaskNotificationsModel model) {
			model.interval = 30; // 30
			model.interval_type = 1;// mins
			model.send_as = 0;
			return model;
		}

		public static TaskNotificationsModel one_hrBefore(
				TaskNotificationsModel model) {
			model.interval = 1; // 1
			model.interval_type = 2;// 1 hour
			model.send_as = 0;
			return model;
		}

		// ////////////////new models
		public static TaskNotificationsModel atTimeOfEvent() {
			TaskNotificationsModel model = new TaskNotificationsModel();
			model.interval = 0; // 0
			model.interval_type = 1; // mins
			model.send_as = 0;
			return model;
		}

		public static TaskNotificationsModel ten_minsBefore() {
			TaskNotificationsModel model = new TaskNotificationsModel();
			model.interval = 10; // 10
			model.interval_type = 1; // mins
			model.send_as = 0;
			return model;
		}

		public static TaskNotificationsModel thirty_minsBefore() {
			TaskNotificationsModel model = new TaskNotificationsModel();
			model.interval = 30; // 30
			model.interval_type = 1;// mins
			model.send_as = 0;
			return model;
		}

		public static TaskNotificationsModel one_hrBefore() {
			TaskNotificationsModel model = new TaskNotificationsModel();
			model.interval = 1; // 1
			model.interval_type = 2;// 1 hour
			model.send_as = 0;
			return model;
		}

		// setUpTime
		public static TaskNotificationsModel onDayAtOnePM() {
			// onDayAtOnePM() setTime Find solution
			TaskNotificationsModel model = new TaskNotificationsModel();
			model.interval = 1; // 1
			model.interval_type = 6;// 6 is test
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 13);
			cal.set(Calendar.MINUTE, 00);
			model.interval_expiration = String.valueOf(cal.getTimeInMillis());
			model.send_as = 0;

			return model;
		}

		public static TaskNotificationsModel onDayAtTenAM() {
			// onDayAtTenAM() setTime
			TaskNotificationsModel model = new TaskNotificationsModel();
			model.interval = 1; // 1
			model.interval_type = 6;// 6 is test
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 10);
			cal.set(Calendar.MINUTE, 00);
			model.interval_expiration = String.valueOf(cal.getTimeInMillis());
			model.send_as = 0;
			return model;
		}

		public static TaskNotificationsModel onDayAtNineAM() {
			// onDayAtNineAM() setTime
			TaskNotificationsModel model = new TaskNotificationsModel();
			model.interval = 1; // 1
			model.interval_type = 6;// 6 is test
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 9);
			cal.set(Calendar.MINUTE, 00);
			model.interval_expiration = String.valueOf(cal.getTimeInMillis());
			model.send_as = 0;

			return model;
		}

		public static TaskNotificationsModel onDayBeforeAtElevenThirtyPM() {
			// onDayBeforeAtElevenThirtyPM() setTime
			TaskNotificationsModel model = new TaskNotificationsModel();
			model.interval = 1; // 1
			model.interval_type = 3;// 1 hour
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 30);
			model.interval_expiration = String.valueOf(cal.getTimeInMillis());

			model.send_as = 0;
			return model;
		}
	}

	public class ColorHex {
		public static final String taskDoneBlue = "#88B1C5";
		public static final String yellow = "#F4D05E";
		public static final String pink = "#F46C5E";
		public static final String seaGreen = "#00A5A6";
		public static final String moonLightBlue = "#34495e";
		public static final String orangeJazz = "#FF982F";
		public static final String caramel = "#FFC182";
		public static final String teal = "#00796B";
		public static final String indigo = "#303F9F";
		public static final String lightGreen = "#8BC34A";
		public static final String grey = "#9E9E9E";
		public static final String blueGrey = "#607D8B";
		public static final String depOrange = "#FF5722";
	}

	public static class DrawableResouces {

		public static int getDrawableResouce(int position) {
			int list_type = R.drawable.ic_assignment_grey600_24dp; // default
			if (position == 0) {
				list_type = R.drawable.ic_assignment_grey600_24dp;
			} else if (position == 1) {
				list_type = R.drawable.ic_attachment_grey600_24dp;
			} else if (position == 2) {
				list_type = R.drawable.ic_build_grey600_24dp;
			} else if (position == 3) {
				list_type = R.drawable.ic_cake_grey600_24dp;
			} else if (position == 4) {
				list_type = R.drawable.ic_content_paste_grey600_24dp;
			} else if (position == 5) {
				list_type = R.drawable.ic_directions_bus_grey600_24dp;
			} else if (position == 6) {
				list_type = R.drawable.ic_email_grey600_24dp;
			} else if (position == 7) {
				list_type = R.drawable.ic_event_note_grey600_24dp;
			} else if (position == 8) {
				list_type = R.drawable.ic_folder_grey600_24dp;
			} else if (position == 9) {
				list_type = R.drawable.ic_group_grey600_24dp;
			} else if (position == 10) {
				list_type = R.drawable.ic_insert_invitation_grey600_24dp;
			} else if (position == 11) {
				list_type = R.drawable.ic_laptop_mac_grey600_24dp;
			} else if (position == 12) {
				list_type = R.drawable.ic_local_airport_grey600_24dp;
			} else if (position == 13) {
				list_type = R.drawable.ic_local_cafe_grey600_24dp;
			} else if (position == 14) {
				list_type = R.drawable.ic_perm_phone_msg_grey600_24dp;
			} else if (position == 15) {
				list_type = R.drawable.ic_shopping_cart_grey600_24dp;
			} else if (position == 16) {
				list_type = R.drawable.ic_wc_grey600_24dp;
			} else if (position == 17) {
				list_type = R.drawable.ic_account_circle_grey600_24dp;
			} else if (position == 18) {
				list_type = R.drawable.ic_home_grey600_24dp;
			} else if (position == 19) {
				list_type = R.drawable.ic_work_grey600_24dp;
			}
			return list_type;
		}

		public static int compareDrawable(int dbResource) {
			int drawable = 0;
			if (dbResource == R.drawable.ic_assignment_grey600_24dp) {
				drawable = R.drawable.ic_assignment_white_24dp;
			} else if (dbResource == R.drawable.ic_attachment_grey600_24dp) {
				drawable = R.drawable.ic_attachment_white_24dp;
			} else if (dbResource == R.drawable.ic_build_grey600_24dp) {
				drawable = R.drawable.ic_build_white_24dp;
			} else if (dbResource == R.drawable.ic_cake_grey600_24dp) {
				drawable = R.drawable.ic_cake_white_24dp;
			} else if (dbResource == R.drawable.ic_content_paste_grey600_24dp) {
				drawable = R.drawable.ic_content_paste_white_24dp;
			} else if (dbResource == R.drawable.ic_directions_bus_grey600_24dp) {
				drawable = R.drawable.ic_directions_bus_white_24dp;
			} else if (dbResource == R.drawable.ic_email_grey600_24dp) {
				drawable = R.drawable.ic_email_white_24dp;
			} else if (dbResource == R.drawable.ic_event_note_grey600_24dp) {
				drawable = R.drawable.ic_event_note_white_24dp;
			} else if (dbResource == R.drawable.ic_folder_grey600_24dp) {
				drawable = R.drawable.ic_folder_white_24dp;
			} else if (dbResource == R.drawable.ic_group_grey600_24dp) {
				drawable = R.drawable.ic_group_white_24dp;
			} else if (dbResource == R.drawable.ic_insert_invitation_grey600_24dp) {
				drawable = R.drawable.ic_insert_invitation_white_24dp;
			} else if (dbResource == R.drawable.ic_laptop_mac_grey600_24dp) {
				drawable = R.drawable.ic_laptop_mac_white_24dp;
			} else if (dbResource == R.drawable.ic_local_airport_grey600_24dp) {
				drawable = R.drawable.ic_local_airport_white_24dp;
			} else if (dbResource == R.drawable.ic_local_cafe_grey600_24dp) {
				drawable = R.drawable.ic_local_cafe_white_24dp;
			} else if (dbResource == R.drawable.ic_perm_phone_msg_grey600_24dp) {
				drawable = R.drawable.ic_perm_phone_msg_white_24dp;
			} else if (dbResource == R.drawable.ic_shopping_cart_grey600_24dp) {
				drawable = R.drawable.ic_shopping_cart_white_24dp;
			} else if (dbResource == R.drawable.ic_wc_grey600_24dp) {
				drawable = R.drawable.ic_wc_white_24dp;
			} else if (dbResource == R.drawable.ic_account_circle_grey600_24dp) {
				drawable = R.drawable.ic_account_circle_white_24dp;
			} else if (dbResource == R.drawable.ic_home_grey600_24dp) {
				drawable = R.drawable.ic_home_white_24dp;
			} else if (dbResource == R.drawable.ic_work_grey600_24dp) {
				drawable = R.drawable.ic_work_white_24dp;
			}
			return drawable;
		}

		public static String getHexCode(int position) {
			String hex = Common.ColorHex.taskDoneBlue;
			;// default
			if (position == 0) {
				hex = Common.ColorHex.taskDoneBlue;
			} else if (position == 1) {
				hex = Common.ColorHex.moonLightBlue;
			} else if (position == 2) {
				hex = Common.ColorHex.orangeJazz;
			} else if (position == 3) {
				hex = Common.ColorHex.pink;
			} else if (position == 4) {
				hex = Common.ColorHex.seaGreen;
			} else if (position == 5) {
				hex = Common.ColorHex.yellow;
			} else if (position == 6) {
				hex = Common.ColorHex.caramel;
			}
			return hex;
		}
	}

	public static class Arrays {
		public static Integer[] resource_icons = {
		/*
		 * If any resource is added, updated or removed
		 * selectedDrawableResouce(param..) should also be updated in
		 * Common.Snippets.selectedDrawableResouce
		 */
		R.drawable.ic_assignment_grey600_24dp,
				R.drawable.ic_attachment_grey600_24dp,
				R.drawable.ic_build_grey600_24dp,
				R.drawable.ic_cake_grey600_24dp,
				R.drawable.ic_content_paste_grey600_24dp,
				R.drawable.ic_directions_bus_grey600_24dp,
				R.drawable.ic_email_grey600_24dp,
				R.drawable.ic_event_note_grey600_24dp,
				R.drawable.ic_folder_grey600_24dp,
				R.drawable.ic_group_grey600_24dp,
				R.drawable.ic_insert_invitation_grey600_24dp,
				R.drawable.ic_laptop_mac_grey600_24dp,
				R.drawable.ic_local_airport_grey600_24dp,
				R.drawable.ic_local_cafe_grey600_24dp,
				R.drawable.ic_perm_phone_msg_grey600_24dp,
				R.drawable.ic_shopping_cart_grey600_24dp,
				R.drawable.ic_wc_grey600_24dp,
				R.drawable.ic_account_circle_grey600_24dp,
				R.drawable.ic_home_grey600_24dp,
				R.drawable.ic_work_grey600_24dp };

		public static Drawable[] colorsDrawables = {
				/*
				 * If anyColor is added, updated or removed the below function
				 * also need to updated setItemBackgroundResouce(param..) in NDF
				 * selectedColorResouce(param) in
				 * Common.Snippets.selectedColorResouce
				 */
				Common.ShapesAndGraphics
						.getRingShape(Common.ColorHex.taskDoneBlue),
				Common.ShapesAndGraphics
						.getRingShape(Common.ColorHex.moonLightBlue),
				Common.ShapesAndGraphics
						.getRingShape(Common.ColorHex.orangeJazz),
				Common.ShapesAndGraphics.getRingShape(Common.ColorHex.pink),
				Common.ShapesAndGraphics.getRingShape(Common.ColorHex.seaGreen),
				Common.ShapesAndGraphics.getRingShape(Common.ColorHex.yellow),
				Common.ShapesAndGraphics.getRingShape(Common.ColorHex.caramel),
				Common.ShapesAndGraphics.getRingShape(Common.ColorHex.teal),
				Common.ShapesAndGraphics
						.getRingShape(Common.ColorHex.lightGreen),
				Common.ShapesAndGraphics.getRingShape(Common.ColorHex.grey),
				Common.ShapesAndGraphics.getRingShape(Common.ColorHex.blueGrey),
				Common.ShapesAndGraphics
						.getRingShape(Common.ColorHex.depOrange)

		};
	}

	public static class ShapesAndGraphics {

		public static int adjustAlpha(int color, float alpha) {
			// float f = alpha/100;
			int a = Math.round(255 * alpha);
			// ( 255 * (float)());
			int red = Color.red(color);
			int green = Color.green(color);
			int blue = Color.blue(color);
			return Color.argb(a, red, green, blue);
		}

		public static ShapeDrawable getfloatingButton(String hex) {
			ShapeDrawable circle;
			circle = new ShapeDrawable(new OvalShape());
			// circle.setBounds(10, 10, 20, 20);
			// circle.setPadding(14, 15, 10, 10);// L,T,R,B
			circle.getPaint().setColor(Color.parseColor(hex));
			return circle;
		}

		public static Drawable getCircularShape_day(String hexCode) {
			// this will be used to fill the rings
			GradientDrawable drawable = new GradientDrawable();
			drawable.setColor(Color.parseColor(hexCode));
			drawable.setShape(GradientDrawable.OVAL);
			drawable.setStroke(5, Color.parseColor(hexCode));
			drawable.setSize(2 * 20 + 5, 2 * 20 + 5);
			return drawable;
		}

		public static Drawable getRingShape_day(String hexCode) {
			GradientDrawable drawable = new GradientDrawable();
			drawable.setColor(Color.TRANSPARENT);
			drawable.setShape(GradientDrawable.OVAL);
			drawable.setStroke(5, Color.parseColor(hexCode));
			drawable.setSize(2 * 20 + 5, 2 * 20 + 5);
			// drawable.setSize(2*wheelRadius+wheelStrokeWidth,2*wheelRadius+wheelStrokeWidth);
			return drawable;
		}

		public static Drawable getCircularShape(String hexCode) {
			// this will be used to fill the rings
			GradientDrawable drawable = new GradientDrawable();
			drawable.setColor(Color.parseColor(hexCode));
			drawable.setShape(GradientDrawable.OVAL);
			drawable.setStroke(5, Color.parseColor(hexCode));
			drawable.setSize(2 * 10 + 5, 2 * 10 + 5);
			return drawable;
		}

		public static Drawable getRingShape(String hexCode) {
			GradientDrawable drawable = new GradientDrawable();
			drawable.setColor(Color.TRANSPARENT);
			drawable.setShape(GradientDrawable.OVAL);
			drawable.setStroke(5, Color.parseColor(hexCode));
			drawable.setSize(2 * 10 + 5, 2 * 10 + 5);
			// drawable.setSize(2*wheelRadius+wheelStrokeWidth,2*wheelRadius+wheelStrokeWidth);
			return drawable;
		}
	}

	public static class ContactStringConversion {

		public static String getDisplayName(Uri _uri, Context _context) {
			String name = null;
			Cursor cursor = _context.getContentResolver().query(_uri, null,
					null, null, null);
			if (cursor.moveToFirst()) {
				int idx = cursor
						.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
				name = cursor.getString(idx);
			}
			cursor.close();
			return name;
		}

		public static String getEmailAddress(String taskTitle, Context _context) {
			String email = null;
			String test = null;
			if (taskTitle.toString().toLowerCase().contains("email")) {
				test = taskTitle.toString().substring(6);
				test = test.trim();
				if (test.contains("content://com.android.contacts/data/")) {
					// String uriString = taskTitle.toString().substring(6);
					// uriString = uriString.trim();
					Uri uri = Uri.parse(test);
					Cursor cursor = _context.getContentResolver().query(uri,
							null, null, null, null);
					if (cursor.moveToFirst()) {
						int idx = cursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
						email = cursor.getString(idx);
					}
					cursor.close();
				}
			}
			return email;
		}

		public static String getPhoneNumber(String taskTitle, Context _context) {
			String phone = null;
			String test = null;
			if (taskTitle.toString().toLowerCase().contains("call")
					|| taskTitle.toString().toLowerCase().contains("sms")) {
				// test = taskTitle.toString().substring(5);
				if (taskTitle.toString().toLowerCase().contains("call")) {
					test = taskTitle.toString().substring(5);
				} else if (taskTitle.toString().toLowerCase().contains("sms")) {
					test = taskTitle.toString().substring(4);
				}
				test = test.trim();
				if (test.contains("ontent://com.android.contacts/contacts/lookup")) {
					Uri uri = Uri.parse(test);
					Cursor cursor = _context.getContentResolver().query(uri,
							null, null, null, null);

					if (cursor.moveToFirst()) {
						// int idx = cursor
						// .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
						// String name = cursor.getString(idx);
						int id = cursor
								.getColumnIndex(ContactsContract.Contacts._ID);
						String contactID = cursor.getString(id);

						Cursor cursor2 = _context.getContentResolver().query(
								CommonDataKinds.Phone.CONTENT_URI,
								null,
								CommonDataKinds.Phone.CONTACT_ID + " = "
										+ contactID, null, null);
						if (cursor2.moveToNext()) {
							phone = (cursor2
									.getString(cursor2
											.getColumnIndex(CommonDataKinds.Phone.NUMBER)));
						}
						cursor2.close();
					}
					cursor.close();
				}
			}
			return phone;
		}
	}

	public static class SoftKeyboard {

		public void showSoftKeyboard(View view, Context c) {
			if (view.requestFocus()) {
				// Extracted from MainActivity
				View mEditText = view;
				mEditText.setFocusable(true);
				mEditText.requestFocus();
				InputMethodManager imm = (InputMethodManager) c
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
			}
		}

		public static void hide(Activity mActivity) {
			if (mActivity.getCurrentFocus() != null) {
				InputMethodManager inputMethodManager = (InputMethodManager) mActivity
						.getSystemService(mActivity.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(mActivity
						.getCurrentFocus().getWindowToken(), 0);
			}
		}

		public static void show(View view, Activity mActivity) {
			view.setFocusable(true);
			view.requestFocus();
			InputMethodManager imm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
		}
	}

	public static class Notification {
		public static void cancel(int id, Context context) {
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(id);
		}
	}
}
