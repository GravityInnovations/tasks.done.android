package com.gravity.innovations.tasks.done;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

public class DateHelper {
	public static String DATE_Z_FORMAT = "yyyy-M-dd'T'HH:mm:ss.SSSSSSS'Z'";
	public static String DATE_DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	public static TimeZone SystemTime = TimeZone.getDefault();
	public static TimeZone UTCTime = TimeZone.getTimeZone("UTC");
	
	public static String deviceUtcTime()
	{
		final SimpleDateFormat sdf = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
		sdf.setTimeZone(UTCTime);
		final String utcTime = sdf.format(new Date());
		return utcTime;
		//Log.e("datetime", "utc: "+utcTime);
		//Log.e("datetime", "def: "+defTime);
	}
	public static String deviceDefaultTime()
	{
		final SimpleDateFormat sdf = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
		sdf.setTimeZone(SystemTime);
		final String utcTime = sdf.format(new Date());
		return utcTime;
		//Log.e("datetime", "utc: "+utcTime);
		//Log.e("datetime", "def: "+defTime);
	}
	public static String deviceUtcZTime()
	{
		final SimpleDateFormat sdf = new SimpleDateFormat(DATE_Z_FORMAT);
		sdf.setTimeZone(UTCTime);
		final String utcTime = sdf.format(new Date());
		return utcTime;
		//Log.e("datetime", "utc: "+utcTime);
		//Log.e("datetime", "def: "+defTime);
	}
	public static String deviceDefaultZTime()
	{
		final SimpleDateFormat sdf = new SimpleDateFormat(DATE_Z_FORMAT);
		sdf.setTimeZone(SystemTime);
		final String utcTime = sdf.format(new Date());
		return utcTime;
		//Log.e("datetime", "utc: "+utcTime);
		//Log.e("datetime", "def: "+defTime);
	}
	public static String UtcZTimeToDeviceDefaultTime(String dateTime) {
		
		try {
			SimpleDateFormat fromDateFormat = new SimpleDateFormat(
					DATE_Z_FORMAT);//z
			fromDateFormat.setTimeZone(UTCTime);
			Date mDate = fromDateFormat.parse(dateTime);
			SimpleDateFormat toFormat = new SimpleDateFormat(
					DATE_DEFAULT_FORMAT);
			String convertedDate = toFormat.format(mDate);
			
			return convertedDate;
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		//def
		
	}
public static String DeviceDefaultTimeToUtcZTime(String dateTime) {
		
		try {
			
			
			
			SimpleDateFormat fromFormat = new SimpleDateFormat(
					DATE_DEFAULT_FORMAT);
			Date mDate = fromFormat.parse(dateTime);
			
			SimpleDateFormat toDateFormat = new SimpleDateFormat(
					DATE_Z_FORMAT);//z
			toDateFormat.setTimeZone(UTCTime);
			
			String convertedDate = toDateFormat.format(mDate);
			return convertedDate;
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		//def
		
	}
	private static String UtcToDeviceTime(String serverTime, String FromFormat, String ToFormat) {
		try {

			// String ServerDate = data.optString("data");//
			// 2015-01-13T12:00:28.3367416Z
			// localtime on Desktop 5:02PM
			SimpleDateFormat serverDateFormat = new SimpleDateFormat(
					FromFormat);//z

			SimpleDateFormat displayFormat = new SimpleDateFormat(
					ToFormat);//def
			displayFormat.setTimeZone(UTCTime);

			// returns formatted serverDateTime
			long serverTimeLong = serverDateFormat.parse(
					serverTime).getTime();
			// 1421135795416;
			serverTime = displayFormat.format(new Date(serverTimeLong));
			//Log.e("ServerTime", serverTime);
			// returns formatted serverDateTime

			// takes cuurent devive time and convert it to display Format
			String currentDateTime = serverDateFormat.format(new Date());
			long deviceCurrentTimeLong = System.currentTimeMillis();
			String deviceTime = displayFormat.format(new Date(
					deviceCurrentTimeLong));
			
			return deviceTime;
			//Log.e("DeviceTime", deviceTime);
			// 1421216214567
			// takes cuurent devive time and convert it to display Format
		} catch (Exception e) {
			e.printStackTrace();
			return "";
			
		}
	}
}
