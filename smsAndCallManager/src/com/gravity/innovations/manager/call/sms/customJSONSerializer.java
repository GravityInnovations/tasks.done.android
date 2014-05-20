package com.gravity.innovations.manager.call.sms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class customJSONSerializer {

	public static JSONObject getJSONObjectold(ArrayList<SelectionCondition> temp, String name)
	{
		JSONArray mArray = new JSONArray();
		//sort temp by property>type
		Collections.sort(temp,new customComparator());
		//make groups
		//for group condition
		String currentType = "";
		JSONArray currentGroup = new JSONArray();
		try {
			for(int i=0;i<temp.size();i++)
			{//mArray.put(temp.get(i).getJSONObject());
				if(!temp.get(i).hasSameTitle(currentType))
					{
					currentGroup = new JSONArray();
					JSONObject o = new JSONObject();
					currentType = temp.get(i).getValue(CommonKeys.PROPRETY_TYPE).Value ;
					
					o.put(currentType+ "(s)", currentGroup);
					mArray.put(o);
					
					
					}
				
				currentGroup.put(temp.get(i).getJSONObject());
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject o = new JSONObject();
		
		try {
			o.put(name, mArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
		
	}
	public static JSONObject getJSONObject(ArrayList<SelectionCondition> temp, String name)
	{
		HashMap<String, ArrayList<JSONObject>> mHashMap =
				new HashMap<String, ArrayList<JSONObject>>();
		for (SelectionCondition item : temp) {
			ArrayList<JSONObject> templist = new ArrayList<JSONObject>();
			for (SelectionCondition itemt : temp) {
				
				if(itemt.hasSameTitle(item.getValue(CommonKeys.PROPRETY_TYPE).Value))
					templist.add(itemt.getJSONObject());
				   
			}
			mHashMap.put(item.getValue(CommonKeys.PROPRETY_TYPE).Value + "(s)", templist);
			//productMap.put(product.getProductCode(), product);
		}
		JSONObject o0 = new JSONObject(mHashMap);
		
		JSONObject o1 = new JSONObject();
		try {
			o1.put(name+"(s)", o0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return o1;
		//return null;
	}
	private static class customComparator implements Comparator<SelectionCondition> {

		@Override
		public int compare(SelectionCondition lhs, SelectionCondition rhs) {
			
			if(lhs.parentProperty == rhs.parentProperty)
				
					if(lhs.getValue(CommonKeys.PROPRETY_TYPE).Value == 
						rhs.getValue(CommonKeys.PROPRETY_TYPE).Value)
						return 1;
					else
						return -1;
			else
			return -1;
		}

		

	}
}
