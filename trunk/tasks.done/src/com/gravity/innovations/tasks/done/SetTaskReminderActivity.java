package com.gravity.innovations.tasks.done;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SetTaskReminderActivity extends ActionBarActivity {

	private TaskModel task;
	private TaskListModel list;
	private ImageButton ib_Save, ib_Cancel;
	private TextView tv_repeat, tv_title, tv_notification;
	private EditText et_title;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_task_reminder);
		mContext = getApplicationContext();

		Intent intent = getIntent();
		Bundle mBundle = intent.getExtras();
		mBundle.getSerializable("key_list");
		mBundle.getSerializable("key_task");
		
		list = (TaskListModel) intent.getSerializableExtra("key_list");
		task = (TaskModel) intent.getSerializableExtra("key_task");

 	    tv_title = (TextView) findViewById(R.id.textViewTitle);
 
		
		// ib_Cancel = (ImageButton) findViewById(R.id.cancel_iv);
		// ib_Save = (ImageButton) findViewById(R.id.accept_iv);
		tv_repeat = (TextView) findViewById(R.id.repeat_tv);

		tv_notification = (TextView) findViewById(R.id.notification_tv);

		tv_title.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) { 
				titleDialog(task);
			}
		});

		tv_repeat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) { 
				repeatDialog();
			}
		});
		tv_notification.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				notificationDialog();
			}
		});

	}
	public void titleDialog(TaskModel task) {// final TaskListModel tasklist, final TaskModel
		// temp) {
//		View view = 
//		EditText myEditText = new EditText(context); // Pass it an Activity or Context
//		myEditText.setLayoutParams(new LayoutParams(..., ...)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
//		myLayout.addView(myEditText);
		
	//	Common.CustomDialog.CustomDialog(this, view);
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		String[] list_items = { "No Notification", "At defined time", "10 minutes before",
//				"30 minutes before", "1 hour before", "Custom..." };
//		builder.setItems(list_items, new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//
//			}
//		});
	builder.setTitle("Reminder");
		builder.show();
	}
	public void repeatDialog() {// final TaskListModel tasklist, final TaskModel
								// temp) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] list_items = { "Does not repeat", "Every day", "Every week",
				"Every month", "Every year", "Custom..." };
		builder.setItems(list_items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				 Toast.makeText(getApplicationContext(), "RepeatDialog " + which,
			 Toast.LENGTH_LONG).show();

			}
		});
		builder.setTitle("Reminder");
		builder.show();

		// final ListView modeList = new ListView(mContext);
		//
		// String[] list_item = { "Once", "Once a Day", "Once a Week",
		// "Once a Month", "Once a Year" };
		//
		// ArrayAdapter<String> itemsAdapter = new
		// ArrayAdapter<String>(mContext,
		// android.R.layout.simple_list_item_1, list_item);
		//
		// modeList.setAdapter(itemsAdapter);
		// builder.setView(modeList);
		// builder.setCancelable(false);
		// final Dialog dialog = builder.create();
		// dialog.show();
		//
		// modeList.setOnItemClickListener(new OnItemClickListener() {
		// public void onItemClick(AdapterView<?> myAdapter, View myView,
		// int myItemInt, long mylng) {
		// // selectRepeatDialogItemId(tasklist, myItemInt, temp);//
		// // listitemid
		// dialog.dismiss();
		// }
		// });
	}

	public void notificationDialog() {// final TaskListModel tasklist, final TaskModel
		// temp) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] list_items = { "No Notification", "At defined time", "10 minutes before",
				"30 minutes before", "1 hour before", "Custom..." };
		builder.setItems(list_items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.setTitle("Reminder");
		builder.show();
	}
}
