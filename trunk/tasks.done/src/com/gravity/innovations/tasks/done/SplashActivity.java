package com.gravity.innovations.tasks.done;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.internal.s;
import com.gravity.innovations.tasks.done.Common.Callbacks.SplashServiceCallback;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity implements
		Common.Callbacks.SplashActivityCallback{
	private TextView status;
	private TextView log;
	private TextView progress_tasks;
	private Activity mActivity;
	private Context mContext;
	private GoogleCloudMessaging gcm;
	private ArrayList<String> tasks;
	private GoogleAuth mAuth;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mSharedPreferencesEditor;
	//private Boolean killAllThreads;// = false;
	// data
	private Common.userData user_data;
	//private int eventItrator = 0;
	//actions
	private ArrayList<Thread> splash_actions;
	private boolean killAllThreads;
	AppHandlerService mService;
	AppHandlerService.AppHandlerBinder mBinder;
	private AppHandlerService service;
	private ProgressBar progress_bar;
	private boolean progress_flag;
	private int progress = 0;
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		
		super.onStop();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		AsyncTask<Void,Void,Void> t = new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
				
					((TheApp) getApplicationContext()).startService((Common.Callbacks.ServiceCallback)mContext);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		};
		t.execute();

//		try {
//			
//			while(progress_flag){
//			Thread.sleep(200);
//			progress_bar.setProgress(progress);
//			progress++;
//			
//			if(progress>=100)
//				progress = 0;
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onStart();
		//service.onActivityOpen(null,null);
		
	}

	@Override
	public void onServiceBound(AppHandlerService handleService) {
		// TODO Auto-generated method stub
		try{
			
//			Intent i = getIntent();
//			Bundle b = i.getBundleExtra("AppServiceExtra");
//			
//			mBinder= (AppHandlerService.AppHandlerBinder)b.getBinder("binder");
//			
			
			service =handleService;//((TheApp) getApplicationContext()).getService();// mBinder.getService();
			service.onActivityOpen(this, this);
			service.addProgressTask("User Started Application");
			//Toast.makeText(getApplicationContext(),x.flattenToString(), 
				//	Toast.LENGTH_LONG).show();
			

			
			
			}
			catch(Exception ex)
			{
				Toast.makeText(getApplicationContext(),"Error: "+ex.toString(), 
						Toast.LENGTH_LONG).show();
			}
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	//getActionBar().hide();
		setContentView(R.layout.activity_splash);
		 if (Build.VERSION.SDK_INT < 16) {
	            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        }
		 else{
			 View decorView = getWindow().getDecorView();
			// Hide the status bar.
			int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
		            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
		            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
			decorView.setSystemUiVisibility(uiOptions);
		 }
		status = (TextView) findViewById(R.id.txt_status);
		progress_tasks = (TextView) findViewById(R.id.txt_tasks);
		progress_bar = (ProgressBar)findViewById(R.id.progressBar);
		mContext = this;
		mActivity = this;
		progress_flag = true;
		try {
			showProgress();
		    // do something that raises an exception
			//runOnUiThread();
			
		} catch (Exception ex)  {
	
		}
		
		
		//t2.run();
//		try{
//			
//			Boolean flag = false;
//			ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//		    for (runningserviceinfo service : manager.getrunningservices(integer.max_value)) 
//		            {
//		    	
//		                if ("com.gravity.innovations.tasks.done.apphandlerservice"
//		                        .equals(service.service.getclassname())) 
//		                {
//		                	flag = true;
//		                	break;
//		                	//service is running
//		                }
//		                
//		            }
//		        
//			if(!flag)
//			{
//				Intent i = new Intent(this, AppHandlerService.class);
//    			i.setAction(".AppHandlerService");//Common.serviceActions.START_APP);
//    			
//    			ComponentName x = startService(i);
//    			
//    			if(x.toString() !="" &&  x.toString()!= null)
//    			{
//    				flag = true;
//    				//service started
//    			}
//    			else
//    			{
//    				//service not started
//    			}
//			}
//			if(flag)
//			{
//				
//			}
//			
//				
//			//Toast.makeText(getApplicationContext(),x.flattenToString(), 
//				//	Toast.LENGTH_LONG).show();
//			
//
//			
//			
//			}
//			catch(Exception ex)
//			{
//				Toast.makeText(getApplicationContext(),"Error: "+ex.toString(), 
//						Toast.LENGTH_LONG).show();
//			}
//		
//		
		
		
		
		
//		splash_actions = new ArrayList<Thread>();
//		user_data = new Common.userData();
//		mActivity = this;
//		killAllThreads = false;
//		mContext = this;
//		tasks = new ArrayList<String>();
//		// load ui
		
//		log = (TextView) findViewById(R.id.log);
//		
//		addProgressTask(getString(R.string.loading_comp));
//		addProgressTask(getString(R.string.creating_ui));
//		Shader textShaderTop = new LinearGradient(0, 100, 0, 90, new int[] {
//				Color.parseColor("#eeeeee"), Color.parseColor("#CCCCCC"),
//				Color.parseColor("#BBBBBB") }, null, TileMode.CLAMP);
//
//		progress_tasks.getPaint().setShader(textShaderTop);
//		InitEvents();
//		//TriggerWaitEvent(Common.LOAD_PREFS);
//		TriggerEvent(Common.LOAD_LOCAL_DB);
//		TriggerEvent(Common.LOAD_PREFS);
//		GoToMainThread();
//		updateLogThread();
		
		
		//addProgressTask(getString(R.string.load_sp));
	}
	private int a = 0;
	private int avoidTasks = 0;
	private int doneTasks = 0;
	private boolean AnyThreadExecuting()
	{
		if(splash_actions.size()-avoidTasks-1 <= doneTasks)
			return false;
		else return true;
//		a=0;
//		boolean flag = false;
//		for(Thread t:splash_actions)
//			if(t.isAlive()){
//				flag = true;
//				a++;
//				//return true;
//				
//			}
////		if(runningThreads>0)
////			return true;
//		return flag;
	}
	private void showProgress()
	{
		 final Handler mHandler = new Handler();
		 new Thread(new Runnable() {
             public void run() {
            	 try {
 					
 					while(progress_flag){
 					Thread.sleep(50);
 					 mHandler.post(new Runnable() {
 						
 			             public void run() {
 			            	
 			            	progress_bar.setProgress(progress);
 			            	
 		 					}
 		 					
 					 }
 			         );
 					progress++;
	 					
	 					if(progress>=100)
	 						progress = 0;
 					//publishProgress(progress);
 					
 				}} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
             }
         }).start();
	}
	public void InitEvents()
	{
//		splash_actions.add(new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				
//			}
//		}));
		splash_actions.add(new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				addProgressTask(getString(R.string.load_db));
				LoadLocalDB();
				doneTasks++;
				//updateLog();
//				
			}
		},"t_"+Common.LOAD_LOCAL_DB));
		splash_actions.add(new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				addProgressTask(getString(R.string.load_sp));
				//TriggerNextEvent();
				LoadPreferences();
				//updateLog();
				if(user_data.is_sync_type)
				{
					addProgressTask(getString(R.string.check_internet));
					TriggerEvent(Common.CHECK_INTERNET);
					
					//TriggerWaitEvent(Common.CHECK_INTERNET);
				}
				else avoidTasks = splash_actions.size()-doneTasks;
				doneTasks++;
				
			}
		},"t_"+Common.LOAD_PREFS));
		
		splash_actions.add(new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				CheckInternet();
				doneTasks++;
				
			}
		},"t_"+Common.CHECK_INTERNET));
		splash_actions.add(new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				ConfigureGCM();
				//doneTasks++;
				//
			}
		},"t_"+Common.CONFIG_GCM));
		splash_actions.add(new Thread(new Runnable() {
			
			@Override
			public void run() {
				//if(user_data.is_sync_type)
				if(user_data.email == null)
    			{
					GetAccount();
    				//TriggerWaitEvent(Common.GET_ACCOUNT);
    				//addProgressTask(getString(R.string.get_google_acc));
    			}
    			else{
    				if(!user_data.gravity_is_registered)
    					{
    						TriggerEvent(Common.GRAVITY_REGISTER);
    					}
    				else avoidTasks++;
    					TriggerEvent(Common.GOOGLE_AUTH);
    				//avoidTasks++;
    				doneTasks++;
    				//TriggerWaitEvent(Common.GOOGLE_AUTH);
    			}
				//
			}
		},"t_"+Common.GET_ACCOUNT));
		
		splash_actions.add(new Thread(new Runnable() {
					
			@Override
			public void run() {
				
					if(!user_data.all_users_synced)
						{
						addProgressTask(getString(R.string.users_sync));
						
						SyncUsers(true);
						}
						else {avoidTasks++; doneTasks++;}
						//addProgressTask(getString(R.string.users_sync));
						//
			}
		},"t_"+Common.USERS_SYNC));//
		splash_actions.add(new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				GoogleAuth();
				//
			}
		},"t_"+Common.GOOGLE_AUTH));
		splash_actions.add(new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(!user_data.gravity_is_registered)
				{
					addProgressTask(getString(R.string.gravity_register));
				
					GravityRegister();
				}
				else
					doneTasks++;
				//
			}
		},"t_"+Common.GRAVITY_REGISTER));
		splash_actions.add(new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(user_data.gravity_is_registered)
				SyncAppData();
				else doneTasks++;
				//
			}
		},"t_"+Common.GRAVITY_SYNC));
		splash_actions.add(new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				GetUserDataFromGoogle();
			}
		},"t_"+Common.GOOGLE_USER_INFO));
	}
	int x[] = {Common.LOAD_LOCAL_DB, Common.LOAD_PREFS,
			Common.CHECK_INTERNET, Common.CONFIG_GCM, Common.GET_ACCOUNT,
			Common.USERS_SYNC, Common.GOOGLE_AUTH, Common.GRAVITY_REGISTER,
			Common.GRAVITY_SYNC, Common.GOOGLE_USER_INFO};
	public void TriggerEvent(final int eventId)
	{
		Boolean flag = false;
		//if(eventId<x)
		for(int a:x)
			if(a==eventId)
				flag = true;
		if(flag)
		new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						try {
							
							Thread.sleep(2000);//Common.SPLASH_TIME_OUT);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//wait(Common.SPLASH_TIME_OUT_SMALL);
						for(Thread t:splash_actions)
						{
							
							if(t.getName().equals("t_"+eventId))
							{
								t.start();
								break;
							}
						}
						
					}
				}).start();
		//splash_actions.get(eventItrator++).start();
		
	}
	
	
	@Override
	public void AuthResult(Intent i) {
		if(i.getBooleanExtra(Common.HAS_EXCEPTION, false))
			//&& i.getIntExtra(Common.EXCEPTION_TYPE,Common.EXCEPTIONS.NoException) != Common.EXCEPTIONS.NoException)
		{
			if(i.getIntExtra(Common.EXCEPTION_TYPE,Common.EXCEPTIONS.NoException) != Common.EXCEPTIONS.NoException)
			{
				startActivityForResult(i, Common.RequestCodes.SPLASH_AUTH);
			}
			
		}

		else if(i.getStringExtra(Common.AUTH_TOKEN)!=null)
		{
			mSharedPreferencesEditor.putString(Common.AUTH_TOKEN,i.getStringExtra(Common.AUTH_TOKEN));
			user_data.google_AuthToken = i.getStringExtra(Common.AUTH_TOKEN);
			mSharedPreferencesEditor.commit();
			doneTasks++;
//			TriggerWaitEvent(Common.CONFIG_GCM);
//			addProgressTask(getString(R.string.checking_other_settings));
//			addProgressTask(getString(R.string.config_gcm));
			
			
			
			//runWorker
		}
	}

	@Override
	public void httpResult(JSONObject data, int RequestCode, int ResultCode) {
		switch (RequestCode) {
		case Common.RequestCodes.GRAVITY_REGISTER:
			
			if(ResultCode == Common.HTTP_RESPONSE_OK)
			{
				data = data.optJSONObject("data");
				user_data.gravity_user_id =data.optString("UserId"); 
				addProgressTask(getString(R.string.gravity_registration_complete));
				mSharedPreferencesEditor
					.putBoolean(Common.USER_IS_REGISTERED, true);
				mSharedPreferencesEditor
				.putString(Common.USER_ID_GRAVITY, user_data.gravity_user_id);
				addProgressTask(getString(R.string.gravity_sync));
				TriggerEvent(Common.GRAVITY_SYNC);
			}
			else
			{	addProgressTask(getString(R.string.gravity_registration_error));
				mSharedPreferencesEditor
				.putBoolean(Common.USER_IS_REGISTERED, false);
				doneTasks++;//for sync
			}
			mSharedPreferencesEditor.commit();
			doneTasks++;
			//TriggerWaitEvent(Common.GRAVITY_SYNC);
			
			//TriggerWaitEvent(Common.CONFIG_GCM);
			//addProgressTask(getString(R.string.checking_other_settings));
			
			break;
		case Common.RequestCodes.GRAVITY_SEND_GCM_CODE:
			Toast.makeText(this, "Result from gravity for gcm code send request "+ResultCode, Toast.LENGTH_LONG).show();
			break;
		case Common.RequestCodes.GRAVITY_GET_TASKLISTS:
			if(ResultCode == Common.HTTP_RESPONSE_OK)
			{
				JSONArray arr_data = data.optJSONArray("data");
				DatabaseHelper db = new DatabaseHelper(this);
				for(int i=0;i<arr_data.length();i++)
				{
					try {
						JSONObject temp = arr_data.getJSONObject(i);
						TaskListModel model = new TaskListModel(temp.getString("Title"));
						db.tasklists.Add(model);//.TaskList_New(model);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				addProgressTask(getString(R.string.gravity_fetch_data_success));
			}
			else
			{
				addProgressTask(getString(R.string.gravity_fetch_data_failed));
			}
			//TriggerEvent(Common.USERS_SYNC);
			addProgressTask(getString(R.string.users_sync));
			doneTasks++;
			break;
		case Common.RequestCodes.GOOGLE_GET_USER_INFO:
			//save user info
			try {
				user_data.name = data.getString("name");
				mSharedPreferencesEditor
				.putString(Common.USER_NAME, user_data.name);
				
				getAndSaveBitmap(data.getString("picture"));
				//next step in getAndSaveBitmap function
				mSharedPreferencesEditor.commit();
				addProgressTask("User information fetch complete");
				
			} catch (JSONException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		}
	}
	public void getAndSaveBitmap(final String url)
    {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Bitmap bitmapImage = null;
		        try 
		        {
		            InputStream in = new java.net.URL(url).openStream();
		            bitmapImage = BitmapFactory.decodeStream(in);
		            ContextWrapper cw = new ContextWrapper(getApplicationContext());
		            File directory = cw.getDir("taskdone", Context.MODE_PRIVATE);
		            if(!directory.exists())
			            directory.mkdir();
		            File mypath=new File(directory,"profile.jpg");
		            if (mypath.exists())
		            	mypath.delete();
		            FileOutputStream fos = null;
		            try {
		               // fos = openFileOutput(filename, Context.MODE_PRIVATE);

		                fos = new FileOutputStream(mypath);

		                // Use the compress method on the BitMap object to write image to the OutputStream
		                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		                fos.flush();
		                fos.close();
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		            //user_data.image = bitmapImage;
		            user_data.image = Bitmap.createScaledBitmap(bitmapImage,75, 75, true);
		            mSharedPreferencesEditor
					.putString(Common.USER_IMAGE, mypath.getAbsolutePath());
		            mSharedPreferencesEditor
					.putBoolean(Common.GOOGLE_IS_USER_SYNCED,true);
		            mSharedPreferencesEditor.commit();
		          //reg to gravity
//					if(!user_data.gravity_is_registered)
//					{
//						addProgressTask(getString(R.string.gravity_register));
//						
//						TriggerWaitEvent(Common.GRAVITY_REGISTER);
//						
//					}
//					else
//					{
//						TriggerWaitEvent(Common.GO_TO_MAIN);
//						addProgressTask(getString(R.string.complete));
//					}
		            //return bitmapImage;
		        }//End of try block...... 
		        catch (Exception e) 
		        {
		            //status = e.getMessage();
		            //return null;
		        }
			}
		}).start();
    }
	
	@Override
	public void displayMsg(String msg) {
		addProgressTask(msg);
	}

	@Override
	public void storeGCMRegisterationId(String regid, int appVersion) {
		// TODO Auto-generated method stub
		mSharedPreferencesEditor.putString(Common.GOOGLE_PROPERTY_REG_ID,regid);
		mSharedPreferencesEditor.putInt(Common.GOOGLE_PROPERTY_APP_VERSION,appVersion);
		mSharedPreferencesEditor.commit();
		user_data.google_reg_id = regid;
		user_data.google_regVer = appVersion;
		mSharedPreferencesEditor.putString(Common.GOOGLE_PROPERTY_REG_ID, regid);
		mSharedPreferencesEditor.putInt(Common.GOOGLE_PROPERTY_APP_VERSION, appVersion);
		
		mSharedPreferencesEditor.commit();
		addProgressTask("Saved GCM reg, app ver ");
		doneTasks++;
		//error
//		if(user_data.email == null)
//			{
//				TriggerEvent(Common.GET_ACCOUNT);
//				addProgressTask(getString(R.string.get_google_acc));
//			}
//			else
//				TriggerEvent(Common.GOOGLE_AUTH);
		
	}

	@Override
	public void CheckInternet() {
		
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
		user_data.google_reg_id = 
				mSharedPreferences.getString(Common.GOOGLE_PROPERTY_REG_ID, "");
		user_data.google_regVer = 
				mSharedPreferences.getInt(Common.GOOGLE_PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		user_data.google_reg_id = 
				mSharedPreferences.getString(Common.GOOGLE_PROPERTY_REG_ID, "");
		user_data.name = mSharedPreferences.getString(Common.USER_NAME, "");
		user_data.all_users_synced = mSharedPreferences.getBoolean(Common.ALL_USERS_SYNCED, false);
		
		user_data.google_is_data_synced = mSharedPreferences.getBoolean(Common.GOOGLE_IS_USER_SYNCED, false);
		//user_data.image = BitmapFactory.decodeFile( mSharedPreferences.getString(Common.USER_IMAGE, ""));
		try{
		user_data.image = Bitmap.createScaledBitmap(BitmapFactory.decodeFile( mSharedPreferences.getString(Common.USER_IMAGE, "")),75, 75, true);
		}
		catch(Exception ex)
		{
			//addProgressTask(ex.getLocalizedMessage());
		}
		//		ImageView v = (ImageView)findViewById(R.id.logo);
//		v.setImageBitmap(user_data.image);
		//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		
		//TriggerWaitEvent(Common.LOAD_LOCAL_DB);
		//addProgressTask(getString(R.string.load_sp));
		//updateLog();
	}

	@Override
	public void GetAccount() {
		Intent i = new Intent(SplashActivity.this,
				AuthenticationActivity.class);
		
		startActivityForResult(i, Common.RequestCodes.SPLASH_ACC);
	}

	@Override
	public void GoogleAuth() {
		addProgressTask(getString(R.string.google_auth));
		mAuth = new GoogleAuth(mContext, null);
		mAuth.SetAccount(user_data.email);
		mAuth.execute();
	}

	@Override
	public void GetUserDataFromGoogle() {
		// TODO Auto-generated method stub
		HttpTask Temp =  new HttpTask(mActivity, 
				Common.USER_INFO_URL+user_data.google_AuthToken, null,
				Common.HttpMethod.HttpGet,
				Common.RequestCodes.GOOGLE_GET_USER_INFO);
		Temp.execute();
	}

	@Override
	public void LoadLocalDB() {
		DatabaseHelper h = new DatabaseHelper(this);
		
		
		/*if (!user_data.is_registered){
			TriggerWaitEvent(Common.GRAVITY_REGISTER);
		addProgressTask(getString(R.string.gravity_register));
			//AccountsController.get_gravity_accounts(Common.RequestCodes.GRAVITY_REGISTER);
		}*/
		//else {
//		if(!user_data.is_sync_type)
//		{
//			TriggerWaitEvent(Common.GO_TO_MAIN);
//			addProgressTask(getString(R.string.complete));
//		}
//		else{
//			addProgressTask(getString(R.string.check_internet));
//			TriggerWaitEvent(Common.CHECK_INTERNET);
//		}
	}

	@Override
	public void GravityRegister() {
		GravityController.register_gravity_account(mActivity,
				user_data, Common.RequestCodes.GRAVITY_REGISTER);
		//AccountsController.get_gravity_accounts(Common.RequestCodes.GRAVITY_REGISTER);
	
	}
	@Override
	public void GoToMainThread() {
		
//		
		new Thread(new Runnable() {
			@Override
			public void run() {
				//int CheckCount = 0;
				try{
					while(true)
					{
						Thread.sleep(2000);
						if(!AnyThreadExecuting())
//						CheckCount=0;
//						else 
//							CheckCount++;// = 0;
//						
//						if(CheckCount>=5 && !isActivityOnPause )//&& runningThreads<=0)
						{
							Intent i = new Intent(SplashActivity.this, MainActivity.class);

							i.putExtra("user", user_data);
							addProgressTask(">>>main started<<<");
							startActivity(i);
							finish();	
							break;
						}
					}
				}
				catch(Exception ex)
				{
					addProgressTask(ex.getMessage());
				}
			}
		}).start();
					
		
//		Intent i = new Intent(SplashActivity.this, MainActivity.class);
//		i.putExtra("user", user_data);
//		startActivity(i);
//		finish();
	}

	@Override
	public void ConfigureGCM() {
		if (GCMController.checkPlayServices(mContext, mActivity)) {
			addProgressTask(getString(R.string.gcm_play_service_success));
	        // If this check succeeds, proceed with normal processing.
	        // Otherwise, prompt user to get valid Play Services APK.
			gcm = GoogleCloudMessaging.getInstance(mContext);
			String tempReg =GCMController.getRegistrationId(getApplicationContext(), user_data); 
            if (tempReg=="") {
                GCMController.registerInBackground(mContext,gcm);
            }
			//shifted to gcm_save_reg_id
			//TriggerWaitEvent(Common.LOAD_LOCAL_DB);
			//addProgressTask(getString(R.string.load_db));
			
		}
		else
		{
			
			addProgressTask(getString(R.string.gcm_invalid_device));
		}
	}

	@Override
	public void SyncAppData() {
		
		GravityController.get_tasklists(mActivity, user_data,
				Common.RequestCodes.GRAVITY_GET_TASKLISTS);
	}

	@SuppressLint("NewApi")
	@Override
	public void SyncUsers(boolean isFirstTime) {
		try{
			ArrayList<HashMap<String, Object>> contacts = getContacts();
			DatabaseHelper db = new DatabaseHelper(mContext);
			int size = contacts.size();
			for (int i = 0; i < size; i++) {

				HashMap<String, Object> contactItem = contacts.get(i);

				ContentValues dataToInsert = new ContentValues();

				UserModel userModel = new UserModel();
				// userModel.contact_id.("contactId").toString();
				// userModel.contactItem.get("name").toString();
				// userModel.contactItem.get("email").toString();
				// userModel.contactItem.get("address").toString();
				// userModel.contactItem.get("phone").toString();

				if (contactItem.get("email") != null) {

					if (contactItem.get("photo") != null
							&& contactItem.get("photo") instanceof Bitmap) {

						Bitmap b = (Bitmap) contactItem.get("photo");
						int bytes = b.getByteCount();

						ByteBuffer buffer = ByteBuffer.allocate(bytes); // Create a
																		// new
																		// buffer
						b.copyPixelsToBuffer(buffer); // Move the byte data to the
														// buffer
						byte[] array = buffer.array();


						if (contactItem.get("email").toString()
								.contains("gmail.com")) { // this is gmail thing
							UserModel user = new UserModel(contactItem.get("name")
									.toString(), contactItem.get("email")
									.toString(), array
							// contactItem.get("phone").toString()
							);
							db.users.Add(user);//.User_New(user);
						}

					} else {

					}

					
				}
			}
			addProgressTask(getString(R.string.users_sync_complete));
			mSharedPreferencesEditor.putBoolean(Common.ALL_USERS_SYNCED, true);
			mSharedPreferencesEditor.commit();
		}
		catch(Exception ex)
		{
			addProgressTask(getString(R.string.users_sync_error));
			addProgressTask(ex.getLocalizedMessage());
		}
		doneTasks++;
				//TriggerWaitEvent(Common.GO_TO_MAIN);
		//addProgressTask(getString(R.string.complete));
		
	}
	public ArrayList<HashMap<String, Object>> getContacts() {

		ArrayList<HashMap<String, Object>> contacts = new ArrayList<HashMap<String, Object>>();
		final String[] projection = new String[] { RawContacts.CONTACT_ID,
				RawContacts.DELETED };

		// @SuppressWarnings("deprecation")
		ContentResolver cr = getContentResolver();
		Cursor rawContacts = cr.query(RawContacts.CONTENT_URI, projection,
				null, null, null);

		final int contactIdColumnIndex = rawContacts
				.getColumnIndex(RawContacts.CONTACT_ID);
		final int deletedColumnIndex = rawContacts
				.getColumnIndex(RawContacts.DELETED);

		if (rawContacts.moveToFirst()) {
			while (!rawContacts.isAfterLast()) {
				final int contactId = rawContacts.getInt(contactIdColumnIndex);
				final boolean deleted = (rawContacts.getInt(deletedColumnIndex) == 1);
				if (!deleted) {
					HashMap<String, Object> contactInfo = new HashMap<String, Object>() {
						{
							put("contactId", "");
							put("name", "");
							put("email", "");
							put("address", "");
							put("photo", "");
							put("phone", "");
						}
					};
					contactInfo.put("contactId", "" + contactId);
					contactInfo.put("name", getName(contactId));
					contactInfo.put("email", getEmail(contactId));
					contactInfo.put("photo",
							getPhoto(contactId) != null ? getPhoto(contactId)
									: "");
					contactInfo.put("address", getAddress(contactId));
					contactInfo.put("phone", getPhoneNumber(contactId));
					contactInfo.put("isChecked", "false");
					contacts.add(contactInfo);
				}
				rawContacts.moveToNext();
			}
		}
		rawContacts.close();
		return contacts;

	}
	private String getName(int contactId) {
		String name = "";
		final String[] projection = new String[] { Contacts.DISPLAY_NAME };

		ContentResolver cr = getContentResolver();
		Cursor contact = cr.query(Contacts.CONTENT_URI, projection,
				Contacts._ID + "=?",
				new String[] { String.valueOf(contactId) }, null);

		if (contact.moveToFirst()) {
			name = contact.getString(contact
					.getColumnIndex(Contacts.DISPLAY_NAME));
			contact.close();
		}
		contact.close();
		return name;

	}

	private String getEmail(int contactId) {
		String emailStr = "";
		final String[] projection = new String[] { Email.DATA, // use
				// Email.ADDRESS
				// for API-Level
				// 11+
				Email.TYPE };
		ContentResolver cr = getContentResolver();
		Cursor email = cr.query(Email.CONTENT_URI, projection, Data.CONTACT_ID
				+ "=?", new String[] { String.valueOf(contactId) }, null);

		if (email.moveToFirst()) {
			final int contactEmailColumnIndex = email
					.getColumnIndex(Email.DATA);

			while (!email.isAfterLast()) {
				emailStr = emailStr + email.getString(contactEmailColumnIndex)
						+ ";";
				email.moveToNext();
			}
		}
		email.close();
		return emailStr;

	}

	private Bitmap getPhoto(int contactId) {
		Bitmap photo = null;
		final String[] projection = new String[] { Contacts.PHOTO_ID };
		ContentResolver cr = getContentResolver();
		Cursor contact = cr.query(Contacts.CONTENT_URI, projection,
				Contacts._ID + "=?",
				new String[] { String.valueOf(contactId) }, null);

		if (contact.moveToFirst()) {
			final String photoId = contact.getString(contact
					.getColumnIndex(Contacts.PHOTO_ID));
			if (photoId != null) {
				photo = getBitmap(photoId);
			} else {
				photo = null;
			}
		}
		contact.close();

		return photo;
	}

	private Bitmap getBitmap(String photoId) {
		ContentResolver cr = getContentResolver();
		Cursor photo = cr.query(Data.CONTENT_URI, new String[] { Photo.PHOTO },
				Data._ID + "=?", new String[] { photoId }, null);

		final Bitmap photoBitmap;
		if (photo.moveToFirst()) {
			byte[] photoBlob = photo.getBlob(photo.getColumnIndex(Photo.PHOTO));
			photoBitmap = BitmapFactory.decodeByteArray(photoBlob, 0,
					photoBlob.length);
		} else {
			photoBitmap = null;
		}

		photo.close();
		//saveImage(photoBitmap);
		return photoBitmap;
	}
	private String getAddress(int contactId) {
		String postalData = "";
		String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND "
				+ ContactsContract.Data.MIMETYPE + " = ?";
		String[] addrWhereParams = new String[] {
				String.valueOf(contactId),
				ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };
		ContentResolver cr = getContentResolver();
		Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI, null,
				addrWhere, addrWhereParams, null);

		if (addrCur.moveToFirst()) {
			postalData = addrCur
					.getString(addrCur
							.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
		}
		addrCur.close();
		return postalData;
	}

	private String getPhoneNumber(int contactId) {

		String phoneNumber = "";
		final String[] projection = new String[] { Phone.NUMBER, Phone.TYPE, };
		ContentResolver cr = getContentResolver();
		Cursor phone = cr.query(Phone.CONTENT_URI, projection, Data.CONTACT_ID
				+ "=?", new String[] { String.valueOf(contactId) }, null);

		if (phone.moveToFirst()) {
			final int contactNumberColumnIndex = phone
					.getColumnIndex(Phone.DATA);

			while (!phone.isAfterLast()) {
				phoneNumber = phoneNumber
						+ phone.getString(contactNumberColumnIndex) + ";";
				phone.moveToNext();
			}

		}
		phone.close();
		return phoneNumber;
	}

	private void addProgressTask(final String current){//remove
		
	}
	public void addProgressTask(final String current, final String last) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					
					status.setText(current);
					progress_tasks.setText(last);
//					String temp = "";
//					while(tasks.size()>5)
//					{
//						
//						tasks.remove(0);
//					}
//					for (int i = tasks.size() - 1; i >= 0; i--) {
//
//						temp += tasks.get(i) + "\n";
//					}

					//tasks.add(s);
					//progress_tasks.setText(temp);
					}
					catch(Exception ex)
					{
						addProgressTask("Error: "+ex.getLocalizedMessage());
					}
			}
		});
		
			
		
	}
	private void updateLogThread()
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!killAllThreads){
					  try {
						Thread.sleep(1000);
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {	
								
//								user_data.email = mSharedPreferences.getString(Common.USER_EMAIL, null);
//							user_data.is_sync_type = mSharedPreferences.getBoolean(
//									Common.USER_IS_SYNC_TYPE, true);
//							
//							user_data.gravity_is_registered = mSharedPreferences.getBoolean(
//									Common.USER_IS_REGISTERED, false);
//							user_data.gravity_user_id = mSharedPreferences.getString(
//									Common.USER_ID_GRAVITY, null);
//							user_data.google_reg_id = 
//									mSharedPreferences.getString(Common.GOOGLE_PROPERTY_REG_ID, "");
//							user_data.google_regVer = 
//									mSharedPreferences.getInt(Common.GOOGLE_PROPERTY_APP_VERSION, Integer.MIN_VALUE);
//							user_data.google_AuthToken = 
//									mSharedPreferences.getString(Common.AUTH_TOKEN, null);
						 
								String txt = "Name: "+user_data.name;
							   txt += "\nEmail: "+user_data.email;
							   txt += "\nIs_Sync_type: "+user_data.is_sync_type;
							   txt += "\nIs Registered: "+user_data.gravity_is_registered;
							   if(user_data.gravity_user_id != "" && user_data.gravity_user_id != null)
							   txt += "\nReg id gravity: taken";
							   else txt += "\nReg id gravity: ";
							   if(user_data.google_reg_id != "" && user_data.google_reg_id != null)
								   txt += "\nReg id: taken";
								   else  txt += "\nReg id: "+user_data.google_reg_id;
							   
						   
							   txt += "\nReg Ver: "+user_data.google_regVer;
							   if(user_data.google_AuthToken != "" && user_data.google_AuthToken != null)
								   txt += "\nGoogle Auth Token: taken";
								   else  txt += "\nReg id: "+user_data.google_reg_id;
								if(user_data.image != null)
									txt += "\nUser Image: taken";
								else txt += "\nUser Image: none";
								txt+="\nRunning Threads: "+a;
							   log.setText(txt);
							}
							
						});
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						addProgressTask(e.getLocalizedMessage());
						//e.printStackTrace();
					}
					  
				}
			}
		}).start();
		
	   
	   
	}
	@SuppressLint("NewApi")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		service.onActivityResult(requestCode, resultCode, data);
