package com.gravity.innovations.manager.call.sms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectionCondition {
	//db vals
	public String parentProperty;
	public ArrayList<value> Values;
	public SelectionCondition() {
		Values = new ArrayList<value>();
	}
	public static class value {
		public String Property;
		public String Value;
		public value(String property, String value)
		{
			this.Property = property;
			this.Value = value;
			
		}
		public static class ValueComparator implements Comparator<value> {

			@Override
			public int compare(value lhs, value rhs) {
				//if(lhs.Property == rhs.Property){
						return lhs.Property.compareTo(rhs.Property);
					//}
				//else return 0;
			}

		}
		
	}
	public boolean hasSameTitle(String PropertyValue)
	{
		for(int i=0;i<Values.size();i++)
		{
			if(Values.get(i).Property == CommonKeys.PROPRETY_TYPE
					&& Values.get(i).Value == PropertyValue)
				return true;
		}
		return false;
	}
	public boolean hasProperty(String PropertyValue)
	{
		for(int i=0;i<Values.size();i++)
		{
			if(Values.get(i).Value == PropertyValue)
				return true;
		}
		return false;
	}
	public value getValue(String PropertyType)
	{
		for(int i=0;i<Values.size();i++)
		{
			if(Values.get(i).Property == PropertyType)
				return Values.get(i);
		}
		return null;
	}
	
	public HashMap<String, String> getHashMap()
	{
		HashMap<String, String> mHashMap = new HashMap<String, String>();
		for(value item:this.Values)
		{
			mHashMap.put(item.Property, item.Value);
		}
		return mHashMap;
	}
	public JSONObject getJSONObject()
	{
		Collections.sort(Values,new value.ValueComparator());
		
		try {
			JSONArray mJSONArray = new JSONArray();
			JSONObject o = new JSONObject();
			for(int i = 0; i<Values.size();i++)
			{
				
				o.put(Values.get(i).Property, Values.get(i).Value);
				
				
			}
			
			//mJSONArray.put(o);
			return o;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		//mJSONArray.toString();
	}
}
