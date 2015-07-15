package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomIconListAdapter extends
		ArrayAdapter<CustomIconListAdapter.OptionsModel> {

	private int layoutResourceId;
	private int pos = -1;
	private Activity mActivity;
	private Animation anim;

	public CustomIconListAdapter(Activity activity, int resource,
			ArrayList<CustomIconListAdapter.OptionsModel> objects) {

		super(activity, resource, objects);
		// TODO Auto-generated constructor stub
		this.mActivity = activity;
		this.layoutResourceId = resource;
	}

	public void setBlinkingPosition(int _position, Animation _anim) {
		this.pos = _position;
		this.anim = _anim;
		notifyDataSetChanged();
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
			if (pos != -1 && anim != null) {
				if (position == pos) {
					row.setAnimation(anim);
				}
			}
			if (pos == -1) {
			}
		}

		return row;

	}

	public static class OptionsModel {
		int Icon_Resource;
		String Text;

		OptionsModel(int Icon_Resource, String Text) {

			this.Icon_Resource = Icon_Resource;
			this.Text = Text;
		}
	}
}
