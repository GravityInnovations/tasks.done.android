package com.gravity.innovations.tasks.done;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;



public class CopyOfHttpTask extends AsyncTask<Void, Void, JSONObject> {

	    
	    private String Url;
	    private int ResponseCode;
	    private List<NameValuePair> postData = null;
	    private String actionType = null;
	    private String Type = null;
	    private Common.Callbacks.HttpCallback callback = null;
	    //constructs
		private int RequestCode;
	    
	    public CopyOfHttpTask(String strUrl, int RequestCode)
	    {
	    	this.RequestCode = RequestCode;
	        this.Url = strUrl;
	    } 

	    public CopyOfHttpTask(String strUrl, Context context)
	    {
	        this.Url = strUrl;
	       // this.callback = (MainActivityListener)context;
	    } 
	    public CopyOfHttpTask(Activity activity, String strUrl, List<NameValuePair> postData, int RequestCode)
	    {
	    	this.Url = strUrl;
	        this.postData = postData;
	        this.RequestCode = RequestCode;
	        this.callback = (Common.Callbacks.HttpCallback)activity;
	       // this.callback = (MainActivityListener)context;
	    } 
	    public CopyOfHttpTask(String strUrl, List<NameValuePair> postData)
	    {
	    	this.Url = strUrl;
	        this.postData = postData;
	    } 
	 
	    public void SetType(String Type)
	    {
	    	this.Type = Type;
	    }
	    //imp
    @Override
    protected JSONObject doInBackground(Void... params) {
      
        return getJSONFromUrl(Url);
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        if(this.callback != null)
        	this.callback.httpResult(jsonObject, this.RequestCode, ResponseCode);
        	//this.callback.ListenJSONArray(jsonArray);
        //Here you do your work with JSON
    }
    
    public JSONObject getJSONFromUrl(String url) {
        String resultString;
        HttpResponse response;
        JSONObject jsonObject = null;
        try {
        	HttpClient httpclient = new DefaultHttpClient();
        	if(this.postData == null)
            {
                HttpGet get = new HttpGet(this.Url);   
                  
                response = httpclient.execute(get); 
            }
            else
            {
            	
               HttpPost post = new HttpPost(this.Url);
               //post.addHeader("Email", "test");
               post.addHeader("Content-Type", "application/x-www-form-urlencoded");
               
               //post.setHeader("Content-Type", "application/x-www-form-urlencoded");
                if(this.postData != null)
                	//post.setEntity(entity);
                	post.setEntity(new UrlEncodedFormEntity(this.postData,HTTP.UTF_8));
                response = httpclient.execute(post);
            	
                // Execute HTTP Post Request
            }
        	 
            HttpEntity entity = response.getEntity();
            resultString = EntityUtils.toString(entity);
            if(this.postData != null && 
            		(resultString.contains("Success") || resultString.contains("User already exists")))
            {
            	//post
            	this.ResponseCode = Common.HTTP_RESPONSE_OK;
            	jsonObject = null;
            }
            else if(this.postData == null)
            {
            	
            	try{
            		jsonObject = new JSONObject(resultString);
            		this.ResponseCode = Common.HTTP_RESPONSE_OK;
            	}
            	catch(JSONException ex)
            	{
            		jsonObject = new JSONObject();
            		try{
            			jsonObject.put("data", new JSONArray(resultString));
            			this.ResponseCode = Common.HTTP_RESPONSE_OK;
            		}
            		catch(JSONException e)
            		{
            			jsonObject = null;
            			this.ResponseCode = Common.HTTP_RESPONSE_ERROR;
            		}
            	}
            }
            else
            this.ResponseCode = Common.HTTP_RESPONSE_ERROR;
        }
        catch (ClientProtocolException e) {
       	 this.ResponseCode = Common.HTTP_RESPONSE_ERROR;
       //	Log.e("SOME_TAG", Log.getStackTraceString(e));
	    } 
        catch (IOException e) {
	    	 this.ResponseCode = Common.HTTP_RESPONSE_ERROR;
	    //	 Log.e("SOME_TAG", Log.getStackTraceString(e));
	    } catch (Exception e) {
	    	 this.ResponseCode = Common.HTTP_RESPONSE_ERROR;
          //  Log.e("SOME_TAG", Log.getStackTraceString(e));
        }
        
        
        return jsonObject;
    }

	
}