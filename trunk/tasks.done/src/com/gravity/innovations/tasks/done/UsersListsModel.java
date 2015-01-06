package com.gravity.innovations.tasks.done;

public class UsersListsModel {
	// private variables
	public int _id;
	public String server_id;
	public String syncStatus;// date time
	public String syncStatusTimeStamp;// date time
	public int user_id;
	public int tasklist_id;

	public UsersListsModel() {
	}

	public UsersListsModel(int id, String _server_id, String _syncStatus,
			String _syncStatusTimeStamp, int _user_id, int tasklist_id) {
		this._id = id;
		this.server_id = _server_id;
		this.syncStatus = _syncStatus;
		this.syncStatusTimeStamp = _syncStatusTimeStamp;
		this.user_id = _user_id;
		this.tasklist_id = tasklist_id;
	}

	public UsersListsModel( String _server_id, String _syncStatus,
			String _syncStatusTimeStamp, int _user_id, int tasklist_id) {
		this.server_id = _server_id;
		this.syncStatus = _syncStatus;
		this.syncStatusTimeStamp = _syncStatusTimeStamp;
		this.user_id = _user_id;
		this.tasklist_id = tasklist_id;
	}
}
