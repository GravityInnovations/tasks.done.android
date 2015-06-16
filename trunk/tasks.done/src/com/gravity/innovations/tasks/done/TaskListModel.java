package com.gravity.innovations.tasks.done;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskListModel implements Serializable {
	
	
	
	public UserModel owner;
	public int _id = -1;
	public String title;
	// public String gravity_id = "";
	public String server_id = "";
	public String DateUpdated;
	public String DateCreated;
	public int owner_id;
	public String syncStatus;// date time
	public String syncStatusTimeStamp;// date time
	public int icon_identifier;

	public ArrayList<TaskModel> tasks = new ArrayList<TaskModel>(); // task_data;
	public ArrayList<UserModel> users = new ArrayList<UserModel>(); // user_data;

	public String fragmentColor;

	// ********* Constructors *********//

	public TaskListModel() {
		// _id = -1;
		// this.tasks = new ArrayList<TaskModel>();
	}

	public ArrayList<TaskModel> getPendingTasks() {
		ArrayList<TaskModel> temp = new ArrayList<TaskModel>();

		for (TaskModel task : this.tasks) {

			if (task.completed == 0) {
				task.parent = this;
				temp.add(task);
				// temp.parent
			}
		}
		return temp;

	}

	public TaskListModel(String title) {
		this.title = title;
		this.tasks = new ArrayList<TaskModel>();
	}

		// ********* Constructors *********//

	public TaskModel GetTask(int id) {
		for (TaskModel item : this.tasks) {
			if (item._id == id) {
				return item;
			}
		}
		return null;
	}

	public Boolean RemoveTask(int id) {
		for (TaskModel item : this.tasks) {
			if (item._id == id) {
				this.tasks.remove(item);
				return true;
			}
		}
		return false;
	}

	public Boolean SearchFilter(CharSequence temp) {
		if (this.title.toLowerCase().contains(temp.toString().toLowerCase()))
			return true;
		else
			return false;
	}
}
