package com.gravity.innovations.tasks.done;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import android.accounts.Account;
import android.accounts.AccountManager;

import org.json.JSONException;
import org.json.JSONObject;

import com.gravity.innovations.tasks.done.Common.userData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Contacts.Photo;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks,
		Common.Callbacks.HttpCallback, Common.Callbacks.TimeCallBack {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// String x = getHash("Faik");
		mContext = this;
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		// new userData();//
		user_data =(Common.userData)getIntent().getExtras().getSerializable("user");
		//init user_data from intent extras
	// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout), mContext,
				user_data);
		mEditText = (EditText) findViewById(R.id.search);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
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
		this.registerReceiver(this.mConnReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));

		this.registerReceiver(this.mNetworkChange, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));

	}

	private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				ConnectivityManager cm = (ConnectivityManager) mContext
						.getSystemService(Context.CONNECTIVITY_SERVICE);

				NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
				boolean isConnected = activeNetwork != null
						&& activeNetwork.isConnectedOrConnecting();
				boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
				boolean isMobile = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
				if (isConnected) {

					if (isWiFi) {
						WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
						if (wifiManager != null) {
							WifiInfo info = wifiManager.getConnectionInfo();
							if (info != null) {
								String ssid = info.getSSID();
								Toast.makeText(getApplicationContext(),
										ssid + " Connected", Toast.LENGTH_LONG)
										.show();
							}
						}

					} else if (isMobile) {
						Toast.makeText(getApplicationContext(),
								"Mobile internet Connected", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Internet Not Connected", Toast.LENGTH_SHORT)
							.show();
				}
			} catch (Exception e) {
				Log.e("interetCheck", "MainActivity");
			}
		}

	};

	private BroadcastReceiver mNetworkChange = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			final ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			final android.net.NetworkInfo wifi = connMgr
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			final android.net.NetworkInfo mobile = connMgr
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (wifi.isAvailable()) {
				Log.d("Wifi Netowk Available ", "Flag No 1");
			} else if (mobile.isAvailable()) {
				Log.d("mobile Netowk Available ", "Flag No 1");
			}
		}

	};

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mConnReceiver);
		unregisterReceiver(mNetworkChange);
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

	// public void onSectionAttached(int number) {
	// switch (number) {
	// case 1:
	// mTitle = getString(R.string.title_section1);
	// break;
	// case 2:
	// mTitle = getString(R.string.title_section2);
	// break;
	// case 3:
	// mTitle = getString(R.string.title_section3);
	// break;
	// }
	// }

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
			listof_nameEmailPic(); // for calling list of name email and pics
			//startProcess(); // for calling thread
		}
		return super.onOptionsItemSelected(item);
	}

	private void startProcess() {
		mProgressDialog = ProgressDialog.show(MainActivity.this, null,
				"please donot interupt let it finish", true);
		mProgressDialog.setCancelable(false);
		new Thread(new Task()).start();
	}

	class Task implements Runnable {
		@Override
		public void run() {
			try {
				dbInsertion();//this is what we need //getContacts();
				mProgressDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void listof_nameEmailPic() {
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
			user.iconRes = temp.image;

			users.add(user);
		}

		final MultiSelectListAdapter adapter = new MultiSelectListAdapter(this,
				R.layout.multiselectlist_row, users);
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

	////////////////////getting contacts informaiton method/////////////////////////////
	public ArrayList<HashMap<String, Object>> getContacts() {

		ArrayList<HashMap<String, Object>> contacts = new ArrayList<HashMap<String, Object>>();
		final String[] projection = new String[] { RawContacts.CONTACT_ID,
				RawContacts.DELETED };

		// @SuppressWarnings("deprecation")
		ContentResolver cr = getContentResolver();
		Cursor rawContacts = cr.query(RawContacts.CONTENT_URI, projection,
				null, null, null);

		final int contactIdColumnIndex = rawContacts
				.getColumnIndex(RawContacts.CONTACT_ID);
		final int deletedColumnIndex = rawContacts
				.getColumnIndex(RawContacts.DELETED);

		if (rawContacts.moveToFirst()) {
			while (!rawContacts.isAfterLast()) {
				final int contactId = rawContacts.getInt(contactIdColumnIndex);
				final boolean deleted = (rawContacts.getInt(deletedColumnIndex) == 1);
				if (!deleted) {
					HashMap<String, Object> contactInfo = new HashMap<String, Object>() {
						{
							put("contactId", "");
							put("name", "");
							put("email", "");
							put("address", "");
							put("photo", "");
							put("phone", "");
						}
					};
					contactInfo.put("contactId", "" + contactId);
					contactInfo.put("name", getName(contactId));
					contactInfo.put("email", getEmail(contactId));
					contactInfo.put("photo",
							getPhoto(contactId) != null ? getPhoto(contactId)
									: "");
					contactInfo.put("address", getAddress(contactId));
					contactInfo.put("phone", getPhoneNumber(contactId));
					contactInfo.put("isChecked", "false");
					contacts.add(contactInfo);
				}
				rawContacts.moveToNext();
			}
		}
		rawContacts.close();
		return contacts;

	}

	// public void dbInsertion(ArrayList<HashMap<String, Object>> contacts) {
	@SuppressLint("NewApi")
	public void dbInsertion() {
		ArrayList<HashMap<String, Object>> contacts = getContacts();
		db = new DatabaseHelper(mContext);
		int size = contacts.size();
		for (int i = 0; i < size; i++) {

			HashMap<String, Object> contactItem = contacts.get(i);

			ContentValues dataToInsert = new ContentValues();

			UserModel userModel = new UserModel();
			// userModel.contact_id.("contactId").toString();
			// userModel.contactItem.get("name").toString();
			// userModel.contactItem.get("email").toString();
			// userModel.contactItem.get("address").toString();
			// userModel.contactItem.get("phone").toString();

			if (contactItem.get("email") != null) {

				if (contactItem.get("photo") != null
						&& contactItem.get("photo") instanceof Bitmap) {

					Bitmap b = (Bitmap) contactItem.get("photo");
					int bytes = b.getByteCount();

					ByteBuffer buffer = ByteBuffer.allocate(bytes); // Create a
																	// new
																	// buffer
					b.copyPixelsToBuffer(buffer); // Move the byte data to the
													// buffer
					byte[] array = buffer.array();

					// userModel.setPhoto(array);

					// UserModel user =
					// new UserModel(
					// contactItem.get("name").toString(),
					// contactItem.get("email").toString(),
					// contactItem.get("phone").toString(),
					// array
					// );
					// db.User_New(user);
					if (contactItem.get("email").toString()
							.contains("gmail.com")) { // this is gmail thing
						UserModel user = new UserModel(contactItem.get("name")
								.toString(), contactItem.get("email")
								.toString(), array
						// contactItem.get("phone").toString()
						);
						db.User_New(user);
					}

				} else {
					// UserModel user = new UserModel(
					// contactItem.get("name").toString(),
					// contactItem.get("email").toString(),
					// array
					// //contactItem.get("phone").toString()
					// );
					// db.User_New(user);

				}

				/*
				 * UserModel user = new UserModel(
				 * contactItem.get("name").toString(),
				 * contactItem.get("email").toString()); db.User_New(user);
				 */
			}
		}
	}

	private String getName(int contactId) {
		String name = "";
		final String[] projection = new String[] { Contacts.DISPLAY_NAME };

		ContentResolver cr = getContentResolver();
		Cursor contact = cr.query(Contacts.CONTENT_URI, projection,
				Contacts._ID + "=?",
				new String[] { String.valueOf(contactId) }, null);

		if (contact.moveToFirst()) {
			name = contact.getString(contact
					.getColumnIndex(Contacts.DISPLAY_NAME));
			contact.close();
		}
		contact.close();
		return name;

	}

	private String getEmail(int contactId) {
		String emailStr = "";
		final String[] projection = new String[] { Email.DATA, // use
				// Email.ADDRESS
				// for API-Level
				// 11+
				Email.TYPE };
		ContentResolver cr = getContentResolver();
		Cursor email = cr.query(Email.CONTENT_URI, projection, Data.CONTACT_ID
				+ "=?", new String[] { String.valueOf(contactId) }, null);

		if (email.moveToFirst()) {
			final int contactEmailColumnIndex = email
					.getColumnIndex(Email.DATA);

			while (!email.isAfterLast()) {
				emailStr = emailStr + email.getString(contactEmailColumnIndex)
						+ ";";
				email.moveToNext();
			}
		}
		email.close();
		return emailStr;

	}

	private Bitmap getPhoto(int contactId) {
		Bitmap photo = null;
		final String[] projection = new String[] { Contacts.PHOTO_ID };
		ContentResolver cr = getContentResolver();
		Cursor contact = cr.query(Contacts.CONTENT_URI, projection,
				Contacts._ID + "=?",
				new String[] { String.valueOf(contactId) }, null);

		if (contact.moveToFirst()) {
			final String photoId = contact.getString(contact
					.getColumnIndex(Contacts.PHOTO_ID));
			if (photoId != null) {
				photo = getBitmap(photoId);
			} else {
				photo = null;
			}
		}
		contact.close();

		return photo;
	}

	private Bitmap getBitmap(String photoId) {
		ContentResolver cr = getContentResolver();
		Cursor photo = cr.query(Data.CONTENT_URI, new String[] { Photo.PHOTO },
				Data._ID + "=?", new String[] { photoId }, null);

		final Bitmap photoBitmap;
		if (photo.moveToFirst()) {
			byte[] photoBlob = photo.getBlob(photo.getColumnIndex(Photo.PHOTO));
			photoBitmap = BitmapFactory.decodeByteArray(photoBlob, 0,
					photoBlob.length);
		} else {
			photoBitmap = null;
		}

		photo.close();
		saveImage(photoBitmap);
		return photoBitmap;
	}

	public void saveImage(Bitmap bmp) {
		Random generator = new Random();
		int n = 10000;
		n = generator.nextInt(n);
		String fname = "Image-" + n + ".jpg";

		String root = Environment.getExternalStorageDirectory().toString();

		File myDir = new File(root + "/contact_images");
		myDir.mkdirs();

		File file = new File(myDir, fname);

		if (file.exists())
			file.delete();

		try {
			FileOutputStream out = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			getApplicationContext().sendBroadcast(
					new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
							+ Environment.getExternalStorageDirectory())));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	private String getAddress(int contactId) {
		String postalData = "";
		String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND "
				+ ContactsContract.Data.MIMETYPE + " = ?";
		String[] addrWhereParams = new String[] {
				String.valueOf(contactId),
				ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };
		ContentResolver cr = getContentResolver();
		Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI, null,
				addrWhere, addrWhereParams, null);

		if (addrCur.moveToFirst()) {
			postalData = addrCur
					.getString(addrCur
							.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
		}
		addrCur.close();
		return postalData;
	}

	private String getPhoneNumber(int contactId) {

		String phoneNumber = "";
		final String[] projection = new String[] { Phone.NUMBER, Phone.TYPE, };
		ContentResolver cr = getContentResolver();
		Cursor phone = cr.query(Phone.CONTENT_URI, projection, Data.CONTACT_ID
				+ "=?", new String[] { String.valueOf(contactId) }, null);

		if (phone.moveToFirst()) {
			final int contactNumberColumnIndex = phone
					.getColumnIndex(Phone.DATA);

			while (!phone.isAfterLast()) {
				phoneNumber = phoneNumber
						+ phone.getString(contactNumberColumnIndex) + ";";
				phone.moveToNext();
			}

		}
		phone.close();
		return phoneNumber;
	}

////////////////////getting contacts informaiton method/////////////////////////////
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
		//mNavigationDrawerFragment.onTimeReceive(mContext, intent);
		mTaskListFragment.updateRelativeTime();//.mTaskAdapter.notifyDataSetChanged();
	}

}
