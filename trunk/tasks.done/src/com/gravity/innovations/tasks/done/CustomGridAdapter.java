package com.gravity.innovations.tasks.done;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CustomGridAdapter extends ArrayAdapter<UserModel>{
	Activity mActivity;
	int resource;
	UserModel mUserModel;
	public CustomGridAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return super.getView(position, convertView, parent);
	}
	 
}

