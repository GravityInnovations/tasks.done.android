package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TaskAdapter extends ArrayAdapter<TaskModel> {
	Activity activity;
	int layoutResourceId;
	TaskModel task;
	int MAX_CHARS = 10;
	ArrayList<TaskModel> task_data = new ArrayList<TaskModel>();
	final long currentTimeMills = System.currentTimeMillis();
	private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
	int flag = 0;
	Context mContext;
	int HighlightTask;
	TaskListModel mTaskListModel;
	NavigationDrawerFragment mNavigationDrawerFragment;

	public TaskAdapter(Activity act, int layoutResourceId,
			TaskListModel parentTaskList, NavigationDrawerFragment NavDrawer,
			ArrayList<TaskModel> data, int _selectedTaskId) {
		super(act, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.activity = act;
		this.task_data = data;
		this.HighlightTask = _selectedTaskId;
		mNavigationDrawerFragment = NavDrawer;
		mTaskListModel = parentTaskList;
		notifyDataSetChanged();
		mContext = act.getApplication().getApplicationContext();
	}

	public long getItemId(int position) {
		int id = task_data.get(position)._id;
		return id;
	}

	public TaskModel getSingularSelectedTaskModel() {
		if (mSelection.size() == 1) {
			for (Integer temp : getCurrentCheckedPosition())
				return task_data.get(temp);
		}
		return null;
	}

	public ArrayList<Integer> getSelectedTaskModels() {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < task_data.size(); i++) {
			if (isPositionChecked(i)) {
				temp.add(task_data.get(i)._id);
			}
		}
		return temp;
	}

	// unused method
	// public void sellectThisThing(int position) {
	// mSelection.put(position, null);
	// }

	public void setNewSelection(int position, boolean value) {
		/*
		 * for fixing gridView problems
		 */
		//--position;
		mSelection.put(position, value);
		notifyDataSetChanged();
	}

	public boolean isPositionChecked(int position) {
		Boolean result = mSelection.get(position);
		return result == null ? false : result;
	}

	public Set<Integer> getCurrentCheckedPosition() {
		return mSelection.keySet();
	}

	public void removeSelection(int position) {
		mSelection.remove(position);
		notifyDataSetChanged();
	}

	public void removeAllSelection() {
		mSelection.clear();// .remove();
		notifyDataSetChanged();
	}

	public void clearSelection() {
		mSelection = new HashMap<Integer, Boolean>();
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		// try{
		TaskModelHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(activity);
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new TaskModelHolder();
			holder.title = (TextView) row.findViewById(R.id.user_task_title);
			holder.details = (TextView) row
					.findViewById(R.id.user_task_details);
			holder.notes = (TextView) row.findViewById(R.id.user_task_notes);

			holder.toggle = (ImageView) row.findViewById(R.id.done_toggle);

			holder.toggle.setTag(R.drawable.task_row_bg);

			holder.alarm_toggle = (ImageView) row
					.findViewById(R.id.alarm_toggle);

			holder.gridView = (GridView) row
					.findViewById(R.id.tasklist_gridview);

			final int pos = position;
			final View row2 = row;

			holder.toggle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// if(((ImageView)v).getBackground()==activity.getResources().getDrawable(R.drawable.task_row_bg))
					if ((Integer) v.getTag() == R.drawable.task_row_bg) {

						TaskModel temp = task_data.get(pos);
						temp.updateTimeNow();
						temp.completed = 1;
						boolean flag = mNavigationDrawerFragment.MarkDoneTask(
								mTaskListModel, temp);

						if (flag) {
							((ImageView) v)
									.setImageResource(R.drawable.task_row_done_bg);
							v.setTag(R.drawable.task_row_done_bg);
							temp.title = temp.title;

							notifyDataSetChanged();
							rearrangeList(pos, row2);
							// animatedSwapWithNextItem(pos);
						}

						// for removing alarms

						// swapListItem(pos, task_data, row2);// final modifier

					} else {
						TaskModel temp = task_data.get(pos);
						temp.completed = 0;
						temp.updateTimeNow();
						boolean flag = mNavigationDrawerFragment.MarkDoneTask(
								mTaskListModel, temp);
						if (flag) {
							((ImageView) v)
									.setImageResource(R.drawable.task_row_bg);
							v.setTag(R.drawable.task_row_bg);
							notifyDataSetChanged();
						}

						// swapListItem(pos, task_data, row2);// final modifier
					}

					//
					// .setBackgroundResource(R.drawable.task_row_done_bg);
				}
			});

			holder.update = (TextView) row.findViewById(R.id.updated);

			row.setTag(holder);
		} else {
			holder = (TaskModelHolder) row.getTag();
		}
		RelativeLayout rl = (RelativeLayout) row.findViewById(R.id.one);
		rl.setBackgroundColor(activity.getResources().getColor(
				android.R.color.background_light)); // default color
		// ((LinearLayout)row.findViewById(R.id.submenu)).setVisibility(View.GONE);
		if (mSelection.get(position) != null) {
			// ((LinearLayout)row.findViewById(R.id.submenu)).setVisibility(View.VISIBLE);
			rl.setBackgroundColor(activity.getResources().getColor(
					R.color.selection_blue));
			// getResources().getColor(android.R.color.holo_blue_light));
		}
		task = task_data.get(position);

		// if (HighlightTask !=-1 && task._id == HighlightTask){
		//
		// Animation animationFadeIn = AnimationUtils
		// .loadAnimation(this.mContext, R.anim.fade_in);
		// row.startAnimation(animationFadeIn);
		// }

		// try {
		// if (holder.title.getText().toString().length() >= MAX_CHARS) {
		// // String temp= task.title.toString();
		// holder.title.setText(task.title.toString().substring(0,
		// MAX_CHARS)
		// + "...");
		// ; }
		//
		// else if (holder.title.getText().toString().length() <= MAX_CHARS) {
		// holder.title.setText(task.title);
		// }
		// } catch (Exception e) {
		// String tag = "TaskAdapter";
		// String msg = "truncation Error";
		// Log.e(tag, msg);
		// }
		//

		if (task.relativeTime == "" || task.relativeTime == null)
			task.updateRelativeTime(currentTimeMills);
		holder.update.setText(task.relativeTime);

		try {
			if (task.completed == 1) {
				holder.toggle.setImageResource(R.drawable.task_row_done_bg);
			} else if (task.completed == 0) {
				holder.toggle.setImageResource(R.drawable.task_row_bg);
			}
		} catch (Exception e) {
			Log.e("taskAdapter", "doneToggle");
		}

		try {
			if (task.remind_interval == 0) {
				holder.alarm_toggle.setVisibility(View.INVISIBLE);
			} else {
				holder.alarm_toggle.setVisibility(View.VISIBLE);
				// holder.alarm_toggle.setImageResource(R.drawable.alarm_taskrow_visible);
			}
		} catch (Exception e) {
			Log.e("taskAdapter", "alarmToggle");
		}

		holder.title.setText(task.title);
		holder.details.setText(task.details);
		holder.notes.setText(task.notes);

		// this to show images at tasks
		Integer[] mThumbIds = { R.drawable.catag_home };

		holder.gridView.setAdapter(new ImageGridAdapter(mThumbIds, mContext));

		holder.gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT)
						.show();
			}
		});
		// this to show images at tasks

		// holder.update.setText(task.updated);

		// }catch(Exception e){
		// Log.e("AdapterTask", "Error");
		// }

		// if (HighlightTask !=-1 && task._id == HighlightTask){
		//
		// Animation animationFadeIn = AnimationUtils
		// .loadAnimation(this.mContext, R.anim.fade_in);
		// row.startAnimation(animationFadeIn);
		// }

		return row;
	}


	private void rearrangeList(final int pos, final View row) {
		int currentpos = pos;

		for (int i = pos; i < task_data.size() - 1; i++) {
			boolean flag = animatedSwapWithNextItem(currentpos, row);
			if (flag) {
				currentpos++;
			} else {
				break;
			}
		}
		notifyDataSetChanged();

	}

	private boolean animatedSwapWithNextItem(int pos, View row) {
		TaskModel temp1 = task_data.get(pos);
		if (pos != task_data.size() - 1) {
			TaskModel temp2 = task_data.get(pos + 1);
			if (temp2.completed == 0) {
				task_data.set(pos, temp2);
				task_data.set(pos + 1, temp1);

				// Animation animationFadeIn = AnimationUtils
				// .loadAnimation(this.mContext, R.anim.abc_slide_in_bottom);
				// row.startAnimation(animationFadeIn);
				//
			} else if (temp2.completed == 1) {
				return false;
				
			}

		}
		return false;
	}
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
	public void blinkTask(int id) {

		// if (HighlightTask !=-1 & task._id == HizghlightTask){

		// Animation animationFadeIn = AnimationUtils
		// .loadAnimation(this.mContext, R.anim.fade_in);
		// row.startAnimation(animationFadeIn);
		// }
	}

	class TaskModelHolder {
		TextView title;
		TextView details;
		TextView notes;
		ImageView toggle;
		TextView update;
		ImageView alarm_toggle;

		GridView gridView; // 20 november 2014
	}

}
