package com.gravity.innovations.manager.call.sms;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		mNavigationDrawerFragment =  (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		/*if(getString(R.string.screen_type) == "phone")
		{
			mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
					(DrawerLayout) findViewById(R.id.drawer_layout));
		}
		else{
			
		}*/
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		Intent i = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
		i.setType(Phone.CONTENT_TYPE);
		//super.startActivityForResult(i, 1001);

	}

	@Override
	public void onNavigationDrawerItemSelected(MainMenuItem item) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		/*
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(1)).commit();
		*/
		try{
			switch(item.FragmentToken)
			{
				case CommonKeys.SMS_BLOCKER_FRAGMENT_TOKEN:
					fragmentManager
					.beginTransaction()
					.replace(R.id.container, 
							(Fragment)smsBlockerFragment.newInstance(item)
							).commit();
					break;
			}
			
		}
		catch(Exception ex)
		{
			String c = ex.getMessage();
		}
	}

	public void onSectionAttached(String Title) {
		mTitle = Title;
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if (id == R.id.action_example) {
			//startActivity(new Intent(this, FormActivity.class ));//new Intent("com.example.MainActivity"));
        	//Toast.makeText(this, "Example action.", Toast.LENGTH_SHORT).show();
            try{
			Intent i = new Intent(this, FormActivity.class);
			startActivity(i);
			}
            catch(Exception ex){
            	String a = ex.getMessage();
            }
			return true;
        }
		return super.onOptionsItemSelected(item);
	}


	
}
