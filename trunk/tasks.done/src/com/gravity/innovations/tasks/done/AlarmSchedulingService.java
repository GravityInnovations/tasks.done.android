package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmSchedulingService extends IntentService {
	DatabaseHelper db;
	Context mContext;

	public AlarmSchedulingService() {
		super("SchedulingService");
	}

	public int NOTIFICATION_ID;
	private NotificationManager mNotificationManager;
	private AlarmBroadcastReciever mAlarmBroadcastReciever = new AlarmBroadcastReciever();
	// NotificationCompat.Builder builder;
	// Bundle extras;

	private static final String PREF_TASKLIST_ID_PRESSED = "tasklist_pressed_id";
	private static final String PREF_TASK_ID_FOR_TASKLIST_PRESSED = "task_id";

	private Boolean isSat = false, isSun = false, isMon = false, isTue = false,
			isWed = false, isThu = false, isFri = false;

	@SuppressLint("NewApi")
	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			// Get passed values
			Bundle extras = intent.getExtras();
			// int uniqueID = extras.getInt("alarm_id");

			String uniqueIDDsS = extras.getString("alarmMgr_id");
			NOTIFICATION_ID = Integer.parseInt(uniqueIDDsS);// alarmMgr_id,
															// alarmID;
			sendNotification(Integer.parseInt(uniqueIDDsS));
			// Release the wake lock provided by the BroadcastReceiver.
			AlarmBroadcastReciever.completeWakefulIntent(intent);
		} catch (Exception e) {
			Log.e("AlarmShedullingSerivce:onHandleIntetnt",
					e.getLocalizedMessage());
		}
	}

	private void weeklyAlarm_calculations(TaskModel task) {
		Calendar calendar_DB = Calendar.getInstance();
		calendar_DB.setTimeInMillis(Long.valueOf(task.rep_startDateTime));
		String abc = task.rep_value;
		String startTime = task.rep_startDateTime;

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

				task.rep_startDateTime = String.valueOf(newDateTime);

				db.tasks.Edit(task);

				set_NextAlarm(db.tasks.Get(task._id), mContext);

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

					task.rep_startDateTime = String.valueOf(newDateTime);

					db.tasks.Edit(task);

					set_NextAlarm(db.tasks.Get(task._id), mContext);
				}
				else if (wkDay_arrayList.get(i) <= currentDay) {
					//if user only selects oneday
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

					task.rep_startDateTime = String.valueOf(newDateTime);

					db.tasks.Edit(task);

					set_NextAlarm(db.tasks.Get(task._id), mContext);
				}
			}
		}
	}

	// Post a notification
	public void sendNotification(int alarm_id) {
		mContext = getApplicationContext();

		db = new DatabaseHelper(mContext);

		TaskNotificationsModel model = db.notification.Get(alarm_id);
		TaskModel task = db.tasks.Get(model.fk_task_id);
		String alarmTitle = task.title;
		String alarmMessage = task.details + "Notification ID: " + model._id
				+ " Interval: " + model.interval + "interval Type is: "
				+ model.interval_type;

		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		int id = task.fk_tasklist_id;

		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("_task_list_id", task.fk_tasklist_id);
		intent.putExtra("_task_id", task._id);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, /* PendingIntent.FLAG_UPDATE_CURRENT| */
				Intent.FILL_IN_DATA | Intent.FLAG_ACTIVITY_NEW_TASK);
		// | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		// use the right class it should be called from the where alarms are set
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(alarmTitle)
				.setStyle(
						new NotificationCompat.BigTextStyle()
								.bigText(alarmMessage))

				.setDefaults(
						Notification.DEFAULT_SOUND
								| Notification.DEFAULT_LIGHTS
								| Notification.FLAG_ONGOING_EVENT
								| Notification.FLAG_FOREGROUND_SERVICE)

				.setContentText(alarmMessage);
		// mBuilder.addAction(R.drawable.ic_action_cancel, "Dismiss",
		// contentIntent);
		// mBuilder.addAction(R.drawable.ic_action_accept, "Done",
		// contentIntent);
		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		if (task.rep_intervalType != 0) {
			// is Repeating type Alarm
			int interval = task.rep_interval;
			if (task.rep_intervalType == 1) {
				// daily
				Long addTheseDaysToExisting = (Long.valueOf(interval) * 86400000L);
				// 1 day in millis = 86400000L
				task.rep_startDateTime = String
						.valueOf((Long.valueOf(task.rep_startDateTime) + addTheseDaysToExisting));
				if (!task.rep_intervalExpiration.toString().isEmpty()
						|| task.rep_intervalExpiration.toString() != null) {
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
							set_NextAlarm(db.tasks.Get(task._id), mContext);
						}

					} catch (Exception e) {
						Log.e("is a longTime", e.getLocalizedMessage());
						Long untilADate = Long
								.valueOf(task.rep_intervalExpiration.toString());
						if (untilADate <= Long.valueOf(task.rep_startDateTime)) {
							// cancel alarm
							mAlarmBroadcastReciever.cancelAlarm(mContext,
									alarm_id);
						} else {
							// fine
							// keep assigning next alarmas
							db.tasks.Edit(task);
							set_NextAlarm(db.tasks.Get(task._id), mContext);
						}
					}
				} else {
					Long endDateTime = Long.valueOf(task.rep_endDateTime);
					if (endDateTime <= Long.valueOf(task.rep_startDateTime)) {
						// do nothing
						// cancel alarm
						mAlarmBroadcastReciever.cancelAlarm(mContext, alarm_id);
					} else if (endDateTime > Long
							.valueOf(task.rep_startDateTime)) {
						db.tasks.Edit(task);
						set_NextAlarm(db.tasks.Get(task._id), mContext);
					}
				}

			} else if (task.rep_intervalType == 2) {
				// weekly
				try {
					// if interval_Expire does Exist
					if (task.rep_intervalExpiration.toString() != null) {
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
									weeklyAlarm_calculations(task);
								}
							}
						} catch (Exception e) {
							// It is a time based
							Log.e("is a longTime", e.getLocalizedMessage());
							Long untilADate = Long
									.valueOf(task.rep_intervalExpiration
											.toString());
							if (untilADate <= Long
									.valueOf(task.rep_startDateTime)) {
								// cancel alarm
								mAlarmBroadcastReciever.cancelAlarm(mContext,
										alarm_id);
							} else {
								// keep assigning next alarmas
								if (!task.rep_value.toString().isEmpty()
										|| task.rep_value.toString() != null) {
									weeklyAlarm_calculations(task);
								}
							}
						}
					}
				} catch (Exception e) {
					// there doesnot exist any interval
					if (!task.rep_value.toString().isEmpty()
							|| task.rep_value.toString() != null) {

						Long endDateTime = Long.valueOf(task.rep_endDateTime);
						if (endDateTime <= Long.valueOf(task.rep_startDateTime)) {
							// do nothing
							// cancel alarm
							mAlarmBroadcastReciever.cancelAlarm(mContext,
									alarm_id);
						} else if (endDateTime > Long
								.valueOf(task.rep_startDateTime)) {

							weeklyAlarm_calculations(task);

						}

					}

				}

				/***********************************************************/
				// // weekly
				// String rep_expirr = task.rep_intervalExpiration.toString();
				// // put this thing in try catch
				// if (rep_expirr.isEmpty()) {
				// try {
				// Integer numOfEvents = Integer
				// .valueOf(task.rep_intervalExpiration.toString());
				// if (numOfEvents == 0) {
				//
				// // cancel alarm
				// mAlarmBroadcastReciever.cancelAlarm(mContext,
				// alarm_id);
				//
				// } else if (numOfEvents > 0 && numOfEvents <= 999) {
				// // fine
				// // keep assigning next alarmas
				//
				// task.rep_intervalExpiration = String
				// .valueOf(numOfEvents - 1);
				// // db.tasks.Edit(task);
				// // set_NextAlarm(db.tasks.Get(task._id), mContext);
				//
				// // //////
				// if (!task.rep_value.toString().isEmpty()
				// || task.rep_value.toString() != null) {
				//
				// weeklyAlarm_calculations(task);
				//
				// }
				// // //////////
				//
				// }
				//
				// } catch (Exception e) {
				// Log.e("is a longTime", e.getLocalizedMessage());
				//
				// Long untilADate = Long
				// .valueOf(task.rep_intervalExpiration.toString());
				// if (untilADate <= Long.valueOf(task.rep_startDateTime)) {
				// // cancel alarm
				// mAlarmBroadcastReciever.cancelAlarm(mContext,
				// alarm_id);
				// } else {
				// // fine
				// // keep assigning next alarmas
				//
				// // db.tasks.Edit(task);
				// // set_NextAlarm(db.tasks.Get(task._id), mContext);
				//
				// // //////
				// if (!task.rep_value.toString().isEmpty()
				// || task.rep_value.toString() != null) {
				//
				// weeklyAlarm_calculations(task);
				//
				// }
				// // //////////
				//
				// }
				// }
				// }
				//
				// else {
				// // //////
				// if (!task.rep_value.toString().isEmpty()
				// || task.rep_value.toString() != null) {
				//
				// weeklyAlarm_calculations(task);
				//
				// }
				// // //////////
				// }
				/***********************************************************/
			} else if (task.rep_intervalType == 3) {
				// monthly

				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(Long.valueOf(task.rep_startDateTime));

				// INTERVAL_DAY
				// * c.getActualMaximum(Calendar.DAY_OF_MONTH)

				Long addTheseMonthsToExisting = (Long.valueOf(interval)
						* c.getActualMaximum(Calendar.DAY_OF_MONTH) * 86400000L);
				// month max in millis * 1 Day in millis

				task.rep_startDateTime = String
						.valueOf((Long.valueOf(task.rep_startDateTime) + addTheseMonthsToExisting));

				if (!task.rep_intervalExpiration.toString().isEmpty()
						|| task.rep_intervalExpiration.toString() != null) {

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
							set_NextAlarm(db.tasks.Get(task._id), mContext);
						}

					} catch (Exception e) {
						Log.e("is a longTime", e.getLocalizedMessage());
						Long untilADate = Long
								.valueOf(task.rep_intervalExpiration.toString());
						if (untilADate <= Long.valueOf(task.rep_startDateTime)) {
							// cancel alarm
							mAlarmBroadcastReciever.cancelAlarm(mContext,
									alarm_id);
						} else {
							// fine
							// keep assigning next alarmas
							db.tasks.Edit(task);
							set_NextAlarm(db.tasks.Get(task._id), mContext);
						}
					}

				} else {
					Long endDateTime = Long.valueOf(task.rep_endDateTime);
					if (endDateTime <= Long.valueOf(task.rep_startDateTime)) {
						// do nothing
						// cancel alarm
						mAlarmBroadcastReciever.cancelAlarm(mContext, alarm_id);
					} else if (endDateTime > Long
							.valueOf(task.rep_startDateTime)) {
						db.tasks.Edit(task);
						set_NextAlarm(db.tasks.Get(task._id), mContext);
					}
				}

			} else if (task.rep_intervalType == 4) {
				// yearly
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(Long.valueOf(task.rep_startDateTime));

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
						* numberOfDays * 86400000L); // month max in millis * 1
														// DAy in millis

				task.rep_startDateTime = String
						.valueOf((Long.valueOf(task.rep_startDateTime) + addTheseYearsToExisting));

				if (!task.rep_intervalExpiration.toString().isEmpty()
						|| task.rep_intervalExpiration.toString() != null) {

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
							set_NextAlarm(db.tasks.Get(task._id), mContext);
						}

					} catch (Exception e) {
						Log.e("is a longTime", e.getLocalizedMessage());
						Long untilADate = Long
								.valueOf(task.rep_intervalExpiration.toString());
						if (untilADate <= Long.valueOf(task.rep_startDateTime)) {
							// cancel alarm
							mAlarmBroadcastReciever.cancelAlarm(mContext,
									alarm_id);
						} else {
							// fine
							// keep assigning next alarmas
							db.tasks.Edit(task);
							set_NextAlarm(db.tasks.Get(task._id), mContext);
						}
					}
				} else {
					Long endDateTime = Long.valueOf(task.rep_endDateTime);
					if (endDateTime <= Long.valueOf(task.rep_startDateTime)) {
						// do nothing
						// cancel alarm
						mAlarmBroadcastReciever.cancelAlarm(mContext, alarm_id);
					} else if (endDateTime > Long
							.valueOf(task.rep_startDateTime)) {
						db.tasks.Edit(task);
						set_NextAlarm(db.tasks.Get(task._id), mContext);
					}
				}

			}

		}
	}

	private void resetBooleans() {
		isMon = false;
		isTue = false;
		isWed = false;
		isThu = false;
		isFri = false;
		isSun = false;
		isSat = false;
	}

	private void set_NextAlarm(TaskModel task, Context mContext) {
		try {
			mAlarmBroadcastReciever.setAlarm(task, mContext);
		} catch (Exception e) {
			Log.e("set Next Alarm", e.getLocalizedMessage());
		}

		// compare your time and notification calculation here
		// task.notifications.
		// setAlarm

		// ArrayList<TaskNotificationsModel> models = //=
		// db.notification.Get(alarm_id);
		// TaskModel task = db.tasks.Get(alarm_id);// .Task_Single(alarm_id);
	}

}
