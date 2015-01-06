package com.gravity.innovations.tasks.done;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gravity.innovations.tasks.done.Common.userData;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks,
		Common.Callbacks.HttpCallback, Common.Callbacks.TimeCallBack
/* , Common.Callbacks.SplashActivityCallback */{

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

	TimePicker mTimePicker;
	int hour;
	int minute;

	DatabaseHelper db;
	ImageView mImageView;
	EditText mEditText;
	ProgressBar mProgressBar;
	ProgressDialog mProgressDialog;
	Common.userData user_data;
	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */

	private ActionBar actionBar;
	private CharSequence mTitle;
	public TaskListFragment mTaskListFragment;

	private int mUserActionBarColor;
	private Button btn_share;
	private static final String PREF_USER_ACTION_BAR_COLOR = "actionbar_color";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		getActionBar().hide();
		//getActionBar().setSubtitle("sub");
		//getActionBar().setNavigationMode(getActionBar().NAVIGATION_MODE_STANDARD);
		//getActionBar().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
		setContentView(R.layout.activity_main);
//		if (Build.VERSION.SDK_INT < 16) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
//	 else{
//		 View decorView = getWindow().getDecorView();
//		// Hide the status bar.
//		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
//				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//	            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//	            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//	            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//		decorView.setSystemUiVisibility(uiOptions);
	// }
		//getActionBar().show();
		Intent i = getIntent();
		int listID = -1, taskID = -1;
		Bundle extra = i.getExtras();
		mContext = getApplicationContext();
		if (extra != null) {
			listID = i.getIntExtra("_task_list_id", 1);
			taskID = i.getIntExtra("_task_id", 1);

		}

		// String x = getHash("Faik");
		mContext = this;
		FragmentManager mgr = getSupportFragmentManager();
		
		mNavigationDrawerFragment = (NavigationDrawerFragment) mgr
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		// new userData();//
		user_data =(Common.userData)getIntent().getExtras().getSerializable("user");//
									// after latest commits commented		
		// init user_data from intent extras
		// Set up the drawer.
		//user_data.image = null;
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout), mContext,
				user_data, listID, taskID);
		mEditText = (EditText) findViewById(R.id.search);
		/*
		 * code below opens soft keyboard on imageClick
		 */
		mImageView = (ImageView) findViewById(R.id.keyboard);
		mImageView.setFocusableInTouchMode(true);
		mImageView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					try {
						showSoftKeyboard(mImageView);
					} catch (Exception e) {
						Log.e("Error MA onCreate", "Error");
					}
					break;
				}

				return true;
			}
		});

	}

	public void floatButtonOnClick(View v) {
		mNavigationDrawerFragment.addOrEditTask(CurrentList, new TaskModel());
	}// creates a dialog for new task

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	// @SuppressLint("NewApi")
	public void showSoftKeyboard(View view) {
		if (view.requestFocus()) {
			mEditText.setFocusable(true);
			mEditText.requestFocus();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);

		}
	}

	@Override
	public void onNavigationDrawerItemSelected(TaskListModel temp,
			int selectTaskId) {
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

				mTaskListFragment.newInstance(temp, selectTaskId,
						mNavigationDrawerFragment);

				fragmentManager
						.beginTransaction()
						.replace(R.id.container,
								mTaskListFragment.getFragment()).commit();
				//actionBar.setTitle(CurrentList.title);
				actionBar.setTitle("");

			}
		} catch (Exception ex) {
			String x = ex.getLocalizedMessage();
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
		manuallySelectOptionMenuItem(id);
		return super.onOptionsItemSelected(item);
	}
	public void manuallySelectOptionMenuItem(int id)
	{
		
		if (id == R.id.action_settings) {
			Intent i = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(i);
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
			listof_nameEmailPic(); // for calling list of users
		} 
		
	}
	ArrayList<UserModel> users;
	public void listof_nameEmailPic() {
		

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

		// removed because it was useless
		// @Override
		// public View onCreateView(LayoutInflater inflater, ViewGroup
		// container,
		// Bundle savedInstanceState) {
		// View rootView = inflater.inflate(R.layout.fragment_main, container,
		// false);
		// TextView textView = (TextView) rootView
		// .findViewById(R.id.section_label);
		// textView.setText(Integer.toString(getArguments().getInt(
		// ARG_SECTION_NUMBER)));
		// return rootView;
		// }

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			// ((MainActivity)
			// activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}

	@Override
	public void httpResult(JSONObject data, int RequestCode, int ResultCode) {
		// TODO Auto-generated method stub
		switch (RequestCode) {
		case Common.RequestCodes.GRAVITY_SEND_TASKLIST:
			if (ResultCode == Common.HTTP_RESPONSE_OK) {
				try {
					data = data.getJSONObject("data");
					data.get("TaskListId");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

			}
			break;
		}

	}

	@Override
	public void onTimeReceive(Context mContext, Intent intent) {
		// mNavigationDrawerFragment.onTimeReceive(mContext, intent);
		mTaskListFragment.updateRelativeTime();// .mTaskAdapter.notifyDataSetChanged();
	}	
}
