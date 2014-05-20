package com.example.splash;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Prefrence extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.xml.pref);
		addPreferencesFromResource(R.xml.pref);
	}
	

}
