package com.gravity.innovations.tasks.done;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "tasksManager";

	// TABLE NAMES
	// TaskModel table
	// task has Auto inc. id, title, details and notes
	private static final String TABLE_TASKS = "tasks";
	// TaskListModel table
	// task list has Auto inc. id and a title
	private static final String TABLE_TASK_LIST = "task_list";
	// Users table
	// user has Auto inc. id name and a email
	private static final String TABLE_USERS = "users";
	// TABLE NAMES

	// TaskModel Table Columns names
	private static final String KEY_PK = "_id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_DETAILS = "details";
	private static final String KEY_NOTES = "notes";
	private static final String KEY_FK_TASKLIST_ID = "fk_tasklist_id";


	// TaskListModel Table Columns names
	// id and title and as above

	// Users id same as
	private static final String KEY_USER_NAME = "user_name";
	private static final String KEY_USER_EMAIL = "user_email";
	private static final String KEY_USER_PHONE_NUM = "user_phone";

	private static final String KEY_SERVER_ID = "server_id";
	private static final String KEY_USER_IMAGE = "user_image";
	
	// Table
	private static final String KEY_DISPLAY_NAME = "display_name";
	private static final String KEY_CONTACT_ID = "contact_id";
	
	
	private static final String KEY_UPDATED_AT = "updated_date_time";
	private static final String KEY_DUE_AT = "due_date_tTime";
	private static final String KEY_COMPLETED_AT = "completed_date_time";
	
	private static final String KEY_REMIND_AT = "remind_date_time";
	private static final String KEY_REMIND_INTERVAL = "remind_interval";
	
	SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd "+ "hh-mm-ss"); //("ddMMyyyyhhmmss");
	public String currentDateTime = simpledateformat.format(new Date());	
	
	// SQLite Create Queries
	// Tasks Table
	private static final String CREATE_TASKS_TABLE = "CREATE TABLE "
			+ TABLE_TASKS + "(" + KEY_PK
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ KEY_TITLE + " TEXT,"
			+ KEY_DETAILS + " TEXT," 
			+ KEY_NOTES + " TEXT,"
			
			+ KEY_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
			+ KEY_DUE_AT + " DATETIME," //date time
			+ KEY_COMPLETED_AT + "  DATETIME DEFAULT CURRENT_TIMESTAMP," //date time
			
			+ KEY_REMIND_AT + " DATETIME, "
			+ KEY_REMIND_INTERVAL + " INTEGER, " //values between 0 to 3
			
			+ KEY_FK_TASKLIST_ID + " INTEGER," + " FOREIGN KEY ("
			+ KEY_FK_TASKLIST_ID + ")" + "REFERENCES " + TABLE_TASK_LIST + "("
			+ KEY_PK + ")" + ")";

	private static final String CREATE_TASK_LIST_TABLE = "CREATE TABLE "
			+ TABLE_TASK_LIST + "(" + KEY_PK
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT" + ")";

	/*
	  private static final String CREATE_USERS_TABLE = "CREATE TABLE "
	 
			+ TABLE_USERS + "(" + KEY_PK
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_NAME + " TEXT,"
			+ KEY_USER_EMAIL + " TEXT" + ")";
	*/
	private static final String CREATE_USERS_TABLE = "CREATE TABLE "
			+ TABLE_USERS + "(" + KEY_PK
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ KEY_USER_NAME + " TEXT,"
			+ KEY_USER_EMAIL + " TEXT," 
			+ KEY_SERVER_ID + " TEXT,"
			+ KEY_USER_IMAGE + " BLOB,"
			+ KEY_CONTACT_ID + " TEXT,"
			+ KEY_DISPLAY_NAME + " TEXT,"
			+ KEY_USER_PHONE_NUM + " TEXT"  //added this line
			+ ")";

	public DatabaseHelper(Context cContext) {
		super(cContext, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_TASKS_TABLE);// create task table
		} catch (Exception e) {
			Log.e("onCreate Error", "TASKS_TABLE not created");
		}
		try {
			db.execSQL(CREATE_TASK_LIST_TABLE);// create task list table
		} catch (Exception e) {
			Log.e("onCreate Error", "TASK_LIST_TABLE not created");
		}
		try {
			db.execSQL(CREATE_USERS_TABLE);// create user table
		} catch (Exception e) {
			Log.e("onCreate Error", "USERS_TABLE not created");
		}
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		try {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);// Drop TaskModel
																// Table
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK_LIST);// Drop
																	// TaskListModel
																	// Table
			onCreate(db);// Create tables again
		} catch (Exception e) {
			Log.e("Database onUpgrade", "not created");
		}
	}

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
		
		
		
		values.put(KEY_UPDATED_AT, currentDateTime); //currentDateTime gets its value from above declared string
		
		values.put(KEY_DUE_AT, task.due_at);
		
		values.put(KEY_REMIND_AT, task.remind_at);
		values.put(KEY_REMIND_INTERVAL, task.remind_interval);
		
		values.put(KEY_FK_TASKLIST_ID, task.fk_tasklist_id);
		int id = (int) db.insert(TABLE_TASKS, null, values);
		db.close();
		return id;
	}

	// Delete a TaskModel
	public boolean Task_Delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TASKS, KEY_PK + " = ?", // it will take
												// input from
												// TaskList_Delete
				new String[] { String.valueOf(id) }); // and delete all tasks
														// against that list
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
		values.put(KEY_UPDATED_AT, task.updated_at); //correct this one this may be the wrong assignment
		values.put(KEY_REMIND_AT, task.remind_at);
		values.put(KEY_REMIND_INTERVAL, task.remind_interval);
		return db.update(TABLE_TASKS, values, KEY_PK + " = ?",
				new String[] { String.valueOf(task._id) });// updating row
	}

	// Getting single TaskModel
	public TaskModel Task_Single(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_TASKS, new String[] { KEY_PK, KEY_TITLE,
				KEY_DETAILS, KEY_NOTES, KEY_FK_TASKLIST_ID }, KEY_PK + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		TaskModel task = new TaskModel(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				Integer.parseInt(cursor.getString(4)));
		// return TaskModel
		cursor.close();
		db.close();
		return task;
	}

	// Getting All Tasks
	public ArrayList<TaskModel> Task_List(int id) {
		ArrayList<TaskModel> data = new ArrayList<TaskModel>();
		try {

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE "
					+ KEY_FK_TASKLIST_ID + " = " + id;

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
					
					task.updated =(cursor.getString(4));
					task.remind_at = (cursor.getString(7));
					task.remind_interval = (Integer.parseInt(cursor.getString(8)));
					
					task.fk_tasklist_id = (Integer
							.parseInt(cursor.getString(9)));
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

	/**
	 * All CRUD FOR TASK LIST(Create, Read, Update, Delete) Operations
	 */
	// Add a TaskListModel
	public int TaskList_New(TaskListModel tasklist) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, tasklist.title);
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

	public TaskListModel TaskList_Single(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_TASK_LIST, new String[] { KEY_PK,
				KEY_TITLE }, KEY_PK + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		TaskListModel tasklist = new TaskListModel(Integer.parseInt(cursor
				.getString(0)), cursor.getString(1));// return TaskListModel
		cursor.close();
		db.close();
		return tasklist;
	}

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
			 Common.CustomViewsData.MultiSelectRowData user =
			 new Common.CustomViewsData.MultiSelectRowData();
			 user.text1 = (cursor.getString(1));
			 user.text2 = (cursor.getString(2));
			 user.iconRes = (cursor.getBlob(4));
			 
			 // Adding Task to list
			 users.add(user);
			 } while (cursor.moveToNext());
			 }

			 return users;
		
			
			
			
			//cursor.close();
		//	db.close();
