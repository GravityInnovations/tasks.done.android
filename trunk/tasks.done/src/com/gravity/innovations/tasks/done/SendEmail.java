package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.gravity.innovations.tasks.done.provider.GmailSender;

public class SendEmail{
	private Activity mActivity;
	private ArrayList<String> emailList;// = new ArrayList<String>();
	int userDataId;

	public SendEmail(ArrayList<String> _emailsList, int _userDataId) {
		this.emailList = _emailsList;
		this.userDataId = _userDataId;
		send();
	}

	private void send() {
		try {
			SendEmailTask sendETask = new SendEmailTask(emailList);
			sendETask.execute(null, null, null);
		} catch (Exception e) {
			Log.e("SendMail", e.getMessage(), e);
		}
	}

	private class SendEmailTask extends AsyncTask<Void, Void, String> {
		// int count;
		ArrayList<String> list;

		public SendEmailTask(ArrayList<String> _list) {

			// this.count = _count;
			this.list = _list;
		}

		protected String doInBackground(Void... params) {
			{
				String generatedURL = "";
				int count = 0;
				for (int i = 0; i < list.size(); i++) {
					try {
						String emailingID = "promotion@gravity.inv.com";//user your creds. here
						String emailingPassword = "passwordString"; //user your creds. here
						GmailSender sender = new GmailSender(emailingID, emailingPassword);
						// String someTrackingID = "thisIsTrackingID";
						// String someUtmSource = "somrSource";
						// String someUtmMedium = "somrMedium";
						// String someUtmCampaign = "someCampaign";
						generatedURL = "https://play.google.com/store/apps/details?id=com.gravity.innovations.tasks.done&referrer=utm_source%3Dapp_referrer%26utm_medium%3Dapplication%26utm_content%3D"+userDataId+"%26utm_campaign%3Dinit_pub";
								//"https://play.google.com/store/apps/details?id=com.gravity.inv.referraltester&referrer=utm_source%3Dtasks.done_test_app%26utm_medium%3Dtasks.done_app%26utm_term%3Dtesting_referrals%26utm_content%3Dtest_USERID==="
								//+ userDataId
								//+ "_case%26utm_campaign%3Dbeta_testing";
						String recieverName = emailList.get(i);
						 sender.sendMail("userName:XYZ Has invited you to download this app",
						 ""
						 +
						 " This email is only intended for testing purpose. Download app from this URL "
						 + generatedURL,
						 "iqitidarhussain@gmail.com", recieverName);
					} catch (Exception e) {
						Log.e("SendMail", e.getMessage(), e);
					}
					count++;
					if (isCancelled())
						break;
				}
				return " " + count + "records entered";
			}
		}

		protected void onPostExecute(Long result) {
			// showDialog("Downloaded " + result + " bytes");
			Log.e("SendMail: TWO TIMES DONE", "SendMail: TWO TIMES DONE");
		}
	}

}
