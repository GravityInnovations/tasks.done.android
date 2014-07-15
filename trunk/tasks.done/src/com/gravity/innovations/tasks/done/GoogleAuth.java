package com.gravity.innovations.tasks.done;

import java.io.IOException;
import java.util.ArrayList;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.gravity.innovations.tasks.done.Common.Callbacks.GoogleAuthCallback;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class GoogleAuth extends AsyncTask<Void, Void, Void> {
	private Context mContext;
	private Account mAccount;
	ArrayList<Account> mAccounts;
	private AccountManager accountManager;
	
	public GoogleAuth(Context mContext, Account mAccount)
	{
		this.mAccount = mAccount;
		this.mContext = mContext;
		accountManager = AccountManager.get(mContext);
		this.mAccounts = new ArrayList<Account>();
		
		for (Account i:accountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE)) {
	    	mAccounts.add(i);
	    }
	}
	public void SetAccount( Account mAccount)
	{
		this.mAccount = mAccount;
		
	}
	public void SetAccount(String Email)
	{
		this.mAccount = getAccount(Email);
		
	}
	public ArrayList<Account> getAccounts()
	{
		return this.mAccounts;
	}
	public Account getAccount(String Email)
	{
		for (Account i:this.mAccounts) {
	    	if(i.name.equals(Email))
	    		return i;
	    }
		return null;
	}
	@Override
	protected Void doInBackground(Void... params) {
		//String className = mContext.getClass().getSimpleName().toString();
		Intent i= new Intent();// = new Intent();
		//Bundle data = new Bundle();
		try {
			String AuthToken = GoogleAuthUtil.getToken(mContext,
					mAccount.name.toString(), Common.AUTH_TOKEN_TYPE);
			if (AuthToken != null) {
				i.putExtra(Common.AUTH_TOKEN, AuthToken);
//					((GoogleAuthCallback) mContext).pushSuccess(AuthToken,
//							mAccount.name);
				
				// sharedPreferencesEditor.putString(keys.USER_EMAIL,
				// account.name);
				// sharedPreferencesEditor.commit();
				// registerDomainUser(account.name);
				// callback.onAuthenticationComplete();
			}
		
		}
		catch(GooglePlayServicesAvailabilityException e){
			
//			((GoogleAuthCallback) mContext).pushFailure(
//					e.getLocalizedMessage(), null);
		}
		catch (UserRecoverableAuthException e) {
			// TODO Auto-generated catch block
			i = e.getIntent();
			i.putExtra(Common.HAS_EXCEPTION, true);
			i.putExtra(Common.EXCEPTION_TYPE, Common.EXCEPTIONS.UserRecoverableAuthException);
			i.putExtra(Common.MESSAGE, e.getLocalizedMessage());
//			((GoogleAuthCallback) mContext).pushFailure(
//					e.getClass().toString(), null);

		}  catch (GoogleAuthException e) {
			
			// TODO Auto-generated catch block
//			((GoogleAuthCallback) mContext).pushFailure(
//						e.getLocalizedMessage(), null);

		}catch (IOException e) {
			// TODO Auto-generated catch block
//			((GoogleAuthCallback) mContext).pushFailure(
//						e.getLocalizedMessage(), null);

		} catch (Exception e) {
//				((GoogleAuthCallback) mContext).pushFailure(
//						e.getLocalizedMessage(), mAccount.name);
			
		}
		((GoogleAuthCallback) mContext).AuthResult(i);
		return null;
	}

}
