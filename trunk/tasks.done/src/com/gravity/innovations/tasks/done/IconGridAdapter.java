package com.gravity.innovations.tasks.done;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IconGridAdapter extends BaseAdapter {

	public static Integer[] imageId;

	private Context mContext;

	public IconGridAdapter(Context c, Integer[] _imageId) {
		mContext = c;
		imageId = _imageId;
	}

	@Override
	public int getCount() {
		return imageId.length;
	}

	@Override
	public Object getItem(int arg0) {
		return imageId[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater li; // = (LayoutInflater)
							// mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View grid;

		if (convertView == null) {
			grid = new View(mContext);
			li = ((Activity) mContext).getLayoutInflater();
			grid = li.inflate(R.layout.grid_cell_imageview, parent, false);
		} else {
			grid = (View) convertView;
		}

		ImageView imageView = (ImageView) grid.findViewById(R.id.image);
		imageView.setImageResource(imageId[position]);

		return grid;
	}
}