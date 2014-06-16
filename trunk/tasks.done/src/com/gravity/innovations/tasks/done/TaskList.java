package com.gravity.innovations.tasks.done;

public class TaskList {
	//handles the task list entries at DrawerFragment 
    // private variables
    public int task_list_id;
    public String task_list_title;

    public TaskList() {
    }

    // constructor
    public TaskList(int id, String title) {
	this.task_list_id = id;
	this.task_list_title = title;
    }

    // constructor
    public TaskList(String title) {
	this.task_list_title = title;
	}

    // getting ID
    public int getID() {
	return this.task_list_id;
    }

    // setting id
    public void setID(int id) {
	this.task_list_id = id;
    }

    // getting title
    public String getTitle() {
	return this.task_list_title;
    }

    // setting title
    public void setTitle(String title) {
	this.task_list_title = title;
    }
    
}
