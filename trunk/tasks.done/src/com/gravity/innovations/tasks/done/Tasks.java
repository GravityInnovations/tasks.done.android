package com.gravity.innovations.tasks.done;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
//Tasks handles methods for database of task create database, enter a task, view tasks list
public class Tasks{	
	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	private static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {
			super(context, Common.QueryDB.DATABASE_NAME, null, Common.QueryDB.DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(Common.QueryDB.CREATE_QUERY);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS" + Common.QueryDB.DATABASE_TABLE);
			onCreate(db);
		}
		
	}
	public Tasks(Context c){
		ourContext = c;
		
	}
	public Tasks open() throws SQLException{
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}
	public void close(){
		ourHelper.close();
	}
	public long createEntry(String taskTitle, String tasksDetails) {
		// TODO Auto-generated method stub
		
		ContentValues cv = new ContentValues();
		cv.put(Common.QueryDB.KEY_TASKTITLE, taskTitle);
		cv.put(Common.QueryDB.KEY_TASKDETAILS, tasksDetails);
		return ourDatabase.insert(Common.QueryDB.DATABASE_TABLE, null, cv);
	}
	public String getData() {
		// TODO Auto-generated method stub
		String[] columns= new String[]{Common.QueryDB.KEY_ROWID,Common.QueryDB.KEY_TASKTITLE,Common.QueryDB.KEY_TASKDETAILS};
		Cursor c = ourDatabase.query(Common.QueryDB.DATABASE_TABLE, columns, null, null, null, null, null); 
		String results="";
		int iRow = c.getColumnIndex(Common.QueryDB.KEY_ROWID);
		int iTitle = c.getColumnIndex(Common.QueryDB.KEY_TASKTITLE);
		int iDetails = c.getColumnIndex(Common.QueryDB.KEY_TASKDETAILS);
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
			results = results + c.getString(iRow) + " " + c.getString(iTitle) + " " + c.getString(iDetails) + "\n";
		}
		return results;
	}
}
