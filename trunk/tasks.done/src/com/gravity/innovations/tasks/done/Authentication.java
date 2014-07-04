package com.gravity.innovations.tasks.done;

import java.io.IOException;
import java.util.ArrayList;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class Authentication {
	ArrayList<Account> mAccounts;
	private AccountManager accountManager;
	private Context mContext;
	//private Context mActivity;
	public Authentication(Context mContext)
	{
		this.mContext = mContext;
		this.mAccounts = new ArrayList<Account>();
		accountManager = AccountManager.get(mContext);
		
	    for (Account i:accountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE)) {
	    	mAccounts.add(i);
	    }
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
	public void getAuthentication(String Email)//returns status
	{
		Account temp = this.getAccount(Email);
		if(temp!=null)
			this.getAuthentication(temp);
			
	}
	protected void getAuthentication(final Account account)//returns status
	{
		/* 
		accountManager.getAuthToken(account, Common.AUTH_TOKEN_TYPE, null,(Activity)mContext, new AccountManagerCallback<Bundle>() {
			String className = mContext.getClass().getSimpleName().toString();
        	
			public void run(AccountManagerFuture<Bundle> future) {
    	      try {
    	    	
    	        // If the user has authorized your application to use the tasks API
    	        // a token is available.
    	    	 
    	        String AuthToken = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
    	        
    	        if(AuthToken != null){
    	        	if(className.equals(Common.AUTH_ACTIVITY))
    	        	((AuthenticationActivity)mContext).pushSuccess(AuthToken, account.name);
    	        	else if(className.equals(Common.SPLASH_ACTIVITY))
    	        		((SplashActivity)mContext).pushSuccess(AuthToken, account.name);
    	        	
    	        	//sharedPreferencesEditor.putString(keys.USER_EMAIL, account.name);
    	        	//sharedPreferencesEditor.commit();
    	        	//registerDomainUser(account.name);
    	        	//callback.onAuthenticationComplete();
    	        }
    	        //startActivity(toListIntent);
    	         
    	        // Now you can use the Tasks API...
    	        //useTasksAPI(token);
    	      } catch (OperationCanceledException e) {
    	    	  if(className.equals(Common.AUTH_ACTIVITY))
    	    	  ((AuthenticationActivity)mContext).pushFailure(e.getLocalizedMessage(),null);
    	    	  else if(className.equals(Common.SPLASH_ACTIVITY))
    	    		  ((SplashActivity)mContext).pushFailure(e.getLocalizedMessage(),null);
    	    	  
    	    	  //
    	    	  // TODO: The user has denied you access to the API, you should handle that
    	      } catch (Exception e) {
    	    	  if(className.equals(Common.AUTH_ACTIVITY))
    	    	  ((AuthenticationActivity)mContext).pushFailure(e.getLocalizedMessage(), account.name);
    	    	  else if(className.equals(Common.SPLASH_ACTIVITY))
    	    		  ((SplashActivity)mContext).pushFailure(e.getLocalizedMessage(),account.name);
    	    	  
    	      }
    	      
    	    }
    	  }, null);*/
		String className = mContext.getClass().getSimpleName().toString();
    	
		try {
			String AuthToken  = GoogleAuthUtil.getToken(mContext,account.name.toString(), Common.AUTH_TOKEN_TYPE);
			if(AuthToken != null){
				
	        	if(className.equals(Common.AUTH_ACTIVITY))
	        	((AuthenticationActivity)mContext).pushSuccess(AuthToken, account.name);
	        	else if(className.equals(Common.SPLASH_ACTIVITY))
	        		((SplashActivity)mContext).pushSuccess(AuthToken, account.name);
	        	
	        	//sharedPreferencesEditor.putString(keys.USER_EMAIL, account.name);
	        	//sharedPreferencesEditor.commit();
	        	//registerDomainUser(account.name);
	        	//callback.onAuthenticationComplete();
	        }
		
		} catch (UserRecoverableAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GoogleAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
