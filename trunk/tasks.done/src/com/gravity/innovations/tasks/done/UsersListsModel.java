package com.gravity.innovations.tasks.done;

public class UsersListsModel {
	// private variables
	public int _id;
	public String server_id;
	public int is_synced_sent;
	public int is_synced;
	public int user_id;
	public int tasklist_id;

	public UsersListsModel() {
	}

	public UsersListsModel(int id, String _server_id, int _is_synced_sent,
			int _is_synced, int _user_id, int tasklist_id) {
		this._id = id;
		this.server_id = _server_id;
		this.is_synced_sent = _is_synced_sent;
		this.is_synced = _is_synced;
		this.user_id = _user_id;
		this.tasklist_id = tasklist_id;
	}

	public UsersListsModel( String _server_id, int _is_synced_sent,
			int _is_synced, int _user_id, int tasklist_id) {
		this.server_id = _server_id;
		this.is_synced_sent = _is_synced_sent;
		this.is_synced = _is_synced;
		this.user_id = _user_id;
		this.tasklist_id = tasklist_id;
	}
}
