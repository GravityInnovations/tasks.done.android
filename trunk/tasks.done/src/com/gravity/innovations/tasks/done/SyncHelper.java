package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import android.content.SharedPreferences;

import com.gravity.innovations.tasks.done.Common.userData;

public class SyncHelper {
	private AppHandlerService mService;
	private ArrayList<TaskListModel> pending_tasklists = new ArrayList<TaskListModel>();
	private ArrayList<Integer> pending_tasks = new ArrayList<Integer>();
	public SyncHelper(AppHandlerService mService)
	{
		this.mService = mService;
		
		// sync_all_tasklists();
	}
	public void sync_all_tasklists()
	{
		
		for(TaskListModel temp: mService.db.tasklists.Get())
		{
			try{
			if(temp.syncStatus!=null && (temp.syncStatus.equals("Synced") || temp.syncStatus=="Synced"))
			{
				
			}
			else{
				sync_tasklist(temp);
			}
			}
			catch(Exception e){
				
			}
		}
	}
	
	public void sync_tasklist(TaskListModel temp)
	{
		//add logic
		//pending_tasklists.add(temp);
		try{
			if(temp.syncStatus==null || (!temp.syncStatus.equals("Synced") && !temp.syncStatus.equals("Delete"))){
				if (temp._id != -1 && temp.server_id.equals("") && mService.hasInternet
						&& mService.user_data.is_sync_type) {
					GravityController.post_tasklist(mService, mService.user_data,
							temp,"add",-1);
					//GravityController.post_tasklist(service, service.user_data,
						//	tasklist, -1);
				}
				else if (temp._id != -1 && !temp.server_id.equals("") && mService.hasInternet
						&& mService.user_data.is_sync_type) {
					GravityController.post_tasklist(mService, mService.user_data,
							temp,"edit",-1);
					//GravityController.post_tasklist(service, service.user_data,
						//	tasklist, -1);
				}
			}
			else if(temp._id != -1 && temp.syncStatus.equals("Delete")&& !temp.server_id.equals("") && mService.hasInternet
					&& mService.user_data.is_sync_type)
			{
				GravityController.post_tasklist(mService, mService.user_data,
						temp,"delete",-1);
			}
		
		}
		catch(Exception e)
		{
			String s = e.getLocalizedMessage();
			s+=e.getMessage();
		}
	}
	
}
