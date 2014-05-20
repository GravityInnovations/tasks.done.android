package com.gravity.innovations.manager.call.sms;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SelectionListViewAdapter extends BaseAdapter {

	private ArrayList<SelectionCondition> list;
    private Context mContext;
    private LayoutInflater inflater;
    public SelectionListViewAdapter(Context context, ArrayList<SelectionCondition> items) 
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
	public SelectionCondition getItem(int position) {
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
			try{
            convertView = inflater.inflate(R.layout.simple_vertical_list_item, parent,false);
        
			}
			catch(Exception ex)
			{
				String a = "";
			}
		}
		
        TextView data1 = (TextView) convertView.findViewById(R.id.data1);
        TextView data2 = (TextView) convertView.findViewById(R.id.data2);
        	
        //button logic here
        SelectionCondition currentItem = getItem(position);
        String data_1 = "";
        		String data_2 = "";
        		for(int i=0;i<currentItem.Values.size();i++)
        		{
        			SelectionCondition.value v = currentItem.Values.get(i);
        			
        			if(v.Property == CommonKeys.DATA1)
        				data_1 = v.Value;
        			else if(v.Property == CommonKeys.DATA2)
        				data_2 = v.Value;
        		}
        		if(data_1 == "" || data_1 == null)
        		{
        			data_1 = data_2;
        			data_2 = "";
        			
        		}
        		data1.setText(data_1);
    			data2.setText(data_2);
    			//break;
        	
        
        //data1.setText(currentItem.Values);
        //description.setText(currentItem.Description);
        return convertView;
	}
	

}

