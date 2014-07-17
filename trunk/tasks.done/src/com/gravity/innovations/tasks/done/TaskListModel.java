package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

public class TaskListModel {
	public int _id;
	public String title;
	public ArrayList<TaskModel> tasks = new ArrayList<TaskModel>(); //task_data;
	public TaskListModel() {
		_id = -1;
	}
	// constructor
	public TaskListModel(int id, String title) {
		this._id = id;
		this.title = title;
	}
	// constructor
	public TaskListModel(String title) {
		this.title = title;
	}

}
