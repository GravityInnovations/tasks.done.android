package com.gravity.innovations.tasks.done;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gravity.innovations.tasks.done.Common.userData;

public class TaskListModel implements Serializable {
	
	
	
	public UserModel owner;
	public int _id = -1;
	public String title;
	// public String gravity_id = "";
	public String server_id = "";
	public String DateUpdated;
	public String DateCreated;
	public int owner_id;
	public String syncStatus;// date time
	public String syncStatusTimeStamp;// date time
	public int icon_identifier;

	public ArrayList<TaskModel> tasks = new ArrayList<TaskModel>(); // task_data;
	public ArrayList<UserModel> users = new ArrayList<UserModel>(); // user_data;

	public String fragmentColor;

	// ********* Constructors *********//

	public TaskListModel() {
		// _id = -1;
		// this.tasks = new ArrayList<TaskModel>();
	}
	
	public TaskListModel(JSONObject temp, userData user_data,DatabaseHelper db)
	{
//		TaskListModel model = new TaskListModel(
//				temp.getString("Title"));
//		
		try{
			this.title = temp.getString("Title");
			this.syncStatus = "Synced";
			this.server_id = temp.optString("TaskListId");
			this.DateUpdated = DateHelper.UtcZTimeToDeviceDefaultTime(temp.optString("DateUpdated"));//Common.toDeviceTime(temp.optString("DateUpdated"));//temp.optString("DateUpdated");
			this.DateCreated = DateHelper.UtcZTimeToDeviceDefaultTime(temp.optString("DateCreated"));
			this.fragmentColor = temp.optString("Color");
			this.icon_identifier = temp.optInt("Icon");
			//this.title = temp.optString("Title");
			//model.owner_id =  temp.optString("Title");
			JSONArray arr_users = temp.optJSONArray("Users");
			String owner_id = temp.optString("OwnerId");
			if(owner_id != null &&
					user_data.gravity_user_id.equals(owner_id))
			{
				this.owner_id = user_data._id;
			}
			for(int j = 0; j< arr_users.length(); j++)
			{
				JSONObject arr_user = arr_users.getJSONObject(j);
				UserModel m = db.users.Get(arr_user.optString("UserId"));
				
				if(m==null){
					m = new UserModel(arr_user);
					m._id = db.users.Add(m);
				}
				if(arr_user.optString("UserId").equals(owner_id) && m._id !=-1)
				{
					this.owner_id = m._id;
				}
				//add user to task list
				
				
			}
		}
		catch(JSONException e)
		{
		}
	}
	public ArrayList<TaskModel> getPendingTasks() {
		ArrayList<TaskModel> temp = new ArrayList<TaskModel>();

		for (TaskModel task : this.tasks) {

			if (task.completed == 0) {
				task.parent = this;
				temp.add(task);
				// temp.parent
			}
		}
		return temp;

	}

	public TaskListModel(String title) {
		this.title = title;
		this.tasks = new ArrayList<TaskModel>();
	}

		// ********* Constructors *********//
	public String toServerJsonString()
	{
		JSONObject temp = new JSONObject();
		try {
			//"Color":"#343437",
//			"Icon":1,
//			"Title":"m title",
//			"DateUpdated": "2015-06-16T12:59:19.897",
//			"DateCreated": "2015-06-16T12:59:19.897"
			temp.put("Color", this.fragmentColor);
			temp.put("Icon", this.icon_identifier);
			temp.put("Title", this.title);
			temp.put("DateUpdated", DateHelper.DeviceDefaultTimeToUtcZTime(this.DateUpdated));
			temp.put("DateCreated", DateHelper.DeviceDefaultTimeToUtcZTime(this.DateCreated));
			if(this.server_id!="" && this.server_id!=null && !this.server_id.equals(""))
			temp.put("TaskListId", this.server_id);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception e)
		{
			
		}
		return temp.toString();
	}
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
}
