package com.gravity.innovations.tasks.done;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskAdapter extends ArrayAdapter<TaskModel> {
	Activity activity;
	int layoutResourceId;
	TaskModel task;
	int MAX_CHARS = 10;
	ArrayList<TaskModel> task_data = new ArrayList<TaskModel>();
	final long currentTimeMills = System.currentTimeMillis();
	private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

	public TaskAdapter(Activity act, int layoutResourceId,
			ArrayList<TaskModel> data) {
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

	public void sellectThisThing(int position){
		mSelection.put(position, null);
	}
	
	public void setNewSelection(int position, boolean value) {
		mSelection.put(position, value); //just made it up
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

	public void removeAllSelection() {
		 mSelection.clear();// .remove();
		notifyDataSetChanged();
	}
	
	public void clearSelection() {
		mSelection = new HashMap<Integer, Boolean>();
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		//try{
		TaskModelHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(activity);
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new TaskModelHolder();
			holder.title = (TextView) row.findViewById(R.id.user_task_title);
			holder.details = (TextView) row
					.findViewById(R.id.user_task_details);
			holder.notes = (TextView) row.findViewById(R.id.user_task_notes);
			holder.update = (TextView) row.findViewById(R.id.updated);

			row.setTag(holder);
		} else {
			holder = (TaskModelHolder) row.getTag();
		}

		row.setBackgroundColor(activity.getResources().getColor(
				android.R.color.background_light)); // default color

		if (mSelection.get(position) != null) {
			row.setBackgroundColor(activity.getResources().getColor(R.color.selection_blue));
					//getResources().getColor(android.R.color.holo_blue_light));
		}
		task = task_data.get(position);
		
//		try {
//
//			String s = task.updated.toString();
//			long previousTime = Long.valueOf(s).longValue();
//			long diff = currentTimeMills - previousTime;
//
//			String relativeTime = null;
//
//			if (diff /1000 < 1){
//				relativeTime = " Just Now";
//			}else if (diff / 1000 >= 1 && diff / 1000 <= 60) {
//				relativeTime = " Less than a minute "; // Just Now
//			} else if (diff / 60000 >= 1 && diff / 60000 <= 60) {
//				if (diff / 60000 == 1) {
//					relativeTime = diff / 60000 + " min earlier "; // for 1
//																	// minute
//																	// earlier
//																	// exactly
//				} else if (diff / 1000 > 1 && diff / 60000 <= 60) {
//					relativeTime = diff / 60000 + " mins earlier "; // for
//																	// minutes
//																	// greater
//																	// then 1
//																	// minute
//				}
//			} else if (diff / 3600000 >= 1 && diff / 3600000 <= 24) {
//				if (diff / 3600000 == 1) {
//					relativeTime = diff / 3600000 + " hr earlier"; // for number
//																	// of hours
//																	// exactly 1
//																	// hour
//				} else if (diff / 3600000 > 1 && diff / 3600000 <= 6) {
//					relativeTime = diff / 3600000 + " hrs earlier"; // for
//																	// number of
//																	// hours
//																	// less then
//																	// 6 hours
//				} else if (diff / 3600000 > 6 && diff / 3600000 <= 24) {
//					// relativeTime = diff / 3600000 + " hrs earlier"; //for
//					// number of hours greater then 6 hours
//					Calendar cl = Calendar.getInstance();
//					cl.setTimeInMillis(previousTime); // here your time in
//														// milliseconds
//					relativeTime = "" + cl.get(Calendar.HOUR_OF_DAY) + ":" 
//							+ cl.get(Calendar.MINUTE);// + " " +
//														// cl.get(Calendar.AM_PM);
//				}
//				// now write condition to check for previous days
//			} else if (diff / (3600000 * 24) >= 1 && diff / (3600000 * 24) <= 7) {
//
//				Calendar cl = Calendar.getInstance();
//				cl.setTimeInMillis(previousTime); // here your time in
//													// milliseconds
//				// relativeTime = "" + cl.get(Calendar.DAY_OF_WEEK);
//				if (cl.get(Calendar.DAY_OF_WEEK) == 1) {
//					relativeTime = "Sun";
//				} else if (cl.get(Calendar.DAY_OF_WEEK) == 2) {
//					relativeTime = "Mon";
//				} else if (cl.get(Calendar.DAY_OF_WEEK) == 3) {
//					relativeTime = "Tues";
//				} else if (cl.get(Calendar.DAY_OF_WEEK) == 4) {
//					relativeTime = "Wed";
//				} else if (cl.get(Calendar.DAY_OF_WEEK) == 5) {
//					relativeTime = "Thurs";
//				} else if (cl.get(Calendar.DAY_OF_WEEK) == 6) {
//					relativeTime = "Fri";
//				} else if (cl.get(Calendar.DAY_OF_WEEK) == 7) {
//					relativeTime = "Sat";
//				}
//			} else if (diff / (3600000 * 24) > 7 && diff / (3600000 * 24) <= 30) {
//				// relativeTime = " Days ";
//				Calendar cl = Calendar.getInstance();
//				cl.setTimeInMillis(previousTime); // here your time in
//													// milliseconds
//				// relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + ":" +
//				// (cl.get(Calendar.MONTH)+1); //increment because this was
//				// giving the wrong month
//				if ((cl.get(Calendar.MONTH) + 1) == 1) {
//					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Jan";
//				} else if ((cl.get(Calendar.MONTH) + 1) == 2) {
//					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Feb";
//				} else if ((cl.get(Calendar.MONTH) + 1) == 3) {
//					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Mar";
//				} else if ((cl.get(Calendar.MONTH) + 1) == 4) {
//					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Apr";
//				} else if ((cl.get(Calendar.MONTH) + 1) == 5) {
//					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " May";
//				} else if ((cl.get(Calendar.MONTH) + 1) == 6) {
//					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Jun";
//				} else if ((cl.get(Calendar.MONTH) + 1) == 7) {
//					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Jul";
//				} else if ((cl.get(Calendar.MONTH) + 1) == 8) {
//					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Aug";
//				} else if ((cl.get(Calendar.MONTH) + 1) == 9) {
//					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Sep";
//				} else if ((cl.get(Calendar.MONTH) + 1) == 10) {
//					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Oct";
//				} else if ((cl.get(Calendar.MONTH) + 1) == 11) {
//					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Nov";
//				} else if ((cl.get(Calendar.MONTH) + 1) == 12) {
//					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Dec";
//				}
//			} else // if (diff / (3600000*24*12) >= 1 && diff / (3600000*24*12)
//					// <= 60) {
//			{ // relativeTime = " Years ";
//				Calendar cl = Calendar.getInstance();
//				cl.setTimeInMillis(previousTime); // here your time in
//													// milliseconds
//				relativeTime = "" + cl.get(Calendar.YEAR)
//						+ (cl.get(Calendar.MONTH) + 1)
//						+ cl.get(Calendar.DAY_OF_MONTH); // increment because
//															// this was giving
//															// the wrong month
//			}
//			holder.update.setText(relativeTime);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		// try {
		// if (holder.title.getText().toString().length() >= MAX_CHARS) {
		// // String temp= task.title.toString();
		// holder.title.setText(task.title.toString().substring(0,
		// MAX_CHARS)
		// + "...");
		// ; }
		//
		// else if (holder.title.getText().toString().length() <= MAX_CHARS) {
		// holder.title.setText(task.title);
		// }
		// } catch (Exception e) {
		// String tag = "TaskAdapter";
		// String msg = "truncation Error";
		// Log.e(tag, msg);
		// }
		//
		if(task.relativeTime== "" || task.relativeTime == null)
			task.updateRelativeTime(currentTimeMills);
		holder.update.setText(task.relativeTime);
		
		holder.title.setText(task.title);
		holder.details.setText(task.details);
		holder.notes.setText(task.notes);
		// holder.update.setText(task.updated);
		
//		}catch(Exception e){
//			Log.e("AdapterTask", "Error");
//		}
		return row;
	}

	class TaskModelHolder {
		TextView title, details, notes, update;
	}

}