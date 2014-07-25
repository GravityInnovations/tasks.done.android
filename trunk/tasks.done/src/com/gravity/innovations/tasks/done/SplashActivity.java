package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity implements
		Common.Callbacks.SplashActivityCallback {
	private ProgressBar progress1;
	private ProgressBar progress2;
	private ProgressBar progress3;
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
	// data
	private Common.userData user_data;
	private void updateLog()
	{
		user_data.email = mSharedPreferences.getString(Common.USER_EMAIL, null);
		user_data.is_sync_type = mSharedPreferences.getBoolean(
				Common.USER_IS_SYNC_TYPE, true);
		
		user_data.gravity_is_registered = mSharedPreferences.getBoolean(
				Common.USER_IS_REGISTERED, false);
		user_data.google_reg_id = 
				mSharedPreferences.getString(Common.GOOGLE_PROPERTY_REG_ID, "");
		user_data.google_regVer = 
				mSharedPreferences.getInt(Common.GOOGLE_PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		user_data.google_AuthToken = 
				mSharedPreferences.getString(Common.AUTH_TOKEN, null);
	   String txt = "Name: "+user_data.name;
	   txt += "\nEmail: "+user_data.email;
	   txt += "\nIs_Sync_type: "+user_data.is_sync_type;
	   txt += "\nIs Registered: "+user_data.gravity_is_registered;
	   txt += "\nReg id: "+user_data.google_reg_id;
	   txt += "\nReg Ver: "+user_data.google_regVer;
	   txt += "\nGoogle Auth token: "+user_data.google_AuthToken;
	   log.setText(txt);
	   
	   
	}
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		//Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Email.CONTENT_URI); /*AccountManager.newChooseAccountIntent(null, null,
		    /*    new String[] { "com.google" }, true, null, null,
		        null, null);
				*/
				//new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
		//i.setType(Phone.CONTENT_TYPE);
		//startActivityForResult(i, 989);
		user_data = new Common.userData();
		mActivity = this;
		mContext = this;
		tasks = new ArrayList<String>();
		// load ui
		status = (TextView) findViewById(R.id.txt_status);
		progress_tasks = (TextView) findViewById(R.id.txt_tasks);
		log = (TextView) findViewById(R.id.log);
		
		addProgressTask(getString(R.string.loading_comp));
		addProgressTask(getString(R.string.creating_ui));
		progress1 = (ProgressBar) findViewById(R.id.progressBar1);
		progress2 = (ProgressBar) findViewById(R.id.progressBar2);
		progress3 = (ProgressBar) findViewById(R.id.progressBar3);
		Shader textShaderTop = new LinearGradient(0, 100, 0, 90, new int[] {
				Color.parseColor("#eeeeee"), Color.parseColor("#CCCCCC"),
				Color.parseColor("#BBBBBB") }, null, TileMode.CLAMP);

		progress_tasks.getPaint().setShader(textShaderTop);

		TriggerWaitEvent(Common.LOAD_PREFS);

		addProgressTask(getString(R.string.load_sp));

	}

	private void addProgressTask(String s) {
		try{
		status.setText(s);
		String temp = "";
		while(tasks.size()>5)
		{
			
			tasks.remove(0);
		}
		for (int i = tasks.size() - 1; i >= 0; i--) {

			temp += tasks.get(i) + "\n";
		}

		tasks.add(s);
		progress_tasks.setText(temp);
		}
		catch(Exception ex)
		{
			//addProgressTask("Error: "+ex.getLocalizedMessage());
		}
	}

	@SuppressLint("NewApi")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode)
		{
			case Common.RequestCodes.SPLASH_ACC:
				String Email = data.getStringExtra(Common.USER_EMAIL);
				if(Email!=null && resultCode == Activity.RESULT_OK)
				{	
//					mAuth = new GoogleAuth(mContext, null);
//					mAuth.SetAccount(Email);
					addProgressTask( getString(R.string.google_result_ok));
					mSharedPreferencesEditor.putBoolean(Common.USER_IS_SYNC_TYPE, true);
					mSharedPreferencesEditor.putString(Common.USER_EMAIL, Email);
					mSharedPreferencesEditor.commit();
					user_data.email = Email;
					user_data.is_sync_type = true;
					//mAuth.execute();
					TriggerWaitEvent(Common.GOOGLE_AUTH);
				}	
				else if(resultCode == Activity.RESULT_CANCELED)
				{
					addProgressTask( getString(R.string.google_result_cancel));
					addProgressTask( getString(R.string.google_disable_sync));
					mSharedPreferencesEditor.putBoolean(Common.USER_IS_SYNC_TYPE, false);
					mSharedPreferencesEditor.putString(Common.USER_EMAIL, null);
					mSharedPreferencesEditor.commit();
					user_data.email = Email;
					user_data.is_sync_type = false;
					TriggerWaitEvent(Common.GO_TO_MAIN);
					addProgressTask(getString(R.string.complete));
				}
				

				//addProgressTask(getString(R.string.go));
				break;
			case Common.RequestCodes.SPLASH_AUTH:
				TriggerWaitEvent(Common.GOOGLE_AUTH);
				break;
		}
		updateLog();
		
		
	}
	private WaitEventHandler wh = new WaitEventHandler();
	private void TriggerWaitEvent(int functionToken) {
		wh.AddRunnable(mActivity, functionToken,Common.SPLASH_TIME_OUT_SMALL);
//		new Common.customPause(mActivity, functionToken,
//				Common.SPLASH_TIME_OUT_SMALL);
	}
	@Override
	public void CheckInternet() {

		if (Common.hasInternet(mActivity)) {
			progress1.setProgress(40);
			addProgressTask(getString(R.string.internet_stable));
			TriggerWaitEvent(Common.CONFIG_GCM);
			addProgressTask(getString(R.string.checking_other_settings));
			addProgressTask(getString(R.string.config_gcm));

		} else {
			addProgressTask(getString(R.string.no_internet));
			addProgressTask(getString(R.string.require_internet));
			TriggerWaitEvent(Common.GO_TO_MAIN);
			addProgressTask(getString(R.string.complete));
			progress1.setProgress(30);
		}
	
		updateLog();
	}

	

	@Override
	public void httpResult(Object data, int RequestCode, int ResultCode) {
		switch (RequestCode) {
		case Common.RequestCodes.GRAVITY_REGISTER:
			
			if(ResultCode == Common.HTTP_RESPONSE_OK)
			{
				addProgressTask(getString(R.string.gravity_registration_complete));
			mSharedPreferencesEditor
					.putBoolean(Common.USER_IS_REGISTERED, true);
			}
			else
			{	addProgressTask(getString(R.string.gravity_registration_error));
				mSharedPreferencesEditor
				.putBoolean(Common.USER_IS_REGISTERED, false);
			}
			mSharedPreferencesEditor.commit();
			TriggerWaitEvent(Common.GO_TO_MAIN);
			addProgressTask(getString(R.string.complete));
			//TriggerWaitEvent(Common.CONFIG_GCM);
			//addProgressTask(getString(R.string.checking_other_settings));
			break;
		case Common.RequestCodes.GRAVITY_SEND_GCM_CODE:
			Toast.makeText(this, "Result from gravity for gcm code send request "+ResultCode, Toast.LENGTH_LONG).show();
			break;
			
		}
	}

	@Override
	public void displayMsg(String msg) {
		addProgressTask(msg);
	}

	@Override
	public void storeGCMRegisterationId(String regid, int appVersion) {
		//addProgressTask("*Saving GCM reg");
		mSharedPreferencesEditor.putString(Common.GOOGLE_PROPERTY_REG_ID,regid);
		mSharedPreferencesEditor.putInt(Common.GOOGLE_PROPERTY_APP_VERSION,appVersion);
		mSharedPreferencesEditor.commit();
		user_data.google_reg_id = regid;
		user_data.google_regVer = appVersion;
		mSharedPreferencesEditor.putString(Common.GOOGLE_PROPERTY_REG_ID, regid);
		mSharedPreferencesEditor.putInt(Common.GOOGLE_PROPERTY_APP_VERSION, appVersion);
		
		mSharedPreferencesEditor.commit();
		//addProgressTask("Saved GCM reg, app ver: ");
		
		//error
		if(user_data.email == null)
			{
				TriggerWaitEvent(Common.GET_ACCOUNT);
				//addProgressTask(getString(R.string.get_google_acc));
			}
			else
				TriggerWaitEvent(Common.GOOGLE_AUTH);
		
		
		//updateLog();
	}

	@Override
	public void LoadPreferences() {
		// TODO Auto-generated method stub
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
		TriggerWaitEvent(Common.LOAD_LOCAL_DB);
		addProgressTask(getString(R.string.load_db));
		updateLog();
	}
	@Override
	public void GetAccount()
	{
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
			updateLog();
	}

	@Override
	public void LoadLocalDB() {
		// TODO Auto-generated method stub
		DatabaseHelper h = new DatabaseHelper(this);
		
		
		/*if (!user_data.is_registered){
			TriggerWaitEvent(Common.GRAVITY_REGISTER);
		addProgressTask(getString(R.string.gravity_register));
			//AccountsController.get_gravity_accounts(Common.RequestCodes.GRAVITY_REGISTER);
		}*/
		//else {
		if(!user_data.is_sync_type)
		{
			TriggerWaitEvent(Common.GO_TO_MAIN);
			addProgressTask(getString(R.string.complete));
		}
	else{
		addProgressTask(getString(R.string.check_internet));
		TriggerWaitEvent(Common.CHECK_INTERNET);
	}
		
	}

	@Override
	public void GravityRegister() {
		GravityController.register_gravity_account(mActivity,
				user_data.email, user_data.google_reg_id, "", Common.RequestCodes.GRAVITY_REGISTER);
		//AccountsController.get_gravity_accounts(Common.RequestCodes.GRAVITY_REGISTER);
	
	}

	@Override
	public void GoToMain() {
		Intent i = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(i);
		finish();
	}

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
            else
            {
            	if(user_data.email == null)
    			{
    				TriggerWaitEvent(Common.GET_ACCOUNT);
    				//addProgressTask(getString(R.string.get_google_acc));
    			}
    			else
    				TriggerWaitEvent(Common.GOOGLE_AUTH);
            }
			//shifted to gcm_save_reg_id
			//TriggerWaitEvent(Common.LOAD_LOCAL_DB);
			//addProgressTask(getString(R.string.load_db));
			
		}
		else
		{
			
			addProgressTask(getString(R.string.gcm_invalid_device));
		}
		updateLog();
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
//			TriggerWaitEvent(Common.CONFIG_GCM);
//			addProgressTask(getString(R.string.checking_other_settings));
//			addProgressTask(getString(R.string.config_gcm));
			if(!user_data.gravity_is_registered)
			{
				addProgressTask(getString(R.string.gravity_register));
				
				TriggerWaitEvent(Common.GRAVITY_REGISTER);
				
			}
			else
			{
				TriggerWaitEvent(Common.GO_TO_MAIN);
				addProgressTask(getString(R.string.complete));
			}
			
			//runWorker
		}
		//updateLog();
	}

	
	//gcm functions
	
	
	
	
}
