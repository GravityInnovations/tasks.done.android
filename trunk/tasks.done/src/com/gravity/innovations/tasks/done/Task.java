package com.gravity.innovations.tasks.done;

public class Task {
	//handles the task entries 
    // private variables
    public int _id;
    public String _task_title;
    public String _task_details;
    public String _task_notes;

    public Task() {
    	this._id = -1;
    }

    // constructor
    public Task(int id, String title, String _task_details, String _task_notes) {
	this._id = id;
	this._task_title = title;
	this._task_details = _task_details;
	this._task_notes = _task_notes;

    }

    // constructor
    public Task(String title, String _task_details, String _task_notes) {
	this._task_title = title;
	this._task_details = _task_details;
	this._task_notes = _task_notes;
    }

    // getting ID
    public int getID() {
	return this._id;
    }

    // setting id
    public void setID(int id) {
	this._id = id;
    }

    // getting title
    public String getTitle() {
	return this._task_title;
    }

    // setting title
    public void setTitle(String title) {
	this._task_title = title;
    }

    // getting details
    public String getDetails() {
	return this._task_details;
    }

    // setting details
    public void setDetails(String details) {
	this._task_details = details;
    }

    // getting notes
    public String getNotes() {
	return this._task_notes;
    }

    // setting notes
    public void setNotes(String notes) {
	this._task_notes = notes;
    }


}
