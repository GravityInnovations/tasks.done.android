package com.gravity.innovations.tasks.done;

public class UserModel {
	
	// private variables
	public int _id;
	public String name;
	public String email;

	public UserModel() {
	}

	public UserModel(int id, String _name, String _email) {
		this._id = id;
		this.name = _name;
		this.email = _email;
	}

	public UserModel(String _name, String _email) {
		this.name = _name;
		this.email = _email;
	}

}
