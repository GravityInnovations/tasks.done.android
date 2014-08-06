package com.gravity.innovations.tasks.done;

public class TaskModel {
	// handles the task entries
	// private variables
	public int _id;
	public String  title;
	public String  details;
	public String  notes;
	public int fk_tasklist_id=1;

	public TaskModel() {
		this._id = -1;
	}

	// constructor
	public TaskModel(int id, String title, String _task_details, String _task_notes, int fk) {
		this._id = id;
		this.title = title;
		this.details = _task_details;
		this.notes = _task_notes;
		fk_tasklist_id=fk;

	}
	public TaskModel(  String title, String _task_details, String _task_notes, int fk) {
		 
		this.title = title;
		this.details = _task_details;
		this.notes = _task_notes;
		fk_tasklist_id=fk;

	}
	// constructor
	public TaskModel(String title, String _task_details, String _task_notes) {
		this.title = title;
		this.details = _task_details;
		this.notes = _task_notes;
	}

	public void set(TaskModel temp)
	{
		this.title = temp.title;
		this.details = temp.details;
		this.notes = temp.notes;
	}
}
