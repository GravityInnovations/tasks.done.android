package com.gravity.innovations.tasks.done;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.google.android.gms.internal.ey;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.Time;
import android.util.Log;

//FAIK malik
public class DatabaseHelper extends SQLiteOpenHelper {
	
	public Tasks tasks = new Tasks();
	public TaskLists tasklists = new TaskLists();
	public Users users = new Users();
	private UsersLists userlists = new UsersLists();
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "task_done_db";

	// ******** TABLE NAMES *********//
	// TaskModel table
	// task has Auto inc. id, title, details and notes
	private static final String TABLE_TASKS = "tasks";
	// TaskListModel table
	// task list has Auto inc. id and a title
	private static final String TABLE_TASK_LIST = "task_lists";
	// Users table
	// user has Auto inc. id name and a email
	private static final String TABLE_USERS = "users";
	private static final String TABLE_USERS_LISTS = "users_lists";
	// ******** TABLE NAMES *********//

	// ******** Columns Entries *********//
	protected static final String KEY_PK = "_id";
	protected static final String KEY_TITLE = "title";
	protected static final String KEY_DETAILS = "details";
	protected static final String KEY_NOTES = "notes";
	protected static final String KEY_FK_TASKLIST_ID = "fk_tasklist_id";
	protected static final String KEY_FK_USER_ID = "fk_user_id";
	protected static final String KEY_USER_NAME = "user_name";
	protected static final String KEY_USER_EMAIL = "user_email";
	// protected static final String KEY_USER_PHONE_NUM = "user_phone";

	protected static final String KEY_SERVER_ID = "server_id";
	protected static final String KEY_PARENT = "parent";
	protected static final String KEY_STATUS = "status";
	protected static final String KEY_COMPLETED = "completed";
	protected static final String KEY_DELETED = "deleted";
	protected static final String KEY_HIDDEN = "hidden";
	protected static final String KEY_LINKS = "links";
	protected static final String KEY_SYNC_STATUS = "sync_status";
	protected static final String KEY_SYNC_STATUS_TIMESTAMP = "sync_status_timestamp";
	protected static final String KEY_SELF_LINK = "self_link";
	protected static final String KEY_USER_IMAGE = "user_image";

	// Table
	protected static final String KEY_DISPLAY_NAME = "display_name";
	protected static final String KEY_CONTACT_ID = "contact_id";

	protected static final String KEY_UPDATED_AT = "updated_date_time";
	// protected static final String KEY_DUE_AT = "due_date_tTime";
	// protected static final String KEY_COMPLETED_AT = "completed_date_time";

	protected static final String KEY_REMIND_AT = "remind_date_time";
	protected static final String KEY_REMIND_INTERVAL = "remind_interval";

	protected static final String KEY_ETAG = "etag";
	protected static final String KEY_USER_ID = "user_id";
	protected static final String KEY_KIND = "kind";
	protected static final String KEY_LIST_TYPE = "icon_identifier";

	protected static final String KEY_ALARM_ID = "alarm_identifier";
	protected static final String KEY_ALARM_STATUS = "alarm_status";
	protected static final String KEY_ALARM_WEEKDAY = "alarm_weekday";

	protected static final String KEY_ASSOCIATED_USERMODELS = "associated_usermodel";
	// ******** Columns Entries *********//

	// ********* SQLite Table Structure Queries *********//
	
	

	
	

	// ********* SQLite Table Structure Queries *********//

	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mSharedPreferencesEditor;
	private AppHandlerService service = null;
	public DatabaseHelper(Context mContext) {
		
		super(mContext, DATABASE_NAME, null, DATABASE_VERSION);
		
		try{
		service = (AppHandlerService)mContext;
		
		
		}catch(Exception e)
		{
			service = null;
		}
		//mSharedPreferences = mContext.getSharedPreferences(Common.SHARED_PREF_KEY,
//				Context.MODE_MULTI_PROCESS);
//		mSharedPreferencesEditor = mSharedPreferences.edit();
	}

	// ********* Creating Tables *********//
	@Override
	public void onCreate(SQLiteDatabase db) {
		String _TAG = "DbHelper onCreate";
		try {
			db.execSQL(Users.CREATE_USERS_TABLE);
		} catch (Exception e) {
			Log.e(_TAG, "USERS_TABLE not created");
		}
		try {
			db.execSQL(TaskLists.CREATE_TASK_LIST_TABLE);
		} catch (Exception e) {
			Log.e(_TAG, "TASK_LIST_TABLE not created");
		}
		
		try {
			db.execSQL(Tasks.CREATE_TASKS_TABLE);
		} catch (Exception e) {
			Log.e(_TAG, "TASKS_TABLE not created");
		}
		
		
		try {
			db.execSQL(UsersLists.CREATE_USERS_LISTS_TABLE);
		} catch (Exception e) {
			Log.e(_TAG, "CREATE_USERS_LISTS_TABLE not created");
		}
	}

	// ********* Creating Tables *********//

