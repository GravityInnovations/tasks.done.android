package com.example.splash;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

public class GridAdapter extends BaseAdapter{
	private Context mContext;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 GridView gridView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	        	gridView = new GridView(mContext);
	        	gridView.setLayoutParams(new GridView.LayoutParams(85, 85));
	        	//gridView.setScaleType(GridView.ScaleType.CENTER_CROP);
	        	gridView.setPadding(8, 8, 8, 8);
	        } else {
	        	gridView = (GridView) convertView;
	        }

	        gridView.setAdapter(mThumbIds);
	        return gridView;
	}
	 private String[] mThumbIds = {
	            "alpha", "beta",
	    };

}
