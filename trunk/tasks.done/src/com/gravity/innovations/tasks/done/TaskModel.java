package com.gravity.innovations.tasks.done;

public class TaskModel {
	// handles the task entries
	// private variables
	public int _id;
	public String title;
	public String details;
	public String notes;
	
	public String updated_at;//date time
	public String due_at;//date time
	public String completed_at;//date time
	
	public int fk_tasklist_id = 1;

	
	
	public String server_id;
	public String updated;//date time 
	
	public String etag;
	public String update;
	
	public String self_link;
	public String parent;
	public String status;
	
	
	public int deleted;
	public int hidden;
	public int isSyncSent;
	public int synced;
	public String links;
	
	public String remind_at;
	public int remind_interval;

	public TaskModel() {
		this._id = -1;
	}

	public TaskModel(int id, String title, String _task_details,
			String _task_notes, int fk) {
		this._id = id;
		this.title = title;
		this.details = _task_details;
		this.notes = _task_notes;
		fk_tasklist_id = fk;
	}

	public TaskModel(String title, String _task_details, String _task_notes,
			int fk) {

		this.title = title;
		this.details = _task_details;
		this.notes = _task_notes;
		fk_tasklist_id = fk;
	}

	public TaskModel(String title, String _task_details, String _task_notes) {
		this.title = title;
		this.details = _task_details;
		this.notes = _task_notes;

	}

	public void set(TaskModel temp) {
		this.title = temp.title;
		this.details = temp.details;
		this.notes = temp.notes;
	}

 	// constructors for new db
	public TaskModel(  String title, String _task_details, String _task_notes, 
			String _due,
			int fk
			) {
		 
		this.title = title;
		this.details = _task_details;
		this.notes = _task_notes;			
		this.due_at = _due; 
		
		fk_tasklist_id = fk;



	}
	// constructors for new db
		public TaskModel(  String title, String _task_details, String _task_notes, 
				String _due,
				String _remind_at, int _remind_interval,
				int fk
				) {
			 
			this.title = title;
			this.details = _task_details;
			this.notes = _task_notes;			
			this.due_at = _due;
			
			this.remind_at = _remind_at;
			this.remind_interval = _remind_interval;
			
			fk_tasklist_id = fk;



		}
		public TaskModel( int _id ,String title, String _task_details, String _task_notes, 
				 
				String _remind_at, 
				int fk
				) {
			this._id = _id;
			this.title = title;
			this.details = _task_details;
			this.notes = _task_notes;			
			 
			
			this.remind_at = _remind_at; 
			
			fk_tasklist_id = fk;



		}
	
		// constructors for new db
		public TaskModel(int _id,  String title, String _task_details, String _task_notes, 
				//String _due,
				String _remind_at, int _remind_interval,
				int fk
				) {
			this._id = _id;
			
			this.title = title;
			this.details = _task_details;
			this.notes = _task_notes;			
			
			//this.due_at = _due;
			
			this.remind_at = _remind_at;
			this.remind_interval = _remind_interval;
			
			fk_tasklist_id = fk;



		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	public TaskModel(int id, String title, String _task_details,
//			String _task_notes, int fk,
//			
//			String _server_id, String _etag, String _updated,
//			String _self_link, String _parent, String _status, 
//			String _due, String _completed, 
//			
//			int _deleted, int _hidden, int _isSyncSent,int _synced,
//			
//			String _links) {
//		this._id = id;
//		this.title = title;
//		this.details = _task_details;
//		this.notes = _task_notes;
//		fk_tasklist_id = fk;
//
//		this.server_id = _server_id;
//		this.etag = _etag;
//		this.updated = _updated;
//		this.self_link = _self_link;
//		this.parent = _parent;
//		this.status = _status;
//		this.due = _due;
//		this.completed = _completed;
//		
//		this.deleted = _deleted;
//		this.hidden = _hidden;
//		this.isSyncSent = _isSyncSent;
//		this.synced = _synced;
//		
//		this.links = _links;
//
//	}
 
//	public TaskModel(String title, String _task_details,
//			String _task_notes, int fk,
//			
//			String _server_id, String _etag, String _updated,
//			String _self_link, String _parent, String _status, 
//			String _due, String _completed, 
//			
//			int _deleted, int _hidden, int _isSyncSent,int _synced,
//			
//			String _links) {
//
//		this.title = title;
//		this.details = _task_details;
//		this.notes = _task_notes;
//		fk_tasklist_id = fk;
//
//		this.server_id = _server_id;
//		this.etag = _etag;
//		this.updated = _updated;
//		this.self_link = _self_link;
//		this.parent = _parent;
//		this.status = _status;
//		this.due = _due;
//		this.completed = _completed;
//		
//		this.deleted = _deleted;
//		this.hidden = _hidden;
//		this.isSyncSent = _isSyncSent;
//		this.synced = _synced;
//		
//		this.links = _links;
//
//	}
//

}
