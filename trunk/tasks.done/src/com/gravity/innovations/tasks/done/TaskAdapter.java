package com.gravity.innovations.tasks.done;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskAdapter extends ArrayAdapter<TaskModel> {
	Activity activity;
	int layoutResourceId;
	TaskModel task;
	int MAX_CHARS = 10;
	ArrayList<TaskModel> task_data = new ArrayList<TaskModel>();
	private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

	public TaskAdapter(Activity act, int layoutResourceId,
			ArrayList<TaskModel> data) {
		super(act, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.activity = act;
		this.task_data = data;
		notifyDataSetChanged();
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

	public void setNewSelection(int position, boolean value) {
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

	public void clearSelection() {
		mSelection = new HashMap<Integer, Boolean>();
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TaskModelHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(activity);
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new TaskModelHolder();
			holder.title = (TextView) row.findViewById(R.id.user_task_title);
			holder.details = (TextView) row
					.findViewById(R.id.user_task_details);
			holder.notes = (TextView) row.findViewById(R.id.user_task_notes);
			holder.update = (TextView) row.findViewById(R.id.updated);

			row.setTag(holder);
		} else {
			holder = (TaskModelHolder) row.getTag();
		}

		row.setBackgroundColor(activity.getResources().getColor(
				android.R.color.background_light)); // default color

		if (mSelection.get(position) != null) {
			row.setBackgroundColor(activity.getResources().getColor(
					android.R.color.holo_blue_light));
		}
		task = task_data.get(position);

		/*
		 * 
		 * String givenDateString // = holder.update.getText().toString(); =
		 * "Tue Apr 24 11:00:00 GMT+05:30 2013"; SimpleDateFormat sdf = new
		 * SimpleDateFormat( "EEE MMM dd HH:mm:ss z yyyy");
		 */
		try {
			// Date mDate = sdf.parse(givenDateString);
			// long timeInMilliseconds = mDate.getTime();
			// System.out.println("Date in milli :: " + timeInMilliseconds);
			// String s = String.valueOf(timeInMilliseconds);
			// int old = Integer.parseInt(s);
			// Date d = new Date();
			// CharSequence sss = DateFormat.format("EEEE, MMMM d, yyyy ",
			// d.getTime());
			// String dd = sss.toString();
			// int neww = Integer.parseInt(dd);
			// int current = old - neww;
			// String curr = String.valueOf(current);
			// holder.update.setText(curr);

			/******************************/
			// limits have to be defined

			Calendar calendarIO = Calendar.getInstance();
			calendarIO.set(2014, 8, 12, 23, 59);
			// calendarIO.set(2014, 2, 14, 7, 0);

			long milliseconds1 = calendarIO.getTimeInMillis();
			long milliseconds2 = System.currentTimeMillis();
			long diff = milliseconds2 - milliseconds1;
			long diffSeconds = diff / 1000;
			long diffMinutes = diff / (60 * 1000);
			long diffHours = diff / (60 * 60 * 1000);
			long diffDays = diff / (24 * 60 * 60 * 1000);

			String relativeTime = null;
			if (diffDays > 1) {
				relativeTime = diffDays + " days ";
			} else if (diffDays > 0) {
				relativeTime = diffDays + " days " + diffHours + " hours";
			} else if (diffHours > 1) {
				relativeTime = diffHours + " hours";
			} else if (diffMinutes > 0) {
				relativeTime = diffMinutes + " minutes.";
			}
			holder.update.setText(relativeTime);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// try {
		// if (holder.title.getText().toString().length() >= MAX_CHARS) {
		// // String temp= task.title.toString();
		// holder.title.setText(task.title.toString().substring(0,
		// MAX_CHARS)
		// + "...");
		// }
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

		holder.title.setText(task.title);
		holder.details.setText(task.details);
		holder.notes.setText(task.notes);
		// holder.update.setText(task.updated);
		return row;
	}

	class TaskModelHolder {
		TextView title, details, notes, update;
	}

}