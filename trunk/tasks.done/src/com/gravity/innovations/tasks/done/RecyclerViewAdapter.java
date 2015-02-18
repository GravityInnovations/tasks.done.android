//package com.gravity.innovations.tasks.done;
//
//import java.sql.Date;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Set;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.ContextMenu;
//import android.view.ContextMenu.ContextMenuInfo;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//public class RecyclerViewAdapter extends
//		RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
//
//	long currentTimeMills = System.currentTimeMillis();
//	ArrayList<TaskModel> task_data = new ArrayList<TaskModel>();
//	TaskListModel mTaskListModel;
//	int layoutResourceId, position, HighlightTask;
//	NavigationDrawerFragment mNavigationDrawerFragment;
//	Activity mActivity;
//	Context mContext;
//	private View footer;
//
//	public RecyclerViewAdapter(Activity act, int layoutResourceId,
//			TaskListModel parentTaskList, NavigationDrawerFragment NavDrawer,
//			ArrayList<TaskModel> data, int _selectedTaskId, View footer) {
//		// super(act, layoutResourceId, data);
//		this.layoutResourceId = layoutResourceId;
//		this.mActivity = act;
//		this.task_data = data;
//		this.HighlightTask = _selectedTaskId;
//		mNavigationDrawerFragment = NavDrawer;
//		mTaskListModel = parentTaskList;
//		notifyDataSetChanged();
//		mContext = act.getApplication().getApplicationContext();
//		this.footer = footer;
//	}
//
//	// Create new views (invoked by the layout manager)
//	@Override
//	public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
//			int viewType) {
//		// create a new view
//		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
//				R.layout.task_listview_row, null);
//
//		// create ViewHolder
//
//		ViewHolder viewHolder = new ViewHolder(itemLayoutView);
//
//		return viewHolder;
//	}
//
//	// Replace the contents of a view (invoked by the layout manager)
//	@Override
//	public void onBindViewHolder(ViewHolder viewHolder, int position) {
//
//		// - get data from your itemsData at this position
//		// - replace the contents of the view with that itemsData
//		// viewHolder.txtViewTitle.setText(itemsData[position].getTitle());
//
//		
//		// Assigning task data to the holders start
//		
//		TaskModel task = getItem(position);//task_data.get(position);
//		initFooter(viewHolder,position);
//		
//		viewHolder.tv_title.setText(task.title);
//		viewHolder.tv_details.setText(task.details);
//		viewHolder.tv_notes.setText(task.notes);
//
//		if (task.relativeTime == "" || task.relativeTime == null)
//			task.updateRelativeTime(currentTimeMills);
//		viewHolder.tv_updatedAt.setText(task.relativeTime);
//
//		try {
//			if (task.completed == 1) {
//				viewHolder.iv_doneToggle
//						.setImageResource(R.drawable.task_row_done_bg);
//			} else if (task.completed == 0) {
//				viewHolder.iv_doneToggle
//						.setImageResource(R.drawable.task_row_bg);
//			}
//		} catch (Exception e) {
//			Log.e("TaskAdapter", "iv_doneToggle");
//		}
//
//		try {
//			if (task.remind_interval == 0) {
//				viewHolder.iv_alarmToggle.setVisibility(View.INVISIBLE);
//			} else {
//				viewHolder.iv_alarmToggle.setVisibility(View.VISIBLE);
//			}
//		} catch (Exception e) {
//			Log.e("TaskAdapter", "iv_alarmToggle");
//		}
//		// Assigning task data to the holder finish
//
//		// mark as dne and undone start
//		final int pos = position;
//		viewHolder.iv_doneToggle.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				TaskModel temp = task_data.get(pos);
//				if (temp.completed == 0) {
//					if ((Integer) v.getTag() == R.drawable.task_row_bg) {
//
//						temp.updateTimeNow();
//						temp.completed = 1;
//						boolean flag = mNavigationDrawerFragment.MarkDoneTask(
//								mTaskListModel, temp);
//
//						if (flag) {
//							((ImageView) v)
//									.setImageResource(R.drawable.task_row_done_bg);
//							v.setTag(R.drawable.task_row_done_bg);
//							temp.title = temp.title;
//
//							notifyDataSetChanged();
//						}
//
//					}
//
//				} else {
//					// TaskModel temp = task_data.get(pos);
//
//					if (temp.completed == 1) {
//
//						temp.completed = 0;
//						temp.updateTimeNow();
//						boolean flag = mNavigationDrawerFragment.MarkDoneTask(
//								mTaskListModel, temp);
//						if (flag) {
//							((ImageView) v)
//									.setImageResource(R.drawable.task_row_bg);
//							v.setTag(R.drawable.task_row_bg);
//							notifyDataSetChanged();
//						}
//					}
//
//				}
//
//			}
//		});
//		// mark as dne and undone finish
//
//		// on item Click start
//
//		// on item click finish
//		final ViewHolder vH = viewHolder;
//		viewHolder.itemView
//				.setOnLongClickListener(new View.OnLongClickListener() {
//					@Override
//					public boolean onLongClick(View v) {
//						setPosition(vH.getPosition());
//						return false;
//					}
//				});
//		viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// open details dialog
//				setPosition(vH.getPosition());
//				mNavigationDrawerFragment.openTaskDetailsDialog(mTaskListModel,
//						getItem(vH.getPosition()));
//			}
//		});
//
//	}
//
//	private void initFooter(ViewHolder viewHolder, int position2) {
//		if (position2 == getItemCount() - 1)
//		{
//			viewHolder.footer.addView(footer);
//		}
//			else
//				viewHolder.footer.setVisibility(View.GONE);
//		
//		
//	}
//
//	// inner class to hold a reference to each item of RecyclerView
//	public static class ViewHolder extends RecyclerView.ViewHolder implements
//			View.OnCreateContextMenuListener {
//
//		// public TextView txtViewDetails, txtViewTitle, txtViewNotes,
//		// txtViewUpdated;
//
//		TextView tv_title, tv_details, tv_notes, tv_updatedAt;
//		ImageView iv_doneToggle, iv_alarmToggle;
//		LinearLayout footer;
//
//		public ViewHolder(View itemLayoutView) {
//			super(itemLayoutView);
//			tv_title = (TextView) itemLayoutView
//					.findViewById(R.id.user_task_title);
//			tv_details = (TextView) itemLayoutView
//					.findViewById(R.id.user_task_details);
//			tv_notes = (TextView) itemLayoutView
//					.findViewById(R.id.user_task_notes);
//			tv_updatedAt = (TextView) itemLayoutView
//					.findViewById(R.id.updated1);
//			iv_doneToggle = (ImageView) itemLayoutView
//					.findViewById(R.id.done_toggle);
//			iv_doneToggle.setTag(R.drawable.task_row_bg);
//			iv_alarmToggle = (ImageView) itemLayoutView
//					.findViewById(R.id.alarm_toggle);
//			footer = (LinearLayout)itemLayoutView.findViewById(R.id.footer);
//
//
//			itemLayoutView.setOnCreateContextMenuListener(this);
//
//		}
//
//		@Override
//		public void onCreateContextMenu(ContextMenu menu, View v,
//				ContextMenuInfo menuInfo) {
//			// menuInfo is null
//			menu.add(Menu.NONE, R.id.item_delete, Menu.NONE, "Delete");
//			menu.add(Menu.NONE, R.id.item_edit, Menu.NONE, "Edit");
//		}
//
//	}
//
//	@Override
//	public void onViewRecycled(ViewHolder holder) {
//		holder.itemView.setOnLongClickListener(null);
//		super.onViewRecycled(holder);
//	}
//
//	// Return the size of your itemsData (invoked by the layout manager)
//	@Override
//	public int getItemCount() {
//		return task_data.size();
//	}
//
//	public TaskModel getItem(int position) {
//		TaskModel task = task_data.get(position);
//		return task;
//	}
//
//	public int getPosition() {
//		return position;
//	}
//
//	public void setPosition(int position) {
//		this.position = position;
//	}
//
//}
