package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Main_Screen  extends Activity {
    Button add_btn;
    ListView Task_listview;
    ArrayList<Task> task_data = new ArrayList<Task>();
    Task_Adapter cAdapter;
    DatabaseHandler db;
    String Toast_msg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	try {
	    Task_listview = (ListView) findViewById(R.id.list);
	    Task_listview.setItemsCanFocus(false);
	    add_btn = (Button) findViewById(R.id.add_btn);

	    Set_Referash_Data();

	} catch (Exception e) {
	    // TODO: handle exception
	    Log.e("some error", "" + e);
	}
	add_btn.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent add_user = new Intent(Main_Screen.this,
			Add_Update_Task.class);
		add_user.putExtra("called", "add");
		add_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
			| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(add_user);
		finish();
	    }
	});

    }

    public void Set_Referash_Data() {
	task_data.clear();
	db = new DatabaseHandler(this);
	ArrayList<Task> contact_array_from_db = db.Get_Tasks();

	for (int i = 0; i < contact_array_from_db.size(); i++) {

	    int tidno = contact_array_from_db.get(i).getID();
	    String title = contact_array_from_db.get(i).getTitle();
	    String details = contact_array_from_db.get(i).getDetails();
	    String notes = contact_array_from_db.get(i).getNotes();
	    Task cnt = new Task();
	    cnt.setID(tidno);
	    cnt.setTitle(title);
	    cnt.setDetails(details);
	    cnt.setNotes(notes);

	    task_data.add(cnt);
	}
	db.close();
	cAdapter = new Task_Adapter(Main_Screen.this, R.layout.listview_row,
		task_data);
	Task_listview.setAdapter(cAdapter);
	cAdapter.notifyDataSetChanged();
    }

    public void Show_Toast(String msg) {
	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	Set_Referash_Data();

    }

    public class Task_Adapter extends ArrayAdapter<Task> {
	Activity activity;
	int layoutResourceId;
	Task user;
	ArrayList<Task> data = new ArrayList<Task>();

	public Task_Adapter(Activity act, int layoutResourceId,
		ArrayList<Task> data) {
	    super(act, layoutResourceId, data);
	    this.layoutResourceId = layoutResourceId;
	    this.activity = act;
	    this.data = data;
	    notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View row = convertView;
	    UserHolder holder = null;

	    if (row == null) {
		LayoutInflater inflater = LayoutInflater.from(activity);

		row = inflater.inflate(layoutResourceId, parent, false);
		holder = new UserHolder();
		holder.title = (TextView) row.findViewById(R.id.user_task_title);
		holder.details = (TextView) row.findViewById(R.id.user_task_details);
		holder.notes = (TextView) row.findViewById(R.id.user_task_notes);
		holder.edit = (Button) row.findViewById(R.id.btn_update);
		holder.delete = (Button) row.findViewById(R.id.btn_delete);
		row.setTag(holder);
	    } else {
		holder = (UserHolder) row.getTag();
	    }
	    user = data.get(position);
	    holder.edit.setTag(user.getID());
	    holder.delete.setTag(user.getID());
	    holder.title.setText(user.getTitle());
	    holder.details.setText(user.getDetails());
	    holder.notes.setText(user.getNotes());

	    holder.edit.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		    // TODO Auto-generated method stub
		    Log.i("Edit Button Clicked", "**********");

		    Intent update_user = new Intent(activity,
			    Add_Update_Task.class);
		    update_user.putExtra("called", "update");
		    update_user.putExtra("USER_ID", v.getTag().toString());
		    activity.startActivity(update_user);

		}
	    });
	    holder.delete.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(final View v) {
		    // TODO Auto-generated method stub

		    // show a message while loader is loading

		    AlertDialog.Builder adb = new AlertDialog.Builder(activity);
		    adb.setTitle("Delete?");
		    adb.setMessage("Are you sure you want to delete ");
		    final int user_id = Integer.parseInt(v.getTag().toString());
		    adb.setNegativeButton("Cancel", null);
		    adb.setPositiveButton("Ok",
			    new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
					int which) {
				    // MyDataObject.remove(positionToRemove);
				    DatabaseHandler dBHandler = new DatabaseHandler(
					    activity.getApplicationContext());
				    dBHandler.Delete_Task(user_id);
				    Main_Screen.this.onResume();

				}
			    });
		    adb.show();
		}

	    });
	    return row;

	}

	class UserHolder {
	    TextView title;
	    TextView details;
	    TextView notes;
	    Button edit;
	    Button delete;
	}

    }

}
