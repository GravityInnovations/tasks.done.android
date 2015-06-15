package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmSchedulingService extends IntentService {
	DatabaseHelper db;
	Context mContext;
	private NotificationManager mNotificationManager;
	private AlarmBroadcastReciever mAlarmBroadcastReciever = new AlarmBroadcastReciever();

	// private static final String PREF_TASKLIST_ID_PRESSED =
	// "tasklist_pressed_id";
	// private static final String PREF_TASK_ID_FOR_TASKLIST_PRESSED =
	// "task_id";

	public AlarmSchedulingService() {
		super("SchedulingService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			// Get passed values
			Bundle extras = intent.getExtras();
			sendNotification(extras.getInt(Common.KEY_EXTRAS_ALARM_ID));
			// Release the wake lock provided by the BroadcastReceiver.
			AlarmBroadcastReciever.completeWakefulIntent(intent);
		} catch (Exception e) {
			Log.e("AlarmShedullingSerivce:onHandleIntetnt",
					e.getLocalizedMessage());
		}
	}

	public void sendNotification(int alarm_id) {
		mContext = getApplicationContext();
		db = new DatabaseHelper(mContext);
		TaskNotificationsModel model = db.notification.Get(alarm_id);
		TaskModel task = db.tasks.Get(model.fk_task_id);
		String alarmTitle = task.title;
		String alarmMessage = task.details;
		//alarmMessage = task.details + "Notification ID: " + model._id
				//+ " Interval: " + model.interval + "interval Type is: "
				//+ model.interval_type;

		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		int id = task.fk_tasklist_id;

		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(Common.KEY_EXTRAS_LIST_ID, task.fk_tasklist_id);
		intent.putExtra(Common.KEY_EXTRAS_TASK_ID, task._id);
		intent.putExtra(Common.KEY_EXTRAS_NOTIFICATION_ID_COMPARING, alarm_id);

		if (task.title.toLowerCase().contains("call")) {
			intent.putExtra(Common.KEY_EXTRAS_NOTIFICATION_ACTION_TYPE, 1);
			intent.putExtra(Common.KEY_EXTRAS_NOTIFICATION_RECEPIENT,
					Common.ContactStringConversion.getPhoneNumber(task.title,
							this));
		} else if (task.title.toLowerCase().contains("sms")) {
			intent.putExtra(Common.KEY_EXTRAS_NOTIFICATION_ACTION_TYPE, 2);
			intent.putExtra(Common.KEY_EXTRAS_NOTIFICATION_RECEPIENT,
					Common.ContactStringConversion.getPhoneNumber(task.title,
							this));
		} else if (task.title.toLowerCase().contains("email")) {
			intent.putExtra(Common.KEY_EXTRAS_NOTIFICATION_ACTION_TYPE, 3);
			intent.putExtra(Common.KEY_EXTRAS_NOTIFICATION_RECEPIENT,
					Common.ContactStringConversion.getEmailAddress(task.title,
							this));
		}

		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.setFlags(Intent.FILL_IN_DATA | Intent.FLAG_ACTIVITY_NEW_TASK); // these
																				// two
																				// work
																				// the
																				// best

		PendingIntent contentIntent = PendingIntent.getActivity(this, alarm_id,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// use the right class it should be called from the where alarms are set
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setAutoCancel(true)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(alarmTitle)
				.setStyle(
						new NotificationCompat.BigTextStyle()
								.bigText(alarmMessage))

				.setDefaults(
						Notification.DEFAULT_SOUND
								| Notification.DEFAULT_VIBRATE
								| Notification.DEFAULT_LIGHTS
								| Notification.FLAG_AUTO_CANCEL)

				.setContentText(alarmMessage);

		if (task.title.toLowerCase().contains("call")) {
			mBuilder.addAction(R.drawable.ic_perm_phone_msg_grey600_24dp,
					"Call", contentIntent);
		} else if (task.title.toLowerCase().contains("sms")) {
			mBuilder.addAction(R.drawable.ic_perm_phone_msg_grey600_24dp,
					"Sms", contentIntent);
		} else if (task.title.toLowerCase().contains("email")) {
			mBuilder.addAction(R.drawable.ic_email_grey600_24dp, "Email",
					contentIntent);
		}

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(alarm_id, mBuilder.build());

		if (task.rep_intervalType != 0) {
			// is Repeating type Alarm
			int interval = task.rep_interval;
			if (task.rep_intervalType == 1) {
				// daily

				if (task.rep_intervalExpiration != null) {
					try {
						Integer numOfEvents = Integer
								.valueOf(task.rep_intervalExpiration.toString());
						if (numOfEvents == 0) {
							// cancel alarm
							mAlarmBroadcastReciever.cancelAlarm(mContext,
									alarm_id);
						} else if (numOfEvents > 0 && numOfEvents <= 999) {
							// fine
							// keep assigning next alarmas
							task.rep_intervalExpiration = String
									.valueOf(numOfEvents - 1);
							db.tasks.Edit(task);
							set_NextAlarm(db.tasks.Get(task._id), mContext,
									model._id);
						}

					} catch (Exception e) {
						Log.e("is a longTime", e.getLocalizedMessage());
						Long untilADate = Long
								.valueOf(task.rep_intervalExpiration.toString());
						if (untilADate <= Long.valueOf(task.startDateTime)) {
							// cancel alarm
							mAlarmBroadcastReciever.cancelAlarm(mContext,
									alarm_id);
						} else {
							// fine
							// keep assigning next alarmas
							db.tasks.Edit(task);
							set_NextAlarm(db.tasks.Get(task._id), mContext,
									model._id);
						}
					}
				} else {
					// no rep_intervalExpiration exists
					Long endDateTime = Long.valueOf(task.endDateTime);
					if (endDateTime <= Long.valueOf(task.startDateTime)) {
						// do nothing
						// cancel alarm
						mAlarmBroadcastReciever.cancelAlarm(mContext, alarm_id);
					} else if (endDateTime > Long.valueOf(task.startDateTime)) {
						Long addTheseDaysToExisting = (Long.valueOf(interval) * 86400000L);
						// 1 day in millis = 86400000L
						task.startDateTime = String
								.valueOf((Long.valueOf(task.startDateTime) + addTheseDaysToExisting));

						db.tasks.Edit(task);
						set_NextAlarm(db.tasks.Get(task._id), mContext,
								model._id);
					}
				}
			} else if (task.rep_intervalType == 2) {
				// weekly
				try {
					// if interval_Expire does Exist
					if (task.rep_intervalExpiration != null) {
						try {
							// identify whether its an event based or time based
							Integer numOfEvents = Integer
									.valueOf(task.rep_intervalExpiration
											.toString());
							if (numOfEvents == 0) {
								// cancel alarm
								mAlarmBroadcastReciever.cancelAlarm(mContext,
										alarm_id);
							} else if (numOfEvents > 0 && numOfEvents <= 999) {
								// assign Alarm
								task.rep_intervalExpiration = String
										.valueOf(numOfEvents - 1);
								if (!task.rep_value.toString().isEmpty()
										|| task.rep_value.toString() != null) {
									weeklyAlarm_calculations(task, model);
								}
							}
						} catch (Exception e) {
							// It is a time based
							Log.e("is a longTime", e.getLocalizedMessage());
							Long untilADate = Long
									.valueOf(task.rep_intervalExpiration
											.toString());
							if (untilADate <= Long.valueOf(task.startDateTime)) {
								// cancel alarm
								mAlarmBroadcastReciever.cancelAlarm(mContext,
										alarm_id);
							} else {
								// keep assigning next alarmas
								if (!task.rep_value.toString().isEmpty()
										|| task.rep_value.toString() != null) {
									weeklyAlarm_calculations(task, model);
								}
							}
						}
					}
					// /////////
					else if (task.rep_intervalExpiration == null) {
						if (task.rep_value == null) {
							// no days entered

							Long endDateTime = Long.valueOf(task.endDateTime);

							Calendar _cal = Calendar.getInstance();
							_cal.setTimeInMillis(Long
									.valueOf(task.startDateTime));
							_cal.add(Calendar.DAY_OF_WEEK, 7);

							Long newDateTime = _cal.getTimeInMillis();

							task.startDateTime = String.valueOf(newDateTime);

							if (endDateTime <= Long.valueOf(task.startDateTime)) {
								// do nothing
								// cancel alarm
								mAlarmBroadcastReciever.cancelAlarm(mContext,
										alarm_id);
							} else if (endDateTime > Long
									.valueOf(task.startDateTime)) {

								db.tasks.Edit(task);

								// set_NextAlarm(db.tasks.Get(task._id),
								// mContext);
								set_NextAlarm(db.tasks.Get(task._id), mContext,
										model._id);

							}

							// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

						}
					}
					// /////////////////////
				} catch (Exception e) {
					// there doesnot exist any interval
					if (task.rep_value != null) {

						Long endDateTime = Long.valueOf(task.endDateTime);
						if (endDateTime <= Long.valueOf(task.startDateTime)) {
							// do nothing
							// cancel alarm
							mAlarmBroadcastReciever.cancelAlarm(mContext,
									alarm_id);
						} else if (endDateTime > Long
								.valueOf(task.startDateTime)) {

							weeklyAlarm_calculations(task, model);

						}

					}

				}
			} else if (task.rep_intervalType == 3) {
				// monthly

				if (task.rep_intervalExpiration != null) {

					try {
						Integer numOfEvents = Integer
								.valueOf(task.rep_intervalExpiration.toString());
						if (numOfEvents == 0) {
							// cancel alarm
							mAlarmBroadcastReciever.cancelAlarm(mContext,
									alarm_id);
						} else if (numOfEvents > 0 && numOfEvents <= 999) {
							// fine
							// keep assigning next alarmas
							task.rep_intervalExpiration = String
									.valueOf(numOfEvents - 1);
							db.tasks.Edit(task);
							set_NextAlarm(db.tasks.Get(task._id), mContext,
									model._id);
						}

					} catch (Exception e) {
						Log.e("is a longTime", e.getLocalizedMessage());
						Long untilADate = Long
								.valueOf(task.rep_intervalExpiration.toString());
						if (untilADate <= Long.valueOf(task.startDateTime)) {
							// cancel alarm
							mAlarmBroadcastReciever.cancelAlarm(mContext,
									alarm_id);
						} else {
							// fine
							// keep assigning next alarmas
							db.tasks.Edit(task);
							set_NextAlarm(db.tasks.Get(task._id), mContext,
									model._id);
						}
					}

				} else {
					Long endDateTime = Long.valueOf(task.endDateTime);
					if (endDateTime <= Long.valueOf(task.startDateTime)) {
						// do nothing
						// cancel alarm
						mAlarmBroadcastReciever.cancelAlarm(mContext, alarm_id);
					} else if (endDateTime > Long.valueOf(task.startDateTime)) {

						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(Long.valueOf(task.startDateTime));

						// INTERVAL_DAY
						// * c.getActualMaximum(Calendar.DAY_OF_MONTH)

						Long addTheseMonthsToExisting = (Long.valueOf(interval)
								* c.getActualMaximum(Calendar.DAY_OF_MONTH) * 86400000L);
						// month max in millis * 1 Day in millis

						task.startDateTime = String
								.valueOf((Long.valueOf(task.startDateTime) + addTheseMonthsToExisting));

						db.tasks.Edit(task);
						set_NextAlarm(db.tasks.Get(task._id), mContext,
								model._id);
					}
				}

			} else if (task.rep_intervalType == 4) {
				// yearly

				if (task.rep_intervalExpiration != null) {

					try {
						Integer numOfEvents = Integer
								.valueOf(task.rep_intervalExpiration.toString());
						if (numOfEvents == 0) {
							// cancel alarm
							mAlarmBroadcastReciever.cancelAlarm(mContext,
									alarm_id);
						} else if (numOfEvents > 0 && numOfEvents <= 999) {
							// fine
							// keep assigning next alarmas
							task.rep_intervalExpiration = String
									.valueOf(numOfEvents - 1);
							db.tasks.Edit(task);
							set_NextAlarm(db.tasks.Get(task._id), mContext,
									model._id);
						}

					} catch (Exception e) {
						Log.e("is a longTime", e.getLocalizedMessage());
						Long untilADate = Long
								.valueOf(task.rep_intervalExpiration.toString());
						if (untilADate <= Long.valueOf(task.startDateTime)) {
							// cancel alarm
							mAlarmBroadcastReciever.cancelAlarm(mContext,
									alarm_id);
						} else {
							// fine
							// keep assigning next alarmas
							db.tasks.Edit(task);
							set_NextAlarm(db.tasks.Get(task._id), mContext,
									model._id);
						}
					}
				} else {
					Long endDateTime = Long.valueOf(task.endDateTime);
					if (endDateTime <= Long.valueOf(task.startDateTime)) {
						// do nothing
						// cancel alarm
						mAlarmBroadcastReciever.cancelAlarm(mContext, alarm_id);
					} else if (endDateTime > Long.valueOf(task.startDateTime)) {

						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(Long.valueOf(task.startDateTime));

						// INTERVAL_DAY
						// * c.getActualMaximum(Calendar.DAY_OF_MONTH)

						GregorianCalendar gCal = new GregorianCalendar();

						Long numberOfDays = 0L;

						if (gCal.isLeapYear(c.get(Calendar.YEAR))) {
							numberOfDays = 366L;
						} else {
							numberOfDays = 365L;
						}

						Long addTheseYearsToExisting = (Long.valueOf(interval)
								* numberOfDays * 86400000L); // month max in
																// millis * 1
																// DAy in millis

						task.startDateTime = String
								.valueOf((Long.valueOf(task.startDateTime) + addTheseYearsToExisting));

						db.tasks.Edit(task);
						set_NextAlarm(db.tasks.Get(task._id), mContext,
								model._id);
					}
				}

			}
		} else if (task.rep_intervalType == 0) {
			// non repeating
			mAlarmBroadcastReciever.cancelAlarm(mContext, alarm_id);
		}
	}

	private void weeklyAlarm_calculations(TaskModel task,
			TaskNotificationsModel model) {
		Calendar calendar_DB = Calendar.getInstance();
		calendar_DB.setTimeInMillis(Long.valueOf(task.startDateTime));
		String abc = task.rep_value;
		String startTime = task.startDateTime;

		int occurrences = 0;// task.rep_valueString

		for (char c : task.rep_value.toString().toCharArray()) {
			if (c == ',') {
				occurrences++;
			}
		}
		StringTokenizer tokens = new StringTokenizer(task.rep_value.toString(),
				",");

		String day0 = null, day1 = null, day2 = null, day3 = null, day4 = null, day5 = null, day6 = null;
		ArrayList<Integer> wkDay_arrayList = new ArrayList<Integer>();

		if (occurrences == 1) {
			day0 = tokens.nextToken();
			wkDay_arrayList.add(Integer.valueOf(day0));
		} else if (occurrences == 2) {
			day0 = tokens.nextToken();
			day1 = tokens.nextToken();
			wkDay_arrayList.add(Integer.valueOf(day0));
			wkDay_arrayList.add(Integer.valueOf(day1));
		} else if (occurrences == 3) {
			day0 = tokens.nextToken();
			day1 = tokens.nextToken();
			day2 = tokens.nextToken();
			wkDay_arrayList.add(Integer.valueOf(day0));
			wkDay_arrayList.add(Integer.valueOf(day1));
			wkDay_arrayList.add(Integer.valueOf(day2));
		} else if (occurrences == 4) {
			day0 = tokens.nextToken();
			day1 = tokens.nextToken();
			day2 = tokens.nextToken();
			day3 = tokens.nextToken();
			wkDay_arrayList.add(Integer.valueOf(day0));
			wkDay_arrayList.add(Integer.valueOf(day1));
			wkDay_arrayList.add(Integer.valueOf(day2));
			wkDay_arrayList.add(Integer.valueOf(day3));
		} else if (occurrences == 5) {
			day0 = tokens.nextToken();
			day1 = tokens.nextToken();
			day2 = tokens.nextToken();
			day3 = tokens.nextToken();
			day4 = tokens.nextToken();
			wkDay_arrayList.add(Integer.valueOf(day0));
			wkDay_arrayList.add(Integer.valueOf(day1));
			wkDay_arrayList.add(Integer.valueOf(day2));
			wkDay_arrayList.add(Integer.valueOf(day3));
			wkDay_arrayList.add(Integer.valueOf(day4));
		} else if (occurrences == 6) {
			day0 = tokens.nextToken();
			day1 = tokens.nextToken();
			day2 = tokens.nextToken();
			day3 = tokens.nextToken();
			day4 = tokens.nextToken();
			day5 = tokens.nextToken();
			wkDay_arrayList.add(Integer.valueOf(day0));
			wkDay_arrayList.add(Integer.valueOf(day1));
			wkDay_arrayList.add(Integer.valueOf(day2));
			wkDay_arrayList.add(Integer.valueOf(day3));
			wkDay_arrayList.add(Integer.valueOf(day4));
			wkDay_arrayList.add(Integer.valueOf(day5));
		} else if (occurrences == 7) {
			day0 = tokens.nextToken();
			day1 = tokens.nextToken();
			day2 = tokens.nextToken();
			day3 = tokens.nextToken();
			day4 = tokens.nextToken();
			day5 = tokens.nextToken();
			day6 = tokens.nextToken();
			wkDay_arrayList.add(Integer.valueOf(day0));
			wkDay_arrayList.add(Integer.valueOf(day1));
			wkDay_arrayList.add(Integer.valueOf(day2));
			wkDay_arrayList.add(Integer.valueOf(day3));
			wkDay_arrayList.add(Integer.valueOf(day4));
			wkDay_arrayList.add(Integer.valueOf(day5));
			wkDay_arrayList.add(Integer.valueOf(day6));
		}

		Calendar currentCal = Calendar.getInstance();
		currentCal.setTimeInMillis(System.currentTimeMillis());
		int currentDay = currentCal.get(Calendar.DAY_OF_WEEK);

		for (int i = 0; i < wkDay_arrayList.size(); i++) {

			if (currentDay == 7) {

				int numberOfDaysToAdd = wkDay_arrayList.get(i) - currentDay;
				if (wkDay_arrayList.get(i) < currentDay) {
					numberOfDaysToAdd += 7;
				}

				calendar_DB.add(Calendar.DAY_OF_WEEK, numberOfDaysToAdd);

				Long newDateTime = calendar_DB.getTimeInMillis();

				i = wkDay_arrayList.size();

				task.startDateTime = String.valueOf(newDateTime);

				db.tasks.Edit(task);

				// set_NextAlarm(db.tasks.Get(task._id), mContext);
				set_NextAlarm(db.tasks.Get(task._id), mContext, model._id);
			}

			else if (currentDay < 7) {
				if (wkDay_arrayList.get(i) > currentDay) {

					// weekday arrayList Must store in order 1-7
					// not
					// 0-6

					// assign alarm
					// it will only assign the alarm that is
					// greater
					// then currentDay and break itSelf
					// we can also use >= condition depending on
					// the
					// situation it needs to be verified

					int numberOfDaysToAdd = wkDay_arrayList.get(i) - currentDay;

					calendar_DB.add(Calendar.DAY_OF_WEEK, numberOfDaysToAdd);

					Long newDateTime = calendar_DB.getTimeInMillis();

					i = wkDay_arrayList.size();

					task.startDateTime = String.valueOf(newDateTime);

					db.tasks.Edit(task);

					// set_NextAlarm(db.tasks.Get(task._id), mContext);
					set_NextAlarm(db.tasks.Get(task._id), mContext, model._id);
				} else if (wkDay_arrayList.get(i) <= currentDay) {
					// if user only selects oneday
					int numberOfDaysToAdd = wkDay_arrayList.get(i) - currentDay;
					if (wkDay_arrayList.get(i) < currentDay) {
						numberOfDaysToAdd += 7;
					}
					if (numberOfDaysToAdd == 0) {
						numberOfDaysToAdd += 7;
					}
					calendar_DB.add(Calendar.DAY_OF_WEEK, numberOfDaysToAdd);

					Long newDateTime = calendar_DB.getTimeInMillis();

					i = wkDay_arrayList.size();

					task.startDateTime = String.valueOf(newDateTime);

					db.tasks.Edit(task);

					// set_NextAlarm(db.tasks.Get(task._id), mContext);
					set_NextAlarm(db.tasks.Get(task._id), mContext, model._id);
				}
			}
		}
	}

	private void set_NextAlarm(TaskModel task, Context mContext, int alarmID) {
		mAlarmBroadcastReciever.setNextAlarm(task, mContext, alarmID);
	}
}