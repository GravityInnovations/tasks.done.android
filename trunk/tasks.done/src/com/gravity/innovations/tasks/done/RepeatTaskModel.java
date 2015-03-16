package com.gravity.innovations.tasks.done;

public class RepeatTaskModel {
	public int task_id;
	public int allDay; // swtich 0/1
	public int interval;
	public int interval_type;
	public String interval_expiration;
	public String interval_week;
	public int week_of_month;
	public int day_of_month;
	// if switch is OFF
	public String startDateTime;
	public String endDateTime;
	// if switch is ON
	public String startDate;
	public String endDate;

	public String startTime;
	public String endTime;

	RepeatTaskModel() {
	}
}
