package com.gravity.innovations.tasks.done;

import android.app.Activity;
import android.os.Handler;

public class WaitEventHandler extends Handler{
	public WaitEventHandler() {
		
	}
	public void AddRunnable(final Activity mActivity, final int functionToken,
			int Time)
	{
		Runnable temp = new Runnable() {

			@Override
			public void run() {
				// if(mActivity.getClass().getSimpleName().toString().equals(Common.AUTH_ACTIVITY))
				switch (functionToken) {
				case Common.CHECK_INTERNET:
					((SplashActivity) mActivity).CheckInternet();
					break;
				case Common.LOAD_PREFS:
					((SplashActivity) mActivity).LoadPreferences();
					break;
				case Common.GOOGLE_AUTH:
					((SplashActivity) mActivity).GoogleAuth();
					break;
				case Common.GET_ACCOUNT:
					((SplashActivity) mActivity).GetAccount();
					break;
				case Common.LOAD_LOCAL_DB:
					((SplashActivity) mActivity).LoadLocalDB();
					break;
				case Common.GRAVITY_REGISTER:
					((SplashActivity) mActivity).GravityRegister();
					break;
				case Common.CONFIG_GCM:
					((SplashActivity) mActivity).ConfigureGCM();
					break;
				case Common.GO_TO_MAIN:
					((SplashActivity) mActivity).GoToMain();
					break;
				default:
					break;
				}
				//delete this;
			}
		};
		
		this.removeCallbacksAndMessages(null);
		this.postDelayed(temp, Time);
	}
	
}
