package com.gravity.innovations.tasks.done;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TaskActivity extends ActionBarActivity implements OnClickListener {

	private ActionBar mActionBar;
	private TaskListModel list;
	private TaskModel task;
	private EditText title;
	private EditText details;
	private EditText notes;
	private View isAllDay;
	private OnCheckedChangeListener allDay_change_listener;
	private OnClickListener editNotificationlistener, addNotificationListener;
	private TextView time_start;
	private TextView time_end;
	private TextView date_start;
	private TextView date_end;
	private Activity mActivity;
	private Calendar cal_startdate;
	private Calendar cal_enddate;
	private Calendar cal_starttime;
	private Calendar cal_endtime;
	private Calendar cal_repeatdate;
	private Calendar mTime;

	private Boolean isSat = false, isSun = false, isMon = false, isTue = false,
			isWed = false, isThu = false, isFri = false, isDaily = false,
			isWeekly = false, isMonthly = false, isYearly = false,
			isForever = false, isUntilADate = false, isForFixedEvents = false;
	private TextView tv_repeat, tv_notification0, tv_notification_addAnother,
			tv_notification1, tv_notification2, tv_notification3;
	private int textViewFlag = 5;
	private int textViewTag = 1;
	private ArrayList<TextView> arrayList_TextView = new ArrayList<TextView>();

	// private ArrayList<TaskNotificationsModel> task.notifications = new
	// ArrayList<TaskNotificationsModel>();// 0-4

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.bottom_first_from_top,
				R.anim.bottom_first_from_top_continue);
		Intent intent = new Intent(this, MainActivity.class);
		setResult(RESULT_CANCELED, intent);// >>>>>>>>>>>>????
		finish();
		// startActivity(intent);
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// decide what to show in the action bar.
		getMenuInflater().inflate(R.menu.save, menu);
		restoreActionBar();
		return super.onCreateOptionsMenu(menu);
	}

	public void restoreActionBar() {
		mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		mActionBar.setDisplayShowTitleEnabled(true);
		if (list != null)
			mActionBar.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor(list.fragmentColor)));
		mActionBar.setTitle("Task Details");// //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>???
		mActionBar.show();
		/***********/
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_save) {
			Intent intent = new Intent(this, MainActivity.class);
			Serializable s = (Serializable) list;
			intent.putExtra("key_list", s);
			Serializable t = (Serializable) task;
			intent.putExtra("key_task", t);
			setResult(/* 12345 */RESULT_OK, intent);// >>>>>>>>>>>>????
			this.finish();
		}
		overridePendingTransition(R.anim.bottom_first_from_top,
				R.anim.bottom_first_from_top_continue);
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.top_first_from_bottom,
				R.anim.top_first_from_bottom_continue);
		setContentView(R.layout.activity_task);
		Intent intent = getIntent();

		Bundle mBundle = intent.getExtras();
		if (mBundle != null) {
			list = (TaskListModel) intent.getSerializableExtra("key_list");
			task = (TaskModel) intent.getSerializableExtra("key_task");
		}
		try {
			Calendar cal_init = Calendar.getInstance();
			cal_startdate = Calendar.getInstance();
			cal_startdate.set(0, 0, 0, 0, 0, 0);
			cal_enddate = Calendar.getInstance();
			cal_enddate.set(0, 0, 0, 0, 0, 0);
			cal_starttime = Calendar.getInstance();
			cal_starttime.set(0, 0, 0, 0, 0, 0);
			cal_endtime = Calendar.getInstance();
			cal_endtime.set(0, 0, 0, 0, 0, 0);
			cal_repeatdate = Calendar.getInstance();
			cal_repeatdate.set(0, 0, 0, 0, 0, 0);
			title = (EditText) findViewById(R.id.txt_title);
			details = (EditText) findViewById(R.id.txt_details);
			notes = (EditText) findViewById(R.id.txt_notes);
			time_start = (TextView) findViewById(R.id.btn_time_1);
			date_end = (TextView) findViewById(R.id.btn_date_2);
			time_end = (TextView) findViewById(R.id.btn_time_2);
			date_start = (TextView) findViewById(R.id.btn_date_1);

			// cal_init = setCalender(cal_init);
			// <<<<<<<<<<<<<<<<<<<<watch out this section one more time
			// if (task._id == -1) {
			// cal_startdate = Common.datetimeHelper
			// .setCalender_CurrentDate(cal_startdate);
			// cal_enddate = Common.datetimeHelper
			// .setCalender_CurrentDate(cal_enddate);
			// date_start
			// .setText(Common.datetimeHelper.getDate(cal_startdate));
			// date_end.setText(Common.datetimeHelper.getDate(cal_enddate));
			// // if (task.rep_allDay == 0) {
			// cal_starttime = Common.datetimeHelper
			// .setCalender_CurrentTime(cal_starttime);
			// cal_endtime = Common.datetimeHelper
			// .setCalender_CurrentTimePlusOneHour(cal_endtime);
			// time_start
			// .setText(Common.datetimeHelper.getTime(cal_starttime));
			// time_end.setText(Common.datetimeHelper.getTime(cal_endtime));
			//
			// Calendar start_datetime = Common.datetimeHelper.mergeCalendars(
			// cal_startdate, cal_starttime);
			// Calendar end_datetime = Common.datetimeHelper.mergeCalendars(
			// cal_enddate, cal_endtime);
			// task.rep_startDateTime = Common.datetimeHelper
			// .getDateInMs(start_datetime);
			// task.rep_endDateTime = Common.datetimeHelper
			// .getDateInMs(end_datetime);
			// } // <<<<<<<<<<<<<<<<<<<<watch out this section one more time

			title.setText(task.title);
			details.setText(task.details);
			notes.setText(task.notes);

			title.addTextChangedListener(mTextWatcher);
			details.addTextChangedListener(mTextWatcher);
			notes.addTextChangedListener(mTextWatcher);

			tv_repeat = (TextView) findViewById(R.id.repeat_tv);
			// ///////////////////////////
			tv_notification0 = (TextView) findViewById(R.id.add_notification_btn);
			tv_notification0.setTag(0);
			// tags for identification of textViews
			tv_notification_addAnother = (TextView) findViewById(R.id.add_another_notification_btn);
			tv_notification_addAnother.setTag(4);
			editNotificationlistener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					int tag = ((Integer) v.getTag());
					try {
						TaskNotificationsModel model = task.notifications
								.get(tag);
						listDialog_notifications(tag, model);

					} catch (Exception e) {
						TaskNotificationsModel model = new TaskNotificationsModel();
						task.notifications.add(model);
						listDialog_notifications(tag, model);

					}

					// Integer.parseInt((String)// v.getTag());
					// listDialog(((TextView) v) tag, model);
				}
			};
			arrayList_TextView = new ArrayList<TextView>();
			arrayList_TextView.add(tv_notification0);
			if (task._id == -1) {
				task.notifications.add(new TaskNotificationsModel());
				// place zeroth <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			}
			arrayList_TextView.add(tv_notification_addAnother);
			addNotificationListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (arrayList_TextView.size() < 5) {
						final TextView newTextView = new TextView(
								getApplicationContext());
						addANewTextView(newTextView);
						arrayList_TextView.add(newTextView);
					}
					if (arrayList_TextView.size() == 5 && textViewFlag == 5) {
						tv_notification_addAnother.setText("Notification Time");
						tv_notification_addAnother.setOnClickListener(null);
						tv_notification_addAnother
								.setOnClickListener(editNotificationlistener);
						task.notifications.add(new TaskNotificationsModel());
						// place fourth <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
						textViewFlag++;
					}
				}
			};
			tv_notification_addAnother
					.setOnClickListener(addNotificationListener);
			tv_notification0.setOnClickListener(editNotificationlistener);
			// /////////////////////
			tv_repeat.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listDialog_repeat();

				}
			});

			// allDay_change_listener = new
			// CompoundButton.OnCheckedChangeListener() {
			// @Override
			// public void onCheckedChanged(CompoundButton buttonView,
			// boolean isChecked) {
			// // TODO Auto-generated method stub
			// if (isChecked) {
			// time_start.setVisibility(View.GONE);
			// time_end.setVisibility(View.GONE);
			// task.rep_allDay = 1;
			// if (task._id == -1) {
			// Calendar new_cal_endtime = Calendar.getInstance();
			// Calendar new_cal_starttime = Calendar.getInstance();
			// new_cal_endtime.set(0, 0, 0, 0, 0);
			// new_cal_starttime.set(0, 0, 0, 0, 0);
			// Calendar start_datetime = Common.datetimeHelper
			// .mergeCalendars(cal_startdate,
			// new_cal_starttime);
			// Calendar end_datetime = Common.datetimeHelper
			// .mergeCalendars(cal_enddate,
			// new_cal_endtime);
			// task.rep_startDateTime = Common.datetimeHelper
			// .getDateInMs(start_datetime);
			// task.rep_endDateTime = Common.datetimeHelper
			// .getDateInMs(end_datetime);
			// } else if (task._id != -1 && task.rep_allDay == 1) {
			// Calendar newCalendar = Calendar.getInstance();
			//
			// newCalendar.setTimeInMillis(Long
			// .valueOf(task.rep_startDateTime));
			//
			// cal_startdate.setTimeInMillis(Common.datetimeHelper
			// .calendar_ReturnDate(newCalendar));
			//
			// date_start.setText(Common.datetimeHelper
			// .getDate(cal_startdate));
			//
			// cal_starttime.setTimeInMillis(Common.datetimeHelper
			// .calendar_ReturnTime(newCalendar));
			//
			// time_start.setText(Common.datetimeHelper
			// .getTime(cal_starttime));
			//
			// newCalendar = Calendar.getInstance();
			//
			// newCalendar.setTimeInMillis(Long
			// .valueOf(task.rep_endDateTime));
			//
			// cal_enddate.setTimeInMillis(Common.datetimeHelper
			// .calendar_ReturnDate(newCalendar));
			// date_end.setText(Common.datetimeHelper
			// .getDate(cal_enddate));
			//
			// cal_endtime.setTimeInMillis(Common.datetimeHelper
			// .calendar_ReturnTime(newCalendar));
			// time_end.setText(Common.datetimeHelper
			// .getTime(cal_endtime));
			//
			// Calendar start_datetime = Common.datetimeHelper
			// .mergeCalendars(cal_startdate,
			// cal_starttime);
			// Calendar end_datetime = Common.datetimeHelper
			// .mergeCalendars(cal_enddate, cal_endtime);
			// task.rep_startDateTime = (Common.datetimeHelper
			// .getDateInMs(start_datetime));
			// task.rep_endDateTime = (Common.datetimeHelper
			// .getDateInMs(end_datetime));
			//
			// }
			//
			// } else { // UN-CHECKED
			// time_start.setVisibility(View.VISIBLE);
			// time_end.setVisibility(View.VISIBLE);
			// task.rep_allDay = 0;
			// // end and start time to init cal
			// if (task._id == -1) {
			// Calendar start_datetime = Common.datetimeHelper
			// .mergeCalendars(cal_startdate,
			// cal_starttime);
			// Calendar end_datetime = Common.datetimeHelper
			// .mergeCalendars(cal_enddate, cal_endtime);
			// task.rep_startDateTime = Common.datetimeHelper
			// .getDateInMs(start_datetime);
			// task.rep_endDateTime = Common.datetimeHelper
			// .getDateInMs(end_datetime);
			// } else if (task._id != -1 && task.rep_allDay == 0) {
			//
			// cal_startdate.set(0, 0, 0, 0, 0);
			// cal_enddate.set(0, 0, 0, 0, 0);
			//
			// cal_startdate.setTimeInMillis(Long
			// .valueOf(task.rep_startDateTime));
			//
			// cal_enddate.setTimeInMillis(Long
			// .valueOf(task.rep_endDateTime));
			//
			// date_start.setText(Common.datetimeHelper
			// .getDate(cal_startdate));
			// date_end.setText(Common.datetimeHelper
			// .getDate(cal_enddate));
			//
			// cal_starttime.set(0, 0, 0, 0, 0);
			// cal_endtime.set(0, 0, 0, 0, 0);
			//
			// cal_starttime.setTimeInMillis(Long
			// .valueOf(task.rep_startDateTime));
			//
			// cal_endtime.setTimeInMillis(Long
			// .valueOf(task.rep_endDateTime));
			//
			// time_start.setText(Common.datetimeHelper
			// .getTime(cal_starttime));
			// time_end.setText(Common.datetimeHelper
			// .getTime(cal_endtime));
			//
			// Calendar start_datetime = Common.datetimeHelper
			// .mergeCalendars(cal_startdate,
			// cal_starttime);
			// Calendar end_datetime = Common.datetimeHelper
			// .mergeCalendars(cal_enddate, cal_endtime);
			// task.rep_startDateTime = (Common.datetimeHelper
			// .getDateInMs(start_datetime));
			// task.rep_endDateTime = (Common.datetimeHelper
			// .getDateInMs(end_datetime));
			//
			// }
			//
			// }
			// if (arrayList_TextView.size() > 2) {
			// if (task._id == -1) {
			// try {
			// resetNotificaionTextViews();
			// } catch (Exception e) {
			// Log.e("allDaySwitch", e.getLocalizedMessage());
			// }
			// }
			// }
			// }
			// };

			allDay_change_listener = new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {// isAllDay

						time_start.setVisibility(View.GONE);
						time_end.setVisibility(View.GONE);
						task.rep_allDay = 1;

						if (task._id == -1) {
							Calendar new_cal_endtime = Calendar.getInstance();
							Calendar new_cal_starttime = Calendar.getInstance();
							
							new_cal_endtime.set(0, 0, 0, 0, 0, 0);
							new_cal_starttime.set(0, 0, 0, 0, 0, 0);
							
							Calendar start_datetime = Common.datetimeHelper
									.mergeCalendars(cal_startdate,
											new_cal_starttime);
							
							Calendar end_datetime = Common.datetimeHelper
									.mergeCalendars(cal_enddate,
											new_cal_endtime);
							task.rep_startDateTime = Common.datetimeHelper
									.getDateInMs(start_datetime);
							task.rep_endDateTime = Common.datetimeHelper
									.getDateInMs(end_datetime);

						} else if (task._id != -1) {

							Calendar newCalendar = Calendar.getInstance();
							newCalendar.setTimeInMillis(Long
									.valueOf(task.rep_startDateTime));
							cal_startdate.setTimeInMillis(Common.datetimeHelper
									.calendar_ReturnDate(newCalendar));
							date_start.setText(Common.datetimeHelper
									.getDate(cal_startdate));
							cal_starttime.setTimeInMillis(Common.datetimeHelper
									.calendar_ReturnTime(newCalendar));
							time_start.setText(Common.datetimeHelper
									.getTime(cal_starttime));

							newCalendar = Calendar.getInstance();
							newCalendar.setTimeInMillis(Long
									.valueOf(task.rep_endDateTime));
							cal_enddate.setTimeInMillis(Common.datetimeHelper
									.calendar_ReturnDate(newCalendar));
							date_end.setText(Common.datetimeHelper
									.getDate(cal_enddate));
							cal_endtime.setTimeInMillis(Common.datetimeHelper
									.calendar_ReturnTime(newCalendar));
							time_end.setText(Common.datetimeHelper
									.getTime(cal_endtime));

							Calendar start_datetime = Common.datetimeHelper
									.mergeCalendars(cal_startdate,
											cal_starttime);
							Calendar end_datetime = Common.datetimeHelper
									.mergeCalendars(cal_enddate, cal_endtime);
							task.rep_startDateTime = (Common.datetimeHelper
									.getDateInMs(start_datetime));
							task.rep_endDateTime = (Common.datetimeHelper
									.getDateInMs(end_datetime));

						}

					} else {// isNotAllDay

						time_start.setVisibility(View.VISIBLE);
						time_end.setVisibility(View.VISIBLE);
						task.rep_allDay = 0;

						if (task._id == -1) {
							cal_startdate = Common.datetimeHelper
									.setCalender_CurrentDate(cal_startdate);
							cal_enddate = Common.datetimeHelper
									.setCalender_CurrentDate(cal_enddate);
							date_start.setText(Common.datetimeHelper
									.getDate(cal_startdate));
							date_end.setText(Common.datetimeHelper
									.getDate(cal_enddate));
							cal_starttime = Common.datetimeHelper
									.setCalender_CurrentTime(cal_starttime);
							cal_endtime = Common.datetimeHelper
									.setCalender_CurrentTimePlusOneHour(cal_endtime);
							time_start.setText(Common.datetimeHelper
									.getTime(cal_starttime));
							time_end.setText(Common.datetimeHelper
									.getTime(cal_endtime));
							Calendar start_datetime = Common.datetimeHelper
									.mergeCalendars(cal_startdate,
											cal_starttime);
							Calendar end_datetime = Common.datetimeHelper
									.mergeCalendars(cal_enddate, cal_endtime);
							task.rep_startDateTime = Common.datetimeHelper
									.getDateInMs(start_datetime);
							task.rep_endDateTime = Common.datetimeHelper
									.getDateInMs(end_datetime);
						} else if (task._id != -1) {

							// cal_startdate.set(0, 0, 0, 0, 0);
							// cal_enddate.set(0, 0, 0, 0, 0);
							cal_startdate.setTimeInMillis(Long
									.valueOf(task.rep_startDateTime));
							cal_enddate.setTimeInMillis(Long
									.valueOf(task.rep_endDateTime));
							date_start.setText(Common.datetimeHelper
									.getDate(cal_startdate));
							date_end.setText(Common.datetimeHelper
									.getDate(cal_enddate));

							cal_starttime.set(0, 0, 0, 0, 0);
							cal_endtime.set(0, 0, 0, 0, 0);
							cal_starttime.setTimeInMillis(Long
									.valueOf(task.rep_startDateTime));
							cal_endtime.setTimeInMillis(Long
									.valueOf(task.rep_endDateTime));
							time_start.setText(Common.datetimeHelper
									.getTime(cal_starttime));
							time_end.setText(Common.datetimeHelper
									.getTime(cal_endtime));

							Calendar start_datetime = Common.datetimeHelper
									.mergeCalendars(cal_startdate,
											cal_starttime);
							Calendar end_datetime = Common.datetimeHelper
									.mergeCalendars(cal_enddate, cal_endtime);
							task.rep_startDateTime = (Common.datetimeHelper
									.getDateInMs(start_datetime));
							task.rep_endDateTime = (Common.datetimeHelper
									.getDateInMs(end_datetime));

						}

					}// else isNotAllDay

					if (arrayList_TextView.size() > 2) {
						if (task._id == -1) {
							try {
								resetNotificaionTextViews();
							} catch (Exception e) {
								Log.e("allDaySwitch", e.getLocalizedMessage());
							}
						}
					}
				}
			};

			try {
				isAllDay = (Switch) findViewById(R.id.onOFF);
				((Switch) isAllDay).setText("All Day");

				((Switch) isAllDay)
						.setOnCheckedChangeListener(allDay_change_listener);
				if (task._id == -1) {
					((Switch) isAllDay).setChecked(true);
					((Switch) isAllDay).setChecked(false);
					// put it to position first time just
					// so the listener works properly
				}
				if (task._id != -1) {

					if (task.rep_allDay == 1) {
						// ((Switch) isAllDay).setChecked(false);
						((Switch) isAllDay).setChecked(true);
					} else if (task.rep_allDay == 0) {
						((Switch) isAllDay).setChecked(true);
						((Switch) isAllDay).setChecked(false);
					}
				}
			} catch (Exception e) {
				isAllDay = (CheckBox) findViewById(R.id.onOFF);
				((CheckBox) isAllDay)
						.setOnCheckedChangeListener(allDay_change_listener);
				((TextView) findViewById(R.id.txt_onOFF)).setText("All Day");

				if (task._id != -1) {

					if (task.rep_allDay == 1) {
						((CheckBox) isAllDay).setChecked(true);
					} else if (task.rep_allDay == 0) {
						((CheckBox) isAllDay).setChecked(false);
					}
				}
			}

			mActivity = this;
			// OnClickListener dateListener = new OnClickListener() {
			// View calendar_dialog_view = null;
			//
			// @Override
			// public void onClick(final View v) {
			// // TODO Auto-generated method stub
			// calendar_dialog_view = mActivity.getLayoutInflater()
			// .inflate(R.layout.calendar_view, null);
			// DialogInterface.OnClickListener posListener = new
			// DialogInterface.OnClickListener() {
			// DatePicker mDatePicker = null;
			// // TimePicker mTimePicker = null;
			// CalendarView mCalendarView = null;
			//
			// // Calendar cal = Calendar.getInstance();
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// // TODO Auto-generated method stub
			// String tag = "";
			// try {
			// mDatePicker = (DatePicker) calendar_dialog_view
			// .findViewById(R.id.datepicker);
			// // mTimePicker = (TimePicker) time_dialog_view
			// // .findViewById(R.id.timepicker);
			// tag = (String) v.getTag();
			// if (tag.equals("start")) {
			// cal_startdate.set(mDatePicker.getYear(),
			// mDatePicker.getMonth() + 1,
			// mDatePicker.getDayOfMonth());
			// date_start.setText(Common.datetimeHelper
			// .getDate(cal_startdate));
			// } else if (tag.equals("end")) {
			// cal_enddate.set(mDatePicker.getYear(),
			// mDatePicker.getMonth() + 1,
			// mDatePicker.getDayOfMonth());
			// date_end.setText(Common.datetimeHelper
			// .getDate(cal_enddate));
			// }
			//
			// } catch (Exception e) {
			// try {
			// mCalendarView = (CalendarView) calendar_dialog_view
			// .findViewById(R.id.calenderView);
			// if (tag.equals("start")) {
			// cal_startdate
			// .setTimeInMillis(mCalendarView
			// .getDate());
			// date_start
			// .setText(Common.datetimeHelper
			// .getDate(cal_startdate));
			// } else if (tag.equals("end")) {
			// cal_enddate
			// .setTimeInMillis(mCalendarView
			// .getDate());
			// date_end.setText(Common.datetimeHelper
			// .getDate(cal_enddate));
			// }
			//
			// } catch (Exception ex) {
			// // tell this to user <unsupported>
			// }
			// }
			//
			// Calendar start_datetime = Common.datetimeHelper
			// .mergeCalendars(cal_startdate,
			// cal_starttime);
			// Calendar end_datetime = Common.datetimeHelper
			// .mergeCalendars(cal_enddate, cal_endtime);
			// task.rep_startDateTime = Common.datetimeHelper
			// .getDateInMs(start_datetime);
			// task.rep_endDateTime = Common.datetimeHelper
			// .getDateInMs(end_datetime);
			//
			// }
			// };
			// Common.CustomDialog.CustomDialog(mActivity,
			// calendar_dialog_view, posListener, R.string.done);// *
			// }
			// };
			// date_start.setOnClickListener(dateListener);
			// date_end.setOnClickListener(dateListener);
			//
			// OnClickListener timeListener = new OnClickListener() {
			// View time_dialog_view = null;
			//
			// @Override
			// public void onClick(final View v) {
			// // TODO Auto-generated method stub
			// time_dialog_view = mActivity.getLayoutInflater().inflate(
			// R.layout.time_view, null);
			//
			// DialogInterface.OnClickListener posListener = new
			// DialogInterface.OnClickListener() {
			// TimePicker mTimePicker = null;
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// // TODO Auto-generated method stub
			// String tag = "";
			// try {
			// mTimePicker = (TimePicker) time_dialog_view
			// .findViewById(R.id.timepicker);
			// tag = (String) v.getTag();
			// if (tag.equals("start")) {
			// cal_starttime.set(Calendar.HOUR,
			// mTimePicker.getCurrentHour());
			// cal_starttime.set(Calendar.MINUTE,
			// mTimePicker.getCurrentMinute());
			// time_start.setText(Common.datetimeHelper
			// .getTime(cal_starttime));
			// } else if (tag.equals("end")) {
			// cal_endtime.set(Calendar.HOUR,
			// mTimePicker.getCurrentHour());
			// cal_endtime.set(Calendar.MINUTE,
			// mTimePicker.getCurrentMinute());
			// time_end.setText(Common.datetimeHelper
			// .getTime(cal_endtime));
			// }
			//
			// } catch (Exception e) {
			//
			// }
			// Calendar start_datetime = Common.datetimeHelper
			// .mergeCalendars(cal_startdate,
			// cal_starttime);
			// Calendar end_datetime = Common.datetimeHelper
			// .mergeCalendars(cal_enddate, cal_endtime);
			//
			// task.rep_startDateTime = Common.datetimeHelper
			// .getDateInMs(start_datetime);
			// task.rep_endDateTime = Common.datetimeHelper
			// .getDateInMs(end_datetime);
			//
			// }
			// };
			// Common.CustomDialog.CustomDialog(mActivity,
			// time_dialog_view, posListener, R.string.done);// *
			// }
			// };

			time_start.setOnClickListener(this);
			time_end.setOnClickListener(this);
			date_start.setOnClickListener(this);
			date_end.setOnClickListener(this);

			// time_start.setOnClickListener(timeListener);
			// time_end.setOnClickListener(timeListener);

		} catch (Exception e) {

		}

		if (task._id != -1) {
			try {
				setRepeatTextView();
				// initRemainingNotificaitonsModels();
				setNotificationTextViews();
				// mTime = getTime fromDB
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		} else if (task._id == -1) {
			// initNotificaitonsMdoels();
			mTime = Calendar.getInstance();
		}
	}

	private TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			task.title = title.getText().toString();
			task.details = details.getText().toString();
			task.notes = notes.getText().toString();
		}
	};

	@SuppressLint({ "ResourceAsColor", "NewApi" })
	private void addANewTextView(TextView textView) {
		try {
			LinearLayout layout = (LinearLayout) findViewById(R.id.append_btn_linearlayout);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			textView.setText("Notification Time");
			textView.setClickable(false);
			textView.setTag(textViewTag);
			textView.setTextSize(17);
			float scale = getResources().getDisplayMetrics().density;
			int dpAsPixels = (int) (10 * scale + 0.5f);
			textView.setPadding(0, dpAsPixels, 0, dpAsPixels);
			textView.setTextColor(getResources().getColor(R.color.black));
			textView.setBackground(getResources().getDrawable(
					android.R.color.transparent));
			textView.setLayoutParams(lp);
			layout.addView(textView);
			if (textViewTag == 1) {
				tv_notification1 = textView;
				tv_notification1.setOnClickListener(editNotificationlistener);
				if (task._id == -1) {
					task.notifications.add(new TaskNotificationsModel());
					// place oneth <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
				}
			} else if (textViewTag == 2) {
				tv_notification2 = textView;
				tv_notification2.setOnClickListener(editNotificationlistener);
				if (task._id == -1) {
					task.notifications.add(new TaskNotificationsModel());
					// place second <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
				}
			} else if (textViewTag == 3) {
				tv_notification3 = textView;
				tv_notification3.setOnClickListener(editNotificationlistener);
				if (task._id == -1) {
					task.notifications.add(new TaskNotificationsModel());
					// Common.GetNotificaitonModel.one_hrBefore());
					// Tested here it works
					// place thirds <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
				}
			}
			textViewTag++;
		} catch (Exception e) {
			Log.e("New TextView createion error", e.getLocalizedMessage());
		}
	}

	private void resetNotificaionTextViews() {
		/*
		 * Remember to put a check here if(task.id==-1) otherwise it should do
		 * something else
		 */

		// when ever switched is pressed it will reset the notifications
		if (textViewTag == 2) {
			((LinearLayout) tv_notification1.getParent())
					.removeView(tv_notification1);
		} else if (textViewTag == 3) {
			((LinearLayout) tv_notification1.getParent())
					.removeView(tv_notification1);
			((LinearLayout) tv_notification2.getParent())
					.removeView(tv_notification2);
		} else if (textViewTag == 4) {
			((LinearLayout) tv_notification1.getParent())
					.removeView(tv_notification1);
			((LinearLayout) tv_notification2.getParent())
					.removeView(tv_notification2);
			((LinearLayout) tv_notification3.getParent())
					.removeView(tv_notification3);
			tv_notification_addAnother.setText("Add another notification");
			textViewFlag = 5;
		}
		textViewTag = 1;
		arrayList_TextView = new ArrayList<TextView>();
		arrayList_TextView = null;
		arrayList_TextView = new ArrayList<TextView>();
		arrayList_TextView.add(tv_notification0);
		task.notifications.add(new TaskNotificationsModel());
		// place zeroth <<<<<<<<<<<<<<<<<<<<<<<<<<<<<s
		arrayList_TextView.add(tv_notification_addAnother);
		tv_notification_addAnother.setOnClickListener(null);
		tv_notification_addAnother.setOnClickListener(addNotificationListener);
		tv_notification1.setOnClickListener(null);
		tv_notification2.setOnClickListener(null);
		tv_notification3.setOnClickListener(null);
		// initNotificaitonsMdoels();
	}

	// Methods Related to Repeat
	private void listDialog_repeat() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] list_items = new String[] { "Does not repeat", "Every day",
				"Every week", "Every month", "Every year", "Custom..." };
		builder.setItems(list_items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setRepeatValues(which);
				// setUp values in db and TextViews
			}
		});
		builder.show();
	}

	private void setRepeatValues(int which) {
		if (which == 0) {
			tv_repeat.setText("Does not repeat");
			Common.Set_RepeatModel.DoesNotRepeat(task);
		} else if (which == 1) {
			tv_repeat.setText("Every day");
			Common.Set_RepeatModel.RepeatEveryDay(task);
		} else if (which == 2) {
			tv_repeat.setText("Every week");
			Common.Set_RepeatModel.RepeatEveryWeek(task);
		} else if (which == 3) {
			tv_repeat.setText("Every month");
			Common.Set_RepeatModel.RepeatEveryMonth(task);
		} else if (which == 4) {
			tv_repeat.setText("Every year");
			Common.Set_RepeatModel.RepeatEveryYear(task);
		} else if (which == 5) {
			customRepeatDialog();
		}
	}

	// public static ArrayAdapter<CharSequence> simpleSpinner(Spinner spinner,
	// int arrayResource,
	// Context context) {
	// ArrayAdapter<CharSequence> adapter = ArrayAdapter
	// .createFromResource(context, arrayResource,
	// android.R.layout.simple_spinner_item);
	// // Specify the layout to use when the list of choices appears
	// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	// return adapter;
	// //spinner.setAdapter(adapter);
	// }

	@SuppressLint("NewApi")
	private void customRepeatDialog() {
		View dialogView = this.getLayoutInflater().inflate(
				R.layout.dialog_repeat_custom, null);
		final Button sunday_btn, monday_btn, tuesday_btn, wednesday_btn, thursday_btn, friday_btn, saturday_btn;
		final Spinner spinner_repeatTimes, spinner_repeatInterval;
		final TextView tv_setDate, spinnerRepeat_tv, tv_repeatIntervalUnit, tv_spinnerValue;

		final EditText et_spinnerRepeat, et_spinnerInterval;
		Switch switchOn;
		sunday_btn = (Button) dialogView.findViewById(R.id.btn_sunday);
		monday_btn = (Button) dialogView.findViewById(R.id.btn_monday);
		tuesday_btn = (Button) dialogView.findViewById(R.id.btn_tuesday);
		wednesday_btn = (Button) dialogView.findViewById(R.id.btn_wednesday);
		thursday_btn = (Button) dialogView.findViewById(R.id.btn_thursday);
		friday_btn = (Button) dialogView.findViewById(R.id.btn_friday);
		saturday_btn = (Button) dialogView.findViewById(R.id.btn_saturday);

		spinner_repeatInterval = (Spinner) dialogView
				.findViewById(R.id.spinner_repeatInterval);
		spinner_repeatInterval.setTextAlignment(R.color.black);
		spinner_repeatInterval.setAdapter(Common.SetAdapter.simpleSpinner(
				spinner_repeatInterval, R.array.array_repeatInterval, this));

		spinner_repeatTimes = (Spinner) dialogView
				.findViewById(R.id.spinner_repeatTimes);
		spinner_repeatTimes.setAdapter(Common.SetAdapter.simpleSpinner(
				spinner_repeatTimes, R.array.array_repeatTimes, this));

		tv_setDate = (TextView) dialogView.findViewById(R.id.tv_setDate);

		spinnerRepeat_tv = (TextView) dialogView
				.findViewById(R.id.tv_spinnerRepeat);

		et_spinnerInterval = (EditText) dialogView
				.findViewById(R.id.et_numOfInterval);

		et_spinnerRepeat = (EditText) dialogView
				.findViewById(R.id.et_spinnerRepeat);

		switchOn = (Switch) dialogView.findViewById(R.id.onOFF);
		switchOn.setChecked(true);

		tv_repeatIntervalUnit = (TextView) dialogView
				.findViewById(R.id.tv_repeatInterval); // DaysorWeekorMonths/Years

		tv_spinnerValue = (TextView) dialogView
				.findViewById(R.id.tv_spinnerRepeat);

		spinner_repeatInterval
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (position == 0) {
							// daily
							sunday_btn.setVisibility(View.GONE);
							monday_btn.setVisibility(View.GONE);
							tuesday_btn.setVisibility(View.GONE);
							wednesday_btn.setVisibility(View.GONE);
							thursday_btn.setVisibility(View.GONE);
							friday_btn.setVisibility(View.GONE);
							saturday_btn.setVisibility(View.GONE);
							isDaily = true;
							isWeekly = false;
							isMonthly = false;
							isYearly = false;
							tv_repeatIntervalUnit.setText("Day");
						} else if (position == 1) {
							// weekly
							sunday_btn.setVisibility(View.VISIBLE);
							monday_btn.setVisibility(View.VISIBLE);
							tuesday_btn.setVisibility(View.VISIBLE);
							wednesday_btn.setVisibility(View.VISIBLE);
							thursday_btn.setVisibility(View.VISIBLE);
							friday_btn.setVisibility(View.VISIBLE);
							saturday_btn.setVisibility(View.VISIBLE);
							isDaily = false;
							isWeekly = true;
							isMonthly = false;
							isYearly = false;
							tv_repeatIntervalUnit.setText("Week");
						} else if (position == 2) {
							// monthly
							sunday_btn.setVisibility(View.GONE);
							monday_btn.setVisibility(View.GONE);
							tuesday_btn.setVisibility(View.GONE);
							wednesday_btn.setVisibility(View.GONE);
							thursday_btn.setVisibility(View.GONE);
							friday_btn.setVisibility(View.GONE);
							saturday_btn.setVisibility(View.GONE);
							isDaily = false;
							isWeekly = false;
							isMonthly = true;
							isYearly = false;
							tv_repeatIntervalUnit.setText("Month");
						} else if (position == 3) {
							// yearly
							sunday_btn.setVisibility(View.GONE);
							monday_btn.setVisibility(View.GONE);
							tuesday_btn.setVisibility(View.GONE);
							wednesday_btn.setVisibility(View.GONE);
							thursday_btn.setVisibility(View.GONE);
							friday_btn.setVisibility(View.GONE);
							saturday_btn.setVisibility(View.GONE);
							isDaily = false;
							isWeekly = false;
							isMonthly = false;
							isYearly = true;
							tv_repeatIntervalUnit.setText("Year");
						}
					}

					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		spinner_repeatTimes
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (position == 0) {
							// forever
							tv_setDate.setVisibility(View.GONE);
							spinnerRepeat_tv.setVisibility(View.GONE);
							et_spinnerRepeat.setVisibility(View.GONE);
							isForever = true;
							isUntilADate = false;
							isForFixedEvents = false;

						} else if (position == 1) {
							// until A date
							tv_setDate.setVisibility(View.VISIBLE);
							spinnerRepeat_tv.setVisibility(View.GONE);
							et_spinnerRepeat.setVisibility(View.GONE);
							isForever = false;
							isUntilADate = true;
							isForFixedEvents = false;

						} else if (position == 2) {
							// until fixed number of events
							tv_setDate.setVisibility(View.GONE);
							spinnerRepeat_tv.setVisibility(View.VISIBLE);
							et_spinnerRepeat.setVisibility(View.VISIBLE);
							isForever = false;
							isUntilADate = false;
							isForFixedEvents = true;

						}

					}

					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		// Switch header

		switchOn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					sunday_btn.setEnabled(true);
					monday_btn.setEnabled(true);
					tuesday_btn.setEnabled(true);
					wednesday_btn.setEnabled(true);
					thursday_btn.setEnabled(true);
					friday_btn.setEnabled(true);
					saturday_btn.setEnabled(true);
					spinnerRepeat_tv.setEnabled(true);
					et_spinnerRepeat.setEnabled(true);
					et_spinnerInterval.setEnabled(true);
					spinner_repeatTimes.setEnabled(true);
					spinner_repeatInterval.setEnabled(true);
					tv_setDate.setEnabled(true);

				} else {
					sunday_btn.setEnabled(false);
					monday_btn.setEnabled(false);
					tuesday_btn.setEnabled(false);
					wednesday_btn.setEnabled(false);
					thursday_btn.setEnabled(false);
					friday_btn.setEnabled(false);
					saturday_btn.setEnabled(false);
					spinnerRepeat_tv.setEnabled(false);
					et_spinnerRepeat.setEnabled(false);
					et_spinnerInterval.setEnabled(false);
					spinner_repeatTimes.setEnabled(false);
					spinner_repeatInterval.setEnabled(false);
					tv_setDate.setEnabled(false);
				}
			}
		});
		sunday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					flag = true;
					Common.Snippets.setBackgroundResource(sunday_btn, flag);
					isSun = true;
				} else {
					flag = false;
					Common.Snippets.setBackgroundResource(sunday_btn, flag);
					isSun = false;
				}
			}
		});
		monday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					flag = true;
					Common.Snippets.setBackgroundResource(monday_btn, flag);
					isMon = true;

				} else {
					flag = false;
					Common.Snippets.setBackgroundResource(monday_btn, flag);
					isMon = false;
				}
			}
		});
		tuesday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					flag = true;
					Common.Snippets.setBackgroundResource(tuesday_btn, flag);
					isTue = true;

				} else {
					flag = false;
					Common.Snippets.setBackgroundResource(tuesday_btn, flag);
					isTue = false;
				}
			}
		});
		wednesday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					flag = true;
					Common.Snippets.setBackgroundResource(wednesday_btn, flag);
					isWed = true;
				} else {
					flag = false;
					Common.Snippets.setBackgroundResource(wednesday_btn, flag);
					isWed = false;
				}
			}
		});
		thursday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					flag = true;
					Common.Snippets.setBackgroundResource(thursday_btn, flag);
					isThu = true;
				} else {
					flag = false;
					Common.Snippets.setBackgroundResource(thursday_btn, flag);
					isThu = false;
				}
			}
		});
		friday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					flag = true;
					Common.Snippets.setBackgroundResource(friday_btn, flag);
					isFri = true;

				} else {
					flag = false;
					Common.Snippets.setBackgroundResource(friday_btn, flag);
					isFri = false;
				}
			}
		});
		saturday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					flag = true;
					Common.Snippets.setBackgroundResource(saturday_btn, flag);
					isSat = true;

				} else {
					flag = false;
					Common.Snippets.setBackgroundResource(saturday_btn, flag);
					isSat = false;
				}
			}
		});

		OnClickListener dateListener = new OnClickListener() {
			View calendar_dialog_view = null;

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				calendar_dialog_view = mActivity.getLayoutInflater().inflate(
						R.layout.calendar_view, null);
				DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
					DatePicker mDatePicker = null;
					CalendarView mCalendarView = null;

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String tag = "";
						try {
							mDatePicker = (DatePicker) calendar_dialog_view
									.findViewById(R.id.datepicker);
							tag = (String) v.getTag();
							if (tag.equals("repeat")) {
								cal_repeatdate.set(mDatePicker.getYear(),
										mDatePicker.getMonth() + 1,
										mDatePicker.getDayOfMonth());
							}
							tv_setDate.setText(Common.datetimeHelper
									.getDate_repeat(cal_repeatdate));

						} catch (Exception e) {
							try {
								mCalendarView = (CalendarView) calendar_dialog_view
										.findViewById(R.id.calenderView);
								cal_repeatdate.setTimeInMillis(mCalendarView
										.getDate());
								tv_setDate.setText(Common.datetimeHelper
										.getDate_repeat(cal_repeatdate));

							} catch (Exception ex) {
								// tell this to user <unsupported>
							}
						}

						// task.rep_startDateTime = Common.datetimeHelper
						// .getDateInMs(start_datetime);

					}
				};
				Common.CustomDialog.CustomDialog(mActivity,
						calendar_dialog_view, posListener, R.string.done);// *
			}
		};
		tv_setDate.setOnClickListener(dateListener);

		et_spinnerRepeat.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if ((et_spinnerRepeat.getText().toString()).isEmpty()
						|| (et_spinnerRepeat.getText().toString()) == ""
						|| (et_spinnerRepeat.getText().toString()) == " ") {
					tv_spinnerValue.setText("event");
				} else if (!(et_spinnerRepeat.getText().toString()).isEmpty()) {
					int num = Integer.valueOf((et_spinnerRepeat.getText()
							.toString()));

					if (num == 0) {
						tv_spinnerValue.setText("event");
					} else if (num == 1) {
						tv_spinnerValue.setText("event");
					} else if (num > 1) {
						tv_spinnerValue.setText("events");
					}

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		et_spinnerInterval.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String preset_singular = null, preset_plural = null;

				if (isDaily) {
					preset_singular = "Day";
					preset_plural = "Days";
				} else if (isWeekly) {
					preset_singular = "Week";
					preset_plural = "Weeks";
				} else if (isMonthly) {
					preset_singular = "Month";
					preset_plural = "Months";
				} else if (isYearly) {
					preset_singular = "Year";
					preset_plural = "Years";
				}
				if ((et_spinnerInterval.getText().toString()).isEmpty()
						|| (et_spinnerInterval.getText().toString()) == ""
						|| (et_spinnerInterval.getText().toString()) == " ") {
					tv_repeatIntervalUnit.setText(preset_singular);
				} else if (!(et_spinnerInterval.getText().toString()).isEmpty()) {
					int num = Integer.valueOf((et_spinnerInterval.getText()
							.toString()));
					if (num == 0) {
						tv_repeatIntervalUnit.setText(preset_singular);
					} else if (num == 1) {
						tv_repeatIntervalUnit.setText(preset_singular);
					} else if (num > 1) {
						tv_repeatIntervalUnit.setText(preset_plural);
					}

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// dialog.cancel();
				// TaskModel task = new TaskModel();
				if (isDaily) {
					task.rep_intervalType = 1;
					task.rep_interval = Integer.valueOf(et_spinnerInterval
							.getText().toString());
					if (isForever) {
						task.rep_intervalExpiration = null;
						tv_repeat.setText("Repeat Daily "
								+ Integer.valueOf(et_spinnerInterval.getText()
										.toString()) + " time/s");
					} else if (isUntilADate) {
						task.rep_intervalExpiration = String
								.valueOf(cal_repeatdate.getTimeInMillis());
						tv_repeat.setText("Repeat Daily "
								+ Integer.valueOf(et_spinnerInterval.getText()
										.toString())
								+ " time/s until "
								+ Common.datetimeHelper
										.getDate_repeat(cal_repeatdate));
					} else if (isForFixedEvents) {
						task.rep_intervalExpiration = et_spinnerRepeat
								.getText().toString();
						tv_repeat.setText("Repeat Daily "
								+ Integer.valueOf(et_spinnerInterval.getText()
										.toString()) + " time/s for "
								+ et_spinnerRepeat.getText().toString()
								+ " events");
					}
				} else if (isWeekly) {
					task.rep_intervalType = 2;
					task.rep_interval = Integer.valueOf(et_spinnerInterval
							.getText().toString());
					// weekdaysIntValue to be inserted in db
					task.rep_value = getWeekDayArrayListForDB();

					// getWeekDayArrayList();
					if (isForever) {
						task.rep_intervalExpiration = null;
						tv_repeat.setText("Repeat Weekly "
								+ Integer.valueOf(et_spinnerInterval.getText()
										.toString()) + " time/s on"
								+ getWeekDayArrayList());
					} else if (isUntilADate) {
						task.rep_intervalExpiration = String
								.valueOf(cal_repeatdate.getTimeInMillis());
						tv_repeat.setText("Repeat Weekly "
								+ Integer.valueOf(et_spinnerInterval.getText()
										.toString())
								+ " time/s until "
								+ Common.datetimeHelper
										.getDate_repeat(cal_repeatdate)
								+ " on " + getWeekDayArrayList());
					} else if (isForFixedEvents) {
						task.rep_intervalExpiration = et_spinnerRepeat
								.getText().toString();
						tv_repeat.setText("Repeat Weekly "
								+ Integer.valueOf(et_spinnerInterval.getText()
										.toString()) + " time/s for "
								+ et_spinnerRepeat.getText().toString()
								+ " events on " + getWeekDayArrayList());
					}
				} else if (isMonthly) {
					task.rep_intervalType = 3;
					task.rep_interval = Integer.valueOf(et_spinnerInterval
							.getText().toString());
					if (isForever) {
						task.rep_intervalExpiration = null;
						tv_repeat.setText("Repeat Monthly "
								+ Integer.valueOf(et_spinnerInterval.getText()
										.toString()) + " time/s");
					} else if (isUntilADate) {
						task.rep_intervalExpiration = String
								.valueOf(cal_repeatdate.getTimeInMillis());
						tv_repeat.setText("Repeat Monthly "
								+ Integer.valueOf(et_spinnerInterval.getText()
										.toString())
								+ " time/s until "
								+ Common.datetimeHelper
										.getDate_repeat(cal_repeatdate));
					} else if (isForFixedEvents) {
						task.rep_intervalExpiration = et_spinnerRepeat
								.getText().toString();
						tv_repeat.setText("Repeat Monthly "
								+ Integer.valueOf(et_spinnerInterval.getText()
										.toString()) + " time/s for "
								+ et_spinnerRepeat.getText().toString()
								+ " events");
					}
				} else if (isYearly) {
					task.rep_intervalType = 4;
					task.rep_interval = Integer.valueOf(et_spinnerInterval
							.getText().toString());
					if (isForever) {
						task.rep_intervalExpiration = null;
						tv_repeat.setText("Repeat Yearly "
								+ Integer.valueOf(et_spinnerInterval.getText()
										.toString()) + " time/s");
					} else if (isUntilADate) {
						task.rep_intervalExpiration = String
								.valueOf(cal_repeatdate.getTimeInMillis());
						tv_repeat.setText("Repeat Yearly "
								+ Integer.valueOf(et_spinnerInterval.getText()
										.toString())
								+ " time/s until "
								+ Common.datetimeHelper
										.getDate_repeat(cal_repeatdate));
					} else if (isForFixedEvents) {
						task.rep_intervalExpiration = et_spinnerRepeat
								.getText().toString();
						tv_repeat.setText("Repeat Yearly "
								+ Integer.valueOf(et_spinnerInterval.getText()
										.toString()) + " time/s for "
								+ et_spinnerRepeat.getText().toString()
								+ " events");
					}
				}
				resetAllFlags();
			}

		};

		Common.CustomDialog.calenderDialog(this, dialogView, null, posListener,
				R.string.done);
	}

	private String getWeekDayArrayListForDB() {
		// it is currently un used but it will be used in customRepeat so dont
		// delete this one
		ArrayList<String> weekdaysIntValue = new ArrayList<String>();
		if (isSun) {
			weekdaysIntValue.add("1");
		}
		if (isMon) {
			weekdaysIntValue.add("2");
		}
		if (isTue) {
			weekdaysIntValue.add("3");
		}
		if (isWed) {
			weekdaysIntValue.add("4");
		}
		if (isThu) {
			weekdaysIntValue.add("5");
		}
		if (isFri) {
			weekdaysIntValue.add("6");
		}
		if (isSat) {
			weekdaysIntValue.add("7");
		}
		// return weekdaysIntValue;

		String indexOfDays = "";

		for (String s : weekdaysIntValue) {
			indexOfDays += s + ",";
		}
		return indexOfDays;
	}

	private ArrayList<String> getWeekDayArrayList() {
		ArrayList<String> weekdaysSelected = new ArrayList<String>();
		ArrayList<Integer> weekdaysIntValue = new ArrayList<Integer>();
		if (isSun) {
			weekdaysSelected.add("Sun");
			weekdaysIntValue.add(1);
		}
		if (isMon) {
			weekdaysSelected.add("Mon");
			weekdaysIntValue.add(2);
		}
		if (isTue) {
			weekdaysSelected.add("Tue");
			weekdaysIntValue.add(3);
		}
		if (isWed) {
			weekdaysSelected.add("Wed");
			weekdaysIntValue.add(4);
		}
		if (isThu) {
			weekdaysSelected.add("Thu");
			weekdaysIntValue.add(5);
		}
		if (isFri) {
			weekdaysSelected.add("Fri");
			weekdaysIntValue.add(6);
		}
		if (isSat) {
			weekdaysSelected.add("Sat");
			weekdaysIntValue.add(7);
		}

		return weekdaysSelected;
	}

	private void resetAllFlags() {
		isDaily = false;
		isWeekly = false;
		isMonthly = false;
		isYearly = false;
		isForever = false;
		isUntilADate = false;
		isForFixedEvents = false;
		isSat = false;
		isSun = false;
		isMon = false;
		isTue = false;
		isWed = false;
		isThu = false;
		isFri = false;
		cal_repeatdate = Calendar.getInstance();
		cal_repeatdate.set(00, 00, 00, 00, 00);
	}

	// //////////////////////////////////////////
	private void setRepeatTextView() {
		// tv_repeat.setText(task.repetition.)
		// based on combinations it should return some text and that text will
		// be displayed at this text view
		int interval = task.rep_interval;
		// interval = number of days or weeks or months or years
		int intervalType = task.rep_intervalType;
		String type = null;
		String stringToBeDisplayed = null;
		if (interval == 0) {
			stringToBeDisplayed = "Does not repeat";
		} else if (interval > 0) {
			if (intervalType == 1) {
				// day or days
				type = "daily";
			} else if (intervalType == 2) {
				// week or weeks
				type = "weekly";
			} else if (intervalType == 3) {
				// monthly
				type = "monthly";
			} else if (intervalType == 4) {
				// yearly
				type = "yearly";
			}
			if (task.rep_intervalExpiration == null
					|| task.rep_intervalExpiration.isEmpty()) {
				// forever
				stringToBeDisplayed = interval + " time/s " + type;
			} else if (task.rep_intervalExpiration != null
					|| !(task.rep_intervalExpiration.isEmpty())) {
				// then its either a date or number of events
				if (Integer.parseInt(task.rep_intervalExpiration) >= 1
						&& Integer.parseInt(task.rep_intervalExpiration) <= 730) {
					// its number of events
					String numberOfEvents = task.rep_intervalExpiration;
					stringToBeDisplayed = interval + " time/s " + type
							+ " for " + numberOfEvents + " events ";
				} else {
					// its a long date/ expire datenumberOfEvents
					String date = task.rep_intervalExpiration;
					stringToBeDisplayed = interval + " time/s " + type
							+ " util " + date + " ";
				}

			}
		}
		tv_repeat.setText(stringToBeDisplayed);
	}

	// Methods Related to Repeat

	// Methods Related to Notifications
	private void listDialog_notifications(final int tag,
			final TaskNotificationsModel model) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] list_items = null;
		if (task.rep_allDay == 0) {
			list_items = new String[] { "No notification", "At time of event",
					"10 minutes before", "30 minutes before", "1 hour before",
					"Custom..." };
		} else if (task.rep_allDay == 1) {
			list_items = new String[] { "No notification", "On Day at 1 PM",
					"On Day at 10 AM", "On Day at 9 AM",
					"The Day before at 11:30 PM", "Custom..." };
		}
		builder.setItems(list_items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// int tag = Integer.valueOf(tv/* _notification */.getTag()
				// .toString());
				setNotificationsValues(which, tag, model);
				// setUp values in db and TextViews
			}
		});
		builder.show();
	}

	private void setNotificationsValues(int which, int tag,
			TaskNotificationsModel model) {
		int size = task.notifications.size();
		String string_TVnotificaion = null;
		TextView generic_TV = null;
		if (tag == 0) {
			generic_TV = tv_notification0;
		} else if (tag == 1) {
			generic_TV = tv_notification1;
		} else if (tag == 2) {
			generic_TV = tv_notification2;
		} else if (tag == 3) {
			generic_TV = tv_notification3;
		} else if (tag == 4) {
			generic_TV = tv_notification_addAnother;
		}
		if (which == 0) {
			string_TVnotificaion = "No Notification";
			model.interval_type = 0;
			task.notifications.set(tag, model);
			generic_TV.setText(string_TVnotificaion);
		} else if (which == 1) {
			if (task.rep_allDay == 0) {
				string_TVnotificaion = "At time of event";
				task.notifications.set(tag,
						Common.GetNotificaitonModel.atTimeOfEvent(model));
			} else if (task.rep_allDay == 1) {
				string_TVnotificaion = "On Day at 1 PM";
				task.notifications.set(tag,
						Common.GetNotificaitonModel.onDayAtOnePM());
			}
			generic_TV.setText(string_TVnotificaion);
		} else if (which == 2) {
			if (task.rep_allDay == 0) {
				string_TVnotificaion = "10 minutes before";
				task.notifications.set(tag,
						Common.GetNotificaitonModel.ten_minsBefore());
			} else if (task.rep_allDay == 1) {
				string_TVnotificaion = "On Day at 10 AM";
				task.notifications.set(tag,
						Common.GetNotificaitonModel.onDayAtTenAM());
			}
			generic_TV.setText(string_TVnotificaion);
		} else if (which == 3) {
			if (task.rep_allDay == 0) {
				string_TVnotificaion = "30 minutes before";
				task.notifications.set(tag,
						Common.GetNotificaitonModel.thirty_minsBefore(model));
			} else if (task.rep_allDay == 1) {
				string_TVnotificaion = "On Day at 9 AM";
				task.notifications.set(tag,
						Common.GetNotificaitonModel.onDayAtNineAM());
			}
			generic_TV.setText(string_TVnotificaion);
		} else if (which == 4) {
			if (task.rep_allDay == 0) {
				string_TVnotificaion = "1 hour before";
				task.notifications.set(tag,
						Common.GetNotificaitonModel.one_hrBefore(model));
			} else if (task.rep_allDay == 1) {
				string_TVnotificaion = "The Day before at 11:30 PM";
				task.notifications.set(tag, Common.GetNotificaitonModel
						.onDayBeforeAtElevenThirtyPM());
			}
			generic_TV.setText(string_TVnotificaion);
		} else if (which == 5) {
			customNotificationDialog_Test(tag, model);
			// what if it return a string and then setting text part will happen
			// here as well
		}
	}

	Boolean isMin = false, isHrs = false, isDys = false, isWks = false;

	private void customNotificationDialog_Test(final int tv_tag,
			final TaskNotificationsModel model) {
		View view = this.getLayoutInflater().inflate(
				R.layout.dialog_notifications_custom, null);
		final EditText nTimes = (EditText) view
				.findViewById(R.id.et_numberOfNotifications);
		final RadioGroup rg_TimeUnit = (RadioGroup) view
				.findViewById(R.id.myRadioGroup);
		final RadioGroup rg_TimeUnit_allDay = (RadioGroup) view
				.findViewById(R.id.myRadioGroup_allDay);
		final TextView tv_timepicker = (TextView) view
				.findViewById(R.id.tv_time_picker);
		final View line_timepicker = (View) view
				.findViewById(R.id.line_time_picker);
		if (task.rep_allDay == 0) {
			rg_TimeUnit.setVisibility(View.VISIBLE);
			isMin = true;

		} else if (task.rep_allDay == 1) {
			rg_TimeUnit_allDay.setVisibility(View.VISIBLE);
			isDys = true;
			tv_timepicker.setVisibility(View.VISIBLE);
			line_timepicker.setVisibility(View.VISIBLE);
			tv_timepicker.setText(DateUtils.formatDateTime(TaskActivity.this,
					mTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));

		}
		final RadioGroup rg_NotificationOrEmail = (RadioGroup) view
				.findViewById(R.id.myRadioGroup2);

		android.widget.RadioGroup.OnCheckedChangeListener mListener = new android.widget.RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				View radioButton = rg_TimeUnit.findViewById(checkedId);
				int type = rg_TimeUnit.indexOfChild(radioButton);
				String check = nTimes.getText().toString();
				if (type == 0) {
					nTimes.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							4) });
					isMin = true;
					isHrs = false;
					isDys = false;
					isWks = false;
					nTimes.setText(check);
				} else if (type == 1) {
					nTimes.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							4) });
					isMin = false;
					isHrs = true;
					isDys = false;
					isWks = false;
					nTimes.setText(check);
				} else if (type == 2) {
					nTimes.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							3) });
					isMin = false;
					isHrs = false;
					isDys = true;
					isWks = false;
					nTimes.setText(check);
				} else if (type == 3) {
					nTimes.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							2) });
					isMin = false;
					isHrs = false;
					isDys = false;
					isWks = true;
					nTimes.setText(check);
				}
			}
		};

		android.widget.RadioGroup.OnCheckedChangeListener mListener_allDay = new android.widget.RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				View radioButton = rg_TimeUnit_allDay.findViewById(checkedId);
				int type = rg_TimeUnit_allDay.indexOfChild(radioButton);
				String check = nTimes.getText().toString();
				if (type == 0) {
					nTimes.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							3) });
					isMin = false;
					isHrs = false;
					isDys = true;
					isWks = false;
					nTimes.setText(check);
				} else if (type == 1) {
					nTimes.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							2) });
					isMin = false;
					isHrs = false;
					isDys = false;
					isWks = true;
					nTimes.setText(check);
				}
			}
		};

		rg_TimeUnit_allDay.setOnCheckedChangeListener(mListener_allDay);

		rg_TimeUnit.setOnCheckedChangeListener(mListener);

		nTimes.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (task.rep_allDay == 0) {

					int times = 0;
					try {
						if (nTimes.getText().toString() == ""
								|| nTimes.getText().toString() == null
								|| nTimes.getText().toString().isEmpty()) {
							// Do Nothing
						} else {
							times = Integer
									.valueOf(nTimes.getText().toString());
						}

					} catch (Exception e) {
						Log.e("setting values", e.getLocalizedMessage());
						times = 1;
						nTimes.setText("1");
					}
					if (isMin) {
						if (times > 600) {
							nTimes.setText("600");
						}
					} else if (isHrs) {
						if (times > 128) {
							nTimes.setText("128");
						}
					} else if (isDys) {
						if (times > 28) {
							nTimes.setText("28");
						}
					} else if (isWks) {
						if (times > 4) {
							nTimes.setText("4");
						}
					}
				} else if (task.rep_allDay == 1) {

					int times = 0;
					try {
						if (nTimes.getText().toString() == ""
								|| nTimes.getText().toString() == null
								|| nTimes.getText().toString().isEmpty()) {
							// Do Nothing
						} else {
							times = Integer
									.valueOf(nTimes.getText().toString());
						}

					} catch (Exception e) {
						Log.e("setting values", e.getLocalizedMessage());
						times = 1;
						nTimes.setText("1");
					}
					if (isDys) {
						if (times > 28) {
							nTimes.setText("28");
						}
					} else if (isWks) {
						if (times > 4) {
							nTimes.setText("4");
						}
					}
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (task.rep_allDay == 0) {
					RadioButton btn0 = (RadioButton) rg_TimeUnit.getChildAt(0);
					RadioButton btn1 = (RadioButton) rg_TimeUnit.getChildAt(1);
					RadioButton btn2 = (RadioButton) rg_TimeUnit.getChildAt(2);
					RadioButton btn3 = (RadioButton) rg_TimeUnit.getChildAt(3);

					if ((nTimes.getText().toString()).isEmpty()
							|| (nTimes.getText().toString()) == ""
							|| (nTimes.getText().toString()) == " ") {
						btn0.setText("Minute");
						btn1.setText("Hour");
						btn2.setText("Day");
						btn3.setText("Week");

					} else if (!(nTimes.getText().toString()).isEmpty()) {
						int num = Integer.valueOf((nTimes.getText().toString()));
						if (num == 0 || num == 1) {
							btn0.setText("Minute");
							btn1.setText("Hour");
							btn2.setText("Day");
							btn3.setText("Week");
						} else if (num > 1) {
							btn0.setText("Minutes");
							btn1.setText("Hours");
							btn2.setText("Days");
							btn3.setText("Weeks");
						}
					}
				} else if (task.rep_allDay == 1) {
					RadioButton btn0 = (RadioButton) rg_TimeUnit_allDay
							.getChildAt(0);
					RadioButton btn1 = (RadioButton) rg_TimeUnit_allDay
							.getChildAt(1);

					if ((nTimes.getText().toString()).isEmpty()
							|| (nTimes.getText().toString()) == ""
							|| (nTimes.getText().toString()) == " ") {
						btn0.setText("Day");
						btn1.setText("Week");

					} else if (!(nTimes.getText().toString()).isEmpty()) {
						int num = Integer.valueOf((nTimes.getText().toString()));
						if (num == 0) {
							btn0.setText("Day");
							btn1.setText("Week");
						} else if (num == 1) {
							btn0.setText("Day");
							btn1.setText("Week");
						} else if (num > 1) {
							btn0.setText("Days");
							btn1.setText("Weeks");
						}
					}
				}
			}
		});

		tv_timepicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						mTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
						mTime.set(Calendar.MINUTE, minute);

						// updateDateAndTimeDisplay();

						tv_timepicker.setText(DateUtils.formatDateTime(
								TaskActivity.this, mTime.getTimeInMillis(),
								DateUtils.FORMAT_SHOW_TIME));

					}
				};
				// TimePickerDialog.

				new TimePickerDialog(TaskActivity.this, mTimeListener, mTime
						.get(Calendar.HOUR_OF_DAY), mTime.get(Calendar.MINUTE),
						true).show();

			}
		});

		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				isMin = false;
				isDys = false;
				isWks = false;
				isHrs = false;
				// tv_tag is refrence for arrayList
				int id_email = rg_NotificationOrEmail.getCheckedRadioButtonId();
				View radioButton_email = rg_NotificationOrEmail
						.findViewById(id_email);
				int isEmail = rg_NotificationOrEmail
						.indexOfChild(radioButton_email);

				int intervalType_radioID = 0;
				if (task.rep_allDay == 1) {
					int id = rg_TimeUnit_allDay.getCheckedRadioButtonId();

					View radioButton_1 = rg_TimeUnit_allDay.findViewById(id);

					intervalType_radioID = rg_TimeUnit_allDay
							.indexOfChild(radioButton_1);

					if (intervalType_radioID == 0) {
						intervalType_radioID = 2;// days
					} else if (intervalType_radioID == 1) {
						intervalType_radioID = 3;// weeks
					}

					model.interval_expiration = String.valueOf((mTime
							.getTimeInMillis()));

				} else if (task.rep_allDay == 0) {
					int id = rg_TimeUnit.getCheckedRadioButtonId();
					View radioButton_0 = rg_TimeUnit.findViewById(id);
					intervalType_radioID = rg_TimeUnit
							.indexOfChild(radioButton_0);

				}
				try {
					model.interval = Integer.valueOf(nTimes.getText()
							.toString());
				} catch (Exception e) {
					Log.e("no value for interval was enetered",
							e.getLocalizedMessage());
					model.interval = 1;
				}
				model.interval_type = intervalType_radioID + 1;
				model.send_notificaion_as_email = isEmail;

				if (task._id == -1) {
					task.notifications.set(tv_tag, model);
				} else {
					// task.notifications.add(tv_tag, temp);
				}

				generateAndSetCustomNotificationString(tv_tag, model);
			}
		};
		Common.CustomDialog.CustomDialogWithRadio(this, view, posListener,
				R.string.done);
	}

	private void generateAndSetCustomNotificationString(int tv_tag,
			TaskNotificationsModel model) {

		// making and setting string part
		String string_TVnotificaion = null;
		String string_timeUnit = null;
		String string_asEmail = " ";

		if (model.interval_type == 1) {
			string_timeUnit = "minute/s";
		} else if (model.interval_type == 2) {
			string_timeUnit = "hour/s";
		} else if (model.interval_type == 3) {
			string_timeUnit = "day/s";
		} else if (model.interval_type == 4) {
			string_timeUnit = "week/s";
		}
		if (model.send_notificaion_as_email == 1) {
			string_asEmail = "as email";
		}

		string_TVnotificaion = model.interval + " " + string_timeUnit + " "
				+ string_asEmail;

		TextView generic_TV = null;
		if (tv_tag == 0) {
			generic_TV = tv_notification0;
		} else if (tv_tag == 1) {
			generic_TV = tv_notification1;
		} else if (tv_tag == 2) {
			generic_TV = tv_notification2;
		} else if (tv_tag == 3) {
			generic_TV = tv_notification3;
		} else if (tv_tag == 4) {
			generic_TV = tv_notification_addAnother;
		}
		generic_TV.setText(string_TVnotificaion);
	}

	// ///////////////////////////////////////////////
	private void setNotificationTextViews() {
		int listSize = task.notifications.size();

		if (listSize == 1) {
			for (TaskNotificationsModel notificationModel : task.notifications) {
				int unitOfTime_type = notificationModel.interval_type;
				String unitOfTime = null;
				if (unitOfTime_type == 1) {
					unitOfTime = " minute/s ";
				} else if (unitOfTime_type == 2) {
					unitOfTime = " hour/s ";
				} else if (unitOfTime_type == 3) {
					unitOfTime = " days/s ";
				} else if (unitOfTime_type == 4) {
					unitOfTime = " weeks/s ";
				}
				tv_notification0.setText(notificationModel.interval
						+ unitOfTime + " before, as "
						+ notificationModel.send_notificaion_as_email);
			}
		} else if (listSize >= 2) {
			int numberOfTextViewsToBeCreated = 0;
			if (listSize == 2) {
				numberOfTextViewsToBeCreated = 1;
			} else if (listSize == 3) {
				numberOfTextViewsToBeCreated = 2;
			} else if (listSize == 4) {
				numberOfTextViewsToBeCreated = 3;
			} else if (listSize == 5) {
				numberOfTextViewsToBeCreated = 3;
				textViewFlag = 6;
			}
			for (int i = 0; i < numberOfTextViewsToBeCreated; i++) {
				final TextView newTextView = new TextView(
						getApplicationContext());
				addANewTextView(newTextView);
				arrayList_TextView.add(newTextView);
			}

			for (TaskNotificationsModel notificationModel : task.notifications) {

				int unitOfTime_type = notificationModel.interval_type;
				String unitOfTime = null;
				if (unitOfTime_type == 1) {
					unitOfTime = " minute/s ";
				} else if (unitOfTime_type == 2) {
					unitOfTime = " hour/s ";
				} else if (unitOfTime_type == 3) {
					unitOfTime = " days/s ";
				} else if (unitOfTime_type == 4) {
					unitOfTime = " weeks/s ";
				}

				if (task.notifications.indexOf(notificationModel) == 0) {

					tv_notification0.setText(notificationModel.interval
							+ unitOfTime + " before, as "
							+ notificationModel.send_notificaion_as_email);
				} else if (task.notifications.indexOf(notificationModel) == 1) {
					tv_notification1.setText(notificationModel.interval
							+ unitOfTime + " before, as "
							+ notificationModel.send_notificaion_as_email);
				} else if (task.notifications.indexOf(notificationModel) == 2) {
					tv_notification2.setText(notificationModel.interval
							+ unitOfTime + " before, as "
							+ notificationModel.send_notificaion_as_email);
				} else if (task.notifications.indexOf(notificationModel) == 3) {
					tv_notification3.setText(notificationModel.interval
							+ unitOfTime + " before, as "
							+ notificationModel.send_notificaion_as_email);
				} else if (task.notifications.indexOf(notificationModel) == 4) {
					tv_notification_addAnother
							.setText(notificationModel.interval
									+ unitOfTime
									+ " before, as "
									+ notificationModel.send_notificaion_as_email);
				}
			}
		}
	}

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		Calendar calendar;
		TaskActivity mTaskActivity;
		Boolean isStart = false;
		Boolean isEnd = false;
		Boolean isDate = false;
		Boolean isTime = false;

		public TimePickerFragment(Calendar _calendar,
				TaskActivity _mTaskActivity, Boolean _isDate, Boolean _isTime,
				Boolean _isStart, Boolean _isEnd) {
			calendar = _calendar;
			mTaskActivity = _mTaskActivity;
			isStart = _isStart;
			isEnd = _isEnd;
			isDate = _isDate;
			isTime = _isTime;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		@Override
		public void onTimeSet(android.widget.TimePicker view, int hourOfDay,
				int minute) {
			// TODO Auto-generated method stub
			calendar = Calendar.getInstance();
			calendar.set(0, 0, 0, 0, 0);
			calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calendar.set(Calendar.MINUTE, minute);
			((TaskActivity) mTaskActivity).setCalender(calendar, isDate,
					isTime, isStart, isEnd);

		}
	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		Calendar calendar;
		TaskActivity mTaskActivity;
		Boolean isStart = false;
		Boolean isEnd = false;
		Boolean isDate = false;
		Boolean isTime = false;

		public DatePickerFragment(Calendar _calendar,
				TaskActivity _mTaskActivity, Boolean _isDate, Boolean _isTime,
				Boolean _isStart, Boolean _isEnd) {
			calendar = _calendar;
			mTaskActivity = _mTaskActivity;
			isStart = _isStart;
			isEnd = _isEnd;
			isDate = _isDate;
			isTime = _isTime;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			// final Calendar c = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			calendar = Calendar.getInstance();
			calendar.set(0, 0, 0, 0, 0);
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			calendar.set(Calendar.MONTH, monthOfYear);
			((TaskActivity) mTaskActivity).setCalender(calendar, isDate,
					isTime, isStart, isEnd);
		}
	}

	public void setCalender(Calendar calendar, Boolean isDate, Boolean isTime,
			Boolean isStart, Boolean isEnd) {

		if (isDate) {
			if (isStart) {
				cal_startdate = calendar;
				date_start
						.setText(Common.datetimeHelper.getDate(cal_startdate));
			} else if (isEnd) {
				cal_enddate = calendar;
				date_end.setText(Common.datetimeHelper.getDate(cal_enddate));
			}
		} else if (isTime) {
			if (isStart) {
				cal_starttime = calendar;
				time_start
						.setText(Common.datetimeHelper.getTime(cal_starttime));
			} else if (isEnd) {
				cal_endtime = calendar;
				time_end.setText(Common.datetimeHelper.getTime(cal_endtime));

			}

		}

		Calendar start_datetime = Common.datetimeHelper.mergeCalendars(
				cal_startdate, cal_starttime);
		Calendar end_datetime = Common.datetimeHelper.mergeCalendars(
				cal_enddate, cal_endtime);
		task.rep_startDateTime = Common.datetimeHelper
				.getDateInMs(start_datetime);
		task.rep_endDateTime = Common.datetimeHelper.getDateInMs(end_datetime);

	}

	// reset();
	// private static void reset() {
	// isEnd = false;
	// isStart = false;
	// isTime = false;
	// isDate = false;
	// }

	@Override
	public void onClick(View v) {
		if (v == time_start) {

			Boolean isStart = true;
			Boolean isTime = true;

			Boolean isEnd = false;
			Boolean isDate = false;

			DialogFragment newFragment = new TimePickerFragment(cal_starttime,
					TaskActivity.this, isDate, isTime, isStart, isEnd);
			newFragment.show(getSupportFragmentManager(), "timePicker");

		} else if (v == time_end) {
			Boolean isEnd = true;
			Boolean isTime = true;

			Boolean isDate = false;
			Boolean isStart = false;

			DialogFragment newFragment = new TimePickerFragment(cal_endtime,
					TaskActivity.this, isDate, isTime, isStart, isEnd);
			newFragment.show(getSupportFragmentManager(), "timePicker");
		} else if (v == date_start) {
			Boolean isStart = true;
			Boolean isDate = true;

			Boolean isEnd = false;
			Boolean isTime = false;

			DialogFragment newFragment = new DatePickerFragment(cal_startdate,
					TaskActivity.this, isDate, isTime, isStart, isEnd);
			newFragment.show(getSupportFragmentManager(), "datePicker");

		} else if (v == date_end) {
			Boolean isEnd = true;
			Boolean isDate = true;

			Boolean isStart = false;
			Boolean isTime = false;

			DialogFragment newFragment = new DatePickerFragment(cal_enddate,
					TaskActivity.this, isDate, isTime, isStart, isEnd);
			newFragment.show(getSupportFragmentManager(), "datePicker");

		}

	}

	// public static class TimePickerFragment_Test extends DialogFragment
	// implements TimePickerDialog.OnTimeSetListener {
	//
	// Calendar calendar;
	// TaskActivity mTaskActivity;
	//
	// public TimePickerFragment_Test(Calendar _calendar,
	// TaskActivity _mTaskActivity) {
	// calendar = _calendar;
	// mTaskActivity = _mTaskActivity;
	// }
	//
	// @Override
	// public Dialog onCreateDialog(Bundle savedInstanceState) {
	// // Use the current time as the default values for the picker
	// int hour = calendar.get(Calendar.HOUR_OF_DAY);
	// int minute = calendar.get(Calendar.MINUTE);
	//
	// // Create a new instance of TimePickerDialog and return it
	// return new TimePickerDialog(getActivity(), this, hour, minute,
	// DateFormat.is24HourFormat(getActivity()));
	// }
	//
	// @Override
	// public void onTimeSet(android.widget.TimePicker view, int hourOfDay,
	// int minute) {
	// // TODO Auto-generated method stub
	// calendar = Calendar.getInstance();
	// calendar.set(0, 0, 0, 0, 0);
	// calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
	// calendar.set(Calendar.MINUTE, minute);
	// //((TaskActivity) mTaskActivity).doSomethingWithCalender(calendar);
	//
	// }
	// }

}
