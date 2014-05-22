package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends Activity implements
		Common.Callbacks.SplashCallback {
	private ProgressBar progress1;
	private ProgressBar progress2;
	private ProgressBar progress3;
	private TextView status;
	private TextView progress_tasks;
	private Activity mActivity;
	private ArrayList<String> tasks;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		
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
		Shader textShaderTop = new LinearGradient(0, 100, 0, 90,
                new int[]{Color.parseColor("#eeeeee"), Color.parseColor("#CCCCCC"), Color.parseColor("#BBBBBB")},
               null, TileMode.CLAMP);
		
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
	private void addProgressTask(String s)
	{
		
		status.setText(s);
		String temp  = "";
		for(int i=tasks.size()-1;i>=0;i--)
		{
			
			temp+=tasks.get(i)+"\n";
		}
		
		tasks.add(s);
		progress_tasks.setText(temp);
	}
	@SuppressLint("NewApi")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bundle mBundle = data.getExtras();
		switch (requestCode) {

		case Common.RequestCodes.SPLASH_AUTH:
			if (mBundle.getString(Common.USER_EMAIL) != null
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
			} else if (resultCode == Activity.RESULT_OK){
				progress1.setProgress(100);
				addProgressTask(getString(R.string.google_result_ok));
			}
			TriggerWaitEvent(Common.LOAD_LOCAL_DB);
			break;
		}
	}
	private void TriggerWaitEvent(int functionToken)
	{
		new Common.customPause(mActivity,
				functionToken, Common.SPLASH_TIME_OUT_SMALL);
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
		
		TriggerWaitEvent(Common.GOOGLE_AUTH);
		addProgressTask(getString(R.string.get_google_acc));
		
	}

	@Override
	public void GoogleAuth() {
		
		Intent i = new Intent(SplashActivity.this, AuthenticationActivity.class);

		startActivityForResult(i, Common.RequestCodes.SPLASH_AUTH);
		//next event triggered is in onActivityResult after returning from authentication activity
	}

	@Override
	public void LoadLocalDB() {
		
		// TODO Auto-generated method stub

	}
}
