package com.gravity.innovations.tasks.done;

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
		}
		return null;
	}
}
