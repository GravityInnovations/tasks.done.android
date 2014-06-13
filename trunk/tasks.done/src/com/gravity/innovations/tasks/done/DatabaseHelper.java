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

	// Tasks table name
	private static final String TABLE_TASKS = "tasks";

	// Tasks Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_TASK_TITLE = "task_title";
	private static final String KEY_TASK_DETAILS = "task_details";
	private static final String KEY_TASK_NOTES = "task_notes";
	private final ArrayList<Task> task_list = new ArrayList<Task>();

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TaskS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_TASK_TITLE + " TEXT,"
				+ KEY_TASK_DETAILS + " TEXT," + KEY_TASK_NOTES + " TEXT" + ")";
		db.execSQL(CREATE_TaskS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new Task
	public void Add_Task(Task task) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TASK_TITLE, task.getTitle()); // Task Name
		values.put(KEY_TASK_DETAILS, task.getDetails()); // Task Phone
		values.put(KEY_TASK_NOTES, task.getNotes()); // Task Email
		// Inserting Row
		db.insert(TABLE_TASKS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single Task
	Task Get_Task(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_TASKS, new String[] { KEY_ID,
				KEY_TASK_TITLE, KEY_TASK_DETAILS, KEY_TASK_NOTES }, KEY_ID
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		Task task = new Task(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3));
		// return Task
		cursor.close();
		db.close();

		return task;
	}

	// Getting All Tasks
	public ArrayList<Task> Get_Tasks() {
		try {
			task_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Task task = new Task();
					task.setID(Integer.parseInt(cursor.getString(0)));
					task.setTitle(cursor.getString(1));
					task.setDetails(cursor.getString(2));
					task.setNotes(cursor.getString(3));
					// Adding Task to list
					task_list.add(task);
				} while (cursor.moveToNext());
			}

			// return Task list
			cursor.close();
			db.close();
			return task_list;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("all_Task", "" + e);
		}

		return task_list;
	}

	// Updating single Task
	public int Update_Task(Task task) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TASK_TITLE, task.getTitle());
		values.put(KEY_TASK_DETAILS, task.getDetails());
		values.put(KEY_TASK_NOTES, task.getNotes());

		// updating row
		return db.update(TABLE_TASKS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(task.getID()) });
	}

	// Deleting single Task
	public void Delete_Task(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TASKS, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	// Getting Tasks Count
	public int Get_Total_Tasks() {
		String countQuery = "SELECT  * FROM " + TABLE_TASKS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