	// ********* Upgrading database *********//
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		try {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK_LIST);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS_LISTS);
			onCreate(db);// create all tables again
		} catch (Exception e) {
			Log.e("Database onUpgrade", "not created Again");
		}
	}

	// ********* Upgrading database *********//

	/**
	 * All CRUD FOR TASK(Create, Read, Update, Delete) Operations
	 */
	
	
	protected class Tasks{
		protected static final String CREATE_TASKS_TABLE = "CREATE TABLE "
				+ TABLE_TASKS
				+ "("
				+ KEY_PK+ " INTEGER PRIMARY KEY AUTOINCREMENT,"// 0
				+ KEY_TITLE+ " TEXT,"
				+ KEY_DETAILS+ " TEXT,"
				+ KEY_NOTES+ " TEXT,"

				+ KEY_UPDATED_AT
				+ " DATETIME," // time long
				
				+ KEY_REMIND_AT+ " DATETIME," // time long
				+ KEY_REMIND_INTERVAL+ " INTEGER," // values 0-3
				/*+ KEY_ALARM_ID + " INTEGER,"*/
				+ KEY_ALARM_STATUS + " INTEGER,"
				+ KEY_ALARM_WEEKDAY + " INTEGER,"
				
				+ KEY_SERVER_ID + " TEXT,"
				+ KEY_COMPLETED + " INTEGER," 
				+ KEY_SYNC_STATUS
				+ " TEXT,"
				+ KEY_SYNC_STATUS_TIMESTAMP
				+ " DATETIME,"// 15
				
				+ KEY_FK_TASKLIST_ID + " INTEGER," + " FOREIGN KEY ("
				+ KEY_FK_TASKLIST_ID + ")" + "REFERENCES " + TABLE_TASK_LIST + "("
				+ KEY_PK + ")" + ")";
		
		private ContentValues setContent(TaskModel task)
		{
			ContentValues values = new ContentValues();
			
			values.put(KEY_TITLE, task.title);
			values.put(KEY_DETAILS, task.details);
			values.put(KEY_NOTES, task.notes);
			values.put(KEY_UPDATED_AT, task.updated);
			values.put(KEY_REMIND_AT, task.remind_at);
			values.put(KEY_REMIND_INTERVAL, task.remind_interval);
			values.put(KEY_SERVER_ID, task.server_id);
			values.put(KEY_COMPLETED, task.completed);
			values.put(KEY_SYNC_STATUS, task.syncStatus);
			values.put(KEY_SYNC_STATUS_TIMESTAMP, task.syncStatusTimeStamp);
			values.put(KEY_ALARM_STATUS, task.alarm_status);
			values.put(KEY_ALARM_WEEKDAY, task.weekday);
			values.put(KEY_FK_TASKLIST_ID, task.fk_tasklist_id);
			return values;
		}
		private TaskModel getValuesFromCursor(Cursor c)
		{
			int colID = c.getColumnIndex(KEY_PK);
			int colTitle = c.getColumnIndex(KEY_TITLE);
			int colDetails = c.getColumnIndex(KEY_DETAILS);
			int colNotes = c.getColumnIndex(KEY_NOTES);
			int colUpdated = c.getColumnIndex(KEY_UPDATED_AT);
			int colRemindAt = c.getColumnIndex(KEY_REMIND_AT);
			int colRemindInt = c.getColumnIndex(KEY_REMIND_INTERVAL);
			int colServerId = c.getColumnIndex(KEY_SERVER_ID);
			int colCompleted = c.getColumnIndex(KEY_COMPLETED);
			int colSyncStatus = c.getColumnIndex(KEY_SYNC_STATUS);
			int colSyncTime = c.getColumnIndex(KEY_SYNC_STATUS_TIMESTAMP);
			int colAlarmStatus = c.getColumnIndex(KEY_ALARM_STATUS);
			int colAlarmWeekday = c.getColumnIndex(KEY_ALARM_WEEKDAY);
			int colFKtasklistid = c.getColumnIndex(KEY_FK_TASKLIST_ID);
			TaskModel task = new TaskModel();
			task._id = c.getInt(colID);
			task.title = c.getString(colTitle);
			task.details = c.getString(colDetails);
			task.notes = c.getString(colNotes);
			task.updated = c.getString(colUpdated);
			task.remind_at = c.getString(colRemindAt);
			task.remind_interval = c.getInt(colRemindInt);
			task.server_id = c.getString(colServerId);
			task.completed = c.getInt(colCompleted);
			task.syncStatus = c.getString(colSyncStatus);
			task.syncStatusTimeStamp = c.getString(colSyncTime);
			task.alarm_status = c.getInt(colAlarmStatus);
			task.weekday = c.getInt(colAlarmWeekday);
			task.fk_tasklist_id = c.getInt(colFKtasklistid);
			return task;
		}
		public int Add(TaskModel task)
		{
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = setContent(task);
			
			int id = (int) db.insert(TABLE_TASKS, null, values);
			db.close();
			return id;
		}
		public boolean Delete(int id) {
			// it will take input from TaskList_Delete
			// and delete all tasks against that list
			SQLiteDatabase db = getWritableDatabase();
			if(db.delete(TABLE_TASKS, KEY_PK + " = ?",
					new String[] { String.valueOf(id) })>0)
				{
				db.close();
				return true;
				
				}
			db.close();
			return false;
		}
		public int Edit(TaskModel task) {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues values = setContent(task);

			return db.update(TABLE_TASKS, values, KEY_PK + " = ?",
					new String[] { String.valueOf(task._id) });// updating row
		}
		
		public TaskModel Get(int id)
		{
			SQLiteDatabase db = getReadableDatabase();
			String query = "SELECT * FROM "+TABLE_TASKS+" WHERE " + KEY_PK +" = "+ id;
			Cursor cursor = db.rawQuery(query, null);//query(query);
//					TABLE_TASKS, new String[] { KEY_PK, KEY_TITLE,
//					KEY_DETAILS, KEY_NOTES,
//
//					KEY_UPDATED_AT, KEY_REMIND_AT, KEY_ALARM_ID, KEY_ALARM_STATUS,
//					KEY_ALARM_WEEKDAY,
//
//					KEY_REMIND_INTERVAL, KEY_FK_TASKLIST_ID }, KEY_PK + "=?",
					//new String[] { String.valueOf(id) }, null, null, null, null);
			if (cursor != null)
				cursor.moveToFirst();
			
			TaskModel task = getValuesFromCursor(cursor);
			// return TaskModel
			cursor.close();
			db.close();
			return task;
		}
		public TaskModel Get(String ServerId)
		{
			SQLiteDatabase db = getReadableDatabase();
			String query = "SELECT * FROM "+TABLE_TASKS+" WHERE " + KEY_SERVER_ID +" = "+ ServerId;
			Cursor cursor = db.rawQuery(query, null);//query(query);

			if (cursor != null)
				cursor.moveToFirst();
			
			TaskModel task = getValuesFromCursor(cursor);
			// return TaskModel
			cursor.close();
			db.close();
			return task;
		}
		public ArrayList<TaskModel> Get(TaskListModel tasklist)
		{
			//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			
			ArrayList<TaskModel> data = new ArrayList<TaskModel>();
			String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE "
					+ KEY_FK_TASKLIST_ID + " = " + tasklist._id +
					// " ORDER BY updated_date_time DESC"
					// " ORDER BY completed, "
					" ORDER BY completed, updated_date_time DESC";

			SQLiteDatabase db = getWritableDatabase();

			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					data.add(getValuesFromCursor(cursor));
				} while (cursor.moveToNext());
			}
			return data;
		}
		public ArrayList<TaskModel> Get()
		{
			//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			
			ArrayList<TaskModel> data = new ArrayList<TaskModel>();
			String selectQuery = "SELECT  * FROM " + TABLE_TASKS 
					+
					" ORDER BY completed, updated_date_time DESC";

			SQLiteDatabase db = getWritableDatabase();

			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					data.add(getValuesFromCursor(cursor));
				} while (cursor.moveToNext());
			}
			return data;
		}
	}
	
	protected class TaskLists{
		private static final String KEY_FK_USER_ID = "fk_user_id";
		private static final String KEY_LIST_COLOR = "list_color";
		protected static final String CREATE_TASK_LIST_TABLE = "CREATE TABLE "
				+ TABLE_TASK_LIST + "(" + KEY_PK
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
				+ KEY_SERVER_ID + " TEXT," + KEY_UPDATED_AT
				+ " DATETIME,"  + KEY_SYNC_STATUS + " TEXT,"
				+ KEY_SYNC_STATUS_TIMESTAMP + " DATETIME," + KEY_LIST_TYPE
				+ " INTEGER," 
				+ KEY_LIST_COLOR + " TEXT,"
				
				+ KEY_FK_USER_ID +" INTEGER,"
				+" FOREIGN KEY ("
				+ KEY_FK_USER_ID + ")" + "REFERENCES " + TABLE_USERS + "("
				+ KEY_PK + ")"
				
				+ ")";
		
		private ContentValues setContent(TaskListModel temp)
		{
			ContentValues values = new ContentValues();
			values.put(KEY_TITLE, temp.title);
			values.put(KEY_SERVER_ID, temp.server_id);
			values.put(KEY_UPDATED_AT, temp.updated);
			values.put(KEY_SYNC_STATUS, temp.syncStatus);
			values.put(KEY_SYNC_STATUS_TIMESTAMP, temp.syncStatusTimeStamp);
			values.put(KEY_LIST_TYPE, temp.icon_identifier);	
			values.put(KEY_LIST_COLOR, temp.fragmentColor);
			values.put(KEY_FK_USER_ID, temp.user_id);
			return values;
		}
		private TaskListModel getValuesFromCursor(Cursor c)
		{
			//list
			int colId = c.getColumnIndex(KEY_PK);
			int colTitle = c.getColumnIndex(KEY_TITLE);
			int colServerID = c.getColumnIndex(KEY_SERVER_ID);
			int colUpdatedAt = c.getColumnIndex(KEY_UPDATED_AT);
			int colUserID = c.getColumnIndex(KEY_FK_USER_ID);
			int colSyncStatus = c.getColumnIndex(KEY_SYNC_STATUS);
			int colSyncStatusTimeStamp = c.getColumnIndex(KEY_SYNC_STATUS_TIMESTAMP);
			int colFragmentColor = c.getColumnIndex(KEY_LIST_COLOR);
			int colListType = c.getColumnIndex(KEY_LIST_TYPE);
			TaskListModel temp = new TaskListModel();
			temp._id = c.getInt(colId);
			temp.title = c.getString(colTitle);
			temp.server_id = c.getString(colServerID);
			temp.updated = c.getString(colUpdatedAt);
			temp.user_id = c.getInt(colUserID);
			temp.syncStatus = c.getString(colSyncStatus);
			temp.syncStatusTimeStamp = c.getString(colSyncStatusTimeStamp);
			temp.fragmentColor = c.getString(colFragmentColor);
			temp.icon_identifier = c.getInt(colListType);
			temp.users = new ArrayList<UserModel>();//users.Get(temp);
			temp.tasks = tasks.Get(temp);
			return temp;
		}
		public int Add(TaskListModel tasklist)
		{
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = setContent(tasklist);
			int id = (int) db.insert(TABLE_TASK_LIST, null, values);
			tasklist._id = id;
			db.close();
			if(id!=-1 && tasklist.server_id == "" && service.hasInternet && service.user_data.is_sync_type)
			{
				GravityController.post_tasklist(service, service.user_data, tasklist, -1);
			}
			return id;
		}
		public boolean Delete(int id) {
			SQLiteDatabase db = getWritableDatabase();
			
			if(db.delete(TABLE_TASK_LIST, KEY_PK + " = ?",
					new String[] { String.valueOf(id) })>0)
				{
				db.close();
				return true;
				
				}
			db.close();
			return false;
		}
		public int Edit(TaskListModel tasklist) {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues values = setContent(tasklist);
			return db.update(TABLE_TASK_LIST, values, KEY_PK + " = ?",
					new String[] { String.valueOf(tasklist._id) });
		}
	
		public ArrayList<TaskListModel> Get() {
			ArrayList<TaskListModel> data = new ArrayList<TaskListModel>();
			try {
				// Select All Query
				String selectQuery = "SELECT  * FROM " + TABLE_TASK_LIST;

				SQLiteDatabase db = getWritableDatabase();

				Cursor cursor = db.rawQuery(selectQuery, null);
				
				// looping through all rows and adding to list
				if (cursor.moveToFirst()) {
					do {
						
						data.add(getValuesFromCursor(cursor));
					} while (cursor.moveToNext());
				}
				cursor.close();
				db.close();
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("all_TaskTitleList", "DBHelper GetTaskTitleList" + e);
			}
			return data;

		}
		public TaskListModel Get(int id) {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db.query(TABLE_TASK_LIST,null, KEY_PK + "=?", new String[] { String.valueOf(id) }, null, null,
					null, null);
			if (cursor != null)
				cursor.moveToFirst();

			TaskListModel tasklist = getValuesFromCursor(cursor);
			// return TaskModel
			cursor.close();
			db.close();
			return tasklist;
		}
		public TaskListModel Get(String ServerId) {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db.query(TABLE_TASK_LIST,null, KEY_SERVER_ID + "=?", new String[] { ServerId }, null, null,
					null, null);
			if (cursor != null)
				cursor.moveToFirst();

			TaskListModel tasklist = getValuesFromCursor(cursor);
			// return TaskModel
			cursor.close();
			db.close();
			return tasklist;
		}
	}
	
	protected class Users{
		
		private static final String CREATE_USERS_TABLE = "CREATE TABLE "
				+ TABLE_USERS + "(" + KEY_PK
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_NAME + " TEXT,"
				+ KEY_USER_EMAIL + " TEXT," + KEY_SERVER_ID + " TEXT,"
				+ KEY_USER_IMAGE + " BLOB," + KEY_CONTACT_ID + " TEXT,"
				+ KEY_DISPLAY_NAME + " TEXT" + ")";
		private ContentValues setContent(UserModel temp)
		{	//UserModel
			ContentValues values = new ContentValues();
			values.put(KEY_USER_NAME, temp.name);
			values.put(KEY_USER_EMAIL, temp.email);
			values.put(KEY_SERVER_ID, temp.server_id);
			values.put(KEY_USER_IMAGE, temp.image);
			values.put(KEY_CONTACT_ID, temp.contact_id);
			values.put(KEY_DISPLAY_NAME, temp.displayName);	 
			return values;
		}
		private UserModel getValuesFromCursor(Cursor c)
		{
			int colId = c.getColumnIndex(KEY_PK);
			int colName = c.getColumnIndex(KEY_USER_NAME);
			int colDisplayName = c.getColumnIndex(KEY_DISPLAY_NAME);
			int colEmail = c.getColumnIndex(KEY_USER_EMAIL);
			int colServerID = c.getColumnIndex(KEY_SERVER_ID);
			int colImage= c.getColumnIndex(KEY_USER_IMAGE);
			int colContactID= c.getColumnIndex(KEY_CONTACT_ID);
			 
			UserModel temp = new UserModel();
			temp._id = c.getInt(colId);
			temp.name = c.getString(colName);
			temp.displayName = c.getString(colDisplayName);
			temp.email = c.getString(colEmail);
			temp.server_id = c.getString(colServerID);
			temp.image = c.getBlob(colImage);
			temp.contact_id = c.getString(colContactID);
			return temp;
			
		}
		public int Add(UserModel user)
		{
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = setContent(user);

			int id = (int) db.insert(TABLE_USERS, null, values);
			db.close();
//			if(service!=null)
//			{
//				GravityController.
//			}
//			if(id!=-1){
//				String PendingServerUsers = mSharedPreferences.getString(SharedPreferencesHelper.Pending_Sync_Users, "");
//				
//				if(PendingServerUsers!="")
//				PendingServerUsers+=","+id;
//				else if(PendingServerUsers == "")
//					PendingServerUsers=id+"";
//				
//				mSharedPreferencesEditor.putString(SharedPreferencesHelper.Pending_Sync_Users, PendingServerUsers);
//			}
			
			return id;
		}
		
		public ArrayList<Common.CustomViewsData.MultiSelectRowData> GetRows()
		{
			ArrayList<Common.CustomViewsData.MultiSelectRowData> users = new ArrayList<Common.CustomViewsData.MultiSelectRowData>();
			for(UserModel user:this.Get())
			{
				Common.CustomViewsData.MultiSelectRowData u = new Common.CustomViewsData.MultiSelectRowData();
				if(user.name!=null && user.name!="")
				u.text1 = user.name;
				else
					u.text1 = user.displayName;
				u.text2 = user.email;
				u.iconRes = user.image;
			}
				return users;
			
		}
		public int ServerValidate(UserModel user)
		{
			SQLiteDatabase db = getWritableDatabase();

			ContentValues values = setContent(user);
			//values.put(KEY_USER_EMAIL, user.email);

			
					int i = db.update(TABLE_USERS, values, KEY_PK  + " = ?",
					new String[] { String.valueOf(user._id) });// updating row
					return i;
		}
		public int Edit(UserModel user) {
			SQLiteDatabase db = getWritableDatabase();

			ContentValues values = setContent(user);
			return db.update(TABLE_USERS, values, KEY_PK + " = ?",
					new String[] { String.valueOf(user._id) });// updating row
		}
		public void Delete(int id) {
			SQLiteDatabase db = getWritableDatabase();
			db.delete(TABLE_USERS, KEY_PK + " = ?",
					new String[] { String.valueOf(id) });
			db.close();
		}
		public void Delete(UserModel user) {
			Delete(user._id);
		}
		public void DeleteAll() {
			SQLiteDatabase db = getWritableDatabase();
			db.delete(TABLE_USERS, null, null);
			db.close();
		}
		public UserModel Get(String ServerId) {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db.query(TABLE_USERS,null, KEY_SERVER_ID + "=?", new String[] { ServerId }, null, null,
					null, null);
			UserModel user = null;
			if (cursor != null){
				cursor.moveToFirst();

			user = getValuesFromCursor(cursor);
			}
			// return TaskModel
			cursor.close();
			db.close();
			return user;
		}
		public ArrayList<UserModel> Get() {
			ArrayList<UserModel> data = new ArrayList<UserModel>();
			try {
				// Select All Query
				String selectQuery = "SELECT  * FROM " + TABLE_USERS;

				SQLiteDatabase db = getWritableDatabase();

				Cursor cursor = db.rawQuery(selectQuery, null);
				
				// looping through all rows and adding to list
				if (cursor.moveToFirst()) {
					do {
						UserModel userModel = getValuesFromCursor(cursor);
						data.add(userModel);
					} while (cursor.moveToNext());
				}
				cursor.close();
				db.close();
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("all_TaskTitleList", "DBHelper GetTaskTitleList" + e);
			}
			return data;

		}
		private ArrayList<UserModel> Get(ArrayList<Integer> ids, double alpha) {
			ArrayList<UserModel> data = new ArrayList<UserModel>();
			for (UserModel user : this.Get()) {
				for (Integer i : ids) {
					int tempid = user._id;// cursor.getInt(col_pk);//Integer.parseInt(cursor.getString(col_pk));
					if (i == tempid || i.equals(tempid)) {
						user.image_alpha = alpha;
						
						data.add(user);
						break;
					}
				}
			}
			return data;

		}
		public ArrayList<UserModel> Get(TaskListModel tasklist) {
			ArrayList<Integer> users_ids1 = new ArrayList<Integer>();
			ArrayList<Integer> users_ids2 = new ArrayList<Integer>();
			SQLiteDatabase db = getReadableDatabase();
			String selectQuery = "SELECT  * FROM " + TABLE_USERS_LISTS + " WHERE "
					+ KEY_FK_TASKLIST_ID + " = " + tasklist._id;

			Cursor cursor = db.rawQuery(selectQuery, null);
			int col_userid = cursor.getColumnIndex(KEY_FK_USER_ID);
			int col_sync = cursor.getColumnIndex(KEY_SYNC_STATUS);
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					// Adding TaskModel to list
					if(cursor.getString(col_sync) == "Synced")
					users_ids1.add(cursor.getInt(col_userid));
					else
						users_ids2.add(cursor.getInt(col_userid));
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
			ArrayList<UserModel> users = new ArrayList<UserModel>();
			
			if (users_ids1.size() > 0) {
				//String[] ids = users_ids.toArray(new String[users_ids.size()]);
				users.addAll(this.Get(users_ids1,1.0));
				 // call to a function for returning
										// userModelss
			} 
			if (users_ids2.size() > 0) {
				//String[] ids = users_ids.toArray(new String[users_ids.size()]);
				users.addAll(this.Get(users_ids2,0.8)); // call to a function for returning
										// userModelss
			} 
			
			
				return users;
			
		}
		private int Share(TaskListModel tasklist, UserModel user) {
			// error, wrong foreign key
			SQLiteDatabase db = getWritableDatabase();
			
			ContentValues values = userlists.setContent(new UsersListsModel(user._id, tasklist._id,"unsynced"));//new ContentValues();
			
			int id = (int) db.insert(TABLE_USERS_LISTS, null, values);
			db.close();
			return id;
		}
		
		public int Share(TaskListModel tasklist, ArrayList<UserModel> users) {
			// error, wrong foreign key
			int id = -1;
			String dataids = "";
			for(UserModel user:users){
				id = this.Share(tasklist, user);
				if(id!=-1)
					dataids += id+",";
				else
					users.remove(user);
			}
			if(tasklist.server_id != null && tasklist.server_id != "" && service.hasInternet && service.user_data.is_sync_type)
			{
				GravityController.share_tasklist(service, service.user_data, tasklist, users, -1, "add");//, dataids);
				
				//.post_tasklist(service, service.user_data, tasklist, -1);

				
			}
			
			
			
			return id;
		}
		
		public int Share_ServerValidate(int TasklistId, int UserId) {
			// error, wrong foreign key
			SQLiteDatabase db = getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put(KEY_SYNC_STATUS, "Synced");
			
		
			int i = db.update(TABLE_USERS_LISTS, values, KEY_USER_ID + " = ? AND "+ KEY_FK_TASKLIST_ID + " = ?",
					new String[] {  UserId+"",TasklistId+"" });
			db.close();
			return i;
			
			
		}
		public int Share_ServerValidate(String TaskListServerId, String UserServerId)
		{
			TaskListModel tasklist = tasklists.Get(TaskListServerId);
			UserModel user = users.Get(UserServerId);
			int i = -1;
			if(tasklist!=null && user !=null)
			{
				i = Share_ServerValidate(tasklist._id, user._id);
			}
			return i;
		}
		public void Share_Remove(TaskListModel tasklist, ArrayList<UserModel> users) {
//			SQLiteDatabase db = this.getWritableDatabase();
//			
//			for(UserModel user:users){
//			String selectQuery = "DELETE FROM " + TABLE_USERS_LISTS + " WHERE "
//					+ KEY_USER_ID + " = " + user._id + " AND " + KEY_FK_TASKLIST_ID
//					+ " = " + tasklist._id + " ";
//			db.execSQL(selectQuery);
//			}
//			db.close();
			if(tasklist.server_id != null && tasklist.server_id != "" && service.hasInternet && service.user_data.is_sync_type)
			{
				GravityController.share_tasklist(service, service.user_data, tasklist, users, -1, "delete");//, dataids);
				
				//.post_tasklist(service, service.user_data, tasklist, -1);

				
			}
			
		}
		public void Share_Remove_ServerValidate(String tasklistServerId, String userServerId) {
			TaskListModel tasklist = tasklists.Get(tasklistServerId);
			UserModel user = users.Get(userServerId);
			if(tasklist!=null && user !=null)
			{
				Share_Remove_ServerValidate(tasklist._id, user._id);
			}
		

			
		}
		public void Share_Remove_ServerValidate(int tasklistid, int userid) {
			SQLiteDatabase db = getWritableDatabase();
			
			String selectQuery = "DELETE FROM " + TABLE_USERS_LISTS + " WHERE "
					+ KEY_FK_USER_ID + " = " + userid + " AND " + KEY_FK_TASKLIST_ID
					+ " = " + tasklistid + " ";
			db.execSQL(selectQuery);
			
			db.close();

			
		}
	}
	
	protected class UsersLists
	{
		
		protected static final String CREATE_USERS_LISTS_TABLE = "CREATE TABLE "+ TABLE_USERS_LISTS + "(" +
				KEY_SYNC_STATUS + " TEXT,"+
				
				KEY_FK_USER_ID + " INTEGER,"+
				KEY_FK_TASKLIST_ID  + " INTEGER,"+
				"PRIMARY KEY ("+ KEY_FK_USER_ID + ","+ KEY_FK_TASKLIST_ID +"),"
				
				/*+ KEY_FK_TASKLIST_ID + " INTEGER,"*/ 
				+ "FOREIGN KEY ("
				+ KEY_FK_USER_ID + ")" + "REFERENCES " + TABLE_USERS + "("+ KEY_PK + ")," 
				
				+ "FOREIGN KEY ("
				+ KEY_FK_TASKLIST_ID + ")" + "REFERENCES " + TABLE_TASK_LIST + "("+ KEY_PK + ")" 
				+ ")";
		
		private ContentValues setContent(UsersListsModel temp)
		{	//UserModel
			ContentValues values = new ContentValues();
			values.put(KEY_FK_USER_ID, temp.user_id);
			values.put(KEY_FK_TASKLIST_ID, temp.tasklist_id);
			values.put(KEY_SYNC_STATUS, temp.SyncStatus);
			return values;
		}
		private UsersListsModel getValuesFromCursor(Cursor c)
		{
			int coluId = c.getColumnIndex(KEY_FK_USER_ID);
			int collId = c.getColumnIndex(KEY_FK_TASKLIST_ID);
			int colSyncStatus = c.getColumnIndex(KEY_SYNC_STATUS);
			UsersListsModel temp = new UsersListsModel();
			temp.user_id = c.getInt(coluId);
			temp.tasklist_id = c.getInt(collId);
			temp.SyncStatus = c.getString(colSyncStatus);//"Synced";
			return temp;
			
		}
		
	
	}
