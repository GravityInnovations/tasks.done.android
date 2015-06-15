package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListAdapter extends ArrayAdapter<TaskListModel> {
	TaskListModel tasklist;
	Activity mActivity;
	int layoutResourceId;
	int MAX_CHARS = 25;
	ArrayList<TaskListModel> data = new ArrayList<TaskListModel>();
	ArrayList<TaskListModel> data_backup = new ArrayList<TaskListModel>();
	int selected = 0;

	public TaskListAdapter(Activity activity, int layoutResourceId,
			ArrayList<TaskListModel> data) {
		super(activity, layoutResourceId, data);
		this.mActivity = activity;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
		this.data_backup = data;
		notifyDataSetChanged();
	}

	public void setSelection(int position) {
		selected = position;
		notifyDataSetChanged();
	}

	public void updateData(ArrayList<TaskListModel> data1) {
		data_backup = data1;
	}

	public long getItemId(int position) {
		int id = data.get(position)._id;
		return id;
	}

	public int getSelectedListId(int position) {
		int id = data.get(position)._id;
		// id--;
		return id;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Filter getFilter() {
		return new Filter() {
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				ArrayList<TaskListModel> temp = (ArrayList<TaskListModel>) results.values;
				if (temp.size() == 0) { // .isEmpty()){
					if (constraint == "")
						data = data_backup;
					else
						data = new ArrayList<TaskListModel>();
					Log.d("AdapterFilterError", "Data is empty");

				} else {
					Log.d("AdapterFilterError", "Data is not Empty");
					data = temp;
				}
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				ArrayList<TaskListModel> filteredResults = new ArrayList<TaskListModel>();
				for (TaskListModel temp : data_backup) {
					if (temp.title.toLowerCase().contains(
							constraint.toString().toLowerCase()))
						filteredResults.add(temp);
				}
				FilterResults results = new FilterResults();
				results.values = filteredResults;
				return results;
			}
		};
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TaskListHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(mActivity);
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new TaskListHolder();
			holder.title = (TextView) row.findViewById(R.id.txt_title);

			holder.list_icon = (ImageView) row.findViewById(R.id.list_icon);

			row.setTag(holder);
		} else {
			holder = (TaskListHolder) row.getTag();
		}
		// row.setBackgroundColor(mActivity.getResources().getColor(
		// android.R.color.background_light)); // default color

		tasklist = data.get(position);
		try {
			if (selected == position)
				row.setBackgroundColor(Color.parseColor("#efefef"));
			else
				row.setBackgroundColor(Color.parseColor("#ffffffff"));
			if (tasklist.title.length() > MAX_CHARS) {
				String temp = tasklist.title.toString().substring(0, MAX_CHARS)
						+ "...";
				holder.title.setText(temp);
			} else {
				holder.title.setText(tasklist.title);
			}
		} catch (Exception e) {
			String tag = "TasklistAdapter";
			String msg = "truncation Error";
			Log.e(tag, msg);
		}

		try {
			Resources resources = mActivity.getResources();
			// holder.list_icon.setImageDrawable(resources
			// .getDrawable(tasklist.icon_identifier));
			Drawable mDrawable = mActivity.getResources().getDrawable(
					tasklist.icon_identifier);
			mDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
			int iColor = Color.GRAY;
			int red = (iColor & 0xFF0000) / 0xFFFF;
			int green = (iColor & 0xFF00) / 0xFF;
			int blue = iColor & 0xFF;
			float[] matrix = { 0, 0, 0, 0, red, 0, 0, 0, 0, green, 0, 0, 0, 0,
					blue, 0, 0, 0, 1, 0 };
			ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
			mDrawable.setColorFilter(colorFilter);
			holder.list_icon.setImageDrawable(mDrawable);

		} catch (Exception e) {
			String tag = "TasklistAdapter";
			String msg = "listICON";
			Log.e(tag, msg);
		}
		return row;
	}

	class TaskListHolder {
		TextView title;
		ImageView list_icon;
	}
}
