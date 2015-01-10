package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;

public class GravityController {

	public void google_get_user_info()
	{
		/*post
		  List<NameValuePair> postData = new ArrayList<NameValuePair>();          
		postData.add(new BasicNameValuePair("email", account.name));
		postData.add(new BasicNameValuePair("username", ""));
		HttpTask RegisterOnSelf = new HttpTask("http://orchard.million8.com/user/register", postData);
		RegisterOnSelf.execute();
		 */
		/*HttpTask temp = new HttpTask(Common.USER_INFO_URL);
		temp.execute();*/
	}
	public static void register_gravity_account(Context context,String Email, String gcm_reg_id,String Username, int RequestCode)
	{
		List<NameValuePair> postData = new ArrayList<NameValuePair>();          
		postData.add(new BasicNameValuePair("Email", Email));
		postData.add(new BasicNameValuePair("Regid", gcm_reg_id));
		postData.add(new BasicNameValuePair("Name", Username));
		HttpTask Temp = new HttpTask(context, Common.GRAVITY_ACCOUNT_URL, postData,
				Common.HttpMethod.HttpPost,RequestCode);
		Temp.execute();
	}
	public static void validate_gravity_accounts(Context context, String Emails,int RequestCode)
	{
		String params = "?Emails="+Emails;
		HttpTask Temp = new HttpTask(context,Common.GRAVITY_ACCOUNT_URL+params, null,
				Common.HttpMethod.HttpGet,
				RequestCode);
		Temp.execute();
	}
	
	public static void get_tasklists(Context context, Common.userData user_data,int RequestCode)
	{
		
		String params = "?id="+user_data.gravity_user_id;
		//HttpTask Temp = new HttpTask(Common.GRAVITY_TASKLIST_URL+params, RequestCode);
		HttpTask Temp = new HttpTask(context, Common.GRAVITY_TASKLIST_URL+params, null,
				Common.HttpMethod.HttpGet,
				RequestCode);
		Temp.execute();
	}
	public static void get_tasks(Context context, Common.userData user_data,int RequestCode)
	{
		
		String params = "?id="+user_data.gravity_user_id;
		//HttpTask Temp = new HttpTask(Common.GRAVITY_TASKLIST_URL+params, RequestCode);
		HttpTask Temp = new HttpTask(context, Common.GRAVITY_TASK_URL+params, null,
				Common.HttpMethod.HttpGet,
				RequestCode);
		Temp.execute();
	}
	public static void post_tasklist(Context context,Common.userData user_data, TaskListModel tasklist, int RequestCode)
	{
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair("deviceid", user_data.google_reg_id));
		postData.add(new BasicNameValuePair("userid", user_data.gravity_user_id));
		postData.add(new BasicNameValuePair("dataid",tasklist._id+""));
		//add or edit
		if(tasklist.server_id!= ""){
			postData.add(new BasicNameValuePair("_id", tasklist.gravity_id));
			postData.add(new BasicNameValuePair("action", "edit"));
		}
		else
			postData.add(new BasicNameValuePair("action", "add"));
		postData.add(new BasicNameValuePair("title", tasklist.title));
		HttpTask Temp = new HttpTask(context, Common.GRAVITY_TASKLIST_URL, postData,Common.HttpMethod.HttpPost,
				RequestCode);
		Temp.execute();
	}
	/*public static void send_gcm_req_code(Activity activity,int RequestCode,String Code)
	{
		List<NameValuePair> postData = new ArrayList<NameValuePair>();          
		postData.add(new BasicNameValuePair("Regid", Code));
		
		HttpTask Temp = new HttpTask(activity, Common.GRAVITY_ACCOUNT_URL, postData, RequestCode);
		Temp.execute();
	}*/
}