//	public ArrayList<Common.CustomViewsData.MultiSelectRowData> Get_Users() {
//
//		ArrayList<Common.CustomViewsData.MultiSelectRowData> users = new ArrayList<Common.CustomViewsData.MultiSelectRowData>();
//
//		try {
//			// Select All Query
//			String selectQuery = "SELECT  * FROM " + TABLE_USERS;
//
//			SQLiteDatabase db = this.getWritableDatabase();
//			Cursor cursor = db.rawQuery(selectQuery, null);
//
//			// looping through all rows and adding to list
//			if (cursor.moveToFirst()) {
//				do {
//					Common.CustomViewsData.MultiSelectRowData user = new Common.CustomViewsData.MultiSelectRowData();
//					user.text1 = (cursor.getString(1));
//					user.text2 = (cursor.getString(2));
//					user.iconRes = (cursor.getBlob(4));
//					
//					// Adding Task to list
//					users.add(user);
//				} while (cursor.moveToNext());
//			}
//
//			return users;
//
//			// cursor.close();
//			// db.close();
//			// for (int i = 0; i < 30; i++) {
//			// Common.CustomViewsData.MultiSelectRowData user = new
//			// Common.CustomViewsData.MultiSelectRowData();
//			// user.text1 = "h";
//			// user.text2 = "some";
//			// user.iconRes = R.drawable.ic_launcher;
//			// // Adding Task to list
//			// users.add(user);
//			// }
//			// // return users;
//			// } catch (Exception e) {
//			// // TODO: handle exception
//			// Log.e("all_Task", "" + e);
//			// }
//
//			// if (cursor.moveToFirst()) {
//			// do {
//
//			// Common.CustomViewsData.MultiSelectRowData user = new
//			// Common.CustomViewsData.MultiSelectRowData();
//			// user.text1 = (cursor.getString(2));
//			// user.text2 = (cursor.getString(1));
//			// user.iconRes = (cursor.getBlob(4));
//			// // Adding TaskModel to list
//			// users.add (user);
//			// } while (cursor.moveToNext());
//			// }
//
//			//
//		} catch (Exception e) {
//			Log.e("", "");
//
//		} finally {
//			return users;
//		}
//	}
//
//	/**
//	 * All CRUD FOR Users(Create, Read, Update, Delete) Operations
//	 */
//
//	// Add a user
//	public int User_New(UserModel user) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		ContentValues values = new ContentValues();
//
//		values.put(KEY_USER_NAME, user.name);// added later
//		
//		values.put(KEY_DISPLAY_NAME, user.displayName);
//		values.put(KEY_USER_EMAIL, user.email);
//		
//		values.put(KEY_USER_IMAGE, user.image);
//		values.put(KEY_CONTACT_ID, user.contact_id);
//
//		int id = (int) db.insert(TABLE_USERS, null, values);
//		db.close();
////		if(service!=null)
////		{
////			GravityController.
////		}
////		if(id!=-1){
////			String PendingServerUsers = mSharedPreferences.getString(SharedPreferencesHelper.Pending_Sync_Users, "");
////			
////			if(PendingServerUsers!="")
////			PendingServerUsers+=","+id;
////			else if(PendingServerUsers == "")
////				PendingServerUsers=id+"";
////			
////			mSharedPreferencesEditor.putString(SharedPreferencesHelper.Pending_Sync_Users, PendingServerUsers);
////		}
//		
//		return id;
//		// return true; //boolean to check if the user is added in db
//	}
//	public int User_validate(UserModel user)
//	{
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//		values.put(KEY_USER_NAME, user.name);
//		values.put(KEY_SERVER_ID, user.server_id);
//		//values.put(KEY_USER_EMAIL, user.email);
//
//		
//				int i = db.update(TABLE_USERS, values, KEY_PK  + " = ?",
//				new String[] { String.valueOf(user._id) });// updating row
//				return i;
//	}
//	// Edit a UserModel
//	public int User_Edit(UserModel user) {
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//		values.put(KEY_USER_NAME, user.name);
//		values.put(KEY_DISPLAY_NAME, user.displayName);
//		//values.put(KEY_USER_EMAIL, user.email);
//		values.put(KEY_USER_IMAGE, user.image);
//		//if(user.server_id!=null)
//		values.put(KEY_USER_ID, user.server_id);
//		return db.update(TABLE_USERS, values, KEY_PK + " = ?",
//				new String[] { String.valueOf(user._id) });// updating row
//	}
//
//	// Delete a user
//	public void User_Delete(int id) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		db.delete(TABLE_USERS, KEY_PK + " = ?",
//				new String[] { String.valueOf(id) });
//		db.close();
//	}
//
//	public void User_Delete_All() {
//		SQLiteDatabase db = this.getWritableDatabase();
//		db.delete(TABLE_USERS, null, null);
//		db.close();
//	}
//
//	public ArrayList<UserModel> User_List() {
//		ArrayList<UserModel> data = new ArrayList<UserModel>();
//		try {
//			// Select All Query
//			String selectQuery = "SELECT  * FROM " + TABLE_USERS;
//
//			SQLiteDatabase db = this.getWritableDatabase();
//
//			Cursor cursor = db.rawQuery(selectQuery, null);
//			int contactIdCol = cursor.getColumnIndex(KEY_CONTACT_ID);
//			int NameCol = cursor.getColumnIndex(KEY_USER_NAME);
//			int ServerIdCol = cursor.getColumnIndex(KEY_SERVER_ID);
//			// looping through all rows and adding to list
//			if (cursor.moveToFirst()) {
//				do {
//					UserModel userModel = new UserModel();
//					userModel._id = (Integer.parseInt(cursor.getString(0)));
//					userModel.name = (cursor.getString(NameCol));
//					userModel.displayName = (cursor.getString(6));
//					userModel.email = (cursor.getString(2));
//					userModel.server_id = (cursor.getString(ServerIdCol));
//					userModel.image = (cursor.getBlob(4));
//					userModel.contact_id = (cursor.getString(contactIdCol));
//					data.add(userModel);
//				} while (cursor.moveToNext());
//			}
//			cursor.close();
//			db.close();
//		} catch (Exception e) {
//			// TODO: handle exception
//			Log.e("all_TaskTitleList", "DBHelper GetTaskTitleList" + e);
//		}
//		return data;
//
//	}

	// Delete a user
