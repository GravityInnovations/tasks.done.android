package com.gravity.innovations.tasks.done;
//This class will include common keys used in application

//default decleration as below
//public static final int MY_KEY_SOME = 0;

//default usuage outside as below
//PublicKeys.MY_KEY_SOME
//pass it to switch, bundle extras, shared prefs etc
public class Common {
	public static final int USER_EMAIL = 1;//used in shared prefs for io of user email address
	public static final int SPLASH_TIME_OUT = 3000;
	public static final String ACCOUNT_TYPE = "com.google";
	public static final String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile https://www.google.com/m8/feeds/ https://www.googleapis.com/auth/drive.appdata https://www.googleapis.com/auth/tasks";
	
	
}
