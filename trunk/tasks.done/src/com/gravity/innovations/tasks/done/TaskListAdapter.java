package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListAdapter extends ArrayAdapter<TaskListModel> {
	TaskListModel tasklist;
	Activity mActivity;
	int layoutResourceId;
	ArrayList<TaskListModel> data = new ArrayList<TaskListModel>();
	
	public TaskListAdapter(Activity activity, int layoutResourceId, ArrayList<TaskListModel> data) {
		super(activity, layoutResourceId, data);
		// TODO Auto-generated constructor stub
		this.mActivity = activity;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
		notifyDataSetChanged();
	}
	public long getItemId (int position)
	{
		int id = data.get(position)._id;
		return id;
	}
	public int getSelectedListId(int position) {
		int id = data.get(position)._id;
	//	id--; 
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TaskListHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(mActivity);
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new TaskListHolder();
			holder.title = (TextView) row
					.findViewById(R.id.txt_title);
			row.setTag(holder);
		} else {
			holder = (TaskListHolder) row.getTag();
 		}
//		row.setBackgroundColor(mActivity.getResources().getColor(
//				android.R.color.background_light)); // default color
		tasklist = data.get(position);
		holder.title.setText(tasklist.title);
		return row;
	}

	class TaskListHolder {
		TextView title;
	}
}
