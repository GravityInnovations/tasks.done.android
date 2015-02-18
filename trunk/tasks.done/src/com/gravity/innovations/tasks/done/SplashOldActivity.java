//package com.gravity.innovations.tasks.done;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.nio.ByteBuffer;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.ContentResolver;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.ContextWrapper;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.LinearGradient;
//import android.graphics.Shader;
//import android.graphics.Shader.TileMode;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.provider.ContactsContract.Contacts;
//import android.provider.ContactsContract.Data;
//import android.provider.ContactsContract.RawContacts;
//import android.provider.ContactsContract.CommonDataKinds.Email;
//import android.provider.ContactsContract.CommonDataKinds.Phone;
//import android.provider.ContactsContract.Contacts.Photo;
//import android.view.Window;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class SplashOldActivity extends Activity implements
//		Common.Callbacks.SplashActivityCallback {
//	private ProgressBar progress1;
//	private ProgressBar progress2;
//	private ProgressBar progress3;
//	private TextView status;
//	private TextView log;
//	private TextView progress_tasks;
//	private Activity mActivity;
//	private Context mContext;
//	private GoogleCloudMessaging gcm;
//	private ArrayList<String> tasks;
//	private GoogleAuth mAuth;
//	private SharedPreferences mSharedPreferences;
//	private SharedPreferences.Editor mSharedPreferencesEditor;
//	// data
//	private Common.userData user_data;
//	private void updateLog()
//	{
//		user_data.email = mSharedPreferences.getString(Common.USER_EMAIL, null);
//		user_data.is_sync_type = mSharedPreferences.getBoolean(
//				Common.USER_IS_SYNC_TYPE, true);
//		
//		user_data.gravity_is_registered = mSharedPreferences.getBoolean(
//				Common.USER_IS_REGISTERED, false);
//		user_data.gravity_user_id = mSharedPreferences.getString(
//				Common.USER_ID_GRAVITY, null);
//		user_data.google_reg_id = 
//				mSharedPreferences.getString(Common.GOOGLE_PROPERTY_REG_ID, "");
//		user_data.google_regVer = 
//				mSharedPreferences.getInt(Common.GOOGLE_PROPERTY_APP_VERSION, Integer.MIN_VALUE);
//		user_data.google_AuthToken = 
//				mSharedPreferences.getString(Common.AUTH_TOKEN, null);
//	   String txt = "Name: "+user_data.name;
//	   txt += "\nEmail: "+user_data.email;
//	   txt += "\nIs_Sync_type: "+user_data.is_sync_type;
//	   txt += "\nIs Registered: "+user_data.gravity_is_registered;
//	   txt += "\nReg id gravity: "+user_data.gravity_user_id;
//	   txt += "\nReg id: "+user_data.google_reg_id;
//	   txt += "\nReg Ver: "+user_data.google_regVer;
//	   txt += "\nGoogle Auth token: "+user_data.google_AuthToken;
//	   log.setText(txt);
//	   
//	   
//	}
//	@SuppressLint("NewApi")
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_splash);
//		//Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Email.CONTENT_URI); /*AccountManager.newChooseAccountIntent(null, null,
//		    /*    new String[] { "com.google" }, true, null, null,
//		        null, null);
//				*/
//				//new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
//		//i.setType(Phone.CONTENT_TYPE);
//		//startActivityForResult(i, 989);
//		user_data = new Common.userData();
//		mActivity = this;
//		mContext = this;
//		tasks = new ArrayList<String>();
//		// load ui
//		status = (TextView) findViewById(R.id.txt_status);
//		progress_tasks = (TextView) findViewById(R.id.txt_tasks);
//		log = (TextView) findViewById(R.id.log);
//		
//		addProgressTask(getString(R.string.loading_comp));
//		addProgressTask(getString(R.string.creating_ui));
//		//progress1 = (ProgressBar) findViewById(R.id.progressBar1);
//		//progress2 = (ProgressBar) findViewById(R.id.progressBar2);
//		//progress3 = (ProgressBar) findViewById(R.id.progressBar3);
//		Shader textShaderTop = new LinearGradient(0, 100, 0, 90, new int[] {
//				Color.parseColor("#eeeeee"), Color.parseColor("#CCCCCC"),
//				Color.parseColor("#BBBBBB") }, null, TileMode.CLAMP);
//
//		progress_tasks.getPaint().setShader(textShaderTop);
//
//		TriggerWaitEvent(Common.LOAD_PREFS);
//
//		addProgressTask(getString(R.string.load_sp));
//
//	}
//
//	private void addProgressTask(String s) {
//		try{
//		status.setText(s);
//		String temp = "";
//		while(tasks.size()>5)
//		{
//			
//			tasks.remove(0);
//		}
//		for (int i = tasks.size() - 1; i >= 0; i--) {
//
//			temp += tasks.get(i) + "\n";
//		}
//
//		tasks.add(s);
//		progress_tasks.setText(temp);
//		}
//		catch(Exception ex)
//		{
//			//addProgressTask("Error: "+ex.getLocalizedMessage());
//		}
//	}
//
//	@SuppressLint("NewApi")
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		switch(requestCode)
//		{
//			case Common.RequestCodes.SPLASH_ACC:
//				String Email = data.getStringExtra(Common.USER_EMAIL);
//				if(Email!=null && resultCode == Activity.RESULT_OK)
//				{	
////					mAuth = new GoogleAuth(mContext, null);
////					mAuth.SetAccount(Email);
//					addProgressTask( getString(R.string.google_result_ok));
//					mSharedPreferencesEditor.putBoolean(Common.USER_IS_SYNC_TYPE, true);
//					mSharedPreferencesEditor.putString(Common.USER_EMAIL, Email);
//					mSharedPreferencesEditor.commit();
//					user_data.email = Email;
//					user_data.is_sync_type = true;
//					//mAuth.execute();
//					TriggerWaitEvent(Common.GOOGLE_AUTH);
//				}	
//				else if(resultCode == Activity.RESULT_CANCELED)
//				{
//					addProgressTask( getString(R.string.google_result_cancel));
//					addProgressTask( getString(R.string.google_disable_sync));
//					mSharedPreferencesEditor.putBoolean(Common.USER_IS_SYNC_TYPE, false);
//					mSharedPreferencesEditor.putString(Common.USER_EMAIL, null);
//					mSharedPreferencesEditor.commit();
//					user_data.email = Email;
//					user_data.is_sync_type = false;
//					TriggerWaitEvent(Common.GO_TO_MAIN);
//					addProgressTask(getString(R.string.complete));
//				}
//				
//
//				//addProgressTask(getString(R.string.go));
//				break;
//			case Common.RequestCodes.SPLASH_AUTH:
//				TriggerWaitEvent(Common.GOOGLE_AUTH);
//				break;
//		}
//		updateLog();
//		
//		
//	}
//	private WaitEventHandler wh = new WaitEventHandler();
//	private void TriggerWaitEvent(int functionToken) {
//		wh.AddRunnable(mActivity, functionToken,Common.SPLASH_TIME_OUT_SMALL);
////		new Common.customPause(mActivity, functionToken,
////				Common.SPLASH_TIME_OUT_SMALL);
//	}
//	@Override
//	public void CheckInternet() {
//
//		if (Common.hasInternet(mActivity)) {
//			progress1.setProgress(40);
//			addProgressTask(getString(R.string.internet_stable));
//			TriggerWaitEvent(Common.CONFIG_GCM);
//			addProgressTask(getString(R.string.checking_other_settings));
//			addProgressTask(getString(R.string.config_gcm));
//
//		} else {
//			addProgressTask(getString(R.string.no_internet));
//			addProgressTask(getString(R.string.require_internet));
//			TriggerWaitEvent(Common.GO_TO_MAIN);
//			addProgressTask(getString(R.string.complete));
//			progress1.setProgress(30);
//		}
//	
//		updateLog();
//	}
//
//	
//
//	@Override
//	public void httpResult(JSONObject data, int RequestCode, int ResultCode) {
//		switch (RequestCode) {
//		case Common.RequestCodes.GRAVITY_REGISTER:
//			
//			if(ResultCode == Common.HTTP_RESPONSE_OK)
//			{
//				data = data.optJSONObject("data");
//				user_data.gravity_user_id =data.optString("UserId"); 
//				addProgressTask(getString(R.string.gravity_registration_complete));
//				mSharedPreferencesEditor
//					.putBoolean(Common.USER_IS_REGISTERED, true);
//				mSharedPreferencesEditor
//				.putString(Common.USER_ID_GRAVITY, user_data.gravity_user_id);
//			}
//			else
//			{	addProgressTask(getString(R.string.gravity_registration_error));
//				mSharedPreferencesEditor
//				.putBoolean(Common.USER_IS_REGISTERED, false);
//			}
//			mSharedPreferencesEditor.commit();
//			TriggerWaitEvent(Common.GRAVITY_SYNC);
//			addProgressTask(getString(R.string.gravity_sync));
//			//TriggerWaitEvent(Common.CONFIG_GCM);
//			//addProgressTask(getString(R.string.checking_other_settings));
//			break;
//		case Common.RequestCodes.GRAVITY_SEND_GCM_CODE:
//			Toast.makeText(this, "Result from gravity for gcm code send request "+ResultCode, Toast.LENGTH_LONG).show();
//			break;
//		case Common.RequestCodes.GRAVITY_GET_TASKLISTS:
//			if(ResultCode == Common.HTTP_RESPONSE_OK)
//			{
//				JSONArray arr_data = data.optJSONArray("data");
//				DatabaseHelper db = new DatabaseHelper(this);
//				for(int i=0;i<arr_data.length();i++)
//				{
//					try {
//						JSONObject temp = arr_data.getJSONObject(i);
//						TaskListModel model = new TaskListModel(temp.getString("Title"));
//						db.TaskList_New(model);
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//				}
//				addProgressTask(getString(R.string.gravity_fetch_data_success));
//			}
//			else
//			{
//				addProgressTask(getString(R.string.gravity_fetch_data_failed));
//			}
//			TriggerWaitEvent(Common.USERS_SYNC);
//			addProgressTask(getString(R.string.users_sync));
//			
//			break;
//		case Common.RequestCodes.GOOGLE_GET_USER_INFO:
//			//save user info
//			try {
//				user_data.name = data.getString("name");
//				mSharedPreferencesEditor
//				.putString(Common.USER_NAME, user_data.name);
//				
//				getAndSaveBitmap(data.getString("picture"));
//				//next step in getAndSaveBitmap function
//				mSharedPreferencesEditor.commit();
//				
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			break;
//		}
//	}
//
//	@Override
//	public void displayMsg(String msg) {
//		addProgressTask(msg);
//	}
//
//	@Override
//	public void storeGCMRegisterationId(String regid, int appVersion) {
//		//addProgressTask("*Saving GCM reg");
//		mSharedPreferencesEditor.putString(Common.GOOGLE_PROPERTY_REG_ID,regid);
//		mSharedPreferencesEditor.putInt(Common.GOOGLE_PROPERTY_APP_VERSION,appVersion);
//		mSharedPreferencesEditor.commit();
//		user_data.google_reg_id = regid;
//		user_data.google_regVer = appVersion;
//		mSharedPreferencesEditor.putString(Common.GOOGLE_PROPERTY_REG_ID, regid);
//		mSharedPreferencesEditor.putInt(Common.GOOGLE_PROPERTY_APP_VERSION, appVersion);
//		
//		mSharedPreferencesEditor.commit();
//		//addProgressTask("Saved GCM reg, app ver: ");
//		
//		//error
//		if(user_data.email == null)
//			{
//				TriggerWaitEvent(Common.GET_ACCOUNT);
//				//addProgressTask(getString(R.string.get_google_acc));
//			}
//			else
//				TriggerWaitEvent(Common.GOOGLE_AUTH);
//		
//		
//		//updateLog();
//	}
//
//	@Override
//	public void LoadPreferences() {
//		// TODO Auto-generated method stub
//		mSharedPreferences = getSharedPreferences(Common.SHARED_PREF_KEY,
//				MODE_MULTI_PROCESS);
//		mSharedPreferencesEditor = mSharedPreferences.edit();
//		user_data.email = mSharedPreferences.getString(Common.USER_EMAIL, null);
//		user_data.is_sync_type = mSharedPreferences.getBoolean(
//				Common.USER_IS_SYNC_TYPE, true);
//		user_data.google_AuthToken = mSharedPreferences.getString(
//				Common.AUTH_TOKEN, null);
//		user_data.gravity_is_registered = mSharedPreferences.getBoolean(
//				Common.USER_IS_REGISTERED, false);
//		user_data.google_reg_id = 
//				mSharedPreferences.getString(Common.GOOGLE_PROPERTY_REG_ID, "");
//		user_data.google_regVer = 
//				mSharedPreferences.getInt(Common.GOOGLE_PROPERTY_APP_VERSION, Integer.MIN_VALUE);
//		user_data.google_reg_id = 
//				mSharedPreferences.getString(Common.GOOGLE_PROPERTY_REG_ID, "");
//		user_data.name = mSharedPreferences.getString(Common.USER_NAME, "");
//		//user_data.image = BitmapFactory.decodeFile( mSharedPreferences.getString(Common.USER_IMAGE, ""));
//		try{
//		user_data.image = Bitmap.createScaledBitmap(BitmapFactory.decodeFile( mSharedPreferences.getString(Common.USER_IMAGE, "")),75, 75, true);
//		}
//		catch(Exception ex)
//		{
//			
//		}
//		//		ImageView v = (ImageView)findViewById(R.id.logo);
////		v.setImageBitmap(user_data.image);
//		TriggerWaitEvent(Common.LOAD_LOCAL_DB);
//		addProgressTask(getString(R.string.load_db));
//		updateLog();
//	}
//	@Override
//	public void GetAccount()
//	{
//		Intent i = new Intent(SplashOldActivity.this,
//				AuthenticationActivity.class);
//		
//		startActivityForResult(i, Common.RequestCodes.SPLASH_ACC);
//	}
//	@Override
//	public void GoogleAuth() {
//		
//		addProgressTask(getString(R.string.google_auth));
//		mAuth = new GoogleAuth(mContext, null);
//		mAuth.SetAccount(user_data.email);
//		mAuth.execute();
//			updateLog();
//	}
//
//	@Override
//	public void LoadLocalDB() {
//		// TODO Auto-generated method stub
//		DatabaseHelper h = new DatabaseHelper(this);
//		
//		
//		/*if (!user_data.is_registered){
//			TriggerWaitEvent(Common.GRAVITY_REGISTER);
//		addProgressTask(getString(R.string.gravity_register));
//			//AccountsController.get_gravity_accounts(Common.RequestCodes.GRAVITY_REGISTER);
//		}*/
//		//else {
//		if(!user_data.is_sync_type)
//		{
//			TriggerWaitEvent(Common.GO_TO_MAIN);
//			addProgressTask(getString(R.string.complete));
//		}
//	else{
//		addProgressTask(getString(R.string.check_internet));
//		TriggerWaitEvent(Common.CHECK_INTERNET);
//	}
//		
//	}
//
//	@Override
//	public void GravityRegister() {
//		GravityController.register_gravity_account(mActivity,
//				user_data.email, user_data.google_reg_id, "", Common.RequestCodes.GRAVITY_REGISTER);
//		//AccountsController.get_gravity_accounts(Common.RequestCodes.GRAVITY_REGISTER);
//	
//	}
//
//	public void GoToMain() {
//		Intent i = new Intent(SplashOldActivity.this, MainActivity.class);
//		i.putExtra("user", user_data);
//		startActivity(i);
//		finish();
//	}
//
//	public void ConfigureGCM() {
//		if (GCMController.checkPlayServices(mContext, mActivity)) {
//			addProgressTask(getString(R.string.gcm_play_service_success));
//	        // If this check succeeds, proceed with normal processing.
//	        // Otherwise, prompt user to get valid Play Services APK.
//			gcm = GoogleCloudMessaging.getInstance(mContext);
//			String tempReg =GCMController.getRegistrationId(getApplicationContext(), user_data); 
//            if (tempReg=="") {
//                GCMController.registerInBackground(mContext,gcm);
//            }
//            else
//            {
//            	if(user_data.email == null)
//    			{
//    				TriggerWaitEvent(Common.GET_ACCOUNT);
//    				//addProgressTask(getString(R.string.get_google_acc));
//    			}
//    			else
//    				TriggerWaitEvent(Common.GOOGLE_AUTH);
//            }
//			//shifted to gcm_save_reg_id
//			//TriggerWaitEvent(Common.LOAD_LOCAL_DB);
//			//addProgressTask(getString(R.string.load_db));
//			
//		}
//		else
//		{
//			
//			addProgressTask(getString(R.string.gcm_invalid_device));
//		}
//		updateLog();
//	}
//	@Override
//	public void AuthResult(Intent i) {
//		if(i.getBooleanExtra(Common.HAS_EXCEPTION, false))
//			//&& i.getIntExtra(Common.EXCEPTION_TYPE,Common.EXCEPTIONS.NoException) != Common.EXCEPTIONS.NoException)
//		{
//			if(i.getIntExtra(Common.EXCEPTION_TYPE,Common.EXCEPTIONS.NoException) != Common.EXCEPTIONS.NoException)
//			{
//				startActivityForResult(i, Common.RequestCodes.SPLASH_AUTH);
//			}
//			
//		}
//
//		else if(i.getStringExtra(Common.AUTH_TOKEN)!=null)
//		{
//			mSharedPreferencesEditor.putString(Common.AUTH_TOKEN,i.getStringExtra(Common.AUTH_TOKEN));
//			user_data.google_AuthToken = i.getStringExtra(Common.AUTH_TOKEN);
//			mSharedPreferencesEditor.commit();
////			TriggerWaitEvent(Common.CONFIG_GCM);
////			addProgressTask(getString(R.string.checking_other_settings));
////			addProgressTask(getString(R.string.config_gcm));
//			if(Common.hasInternet(mActivity))
//			{
//				addProgressTask(getString(R.string.google_get_user_info));
//				
//				TriggerWaitEvent(Common.GOOGLE_USER_INFO);
//			}
//			
//			
//			//runWorker
//		}
//		//updateLog();
//	}
//	
//	
//	@Override
//	public void SyncAppData() {
//		// TODO Auto-generated method stub
//		GravityController.get_tasklists(mActivity, user_data,
//				Common.RequestCodes.GRAVITY_GET_TASKLISTS);
//	}
//	@Override
//	public void GetUserDataFromGoogle() {
//		// TODO Auto-generated method stub
//		HttpTask Temp =  new HttpTask(mActivity, 
//				Common.USER_INFO_URL+user_data.google_AuthToken, null,
//				Common.HttpMethod.HttpGet,
//				Common.RequestCodes.GOOGLE_GET_USER_INFO);
//		Temp.execute();
//	}
//	public void getAndSaveBitmap(final String url)
//    {
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				Bitmap bitmapImage = null;
//		        try 
//		        {
//		            InputStream in = new java.net.URL(url).openStream();
//		            bitmapImage = BitmapFactory.decodeStream(in);
//		            ContextWrapper cw = new ContextWrapper(getApplicationContext());
//		            File directory = cw.getDir("taskdone", Context.MODE_PRIVATE);
//		            if(!directory.exists())
//			            directory.mkdir();
//		            File mypath=new File(directory,"profile.jpg");
//		            if (mypath.exists())
//		            	mypath.delete();
//		            FileOutputStream fos = null;
//		            try {
//		               // fos = openFileOutput(filename, Context.MODE_PRIVATE);
//
//		                fos = new FileOutputStream(mypath);
//
//		                // Use the compress method on the BitMap object to write image to the OutputStream
//		                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//		                fos.flush();
//		                fos.close();
//		            } catch (Exception e) {
//		                e.printStackTrace();
//		            }
//		            //user_data.image = bitmapImage;
//		            user_data.image = Bitmap.createScaledBitmap(bitmapImage,75, 75, true);
//		            mSharedPreferencesEditor
//					.putString(Common.USER_IMAGE, mypath.getAbsolutePath());
//		            mSharedPreferencesEditor.commit();
//		          //reg to gravity
//					if(!user_data.gravity_is_registered)
//					{
//						addProgressTask(getString(R.string.gravity_register));
//						
//						TriggerWaitEvent(Common.GRAVITY_REGISTER);
//						
//					}
//					else
//					{
//						TriggerWaitEvent(Common.GO_TO_MAIN);
//						addProgressTask(getString(R.string.complete));
//					}
//		            //return bitmapImage;
//		        }//End of try block...... 
//		        catch (Exception e) 
//		        {
//		            //status = e.getMessage();
//		            //return null;
//		        }
//			}
//		}).start();
//    }
//	@SuppressLint("NewApi")
//	@Override
//	public void SyncUsers() {
//		ArrayList<HashMap<String, Object>> contacts = getContacts();
//		DatabaseHelper db = new DatabaseHelper(mContext);
//		int size = contacts.size();
//		for (int i = 0; i < size; i++) {
//
//			HashMap<String, Object> contactItem = contacts.get(i);
//
//			ContentValues dataToInsert = new ContentValues();
//
//			UserModel userModel = new UserModel();
//			// userModel.contact_id.("contactId").toString();
//			// userModel.contactItem.get("name").toString();
//			// userModel.contactItem.get("email").toString();
//			// userModel.contactItem.get("address").toString();
//			// userModel.contactItem.get("phone").toString();
//
//			if (contactItem.get("email") != null) {
//
//				if (contactItem.get("photo") != null
//						&& contactItem.get("photo") instanceof Bitmap) {
//
//					Bitmap b = (Bitmap) contactItem.get("photo");
//					int bytes = b.getByteCount();
//
//					ByteBuffer buffer = ByteBuffer.allocate(bytes); // Create a
//																	// new
//																	// buffer
//					b.copyPixelsToBuffer(buffer); // Move the byte data to the
//													// buffer
//					byte[] array = buffer.array();
//
//
//					if (contactItem.get("email").toString()
//							.contains("gmail.com")) { // this is gmail thing
//						UserModel user = new UserModel(contactItem.get("name")
//								.toString(), contactItem.get("email")
//								.toString(), array
//						// contactItem.get("phone").toString()
//						);
//						db.User_New(user);
//					}
//
//				} else {
//
//				}
//
//				
//			}
//		}
//		TriggerWaitEvent(Common.GO_TO_MAIN);
//		addProgressTask(getString(R.string.complete));
//		
//	}
//	public ArrayList<HashMap<String, Object>> getContacts() {
//
//		ArrayList<HashMap<String, Object>> contacts = new ArrayList<HashMap<String, Object>>();
//		final String[] projection = new String[] { RawContacts.CONTACT_ID,
//				RawContacts.DELETED };
//
//		// @SuppressWarnings("deprecation")
//		ContentResolver cr = getContentResolver();
//		Cursor rawContacts = cr.query(RawContacts.CONTENT_URI, projection,
//				null, null, null);
//
//		final int contactIdColumnIndex = rawContacts
//				.getColumnIndex(RawContacts.CONTACT_ID);
//		final int deletedColumnIndex = rawContacts
//				.getColumnIndex(RawContacts.DELETED);
//
//		if (rawContacts.moveToFirst()) {
//			while (!rawContacts.isAfterLast()) {
//				final int contactId = rawContacts.getInt(contactIdColumnIndex);
//				final boolean deleted = (rawContacts.getInt(deletedColumnIndex) == 1);
//				if (!deleted) {
//					HashMap<String, Object> contactInfo = new HashMap<String, Object>() {
//						{
//							put("contactId", "");
//							put("name", "");
//							put("email", "");
//							put("address", "");
//							put("photo", "");
//							put("phone", "");
//						}
//					};
//					contactInfo.put("contactId", "" + contactId);
//					contactInfo.put("name", getName(contactId));
//					contactInfo.put("email", getEmail(contactId));
//					contactInfo.put("photo",
//							getPhoto(contactId) != null ? getPhoto(contactId)
//									: "");
//					contactInfo.put("address", getAddress(contactId));
//					contactInfo.put("phone", getPhoneNumber(contactId));
//					contactInfo.put("isChecked", "false");
//					contacts.add(contactInfo);
//				}
//				rawContacts.moveToNext();
//			}
//		}
//		rawContacts.close();
//		return contacts;
//
//	}
//	private String getName(int contactId) {
//		String name = "";
//		final String[] projection = new String[] { Contacts.DISPLAY_NAME };
//
//		ContentResolver cr = getContentResolver();
//		Cursor contact = cr.query(Contacts.CONTENT_URI, projection,
//				Contacts._ID + "=?",
//				new String[] { String.valueOf(contactId) }, null);
//
//		if (contact.moveToFirst()) {
//			name = contact.getString(contact
//					.getColumnIndex(Contacts.DISPLAY_NAME));
//			contact.close();
//		}
//		contact.close();
//		return name;
//
//	}
//
//	private String getEmail(int contactId) {
//		String emailStr = "";
//		final String[] projection = new String[] { Email.DATA, // use
//				// Email.ADDRESS
//				// for API-Level
//				// 11+
//				Email.TYPE };
//		ContentResolver cr = getContentResolver();
//		Cursor email = cr.query(Email.CONTENT_URI, projection, Data.CONTACT_ID
//				+ "=?", new String[] { String.valueOf(contactId) }, null);
//
//		if (email.moveToFirst()) {
//			final int contactEmailColumnIndex = email
//					.getColumnIndex(Email.DATA);
//
//			while (!email.isAfterLast()) {
//				emailStr = emailStr + email.getString(contactEmailColumnIndex)
//						+ ";";
//				email.moveToNext();
//			}
//		}
//		email.close();
//		return emailStr;
//
//	}
//
//	private Bitmap getPhoto(int contactId) {
//		Bitmap photo = null;
//		final String[] projection = new String[] { Contacts.PHOTO_ID };
//		ContentResolver cr = getContentResolver();
//		Cursor contact = cr.query(Contacts.CONTENT_URI, projection,
//				Contacts._ID + "=?",
//				new String[] { String.valueOf(contactId) }, null);
//
//		if (contact.moveToFirst()) {
//			final String photoId = contact.getString(contact
//					.getColumnIndex(Contacts.PHOTO_ID));
//			if (photoId != null) {
//				photo = getBitmap(photoId);
//			} else {
//				photo = null;
//			}
//		}
//		contact.close();
//
//		return photo;
//	}
//
//	private Bitmap getBitmap(String photoId) {
//		ContentResolver cr = getContentResolver();
//		Cursor photo = cr.query(Data.CONTENT_URI, new String[] { Photo.PHOTO },
//				Data._ID + "=?", new String[] { photoId }, null);
//
//		final Bitmap photoBitmap;
//		if (photo.moveToFirst()) {
//			byte[] photoBlob = photo.getBlob(photo.getColumnIndex(Photo.PHOTO));
//			photoBitmap = BitmapFactory.decodeByteArray(photoBlob, 0,
//					photoBlob.length);
//		} else {
//			photoBitmap = null;
//		}
//
//		photo.close();
//		//saveImage(photoBitmap);
//		return photoBitmap;
//	}
//	private String getAddress(int contactId) {
//		String postalData = "";
//		String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND "
//				+ ContactsContract.Data.MIMETYPE + " = ?";
//		String[] addrWhereParams = new String[] {
//				String.valueOf(contactId),
//				ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };
//		ContentResolver cr = getContentResolver();
//		Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI, null,
//				addrWhere, addrWhereParams, null);
//
//		if (addrCur.moveToFirst()) {
//			postalData = addrCur
//					.getString(addrCur
//							.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
//		}
//		addrCur.close();
//		return postalData;
//	}
//
//	private String getPhoneNumber(int contactId) {
//
//		String phoneNumber = "";
//		final String[] projection = new String[] { Phone.NUMBER, Phone.TYPE, };
//		ContentResolver cr = getContentResolver();
//		Cursor phone = cr.query(Phone.CONTENT_URI, projection, Data.CONTACT_ID
//				+ "=?", new String[] { String.valueOf(contactId) }, null);
//
//		if (phone.moveToFirst()) {
//			final int contactNumberColumnIndex = phone
//					.getColumnIndex(Phone.DATA);
//
//			while (!phone.isAfterLast()) {
//				phoneNumber = phoneNumber
//						+ phone.getString(contactNumberColumnIndex) + ";";
//				phone.moveToNext();
//			}
//
//		}
//		phone.close();
//		return phoneNumber;
//	}
//	@Override
//	public void GoToMainThread() {
//		// TODO Auto-generated method stub
//		
//	}
//	@Override
//	public void onServiceBound(AppHandlerService handleService) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	@Override
//	public void startResultActivity(Intent intent, int RequestCode) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	//gcm functions
//	
//	
//	
//	
//}