//		switch(requestCode)
//		{
//			case Common.RequestCodes.SPLASH_ACC:
//				String Email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);//data.getStringExtra(Common.USER_EMAIL);
//				service.onAccountProvided(Email);
//				
//				//addProgressTask(getString(R.string.go));
//				break;
//			case Common.RequestCodes.SPLASH_AUTH:
//				addProgressTask(getString(R.string.google_auth));
//				TriggerEvent(Common.GOOGLE_AUTH);
//				break;
//		}
		//updateLog();
		
		
	}
	
	@Override
	public void startResultActivity(final Intent intent, final int RequestCode) {
		// TODO Auto-generated method stub
		final Activity a = this;
		runOnUiThread(new Runnable() {
			int choice = 0;
			@Override
			public void run() {
				try{
				    startActivityForResult(intent, RequestCode);
				}
				catch(Exception e)
				{
					String s = e.getLocalizedMessage();
//					Intent i = new Intent(a.getApplicationContext(),MainActivity.class);
//					try{
//						startActivity(intent);
//					}
//					catch(Exception ex)
//					{
//						String d = ex.getLocalizedMessage();
//						
//					}
					
				}
//				OnClickListener optListener = new OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						choice = which;
//						
//					}
//				};
//				View view = mActivity.getLayoutInflater().inflate(R.layout.activity_authentication,null);
//				mAuth = new GoogleAuth(mContext, null);
//				//if(user_data.user_email == null)
//				List<String> options = new ArrayList<String>();//List<String>();
//				final ArrayAdapter<String> a = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice);
//				for(Account mAccount:mAuth.getAccounts())
//				{
//					a.add(mAccount.name);
//					options.add(mAccount.name);
//				}
//				
//				//Object[] opts =options.toArray();//{"a","b","c"};
//				
//				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//			    builder.setTitle("Login")
//			    .setSingleChoiceItems(a, 0,optListener)
//			    .setPositiveButton(R.string.proceed, new OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						
//						((SplashServiceCallback)mContext).onAccountProvided(a.getItem(choice));
//					}
//				}).setNegativeButton(R.string.useoffline, new OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						((SplashServiceCallback)mContext).onAccountProvided("");
//					}
//				});
//			    
//			    builder.create().show();
//				Common.CustomDialog.CustomDialog(mContext, view, negListener , posListener, R.string.proceed, R.string.useoffline, "Login");
			}
		});
		
		
	}

	
	
}
