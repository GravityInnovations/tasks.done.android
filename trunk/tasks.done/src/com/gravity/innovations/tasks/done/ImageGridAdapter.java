package com.gravity.innovations.tasks.done;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageGridAdapter extends BaseAdapter {
	private Context mContext;
	Integer[] mThumbIds = null;
	ArrayList<Bitmap> mThumbsBitmap = null;
	public ImageGridAdapter(Integer[] _mThumbIds, Context c) {
		mContext = c;
		mThumbIds = _mThumbIds;
	}
	public ImageGridAdapter(ArrayList<Bitmap> _mThumbBmp, Context c) {
		mContext = c;
		mThumbsBitmap = _mThumbBmp;
	}
	public int getCount() {
		return mThumbIds.length;
	}

	public Object getItem(int position) {
		return null;
		
	}

	public long getItemId(int position) {
		return 0;
	}

	@Override
	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setAdjustViewBounds(true);
			imageView.setPadding(0, 0, 0, 0);

		} else {
			imageView = (ImageView) convertView;
		}
		Bitmap flatIcon, roundIcon;
		if(mThumbIds!=null){
		flatIcon = BitmapFactory.decodeResource(mContext.getResources(),
				mThumbIds[position]);
		
		}
		else
		{
			flatIcon = mThumbsBitmap.get(position);
		}
		roundIcon = getRoundedCornerBitmap(flatIcon);
		// Drawable imageThumb = new
		// BitmapDrawable(mContext.getResources(),roundIcon);

		imageView.setImageBitmap(roundIcon);// .setImageResource(imageThumb);//(mThumbIds[position]);
		return imageView;
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

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		int pixels = 100;
		/*
		 * if you decrease pixel then image will convert to a rectangle with
		 * rounded corners 20 for rectangle and 100 for a pure circle
		 */
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
}