package com.gravity.innovations.tasks.done;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.gravity.innovations.custom.views.CalendarView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
	private CalendarView cal;
	private Activity mActivity;

	public DialogViewFragment(TaskListModel listModel, TaskModel taskModel,
			NavigationDrawerFragment ndf, Activity activity) {
		this.listModel = listModel;
		this.taskModel = taskModel;
		this.mNavigationDrawerFragment = ndf;
		// this.dialog = _dialog = this.dialog = this;
		this.dialog = this;
		mActivity = activity;
	}

	@Override
	public void show(FragmentManager manager, String tag) {
		// TODO Auto-generated method stub
		super.show(manager, tag);
		if (cal != null)
			cal.ScrollToToday();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.dialog_task_details, container);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		if (cal != null)
			cal.ScrollToToday();
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		int MAX_CHARS = 7;
		tv_title = (TextView) view.findViewById(R.id.txt_task_name);
		tv_details = (TextView) view.findViewById(R.id.txt_details);
		tv_notes = (TextView) view.findViewById(R.id.txt_notes);
		tv_updated = (TextView) view.findViewById(R.id.txt_time_updated);
		tv_sync_time = (TextView) view.findViewById(R.id.txt_time_synced);
		cal = (CalendarView) view.findViewById(R.id.calendar1);
		markDoneToggle = (ImageView) view.findViewById(R.id.detail_done_toggle);

		taskEdit = (ImageView) view
				.findViewById(R.id.btn_edit_task_detail_dialog);

		taskShare = (ImageView) view
				.findViewById(R.id.btn_share_task_detail_dialog);

		taskDelete = (ImageView) view
				.findViewById(R.id.btn_delete_task_detail_dialog);

		try {
			// assigning title
			if (taskModel.title.toLowerCase().contains("call")
					|| taskModel.title.toLowerCase().contains("sms")
					|| taskModel.title.toLowerCase().contains("email")) {
				String uri = taskModel.title;
				String type = null;
				if (taskModel.title.toLowerCase().contains("call")) {
					uri = uri.substring(5);
					type = "Call ";
				} else if (taskModel.title.toLowerCase().contains("sms")) {
					uri = uri.substring(4);
					type = "Sms ";
				} else if (taskModel.title.toLowerCase().contains("email")) {
					uri = uri.substring(6);
					type = "Email ";
				}
				String temp = type
						+ Common.ContactStringConversion.getDisplayName(
								Uri.parse(uri), mActivity);
				
				if (temp.length() > MAX_CHARS) {
					tv_title.setText(temp.substring(0, MAX_CHARS)
							+ "...");
				} else {
					tv_title.setText(temp);
				}
				
			} else {
				//tv_title.setText(taskModel.title.toString());
				
				if (taskModel.title.length() > MAX_CHARS) {
					String temp = taskModel.title.toString().substring(0, MAX_CHARS)
							+ "...";
					tv_title.setText(temp);
				} else {
					tv_title.setText(taskModel.title);
				}
			}
		} catch (Exception e) {
			tv_title.setText("title");
		}
		try {
			// assigning details
			tv_details.setText(taskModel.details.toString());
		} catch (Exception e) {
			tv_details.setText("has no details yet");
		}
		try {
			// assigning notes
			tv_notes.setText(taskModel.notes.toString());
		} catch (Exception e) {
			tv_notes.setText("has no notes yet");
		}

		// long to string time formatting
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd " + " "
				+ "hh:MM:ss");

		if (taskModel.DateUpdated == null) {
			tv_updated.setText("Last Updated: not updated yet");
		} else {
			long update = Long.parseLong(taskModel.DateUpdated.toString());
			String dateString = formatter.format(new Date(update));
			tv_updated.setText("Last Updated: " + dateString);
		}
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
			Log.e("AssigningSyFcedTimeStamp", "NDF openTaskDetailsDialog");
		}

		float alpha = 0;
		if (taskModel.completed == 1) {
			alpha = 1.0f;
			// markDoneToggle.setBackgroundResource(R.drawable.task_row_done_bg);
		} else if (taskModel.completed == 0) {
			alpha = 0.10f;
			// markDoneToggle.setBackgroundResource(R.drawable.task_row_bg);
		}
		markDoneToggle.setBackgroundColor(Common.ShapesAndGraphics.adjustAlpha(
				Color.parseColor(listModel.fragmentColor), alpha));

		taskDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					ArrayList<Integer> arrayList = new ArrayList<Integer>();
					arrayList.add(taskModel._id);
					mNavigationDrawerFragment
							.deleteTask(listModel, taskModel/* arrayList */);
					dialog.dismiss();
				} catch (Exception e) {
					Log.e("DialogViewFragment", "taskDelete");
				}

			}
		});

		taskEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				// ZZ**ZZ mNavigationDrawerFragment.addOrEditTask(listModel,
				// taskModel);
			}
		});

		taskShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// code here for share
				dialog.dismiss();
			}
		});

		markDoneToggle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				float alphaToggle = 0;
				if (taskModel.completed == 1) {
					alphaToggle = 0.10f;
					// markDoneToggle
					// .setBackgroundResource(R.drawable.task_row_bg);
					taskModel.completed = 0;

				} else if (taskModel.completed == 0) {
					alphaToggle = 1.0f;
					taskModel.completed = 1;
					// markDoneToggle
					// .setBackgroundResource(R.drawable.task_row_done_bg);

				}
				try {
					markDoneToggle.setBackgroundColor(Common.ShapesAndGraphics
							.adjustAlpha(
									Color.parseColor(listModel.fragmentColor),
									alphaToggle));
					Animation animationFadeIn = AnimationUtils.loadAnimation(
							mActivity, android.R.anim.fade_in);
					markDoneToggle.startAnimation(animationFadeIn);
				} catch (Exception e) {
					Log.e("markDoneToggle", e.getLocalizedMessage());
				}

				mNavigationDrawerFragment.MarkDoneTask(listModel, taskModel);

			}
		});
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mNavigationDrawerFragment.mAdapter.notifyDataSetChanged();
	}
}
