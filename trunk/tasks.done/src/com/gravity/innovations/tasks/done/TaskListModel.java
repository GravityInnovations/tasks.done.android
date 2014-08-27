package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

public class TaskListModel {
	public int _id;
	public String title;
	public String gravity_id = "";
	public ArrayList<TaskModel> tasks = new ArrayList<TaskModel>(); // task_data;

	public TaskListModel() {
		_id = -1;
		this.tasks = new ArrayList<TaskModel>();
	}

	// constructor
	public TaskListModel(int id, String title) {
		this._id = id;
		this.title = title;
		this.tasks = new ArrayList<TaskModel>();
	}

	// constructor
	public TaskListModel(String title) {
		this.title = title;
		this.tasks = new ArrayList<TaskModel>();
	}

	public TaskListModel(int id, String title, ArrayList<TaskModel> tasks) {
		this._id = id;
		this.title = title;
		this.tasks = tasks;
	}

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
