package com.gravity.innovations.tasks.done;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class DialogViewFragment extends DialogFragment {

	private View view;
	private TextView tv_title, tv_details, tv_notes, tv_updated, tv_sync_time;
	private ImageView markDoneToggle, taskEdit, taskShare, taskDelete;
	private TaskListModel listModel;
	private TaskModel taskModel;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DialogViewFragment dialog;
 
	public DialogViewFragment(TaskListModel listModel, TaskModel taskModel,
			NavigationDrawerFragment ndf) {
		this.listModel = listModel;
		this.taskModel = taskModel;
		this.mNavigationDrawerFragment = ndf;
		//this.dialog = _dialog = this.dialog = this;
		this.dialog = this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.dialog_task_full_details, container);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		tv_title = (TextView) view.findViewById(R.id.txt_task_name);
		tv_details = (TextView) view.findViewById(R.id.txt_details);
		tv_notes = (TextView) view.findViewById(R.id.txt_notes);
		tv_updated = (TextView) view.findViewById(R.id.txt_time_updated);
		// TextView tv_due = (TextView) view.findViewById(R.id.);
		tv_sync_time = (TextView) view.findViewById(R.id.txt_time_synced);

		markDoneToggle = (ImageView) view.findViewById(R.id.detail_done_toggle);

		taskEdit = (ImageView) view
				.findViewById(R.id.btn_edit_task_detail_dialog);

		taskShare = (ImageView) view
				.findViewById(R.id.btn_share_task_detail_dialog);

		taskDelete = (ImageView) view
				.findViewById(R.id.btn_delete_task_detail_dialog);

		// ********************AssigningTextViews*********************//
		// assigning title
		tv_title.setText(taskModel.title.toString());
		// assigning details
		String details = taskModel.details.toString();
		if (details.isEmpty()) {
			details = "no details yet";
			tv_details.setText(details);
		} else {
			tv_details.setText(details);
		}
		// assigning notes
		String notes = taskModel.notes.toString();
		if (notes.isEmpty()) {
			notes = "no details yet";
			tv_notes.setText(notes);
		} else {
			tv_notes.setText(notes);
		}
		// long to string time formatting
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd " + " "
				+ "hh:MM:ss");
		// assigning updated time
		if (taskModel.updated == null) {
			tv_updated.setText("Last Updated: not updated yet");
		} else {
			long update = Long.parseLong(taskModel.updated.toString());
			String dateString = formatter.format(new Date(update));
			tv_updated.setText("Last Updated: " + dateString);
		}
		// assigning sync time
		try {
			if (taskModel.syncStatusTimeStamp == null) {
				tv_sync_time.setText("Last Synced: not synced yet");
			} else {
				String lastSyced = (taskModel.syncStatusTimeStamp).toString();
				long lastSyncTime = Long.parseLong(lastSyced);
				String syncString = formatter.format(new Date(lastSyncTime));
				tv_sync_time.setText("Last Synced: " + syncString);
			}
		} catch (Exception e) {
			Log.e("AssigningSycedTimeStamp", "NDF openTaskDetailsDialog");
		}
		// ********************ImageViews*********************//
		if (taskModel.completed == 1) {
			markDoneToggle.setBackgroundResource(R.drawable.task_row_done_bg);
		} else if (taskModel.completed == 0) {
			markDoneToggle.setBackgroundResource(R.drawable.task_row_bg);
		}

		// taskDelete: to delete the task
		taskDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					ArrayList<Integer> arrayList = new ArrayList<Integer>();
					arrayList.add(taskModel._id);
					mNavigationDrawerFragment.deleteTask(listModel, arrayList);
					dialog.dismiss();
				} catch (Exception e) {
					Log.e("DialogViewFragment", "taskDelete");
				}

			}
		});

		// taskEdit: to edit the task
		taskEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				mNavigationDrawerFragment.addOrEditTask(listModel, taskModel);
			}
		});

		// taskShare: to share the task
		taskShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// code here for share
				dialog.dismiss();
			}
		});

		// markDoneToggle: to mark the task as done or otherwise
		markDoneToggle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (taskModel.completed == 1) {
					markDoneToggle
							.setBackgroundResource(R.drawable.task_row_bg);
					taskModel.completed = 0;
					mNavigationDrawerFragment
							.MarkDoneTask(listModel, taskModel);
				} else if (taskModel.completed == 0) {
					taskModel.completed = 1;
					mNavigationDrawerFragment
							.MarkDoneTask(listModel, taskModel);
					markDoneToggle
							.setBackgroundResource(R.drawable.task_row_done_bg);
				}
			}
		});

	}

}
