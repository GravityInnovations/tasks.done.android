package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "tasksManager";

	// TABLE NAMES
	// Tasks table
	// tasks has Auto inc. id, title, details and notes
	private static final String TABLE_TASKS = "tasks";
	// TaskModel List Table
	// task list has Auto inc. id and a title
	private static final String TABLE_TASK_LIST = "task_list";
	// Users Table
	// task list has Auto inc. id name and a email
	private static final String TABLE_USERS = "users";
	// TABLE NAMES

	// Tasks Table Columns names
	private static final String KEY_PK = "_id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_DETAILS = "details";
	private static final String KEY_NOTES = "notes";
	private static final String KEY_FK_TASKLIST_ID = "fk_tasklist_id";

	// TaskModel List Table Columns names
	// id and title and as above

	// id same
	private static final String KEY_USER_NAME = "user_name";
	private static final String KEY_USER_EMAIL = "user_email";

	// SQLite Create Queries
	// Tasks Table
	private static final String CREATE_TASKS_TABLE = "CREATE TABLE "
			+ TABLE_TASKS + "(" + KEY_PK
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
			+ KEY_DETAILS + " TEXT," + KEY_NOTES + " TEXT,"
			+ KEY_FK_TASKLIST_ID + " INTEGER," + " FOREIGN KEY ("
			+ KEY_FK_TASKLIST_ID + ")" + "REFERENCES " + TABLE_TASK_LIST + "("
			+ KEY_PK + ")" + ")";

	private static final String CREATE_TASK_LIST_TABLE = "CREATE TABLE "
			+ TABLE_TASK_LIST + "(" + KEY_PK
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT" + ")";

	private static final String CREATE_USERS_TABLE = "CREATE TABLE "
			+ TABLE_USERS + "(" + KEY_PK
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_NAME + " TEXT,"
			+ KEY_USER_EMAIL + " TEXT" + ")";

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
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		try {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);// Drop TaskModel Table
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
					task.fk_tasklist_id = (Integer
							.parseInt(cursor.getString(4)));
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
		//Task_Delete(id); // Delete tasks and then delete list
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
		TaskListModel tasklist = new TaskListModel(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1));// return TaskListModel
		cursor.close();
		db.close();
		return tasklist;
	}
	
	public ArrayList<Common.CustomViewsData.MultiSelectRowData> Get_Users() {

		ArrayList<Common.CustomViewsData.MultiSelectRowData> users
			= new ArrayList<Common.CustomViewsData.MultiSelectRowData>();

		try {
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_USERS;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
//			if (cursor.moveToFirst()) {
//				do {
//					Common.CustomViewsData.MultiSelectRowData user = 
//							new Common.CustomViewsData.MultiSelectRowData();
//					user.text1 = (cursor.getString(0));
//					user.text2 = (cursor.getString(1));
//					// Adding Task to list
//					users.add(user);
//				} while (cursor.moveToNext());
//			}

			// return Task list
			cursor.close();
			db.close();
			for(int i=0;i<30;i++)
			{
				Common.CustomViewsData.MultiSelectRowData user = 
						new Common.CustomViewsData.MultiSelectRowData();
				user.text1 = "h";
				user.text2 = "some";
				user.iconRes = R.drawable.ic_launcher;
				// Adding Task to list
				users.add(user);
			}
			//return users;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("all_Task", "" + e);
		}

		return users;


	}
	
	public void Add_User() {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", "faik malik"); // Task Name
		values.put("email", "faik.malik89@gmail.com"); // Task Phone
		values.put("username", "faik.malik"); // Task Email
		values.put("checked", "false");
		// Inserting Row
		db.insert(TABLE_USERS, null, values);
		db.close(); // Closing database connection
	}//ends
}