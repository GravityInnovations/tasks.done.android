package com.gravity.innovations.tasks.done;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogListViewAdapter extends ArrayAdapter<String> {
	Activity mContext;
//	String[] stringArray = new String[] { "Set Time and Date", "Repeat",
//			"Set Location" };
	private final String[] _list_item;
	private final Integer[] _imageId;

	// private Activity mCcontext;

	public DialogListViewAdapter(Activity context, int resource, String[] web_,
			Integer[] image_Id) {
		// super(context, resource);
		// TODO Auto-generated constructor stub
		super(context, R.layout.listview_dialog, web_);
		this.mContext = context;
		this._list_item = web_;
		this._imageId = image_Id;

	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = mContext.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.dialog_listview_row, null,
				true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.et_list_item);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.list_icon);
		txtTitle.setText(_list_item[position]);
		imageView.setImageResource(_imageId[position]);
		return rowView;
	}
}