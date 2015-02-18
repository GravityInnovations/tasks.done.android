package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.List;

import com.gravity.innovations.tasks.done.CustomIconListAdapter.OptionsModel;
import com.gravity.innovations.tasks.done.TaskListAdapter.TaskListHolder;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomIconListAdapter extends ArrayAdapter<CustomIconListAdapter.OptionsModel>{

	private int layoutResourceId;
	private Activity mActivity;

	public CustomIconListAdapter(Activity activity, int resource,ArrayList<CustomIconListAdapter.OptionsModel> objects) {
		
		super(activity, resource, objects);
		// TODO Auto-generated constructor stub
		this.mActivity = activity;
		this.layoutResourceId = resource;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
		
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(mActivity);
			row = inflater.inflate(layoutResourceId, parent, false);
			TextView title = (TextView) row.findViewById(R.id.txt_title);
			
			ImageView icon = (ImageView) row.findViewById(R.id.list_icon);
			OptionsModel temp = this.getItem(position);
			title.setText(temp.Text);
			icon.setBackgroundResource(temp.Icon_Resource);
		} 
		
		
		return row;
		
	}

	public static class OptionsModel{
		int Icon_Resource;
		String Text;
		OptionsModel(int Icon_Resource,
		String Text){
			
			this.Icon_Resource = Icon_Resource;
			this.Text = Text;
		}
	}
}
