package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskAdapterV2 extends RecyclerView.Adapter {

	long currentTimeMills = System.currentTimeMillis();
	ArrayList<TaskModel> task_data = new ArrayList<TaskModel>();
	TaskListModel mTaskListModel;
	int layoutResourceId, position, HighlightTask;
	NavigationDrawerFragment mNavigationDrawerFragment;
	Activity mActivity;
	Context mContext;
	private View footer;
	private RecyclerView mRecyclerView;
	public DefaultItemAnimator anim;

	private static final int TYPE_HEADER = Integer.MIN_VALUE;
	private static final int TYPE_FOOTER = Integer.MIN_VALUE + 1;
	private static final int TYPE_ADAPTEE_OFFSET = 2;

	public TaskAdapterV2(Activity act, int layoutResourceId,
			TaskListModel parentTaskList, NavigationDrawerFragment NavDrawer,
			/* ArrayList<TaskModel> data, */int _selectedTaskId, View footer,
			RecyclerView mRecyclerView) {
		// super(act, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.mActivity = act;

		/* this.task_data = data; */
		this.task_data = parentTaskList.tasks;
		// TaskModel nullTask = new TaskModel();
		// task_data.add(nullTask);// add one more object

		this.HighlightTask = _selectedTaskId;
		mNavigationDrawerFragment = NavDrawer;
		mTaskListModel = parentTaskList;
		notifyDataSetChanged();
		mContext = act.getApplication().getApplicationContext();
		this.footer = footer;
		anim = new DefaultItemAnimator();
		this.mRecyclerView = mRecyclerView;
		// anim.setMoveDuration(3000);
		anim.setSupportsChangeAnimations(true);

	}

	public TaskModel getItem(int position) {
		TaskModel task = task_data.get(position);
		return task;
	}
	
	@Override
	public int getItemCount() {
		int itemCount = getItemCount();
		// if (useHeader()) {
		// itemCount += 1;
		// }
		if (useFooter()) {
			itemCount += 1;
		}
		return itemCount;

	}

	private boolean useFooter() { 
		return true;
	}

	@Override
	public int getItemViewType(int position) {
		// if (position == 0 && useHeader()) {
		// return TYPE_HEADER;
		// }
		if (position == getItemCount() && useFooter()) {
			return TYPE_FOOTER;
		}
		if (getBasicItemType(position) >= Integer.MAX_VALUE
				- TYPE_ADAPTEE_OFFSET) {
			new IllegalStateException(
					"HeaderRecyclerViewAdapter offsets your BasicItemType by "
							+ TYPE_ADAPTEE_OFFSET + ".");
		}
		return getBasicItemType(position) + TYPE_ADAPTEE_OFFSET;
	}

	private int getBasicItemType(int position2) {
		 if (position == 0 && useHeader()) {
	            return TYPE_HEADER;
	        }
	        if (position == getItemCount() && useFooter()) {
	            return TYPE_FOOTER;
	        }
	        if (getBasicItemType(position) >= Integer.MAX_VALUE - TYPE_ADAPTEE_OFFSET) {
	            new IllegalStateException("HeaderRecyclerViewAdapter offsets your BasicItemType by " + TYPE_ADAPTEE_OFFSET + ".");
	        }
	        return getBasicItemType(position) + TYPE_ADAPTEE_OFFSET;
	}

	public void onBindViewHolder(ViewHolder holder, int position) {
		// if (position == 0 && holder.getItemViewType() == TYPE_HEADER) {
		// onBindHeaderView(holder, position);
		// } else
		if (position == getItemCount()
				&& holder.getItemViewType() == TYPE_FOOTER) {
			onBindFooterView(holder, position);
		} else {
			onBindBasicItemView(holder, position - (useHeader() ? 1 : 0));
		}
	}

	private boolean useHeader() {
		// TODO Auto-generated method stub
		return false;
	}

	public void onBindFooterView(ViewHolder holder, int position) {

	}

	public void onBindBasicItemView(ViewHolder holder, int position) {

	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// if (viewType == TYPE_HEADER) {
		// return onCreateHeaderViewHolder(parent, viewType);
		// } else
		if (viewType == TYPE_FOOTER) {
			return onCreateFooterViewHolder(parent, viewType);
		}
		return onCreateBasicItemViewHolder(parent, viewType
				- TYPE_ADAPTEE_OFFSET);
	}

	public ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {

		return null;
	}

	public ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {

		return null;
	}

	public static class FooterHolder extends RecyclerView.ViewHolder {
		LinearLayout footerLayout;

		public FooterHolder(View itemLayoutView) {
			super(itemLayoutView);

		}

	}

	// inner class to hold a reference to each item of RecyclerView
	public static class ViewHolder extends RecyclerView.ViewHolder implements
			View.OnCreateContextMenuListener {

		TextView tv_title;

		public ViewHolder(View itemLayoutView) {
			super(itemLayoutView);
			tv_title = (TextView) itemLayoutView
					.findViewById(R.id.user_task_title);

			itemLayoutView.setOnCreateContextMenuListener(this);

		}

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// menuInfo is null
			menu.add(Menu.NONE, R.id.item_delete, Menu.NONE, "Delete");
			menu.add(Menu.NONE, R.id.item_edit, Menu.NONE, "Edit");
		}

	}

	@Override
	public void onBindViewHolder(
			android.support.v7.widget.RecyclerView.ViewHolder arg0, int position) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = (ViewHolder) arg0;
		// - get data from your itemsData at this position
		// - replace the contents of the view with that itemsData
		// viewHolder.txtViewTitle.setText(itemsData[position].getTitle());

		// Assigning task data to the holders start

		TaskModel task = getItem(position);// task_data.get(position);
		// initFooter(viewHolder, position);

		viewHolder.tv_title.setText(task.title);
	}
}