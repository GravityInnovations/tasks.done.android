package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends Activity implements
		Common.Callbacks.SplashActivityCallback {
	private ProgressBar progress1;
	private ProgressBar progress2;
	private ProgressBar progress3;
	private TextView status;
	private TextView progress_tasks;
	private Activity mActivity;
	private ArrayList<String> tasks;
	private Authentication mAuth;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mSharedPreferencesEditor;
	// data
	private Common.userData user_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		user_data = new Common.userData();
		mActivity = this;
		tasks = new ArrayList<String>();
		// load ui
		status = (TextView) findViewById(R.id.txt_status);
		progress_tasks = (TextView) findViewById(R.id.txt_tasks);
		addProgressTask(getString(R.string.loading_comp));
		addProgressTask(getString(R.string.creating_ui));
		progress1 = (ProgressBar) findViewById(R.id.progressBar1);
		progress2 = (ProgressBar) findViewById(R.id.progressBar2);
		progress3 = (ProgressBar) findViewById(R.id.progressBar3);
		Shader textShaderTop = new LinearGradient(0, 100, 0, 90, new int[] {
				Color.parseColor("#eeeeee"), Color.parseColor("#CCCCCC"),
				Color.parseColor("#BBBBBB") }, null, TileMode.CLAMP);

		progress_tasks.getPaint().setShader(textShaderTop);

		// Thread timer = new Thread() {
		// public void run() {
		// try {
		// sleep(2000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// } finally {
		// Intent openMainActivity = new Intent(SplashActivity.this,
		// MainActivity.class);
		// startActivity(openMainActivity);
		// }
		// }
		// };
		// timer.start();
		/*
		 * new Handler().postDelayed(new Runnable() {
		 * 
		 * /* Showing splash screen with a timer. This will be useful when you
		 * want to show case your app logo / company
		 * 
		 * 
		 * @Override public void run() { // This method will be executed once
		 * the timer is over // Start your app main activity Intent i = new
		 * Intent(SplashActivity.this, AuthenticationActivity.class);
		 * startActivity(i); //startActivityForResult(i,
		 * Common.RequestCodes.SPLASH_AUTH); // close this activity finish(); }
		 * }, Common.SPLASH_TIME_OUT);
		 */

		// 1 - Check Internet

		addProgressTask(getString(R.string.check_internet));
		TriggerWaitEvent(Common.CHECK_INTERNET);
		// 2 - Load Prefs

		// 3 - Connect with google - recheck internet within

		// 4 - Load Db data
		// 5 - Every thing else

	}

	private void addProgressTask(String s) {

		status.setText(s);
		String temp = "";
		for (int i = tasks.size() - 1; i >= 0; i--) {

			temp += tasks.get(i) + "\n";
		}

		tasks.add(s);
		progress_tasks.setText(temp);
	}

	@SuppressLint("NewApi")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bundle mBundle = data.getExtras();
		switch (requestCode) {

		case Common.RequestCodes.SPLASH_AUTH:
			Google_Auth_Recieve_Result(mBundle.getString(Common.USER_EMAIL),
					mBundle.getString(Common.EXCEPTION),
					resultCode);
			/*if (mBundle.getString(Common.USER_EMAIL) != null
					&& resultCode == Activity.RESULT_OK) {
				// save to sp
				addProgressTask(mBundle.getString(Common.USER_EMAIL)
						+ getString(R.string.got_email_success_full));

			} else if (mBundle.getString(Common.USER_EMAIL) != null
					&& mBundle.getString(Common.EXCEPTION) != null) {
				addProgressTask(mBundle.getString(Common.USER_EMAIL)
						+ getString(R.string.got_email_success_half));

			}

			if (resultCode == Activity.RESULT_CANCELED) {
				progress1.setAlpha((float) 0.4);
				addProgressTask(getString(R.string.google_result_cancel));
				addProgressTask(getString(R.string.google_disable_sync));
				progress1.setProgress(80);
			} else if (resultCode == Activity.RESULT_OK) {
				progress1.setProgress(100);
				addProgressTask(getString(R.string.google_result_ok));
			}*/
			TriggerWaitEvent(Common.LOAD_LOCAL_DB);
			break;
		}
	}

	private void TriggerWaitEvent(int functionToken) {
		new Common.customPause(mActivity, functionToken,
				Common.SPLASH_TIME_OUT_SMALL);
	}

	@Override
	public void CheckInternet() {

		if (Common.hasInternet(mActivity)) {
			progress1.setProgress(40);
			addProgressTask(getString(R.string.internet_stable));

		} else {
			addProgressTask(getString(R.string.no_internet));
			addProgressTask(getString(R.string.require_internet));

			progress1.setProgress(30);
		}
		TriggerWaitEvent(Common.LOAD_PREFS);

		addProgressTask(getString(R.string.load_sp));
	}

	@Override
	public void LoadPreferences() {
		// init
		mSharedPreferences = getSharedPreferences(Common.SHARED_PREF_KEY,
				MODE_MULTI_PROCESS);
		mSharedPreferencesEditor = mSharedPreferences.edit();
		user_data.user_email = mSharedPreferences.getString(Common.USER_EMAIL,
				null);
		// sharedPreferencesEditor = sharedPreferences.edit();
		// sharedPreferencesEditor.putString(keys.USER_EMAIL, account.name);
		// sharedPreferencesEditor.commit();
		TriggerWaitEvent(Common.GOOGLE_AUTH);
		addProgressTask(getString(R.string.get_google_acc));

	}

	@Override
	public void GoogleAuth() {
		if (user_data.user_email == null) {
			Intent i = new Intent(SplashActivity.this,
					AuthenticationActivity.class);
			/*
			 * Bundle mBundle = new Bundle();
			 * mBundle.putSerializable(Common.USER_DATA, user_data);
			 * i.putExtras(mBundle);
			 */
			startActivityForResult(i, Common.RequestCodes.SPLASH_AUTH);
			// next event triggered is in onActivityResult after returning from
			// authentication activity

		}
		else
		{
			mAuth = new Authentication(mActivity);
			
			mAuth.getAuthentication(user_data.user_email);
		}
	}
	@SuppressLint("NewApi")
	public void Google_Auth_Recieve_Result(String Email,String Error, int resultCode)
	{
		 
				// sharedPreferencesEditor.putString(keys.USER_EMAIL, account.name);
				// sharedPreferencesEditor.commit();
		user_data.user_email = Email;
		if (Email != null && resultCode == Activity.RESULT_OK) {
			// save to sp
			addProgressTask(Email
					+ getString(R.string.got_email_success_full));
			mSharedPreferencesEditor.putString(Common.USER_EMAIL, Email);
			mSharedPreferencesEditor.putBoolean(Common.USER_IS_VERIFICATION_COMPLETE,
					true);

		} else if (Email != null&& Error != null) {
			addProgressTask(Email);
			mSharedPreferencesEditor.putString(Common.USER_EMAIL, Email);
			mSharedPreferencesEditor.putBoolean(Common.USER_IS_VERIFICATION_COMPLETE,
					false);
		}
		else if(Email == null && Error !=null)
		{
			mSharedPreferencesEditor.putBoolean(Common.USER_IS_SYNC_TYPE,
					false);
		}
		if (resultCode == Activity.RESULT_CANCELED) {
			progress1.setAlpha((float) 0.4);
			addProgressTask(getString(R.string.google_result_cancel));
			addProgressTask(getString(R.string.google_disable_sync));
			progress1.setProgress(80);
		} else if (resultCode == Activity.RESULT_OK) {
			progress1.setProgress(100);
			addProgressTask(getString(R.string.google_result_ok));
			mSharedPreferencesEditor.putBoolean(Common.USER_IS_SYNC_TYPE,
					true);
		}
		mSharedPreferencesEditor.commit();
	}
	@Override
	public void LoadLocalDB() {

		// TODO Auto-generated method stub

	}

	@Override
	public void pushSuccess(String AuthToken, String Email) {//by auth class
		//nothing to do with auth token
		Google_Auth_Recieve_Result(Email, null, Activity.RESULT_OK);
	}

	@Override
	public void pushFailure(String Error, String Email) {//by auth class
		// TODO Auto-generated method stub
		Google_Auth_Recieve_Result(Email, Error, Activity.RESULT_CANCELED);
	}
}
