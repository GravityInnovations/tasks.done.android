package com.gravity.innovations.tasks.done;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonHelper {
	public static JSONObject toJsonObject(String str)
	{
		try {
			return  new JSONObject(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return toJsonArray(str);
		}
		//return null;
	}
	public static JSONObject toJsonArray(String str)
	{
		try {
			
			JSONObject o = new JSONObject();
			o.put("data", new JSONArray(str));
			return o;
			//return  new JSONArray(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
