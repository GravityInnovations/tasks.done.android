package com.gravity.innovations.tasks.done;

public class UsersListsModel {
	// private variables
	public int user_id;
	public int tasklist_id;
	public String SyncStatus ="unsynced";
	public UsersListsModel() {
	}

	
	public UsersListsModel( int _user_id, int tasklist_id, String SyncStatus) {
		
		this.user_id = _user_id;
		this.tasklist_id = tasklist_id;
		this.SyncStatus = SyncStatus;
	}
}
