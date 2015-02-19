package com.gravity.innovations.tasks.done;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.appcompat.R.color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskAdapter extends RecyclerView.Adapter {// <TaskAdapter.ViewHolder>
														// {

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

	// Create new views (invoked by the layout manager)
	@Override
	public RecyclerView.ViewHolder/* TaskAdapter.ViewHolder */onCreateViewHolder(
			ViewGroup parent, int viewType) {
		// create a new view
		// View itemLayoutView =
		// LayoutInflater.from(parent.getContext()).inflate(
		// R.layout.task_listview_row, null);

		if (viewType == 1) {
			View itemLayoutView = footer;
			// LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_footer,null);
			FooterHolder viewHolder = new FooterHolder(itemLayoutView);
			return viewHolder;
		} else {
			View itemLayoutView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.task_listview_row, null);

			// create ViewHolder

			ViewHolder viewHolder = new ViewHolder(itemLayoutView);
			return viewHolder;

		}

	}
 

	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder(
			android.support.v7.widget.RecyclerView.ViewHolder arg0, final int position) {
		if (getItemViewType(position) == 0) {
			try {				
				ViewHolder viewHolder = (ViewHolder) arg0;
				// - get data from your itemsData at this position
				// - replace the contents of the view with that itemsData
				// viewHolder.txtViewTitle.setText(itemsData[position].getTitle());

				// Assigning task data to the holders start

				TaskModel task = getItem(position);// task_data.get(position);
				// initFooter(viewHolder, position);

				viewHolder.tv_title.setText(task.title);
				viewHolder.tv_details.setText(task.details);
				viewHolder.tv_notes.setText(task.notes);

				if (task.relativeTime == "" || task.relativeTime == null)
					task.updateRelativeTime(currentTimeMills);
				viewHolder.tv_updatedAt.setText(task.relativeTime);
				//viewHolder.iv_doneToggle.setBackgroundColor(Color.parseColor(mTaskListModel.fragmentColor));
				try {
					//viewHolder.iv_doneToggle.setBackgroundColor(Color.parseColor(mTaskListModel.fragmentColor));
					float alpha = 0;
					if (task.completed == 1) {
						alpha = 1.0f;
//						viewHolder.iv_doneToggle
//								.setImageResource(R.drawable.task_row_done_bg);
						//v//iewHolder.iv_doneToggle.setb
					} else if (task.completed == 0) {
//						viewHolder.iv_doneToggle
//								.setImageResource(R.drawable.task_row_bg);
						alpha = 0.10f;
					}
					viewHolder.iv_doneToggle.setBackgroundColor(adjustAlpha(Color.parseColor(mTaskListModel.fragmentColor), alpha));
					
				} catch (Exception e) {
					Log.e("TaskAdapter", "iv_doneToggle");
				}

				try {
					if (task.remind_interval == 0) {
						viewHolder.iv_alarmToggle.setVisibility(View.INVISIBLE);
					} else {
						viewHolder.iv_alarmToggle.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
					Log.e("TaskAdapter", "iv_alarmToggle");
				}
				// Assigning task data to the holder finish

				// mark as dne and undone start

				//final int pos = position;
				viewHolder.iv_doneToggle
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								//int position = pos;
								final int pos_final = calculatePos(position);
						
								TaskModel temp = task_data.get(position);
								if(temp.completed ==0)
								{
									temp.completed = 1;
									temp.updateTimeNow();
									boolean flag = mNavigationDrawerFragment
											.MarkDoneTask(mTaskListModel,
													temp);
									if (!flag){
										Log.e("TaskAdapter ", "NotMarkedAs Done");
									}
								}
								else
								{
									temp.completed = 0;
									temp.updateTimeNow();
									boolean flag = mNavigationDrawerFragment
											.MarkDoneTask(mTaskListModel,
													temp);
									if (!flag){
										Log.e("TaskAdapter ", "NotMarkedAs UnDone");
										}
								}
								
//								if (temp.completed == 0) {
//									if ((Integer) v.getTag() == R.drawable.task_row_bg) {
//
//										temp.updateTimeNow();
//										temp.completed = 1;
//										boolean flag = mNavigationDrawerFragment
//												.MarkDoneTask(mTaskListModel,
//														temp);
//										if (flag) {
//											((ImageView) v)
//													.setImageResource(R.drawable.task_row_done_bg);
//											v.setTag(R.drawable.task_row_done_bg);
//											temp.title = temp.title;
//										}
//									}
//
//								} else { 
//									if (temp.completed == 1) {
//										temp.completed = 0;
//										temp.updateTimeNow();
//										boolean flag = mNavigationDrawerFragment
//												.MarkDoneTask(mTaskListModel,
//														temp);
//										if (flag) {
//											((ImageView) v)
//													.setImageResource(R.drawable.task_row_bg);
//											v.setTag(R.drawable.task_row_bg);
//										}
//									}
//								}

								int pos1 = position;
								int pos2 = pos_final;
								notifyItemMoved(pos1, pos2);
								TaskModel rem = task_data.remove(pos1);
								// notifyItemRemoved(pos1);
								// notifyDataSetChanged();
								task_data.add(pos2, rem);

								
								for(int i=0;i<getItemCount()-1;i++)
								{
									TaskModel M = getItem(i);
									notifyItemChanged(i);
									
								}
								mRecyclerView.scrollToPosition(pos_final);

							}

						});

				// mark as done and undone finish

				// on item Clicks start

				final ViewHolder vH = viewHolder;

				viewHolder.taskRowLayout
						.setOnLongClickListener(new View.OnLongClickListener() {
							@Override
							public boolean onLongClick(View v) {
								setPosition(vH.getPosition());
								return false;
							}
						});

				viewHolder.taskRowLayout
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// open details dialog
								setPosition(vH.getPosition());
								mNavigationDrawerFragment
										.openTaskDetailsDialog(mTaskListModel,
												getItem(vH.getPosition()));
								// swapWithNextItem();
							}
						});
				// on item clicks finish
			} catch (Exception e) {
				Log.e(this.toString() + ": onBinder, TaskAdapter",
						e.getLocalizedMessage());
			}
		}
	}
	public int adjustAlpha(int color, float alpha) {
		//float f = alpha/100;
		int a =Math.round(255 * alpha);//( 255 * (float)());
	    int red = Color.red(color);
	    int green = Color.green(color);
	    int blue = Color.blue(color);
	    return Color.argb(a, red, green, blue);
	}
	private int calculatePos(int pos) {
		int pos_final = pos;
		boolean completedExists = false;
		TaskModel task = getItem(pos);
		if (task.completed == 1) {
			pos_final = 0;
		} else {
			int a = getItemCount()-1;
			for (int i = 0; i < getItemCount()-1; i++) {//-1ed
				TaskModel temp = getItem(i);
				if (temp.completed == 1) {
					completedExists = true;
					pos_final = i - 1;
					break;
				}

			}
//			if (pos == pos_final)
			if(!completedExists)
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

		// LinearLayout footerLayout;
		// View searchLineLayout;
		// TextView updatedTimeLayout;

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

			// footerLayout = (LinearLayout) itemLayoutView
			// .findViewById(R.id.footer);

			// searchLineLayout = (View) itemLayoutView
			// .findViewById(R.id.search_line);
			//
			// updatedTimeLayout = (TextView) itemLayoutView
			// .findViewById(R.id.updated1);

			itemLayoutView.setOnCreateContextMenuListener(this);

		}

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// menuInfo is null
			menu.add(Menu.NONE, R.id.item_delete, Menu.NONE, "Delete");
			menu.add(Menu.NONE, R.id.item_edit, Menu.NONE, "Edit");
			menu.add(Menu.NONE, R.id.item_set_reminder, Menu.NONE, "Set Reminder");
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
		int i = getItemCount();//+1;
		if (position == i - 1) {
			return 1;
		} else
			return 0;
	}

	// Return the size of your itemsData (invoked by the layout manager)
	@Override
	public int getItemCount() {
		return task_data.size()+1;
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

	// // for swapping call swapWithNextITem
	// public void swapItemsQ(int p1, int p2) {
	// int pos1 = 1;
	// int pos2 = 2;
	// notifyItemMoved(pos1, pos2);
	// TaskModel temp = task_data.remove(pos1);
	// // notifyItemRemoved(pos1);
	// // notifyDataSetChanged();
	// task_data.add(pos2, temp);
	//
	// //
	//
	// // TaskModel task = getItem(pos1);
	// // for(int i=0;i<getItemCount();i++)
	// // {
	// // TaskModel temp = getItem(i);
	// // task_data.set(i, task_data.get(i+1));
	// // //notifyItemMoved(i,i+1);
	// // task_data.set(i+1, temp);
	// // notifyItemMoved(i,i+1);
	// // notifyItemChanged(i);
	// // notifyItemChanged(i+1);
	// //
	// // //notifyItemMoved(pos1,pos2);
	// // if(i+1==pos2)
	// // break;
	// // }
	// // if(pos1>pos2)
	// // notifyItemMoved(pos1,pos2);
	// // else
	// // notifyItemMoved(pos2,pos1);
	// // String temp = task_data.get(pos2);
	// // task_data.set(pos2, task_data.get(pos1));
	// //
	// // task_data.set(pos1, temp);
	// // notifyItemChanged(pos1);
	// // notifyItemChanged(pos2);
	// // notifyDataSetChanged();
	// //
	// //
	//
	// // int pos1 = 0, pos2 = 1;
	// // notifyItemMoved(pos2, pos1);
	// // notifyItemChanged(pos2);
	// // boolean flag = false;
	// // try {
	// //
	// // TaskModel temp = getItem(pos2);
	// // // String temp = task_data.get(pos2);
	// // task_data.set(pos2, task_data.get(pos1));
	// //
	// // task_data.set(pos1, temp);
	// //
	// // flag= true;
	// // // new Runnable(new ).run();
	// //
	// // } catch (Exception e) {
	// // flag= false;
	// // }
	// // if(flag)
	// // for (int i = 0; i < getItemCount(); i++)
	// // notifyItemChanged(i);
	//
	// }

}
