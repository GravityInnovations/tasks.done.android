package com.gravity.innovations.tasks.done;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CustomBaseAdapter_ColorsGrid extends BaseAdapter {

	public static Drawable[] color;
	private Context mContext;

	public CustomBaseAdapter_ColorsGrid(Context c, Drawable[] _color) {
		mContext = c;
		color = _color;
	}

	@Override
	public int getCount() {
		return color.length;
	}

	@Override
	public Object getItem(int arg0) {
		return color[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater li;
		View grid;
		if (convertView == null) {
			grid = new View(mContext);
			li = ((Activity) mContext).getLayoutInflater();
			grid = li.inflate(R.layout.grid_cell_imageview, parent, false);
		} else {
			grid = (View) convertView;
		}
		ImageView imageView = (ImageView) grid.findViewById(R.id.image);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			imageView.setBackground(color[position]);
		} else {
			imageView.setBackgroundDrawable(color[position]);
		}
		return grid;
	}
}