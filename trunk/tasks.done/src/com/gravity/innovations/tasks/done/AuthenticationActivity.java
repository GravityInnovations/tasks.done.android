package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
//Faik:untested 3:23 21/05/14
public class AuthenticationActivity extends Activity {
	private ArrayList<Account> mAccounts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_authentication);
		mAccounts = new DeviceAccounts(this).getAccounts();
		RadioGroup account_options = (RadioGroup) findViewById(R.id.opts_auth_accounts);
		for(Account mAccount:mAccounts)
		{
			RadioButton temp = new RadioButton(this);
			temp.setText(mAccount.name);
			account_options.addView(temp);
		}
	}
	
}

