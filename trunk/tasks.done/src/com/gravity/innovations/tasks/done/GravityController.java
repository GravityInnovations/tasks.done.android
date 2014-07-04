package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;

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
	public static void register_gravity_account(Activity activity,String Email, String gcm_reg_id, int RequestCode)
	{
		List<NameValuePair> postData = new ArrayList<NameValuePair>();          
		postData.add(new BasicNameValuePair("Email", Email));
		postData.add(new BasicNameValuePair("Regid", gcm_reg_id));
		
		HttpTask Temp = new HttpTask(activity, Common.GRAVITY_ACCOUNT_URL, postData, RequestCode);
		Temp.execute();
	}
	public static void get_gravity_accounts(int RequestCode)
	{
		
		HttpTask Temp = new HttpTask(Common.GRAVITY_ACCOUNT_URL, RequestCode);
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
