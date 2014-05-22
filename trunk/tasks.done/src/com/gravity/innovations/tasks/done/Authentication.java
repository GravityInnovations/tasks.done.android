package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

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
	public Authentication(Context mContext)
	{
		this.mContext = mContext;
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
	
	protected void getAuthentication(final Account account)//returns status
	{
		 
		accountManager.getAuthToken(account, Common.AUTH_TOKEN_TYPE, null,(Activity)mContext, new AccountManagerCallback<Bundle>() {
    	    public void run(AccountManagerFuture<Bundle> future) {
    	      try {
    	    	
    	        // If the user has authorized your application to use the tasks API
    	        // a token is available.
    	    	 
    	        String AuthToken = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
    	        if(AuthToken != null){
    	        	if(mContext.getClass().getSimpleName().toString().equals(Common.AUTH_ACTIVITY))
    	        	((AuthenticationActivity)mContext).pushSuccess(AuthToken, account.name);
    				
    	        	//sharedPreferencesEditor.putString(keys.USER_EMAIL, account.name);
    	        	//sharedPreferencesEditor.commit();
    	        	//registerDomainUser(account.name);
    	        	//callback.onAuthenticationComplete();
    	        }
    	        //startActivity(toListIntent);
    	         
    	        // Now you can use the Tasks API...
    	        //useTasksAPI(token);
    	      } catch (OperationCanceledException e) {
    	    	  ((AuthenticationActivity)mContext).pushFalure(e.getLocalizedMessage(),null);
    				
    	    	  //
    	    	  // TODO: The user has denied you access to the API, you should handle that
    	      } catch (Exception e) {
    	    	  ((AuthenticationActivity)mContext).pushFalure(e.getLocalizedMessage(), account.name);
  				
    	      }
    	      
    	    }
    	  }, null);
	}
}
