package com.gravity.innovations.tasks.done;

public class UserModel {

	// private variables
	public int _id;
	public String name;
	public String email;
	public String server_id;
	public byte[] image;

	// variables for email functions
	public String displayName;
	// string email
	public String contact_id;

	public UserModel() {
	}

	public UserModel(int id, String _name, String _email, String _server_id,
			byte[] _image) {
		this._id = id;
		this.name = _name;
		this.email = _email;
		this.server_id = _server_id;
		this.image = _image;
	}

	public UserModel(String _name, String _email, String _server_id,
			byte[] _image) {
		this.name = _name;
		this.email = _email;
		this.server_id = _server_id;
		this.image = _image;
	}

	public UserModel(int id, String _name, String _email, String _server_id) {
		this._id = id;
		this.name = _name;
		this.email = _email;
		this.server_id = _server_id;
	}

//	public UserModel(String _name, String _email, String _server_id) {
//		this.name = _name;
//		this.email = _email;
//		this.server_id = _server_id;
//	}

	
	// constructor for email functions
	public UserModel(String _contact_id, String _email, String _displayName) {
		this.contact_id = _contact_id;
		this.email = _email;
		this.displayName = _displayName;
	}
	public UserModel(String _email, String _displayName) {
		this.email = _email;
		this.displayName = _displayName;
	}
}
