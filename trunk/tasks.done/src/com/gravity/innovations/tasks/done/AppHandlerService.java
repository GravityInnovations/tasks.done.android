package com.gravity.innovations.tasks.done;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.ContentResolver;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Contacts.Photo;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AppHandlerService extends Service implements
		Common.Callbacks.SplashServiceCallback {
	public Common.userData user_data;
	private ArrayList<String> progress_tasks;

	private NotificationManager mNotificationManager;
	private Intent thisIntent;
	private Intent AppStateIntent;
	public String AppStateClassName;
	private Activity FocusedActivity;

	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mSharedPreferencesEditor;
	private Boolean isFirstTime = true;
	private Context mContext = this;
	public DatabaseHelper db = null;
	public boolean hasInternet = false;
	private NotificationHandler nH= null;
	public AppHandlerService() {
		super();
		// setIntentRedelivery(true);
		// mNotificationManager = (NotificationManager)
		// this.getSystemService(Context.NOTIFICATION_SERVICE);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		try {

			Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

				@Override
				public void uncaughtException(Thread thread, Throwable ex) {
					addProgressTask(ex.getLocalizedMessage());
				}
			});
			// super.onCreate();
			// AppContext = getApplicationContext();
			progress_tasks = new ArrayList<String>();
			AppStateIntent = new Intent(this, SplashActivity.class);
			AppStateClassName = SplashActivity.class.getName();
			user_data = new Common.userData();
			nH = new NotificationHandler(this);
			
			// InitEvents();
			
			sendNotification("task.done", "background service is running", 2);
			//
