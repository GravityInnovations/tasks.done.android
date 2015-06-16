package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomBaseAdapter_WeekdaysGrid extends BaseAdapter {

	public static String[] days;
	private Context mContext;
	//private int selected;
	
	private ArrayList<Boolean> selectionArray = new ArrayList<Boolean>();

	public CustomBaseAdapter_WeekdaysGrid(Context c, String[] _days) {
		mContext = c;
		days = _days;
		for (int i = 0; i <= 7; i++) {
			selectionArray.add(true);
		}
	}
	
	public ArrayList<Boolean> getSelectionArray() {
		return selectionArray;
	}

	@Override
	public int getCount() {
		return days.length;
	}

	@Override
	public Object getItem(int arg0) {
		return days[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void setSelection(int position) {
		//selected = position;
		if (selectionArray.get(position) == false) {
			selectionArray.set(position, true);
		} else {
			
			selectionArray.set(position, false);
		}

		notifyDataSetChanged();
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater li;
		View gridItem;
		if (convertView == null) {
			gridItem = new View(mContext);
			li = ((Activity) mContext).getLayoutInflater();
			gridItem = li.inflate(R.layout.grid_cell_textview, parent, false);
		} else {
			gridItem = (View) convertView;
		}
		TextView textView = (TextView) gridItem.findViewById(R.id.text);
		textView.setText(days[position]);
		
		if (selectionArray.get(position) == false) {
			textView.setBackground(Common.ShapesAndGraphics.getCircularShape_day("#ff33b5e5"));
			textView.setTextColor(Color.WHITE);
			//selectionArray.set(position, true);
		} else {
			textView.setBackground(Common.ShapesAndGraphics
					.getRingShape_day("#ff33b5e5"));
			textView.setTextColor(Color.BLACK);
			//selectionArray.set(position, false);
		}
		return gridItem;
	}

}