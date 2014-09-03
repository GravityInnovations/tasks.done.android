package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import com.gravity.innovations.tasks.done.TaskAdapter.TaskModelHolder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserAdapter extends ArrayAdapter<UserModel> {

	Activity mActivity;
	UserModel userModel;
	int resource;
	ArrayList<UserModel> user_data = new ArrayList<UserModel>();

	public UserAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		UserModelHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(mActivity);
			row = inflater.inflate(resource, parent, false);
			holder = new UserModelHolder();
			holder.contact_id = (TextView) row.findViewById(R.id.user_contact_id);
			holder.displayName = (TextView) row
					.findViewById(R.id.user_display_name);
			holder.email = (TextView) row.findViewById(R.id.user_email);
			row.setTag(holder);
		} else {
			holder = (UserModelHolder) row.getTag();
		}

		row.setBackgroundColor(mActivity.getResources().getColor(
				android.R.color.background_light)); // default color

		userModel = user_data.get(position);
		holder.contact_id.setText(userModel.contact_id);
		holder.displayName.setText(userModel.displayName);
		holder.email.setText(userModel.email);
		return row;
	}

	class UserModelHolder {
		TextView contact_id;
		TextView displayName;
		TextView email;
	}
}
