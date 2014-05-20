package com.gravity.innovations.manager.call.sms;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
//testing
public class MainMenuAdapter extends BaseAdapter {

	private List<MainMenuItem> list;
    private Context mContext;
    private LayoutInflater inflater;
    public MainMenuAdapter(Context  context, List<MainMenuItem> items) 
    {try{
        this.mContext=context;
        this.list = items;

        this.inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
}
catch(Exception ex)
{
	String a = ex.getMessage();
	a += "";
}
    }
    
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public MainMenuItem getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            convertView = inflater.inflate(R.layout.main_menu_item, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.menuitem_title);
        TextView description = (TextView) convertView.findViewById(R.id.menuitem_desc);
        MainMenuItem currentItem = getItem(position);
        
        title.setText(currentItem.Title);
        description.setText(currentItem.Description);
        return convertView;
	}
	

}

