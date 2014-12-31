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

	Integer[] imageId = { R.drawable.catag_social,
			R.drawable.catag_personal, R.drawable.catag_others,
			R.drawable.catag_home,R.drawable.catag_work};
    private Context mContext;

    public IconGridAdapter(Context c) {
        mContext = c;
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
    	LayoutInflater li; //= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View grid;

        if(convertView==null){
            grid = new View(mContext);
            li= ((Activity) mContext).getLayoutInflater();
            grid=li.inflate(R.layout.tasklist_grid_cell, parent, false);
        }else{
            grid = (View)convertView;
        }

        ImageView imageView = (ImageView)grid.findViewById(R.id.image);
        imageView.setImageResource(imageId[position]);

        return grid;
    }
}