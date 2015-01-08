package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import com.gravity.innovations.tasks.done.MultiSelectListAdapter.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class BitmapAdapter extends
		ArrayAdapter<Bitmap> {
	private Context mContext;
	private Activity mActivity;
	private GridView mGridView;
	
	public BitmapAdapter(Context context, GridView mGridView, int resource, ArrayList<Bitmap> objects, Activity mActivity) {
		super(context, resource, objects);
		this.mContext = mContext;
		this.mActivity = mActivity;
		this.mGridView = mGridView;
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
		//mGridView.setLayoutParams(new LayoutParams(mActivityLayoutParams.WRAP_CONTENT));
//		GridView.LayoutParams params = (GridView.LayoutParams)
//				mGridView.getLayoutParams();
//			
//				params.height = 300;
//				mGridView.setLayoutParams(params);
//				mGridView
	}


	@Override
	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			LayoutInflater inflater = LayoutInflater.from(mActivity);
			convertView = inflater.inflate(R.layout.grid_cell,parent, false);
			holder = new ViewHolder();
			holder.img =(ImageView) convertView.findViewById(R.id.grid_item_image);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Bitmap flatIcon, roundIcon;
		
		
			flatIcon = this.getItem(position);
		
		roundIcon = ImageGridAdapter.getRoundedCornerBitmap(flatIcon);
		// Drawable imageThumb = new
		// BitmapDrawable(mContext.getResources(),roundIcon);

		holder.img.setImageBitmap(roundIcon);// .setImageResource(imageThumb);//(mThumbIds[position]);
		return convertView;
		/*
		 * View grid; LayoutInflater inflater = (LayoutInflater) mContext
		 * .getSystemService(Context.LAYOUT_INFLATER_SERVICE); if (convertView
		 * == null) { grid = new View(mContext); grid =
		 * inflater.inflate(R.layout.grid_cell_tasklist, null); ImageView
		 * imageView = (ImageView)grid.findViewById(R.id.grid_image);
		 * 
		 * 
		 * imageView.setImageResource(mThumbIds[position]); } else { grid =
		 * (View) convertView; } return grid;
		 */
	}
	class ViewHolder {
		ImageView img;
	}
}
