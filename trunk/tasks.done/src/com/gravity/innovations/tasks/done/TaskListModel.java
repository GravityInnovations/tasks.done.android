package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

public class TaskListModel {
	public int _id;
	public String title;
	public String gravity_id = "";

	public String server_id="";
	public String etag;
	public String updated;
	public String self_link;
	public String kind;
	public int user_id;
	public String syncStatus;// date time
	public String syncStatusTimeStamp;// date time
	public int icon_identifier;

	public ArrayList<TaskModel> tasks = new ArrayList<TaskModel>(); // task_data;
	public ArrayList<UserModel> users = new ArrayList<UserModel>(); // task_data;
	// ********* Constructors *********//
	public TaskListModel() {
		_id = -1;
		this.tasks = new ArrayList<TaskModel>();
	}

	public TaskListModel(String title) {
		this.title = title;
		this.tasks = new ArrayList<TaskModel>();
	}

	public TaskListModel(int id, String title, int _icon_identifier) {
		this._id = id;
		this.title = title;
		this.icon_identifier = _icon_identifier;
		this.tasks = new ArrayList<TaskModel>();
	}

	public TaskListModel(String title, int _icon_identifier) {
		this.title = title;
		this.icon_identifier = _icon_identifier;
		this.tasks = new ArrayList<TaskModel>();
	}

	public TaskListModel(int id, String _title, String _server_id,
			String _etag, String _updated, String _self_link, String _kind,
			int _user_id, String _syncStatus, String _syncStatusTimeStamp, int _list_type) {
		this._id = id;
		this.title = _title;
		this.server_id = _server_id;
		this.etag = _etag;
		this.updated = _updated;
		this.self_link = _self_link;
		this.kind = _kind;
		this.user_id = _user_id;
		this.syncStatus = _syncStatus;
		this.syncStatusTimeStamp = _syncStatusTimeStamp;
		this.icon_identifier = _list_type;
		this.tasks = new ArrayList<TaskModel>();
	}// for onClickNotifiacation
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

	/*
	 * Extra code i dont know what it is doing here
	 */
	// public TaskListModel(int id, String title, ArrayList<TaskModel> tasks) {
	// this._id = id;
	// this.title = title;
	// this.tasks = tasks;
	// }
	// public TaskListModel(int id, String title, String _server_id, String
	// _etag,
	// String _updated, String _self_link, String _kind, int _user_id, int
	// _isSyncSent,
	// int _synced) {
	// this._id = id;
	// this.title = title;
	// this.server_id = _server_id;
	// this.etag = _etag;
	// this.updated = _updated;
	// this.self_link = _self_link;
	// this.kind = _kind;
	// this.user_id =_user_id;
	// this.isSyncSent = _isSyncSent;
	// this.synced = _synced;
	// this.tasks = new ArrayList<TaskModel>();
	// }
	// new database changes
	// constructor

	// public TaskListModel( String title, String _server_id, String _etag,
	// String _updated, String _self_link, String _kind, int _user_id, int
	// _isSyncSent,
	// int _synced) {
	// this.title = title;
	// this.server_id = _server_id;
	// this.etag = _etag;
	// this.updated = _updated;
	// this.self_link = _self_link;
	// this.kind = _kind;
	// this.user_id =_user_id;
	// this.isSyncSent = _isSyncSent;
	// this.synced = _synced;
	// this.tasks = new ArrayList<TaskModel>();
	// }
	/*
	 * Extra code i dont know what it is doing here
	 */
}
