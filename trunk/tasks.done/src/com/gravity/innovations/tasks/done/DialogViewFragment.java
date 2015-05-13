package com.gravity.innovations.tasks.done;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.gravity.innovations.custom.views.CalendarView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.graphics.Color;
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
		view = inflater.inflate(R.layout.dialog_task_full_details, container);
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

		tv_title = (TextView) view.findViewById(R.id.txt_task_name);
		tv_details = (TextView) view.findViewById(R.id.txt_details);
		tv_notes = (TextView) view.findViewById(R.id.txt_notes);
		tv_updated = (TextView) view.findViewById(R.id.txt_time_updated);
		// TextView tv_due = (TextView) view.findViewById(R.id.);
		tv_sync_time = (TextView) view.findViewById(R.id.txt_time_synced);
		cal = (CalendarView) view.findViewById(R.id.calendar1);
		//
		markDoneToggle = (ImageView) view.findViewById(R.id.detail_done_toggle);

		taskEdit = (ImageView) view
				.findViewById(R.id.btn_edit_task_detail_dialog);

		taskShare = (ImageView) view
				.findViewById(R.id.btn_share_task_detail_dialog);

		taskDelete = (ImageView) view
				.findViewById(R.id.btn_delete_task_detail_dialog);

		// ********************AssigningTextViews*********************//
		try{
			// assigning title
			tv_title.setText( taskModel.title.toString() );
		}catch(Exception e){
			tv_title.setText( "title" );
		}
		try{
			// assigning details 
			tv_details.setText( taskModel.details.toString() );
		}catch(Exception e){
			tv_details.setText( "has no details yet" );
		}
		try{
			// assigning notes
			tv_notes.setText( taskModel.notes.toString() );
		}catch(Exception e){
			tv_notes.setText( "has no notes yet" );
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
			Log.e("AssigningSyFcedTimeStamp", "NDF openTaskDetailsDialog");
		}
		// ********************ImageViews*********************//
		float alpha = 0;
		if (taskModel.completed == 1) {
			alpha = 1.0f;
			// markDoneToggle.setBackgroundResource(R.drawable.task_row_done_bg);
		} else if (taskModel.completed == 0) {
			alpha = 0.10f;
			// markDoneToggle.setBackgroundResource(R.drawable.task_row_bg);
		}
		markDoneToggle.setBackgroundColor(adjustAlpha(
				Color.parseColor(listModel.fragmentColor), alpha));

		// taskDelete: to delete the task
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

		// taskEdit: to edit the task
		taskEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				// ZZ**ZZ mNavigationDrawerFragment.addOrEditTask(listModel,
				// taskModel);
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
					markDoneToggle.setBackgroundColor(adjustAlpha(
							Color.parseColor(listModel.fragmentColor),
							alphaToggle));
					Animation animationFadeIn = AnimationUtils.loadAnimation(
							mActivity, R.anim.fade_in);
					markDoneToggle.startAnimation(animationFadeIn);
				} catch (Exception e) {
					Log.e("markDoneToggle", e.getLocalizedMessage());
				}

				mNavigationDrawerFragment.MarkDoneTask(listModel, taskModel);

			}
		});

		// AsyncTask<Void, Void, Void> t = new AsyncTask<Void, Void, Void>() {
		//
		// @Override
		// protected void onPostExecute(Void result) {
		// // TODO Auto-generated method stub
		// super.onPostExecute(result);
		// cal.ScrollToToday();
		// }
		//
		// @Override
		// protected Void doInBackground(Void... params) {
		// // TODO Auto-generated method stub
		// try {
		// Thread.sleep(3000);
		//
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// return null;
		// }
		// };
		// t.execute();

	}

	@Override
	public void onDetach() {
		super.onDetach();
		mNavigationDrawerFragment.mAdapter.notifyDataSetChanged();
	}

	public int adjustAlpha(int color, float alpha) {
		// float f = alpha/100;
		int a = Math.round(255 * alpha);// ( 255 * (float)());
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		return Color.argb(a, red, green, blue);
	}
}
