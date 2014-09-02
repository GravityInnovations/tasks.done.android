package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskAdapter extends ArrayAdapter<TaskModel> {
	Activity activity;
	int layoutResourceId;
	TaskModel task;
	ArrayList<TaskModel> task_data = new ArrayList<TaskModel>();
	private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

	public TaskAdapter(Activity act, int layoutResourceId, ArrayList<TaskModel> data) {
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
		holder.title.setText(task.title);
		holder.details.setText(task.details);
		holder.notes.setText(task.notes);
		return row;
	}
	
	class TaskModelHolder {
		TextView title;
		TextView details;
		TextView notes;
	}

}