package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class DeviceAccounts {
	ArrayList<Account> mAccounts;
	private AccountManager accountManager;
	
	public DeviceAccounts(Context mContext)
	{
		this.mAccounts = new ArrayList<Account>();
		accountManager = AccountManager.get(mContext);
		
	    for (Account i:accountManager.getAccountsByType(Common.ACCOUNT_TYPE)) {
	    	mAccounts.add(i);
	    }
	}
	public ArrayList<Account> getAccounts()
	{
		return this.mAccounts;
	}
}
