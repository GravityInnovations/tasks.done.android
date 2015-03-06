package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardAdapter extends
		RecyclerView.Adapter<DashboardAdapter.RecyclerArrayHolder> implements
		Filterable {
	ArrayList<TaskModel> task_data = new ArrayList<TaskModel>();
	ArrayList<TaskModel> data_backup = new ArrayList<TaskModel>();
	int layoutResourceId, position;
	NavigationDrawerFragment mNavigationDrawerFragment;
	TaskListAdapter mTaskListAdapter;
	Activity mActivity;
	Context mContext;
	// private View footer;
	// static EditText search;
	//DatabaseHelper db;

	public DashboardAdapter(Activity act, int layoutResourceId, NavigationDrawerFragment NavDrawer,
			ArrayList<TaskModel> data
	// ,View footer,EditText search_et
	// ,TaskListAdapter _mTaskListAdapter
	) {
		// super(act, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.mActivity = act;
		this.task_data = data;
		data_backup = data;
		mNavigationDrawerFragment = NavDrawer;
		notifyDataSetChanged();
		mContext = act;
		// this.footer = footer;
		// this.search = search_et;
	}

	long currentTimeMills = System.currentTimeMillis();
	private Filter collectionFilter;

	protected static class RecyclerArrayHolder extends RecyclerView.ViewHolder {

		public TextView tv_title, tv_updatedAt, tv_list_title;
		public View rowIdentifier;
		public RelativeLayout row;

		public RecyclerArrayHolder(View itemView) {
			super(itemView);
			tv_title = (TextView) itemView.findViewById(R.id.tv_task_title);
			tv_updatedAt = (TextView) itemView.findViewById(R.id.updated_time);
			tv_list_title = (TextView) itemView
					.findViewById(R.id.tv_list_title);
			rowIdentifier = (View) itemView.findViewById(R.id.listColor);
			row = (RelativeLayout) itemView.findViewById(R.id.row_layout);
		}

	}

	public Filter getFilter() {
		if (collectionFilter == null)
			collectionFilter = new CollectionFilter();

		return collectionFilter;
	}

	public class CollectionFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			ArrayList<TaskModel> filteredResults = new ArrayList<TaskModel>();
			// getFilteredResults(constraint);

			for (TaskModel temp : data_backup) {
				if (temp.title.toLowerCase().contains(
						constraint.toString().toLowerCase()))
					filteredResults.add(temp);
			}
			FilterResults results = new FilterResults();
			results.values = filteredResults;
			return results;
		}

		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// data_backup = data; // data_backup source is un-filtered data
			// data = (ArrayList<TaskListModel>) results.values;

			ArrayList<TaskModel> temp = (ArrayList<TaskModel>) results.values;
			if (temp.size() == 0) { // .isEmpty()){
				if (constraint == "")
					task_data = data_backup;
				else
					task_data = new ArrayList<TaskModel>();
				Log.d("AdapterFilterError", "Data is empty");

			} else {
				Log.d("AdapterFilterError", "Data is not Empty");
				task_data = temp;
			}

			notifyDataSetChanged();
		}
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return task_data.size();
	}

	@Override
	public void onBindViewHolder(RecyclerArrayHolder viewHolder, int position) {
		// TODO Auto-generated method stub

		final TaskModel task = getItem(position);// task_data.get(position);
		
		viewHolder.tv_title.setText(task.title);

		if (task.relativeTime == "" || task.relativeTime == null)
			task.updateRelativeTime(currentTimeMills);
		viewHolder.tv_updatedAt.setText(task.relativeTime);
		
		viewHolder.tv_list_title.setText(task.parent.title);
		String hex = task.parent.fragmentColor;
		viewHolder.rowIdentifier.setBackgroundColor(Color.parseColor(hex));
		
		viewHolder.row.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				mNavigationDrawerFragment.selectItem(task);
			}
		});
	}

	@Override
	public RecyclerArrayHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.row_task_dashboard, null);

		
		// create ViewHolder
		RecyclerArrayHolder viewHolder = new RecyclerArrayHolder(itemLayoutView);
		//
		return viewHolder;
		// View view = mInflater.inflate(mResource, viewGroup, false);
		// return new RecyclerArrayHolder(itemLayoutView);
	}

	public TaskModel getItem(int position) {
		TaskModel task = task_data.get(position);
		return task;
	}
}
