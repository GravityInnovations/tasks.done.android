package com.gravity.innovations.tasks.done;

import org.json.JSONException;
import org.json.JSONObject;

public class UserModel {

	// private variables
	public int _id = -1;
	public String name;
	public String email;
	public String server_id;
	public byte[] image;
	public double image_alpha = 1.0;
	public String phone;

	public String displayName;
	// string email
	public String contact_id;

	public UserModel() {
	}
	public JSONObject toJsonObj()
	{
		 JSONObject obj = new JSONObject();
	        try {
	            obj.put("Email", email);
	        } catch (JSONException e) {
	           // trace("DefaultListItem.toString JSONException: "+e.getMessage());
	        }
	        return obj;
	}
	
//	public UserModel(int id, String _name, String _email, String _server_id,
//			byte[] _image) {
//		this._id = id;
//		this.name = _name;
//		this.email = _email;
//		this.server_id = _server_id;
//		this.image = _image;
//	}
//
//	public UserModel(String _name, String _email, String _server_id,
//			byte[] _image) {
//		this.name = _name;
//		this.email = _email;
//		this.server_id = _server_id;
//		this.image = _image;
//	}
//
//	public UserModel(int id, String _name, String _email, String _server_id) {
//		this._id = id;
//		this.name = _name;
//		this.email = _email;
//		this.server_id = _server_id;
//	}
//
////	public UserModel(String _name, String _email, String _server_id) {
////		this.name = _name;
////		this.email = _email;
////		this.server_id = _server_id;
////	}
//
//	
//	// constructor for email functions
//	public UserModel(String _contact_id, String _email, String _displayName) {
//		this.contact_id = _contact_id;
//		this.email = _email;
//		this.displayName = _displayName;
//	}
	
	public UserModel(String _displayName, String _email, String _phone, byte[] _image) {
		this.displayName = _displayName;
		this.email = _email;
		this.phone = _phone;
		this.image = _image;
	}
//	public UserModel(String _displayName, String _email, String _phone) {
//		this.displayName = _displayName;
//		this.email = _email;
//		this.phone = _phone;
//	}//for else case
	
	public UserModel(String _displayName, String _email, byte[] _image) {
		this.displayName = _displayName;
		this.email = _email;
		this.image = _image;
	}//for else case
	
	
	public UserModel(String _displayName, String _email) {
		this.displayName = _displayName;
		this.email = _email;
	}//for else case
}
