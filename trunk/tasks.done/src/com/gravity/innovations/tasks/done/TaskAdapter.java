package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
			holder.toggle = (ImageView) row.findViewById(R.id.done_toggle);
			holder.toggle.setTag(R.drawable.task_row_bg);
			holder.toggle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//					if(((ImageView)v).getBackground() == 
//							activity.getResources().getDrawable(R.drawable.task_row_bg)
//					)
					if ((Integer)v.getTag() == R.drawable.task_row_bg) {
						((ImageView)v).setImageResource(R.drawable.task_row_done_bg);
	                    v.setTag(R.drawable.task_row_done_bg);
	                } 	
					else
					{
						((ImageView)v).setImageResource(R.drawable.task_row_bg);
	                    v.setTag(R.drawable.task_row_bg);
					}
					
//					
					//.setBackgroundResource(R.drawable.task_row_done_bg);
				}
			});

			row.setTag(holder);
		} else {
			holder = (TaskModelHolder) row.getTag();
		}

		row.setBackgroundColor(activity.getResources().getColor(
				android.R.color.background_light)); // default color
		((LinearLayout)row.findViewById(R.id.submenu)).setVisibility(View.GONE);
		if (mSelection.get(position) != null) {
			row.setBackgroundColor(activity.getResources().getColor(
					android.R.color.holo_blue_light));
			((LinearLayout)row.findViewById(R.id.submenu)).setVisibility(View.VISIBLE);
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
		ImageView toggle;
	}

}