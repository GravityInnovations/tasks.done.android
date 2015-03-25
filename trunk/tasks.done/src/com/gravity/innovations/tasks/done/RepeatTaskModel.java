package com.gravity.innovations.tasks.done;

public class RepeatTaskModel {
	public int task_id;
	public int allDay; // swtich 0/1
	public int interval;// exact number of days/week/months from custom dialog
	public int interval_type;// 0-4

	public String interval_expiration;// from dialog forever/date/for fixed
										// number of events
	public String interval_week;
	public int week_of_month;
	public int day_of_month;
	// if switch is OFF Hours and Minuts == 00

	public String startDateTime;
	public String endDateTime;

	RepeatTaskModel() {
	}
}
