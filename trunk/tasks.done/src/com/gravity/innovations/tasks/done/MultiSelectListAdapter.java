package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	
	public void setNewSelection(View v, int position, boolean value) {
		mSelection.put(position, value);
		v.setBackgroundColor(Color.parseColor("#efefef"));
		notifyDataSetChanged();
	}
	public void setNewSelection(ArrayList<Common.CustomViewsData.MultiSelectRowData> data) {
		for(Common.CustomViewsData.MultiSelectRowData item:data)
		mSelection.put(this.getPosition(item),true);
		
		//v.setBackgroundColor(Color.parseColor("#efefef"));
		notifyDataSetChanged();
	}
	public void setOrRemoveSelection(View v,int position) {
		if (!isPositionChecked(position)) {
			setNewSelection(v,position, true);
		} else
			removeSelection(v,position);
	}

	public boolean isPositionChecked(int position) {
		Boolean result = mSelection.get(position);
		return result == null ? false : result;
	}

	public Set<Integer> getCurrentCheckedPosition() {
		return mSelection.keySet();
	}

	public void removeSelection(View v, int position) {
		mSelection.remove(position);
		v.setBackgroundColor(Color.parseColor("#ffffff"));
		notifyDataSetChanged();
	}

	public void clearSelection() {
		mSelection = new HashMap<Integer, Boolean>();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return data.size();//super.getCount();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(mActivity);
			row = inflater.inflate(R.layout.multiselectlist_row,parent, false);
			holder = new ViewHolder();
			LinearLayout l = (LinearLayout)row.findViewById(R.id.LL);
			holder.text1 = (TextView) l.findViewById(R.id.textView1);
			holder.text2 = (TextView) l.findViewById(R.id.textView2);
			holder.icon1 = (ImageView) row.findViewById(R.id.imageView1);
			holder.icon2 = (ImageView) row.findViewById(R.id.imageView2);
			 row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		Common.CustomViewsData.MultiSelectRowData current = data.get(position);
		if (isPositionChecked(position)) {
			setNewSelection(row,position, true);
		} else
			removeSelection(row,position);
		try {
			//holder.icon.setPadding(5, 5, 5, 5);
			
			holder.text1.setText(current.text1);
			holder.text2.setText(current.text2);
			Resources r = mActivity.getResources();
			if(current.iconRes != null){
				
				Bitmap b = ImageGridAdapter.getRoundedCornerBitmap(Bitmap.createScaledBitmap(converttoimage(current.iconRes),
						150, 150, true));
				holder.icon1.setImageBitmap(b);
			}else{
				
			//holder.icon.setImageResource(R.drawable.catag_personal);
				
			Bitmap b = ImageGridAdapter.getRoundedCornerBitmap(Bitmap.createScaledBitmap((BitmapFactory.decodeResource(r, R.drawable.catag_personal)),
					150, 150, true));
			holder.icon1.setImageBitmap(b);
			
			}
			if(current.iconResId!=-1){
				holder.icon2.setBackgroundResource(current.iconResId);
				holder.icon2.setVisibility(View.VISIBLE);
			}
			else
				holder.icon2.setVisibility(View.GONE);
			// there was nothing here
		} catch (Exception ex) {
			String x = ex.toString();
		}
		// row.setBackgroundColor(mContext.getResources().getColor(android.R.color.background_light));
		// // default color
		//
		// if (mSelection.get(position) != null) {
		// row.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_blue_light));//
		// makes a selected position colored
		// }
		
		//row.setTag(current);
		  //holder.icon.setImageResource(current.iconRes);
		return row;

	}

	public Bitmap converttoimage(byte[] _data) {
		Bitmap bmp = BitmapFactory.decodeByteArray(_data, 0, _data.length);
		return bmp;
}

	class ViewHolder {
		TextView text1, text2;
		ImageView icon1, icon2;
	}

}
