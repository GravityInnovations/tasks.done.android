package com.gravity.innovations.tasks.done;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.google.android.gms.internal.ey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.Time;
import android.util.Log;
//FAIK malik
public class DatabaseHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "task_done_db";

	//******** TABLE NAMES *********//
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
	//******** TABLE NAMES *********//

	//******** Columns Entries *********//
	private static final String KEY_PK = "_id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_DETAILS = "details";
	private static final String KEY_NOTES = "notes";
	private static final String KEY_FK_TASKLIST_ID = "fk_tasklist_id";
 
	private static final String KEY_USER_NAME = "user_name";
	private static final String KEY_USER_EMAIL = "user_email";
	// private static final String KEY_USER_PHONE_NUM = "user_phone";

	private static final String KEY_SERVER_ID = "server_id";
	private static final String KEY_PARENT = "parent";
	private static final String KEY_STATUS = "status";
	private static final String KEY_COMPLETED = "completed";
	private static final String KEY_DELETED = "deleted";
	private static final String KEY_HIDDEN = "hidden";
	private static final String KEY_LINKS = "links";
	private static final String KEY_SYNC_STATUS = "sync_status";
	private static final String KEY_SYNC_STATUS_TIMESTAMP = "sync_status_timestamp";
	private static final String KEY_SELF_LINK = "self_link";
	private static final String KEY_USER_IMAGE = "user_image";

	// Table
	private static final String KEY_DISPLAY_NAME = "display_name";
	private static final String KEY_CONTACT_ID = "contact_id";

	private static final String KEY_UPDATED_AT = "updated_date_time";
	// private static final String KEY_DUE_AT = "due_date_tTime";
	// private static final String KEY_COMPLETED_AT = "completed_date_time";

	private static final String KEY_REMIND_AT = "remind_date_time";
	private static final String KEY_REMIND_INTERVAL = "remind_interval";

	private static final String KEY_ETAG = "etag";
	private static final String KEY_USER_ID = "user_id";
	private static final String KEY_KIND = "kind";
	private static final String KEY_LIST_TYPE = "icon_identifier";

	private static final String KEY_ALARM_ID = "alarm_identifier";
	private static final String KEY_ALARM_STATUS = "alarm_status";
	private static final String KEY_ALARM_WEEKDAY = "alarm_weekday";
	
	private static final String KEY_ASSOCIATED_USERMODELS = "associated_usermodel";
	//******** Columns Entries *********//
	
	//********* SQLite Table Structure Queries *********//
	private static final String CREATE_TASKS_TABLE = "CREATE TABLE "
			+ TABLE_TASKS
			+ "("
			+ KEY_PK
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"//0
			+ KEY_TITLE	+ " TEXT,"
			+ KEY_DETAILS + " TEXT,"
			+ KEY_NOTES + " TEXT,"

			+ KEY_UPDATED_AT + " DATETIME," //time long
			+ KEY_REMIND_AT	+ " DATETIME," //time long
			+ KEY_REMIND_INTERVAL + " INTEGER," //values 0-3

			+ KEY_SERVER_ID + " TEXT," 
			+ KEY_SELF_LINK + " TEXT," 
			+ KEY_PARENT + " TEXT," 
			+ KEY_STATUS + " TEXT,"//10 
			+ KEY_COMPLETED + " INTEGER,"
			+ KEY_DELETED + " INTEGER," 
			+ KEY_HIDDEN + " INTEGER,"
			+ KEY_SYNC_STATUS + " TEXT," 
			+ KEY_SYNC_STATUS_TIMESTAMP + " DATETIME,"//15
			+ KEY_LINKS + " TEXT,"

			+ KEY_ALARM_ID + " INTEGER," 
			+ KEY_ALARM_STATUS + " INTEGER,"
			+ KEY_ALARM_WEEKDAY + " INTEGER,"
			
			+ KEY_ASSOCIATED_USERMODELS + " TEXT,"//20 associated users

			+ KEY_FK_TASKLIST_ID + " INTEGER," + " FOREIGN KEY ("
			+ KEY_FK_TASKLIST_ID + ")" + "REFERENCES " + TABLE_TASK_LIST + "("
			+ KEY_PK + ")" + ")";

	private static final String CREATE_TASK_LIST_TABLE = "CREATE TABLE "
			+ TABLE_TASK_LIST 
			+ "(" + KEY_PK
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ KEY_TITLE + " TEXT,"
			+ KEY_SERVER_ID + " TEXT," 
			+ KEY_ETAG + " TEXT," 
			+ KEY_UPDATED_AT + " DATETIME," 
			+ KEY_SELF_LINK + " TEXT," 
			+ KEY_KIND + " TEXT,"
			+ KEY_USER_ID + " INTEGER," 
			+ KEY_SYNC_STATUS + " TEXT,"
			+ KEY_SYNC_STATUS_TIMESTAMP + " DATETIME," 
			+ KEY_LIST_TYPE + " INTEGER" //values 1-5 icons
			+ ")";

	private static final String CREATE_USERS_TABLE = "CREATE TABLE "
			+ TABLE_USERS 
			+ "(" + KEY_PK
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ KEY_USER_NAME + " TEXT,"
			+ KEY_USER_EMAIL + " TEXT," 
			+ KEY_SERVER_ID + " TEXT,"
			+ KEY_USER_IMAGE + " BLOB," 
			+ KEY_CONTACT_ID + " TEXT,"
			+ KEY_DISPLAY_NAME + " TEXT"
			+ ")";

	private static final String CREATE_USERS_LISTS_TABLE = "CREATE TABLE "
			+ TABLE_USERS_LISTS 
			+ "(" + KEY_PK
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ KEY_SERVER_ID + " TEXT,"
			+ KEY_SYNC_STATUS + " TEXT," 
			+ KEY_SYNC_STATUS_TIMESTAMP + " DATETIME,"
			+ KEY_USER_ID + " INTEGER," 
			+ KEY_FK_TASKLIST_ID + " INTEGER" 
			+ ")";
	//********* SQLite Table Structure Queries *********//
	
	public DatabaseHelper(Context cContext) {
		super(cContext, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//********* Creating Tables *********// 
	@Override
	public void onCreate(SQLiteDatabase db) {
		String _TAG = "DbHelper onCreate";
		try {
			db.execSQL(CREATE_TASKS_TABLE);
		} catch (Exception e) {
			Log.e(_TAG, "TASKS_TABLE not created");
		}
		try {
			db.execSQL(CREATE_TASK_LIST_TABLE);
		} catch (Exception e) {
			Log.e(_TAG, "TASK_LIST_TABLE not created");
		}
		try {
			db.execSQL(CREATE_USERS_TABLE);
		} catch (Exception e) {
			Log.e(_TAG, "USERS_TABLE not created");
		}
		try {
			db.execSQL(CREATE_USERS_LISTS_TABLE);
		} catch (Exception e) {
			Log.e(_TAG, "CREATE_USERS_LISTS_TABLE not created");
		}
	}
	//********* Creating Tables *********// 
	
	//********* Upgrading database *********// 
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
	//********* Upgrading database *********// 
	
	/**
	 * All CRUD FOR TASK(Create, Read, Update, Delete) Operations
	 */
	
	// Add new TaskModel
	public int Task_New(TaskModel task) {
		// error, wrong foreign key
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, task.title);
		values.put(KEY_DETAILS, task.details);

		values.put(KEY_NOTES, task.notes);
		values.put(KEY_UPDATED_AT, task.updated);//4 
		// currentDateTime gets its value from above declared string
		values.put(KEY_REMIND_AT, task.remind_at);
		values.put(KEY_REMIND_INTERVAL, task.remind_interval);
		values.put(KEY_SERVER_ID, task.server_id);
		values.put(KEY_SELF_LINK, task.self_link);
		values.put(KEY_PARENT, task.parent);
		values.put(KEY_STATUS, task.status);//10

		values.put(KEY_COMPLETED, task.completed);
		values.put(KEY_DELETED, task.deleted);
		values.put(KEY_HIDDEN, task.hidden);
		values.put(KEY_SYNC_STATUS, task.syncStatus);
		values.put(KEY_SYNC_STATUS_TIMESTAMP, task.syncStatusTimeStamp);//15
		values.put(KEY_LINKS, task.links);

		values.put(KEY_ALARM_ID, task.alarm_id);
		values.put(KEY_ALARM_STATUS, task.alarm_status);
		values.put(KEY_ALARM_WEEKDAY, task.weekday);
		
		values.put(KEY_ASSOCIATED_USERMODELS, task.associated_usermodels);//20

		values.put(KEY_FK_TASKLIST_ID, task.fk_tasklist_id);
		int id = (int) db.insert(TABLE_TASKS, null, values);
		db.close();
		return id;
	}

	// Delete a TaskModel
	public boolean Task_Delete(int id) {
		// it will take input from TaskList_Delete
		// and delete all tasks against that list
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TASKS, KEY_PK + " = ?", 
				new String[] { String.valueOf(id) }); 
		db.close();
		return true;
	}

	// Updating a TaskModel
	public int Task_Edit(TaskModel task) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, task.title);
		values.put(KEY_DETAILS, task.details);
		values.put(KEY_NOTES, task.notes);
		//correct this one this may be the wrong assignment
		values.put(KEY_UPDATED_AT, task.updated); 
		//currentDateTime gets its value from above declared string
		values.put(KEY_REMIND_AT, task.remind_at);//5
		values.put(KEY_REMIND_INTERVAL, task.remind_interval);
		values.put(KEY_SERVER_ID, task.server_id);
		values.put(KEY_SELF_LINK, task.self_link);
		values.put(KEY_PARENT, task.parent);
		values.put(KEY_STATUS, task.status);//10
		values.put(KEY_COMPLETED, task.completed);
		values.put(KEY_DELETED, task.deleted);
		values.put(KEY_HIDDEN, task.hidden);
		values.put(KEY_SYNC_STATUS, task.syncStatus);
		values.put(KEY_SYNC_STATUS_TIMESTAMP, task.syncStatusTimeStamp);//15
		values.put(KEY_LINKS, task.links);

		values.put(KEY_ALARM_ID, task.alarm_id);
		values.put(KEY_ALARM_STATUS, task.alarm_status);
		values.put(KEY_ALARM_WEEKDAY, task.weekday);
		values.put(KEY_ASSOCIATED_USERMODELS, task.associated_usermodels);//20

		return db.update(TABLE_TASKS, values, KEY_PK + " = ?",
				new String[] { String.valueOf(task._id) });// updating row
	}

	/*
	 * public void Task_Completed(TaskModel task, int flag){ SQLiteDatabase db =
	 * this.getWritableDatabase(); ContentValues values = new ContentValues();
	 * values.put(KEY_COMPLETED, flag); db.update(TABLE_TASKS, values, KEY_PK +
	 * " = ?", new String[] { String.valueOf(task._id) });// updating row }
	 */
	// Getting single TaskModel //needed at notification
	public TaskModel Task_Single(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_TASKS, new String[] { 
				KEY_PK, KEY_TITLE, KEY_DETAILS, KEY_NOTES,

				KEY_UPDATED_AT, KEY_REMIND_AT, 
				KEY_ALARM_ID, KEY_ALARM_STATUS, KEY_ALARM_WEEKDAY,

				KEY_REMIND_INTERVAL, KEY_FK_TASKLIST_ID }, KEY_PK + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		TaskModel task = new TaskModel(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getString(5),
				Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor
						.getString(7)), Integer.parseInt(cursor.getString(8)),
				Integer.parseInt(cursor.getString(9)), Integer.parseInt(cursor
						.getString(10)));
		// return TaskModel
		cursor.close();
		db.close();
		return task;
	}

	
	// Getting All Tasks of task list
	public ArrayList<TaskModel> Task_List(int id) {
		ArrayList<TaskModel> data = new ArrayList<TaskModel>();
		try {

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE "
					+ KEY_FK_TASKLIST_ID + " = " + id + 
					//" ORDER BY updated_date_time DESC" 
					//" ORDER BY completed, "
					 " ORDER BY completed, updated_date_time DESC";

			SQLiteDatabase db = this.getWritableDatabase();

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					TaskModel task = new TaskModel();
					task._id = (Integer.parseInt(cursor.getString(0)));
					task.title = (cursor.getString(1));
					task.details = (cursor.getString(2));
					task.notes = (cursor.getString(3));
					task.updated = (cursor.getString(4));
					task.remind_at = (cursor.getString(5));
					task.remind_interval = (Integer.parseInt(cursor
							.getString(6)));
					task.server_id = (cursor.getString(7));
					task.self_link = (cursor.getString(8));
					task.parent = (cursor.getString(9));
					task.status = (cursor.getString(10));
					task.completed = (Integer.parseInt(cursor.getString(11)));
					task.deleted = (Integer.parseInt(cursor.getString(12)));
					task.hidden = (Integer.parseInt(cursor.getString(13)));
					task.syncStatus = cursor.getString(14);
					task.syncStatusTimeStamp = cursor.getString(15);
					task.links = (cursor.getString(16));

					task.alarm_id = (Integer.parseInt(cursor.getString(17)));
					task.alarm_status = (Integer.parseInt(cursor.getString(18)));
					task.weekday = (Integer.parseInt(cursor.getString(19))); 
					//task.associated_usermodels = (cursor.getString(20));
					task.fk_tasklist_id = (Integer.parseInt(cursor
							.getString(21)));
					// Adding TaskModel to list
					data.add(task);
				} while (cursor.moveToNext());
			}

			// return TaskModel list
			cursor.close();
			db.close();
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("all_Task", "DBHelper GetTasks" + e);
		}
		return data;
	}

	// Getting All Alarm of tasks
	public ArrayList<TaskModel> Alarm_List(/* int id */) {
		ArrayList<TaskModel> data = new ArrayList<TaskModel>();
		int id = 1;
		try {

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE "
					+ KEY_ALARM_STATUS + " = " + id;


			SQLiteDatabase db = this.getWritableDatabase();

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					TaskModel task = new TaskModel();
					task._id = (Integer.parseInt(cursor.getString(0)));
					task.title = (cursor.getString(1));
					task.details = (cursor.getString(2));
					task.notes = (cursor.getString(3));
					task.updated = (cursor.getString(4));
					task.remind_at = (cursor.getString(5));
					task.remind_interval = (Integer.parseInt(cursor
							.getString(6)));
					task.server_id = (cursor.getString(7));
					task.self_link = (cursor.getString(8));
					task.parent = (cursor.getString(9));
					task.status = (cursor.getString(10));
					task.completed = (Integer.parseInt(cursor.getString(11)));
					task.deleted = (Integer.parseInt(cursor.getString(12)));
					task.hidden = (Integer.parseInt(cursor.getString(13)));
					task.syncStatus = cursor.getString(14);
					task.syncStatusTimeStamp = cursor.getString(15);
					task.links = (cursor.getString(16));

					task.alarm_id = (Integer.parseInt(cursor.getString(17)));
					task.alarm_status = (Integer.parseInt(cursor.getString(18)));
					task.weekday = (Integer.parseInt(cursor.getString(19)));
					 
					task.fk_tasklist_id = (Integer.parseInt(cursor
							.getString(20)));

					// Adding TaskModel to list
					data.add(task);
				} while (cursor.moveToNext());
			}

			// return TaskModel list
			cursor.close();
			db.close();
			return data;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("all_Alarms", "DBHelper Active Alarm_LIST" + e);
		}
		return data;
	}

	/**
	 * All CRUD FOR TASK LIST(Create, Read, Update, Delete) Operations
	 */
	// Add a TaskListModel
	public int TaskList_New(TaskListModel tasklist) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, tasklist.title);
		values.put(KEY_SERVER_ID, tasklist.server_id);
		values.put(KEY_ETAG, tasklist.etag);
		values.put(KEY_UPDATED_AT, tasklist.updated);
		values.put(KEY_SELF_LINK, tasklist.self_link);
		values.put(KEY_KIND, tasklist.kind);
		values.put(KEY_USER_ID, tasklist.user_id);
		values.put(KEY_SYNC_STATUS, tasklist.syncStatus);
		values.put(KEY_SYNC_STATUS_TIMESTAMP, tasklist.syncStatusTimeStamp);
		values.put(KEY_LIST_TYPE, tasklist.icon_identifier);

		int id = (int) db.insert(TABLE_TASK_LIST, null, values);

		db.close();
		return id;
		// return true; //boolean to check if the tasklist is added in db

	}

	// Delete a TaskListModel
	public void TaskList_Delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TASK_LIST, KEY_PK + " = ?",
				new String[] { String.valueOf(id) });
		// Task_Delete(id); // Delete tasks and then delete list
		db.close();
	}

	// Updating single TaskListModel
	public int TaskList_Edit(TaskListModel tasklist) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, tasklist.title);
		values.put(KEY_LIST_TYPE, tasklist.icon_identifier);
		return db.update(TABLE_TASK_LIST, values, KEY_PK + " = ?",
				new String[] { String.valueOf(tasklist._id) });
	}

	// Getting All TasksLists

	public ArrayList<TaskListModel> TaskList_List() {
		ArrayList<TaskListModel> data = new ArrayList<TaskListModel>();
		try {
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_TASK_LIST;

			SQLiteDatabase db = this.getWritableDatabase();

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					TaskListModel tasklist = new TaskListModel();
					tasklist._id = (Integer.parseInt(cursor.getString(0)));
					tasklist.title = (cursor.getString(1));

					tasklist.server_id = (cursor.getString(2));
					tasklist.etag = (cursor.getString(3));
					tasklist.updated = (cursor.getString(4));
					tasklist.self_link = (cursor.getString(5));
					tasklist.kind = (cursor.getString(6));
					tasklist.user_id = (Integer.parseInt(cursor.getString(7)));
					tasklist.syncStatus =  (cursor.getString(8)) ;
					tasklist.syncStatusTimeStamp = (cursor.getString(9));
					tasklist.icon_identifier = (Integer.parseInt(cursor
							.getString(10)));
					// Adding TaskModel to list
					tasklist.tasks = this.Task_List(tasklist._id);
					data.add(tasklist);
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
	// Getting single TaskListModel 
		//needed at opening drawer after onClickNotification
		
	public TaskListModel TaskList_Single(int id) {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(TABLE_TASK_LIST, new String[] { 
					
					 KEY_PK, KEY_TITLE, KEY_SERVER_ID, KEY_ETAG, 
					 KEY_UPDATED_AT, KEY_SELF_LINK, KEY_KIND, 
					 KEY_USER_ID, KEY_SYNC_STATUS, KEY_SYNC_STATUS_TIMESTAMP, KEY_LIST_TYPE
					
				}, KEY_PK + "=?",
					new String[] { String.valueOf(id) }, null, null, null, null);
			if (cursor != null)
				cursor.moveToFirst();
			
			TaskListModel tasklist = new TaskListModel(Integer.parseInt(cursor.getString(0)),
					
					cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getString(6), 
					
					Integer.parseInt(cursor.getString(7)),  (cursor.getString(8)),
					 (cursor.getString(9)) , Integer.parseInt(cursor.getString(10)));
			// return TaskModel
			cursor.close();
			db.close();
			return tasklist;
		}

	/*
	 * public TaskListModel TaskList_Single(int id) { SQLiteDatabase db =
	 * this.getReadableDatabase();
	 * 
	 * Cursor cursor = db.query(TABLE_TASK_LIST, new String[] { KEY_PK,
	 * KEY_TITLE }, KEY_PK + "=?", new String[] { String.valueOf(id) }, null,
	 * null, null, null); if (cursor != null) cursor.moveToFirst();
	 * TaskListModel tasklist = new TaskListModel(Integer.parseInt(cursor
	 * .getString(0)), cursor.getString(1));// return TaskListModel
	 * cursor.close(); db.close(); return tasklist; }
	 */
	
	public ArrayList<Common.CustomViewsData.MultiSelectRowData> Get_Users() {

		ArrayList<Common.CustomViewsData.MultiSelectRowData> users = new ArrayList<Common.CustomViewsData.MultiSelectRowData>();

		try {
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_USERS;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Common.CustomViewsData.MultiSelectRowData user = new Common.CustomViewsData.MultiSelectRowData();
					user.text1 = (cursor.getString(1));
					user.text2 = (cursor.getString(2));
					user.iconRes = (cursor.getBlob(4));

					// Adding Task to list
					users.add(user);
				} while (cursor.moveToNext());
			}

			return users;

			// cursor.close();
			// db.close();
			// for (int i = 0; i < 30; i++) {
			// Common.CustomViewsData.MultiSelectRowData user = new
			// Common.CustomViewsData.MultiSelectRowData();
			// user.text1 = "h";
			// user.text2 = "some";
			// user.iconRes = R.drawable.ic_launcher;
			// // Adding Task to list
			// users.add(user);
			// }
			// // return users;
			// } catch (Exception e) {
			// // TODO: handle exception
			// Log.e("all_Task", "" + e);
			// }

			// if (cursor.moveToFirst()) {
			// do {

			// Common.CustomViewsData.MultiSelectRowData user = new
			// Common.CustomViewsData.MultiSelectRowData();
			// user.text1 = (cursor.getString(2));
			// user.text2 = (cursor.getString(1));
			// user.iconRes = (cursor.getBlob(4));
			// // Adding TaskModel to list
			// users.add (user);
			// } while (cursor.moveToNext());
			// }

			//
		} catch (Exception e) {
			Log.e("", "");

		} finally {
			return users;
		}
	}

	/**
	 * All CRUD FOR Users(Create, Read, Update, Delete) Operations
	 */

	/*
	 * public int User_New() { SQLiteDatabase db = this.getWritableDatabase();
	 * ContentValues values = new ContentValues(); values.put(KEY_USER_NAME,
	 * "mushahid.hassan"); // Task Name values.put(KEY_USER_EMAIL,
	 * "mushahidhassan110@gmail.com"); // Task Phone
	 * 
	 * // Inserting Row int id = (int) db.insert(TABLE_USERS, null, values);
	 * db.close(); // Closing database connection return id; }// ends
	 */
	// Add a user
	public int User_New(UserModel user) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_USER_NAME, user.name);//added later

		values.put(KEY_DISPLAY_NAME, user.displayName);
		values.put(KEY_USER_EMAIL, user.email);
		values.put(KEY_USER_IMAGE, user.image);
		values.put(KEY_CONTACT_ID, user.contact_id);

		int id = (int) db.insert(TABLE_USERS, null, values);
		db.close();
		return id;
		// return true; //boolean to check if the user is added in db
	}
	
	// Edit a UserModel
		public int User_Edit(UserModel user) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(KEY_USER_NAME, user.name);
			values.put(KEY_DISPLAY_NAME, user.displayName);
			values.put(KEY_USER_EMAIL, user.email);
			values.put(KEY_USER_IMAGE, user.image);

			values.put(KEY_USER_ID, user.server_id);
			return db.update(TABLE_TASKS, values, KEY_PK + " = ?",
					new String[] { String.valueOf(user._id) });// updating row
		}

	// Delete a user
	public void User_Delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, KEY_PK + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}
	public void User_Delete_All() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, null,null);
		db.close();
	}

	public ArrayList<UserModel> User_List() {
		ArrayList<UserModel> data = new ArrayList<UserModel>();
		try {
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_USERS;

			SQLiteDatabase db = this.getWritableDatabase();

			Cursor cursor = db.rawQuery(selectQuery, null);
			int contactIdCol = cursor.getColumnIndex(KEY_CONTACT_ID);
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					UserModel tasklist = new UserModel();
					tasklist._id = (Integer.parseInt(cursor.getString(0)));
					tasklist.displayName = (cursor.getString(6));
					tasklist.email = (cursor.getString(2));
					tasklist.image =(cursor.getBlob(4));
					tasklist.contact_id = (cursor.getString(contactIdCol));
					data.add(tasklist);
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

//	private byte[] getBlob(byte[] blob) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	public ArrayList<UserModel> User_List(int id) {
		ArrayList<UserModel> data = new ArrayList<UserModel>();
		try {
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
					+ KEY_PK + " = " + id;

			SQLiteDatabase db = this.getWritableDatabase();

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					UserModel userlist = new UserModel();
					userlist._id = (Integer.parseInt(cursor.getString(0)));
					userlist.displayName = (cursor.getString(6));
					userlist.email = (cursor.getString(2));
					userlist.image =(cursor.getBlob(4));

					data.add(userlist);
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
	
	public int UserList_New(TaskListModel tasklist, UserModel user) {
		// error, wrong foreign key
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_USER_ID, user._id);
		values.put(KEY_FK_TASKLIST_ID, tasklist._id);
		int id = (int) db.insert(TABLE_TASKS, null, values);
		db.close();
		return id;
	}
	// Delete a user
	public void UserList_Delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, KEY_PK + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}
	
}
//SimpleDateFormat simpledateformat = new
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
