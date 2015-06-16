package com.gravity.innovations.tasks.done;

import java.io.Serializable;

public class TaskNotificationsModel implements Serializable {
	public int _id = -1;
	public int interval;
	// values 0-3 minutes/hours/days/weeks
	public int interval_type = 0;
	// values 0-1 as Notificaiton/Email if not specifies setDefault as 0
	public int send_as;
	public String interval_expiration;
	public String server_id;
	public int fk_task_id = 1;

	TaskNotificationsModel() {
	}
}