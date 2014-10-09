package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MultiSelectListAdapter extends
		ArrayAdapter<Common.CustomViewsData.MultiSelectRowData> {

	private Context mContext;
	private Activity mActivity;
	int layoutResourceId;
	ArrayList<Common.CustomViewsData.MultiSelectRowData> data = new ArrayList<Common.CustomViewsData.MultiSelectRowData>();
	private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

	public MultiSelectListAdapter(Activity mActivity, int layoutResourceId,
			ArrayList<Common.CustomViewsData.MultiSelectRowData> data) {
		// (data.getClass().toString())
		super(mActivity, layoutResourceId, data);
		this.mActivity = mActivity;
		this.layoutResourceId = layoutResourceId;
		this.mContext = mActivity;
		this.data = data;
		notifyDataSetChanged();
	}

	public Common.CustomViewsData.MultiSelectRowData getSingularSelected() {
		if (mSelection.size() == 1) {
			for (Integer temp : getCurrentCheckedPosition())
				return data.get(temp);
		}
		return null;
	}

	public ArrayList<Integer> getSelected() {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < data.size(); i++) {
			if (isPositionChecked(i)) {
				temp.add(i);
			}
		}
		return temp;
	}

	public void setNewSelection(int position, boolean value) {
		mSelection.put(position, value);
		notifyDataSetChanged();
	}

	public void setOrRemoveSelection(int position) {
		if (!isPositionChecked(position)) {
			setNewSelection(position, true);
		} else
			removeSelection(position);
	}

	public boolean isPositionChecked(int position) {
		Boolean result = mSelection.get(position);
		return result == null ? false : result;
	}

	public Set<Integer> getCurrentCheckedPosition() {
		return mSelection.keySet();
	}

	public void removeSelection(int position) {
		mSelection.remove(position);
		notifyDataSetChanged();
	}

	public void clearSelection() {
		mSelection = new HashMap<Integer, Boolean>();
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = new ViewHolder();
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(mActivity);
			row = inflater.inflate(R.layout.multiselectlist_row, parent, false);
			holder.text1 = (TextView) row.findViewById(R.id.textView1);
			holder.text2 = (TextView) row.findViewById(R.id.textView2);
			holder.icon = (ImageView) row.findViewById(R.id.imageView1);
			// is image view ko correct name dall ke check karna ha

			// row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		// row.setBackgroundColor(mContext.getResources().getColor(android.R.color.background_light));
		// // default color
		//
		// if (mSelection.get(position) != null) {
		// row.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_blue_light));//
		// makes a selected position colored
		// }
		Common.CustomViewsData.MultiSelectRowData current = data.get(position);
		try {
			holder.text1.setText(current.text1);
			holder.text2.setText(current.text2);
			// holder.icon.setImageBitmap(converttoimage(current.iconRes));//
			// there was nothing here
		} catch (Exception ex) {
			String x = ex.toString();
		}

		// holder.icon.setImageResource(current.iconRes);
		return row;

	}

	public Bitmap converttoimage(byte[] _data) {
		Bitmap bmp = BitmapFactory.decodeByteArray(_data, 0, _data.length);
		return bmp;

	}

	class ViewHolder {
		TextView text1, text2;
		ImageView icon;
	}

}
