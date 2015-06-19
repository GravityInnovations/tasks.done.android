package com.gravity.innovations.tasks.done;

import java.io.Serializable;

import org.json.JSONObject;

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
	
	public TaskNotificationsModel(JSONObject notifObj, int task_id)
	{
		this.fk_task_id = task_id;
		this.interval = notifObj.optInt("Interval");
		this.interval_type = notifObj.optInt("Type");
		this.interval_expiration = notifObj.optString("Expiration");//>>>>>>>>>>>>>>>>>>>>cross check for date or null
		this.send_as = notifObj.optInt("SendAs");
		this.server_id = notifObj.optString("Id");
	}
}