//	public void UserList_Delete(TaskListModel tasklist, ArrayList<UserModel> users) {
////		SQLiteDatabase db = this.getWritableDatabase();
////		
////		for(UserModel user:users){
////		String selectQuery = "DELETE FROM " + TABLE_USERS_LISTS + " WHERE "
////				+ KEY_USER_ID + " = " + user._id + " AND " + KEY_FK_TASKLIST_ID
////				+ " = " + tasklist._id + " ";
////		db.execSQL(selectQuery);
////		}
////		db.close();
//		if(tasklist.server_id != null && tasklist.server_id != "" && service.hasInternet && service.user_data.is_sync_type)
//		{
//			GravityController.share_tasklist(service, service.user_data, tasklist, users, -1, "delete");//, dataids);
//			
//			//.post_tasklist(service, service.user_data, tasklist, -1);
//
//			
//		}
//		
//	}
//	public void UserList_Delete_Validate(String tasklistid, String userid) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		
//		String selectQuery = "DELETE FROM " + TABLE_USERS_LISTS + " WHERE "
//				+ KEY_USER_ID + " = " + userid + " AND " + KEY_FK_TASKLIST_ID
//				+ " = " + tasklistid + " ";
//		db.execSQL(selectQuery);
//		
//		db.close();
////		if(tasklist.server_id != null && tasklist.server_id != "" && service.hasInternet && service.user_data.is_sync_type)
////		{
////			GravityController.share_tasklist(service, service.user_data, tasklist, users, -1, "delete");//, dataids);
////			
////			//.post_tasklist(service, service.user_data, tasklist, -1);
////
////			
////		}
//		
//	}
//	public int UserList_New(TaskListModel tasklist, UserModel user) {
//		// error, wrong foreign key
//		SQLiteDatabase db = this.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put(KEY_USER_ID, user._id);
//		values.put(KEY_FK_TASKLIST_ID, tasklist._id);
//		int id = (int) db.insert(TABLE_USERS_LISTS, null, values);
//		db.close();
//		return id;
//	}
//	
//	public int UserList_New(TaskListModel tasklist, ArrayList<UserModel> users) {
//		// error, wrong foreign key
//		SQLiteDatabase db = this.getWritableDatabase();
//		int id = -1;
//		String dataids = "";
//		for(UserModel user:users){
//			
//			ContentValues values = new ContentValues();
//			values.put(KEY_USER_ID, user._id);
//			values.put(KEY_FK_TASKLIST_ID, tasklist._id);
//			id = (int) db.insert(TABLE_USERS_LISTS, null, values);
//			if(id!=-1)
//				dataids += id+",";
//			else
//				users.remove(user);
//		}
//		db.close();
//		if(tasklist.server_id != null && tasklist.server_id != "" && service.hasInternet && service.user_data.is_sync_type)
//		{
//			GravityController.share_tasklist(service, service.user_data, tasklist, users, -1, "add");//, dataids);
//			
//			//.post_tasklist(service, service.user_data, tasklist, -1);
//
//			
//		}
//		
//		
//		
//		return id;
//	}
//	
//	public int UserList_New_Validate(String TasklistId, String UserId) {
//		// error, wrong foreign key
//		SQLiteDatabase db = this.getWritableDatabase();
//		
//		ContentValues values = new ContentValues();
//		values.put(KEY_SYNC_STATUS, "Synced");
//		
//	
//		return db.update(TABLE_USERS_LISTS, values, KEY_USER_ID + " = ? AND"+ KEY_FK_TASKLIST_ID + " = ?",
//				new String[] {  UserId,TasklistId });
//		
//		
//	}
//
//	public ArrayList<UserModel> UserList_List(int tasklistID) {
//		ArrayList<Integer> users_ids1 = new ArrayList<Integer>();
//		ArrayList<Integer> users_ids2 = new ArrayList<Integer>();
//		SQLiteDatabase db = this.getReadableDatabase();
//		String selectQuery = "SELECT  * FROM " + TABLE_USERS_LISTS + " WHERE "
//				+ KEY_FK_TASKLIST_ID + " = " + tasklistID;
//
//		Cursor cursor = db.rawQuery(selectQuery, null);
//		int col_userid = cursor.getColumnIndex(KEY_USER_ID);
//		int col_sync = cursor.getColumnIndex(KEY_SYNC_STATUS);
//		// looping through all rows and adding to list
//		if (cursor.moveToFirst()) {
//			do {
//				// Adding TaskModel to list
//				if(cursor.getString(col_sync) == "Synced")
//				users_ids1.add(cursor.getInt(col_userid));
//				else
//					users_ids2.add(cursor.getInt(col_userid));
//			} while (cursor.moveToNext());
//		}
//		cursor.close();
//		db.close();
//		ArrayList<UserModel> users = new ArrayList<UserModel>();
//		
//		if (users_ids1.size() > 0) {
//			//String[] ids = users_ids.toArray(new String[users_ids.size()]);
//			users.addAll(Users_List(users_ids1,1.0));
//			 // call to a function for returning
//									// userModelss
//		} 
//		if (users_ids2.size() > 0) {
//			//String[] ids = users_ids.toArray(new String[users_ids.size()]);
//			users.addAll(Users_List(users_ids2,0.8)); // call to a function for returning
//									// userModelss
//		} 
//		
//		
//			return users;
//		
//	}

