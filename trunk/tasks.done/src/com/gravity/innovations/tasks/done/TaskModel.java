package com.gravity.innovations.tasks.done;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskModel implements Comparable, Serializable {
	
	
	public int _id;//
	public String title;//
	public String details;//
	public String notes;//
	public String DateUpdated;//
	public String DateCreated;//
	public String server_id;//
	public TaskListModel parent;//
	public ArrayList<TaskNotificationsModel> notifications = new ArrayList<TaskNotificationsModel>();
	
	public int completed;// yes(1) or no(0)
	public String startDateTime;//
	public String endDateTime;//
	//public String syncStatus;// date time
	//public String syncStatusTimeStamp;// date time
	//public String links;
	//public String associated_usermodels;
	public int fk_tasklist_id = 1;
	//public String relativeTime;// !db

	public int allDay;// // swtich 0/1
	public int rep_interval;//
	// exact number of days/week/months
	public int rep_intervalType;//// 0-4

	public String rep_intervalExpiration;//
	// forever/date/for fixed number of events
	public String rep_value;//
	

	// ********* Constructors *********//
	public TaskModel() {
		this._id = -1;
		updateTimeNow();
	}

	public TaskModel(String title, String _task_details, String _task_notes,
			int fk) {
		// started editing form this constructor
		this.title = title;
		this.details = _task_details;
		this.notes = _task_notes;
		fk_tasklist_id = fk;
		updateTimeNow();
	}
	
	// ********* Constructors *********//
	public void updateTimeNow() {
		long a = System.currentTimeMillis();
		String currentDateTime = Long.toString(a);
		this.updated = currentDateTime;
		updateRelativeTime(a);
		// return a;
	}

	public void set(TaskModel temp) {
		this.title = temp.title;
		this.details = temp.details;
		this.notes = temp.notes;
		updateTimeNow();
	}

	public TaskModel updateRelativeTime(long currentTimeMills) {
		// long currentTimeMills = System.currentTimeMillis();
		try {
			String s = this.updated.toString();
			long previousTime = Long.valueOf(s).longValue();
			long diff = currentTimeMills - previousTime;
			String relativeTime = null;
			if (diff / 1000 < 1) {
				relativeTime = " just now";
			} else if (diff / 1000 >= 1 && diff / 1000 <= 60) {
				relativeTime = " just now";
			} else if (diff / 60000 >= 1 && diff / 60000 <= 60) {
				if (diff / 60000 == 1) {
					relativeTime = "about " + diff / 60000 + " min ago";
					// for 1 minute earlier exactly
				} else if (diff / 1000 > 1 && diff / 60000 < 60) {
					relativeTime = "about " + diff / 60000 + " mins ago";
					// for minutes greater then 1 minute
				}
			} else if (diff / 3600000 >= 1 && diff / 3600000 <= 24) {
				if (diff / 3600000 == 1) {
					relativeTime = "about " + diff / 3600000 + " hr ago";
					// for number of hours exactly 1 hour
				} else if (diff / 3600000 > 1 && diff / 3600000 <= 6) {
					relativeTime = "about " + diff / 3600000 + " hrs ago";
					// for number of hours less then 6 hours
				} else if (diff / 3600000 > 6 && diff / 3600000 <= 24) {
					// relativeTime = diff / 3600000 + " hrs earlier";
					// for number of hours greater then 6 hours
					Calendar cl = Calendar.getInstance();
					cl.setTimeInMillis(previousTime);
					// here your time in milliseconds
					relativeTime = "" + cl.get(Calendar.HOUR_OF_DAY) + ":"
							+ cl.get(Calendar.MINUTE);// + " " +
					// cl.get(Calendar.AM_PM);
				}
				// now write condition to check for previous days
			} else if (diff / (3600000 * 24) >= 1 && diff / (3600000 * 24) <= 7) {
				Calendar cl = Calendar.getInstance();
				cl.setTimeInMillis(previousTime);
				// here your time in milliseconds
				// relativeTime = "" + cl.get(Calendar.DAY_OF_WEEK);
				if (cl.get(Calendar.DAY_OF_WEEK) == 1) {
					relativeTime = "Sun";
				} else if (cl.get(Calendar.DAY_OF_WEEK) == 2) {
					relativeTime = "Mon";
				} else if (cl.get(Calendar.DAY_OF_WEEK) == 3) {
					relativeTime = "Tues";
				} else if (cl.get(Calendar.DAY_OF_WEEK) == 4) {
					relativeTime = "Wed";
				} else if (cl.get(Calendar.DAY_OF_WEEK) == 5) {
					relativeTime = "Thurs";
				} else if (cl.get(Calendar.DAY_OF_WEEK) == 6) {
					relativeTime = "Fri";
				} else if (cl.get(Calendar.DAY_OF_WEEK) == 7) {
					relativeTime = "Sat";
				}
			} else if (diff / (3600000 * 24) > 7 && diff / (3600000 * 24) <= 30) {
				// relativeTime = " Days ";
				Calendar cl = Calendar.getInstance();
				cl.setTimeInMillis(previousTime);
				// here your time in milliseconds
				// relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + ":" +
				// (cl.get(Calendar.MONTH)+1); //increment because this was
				// giving the wrong month
				if ((cl.get(Calendar.MONTH) + 1) == 1) {
					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Jan";
				} else if ((cl.get(Calendar.MONTH) + 1) == 2) {
					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Feb";
				} else if ((cl.get(Calendar.MONTH) + 1) == 3) {
					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Mar";
				} else if ((cl.get(Calendar.MONTH) + 1) == 4) {
					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Apr";
				} else if ((cl.get(Calendar.MONTH) + 1) == 5) {
					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " May";
				} else if ((cl.get(Calendar.MONTH) + 1) == 6) {
					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Jun";
				} else if ((cl.get(Calendar.MONTH) + 1) == 7) {
					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Jul";
				} else if ((cl.get(Calendar.MONTH) + 1) == 8) {
					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Aug";
				} else if ((cl.get(Calendar.MONTH) + 1) == 9) {
					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Sep";
				} else if ((cl.get(Calendar.MONTH) + 1) == 10) {
					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Oct";
				} else if ((cl.get(Calendar.MONTH) + 1) == 11) {
					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Nov";
				} else if ((cl.get(Calendar.MONTH) + 1) == 12) {
					relativeTime = "" + cl.get(Calendar.DAY_OF_MONTH) + " Dec";
				}
			} else {
				// if (diff / (3600000*24*12) >= 1 && diff / (3600000*24*12)
				// <= 60) {
				// relativeTime = " Years ";
				Calendar cl = Calendar.getInstance();
				cl.setTimeInMillis(previousTime);
				// here your time in milliseconds
				relativeTime = "" + cl.get(Calendar.YEAR)
						+ (cl.get(Calendar.MONTH) + 1)
						+ cl.get(Calendar.DAY_OF_MONTH);
				// increment because this was giving month-1
			}
			this.relativeTime = relativeTime;
			// holder.update.setText(relativeTime);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public int compareTo(Object another) {
		// TODO Auto-generated method stub
		TaskModel temp = (TaskModel) another;
		return this.completed - temp.completed;
	}

}
