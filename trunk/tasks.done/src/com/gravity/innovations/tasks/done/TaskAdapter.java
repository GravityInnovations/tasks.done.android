package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskAdapter extends RecyclerView.Adapter {
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
	private static final int TYPE_FOOTER = Integer.MIN_VALUE + 1;

	public TaskAdapter(Activity act, int layoutResourceId,
			TaskListModel parentTaskList, NavigationDrawerFragment NavDrawer,
			int _selectedTaskId, View footer, RecyclerView mRecyclerView) {
		// super(act, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.mActivity = act;
		this.task_data = parentTaskList.tasks;
		this.HighlightTask = _selectedTaskId;
		mNavigationDrawerFragment = NavDrawer;
		mTaskListModel = parentTaskList;
		notifyDataSetChanged();
		mContext = act.getApplication().getApplicationContext();
		this.footer = footer;
		anim = new DefaultItemAnimator();
		this.mRecyclerView = mRecyclerView;
		anim.setSupportsChangeAnimations(true);

	}

	@Override
	public RecyclerView.ViewHolder/* TaskAdapter.ViewHolder */onCreateViewHolder(
			ViewGroup parent, int viewType) {
		if (viewType == 1) {
			View itemLayoutView = footer;
			FooterHolder viewHolder = new FooterHolder(itemLayoutView);
			return viewHolder;
		} else {
			View itemLayoutView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.row_task_listview, null);
			ViewHolder viewHolder = new ViewHolder(itemLayoutView);
			return viewHolder;
		}
	}

	@Override
	public void onBindViewHolder(
			android.support.v7.widget.RecyclerView.ViewHolder arg0,
			final int position) {
		if (getItemViewType(position) == 0) {
			try {
				ViewHolder viewHolder = (ViewHolder) arg0;
				TaskModel task = getItem(position);// task_data.get(position);
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

				viewHolder.tv_details.setText(task.details);
				viewHolder.tv_notes.setText(task.notes);

				if (task.relativeTime == "" || task.relativeTime == null)
					task.updateRelativeTime(currentTimeMills);
				viewHolder.tv_updatedAt.setText(task.relativeTime);
				try {
					// viewHolder.iv_doneToggle.setBackgroundColor(Color.parseColor(mTaskListModel.fragmentColor));
					float alpha = 0;
					if (task.completed == 1) {
						alpha = 1.0f;
					} else if (task.completed == 0) {
						alpha = 0.10f;
					}
					viewHolder.iv_doneToggle.setBackgroundColor(Common.ShapesAndGraphics.adjustAlpha(
							Color.parseColor(mTaskListModel.fragmentColor),
							alpha));
				} catch (Exception e) {
					Log.e("TaskAdapter", "iv_doneToggle");
				}

				try {
					if (task.rep_interval == 0) {
						viewHolder.iv_alarmToggle.setVisibility(View.INVISIBLE);
					} else {
						viewHolder.iv_alarmToggle.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
					Log.e("TaskAdapter", "iv_alarmToggle");
				}
				int highlightPostition = position;
				if (HighlightTask != -1 && task._id == HighlightTask) {
					highlightPostition++;
					Animation animationFadeIn = AnimationUtils.loadAnimation(
							this.mContext, android.R.anim.fade_in);
					viewHolder.taskRowLayout.startAnimation(animationFadeIn);
					mRecyclerView.scrollToPosition(highlightPostition);
				}
				viewHolder.iv_doneToggle
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								final int pos_final = calculatePos(position);
								TaskModel temp = task_data.get(position);
								if (temp.completed == 0) {
									temp.completed = 1;
									temp.updateTimeNow();
									boolean flag = mNavigationDrawerFragment
											.MarkDoneTask(mTaskListModel, temp);
									if (!flag) {
										Log.e("TaskAdapter ",
												"NotMarkedAs Done");
									}
								} else {
									temp.completed = 0;
									temp.updateTimeNow();
									boolean flag = mNavigationDrawerFragment
											.MarkDoneTask(mTaskListModel, temp);
									if (!flag) {
										Log.e("TaskAdapter ",
												"NotMarkedAs UnDone");
									}
								}

								int pos1 = position;
								int pos2 = pos_final;
								notifyItemMoved(pos1, pos2);
								TaskModel rem = task_data.remove(pos1);
								task_data.add(pos2, rem);

								for (int i = 0; i < getItemCount() - 1; i++) {
									TaskModel M = getItem(i);
									notifyItemChanged(i);

								}
								mRecyclerView.scrollToPosition(pos_final);

							}

						});

				final ViewHolder vH = viewHolder;

				viewHolder.taskRowLayout
						.setOnLongClickListener(new View.OnLongClickListener() {
							@Override
							public boolean onLongClick(View v) {
								int pos = vH.getPosition();
								setPosition(vH.getPosition());
								return false;
							}
						});

				viewHolder.taskRowLayout
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								// open details dialog
								int pos = vH.getPosition();
								setPosition(vH.getPosition());
								mNavigationDrawerFragment
										.openTaskDetailsDialog(mTaskListModel,
												getItem(vH.getPosition()));
							}
						});
			} catch (Exception e) {
				Log.e(this.toString() + ": onBinder, TaskAdapter",
						e.getLocalizedMessage());
			}
		}
	}

	private int calculatePos(int pos) {
		int pos_final = pos;
		boolean completedExists = false;
		TaskModel task = getItem(pos);
		if (task.completed == 1) {
			pos_final = 0;
		} else {
			int a = getItemCount() - 1;
			for (int i = 0; i < getItemCount() - 1; i++) {// -1ed
				TaskModel temp = getItem(i);
				if (temp.completed == 1) {
					completedExists = true;
					pos_final = i - 1;
					break;
				}

			}
			// if (pos == pos_final)
			if (!completedExists)
				pos_final = getItemCount() - 2;
		}
		return pos_final;
		// int check=task.completed;
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
		TextView tv_title, tv_details, tv_notes, tv_updatedAt;
		ImageView iv_doneToggle, iv_alarmToggle;
		RelativeLayout taskRowLayout;

		public ViewHolder(View itemLayoutView) {
			super(itemLayoutView);
			tv_title = (TextView) itemLayoutView
					.findViewById(R.id.user_task_title);
			tv_details = (TextView) itemLayoutView
					.findViewById(R.id.user_task_details);
			tv_notes = (TextView) itemLayoutView
					.findViewById(R.id.user_task_notes);
			tv_updatedAt = (TextView) itemLayoutView
					.findViewById(R.id.updated1);
			iv_doneToggle = (ImageView) itemLayoutView
					.findViewById(R.id.done_toggle);
			iv_doneToggle.setTag(R.drawable.task_row_bg);

			iv_alarmToggle = (ImageView) itemLayoutView
					.findViewById(R.id.alarm_toggle);

			taskRowLayout = (RelativeLayout) itemLayoutView
					.findViewById(R.id.row_layout);
			itemLayoutView.setOnCreateContextMenuListener(this);

		}

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			menu.add(Menu.NONE, R.id.item_delete, Menu.NONE, "Delete");
			menu.add(Menu.NONE, R.id.item_edit, Menu.NONE, "Edit");
		}
	}

	@Override
	public void onViewRecycled(
			android.support.v7.widget.RecyclerView.ViewHolder holder) {
		holder.itemView.setOnLongClickListener(null);
		super.onViewRecycled(holder);
	}

	@Override
	public int getItemViewType(int position) {
		int i = getItemCount();// +1;
		if (position == i - 1) {
			return 1;
		} else
			return 0;
	}

	// Return the size of your itemsData (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return task_data.size() + 1;
	}

	public TaskModel getItem(int position) {
		TaskModel task = task_data.get(position);
		return task;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
