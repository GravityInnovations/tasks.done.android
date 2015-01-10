package com.gravity.innovations.tasks.done;

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
            	String s = extras.getString("data").toString();
            	JSONObject data = JsonHelper.toJsonObject(s);
            	if(data!=null)
            	handle(data);
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
	
	
	private void handle(JSONObject bundle)
	{
		 try {
			String status = bundle.optString("status");
			String message = bundle.optString("message");
			String dataid = bundle.optString("dataid");
			JSONObject sender = bundle.optJSONObject("sender");
			JSONObject data = bundle.optJSONObject("data");
			if(status == TASKLIST_ADD)
			{
				//if self or not logic
				if(mService.user_data.gravity_user_id == sender.optString("UserId") 
					|| mService.user_data.gravity_user_id.equals(sender.optString("UserId"))){
					//for self
					TaskListModel temp = mService.db.TaskList_Single(Integer.parseInt(dataid));//new TaskListModel();
					
					temp.server_id = data.optString("TaskListId");
					temp.updated = data.optString("updated");
					mService.response_new_tasklist(temp);
				}
				//temp.user_id = user._id;//data.optString("");
			}
			
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
	}
	private static String TASKLIST_ADD = "add_tasklist";
	private static String TASKLIST_DELETE = "delete_tasklist";
	private static String TASKLIST_EDIT = "edit_tasklist";
	
}
