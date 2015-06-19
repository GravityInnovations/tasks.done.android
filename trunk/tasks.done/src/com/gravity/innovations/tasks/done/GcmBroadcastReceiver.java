package com.gravity.innovations.tasks.done;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;


public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	private AppHandlerService mService;

	@Override
	public void onReceive(Context context, Intent intent) {
		mService = ((TheApp) context.getApplicationContext()).getService();//.startService((Common.Callbacks.ServiceCallback)context);
		Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context.getApplicationContext());
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
               // sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: " +
                       // extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
            	String s_data = extras.getString("data").toString();
            	String s_cmd = extras.getString("cmd").toString();
            	String s_type = extras.getString("datatype").toString();
            	String s_sender = extras.getString("sender").toString();
            	int s_dataid = Integer.parseInt(extras.getString("dataid"));
            	//>>>>>>>>>>>>>>>>>.. sender info here
            	
            	JSONObject data = JsonHelper.toJsonObject(s_data);
            	if(data!=null)
            	handle(data,s_cmd,s_type,s_sender,s_dataid);
            	//mService.sendNotification("task.done", s, 3000);
            }
        }
		
		
		//if(mService!=null)
			//handle(context, intent);
			//mService.sendNotification("task.done", "GCM RECIEVED", 3000);
		// Explicitly specify that GcmIntentService will handle the intent.
//        ComponentName comp = new ComponentName(context.getPackageName(),
//                GcmIntentService.class.getName());
//        // Start the service, keeping the device awake while it is launching.
//        startWakefulService(context, (intent.setComponent(comp)));
//        setResultCode(Activity.RESULT_OK);
	}
	
	
	private void handle(JSONObject bundle, String cmd, String datatype, String sender,int dataid)
	{
		 try {
			 if(sender.equals(mService.user_data.google_reg_id) || sender == mService.user_data.google_reg_id)
			 {
				
				 	//TaskListModel mTaskList = mService.db.tasklists.Get(dataid);
				 	TaskListModel Temp = new TaskListModel(bundle,mService.user_data, mService.db);
				 	Temp._id = dataid;
				 	
				 	Temp.syncStatus = "Synced";
				 	Temp.syncStatusTimeStamp = DateHelper.deviceDefaultTime();
					mService.response_new_tasklist(Temp);
				 	//temp.server_id = data.optString("TaskListId");
					//temp.DateUpdated = data.optString("updated");
				 	
				 	//mService.db.tasklists.SetSynced(mTaskList);
			 }
			 else{
				 if(datatype == DATA_TASKLIST)
				 {
					 if(cmd.equals(CMD_ADD))
					 {
						 
						 mService.json_add_tasklist(bundle);
					 }
				 }
				 else if(datatype == DATA_TASK)
				 {
					 
				 }
			 }
			 
			 
			 
			/*String status = bundle.optString("status");
			String message = bundle.optString("message");
			String dataid = bundle.optString("dataid");
			JSONArray users = bundle.optJSONArray("users");
			
			JSONObject sender = bundle.optJSONObject("sender");
			JSONObject data = bundle.optJSONObject("data");
			if(status == TASKLIST_ADD || status.equals(TASKLIST_ADD))
			{
				//if self or not logic
				if(mService.user_data.gravity_user_id!=null && sender.optString("UserId") !=null){
				if(mService.user_data.gravity_user_id == sender.optString("UserId") 
					|| mService.user_data.gravity_user_id.equals(sender.optString("UserId"))){
					//for self
					validate_tasklist(Integer.parseInt(dataid), data);
				}
				
				}
				//temp.user_id = user._id;//data.optString("");
			}
			else if(status == TASKLIST_SHARE_ADD || status.equals(TASKLIST_SHARE_ADD))
			{
				//if self or not logic
				if(mService.user_data.gravity_user_id!=null && sender.optString("UserId") !=null){
					String tasklistid = data.optString("TaskListId");
					String color = data.optString("Color");
					int icon = data.optInt("icon");
					TaskListModel tasklist = new TaskListModel();
					String usid = data.optString("UserId");
					UserModel owner = mService.db.users.Get(usid);
					tasklist.server_id = tasklistid;
					if(mService.user_data.gravity_user_id == usid 
							|| mService.user_data.gravity_user_id.equals(usid)){
						tasklist.owner_id = mService.user_data._id;//owner._id;
					}
					else if(owner!=null)
					{
						tasklist.owner_id = owner._id;
					}
					tasklist.owner = owner;
					//tasklist.user_id = mService.user_data._id;//owner._id;
					tasklist.title = data.optString("Title");
					tasklist.icon_identifier = icon;
					tasklist.fragmentColor = color;
					tasklist.syncStatus = "Synced";
					tasklist.DateUpdated = data.optString("updated");
					if(mService.user_data.gravity_user_id == sender.optString("UserId") 
						|| mService.user_data.gravity_user_id.equals(sender.optString("UserId"))){
					
			
						
						mService.response_share_add_tasklist(tasklist,owner, users, true);
					}
					else {
						
						mService.response_share_add_tasklist(tasklist,owner, users, false);
					}
				
				}
				//temp.user_id = user._id;//data.optString("");
			}
			else if(status == TASKLIST_SHARE_DELETE || status.equals(TASKLIST_SHARE_DELETE))
			{
				if(mService.user_data.gravity_user_id!=null && sender.optString("UserId") !=null){
					if(mService.user_data.gravity_user_id == sender.optString("UserId") 
						|| mService.user_data.gravity_user_id.equals(sender.optString("UserId"))){
						
						String tasklistid = data.optString("TaskListId");
	//					
						//mService.response_share_remove_tasklist(tasklistid, userids);
					}
					else {
						
					}
				
				}
			}*/
			
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
	}
	private static String DATA_TASKLIST = "TaskList";
	private static String DATA_TASK = "Task";
	private static String CMD_ADD = "add";
	private static String CMD_DELETE = "delete";
	private static String CMD_EDIT = "edit";
	
	private static String CMD_SHARE = "share";
	
	
	private static String TASKLIST_ADD = "add_tasklist";
	private static String TASKLIST_DELETE = "delete_tasklist";
	private static String TASKLIST_EDIT = "edit_tasklist";
	private static String TASKLIST_SHARE_ADD = "add_share_tasklist";
	private static String TASKLIST_SHARE_DELETE = "delete_share_tasklist";
	
	
	private void validate_tasklist(int TasklistLocalId, JSONObject data)
	{
		TaskListModel temp = mService.db.tasklists.Get(TasklistLocalId);//TaskList_Single(TasklistLocalId);//new TaskListModel();
		temp.syncStatus = "Synced";
		temp.server_id = data.optString("TaskListId");
		temp.DateUpdated = data.optString("updated");
		//mService.response_new_tasklist(temp);
	}
	
}
