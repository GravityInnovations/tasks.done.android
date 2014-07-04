package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
//Faik:untested 3:23 21/05/14
public class AuthenticationActivity extends Activity implements Common.Callbacks.AuthActivityCallback {
	private Authentication mAuth;
	private Button btn_auth;
	private Button btn_skip;
	private RadioGroup account_options;
	private Activity mActivity;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_authentication);
		mContext = this;
		mActivity = this;
		//load ui components
		account_options = (RadioGroup) findViewById(R.id.opts_auth_accounts);
		btn_auth = (Button) findViewById(R.id.btn_auth);
		btn_skip = (Button) findViewById(R.id.btn_auth_skip);
		btn_auth.setEnabled(false);
		//data load
		//mRecievedBundle = getIntent().getExtras();
		
		//user_data = (Common.userData)mRecievedBundle.getSerializable(Common.USER_EMAIL);
		//Actions
		mAuth = new Authentication(mContext);
		//if(user_data.user_email == null)
		for(Account mAccount:mAuth.getAccounts())
		{
			RadioButton temp = new RadioButton(this);
			temp.setText(mAccount.name);
			
			account_options.addView(temp);
		}
		account_options.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				btn_auth.setEnabled(true);
			}
		});
		btn_auth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*Bundle mBundle = new Bundle();
				mBundle.putString(Common.USER_EMAIL, "");
				  Intent i = getIntent(); //gets the intent that called this intent
				  i.putExtras(mBundle);
				  setResult(Activity.RESULT_OK, i);
				  finish();*/
				Account selectedAccount = null;
				RadioButton temp = (RadioButton) findViewById(account_options.getCheckedRadioButtonId());
				
				for(Account mAccount:mAuth.getAccounts())
				{
					if(mAccount.name == temp.getText())
						selectedAccount = mAccount;
				}
				if(selectedAccount != null)
					if(Common.hasInternet(mActivity))
					{
						mAuth.getAuthentication(selectedAccount);
					}
					else
					{
						Intent i = getIntent(); //gets the intent that called this intent
						setResult(Activity.RESULT_OK, i);
						finish();
					}
				
				
			}
		});
		btn_skip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				  Intent i = setIntent(null, null, null);
				  setResult(Activity.RESULT_CANCELED, i);
				  finish();
			}
		});
	}
	private Intent setIntent(String AuthToken, String Email, String Error)
	{
		Intent i = getIntent();
		Bundle mBundle = new Bundle();
		mBundle.putString(Common.USER_EMAIL, Email);
		mBundle.putString(Common.AUTH_TOKEN, AuthToken);
		mBundle.putString(Common.EXCEPTION, Error);
		i.putExtras(mBundle);
		return i;
	}
	@Override
	public void pushSuccess(String AuthToken, String Email) {
		// TODO Auto-generated method stub
		/*Toast toast = Toast.makeText(mContext, s, 3000);
		toast.show();*/
		 Intent i = setIntent(AuthToken, Email, null); //gets the intent that called this intent
		  setResult(Activity.RESULT_OK, i);
		  finish();
	}
	@Override
	public void pushFailure(String Error, String Email) {
		
		Intent i = setIntent(null, Email, Error);
		if(Email == null)
		setResult(Activity.RESULT_CANCELED, i);
		else
			setResult(Activity.RESULT_OK, i);
		finish();
	}
	
}

