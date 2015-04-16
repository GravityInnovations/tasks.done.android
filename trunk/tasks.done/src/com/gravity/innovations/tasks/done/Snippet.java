package com.gravity.innovations.tasks.done;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Snippet {
	Context mContext = null;// initialize with context

	// GetUTC g = new GetUTC();
	// g.GetUTCdatetimeAsDate();

	private String getDeviceIMEI() {
		// requires READ_PHONE_STATE permission in manifest.xml
		TelephonyManager telephonyManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String id = telephonyManager.getDeviceId();
		return id;
	}

	private String geDeviceMac() {
		// requires ACCESS_WIFI_STATE permission in manifest.xml
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wInfo = wifiManager.getConnectionInfo();
		String mac = wInfo.getMacAddress();
		mac = mac + "";
		return mac;
	}

	public class GetUTC {

		static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

		// "yyyy-MM-dd HH:mm:ss z";

		public Date GetUTCdatetimeAsDate() {
			// note: doesn't check for null
			return StringDateToDate(GetUTCdatetimeAsString());
		}

		public String GetUTCdatetimeAsString() {
			final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			final String utcTime = sdf.format(new Date());

			return utcTime;
		}

		public Date StringDateToDate(String StrDate) {
			Date dateToReturn = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);

			try {
				dateToReturn = (Date) dateFormat.parse(StrDate);

				SimpleDateFormat formatter = new SimpleDateFormat(
						"dd.MM.yyyy HH:mm:ss");
				String reportDate = formatter.format(dateToReturn);

				StringTokenizer timeTokens = new StringTokenizer(reportDate,
						" : ");
				String date_string_full = timeTokens.nextToken();// date
				String hours_string = timeTokens.nextToken();// hours
				String minute_string = timeTokens.nextToken();// minute
				String seconds_string = timeTokens.nextToken();// seconds

				StringTokenizer dateTokens = new StringTokenizer(
						date_string_full, ".");
				String date_string = dateTokens.nextToken();// date
				String month_string = dateTokens.nextToken();// month
				String year_string = dateTokens.nextToken();// year

				// yy-mm-ddThh:mm:ss.{6Digit}{sp/+}
				String serverString = year_string + "-" + month_string + "-"
						+ date_string + "T" + hours_string + ":"
						+ minute_string + ":" + seconds_string + ".";
				// getTime();
				// ServerAndDeviceTime();
				serverString = serverString + " ";

			} catch (ParseException e) {
				e.printStackTrace();
			}
			return dateToReturn;
		}
	}
	
	
	
	
	
	
	
	
	
	
	///////////////////////////////////////NDF
	public long getTime() throws ParseException {
		// SimpleDateFormat df = new SimpleDateFormat(
		// "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'GMT'");
		// Date date = (Date) df.parse("2015-01-10T15:09:16.5975889+5:00");
		// long time0 = date.getTime();
		// long d = df.parse ("2015-01-10T15:09:16.5975889+5:00").getTime();

		/************************************************/
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-M-dd'T'HH:mm:ss.SSSSSSS'Z'");
		long time = dateFormat.parse("2015-01-10T15:09:16.5975889Z").getTime();
		return time;// date.getTime();
		/************************************************/

	}

	public void ServerAndDeviceTime() {
		try {

			// String ServerDate = data.optString("data");//
			// 2015-01-13T12:00:28.3367416Z
			// localtime on Desktop 5:02PM
			String DATEFORMAT_SERVER = "yyyy-M-dd'T'HH:mm:ss.SSSSSSS'Z'";
			SimpleDateFormat serverDateFormat = new SimpleDateFormat(
					DATEFORMAT_SERVER);

			String DATEFORMAT_DISPLAY = "yyyy-MM-dd HH:mm:ss.SSS";
			SimpleDateFormat displayFormat = new SimpleDateFormat(
					DATEFORMAT_DISPLAY);
			displayFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

			// returns formatted serverDateTime
			long serverTimeLong = serverDateFormat.parse(
					"2015-01-13T12:00:28.3367416Z").getTime();
			// 1421135795416;
			String serverTime = displayFormat.format(new Date(serverTimeLong));
			Log.e("ServerTime", serverTime);
			// returns formatted serverDateTime

			// takes cuurent devive time and convert it to display Format
			String currentDateTime = serverDateFormat.format(new Date());
			long deviceCurrentTimeLong = System.currentTimeMillis();
			String deviceTime = displayFormat.format(new Date(
					deviceCurrentTimeLong));
			Log.e("DeviceTime", deviceTime);
			// 1421216214567
			// takes cuurent devive time and convert it to display Format
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
}
