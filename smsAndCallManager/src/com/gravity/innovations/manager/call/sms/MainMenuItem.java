package com.gravity.innovations.manager.call.sms;

import android.app.Fragment;

public class MainMenuItem {
	public String Title;
	public String FragmentToken;
	public String Description;
	public MainMenuItem(String Title, String Description, 
			String FragmentToken)
	{
		this.Title = Title;
		this.Description = Description;
		this.FragmentToken = FragmentToken;
	}
}