//			for (int i = 0; i < 30; i++) {
//				Common.CustomViewsData.MultiSelectRowData user = new Common.CustomViewsData.MultiSelectRowData();
//				user.text1 = "h";
//				user.text2 = "some";
//				user.iconRes = R.drawable.ic_launcher;
//				// Adding Task to list
//				users.add(user);
//			}
//			// return users;
//		} catch (Exception e) {
//			// TODO: handle exception
//			Log.e("all_Task", "" + e);
//		}

			
			
			
//			if (cursor.moveToFirst()) {
//				do {
			 
//					Common.CustomViewsData.MultiSelectRowData user = new Common.CustomViewsData.MultiSelectRowData();
//					user.text1 = (cursor.getString(2));
//					user.text2 = (cursor.getString(1));
//					user.iconRes = (cursor.getBlob(4));
//					// Adding TaskModel to list
//					users.add (user); 
//				} while (cursor.moveToNext());
//			}
			
	//	
	}
	catch(Exception e){
		Log.e("", "");
		
	}
		finally{
			return users;
		}
	}

	/**
	 * All CRUD FOR Users(Create, Read, Update, Delete) Operations
	 */
 
	
/*	
	public int User_New() {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_USER_NAME, "mushahid.hassan"); // Task Name
		values.put(KEY_USER_EMAIL, "mushahidhassan110@gmail.com"); // Task Phone
	 
		// Inserting Row
		int id = (int) db.insert(TABLE_USERS, null, values);
		db.close(); // Closing database connection
		return id;
	}// ends
*/
	// Add a user
	public int User_New(UserModel user) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_DISPLAY_NAME, user.displayName);
		values.put(KEY_USER_EMAIL, user.email);
		
		values.put(KEY_USER_PHONE_NUM, user.phone);
		values.put(KEY_USER_IMAGE, user.image);
		
		int id = (int) db.insert(TABLE_USERS, null, values);
		db.close();
		return id;
		// return true; //boolean to check if the user is added in db
	}

	// Delete a user
	public void User_Delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, KEY_PK + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}
	public ArrayList<UserModel> User_List() {
		ArrayList<UserModel> data = new ArrayList<UserModel>();
		try {
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_USERS;

			SQLiteDatabase db = this.getWritableDatabase();

			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					UserModel tasklist = new UserModel();
					 tasklist._id = (Integer.parseInt(cursor.getString(0)));

					tasklist.displayName = (cursor.getString(6));
					tasklist.email = (cursor.getString(2));
					 
					tasklist.image = (cursor.getBlob(4)); 
				
					
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
}