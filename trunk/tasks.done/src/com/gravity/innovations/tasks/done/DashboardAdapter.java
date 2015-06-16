package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DashboardAdapter extends
		RecyclerView.Adapter<DashboardAdapter.RecyclerArrayHolder> implements
		Filterable {
	ArrayList<TaskModel> task_data = new ArrayList<TaskModel>();
	ArrayList<TaskModel> data_backup = new ArrayList<TaskModel>();
	NavigationDrawerFragment mNavigationDrawerFragment;
	Context mContext;

	public DashboardAdapter(Activity act, NavigationDrawerFragment NavDrawer,
			ArrayList<TaskModel> data) {
		this.task_data = data;
		data_backup = data;
		mNavigationDrawerFragment = NavDrawer;
		notifyDataSetChanged();
		mContext = act;
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
			ArrayList<TaskModel> temp = (ArrayList<TaskModel>) results.values;
			if (temp.size() == 0) {
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
		return task_data.size();
	}

	@Override
	public void onBindViewHolder(RecyclerArrayHolder viewHolder, int position) {
		final TaskModel task = getItem(position);

		if (task.title.toLowerCase().contains("call")
				|| task.title.toLowerCase().contains("sms")
				|| task.title.toLowerCase().contains("email")) {

			String uri = task.title;
			String type = null;
			if (task.title.toLowerCase().contains("call")) {
				uri = uri.substring(5);
				type = "Call ";
			} else if (task.title.toLowerCase().contains("sms")) {
				uri = uri.substring(4);
				type = "Sms ";
			} else if (task.title.toLowerCase().contains("email")) {
				uri = uri.substring(6);
				type = "Email ";
			}

			viewHolder.tv_title.setText(type
					+ Common.ContactStringConversion.getDisplayName(
							Uri.parse(uri), mContext));

		} else {
			viewHolder.tv_title.setText(task.title);
		}

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
				R.layout.row_dashboard, null);

		RecyclerArrayHolder viewHolder = new RecyclerArrayHolder(itemLayoutView);
		return viewHolder;
	}

	public TaskModel getItem(int position) {
		TaskModel task = task_data.get(position);
		return task;
	}
}