//	private ArrayList<UserModel> Users_List(ArrayList<Integer> ids,double alpha) {
//		ArrayList<UserModel> data = new ArrayList<UserModel>();
//
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_PK,
//				KEY_DISPLAY_NAME, KEY_USER_NAME, KEY_USER_EMAIL,
//				KEY_USER_IMAGE, KEY_CONTACT_ID
//
//		}, null, null, null, null, KEY_PK+ " ASC ", null);
//		int col_pk = cursor.getColumnIndex(KEY_PK);
//		int col_displayName = cursor.getColumnIndex(KEY_DISPLAY_NAME);
//		int col_name = cursor.getColumnIndex(KEY_USER_NAME);
//		int col_email = cursor.getColumnIndex(KEY_USER_EMAIL);
//		int col_image = cursor.getColumnIndex(KEY_USER_IMAGE);
//		int col_contactID = cursor.getColumnIndex(KEY_CONTACT_ID);
//		// get all
//		if (cursor.moveToFirst()) {
//			do {
//				for(Integer i:ids){
//					int tempid = cursor.getInt(col_pk);//Integer.parseInt(cursor.getString(col_pk));
//					if(i ==  tempid || i.equals(tempid)){
//						
//				UserModel temp = new UserModel();
//				temp._id = cursor.getInt(col_pk);
//				temp.image_alpha = alpha;
//				temp.displayName = cursor.getString(col_displayName);
//				temp.name = cursor.getString(col_name);
//				temp.contact_id = cursor.getString(col_contactID);
//				temp.email = cursor.getString(col_email);
//				temp.image = cursor.getBlob(col_image);
//
//				// other attributes
//
//				data.add(temp);
//				break;
//					}
//				}
//				// Adding TaskModel to list
//				// data.add((Integer.parseInt(cursor.getString(0))));
//			} while (cursor.moveToNext());
//		}
//		cursor.close();
//		db.close();
//		return data;
//
//	}

}
/*
 * public int User_New() { SQLiteDatabase db = this.getWritableDatabase();
 * ContentValues values = new ContentValues(); values.put(KEY_USER_NAME,
 * "mushahid.hassan"); // Task Name values.put(KEY_USER_EMAIL,
 * "mushahidhassan110@gmail.com"); // Task Phone
 * 
 * // Inserting Row int id = (int) db.insert(TABLE_USERS, null, values);
 * db.close(); // Closing database connection return id; }// ends
 */
// SimpleDateFormat simpledateformat = new
// SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");//("yyyy-MM-dd "+ "hh-mm-ss");
// //("ddMMyyyyhhmmss");

// public String currentDateTime = simpledateformat.format(new Date());
// long a = System.currentTimeMillis();
// String currentDateTime = Long.toString(a);

/*
 * 
 * Calendar rightNow = Calendar.getInstance(); long offset =
 * rightNow.get(Calendar.ZONE_OFFSET) + rightNow.get(Calendar.DST_OFFSET);
 */
// private byte[] getBlob(byte[] blob) {
// // TODO Auto-generated method stub
// return null;
// }