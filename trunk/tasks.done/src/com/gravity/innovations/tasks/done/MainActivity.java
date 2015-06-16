package com.gravity.innovations.tasks.done;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.gravity.innovations.tasks.done.Common.Callbacks.ServiceCallback;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks,
		Common.Callbacks.HttpCallback, Common.Callbacks.TimeCallBack,
		ServiceCallback
/* , Common.Callbacks.SplashActivityCallback */{

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	public NavigationDrawerFragment mNavigationDrawerFragment;
	private Context mContext;
	private TaskListModel CurrentList;
	AccountManager mAccountManager;
	ArrayList<Account> mAccounts;
	Account mAccount;
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
	private AppHandlerService service;
	private ActionBar actionBar;
	private CharSequence mTitle;
	public TaskListFragment mTaskListFragment;
	private DashboardFragment mDashboardFragment;
	private int mUserActionBarColor;
	private Button btn_share;
	private TimeReciever mTimeReciever;
	private static final String PREF_USER_ACTION_BAR_COLOR = "actionbar_color";

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onStart();
		if (service != null)
			service.onActivityOpen(null, null);

		// Commented by Mushahid 12April2014 on Testing for exception
		// <<<<<<<<<<<<<<<<
		// try {
		// mContext.unregisterReceiver(mTimeReciever);
		// } catch (Exception e) {
		// Log.e(this.toString() + ": onDestroy", e.getLocalizedMessage());
		// }
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (service != null)
			service.onActivityOpen(this, this);
		try {
			mTimeReciever = new TimeReciever();
			mContext.registerReceiver(mTimeReciever, new IntentFilter(
					"android.intent.action.TIME_TICK"));
		} catch (Exception e) {
			Log.e(this.toString() + ": onResume", e.getLocalizedMessage());
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		try {
			if (service != null)
				service.onActivityOpen(null, null);
			mContext.unregisterReceiver(mTimeReciever);
		} catch (Exception e) {
			Log.e(this.toString() + ": onPause", e.getLocalizedMessage());
		}

	}

	// protected void onStart() {
	// // TODO Auto-generated method stub
	// super.onStart();
	// AsyncTask<Void,Void,Void> t = new AsyncTask<Void, Void, Void>(){
	//
	// @Override
	// protected Void doInBackground(Void... params) {
	// // TODO Auto-generated method stub
	// try {
	//
	// ((TheApp)
	// getApplicationContext()).startService((Common.Callbacks.ServiceCallback)mContext);
	//
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return null;
	// }
	// };
	// //t.execute();
	//
	//
	//
	// }
	//
	public void onServiceBound(AppHandlerService handleService) {
		// TODO Auto-generated method stub
		try {

			// Intent i = getIntent();
			// Bundle b = i.getBundleExtra("AppServiceExtra");
			//
			// mBinder=
			// (AppHandlerService.AppHandlerBinder)b.getBinder("binder");
			//

			service = ((TheApp) getApplicationContext()).getService();// mBinder.getService();
			service.onActivityOpen(this, this);
			service.addProgressTask("Main Application opened");

		} catch (Exception ex) {
			Toast.makeText(
					getApplicationContext(),
					"Problem Starting Service, Please Restart for Proper Experience",
					Toast.LENGTH_LONG).show();
		}
	}

	private void startActions() {
		Intent i = getIntent();
		int listID = -1, taskID = -1, actionID = -1, notification_ID;
		Bundle extra = i.getExtras();
		mContext = getApplicationContext();
		if (extra != null) {
			listID = i.getIntExtra(Common.KEY_EXTRAS_LIST_ID, -1);
			taskID = i.getIntExtra(Common.KEY_EXTRAS_TASK_ID, -1);
			notification_ID = i.getIntExtra(Common.KEY_EXTRAS_NOTIFICATION_ID_COMPARING, -1);
			actionID = i.getIntExtra(
					Common.KEY_EXTRAS_NOTIFICATION_ACTION_TYPE, -1);
			//Common.Notification.cancel(i.getIntExtra(Common.KEY_EXTRAS_NOTIFICATION_ID, -1), mContext);
			//autoCancel notification property was not working
			if (actionID != -1) {
				Intent intent = new Intent("CUSTOM_NOTIFICATION_INTENT");
				intent.setAction(Common.KEY_INTENT_NOTIFICATION_SET_ACTION);
				intent.putExtra(Common.KEY_EXTRAS_NOTIFICATION_ACTION_TYPE,
						String.valueOf(actionID));
				intent.putExtra(
						Common.KEY_EXTRAS_NOTIFICATION_RECEPIENT,
						i.getStringExtra(Common.KEY_EXTRAS_NOTIFICATION_RECEPIENT));
				sendBroadcast(intent);
			}
		}

		mContext = this;
		FragmentManager mgr = getSupportFragmentManager();

		mNavigationDrawerFragment = (NavigationDrawerFragment) mgr
				.findFragmentById(R.id.navigation_drawer);
		mTitle = "";
		// getTitle();
		// new userData();//
		user_data = service.user_data;
		// (Common.userData)getIntent().getExtras().getSerializable("user");
		// after latest commits commented
		// init user_data from intent extras
		// Set up the drawer.
		// user_data.image = null;
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout), mContext,
				user_data, listID, taskID, service);
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
						Common.SoftKeyboard.show(mImageView, MainActivity.this );
					} catch (Exception e) {
						Log.e("Error MA onCreate", "Error");
					}
					break;
				}

				return true;
			}
		});
		displayAds();

	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		 //getActionBar().hide();//***************
		setContentView(R.layout.activity_main);
		restoreActionBar();
		//getActionBar().hide();
		service = ((TheApp) getApplicationContext()).getService();// mBinder.getService();
		if (service != null) {
			service.onActivityOpen(this, this);
			service.addProgressTask("Main Application opened");
			startActions();
		} else {
			AsyncTask<Void, Void, Void> t = new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					// TODO Auto-generated method stub
					try {

						((TheApp) getApplicationContext())
								.startService((Common.Callbacks.ServiceCallback) mContext);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
			};
			t.execute();
		}

	}

	public void displayAds() {
		try {
			AdView mAdView = (AdView) findViewById(R.id.adView);
			// mAdView.setAdSize(AdSize.SMART_BANNER);
			mAdView.setAdListener(new AdListener() {

				@Override
				public void onAdClosed() {
					// TODO Auto-generated method stub
					super.onAdClosed();
				}

				@Override
				public void onAdFailedToLoad(int errorCode) {
					// TODO Auto-generated method stub
					super.onAdFailedToLoad(errorCode);
					switch (errorCode) {
					case AdRequest.ERROR_CODE_INTERNAL_ERROR:
						break;
					case AdRequest.ERROR_CODE_INVALID_REQUEST:
						break;
					case AdRequest.ERROR_CODE_NETWORK_ERROR:
						break;
					case AdRequest.ERROR_CODE_NO_FILL:
						break;
					}
				}

				@Override
				public void onAdLeftApplication() {
					// TODO Auto-generated method stub
					super.onAdLeftApplication();
				}

				@Override
				public void onAdLoaded() {
					// TODO Auto-generated method stub
					super.onAdLoaded();
				}

				@Override
				public void onAdOpened() {
					// TODO Auto-generated method stub
					super.onAdOpened();
				}

			});
			AdRequest adRequest = new AdRequest.Builder().build();
			mAdView.loadAd(adRequest);
		} catch (Exception e) {
			Log.e("displayAd:MainActivity", e.getLocalizedMessage());
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent intent) {
		// created for Calendar API
		super.onActivityResult(arg0, arg1, intent);
		if (arg0 == 12345) {
			if (arg1 == RESULT_OK) {
				Bundle mBundle = intent.getExtras();
				mBundle.getSerializable(Common.KEY_EXTRAS_LIST);
				mBundle.getSerializable(Common.KEY_EXTRAS_TASK);
				TaskListModel list = (TaskListModel) intent
						.getSerializableExtra(Common.KEY_EXTRAS_LIST);
				TaskModel task = (TaskModel) intent
						.getSerializableExtra(Common.KEY_EXTRAS_TASK);
				mNavigationDrawerFragment.updateTask(list, task);
			} else if (arg1 == RESULT_CANCELED) {
				onDashboardSelected();
			}
		}
	}

	@Override
	public void onNavigationDrawerItemSelected(TaskListModel temp,
			int selectTaskId) {

		CurrentList = temp;
		if (mNavigationDrawerFragment != null) {
			try {
				if (temp._id == -1) {
					int id = temp._id;
					try {
						onDashboardSelected();
					} catch (Exception e) {
						Log.e("asf", "asdf");
					}
					mNavigationDrawerFragment.openNavigationDrawer(id);
				} else {
					// update the main content by replacing fragment
					// actionBar = getSupportActionBar();
					// actionBar.setTitle("");
					FragmentManager fragmentManager = this
							.getSupportFragmentManager();
					mTaskListFragment = new TaskListFragment();

					mTaskListFragment.newInstance(temp, selectTaskId,
							mNavigationDrawerFragment, service);

					fragmentManager
							.beginTransaction()
							.replace(R.id.container,
									mTaskListFragment.getFragment()).commit();
					// actionBar.setTitle(CurrentList.title);
					//

				}
			} catch (Exception ex) {
				String x = ex.getLocalizedMessage();
			}
		}
	}

	@Override
	public void onDashboardSelected() {

		if (mNavigationDrawerFragment != null) {
			try {

				FragmentManager fragmentManager = this
						.getSupportFragmentManager();
				// db = new DatabaseHelper(mContext);
				// ArrayList<TaskModel> tasks = db.tasks.GetsDashboardTasks();
				mDashboardFragment = new DashboardFragment();
				mDashboardFragment.newInstance(
						mNavigationDrawerFragment.getPendingTasks(),
						mNavigationDrawerFragment, service);

				fragmentManager
						.beginTransaction()
						.replace(R.id.container,
								mDashboardFragment.getFragment()).commit();

				mNavigationDrawerFragment.shutNavigationDrawer();
			} catch (Exception ex) {
				String x = ex.getLocalizedMessage();
			}
		}
	}

	public void restoreActionBar() {
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		// actionBar.setTitle(mTitle);
		actionBar.setTitle(CurrentList.title);
		try{
		actionBar.hide();
		}
		catch(Exception e)
		{
			
		}
		/***********/
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

	public void manuallySelectOptionMenuItem(int id) {

		if (id == R.id.action_settings) {
			Intent i = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(i);
		} else if (id == R.id.action_dashboard) {
			onDashboardSelected();
		} else if (id == R.id.action_refer_friend) {
			mNavigationDrawerFragment
					.referAFriend(/* service.user_data._id */1234567890);
			// Intent intent = new Intent(MainActivity.this,
			// MailActivity.class);
			// startActivity(intent);
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
			// ZZ**ZZ mNavigationDrawerFragment.addOrEditTask(CurrentList, new
			// TaskModel());
		} else if (id == R.id.action_share) {
			listof_nameEmailPic(); // for calling list of users
		}

	}

	ArrayList<UserModel> users;

	public void listof_nameEmailPic() {
		DatabaseHelper h = new DatabaseHelper(mContext);

		users = new ArrayList<UserModel>();

		ArrayList<Common.CustomViewsData.MultiSelectRowData> users_lv = new ArrayList<Common.CustomViewsData.MultiSelectRowData>();

		users = h.users.Get();// .User_List();
		ArrayList<String> S = new ArrayList<String>();
		for (UserModel temp : users) {
			Common.CustomViewsData.MultiSelectRowData user = new Common.CustomViewsData.MultiSelectRowData();
			user.text1 = temp.displayName;
			user.text2 = temp.email;

			// Bitmap bmp = BitmapFactory.decodeByteArray(temp.image, 0,
			// temp.image.length);
			// ImageView image = (ImageView) findViewById(R.id.imageView1);

			// user.iconRes.setImageBitmap(bmp);
			// byte[] byteArray = getBlob(temp.image);
			// Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0
			// ,byteArray.length);
			S.add(temp.displayName);
			user.iconRes = temp.image;
			users_lv.add(user);

		}
		// CharSequence[] cs = S.toArray(new CharSequence[S.size()]);
		//
		// AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		// builder.setIcon(android.R.drawable.ic_popup_reminder);
		// builder.setTitle("share");
		// builder.setItems(cs, null);
		// builder.create().show();
		final MultiSelectListAdapter adapter = new MultiSelectListAdapter(this,
				R.layout.row_multiselectlist, users_lv);

		OnItemClickListener onItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.setOrRemoveSelection(view, position);

			}
		};
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
				ArrayList<Integer> sel = adapter.getSelected();
				ArrayList<UserModel> sel_users = new ArrayList<UserModel>();
				for (Integer i : sel) {
					sel_users.add(users.get(i));
				}
				// add sel_users in table_users_tasklist and update grid adapter
				// in header
				Toast.makeText(mContext, toString().valueOf(which),
						Toast.LENGTH_SHORT).show();

				// TaskModel tempModel = null;
				// String temp = tempModel.associated_usermodels;
				// temp = temp + ", " + which;
				// tempModel.associated_usermodels = temp;
				// db.Task_Edit(tempModel);

			}
		};
		Common.CustomDialog.MultiChoiceDialog(mContext, adapter,
				onItemClickListener, negListener, posListener, R.string.ok,
				R.string.cancel, "Share");

		// View view = getLayoutInflater().inflate(
		// R.layout.multiselectlist_list, null);
		//
		// ListView lst = (ListView) view.findViewById(R.id.listView1);
		// lst.setAdapter(adapter);
		//
		//
		//
		//
		// Common.CustomDialog.CustomDialog(mContext, view);

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

	public void updateCurrentTask() {
		// for marking the task as done from detailsDial
		try {
			mTaskListFragment.updateCurrentTask();
			// mTaskListFragment.mTaskAdapter.notifyItemChanged(mTaskListFragment.mTaskAdapter.getPosition());
		} catch (Exception e) {
			Log.e("updateCurrentTask:MainActivity", e.getLocalizedMessage());
		}
	}

	public ArrayList<UserModel> getUsers() {
		// TODO Auto-generated method stub

		if (service != null)
			return service.db.users.Get();// .User_List();
		return new ArrayList<UserModel>();
	}

	@Override
	public void startResultActivity(Intent intent, int RequestCode) {
		// TODO Auto-generated method stub

	}

}