//			 LoadLocalDB() ;
//			SyncUsers(true);
			//TriggerEvent(Common.USERS_SYNC);
			TriggerEvent(Common.LOAD_PREFS);
		} catch (Exception ex) {
			progress_tasks.add(0, ex.getLocalizedMessage());
		}

	}

	// @Override
	// protected void onHandleIntent(Intent intent) {
	// //thisIntent = new Intent(this, testActivity.class);
	// //sendNotification("App Service handle", "my message"+x, 2);
	// //while(true);
	// }
	int dddd = 0;


	boolean commandLock = false;
	@SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//sendNotification("title", "onstart"+dddd,dddd++);
		try {
			
			if (flags == 0) {
				thisIntent = intent;
				((TheApp) getApplicationContext()).setService(this);

				// sendNotification("title", "msg",dddd);
				// dddd++;
				if (intent.getAction() == Common.serviceActions.START_APP) {

					// Bundle b = new Bundle();
					// b.putBinder("binder",new AppHandlerBinder());
					// AppStateIntent.putExtra("AppServiceExtra", b);
					// AppStateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// startActivity(AppStateIntent);

					// TriggerEvent(Common.LOAD_LOCAL_DB);
					// sendNotification("App Service Started", "my message"+x,
					// 1);
				}
				else if(intent.getAction() == Common.serviceActions.RESTART_SERVICE)
				{
					sendNotification("task.done", "background service is running", 2);

					TriggerEvent(Common.LOAD_PREFS);
				}

				if (isFirstTime) {
					isFirstTime = false;

				}
			}
		} catch (Exception ex) {
			// Toast.makeText(getApplicationContext(), "Error: " +
			// ex.toString(),
			// Toast.LENGTH_LONG).show();
		}
		// sendNotification("App started", "my message"+x, 1111);
		return super.onStartCommand(intent, flags, startId);
	}

	@SuppressLint("NewApi")
	public void sendNotification(String Title, String Message, int RequestCode) {
		nH.showNotification(RequestCode, Message, new Intent(this, AppHandlerService.class));
//		thisIntent = new Intent(this, testActivity.class);
//		// Bundle b = new Bundle();
//		// b.putBinder("mybinder",new AppHandlerBinder());
//		// thisIntent.putExtra("service", b);
//		// Intent intent = new Intent(this, MainActivity.class);
//		mNotificationManager = (NotificationManager) this
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//
//		PendingIntent contentIntent = // PendingIntent.getService(this,
//		// RequestCode, thisIntent, 0);
//
//		PendingIntent.getActivity(this, RequestCode, thisIntent, /*
//																 * PendingIntent.
//																 * FLAG_UPDATE_CURRENT
//																 * |
//																 */
//				Intent.FILL_IN_DATA | Intent.FLAG_ACTIVITY_NEW_TASK);
//		// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//		// use the right class it should be called from the where alarms are set
//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//				this)
//				.setSmallIcon(R.drawable.ic_launcher)
//				.setContentTitle(Title)
//				.setStyle(
//						new NotificationCompat.BigTextStyle().bigText(Message))
//				.setDefaults(
//						Notification.FLAG_NO_CLEAR
//								| Notification.FLAG_ONGOING_EVENT
//								| Notification.DEFAULT_SOUND
//								| Notification.DEFAULT_LIGHTS
//								| Notification.DEFAULT_VIBRATE
//								| Notification.DEFAULT_ALL)
//				.setContentText(Message);
//		// mBuilder.addAction(R.drawable.ic_action_cancel, "Dismiss",
//		// contentIntent);
//		// mBuilder.addAction(R.drawable.ic_action_accept, "Done",
//		// contentIntent);
//		// x++;
//
//		mBuilder.setContentIntent(contentIntent);
//		mNotificationManager.notify(RequestCode, mBuilder.build());

	}

	private final IBinder mBinder = new AppHandlerBinder();

	@Override
	public IBinder onBind(Intent intent) {
		// sendNotification("App binded", "my message"+x, 1111);
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	public class AppHandlerBinder extends Binder {
		AppHandlerService getService() {
			return AppHandlerService.this;
		}
	}

	// @Override
	// public void setIntentRedelivery(boolean enabled) {
	// // TODO Auto-generated method stub
	// super.setIntentRedelivery(enabled);
	// }

	public void addProgressTask(final String s) {
		
			TimerTask t = new TimerTask() {
				@Override
				public void run() {
					if (FocusedActivity != null
							&& FocusedActivity.getClass() == SplashActivity.class) {
					try {
						String temp = "";
						for (int i = 0; i < progress_tasks.size(); i++) {

							temp += progress_tasks.get(i) + "\n";
						}
						SplashActivity a = ((SplashActivity) FocusedActivity);//

						a.addProgressTask(s, temp);// +"\ndone: "+donetasks+" todo: "+todotasks,
													// temp);
					} catch (Exception ex) {
						progress_tasks.add(0, ex.getLocalizedMessage());
					}
					}
					if (s != "")
						progress_tasks.add(0, s);
				}

			};
			t.run();
		}
	

	public void onActivityOpen(Activity activity, Context context) {
		try {
			FocusedActivity = activity;

			if (FocusedActivity != null
					&& !FocusedActivity.toString().toLowerCase()
							.contains(AppStateClassName.toLowerCase())) {
				if (AppStateClassName != SplashActivity.class.getName()) {
					FocusedActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AppStateIntent.putExtra("user", user_data);
							FocusedActivity.startActivity(AppStateIntent);
							FocusedActivity.finish();
						}
					});
					// startActivity(AppStateIntent);

				}
			}
			// if(FocusedActivity!= null && FocusedActivity.getClass() ==
			// SplashActivity.class)
			// {
			// addProgressTask("User interaction");
			// }
			// addProgressTask("test");
		} catch (Exception ex) {
			addProgressTask(ex.getLocalizedMessage());
			// progress_tasks.add(0,ex.getLocalizedMessage());
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		sendNotification("task.done", "Background service stoped", 2);
		((TheApp) getApplicationContext()).setService(null);
		super.onDestroy();

	}

	private GoogleCloudMessaging gcm;
	private com.gravity.innovations.tasks.done.GoogleAuth mAuth;
	private int todotasks = 0;
	private int donetasks = 0;

	public void TriggerEvent(final int eventId) {
		AsyncTask<Void, Void, Void> t = new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				todotasks++;
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				switch (eventId) {
				case Common.LOAD_LOCAL_DB:
					addProgressTask(getString(R.string.load_db));
					LoadLocalDB();
					donetasks++;
					break;
				case Common.LOAD_PREFS:
					addProgressTask(getString(R.string.load_sp));
					// TriggerNextEvent();
					LoadPreferences();

					TriggerEvent(Common.LOAD_LOCAL_DB);

					if (user_data.is_sync_type) {
						if (user_data.email == "" || user_data.email == null)
							TriggerEvent(Common.GET_ACCOUNT);// GetAccount();

						else if (user_data.email != ""
								&& user_data.email != null) {
							addProgressTask(getString(R.string.check_internet));
							TriggerEvent(Common.CHECK_INTERNET);
						}

						// TriggerWaitEvent(Common.CHECK_INTERNET);
						donetasks++;
					} else {

						TriggerEvent(Common.GO_TO_MAIN);
						donetasks++;
					}

					break;
				case Common.CHECK_INTERNET:
					CheckInternet();
					donetasks++;
					break;
				case Common.CONFIG_GCM:
					ConfigureGCM();
					break;
				case Common.GET_ACCOUNT:

					GetAccount();

					break;
				case Common.GOT_ACCOUNT:

					if (user_data.is_sync_type && user_data.email != null) {

						if (!user_data.gravity_is_registered) {
							TriggerEvent(Common.GRAVITY_REGISTER);
						}

						TriggerEvent(Common.GOOGLE_AUTH);
					}
					donetasks++;
					break;
				case Common.USERS_SYNC:
					if (user_data.is_sync_type) {// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>needs
													// review
						addProgressTask(getString(R.string.users_sync));
						if (!user_data.all_users_synced)
							SyncUsers(true);
						else
							SyncUsers(false);
						//
						// SyncUsers();
					}
					donetasks++;
					break;
				case Common.GOOGLE_AUTH:
					GoogleAuth();

					break;
				case Common.GOOGLE_USER_INFO:
					GetUserDataFromGoogle();
					break;
				case Common.GRAVITY_REGISTER:
					if (!user_data.gravity_is_registered) {
						addProgressTask(getString(R.string.gravity_register));

						GravityRegister();
					}
					break;
				case Common.GRAVITY_SYNC:

					if (user_data.gravity_is_registered) {
						addProgressTask(getString(R.string.gravity_sync));
						SyncAppData();

					}
					break;
				case Common.GO_TO_MAIN:
					// donetasks++;
					todotasks--;
					int d = donetasks;
					int x = todotasks;
					if (donetasks == todotasks) {
						AppStateIntent = new Intent(FocusedActivity,
								MainActivity.class);
						//AppStateClassName = MainActivity.class.getName();
						FocusedActivity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								AppStateIntent.putExtra("user", user_data);
								FocusedActivity.startActivity(AppStateIntent);
								FocusedActivity.finish();
							}
						});
						//
						// FocusedActivity.finish();

						// addProgressTask(">>>>>>>>>>>>>>>>>>main launched<<<<<<<<<<<<<<<<<<<");
					} else
						try {
							Thread.sleep(2000);
							// this.execute();
							TriggerEvent(Common.GO_TO_MAIN);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					break;
				default:
					addProgressTask("No such option exists yet");
					todotasks--;
					break;

				}
				return null;
			}
		};
		t.execute();

		// splash_actions.get(eventItrator++).start();

	}

	@Override
	public void LoadLocalDB() {
		db = new DatabaseHelper(this);
		db.User_New(new UserModel("sp","sp"));
	}

	@Override
	public void AuthResult(Intent i) {
		if (i.getBooleanExtra(Common.HAS_EXCEPTION, false))
		// && i.getIntExtra(Common.EXCEPTION_TYPE,Common.EXCEPTIONS.NoException)
		// != Common.EXCEPTIONS.NoException)
		{
			if (i.getIntExtra(Common.EXCEPTION_TYPE,
					Common.EXCEPTIONS.NoException) != Common.EXCEPTIONS.NoException) {
				if (FocusedActivity != null) {

					// startActivityForResult(intent,
					// Common.RequestCodes.SPLASH_ACC);
					((Common.Callbacks.ServiceCallback) FocusedActivity)
							.startResultActivity(i,
									Common.RequestCodes.SPLASH_AUTH);// .loginDialog(mContext);

				} else {
					AuthResult(i);
					// sendNotification("task.done",
					// "you need to open activity",2);
					// show notification to open splash
				}
				//
			}

		}

		else if (i.getStringExtra(Common.AUTH_TOKEN) != null) {
			mSharedPreferencesEditor.putString(Common.AUTH_TOKEN,
					i.getStringExtra(Common.AUTH_TOKEN));
			user_data.google_AuthToken = i.getStringExtra(Common.AUTH_TOKEN);
			mSharedPreferencesEditor.commit();
			// TriggerWaitEvent(Common.CONFIG_GCM);
			// addProgressTask(getString(R.string.checking_other_settings));
			// addProgressTask(getString(R.string.config_gcm));
			addProgressTask("Google Authentication complete");
			if (hasInternet) {
				addProgressTask(getString(R.string.google_get_user_info));

				TriggerEvent(Common.GOOGLE_USER_INFO);
			}
			donetasks++;
			//

			// runWorker
		}
	}

	public void httpResult(JSONObject data, int RequestCode, int ResultCode) {
		switch (RequestCode) {
		case Common.RequestCodes.GRAVITY_REGISTER:

			if (ResultCode == Common.HTTP_RESPONSE_OK) {
				data = data.optJSONObject("data");
				user_data.gravity_user_id = data.optString("UserId");
				user_data.gravity_is_registered = true;
				
				addProgressTask(getString(R.string.gravity_registration_complete));
				mSharedPreferencesEditor.putBoolean(Common.USER_IS_REGISTERED,
						true);
				mSharedPreferencesEditor.putString(Common.USER_ID_GRAVITY,
						user_data.gravity_user_id);
				TriggerEvent(Common.GRAVITY_SYNC);
			} else {
				addProgressTask(getString(R.string.gravity_registration_error));
				mSharedPreferencesEditor.putBoolean(Common.USER_IS_REGISTERED,
						false);

			}
			mSharedPreferencesEditor.commit();
			// TriggerWaitEvent(Common.GRAVITY_SYNC);

			// TriggerWaitEvent(Common.CONFIG_GCM);
			// addProgressTask(getString(R.string.checking_other_settings));
			donetasks++;
			break;
		case Common.RequestCodes.GRAVITY_SEND_GCM_CODE:
			Toast.makeText(
					this,
					"Result from gravity for gcm code send request "
							+ ResultCode, Toast.LENGTH_LONG).show();
			break;
		case Common.RequestCodes.GRAVITY_GET_TASKLISTS:
			if (ResultCode == Common.HTTP_RESPONSE_OK) {
				JSONArray arr_data = data.optJSONArray("data");
				DatabaseHelper db = new DatabaseHelper(this);
				for (int i = 0; i < arr_data.length(); i++) {
					try {
						JSONObject temp = arr_data.getJSONObject(i);
						TaskListModel model = new TaskListModel(
								temp.getString("Title"));
						db.TaskList_New(model);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				addProgressTask(getString(R.string.gravity_fetch_data_success));
			} else {
				addProgressTask(getString(R.string.gravity_fetch_data_failed));
			}
			// TriggerEvent(Common.USERS_SYNC);
			donetasks++;

			break;
		case Common.RequestCodes.GRAVITY_GET_TASKS:
			if (ResultCode == Common.HTTP_RESPONSE_OK) {
				JSONArray arr_data = data.optJSONArray("data");
				DatabaseHelper db = new DatabaseHelper(this);
				for (int i = 0; i < arr_data.length(); i++) {
					try {
						JSONObject temp = arr_data.getJSONObject(i);
						TaskListModel model = new TaskListModel(
								temp.getString("Title"));
						db.TaskList_New(model);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				addProgressTask(getString(R.string.gravity_fetch_data_success));
			} else {
				addProgressTask(getString(R.string.gravity_fetch_data_failed));
			}
			// TriggerEvent(Common.USERS_SYNC);
			donetasks++;

			break;
		case Common.RequestCodes.GOOGLE_GET_USER_INFO:
			// save user info
			try {
				user_data.name = data.getString("name");
				mSharedPreferencesEditor.putString(Common.USER_NAME,
						user_data.name);

				getAndSaveBitmap(data.getString("picture"));
				// next step in getAndSaveBitmap function
				mSharedPreferencesEditor.commit();
				addProgressTask("User information fetch complete");
				donetasks++;

			} catch (JSONException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case Common.RequestCodes.GRAVITY_VALIDATE_USERS:
			if (ResultCode == Common.HTTP_RESPONSE_OK) {
				JSONArray jdata= data.optJSONArray("data");//data.optJSONObject("data");
				//user_data.gravity_user_id = data.optString("UserId");
				for(int i=0;i<jdata.length();i++)
				{
					JSONObject juser = null;
					try {
						juser = jdata.getJSONObject(i);
						String Email = juser.optString("Email");
						if(Email!=null && Email!="")
						for(UserModel user:AppUsers)
						{
							if(user.email.toLowerCase().equals(Email.toLowerCase())||user.email == Email){
								user.name = juser.optString("Name");
								user.server_id = juser.optString("UserId");
								db.User_validate(user);
								break;
							}
							//user.email = juser.optString("Email");
							
						}
						
						
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			//TriggerEvent(Common.GO_TO_MAIN);
			break;
		}
	}

	public void getAndSaveBitmap(final String url) {
		AsyncTask<Void, Void, Void> t = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Bitmap bitmapImage = null;
				try {
					InputStream in = new java.net.URL(url).openStream();
					bitmapImage = BitmapFactory.decodeStream(in);
					ContextWrapper cw = new ContextWrapper(
							getApplicationContext());
					File directory = cw
							.getDir("taskdone", Context.MODE_PRIVATE);
					if (!directory.exists())
						directory.mkdir();
					File mypath = new File(directory, "profile.jpg");
					if (mypath.exists())
						mypath.delete();
					FileOutputStream fos = null;
					try {
						// fos = openFileOutput(filename, Context.MODE_PRIVATE);

						fos = new FileOutputStream(mypath);

						// Use the compress method on the BitMap object to write
						// image to the OutputStream
						bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100,
								fos);
						fos.flush();
						fos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					// user_data.image = bitmapImage;
					user_data.image = bitmapImage;
					// Bitmap.createScaledBitmap(bitmapImage,
					// 75, 75, true);
					mSharedPreferencesEditor.putString(Common.USER_IMAGE,
							mypath.getAbsolutePath());
					mSharedPreferencesEditor.putBoolean(
							Common.GOOGLE_IS_USER_SYNCED, true);
					mSharedPreferencesEditor.commit();
					// donetasks++;
					// reg to gravity
					// if(!user_data.gravity_is_registered)
					// {
					// addProgressTask(getString(R.string.gravity_register));
					//
					// TriggerWaitEvent(Common.GRAVITY_REGISTER);
					//
					// }
					// else
					// {
					// TriggerWaitEvent(Common.GO_TO_MAIN);
					// addProgressTask(getString(R.string.complete));
					// }
					// return bitmapImage;
				}// End of try block......
				catch (Exception e) {
					// status = e.getMessage();
					// return null;
				}
				return null;
			}

		};
		t.execute();
	}

	@Override
	public void displayMsg(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void CheckInternet() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = cm.getActiveNetworkInfo();
		boolean flag = true;
		if (i == null || !i.isAvailable() || !i.isConnected())
			flag = false;

		if (flag) {
			hasInternet = true;
			addProgressTask(getString(R.string.internet_stable));
			addProgressTask(getString(R.string.checking_other_settings));
			addProgressTask(getString(R.string.config_gcm));
			TriggerEvent(Common.CONFIG_GCM);
			TriggerEvent(Common.GOT_ACCOUNT);
			TriggerEvent(Common.USERS_SYNC);

			// TriggerNextEvent();
			// TriggerNextEvent();
			// TriggerWaitEvent(Common.CONFIG_GCM);

		} else {
			addProgressTask(getString(R.string.no_internet));
			addProgressTask(getString(R.string.require_internet));
			showTextDialog(
					"Internet Requires",
					"This application requires internet to use sync and share features",
					"Retry!", "Use offline", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							TriggerEvent(Common.CHECK_INTERNET);
						}
					}, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							TriggerEvent(Common.GO_TO_MAIN);
						}
					});
			//
			try {
				// Thread.sleep(2000);

				// TriggerEvent(Common.CHECK_INTERNET);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TriggerWaitEvent(Common.GO_TO_MAIN);
			// addProgressTask(getString(R.string.complete));
			// avoidTasks = splash_actions.size()-doneTasks;
		}
	}

	private void showTextDialog(final String Title, final String message,
			final String posText, final String negText,
			final DialogInterface.OnClickListener posListener,
			final DialogInterface.OnClickListener negListener) {
		if (FocusedActivity != null) {

			FocusedActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Common.CustomDialog.CustomDialog((Context) FocusedActivity,
							Title, message, posText, negText, posListener,
							negListener);
				}
			});
		} else {
			try {
				Thread.sleep(1000);
				showTextDialog(Title, message, posText, negText, posListener,
						negListener);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public void LoadPreferences() {
		mSharedPreferences = getSharedPreferences(Common.SHARED_PREF_KEY,
				MODE_MULTI_PROCESS);
		mSharedPreferencesEditor = mSharedPreferences.edit();
		user_data.email = mSharedPreferences.getString(Common.USER_EMAIL, null);
		user_data.is_sync_type = mSharedPreferences.getBoolean(
				Common.USER_IS_SYNC_TYPE, true);
		user_data.google_AuthToken = mSharedPreferences.getString(
				Common.AUTH_TOKEN, null);
		user_data.gravity_is_registered = mSharedPreferences.getBoolean(
				Common.USER_IS_REGISTERED, false);
		user_data.gravity_user_id = mSharedPreferences.getString(
				Common.USER_ID_GRAVITY, "");
		user_data.google_regVer = mSharedPreferences.getInt(
				Common.GOOGLE_PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		
		user_data.google_reg_id = mSharedPreferences.getString(
				Common.GOOGLE_PROPERTY_REG_ID, "");
		user_data.name = mSharedPreferences.getString(Common.USER_NAME, "");
		user_data.all_users_synced = mSharedPreferences.getBoolean(
				Common.ALL_USERS_SYNCED, false);

		user_data.google_is_data_synced = mSharedPreferences.getBoolean(
				Common.GOOGLE_IS_USER_SYNCED, false);
		// user_data.image = BitmapFactory.decodeFile(
		// mSharedPreferences.getString(Common.USER_IMAGE, ""));
		try {
			user_data.image = Bitmap.createScaledBitmap(BitmapFactory
					.decodeFile(mSharedPreferences.getString(Common.USER_IMAGE,
							"")), 75, 75, true);
		} catch (Exception ex) {
			addProgressTask(ex.getLocalizedMessage());

		}
	}

	@Override
	public void GetAccount() {
		if (FocusedActivity != null) {
			String[] accountTypes = new String[] { "com.google" };
			Intent intent = AccountPicker.newChooseAccountIntent(null, null,
					accountTypes, true, null, null, null, null);
			// startActivityForResult(intent, Common.RequestCodes.SPLASH_ACC);
			((Common.Callbacks.ServiceCallback) FocusedActivity)
					.startResultActivity(intent, Common.RequestCodes.SPLASH_ACC);// .loginDialog(mContext);

		} else {
			try {
				Thread.sleep(500);
				TriggerEvent(Common.GET_ACCOUNT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// GetAccount();
			// sendNotification("task.done", "you need to open activity", 2);
			// show notification to open splash
		}
		//
		// TODO Auto-generated method stub

	}

	@Override
	public void GoogleAuth() {
		// TODO Auto-generated method stub
		addProgressTask(getString(R.string.google_auth));
		mAuth = new GoogleAuth(mContext, null);
		mAuth.SetAccount(user_data.email);
		mAuth.execute();
	}

	@Override
	public void GetUserDataFromGoogle() {
		// TODO Auto-generated method stub
		HttpTask Temp = new HttpTask(mContext, Common.USER_INFO_URL
				+ user_data.google_AuthToken, null, Common.HttpMethod.HttpGet,
				Common.RequestCodes.GOOGLE_GET_USER_INFO);
		Temp.execute();
	}

	@Override
	public void GravityRegister() {
		// TODO Auto-generated method stub
		if(user_data.name != null && user_data.google_reg_id != null && user_data.email!=null &&
				user_data.name != "" && user_data.google_reg_id != "" && user_data.email!="")
			
			
		GravityController.register_gravity_account(mContext, user_data.email,
				user_data.google_reg_id, "",
				Common.RequestCodes.GRAVITY_REGISTER);
		else{
			
				AsyncTask<Void,Void,Void> t = new AsyncTask<Void, Void, Void>(){

					@Override
					protected Void doInBackground(Void... params) {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(1000);
							GravityRegister();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						return null;
					}};
				t.execute();
			
		}
	}

	@Override
	public void GoToMainThread() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ConfigureGCM() {
		boolean flag = false;
		if (FocusedActivity != null) {
			if (GCMController.checkPlayServices(mContext, FocusedActivity)) {
				addProgressTask(getString(R.string.gcm_play_service_success));
				// If this check succeeds, proceed with normal processing.
				// Otherwise, prompt user to get valid Play Services APK.
				gcm = GoogleCloudMessaging.getInstance(mContext);
				String tempReg = GCMController.getRegistrationId(
						getApplicationContext(), user_data);
				if (tempReg == "") {
					GCMController.registerInBackground(mContext, gcm);

				} else {
					donetasks++;
					int d = donetasks;
					int x = todotasks;
					TriggerEvent(Common.GO_TO_MAIN);
				}
				flag = true;
				// shifted to gcm_save_reg_id
				// TriggerWaitEvent(Common.LOAD_LOCAL_DB);
				// addProgressTask(getString(R.string.load_db));

			} else {

				addProgressTask(getString(R.string.gcm_invalid_device));

			}
		} else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TriggerEvent(Common.CONFIG_GCM);// check this

		}
		if (!flag)
			todotasks--;
		// TODO Auto-generated method stub

	}

	@Override
	public void storeGCMRegisterationId(String regid, int appVersion) {
		try {
			mSharedPreferencesEditor.putString(Common.GOOGLE_PROPERTY_REG_ID,
					regid);
			mSharedPreferencesEditor.putInt(Common.GOOGLE_PROPERTY_APP_VERSION,
					appVersion);
			mSharedPreferencesEditor.commit();
			user_data.google_reg_id = regid;
			user_data.google_regVer = appVersion;
			mSharedPreferencesEditor.putString(Common.GOOGLE_PROPERTY_REG_ID,
					regid);
			mSharedPreferencesEditor.putInt(Common.GOOGLE_PROPERTY_APP_VERSION,
					appVersion);

			mSharedPreferencesEditor.commit();
			addProgressTask("Saved GCM reg, app ver ");
			donetasks++;
			//TriggerEvent(Common.GRAVITY_REGISTER);
		} catch (Exception e) {
			addProgressTask(e.getLocalizedMessage());
		}
	}

	@Override
	public void SyncAppData() {
		// TODO Auto-generated method stub
		GravityController.get_tasklists(mContext, user_data,
				Common.RequestCodes.GRAVITY_GET_TASKLISTS);
//		GravityController.get_tasks(mContext, user_data,
//				Common.RequestCodes.GRAVITY_GET_TASKS);
		//donetasks++;// temp
	}

	public byte[] loadContactPhoto(long photoId) {
		ContentResolver cr = getContentResolver();
		Cursor photo = cr.query(Data.CONTENT_URI, new String[] { Photo.PHOTO },
				Data._ID + "=?", new String[] { photoId + "" }, null);

		if (photo.moveToFirst()) {
			byte[] photoBlob = photo.getBlob(photo.getColumnIndex(Photo.PHOTO));

			photo.close();
			// saveImage(photoBitmap);
			return photoBlob;

		}
		return null;

	}

	private ArrayList<UserModel> getUsers() {
		ArrayList<UserModel> users = new ArrayList<UserModel>();
		try {

			ContentResolver cr = mContext.getContentResolver();
			final String[] projection = new String[] {
					ContactsContract.Contacts._ID,
					ContactsContract.CommonDataKinds.Email.DATA, // ContactsContract.Contacts.Data.IS_PRIMARY,
					ContactsContract.Contacts.DISPLAY_NAME,
					ContactsContract.Contacts.PHOTO_ID };
			final String selection = ContactsContract.CommonDataKinds.Email.DATA
					+ " LIKE ?";
			String[] mSelectionArgs = { "%gmail.com" };
			Cursor rawContacts = cr.query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI,
					projection, selection, mSelectionArgs,
					ContactsContract.Contacts.DISPLAY_NAME + " ASC");// ,null);//ContactsLoader.SELECTION,
																		// ContactsLoader.mSelectionArgs,
																		// null);
			//s = new ArrayList<String>();

			// ImageView img = (ImageView) findViewById(R.id.logo);
			if (rawContacts.moveToFirst()) {

				do {
					UserModel user = new UserModel();
					try {

						user.email = rawContacts
								.getString(rawContacts
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
						if(user.email.toLowerCase().equals(user_data.email.toLowerCase()) ||
								user.email.toLowerCase() == user_data.email){
							
						}
						else{
							
						long photoid = rawContacts
								.getLong(rawContacts
										.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
						user.contact_id = rawContacts.getLong(rawContacts
								.getColumnIndex(ContactsContract.Contacts._ID))
								+ "";
						user.displayName = rawContacts
								.getString(rawContacts
										.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						user.image = loadContactPhoto(photoid);
						// users.add(user);
						db.User_New(user);
//
//						s.add(rawContacts.getString(rawContacts
//								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
						users.add(user);
						}
					} catch (Exception e) {
						// addProgressTask(e.getLocalizedMessage());
						// Toast.makeText(mContext,"Error: "+e.toString(),
						// Toast.LENGTH_LONG).show();
					}

				} while (rawContacts.moveToNext());
				rawContacts.close();

			}

		} catch (Exception e) {
			addProgressTask(e.getLocalizedMessage());
		}
		return users;
	}

	ArrayList<String> s;
	ArrayList<UserModel> AppUsers = new ArrayList<UserModel>();
	@SuppressLint("InlinedApi")
	@Override
	public void SyncUsers(boolean isFirstTime) {
		// db.User_Delete_All();
		if (isFirstTime) {
			// TODO Auto-generated method stub
			try {
				db.User_Delete_All();
				ArrayList<UserModel> users = getUsers();
//				for (UserModel user : users) {
//					 //db.User_New(user);
//				}
				//GravityController.validate_gravity_accounts(mContext, "faik.malik89@gmail.com", Common.RequestCodes.GRAVITY_VALIDATE_USERS);
				int limit = 50;
				for(int i=0; i<users.size();i+=limit)
				{
					int end = i+limit;
					if(end>users.size())end = users.size()-1;
					
					List<UserModel> subList = users.subList(i, end);
					String emails = "";
					for(UserModel m:subList)
					{
						if(emails == "")
							emails = m.email;
						else
						emails+=","+m.email;
					}
					if(emails!="" && hasInternet)
					{
						AppUsers = db.User_List();
						GravityController.validate_gravity_accounts(mContext, emails, Common.RequestCodes.GRAVITY_VALIDATE_USERS);
					}
					
				}
				//
				 mSharedPreferencesEditor.putBoolean(Common.ALL_USERS_SYNCED,
				 true);
				 mSharedPreferencesEditor.commit();
				 
			} catch (Exception e) {
				addProgressTask(e.getLocalizedMessage());
			}
		} else {
			ArrayList<UserModel> db_data = db.User_List();
			ArrayList<UserModel> contacts_data = getUsers();
			for (UserModel c_user : contacts_data) {
				boolean found = false;
				for (UserModel db_user : db_data) {
					try {
						String c1 = db_user.contact_id;
						String c2 = c_user.contact_id;
						if (db_user.contact_id == c_user.contact_id
								|| db_user.contact_id.equals(c_user.contact_id)) {
							found = true;
							db_user.displayName = c_user.displayName;
							db_user.image = c_user.image;
							db_user.email = c_user.email;
							db.User_Edit(db_user);
							db_data.remove(db_user);
							contacts_data.remove(c_user);
							break;
						}

					} catch (Exception e) {

					}
				}
				if (!found) {
					db.User_New(c_user);
				}
			}
		}
		addProgressTask("user sync complete");

	}

	@Override
	public void onAccountProvided(String email, int ResultCode) {
		// TODO Auto-generated method stub
		if (email != "" && ResultCode == Activity.RESULT_OK) {

			addProgressTask(getString(R.string.google_result_ok));
			mSharedPreferencesEditor.putBoolean(Common.USER_IS_SYNC_TYPE, true);
			mSharedPreferencesEditor.putString(Common.USER_EMAIL, email);
			mSharedPreferencesEditor.commit();
			user_data.email = email;
			user_data.is_sync_type = true;
			addProgressTask(getString(R.string.check_internet));
			TriggerEvent(Common.CHECK_INTERNET);
		} else {
			addProgressTask(getString(R.string.google_result_cancel));
			addProgressTask(getString(R.string.google_disable_sync));
			mSharedPreferencesEditor
					.putBoolean(Common.USER_IS_SYNC_TYPE, false);
			mSharedPreferencesEditor.putString(Common.USER_EMAIL, null);
			mSharedPreferencesEditor.commit();
			user_data.email = email;
			user_data.is_sync_type = false;
			TriggerEvent(Common.GO_TO_MAIN);
			// goto main
		}
		donetasks++;

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Common.RequestCodes.SPLASH_ACC:
			String Email = "";
			try {
				Email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);// data.getStringExtra(Common.USER_EMAIL);
			} catch (Exception e) {

			}
			onAccountProvided(Email, resultCode);

			// addProgressTask(getString(R.string.go));
			break;
		case Common.RequestCodes.SPLASH_AUTH:
			addProgressTask(getString(R.string.google_auth));
			TriggerEvent(Common.GOOGLE_AUTH);
			break;
		}
		// updateLog();

	}
	public void response_new_tasklist(TaskListModel temp)
	{
		if(db.TaskList_Edit(temp)>0)
		{
			if (FocusedActivity != null
					&& FocusedActivity.getClass() == MainActivity.class) {
				
				//navigation drawer update tasklist
				
			}
		}
	}

}
