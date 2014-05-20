package com.gravity.innovations.manager.call.sms;

import java.util.ArrayList;

public class MainMenu {
	 public ArrayList<MainMenuItem> list;

public MainMenu()
	 {
		 list = new ArrayList<MainMenuItem>();
			list.add(new MainMenuItem("SMS Blocker", 
					"Manage your block/screen list for sms",
					CommonKeys.SMS_BLOCKER_FRAGMENT_TOKEN));
					//"smsBlockerManagerActivity"));
			
	 }
}
