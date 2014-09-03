package com.gravity.innovations.tasks.done;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.internal.cr;
import com.gravity.innovations.tasks.done.Common.User;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.ContactsContract;
import android.database.Cursor;
import android.hardware.Camera.Size;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private Context mContext;
	private TaskListModel CurrentList;

	AccountManager mAccountManager;
	ArrayList<Account> mAccounts;
	Account mAccount;

	DatabaseHelper db;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private ActionBar actionBar;
	private CharSequence mTitle;
	public TaskListFragment mTaskListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// String x = getHash("Faik");
		mContext = this;
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout), mContext);
		/*
		 * try{ Common.CustomDialog.CustomDialog(mContext, view, negListener,
		 * posListener, R.string.dialog_ok, R.string.dialog_cancel, "Share");
		 * //Put in listview adapter = new
		 * MultiSelectListAdapter(MainActivity.this,
		 * R.layout.multiselectlist_row, h.Get_Users()); String[] from = {
		 * "php_key","c_key","android_key","hacking_key" };
		 * //listview.setAdapter(new ArrayAdapter<String>(mContext,
		 * R.layout.multiselectlist_row,R.id.textView1,from));
		 * listview.setAdapter(adapter); } catch(Exception ex) { String x; }
		 */
	}

	@Override
	public void onNavigationDrawerItemSelected(TaskListModel temp) {
		CurrentList = temp;
		try {
			if (temp._id == -1) {
				int id = temp._id;
				mNavigationDrawerFragment.onMinusOne(id);
			} else {
				// update the main content by replacing fragments
				FragmentManager fragmentManager = this
						.getSupportFragmentManager();
				mTaskListFragment = new TaskListFragment();
				mTaskListFragment.newInstance(temp, mNavigationDrawerFragment);
				fragmentManager
						.beginTransaction()
						.replace(R.id.container,
								mTaskListFragment.getFragment()).commit();
				actionBar.setTitle(CurrentList.title);
			}
		} catch (Exception ex) {
			String x = ex.getLocalizedMessage();
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		// actionBar.setTitle(mTitle);
		actionBar.setTitle(CurrentList.title);
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

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.action_add) {
			mNavigationDrawerFragment.addOrEditTaskList(new TaskListModel());
		} else if (id == R.id.action_delete) {
			mNavigationDrawerFragment.deleteTaskList(CurrentList);
		} else if (id == R.id.action_edit) {
			mNavigationDrawerFragment.addOrEditTaskList(CurrentList);
		} else if (id == R.id.action_developer) {
			Intent i = new Intent(MainActivity.this, GravityActivity.class);
			startActivity(i);
		} else if (id == R.id.action_store) {
			Intent i = new Intent(MainActivity.this, StoreActivity.class);
			startActivity(i);
		} else if (id == R.id.action_add_task) {
			mNavigationDrawerFragment.addOrEditTask(CurrentList,
					new TaskModel());
		} else if (id == R.id.action_share) {
			 /*		 		
			db = new DatabaseHelper(mContext);
			for (int i=0;i<=10;i++ ){
			db.User_New();
			} 
		
		*/
			//  getContacts();
		 
			DatabaseHelper h = new DatabaseHelper(mContext);
			DialogInterface.OnClickListener negListener = new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			};
			DialogInterface.OnClickListener posListener = new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			};
			
			
			ArrayList<UserModel> email_records = new ArrayList<UserModel>();
			ArrayList<Common.CustomViewsData.MultiSelectRowData> users = new ArrayList<Common.CustomViewsData.MultiSelectRowData>();
			
		  	email_records = h.User_List();
			for (UserModel temp : email_records) {
				Common.CustomViewsData.MultiSelectRowData user = new Common.CustomViewsData.MultiSelectRowData();
				user.text1 = temp.displayName;
				user.text2 = temp.email;
				user.iconRes = R.drawable.ic_launcher;
				users.add(user);
			}
			
			final MultiSelectListAdapter adapter = new MultiSelectListAdapter(
					this, R.layout.multiselectlist_row, users);
			DialogInterface.OnClickListener itemClickListner = new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// dialog.cancel();
					adapter.setNewSelection(which, true);
				}
			};
			OnItemClickListener onItemClickListener = new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					adapter.setOrRemoveSelection(position);
				}
			};
			Common.CustomDialog.MultiChoiceDialog(mContext, adapter,
					onItemClickListener, negListener, posListener,
					R.string.dialog_ok, R.string.dialog_cancel, "Share");
 	     }
		 
		return super.onOptionsItemSelected(item);
	}

	// @SuppressLint("NewApi")
	public ArrayList<String> getContacts() {

		ArrayList<String> data_emails = new ArrayList<String>();
		ArrayList<String> emails = new ArrayList<String>();
		final String[] PROJECTION = new String[] {
				ContactsContract.CommonDataKinds.Email.CONTACT_ID,
				ContactsContract.Contacts.DISPLAY_NAME };

		ContentResolver cr = getContentResolver();
		Cursor cursor = cr.query(ContactsContract.Data.CONTENT_URI, null, null,
				null, null);
		if (cursor != null) {
			try {
				db = new DatabaseHelper(mContext);
				final int contactIdIndex = cursor
						.getColumnIndex(ContactsContract.Data.CONTACT_ID);
				final int displayNameIndex = cursor
						.getColumnIndex(ContactsContract.Data.DISPLAY_NAME);
				long contactId;
				String displayName;
				// ArrayList<String> emails = new ArrayList<String>();
				while (cursor.moveToNext()) {

					displayName = cursor.getString(displayNameIndex);
					contactId = cursor.getLong(contactIdIndex);
					emails = getEmails(contactId, displayName);
					for (String email : emails) {
						UserModel user = new UserModel(
								String.valueOf(contactId), email, displayName);
					 	db.User_New(user);   
					}
					// emails.add(displayName);
					// data_emails = emails;
					// for (int i=0; i<=emails.size(); i++){
					// // data_emails.addAll(emails);
					// db = new DatabaseHelper(mContext);
					// db.Users_Email_New(String.valueOf(contactId),
					// displayName, emails.get(i++));
					// Log.e("String.valueOf( contactId) + displayName","emails");
					// }
				}

			} finally {
				cursor.close();

			}

		}

		String x = "";
		return data_emails;
		// return data_email;

	}

	public ArrayList<String> getEmails(long contactId, String displayName) {
		ArrayList<String> emails = new ArrayList<String>();
		final String[] projection = new String[] { Email.DATA, // use
																// Email.ADDRESS
																// for API-Level
																// 11+
				Email.TYPE };
		ContentResolver cr = getContentResolver();
		Cursor cursor = cr.query(
				ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Email.CONTACT_ID + "="
						+ contactId, null, null);
		if (cursor != null) {
			try {

				final int emailIndex = cursor
						.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
				final int typeIndex = cursor
						.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE);
				String email, type;
				while (cursor.moveToNext()) {
					email = cursor.getString(emailIndex);
					// type = cursor.getString(typeIndex);
					if (email.contains("gmail.com"))
						emails.add(email);

				}
			} finally {
				cursor.close();
			}
		}

		return emails;
	}

	public String getHash(String message) {
		String algorithm = "SHA384";
		String hex = "";
		try {
			byte[] buffer = message.getBytes();
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(buffer);
			byte[] digest = md.digest();
			for (int i = 0; i < digest.length; i++) {
				int b = digest[i] & 0xff;
				if (Integer.toHexString(b).length() == 1)
					hex = hex + "0";
				hex = hex + Integer.toHexString(b);
			}
			return hex;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			TextView textView = (TextView) rootView
					.findViewById(R.id.section_label);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}
}