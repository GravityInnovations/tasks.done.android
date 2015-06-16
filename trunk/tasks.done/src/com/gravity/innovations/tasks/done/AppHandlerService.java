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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
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
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Contacts.Photo;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.view.View;
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
	ArrayList<Common.AlertData> pendingAlerts = new ArrayList<Common.AlertData>();
	private tdHandler mHandler;
	//private ArrayList<>
	public AppHandlerService() {
		super();
		// setIntentRedelivery(true);
		// mNotificationManager = (NotificationManager)
		// this.getSystemService(Context.NOTIFICATION_SERVICE);
		// TODO Auto-generated constructor stub
	}
	public void openAdDialog(final long interval)
	{
		final Runnable r = new Runnable() {
			private InterstitialAd mAdView;
			@Override
			public void run() {
				mAdView =  new InterstitialAd(FocusedActivity);//(AdView) view.findViewById(R.id.LargeadView);
				mAdView.setAdUnitId("ca-app-pub-1363931415415684/7076105056");
				mAdView.setAdListener(new AdListener() {

					@Override
					public void onAdClosed() {
						// TODO Auto-generated method stub
						super.onAdClosed();
						openAdDialog(1000*60);
					}

					@Override
					public void onAdFailedToLoad(int errorCode) {
						// TODO Auto-generated method stub
						super.onAdFailedToLoad(errorCode);
						switch(errorCode)
						{
						case AdRequest.ERROR_CODE_INTERNAL_ERROR:
							break;
						case AdRequest.ERROR_CODE_INVALID_REQUEST:
							break;
						case AdRequest.ERROR_CODE_NETWORK_ERROR:
							break;
						case AdRequest.ERROR_CODE_NO_FILL:
							break;
						}
						openAdDialog(2000);
					}

					@Override
					public void onAdLeftApplication() {
						// TODO Auto-generated method stub
						super.onAdLeftApplication();
					}

					@Override
					public void onAdLoaded() {
						// TODO Auto-generated method stub
						super.onAdLoaded();
						mAdView.show();
					}

					@Override
					public void onAdOpened() {
						// TODO Auto-generated method stub
						super.onAdOpened();
					}
					 
				});
				AdRequest adRequest = new AdRequest.Builder().build();
				 mAdView.loadAd(adRequest);
				 
			}
		};
		AsyncTask<Void,Void,Void> t = new AsyncTask<Void, Void, Void>(){

			

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					if(FocusedActivity!=null)
					{
						Thread.sleep(interval);
						runOnUiThread(r);
					}
					else{
						Thread.sleep(3000);
						openAdDialog(4000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}};
		
		t.execute();
		
		
		
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
			LoadLocalDB();
			nH = new NotificationHandler(this, db.tasks.GetsPendingTasks().size());
			
			// InitEvents();
			
			sendNotification("task.done", "background service is running", 2);
			//
//			 LoadLocalDB() ;
//			SyncUsers(true);
			//TriggerEvent(Common.USERS_SYNC);
			HandlerThread thread = new HandlerThread("Thread name", android.os.Process.THREAD_PRIORITY_DEFAULT);//.THREAD_PRIORITY_BACKGROUND);
		    thread.start();
		    Looper looper = thread.getLooper();
		    mHandler = new tdHandler(looper);
			TriggerEvent(Common.LOAD_PREFS);
			//openAdDialog(4000);
			 
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


	//boolean commandLock = false;
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
					
					if(AppStateClassName == MainActivity.class.getName()
							){
						AppStateIntent = new Intent(FocusedActivity,
								MainActivity.class);
						startActivity(AppStateIntent);
						//AppStateClassName = MainActivity.class.getName();
//						FocusedActivity.runOnUiThread(new Runnable() {
//
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								AppStateIntent.putExtra("user", user_data);
//								FocusedActivity.
//								FocusedActivity.finish();
//							}
//						});
					}
					
					if(FocusedActivity!=null && FocusedActivity.getClass() == MainActivity.class)
					;
					else
					{
						//TriggerEvent(Common.GO_TO_MAIN);
						
					}
					
					//showNextAlert();
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
	private String getDeviceIMEI() {
		//requires READ_PHONE_STATE permission in manifest.xml
		try{
			TelephonyManager telephonyManager = (TelephonyManager) this
					.getSystemService(Context.TELEPHONY_SERVICE);
			String id = telephonyManager.getDeviceId();
			return id;
		}
		catch(Exception e)
		{
			addProgressTask(e.getMessage());
			return null;
		}
		
	}
	private String getDeviceTitle()
	{
		try{
		 String manufacturer = Build.MANUFACTURER;
		   String model = Build.MODEL;
		   if (model.startsWith(manufacturer)) {
		      return model;
		   } else {
		      return manufacturer + " " + model;
		   }
		}
		catch(Exception e)
		{
			return "default";
		}
	}
	private String getDeviceMac(){
		try{

			Boolean flag = false;
			//requires ACCESS_WIFI_STATE permission in manifest.xml
			WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
			if(!wifiManager.isWifiEnabled())
			{
				wifiManager.setWifiEnabled(true);
				flag = true;
			}
			WifiInfo wInfo = wifiManager.getConnectionInfo();
			String mac = wInfo.getMacAddress();
			if(flag)
				wifiManager.setWifiEnabled(false);
			return mac;	
		}
		catch(Exception e)
		{
			addProgressTask(e.getMessage());
			return null;
			
		}
	}
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

	public class tdHandler extends Handler {
		  public tdHandler(Looper looper) {
		    super(looper);
		  }

		  @Override
		  public void handleMessage(Message msg) {
		    super.handleMessage(msg);
		    int eventId = msg.arg1;
		   
		    try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch (eventId) {
			case 999999: //save bitmap
				String url = (String) msg.obj;
				getAndSaveBitmap(url);
				break;
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
					AppStateClassName = MainActivity.class.getName();
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
		    
		    // Do some processing
		    //boolean stopped = stopSelfResult(startId);
		    // stopped is true if the service is stopped
		  }
		}
	public void TriggerEvent(final int eventId) {
//		Message msg = mHandler.obtainMessage();
//	    msg.arg1 = eventId;
//	    todotasks++;
//	    mHandler.sendMessage(msg);
//	    
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
					Thread.sleep(1000);
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
						AppStateClassName = MainActivity.class.getName();
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
	public void addContractContact(UserModel temp)
	{
		try{
			 ArrayList<ContentProviderOperation> ops =
			          new ArrayList<ContentProviderOperation>();
			
			 int rawContactInsertIndex = ops.size();
			 ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
			          .withValue(RawContacts.ACCOUNT_TYPE, "com.google")
			          .withValue(RawContacts.ACCOUNT_NAME, temp.name)
			          .build());

			 ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
			          .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
			          .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
			          .withValue(StructuredName.DISPLAY_NAME, temp.name)
			          .build());
			 ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
					    .withValue(ContactsContract.Data.MIMETYPE,
					            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
					    .withValue(ContactsContract.CommonDataKinds.Email.DATA, temp.email)
					    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, "com.google")
					    .build());
			 getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		}
		catch(Exception e)
		{
			String s = "";
			s+=0;
		}
	}
	private JSONObject registeration_response;
	public void httpResult(JSONObject data, int RequestCode, int ResultCode) {
		switch (RequestCode) {
		case Common.RequestCodes.GRAVITY_REGISTER:

			if (ResultCode == Common.HTTP_RESPONSE_OK) {
				registeration_response = data;
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
						db.tasklists.Add(model);//.TaskList_New(model);
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
//				Message msg = mHandler.obtainMessage();
//			    msg.arg1 = 999999;
//			    msg.obj = data.getString("picture");
			    //todotasks++;
			   // mHandler.sendMessage(msg);
				// next step in getAndSaveBitmap function
				mSharedPreferencesEditor.commit();
				addProgressTask("User information fetch complete");
				donetasks++;

			} catch (JSONException e) {

				// TODO Auto-generated catch block
				addProgressTask("Unable to fetch your information from Google");
				donetasks++;
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
								db.users.ServerValidate(user);//.User_validate(user);
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
					Common.CustomDialog.set((Context) FocusedActivity,
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
		user_data.mac = mSharedPreferences.getString(Common.DEVICE_MAC, "");
		if(user_data.mac == "" || user_data.mac.equals("")){
			user_data.mac =  getDeviceMac();
			mSharedPreferencesEditor.putString(Common.DEVICE_MAC,user_data.mac);
			//user_data.mac = temp;
		}
			
		user_data.imei = mSharedPreferences.getString(Common.DEVICE_IMEI, "");
		if(user_data.imei == "" || user_data.imei.equals("")){
			user_data.imei=getDeviceIMEI();
			mSharedPreferencesEditor.putString(Common.DEVICE_IMEI,user_data.imei );
			
		}
		user_data.device_title = mSharedPreferences.getString(Common.DEVICE_TITLE, "");
		if(user_data.device_title == "" || user_data.device_title.equals("")){
			user_data.device_title =getDeviceTitle();
			mSharedPreferencesEditor.putString(Common.DEVICE_TITLE, user_data.device_title);
			
		}
		
		mSharedPreferencesEditor.commit();
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
		user_data._id = mSharedPreferences.getInt(
				Common.USER_LOCAL_ID, -1);
		
		
		// user_data.image = BitmapFactory.decodeFile(
		// mSharedPreferences.getString(Common.USER_IMAGE, ""));
		try {
			user_data.image = Bitmap.createScaledBitmap(BitmapFactory
					.decodeFile(mSharedPreferences.getString(Common.USER_IMAGE,
							"")), 75, 75, true);
		} catch (Exception ex) {
			//addProgressTask(ex.getLocalizedMessage());

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
				user_data.name != "" && user_data.google_reg_id != "" && user_data.email!="" 
				&& user_data.mac !="" && user_data.mac!=null&& user_data.device_title !="" && user_data.device_title!=null)
			
			
		GravityController.register_gravity_account(mContext, user_data,
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
//					try {
//						Thread.sleep(1000);
//						//todotasks--;
//						//TriggerEvent(Common.GRAVITY_REGISTER);
//						GravityRegister();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
			
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
		//GravityController.get_tasklists(mContext, user_data,
			//	Common.RequestCodes.GRAVITY_GET_TASKLISTS);
//		GravityController.get_tasks(mContext, user_data,
//				Common.RequestCodes.GRAVITY_GET_TASKS);
		//donetasks++;// temp
		
		
		//registeration_response;
		JSONArray arr_data = registeration_response.optJSONObject("data").optJSONArray("TaskLists");
		//DatabaseHelper db = new DatabaseHelper(this);
		for (int i = 0; i < arr_data.length(); i++) {
			try {
//				 "TaskListId": "b0ec6f16-30f8-482d-82c1-6a983d395e8d",
//	                "Color": "#343434",
//	                "Icon": 1,
//	                "Title": "Welcome to tasks.done",
//	                "DateCreated": "2015-06-16T10:25:26.5",
//	                "DateUpdated": "2015-06-16T10:25:26.5",
//	                "OwnerId": "1b701633-69b8-4960-98ef-4c79dabeb7d1",
				JSONObject temp = arr_data.getJSONObject(i);
				TaskListModel model = new TaskListModel(
						temp.getString("Title"));
				
				model.syncStatus = "Synced";
				model.server_id = temp.optString("TaskListId");
				model.DateUpdated = Common.toDeviceTime(temp.optString("DateUpdated"));//temp.optString("DateUpdated");
				model.DateCreated = Common.toDeviceTime(temp.optString("DateCreated"));
				model.fragmentColor = temp.optString("Color");
				model.icon_identifier = temp.optInt("Icon");
				model.title = temp.optString("Title");
				//model.owner_id =  temp.optString("Title");
				JSONArray arr_users = temp.optJSONArray("Users");
				String owner_id = temp.optString("OwnerId");
				if(owner_id != null &&
						user_data.gravity_user_id.equals(owner_id))
				{
					model.owner_id = user_data._id;
				}
				for(int j = 0; j< arr_users.length(); j++)
				{
					JSONObject arr_user = arr_users.getJSONObject(j);
					UserModel m = db.users.Get(arr_user.optString("UserId"));
					
					if(m==null){
						m = new UserModel(arr_user);
						m._id = db.users.Add(m);
					}
					if(arr_user.optString("UserId").equals(owner_id) && m._id !=-1)
					{
						model.owner_id = m._id;
					}
					
					
					
				}
				int id = db.tasklists.Add(model);//.TaskList_New(model);
//				{
//                    "Notifications": [],
//                    "TaskId": "c7c49467-b6e8-4476-8e10-0aaef9d2528e",
//                    "Title": "Add New List",
//                    "Details": "1- Swipe from left side of screen\n2 - Click on down button on the top right side of the pane\n3 - Click on \"New Catagory\"4 - Follow The Steps",
//                    "Notes": "task.done predefined tutorial",
//                    "DateUpdated": "2015-06-16T10:25:26.513",
//                    "Completed": false,
//                    "isAllDay": true,
//                    "DateCreated": "2015-06-16T10:25:26.513",
//                    "StartDate": "2015-06-16T10:25:26.513",
//                    "EndDate": "2015-06-16T10:25:26.513",
//                    "Rep_Interval": 1,
//                    "Rep_Type": 1,
//                    "Rep_Expiration": "2015-06-16T10:25:26.513",
//                    "Rep_Value": ""
//                },
				JSONArray tasks = temp.optJSONArray("Tasks");
				for (int j = 0; j < tasks.length(); j++) {
					TaskModel taskModel = new TaskModel();
					JSONObject taskObj = tasks.getJSONObject(j);
					taskModel.title = taskObj.optString("Title");
					taskModel.details = taskObj.optString("Details");
					taskModel.notes = taskObj.optString("Notes");
					if(taskObj.optBoolean("Completed"))
					taskModel.completed = 1;//.optString("Title");
					else taskModel.completed = 0;
					taskModel.server_id =  taskObj.optString("TaskId");//
					if(taskObj.optBoolean("isAllDay"))
						taskModel.allDay = 1;//.optString("Title");
						else taskModel.allDay = 0;
					taskModel.DateCreated = Common.toDeviceTime(taskObj.optString("DateCreated"));
					taskModel.DateUpdated = Common.toDeviceTime(taskObj.optString("DateUpdated"));
					taskModel.startDateTime = Common.toDeviceTime(taskObj.optString("StartDate"));
					taskModel.endDateTime = Common.toDeviceTime(taskObj.optString("EndDate"));
					taskModel.rep_interval = taskObj.optInt("Rep_Interval");
					taskModel.rep_intervalType = taskObj.optInt("Rep_Type");
					taskModel.rep_intervalExpiration = taskObj.optString("Rep_Expiration");//>>>>>>>>>>>>>>>>>>>>cross check for date
					taskModel.rep_value = taskObj.optString("Rep_Value");
					taskModel.syncStatus = "Synced";
					
					taskModel.fk_tasklist_id = id;
					
					int task_id = db.tasks.Add(taskModel);
					JSONArray notifications = taskObj.optJSONArray("Notifications");
					for (int k = 0; k < notifications.length();k++) {
						TaskNotificationsModel mTaskNotificationsModel = new TaskNotificationsModel();
						JSONObject notifObj = notifications.getJSONObject(k);
						mTaskNotificationsModel.fk_task_id = task_id;
						mTaskNotificationsModel.interval = notifObj.optInt("Interval");
						mTaskNotificationsModel.interval_type = notifObj.optInt("Type");
						mTaskNotificationsModel.interval_expiration = notifObj.optString("Expiration");//>>>>>>>>>>>>>>>>>>>>cross check for date or null
						mTaskNotificationsModel.send_as = notifObj.optInt("SendAs");
						mTaskNotificationsModel.server_id = notifObj.optString("Id");
						db.notification.Add(mTaskNotificationsModel, task_id);
						//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ADD SYSTEM ALARAMS HERE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
						
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		addProgressTask(getString(R.string.gravity_fetch_data_success));
	
	// TriggerEvent(Common.USERS_SYNC);
	//donetasks++;

		
		
		
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

	private ArrayList<UserModel> getUserContacts(boolean add) {
		ArrayList<UserModel> users = new ArrayList<UserModel>();
		try {
			if(user_data._id == -1){
				
				user_data._id = db.users.Add(new UserModel("Me",user_data.email));//.User_New(new UserModel("Me",user_data.email));
				mSharedPreferencesEditor.putInt(Common.USER_LOCAL_ID, user_data._id);
				mSharedPreferencesEditor.commit();
				}
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
						if(add)
						db.users.Add(user);//.User_New(user);
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
	public boolean commandLock = false;
	@SuppressLint("InlinedApi")
	@Override
	public void SyncUsers(boolean isFirstTime) {
		// db.User_Delete_All();
		ArrayList<UserModel> users = new ArrayList<UserModel>();
		if (isFirstTime) {
			// TODO Auto-generated method stub
			try {
				db.users.DeleteAll();//.User_Delete_All();
				users = getUserContacts(true);
//				for (UserModel user : users) {
//					 //db.User_New(user);
//				}
				//GravityController.validate_gravity_accounts(mContext, "faik.malik89@gmail.com", Common.RequestCodes.GRAVITY_VALIDATE_USERS);
				
				 
			} catch (Exception e) {
				addProgressTask(e.getLocalizedMessage());
			}
		} else {
			try {
				Thread.sleep(0);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ArrayList<UserModel> db_data = db.users.Get();//.User_List();
			ArrayList<UserModel> contacts_data = getUserContacts(false);
			for(int i = 0;i<contacts_data.size();i++)
			{
				UserModel c_user = contacts_data.get(i);
			//for (UserModel c_user : contacts_data) {
				boolean found = false;
				for (UserModel db_user : db_data) {
					try {
						String c1 = db_user.contact_id;
						String c2 = c_user.contact_id;
						if(db_user.displayName!= null && db_user.displayName.equals("Me")){
							db_data.remove(db_user);
							found = true;break;
						}
						if(c1==null || c2 == null);
						else if (db_user.contact_id == c_user.contact_id
								|| db_user.contact_id.equals(c_user.contact_id)) {
							found = true;
							db_user.displayName = c_user.displayName;
							db_user.image = c_user.image;
							db_user.email = c_user.email;
							db.users.Edit(db_user);//.User_Edit(db_user);
							db_data.remove(db_user);
							contacts_data.remove(c_user);
							break;
						}

					} catch (Exception e) {
						addProgressTask(e.getMessage());
					}
					
				}
				
				try{
					
					if (!found) {
						users.add(c_user);
						db.users.Add(c_user);//.User_New(c_user);
					}
				}
				catch(Exception e)
				{
					addProgressTask(e.getMessage());
				}
				
			}
			
		}
		users = db.users.Get();
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
				AppUsers = db.users.Get();//.User_List();
				GravityController.validate_gravity_accounts(mContext, emails, Common.RequestCodes.GRAVITY_VALIDATE_USERS);
			}
			
		}
		//
		 mSharedPreferencesEditor.putBoolean(Common.ALL_USERS_SYNCED,
		 true);
		 mSharedPreferencesEditor.commit();
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
	public Bitmap getUserImage(UserModel user) {
		Bitmap bmp =null;
			if (user.image != null){
				
				Bitmap b = BitmapFactory.decodeByteArray(user.image, 0,
						user.image.length);
				b =  ImageGridAdapter.getRoundedCornerBitmap(b,user.image_alpha);
				
				bmp = b;
			
			}
			else{
				Bitmap b = BitmapFactory.decodeResource(getResources(),
						R.drawable.ic_account_circle_grey600_24dp);
				b =  ImageGridAdapter.getRoundedCornerBitmap(b,user.image_alpha);
				bmp = b;
			}
		
		return bmp;
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
	public void runOnUiThread(Runnable runnable)
	{
		if (FocusedActivity != null)
			FocusedActivity.runOnUiThread(runnable);
	}
	public void response_new_tasklist(TaskListModel temp)
	{
		
		if(db.tasklists.Edit(temp)>0)
		{
			if (FocusedActivity != null
					&& FocusedActivity.getClass() == MainActivity.class) {
				
						// TODO Auto-generated method stub
						((MainActivity)FocusedActivity).mNavigationDrawerFragment.editTaskListInAdapter(temp);
						
					}
				
				
				//navigation drawer update tasklist
				
			
		}
	}
	class dialogClickListener implements DialogInterface.OnClickListener{
		TaskListModel s_tasklist;
		UserModel s_owner;
		JSONArray s_users;
		public dialogClickListener(TaskListModel s_tasklist,
		UserModel s_owner,
		JSONArray s_users)
		{
			this.s_owner = s_owner; this.s_tasklist=s_tasklist; this.s_users = s_users;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			response_share_add_tasklist(s_tasklist,s_owner, s_users, true);
			showNextAlert();
		}
		
	}
	public void showNextAlert()
	{
		if(pendingAlerts.size()>0){
			if(FocusedActivity!=null && FocusedActivity.getClass() == MainActivity.class){
			
				Common.CustomDialog.textDialog(FocusedActivity,pendingAlerts.get(0)).create().show();
				pendingAlerts.remove(0);
			}
			else
			{
				try {
					Thread.sleep(2000);
					showNextAlert();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	public void response_share_add_tasklist(TaskListModel tasklist,UserModel owner,JSONArray users, boolean self)
	{
		if(!self)
		{
			
		
			//if(FocusedActivity!=null){
			Common.AlertData ad = new Common.AlertData(tasklist.icon_identifier, "Share Request - "+tasklist.title,
					owner.name+" would like to share above catagory with you"
					
					, "Accept", "Decline",new dialogClickListener(tasklist, owner,users), new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							showNextAlert();
						}
					});
			pendingAlerts.add(ad);
			if(FocusedActivity!=null && FocusedActivity.getClass() == MainActivity.class)
			{
			//Common.CustomDialog.TextDialog(this.getApplicationContext(),ad).create().show();
			showNextAlert();
				
			}
			else{
				
				Intent i =  new Intent(this, AppHandlerService.class);
				
				i.setAction(Common.serviceActions.START_APP);
				nH.showAlertNotification((pendingAlerts.size()-1)*50, owner.name+" has shared "+tasklist.title+" with you", i, getUserImage(owner));
			}
			//}
			
		}
		else
		{
			
			ArrayList<UserModel> tempusers = new ArrayList<UserModel>();
			String userids = "";
			boolean flagNewTaskList = false;
			boolean flagNewUsers = false;
			tasklist._id = db.tasklists.Get(tasklist.server_id)._id;
			if(tasklist._id == -1)
			{
				tasklist._id = db.tasklists.Add(tasklist);
				flagNewTaskList = true;
			}
			if(tasklist._id != -1)
			{
				for(int i=0;i<users.length();i++)
				{
					try {
						JSONObject o = users.getJSONObject(i);
						
						UserModel u = new UserModel();
						u = db.users.Get(o.optString("UserId"));
						if(u==null)
						{
							u = new UserModel();
							String name = o.optString("Name");
							u.name = name;
							u.displayName = name;
							u.email = o.optString("Email");
							u.server_id = o.optString("UserId");
							if(u.email.equals(user_data.email))
								u= null;
							else
							u._id = db.users.Add(u);
							
							if(u!=null && u._id!=-1){
							tempusers.add(u);
							db.users.Share(tasklist, u, "Synced");
							userids += o.optString("UserId") + ",";
							}
							
							flagNewUsers = true;
							
						}
						else{
							db.users.Share_ServerValidate(tasklist._id, u._id);
							userids += o.optString("UserId") + ",";
						}
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				//
				
			}
			
			if (FocusedActivity != null
					&& FocusedActivity.getClass() == MainActivity.class) {
				
						// TODO Auto-generated method stub
						if(!flagNewTaskList && !flagNewUsers)
						((MainActivity)FocusedActivity).mNavigationDrawerFragment.addUserShareInAdapter(tasklist.server_id, userids, false);
						else if(flagNewTaskList){
							((MainActivity)FocusedActivity).mNavigationDrawerFragment.addTaskList(tasklist);
							((MainActivity)FocusedActivity).mNavigationDrawerFragment.addUserShareInAdapter(tasklist.server_id, userids, false);
						}
						else if(flagNewUsers){
							//((MainActivity)FocusedActivity).mNavigationDrawerFragment.addUserToTaskList(tasklist, tempusers);
							((MainActivity)FocusedActivity).mNavigationDrawerFragment.addUserShareInAdapter(tasklist.server_id, userids, true);
						}
						else if(flagNewUsers && flagNewTaskList){
							((MainActivity)FocusedActivity).mNavigationDrawerFragment.addTaskList(tasklist);
							//((MainActivity)FocusedActivity).mNavigationDrawerFragment.addUserToTaskList(tasklist, tempusers);
							((MainActivity)FocusedActivity).mNavigationDrawerFragment.addUserShareInAdapter(tasklist.server_id, userids, true);
						}
					}
				
				
				//navigation drawer update tasklist
				
			
		}
	}
	public void response_share_remove_tasklist(String tasklistid, String userids) {
		// TODO Auto-generated method stub
		if (FocusedActivity != null
				&& FocusedActivity.getClass() == MainActivity.class) {
			
					// TODO Auto-generated method stub
					((MainActivity)FocusedActivity).mNavigationDrawerFragment.removeUserShareInAdapter(tasklistid, userids);
					for(String UserId:userids.split(","))
					{
						if(UserId!="")
					db.users.Share_Remove_ServerValidate(tasklistid, UserId);//.UserList_Delete_Validate(tasklistid, UserId);
					}
				}
			
			
			//navigation drawer update tasklist
			
		
	}
	public void push_referee()
	{	//if(hasInternet()){}
	}

}
