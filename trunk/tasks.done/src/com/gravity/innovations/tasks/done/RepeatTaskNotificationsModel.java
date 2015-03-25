package com.gravity.innovations.tasks.done;

public class RepeatTaskNotificationsModel {
	public int task_id;
	//Number of !^
	public int interval;
	// values 0-3 minutes/hours/days/weeks
	public int interval_type;
	//values 0-1 as Notificaiton/Email if not specifies setDefault as 0
	public int send_notificaion_as_email;
	
	RepeatTaskNotificationsModel() {
	}
	
}
