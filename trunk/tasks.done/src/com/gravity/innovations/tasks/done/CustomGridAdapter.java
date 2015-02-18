package com.gravity.innovations.tasks.done;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

//import com.gravity.innovations.tasks.done.TaskAdapter.TaskModelHolder;

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

public class CustomGridAdapter extends ArrayAdapter<UserModel>{
	Activity mActivity;
	int resource;
	UserModel mUserModel;
	Context mContext;
	ArrayList<UserModel> data = new ArrayList<UserModel>();
	public CustomGridAdapter(Context _context, int _resource, ArrayList<UserModel> _data) {
		super(_context, _resource, _data);
		 this.resource = _resource;
         this.mContext = _context;
         this.data = _data;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//return super.getView(position, convertView, parent);	        
	         View row = convertView;
	         UserModelHolder holder = null;
	      
	         if(row == null)
	         {
	             LayoutInflater inflater = (mActivity).getLayoutInflater();
	             row = inflater.inflate(resource, parent, false);
	          
	             holder = new UserModelHolder();
	             holder.image = (ImageView)row.findViewById(R.id.grid_item_image);
	             holder.displayName = (TextView)row.findViewById(R.id.grid_item_name);
	             
	             row.setTag(holder);
	         }
	         else
	         {
	             holder = (UserModelHolder)row.getTag();
	         }
	      
	         UserModel user = data.get(position);
	         holder.displayName.setText(user.displayName);
	         //convert byte to bitmap take from user class
	        
	         byte[] userImage=user.image;
	         ByteArrayInputStream imageStream = new ByteArrayInputStream(userImage);
	         
	         Bitmap image = BitmapFactory.decodeStream(imageStream);
	         holder.image.setImageBitmap(image);
	         
	        return row;
	      
	     }

	class UserModelHolder {
		ImageView image;
        TextView displayName;

	}
}

