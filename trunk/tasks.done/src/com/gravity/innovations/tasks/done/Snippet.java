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

	// /////////////////////////////////////NDF
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

	

}
