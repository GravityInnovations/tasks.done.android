package com.gravity.innovations.tasks.done;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SetTaskReminderActivity extends ActionBarActivity {
	private TaskModel task;
	private TaskListModel list;
	private ImageButton ib_Save, ib_Cancel;
	private TextView tv_title, tv_details, tv_notes;
	// ,tv_notification;// change tv_repeat to button

	private EditText et_title, et_details, et_notes;
	private Switch _switch;
	private TextView tv_repeat, tv_timeStart, tv_timeEnd, tv_dateStart,
			tv_dateEnd, tv_notification, tv_notification_addAnother;

	private Context mContext;
	private ActionBar actionBar;
	private Calendar c;
	boolean isSwitchChecked = false;
	String v, nE = null;

	View.OnClickListener mListener;
	String dateStart, dateEnd, timeStart, timeEnd;// , dateTimeStart,
													// dateTimeEnd;

	// String v = null;

	// private Button time1_btn, time2_btn;
	// private Button date1_btn;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_task_reminder);
		mContext = getApplicationContext();

		Intent intent = getIntent();
		Bundle mBundle = intent.getExtras();
		mBundle.getSerializable("key_list");
		mBundle.getSerializable("key_task");

		list = (TaskListModel) intent.getSerializableExtra("key_list");
		task = (TaskModel) intent.getSerializableExtra("key_task");
		// widgets declaration
		et_title = (EditText) findViewById(R.id.textViewTitle);
		et_title.setText(task.title);

		et_details = (EditText) findViewById(R.id.textViewDetails);
		et_details.setText(task.details);

		et_notes = (EditText) findViewById(R.id.textViewNotes);
		et_notes.setText(task.notes);

		tv_timeStart = (TextView) findViewById(R.id.btn_time_1);
		tv_dateStart = (TextView) findViewById(R.id.btn_date_1);
		tv_timeEnd = (TextView) findViewById(R.id.btn_time_2);
		tv_dateEnd = (TextView) findViewById(R.id.btn_date_2);

		_switch = (Switch) findViewById(R.id.onOFF);
		_switch.setChecked(false);
		_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					tv_timeStart.setEnabled(false);
					tv_timeEnd.setEnabled(false);
					tv_timeStart.setVisibility(View.GONE);
					tv_timeEnd.setVisibility(View.GONE);
					isSwitchChecked = true;
				} else {
					tv_timeStart.setEnabled(true);
					tv_timeEnd.setEnabled(true);
					tv_timeStart.setVisibility(View.VISIBLE);
					tv_timeEnd.setVisibility(View.VISIBLE);
					isSwitchChecked = false;
				}

			}
		});

		tv_timeStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// open openCustomTimepickerDialog dialog
				openCustomTimepickerDialog();

			}
		});
		tv_timeEnd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// open openCustomTimepickerDialog dialog
				openCustomTimepickerDialog();

			}
		});

		tv_dateStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// open openCustomTimepickerDialog dialog
				openCalenderViewDialogStartDate();

			}
		});
		tv_dateEnd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// open openCustomTimepickerDialog dialog
				openCalenderViewDialogEndDate();

			}
		});

		tv_repeat = (TextView) findViewById(R.id.repeat_tv);
		tv_repeat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				repeatDialog(tv_repeat);
			}
		});

		tv_notification = (TextView) findViewById(R.id.add_notification_btn);
		tv_notification_addAnother = (TextView) findViewById(R.id.add_another_notification_btn);

		tv_notification.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// notificationDialog();
				radioDialog();
			}
		});

		final ArrayList<TextView> notificaitonBtnList = new ArrayList<TextView>();
		notificaitonBtnList.add(tv_notification);
		notificaitonBtnList.add(tv_notification_addAnother);

		tv_notification_addAnother
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (notificaitonBtnList.size() < 5) {
							// Button newButton = new
							// Button(getApplicationContext());
							TextView newButton = new TextView(
									getApplicationContext());
							newButton
									.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											radioDialog();
										}
									});
							addANewButton(newButton);
							notificaitonBtnList.add(newButton);
						}
					}
				});

	}

	@SuppressLint({ "ResourceAsColor", "NewApi" })
	public void addANewButton(TextView button) {
		try {
			LinearLayout layout = (LinearLayout) findViewById(R.id.append_btn_linearlayout);
			// set the properties for button
			// Button btnTag = new Button(this);

			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			// lp.setMargins(0, 10, 0, 10);
			// lp.setMargins(0, 0, 0, 10);

			// lp.addRule(RelativeLayout.BELOW, R.id.add_notification_btn);
			// lp.addRule(RelativeLayout.ALIGN_LEFT, R.id.add_notification_btn);

			// button.setLayoutParams(new
			// LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
			// RelativeLayout.LayoutParams.WRAP_CONTENT));
			button.setText("Notification Time");
			button.setTextSize(17);
			float scale = getResources().getDisplayMetrics().density;
			int dpAsPixels = (int) (10 * scale + 0.5f);

			button.setPadding(0, dpAsPixels, 0, dpAsPixels);
			button.setTextColor(getResources().getColor(R.color.black));// Color.parseColor("#bdbdbd"));
			button.setBackground(getResources().getDrawable(
					android.R.color.transparent));
			button.setLayoutParams(lp);
			// button.setPadding("0", "0", "0", "5");
			// add button to the layout
			layout.addView(button);
			// return btnTag;
		} catch (Exception e) {
			Log.e("Button createion error", e.getLocalizedMessage());
		}

	}

	@SuppressLint("NewApi")
	public void openCalenderViewDialogStartDate() {
		View dialog_headerView = this.getLayoutInflater().inflate(
				R.layout.dialog_calenderview_header, null);
		View dialog_detailsView = this.getLayoutInflater().inflate(
				R.layout.dialog_calenderview, null);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					// save the new task title
				}
			};
			Common.CustomDialog.CustomDialog(this, dialog_detailsView,
					posListener, R.string.dialog_done);

		} else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			final TextView tv_year, tv_date, tv_month, tv_weekday;

			tv_weekday = (TextView) dialog_headerView.findViewById(R.id.tv_day);

			tv_date = (TextView) dialog_headerView.findViewById(R.id.tv_date);
			tv_month = (TextView) dialog_headerView.findViewById(R.id.tv_month);
			tv_year = (TextView) dialog_headerView.findViewById(R.id.tv_year);
			c = Calendar.getInstance();
			// tv_year_final = tv_year;
			// tv_date_final = tv_date;
			// tv_month_final = tv_month;

			CalendarView calendarView = (CalendarView) dialog_detailsView
					.findViewById(R.id.calenderView);
			calendarView
					.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
						public void onSelectedDayChange(CalendarView view,
								int year, int month, int dayOfMonth) {

							c.set(Calendar.YEAR, year);
							c.set(Calendar.MONTH, month);
							c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

							String monthString = c.getDisplayName(
									Calendar.MONTH, Calendar.SHORT,
									Locale.getDefault());
							tv_year.setText(String.valueOf(year));
							tv_date.setText(String.valueOf(dayOfMonth));

							tv_month.setText(monthString);

							String weekdayString = c.getDisplayName(
									Calendar.DAY_OF_WEEK, Calendar.LONG,
									Locale.getDefault());

							tv_weekday.setText(weekdayString);
							dateStart = weekdayString + ", " + monthString
									+ " " + String.valueOf(dayOfMonth) + ", "
									+ String.valueOf(year);
						}
					});
			DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					Date newDate = c.getTime();
					long a = c.getTimeInMillis();
					tv_dateStart.setText(dateStart);
				}
			};

			Common.CustomDialog.CustomDialog(this, dialog_detailsView,
					dialog_headerView, posListener, R.string.dialog_done);
		} else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					// save the new task title
				}
			};
			View dialogView = this.getLayoutInflater().inflate(
					R.layout.cal_view, null);
			Common.CustomDialog.CustomDialog(this, dialogView, posListener,
					R.string.dialog_done);

		}
	}

	@SuppressLint("NewApi")
	public void openCalenderViewDialogEndDate() {
		View dialog_headerView = this.getLayoutInflater().inflate(
				R.layout.dialog_calenderview_header, null);
		View dialog_detailsView = this.getLayoutInflater().inflate(
				R.layout.dialog_calenderview, null);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					// save the new task title
				}
			};
			Common.CustomDialog.CustomDialog(this, dialog_detailsView,
					posListener, R.string.dialog_done);

		} else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			final TextView tv_year, tv_date, tv_month, tv_weekday;

			tv_weekday = (TextView) dialog_headerView.findViewById(R.id.tv_day);

			tv_date = (TextView) dialog_headerView.findViewById(R.id.tv_date);
			tv_month = (TextView) dialog_headerView.findViewById(R.id.tv_month);
			tv_year = (TextView) dialog_headerView.findViewById(R.id.tv_year);
			c = Calendar.getInstance();
			// tv_year_final = tv_year;
			// tv_date_final = tv_date;
			// tv_month_final = tv_month;

			CalendarView calendarView = (CalendarView) dialog_detailsView
					.findViewById(R.id.calenderView);
			calendarView
					.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
						public void onSelectedDayChange(CalendarView view,
								int year, int month, int dayOfMonth) {

							c.set(Calendar.YEAR, year);
							c.set(Calendar.MONTH, month);
							c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

							String monthString = c.getDisplayName(
									Calendar.MONTH, Calendar.SHORT,
									Locale.getDefault());
							tv_year.setText(String.valueOf(year));
							tv_date.setText(String.valueOf(dayOfMonth));

							tv_month.setText(monthString);

							String weekdayString = c.getDisplayName(
									Calendar.DAY_OF_WEEK, Calendar.LONG,
									Locale.getDefault());

							tv_weekday.setText(weekdayString);
							dateEnd = weekdayString + ", " + monthString + " "
									+ String.valueOf(dayOfMonth) + ", "
									+ String.valueOf(year);
						}
					});
			DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					Date newDate = c.getTime();
					long a = c.getTimeInMillis();
					tv_dateEnd.setText(dateEnd);
				}
			};

			Common.CustomDialog.CustomDialog(this, dialog_detailsView,
					dialog_headerView, posListener, R.string.dialog_done);
		} else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					// save the new task title
				}
			};
			View dialogView = this.getLayoutInflater().inflate(
					R.layout.cal_view, null);
			Common.CustomDialog.CustomDialog(this, dialogView, posListener,
					R.string.dialog_done);

		}
	}

	@SuppressLint("NewApi")
	public void openCustomTimepickerDialog() {
		View dialog_detailsView = this.getLayoutInflater().inflate(
				R.layout.dialog_timepicker, null);
		final TimePicker timePicker = (TimePicker) dialog_detailsView
				.findViewById(R.id.timepicker);

		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				// save the new task title
				int hour, minute = 0;
				hour = timePicker.getCurrentHour();
				minute = timePicker.getCurrentMinute();
//				timeStart = (String.valueOf(hour) + ":" + String
//						.valueOf(minute));			
				if (hour <= 12) { //for am time
					if(minute < 10){//appends 0 if time and hour are single digits
						String mins =  String.valueOf(minute);
						tv_timeStart.setText("0" + hour + ":" + "0" + mins + " AM");
					}
				} 
				else if (hour > 12) {
					if (hour == 13) {
						hour = 1;
					} else if (hour == 14) {
						hour = 2;
					} else if (hour == 15) {
						hour = 3;
					} else if (hour == 16) {
						hour = 4;
					} else if (hour == 17) {
						hour = 5;
					} else if (hour == 18) {
						hour = 6;
					} else if (hour == 19) {
						hour = 7;
					} else if (hour == 20) {
						hour = 8;
					} else if (hour == 21) {
						hour = 9;
					} else if (hour == 22) {
						hour = 10;
					} else if (hour == 23) {
						hour = 11;
					} else if (hour == 24) {
						hour = 12;
					}
					timeStart = ("0" + String.valueOf(hour) + ":"
							+ String.valueOf(minute) + " PM");
					tv_timeStart.setText(timeStart);
				}
				// tv_timeStart.setText(timeStart);
			}
		};
		Common.CustomDialog.CustomDialog(this, dialog_detailsView, posListener,
				R.string.dialog_done);

	}

	@SuppressLint("NewApi")
	public void openCustomTimepickerDialogEndTime() {
		View dialog_detailsView = this.getLayoutInflater().inflate(
				R.layout.dialog_timepicker, null);
		final TimePicker timePicker = (TimePicker) dialog_detailsView
				.findViewById(R.id.timepicker);

		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				// save the new task title
				int hour, minute = 0;
				hour = timePicker.getCurrentHour();
				minute = timePicker.getCurrentMinute();
				timeEnd = (String.valueOf(hour) + String.valueOf(minute));
				tv_timeEnd.setText(timeEnd);

			}
		};
		Common.CustomDialog.CustomDialog(this, dialog_detailsView, posListener,
				R.string.dialog_done);

	}

	@SuppressLint("NewApi")
	public void openCustomTimepickerDialogCustom() {
		Boolean is24HourFormat = DateFormat.is24HourFormat(mContext);

		if (is24HourFormat) {

			View dialog_detailsView = this.getLayoutInflater().inflate(
					R.layout.dialog_custom_timepicker, null);

			final TextView tv_ampm = (TextView) dialog_detailsView
					.findViewById(R.id.tv_chrnonometer_am_pm);
			tv_ampm.setVisibility(View.GONE);

			TextView hour_zeroth = (TextView) dialog_detailsView
					.findViewById(R.id.tv_hours_zero_index);

			TextView hour_oneth = (TextView) dialog_detailsView
					.findViewById(R.id.tv_hours_one_index);

			TextView minutes_oneth = (TextView) dialog_detailsView
					.findViewById(R.id.tv_minutes_one_index);

			TextView minutes_zeroth = (TextView) dialog_detailsView
					.findViewById(R.id.tv_minutes_zero_index);

			final TextView final_hour_zeroth = hour_zeroth, final_hour_oneth = hour_oneth;

			final TextView final_minutes_zeroth = minutes_zeroth, final_minutes_oneth = minutes_oneth;

			Button amOrPmButton, button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8, button_9, button_0, button_backspace;
			button_1 = (Button) dialog_detailsView.findViewById(R.id.tv_1);
			button_2 = (Button) dialog_detailsView.findViewById(R.id.tv_2);
			button_3 = (Button) dialog_detailsView.findViewById(R.id.tv_3);
			button_4 = (Button) dialog_detailsView.findViewById(R.id.tv_4);
			button_5 = (Button) dialog_detailsView.findViewById(R.id.tv_5);
			button_6 = (Button) dialog_detailsView.findViewById(R.id.tv_6);
			button_7 = (Button) dialog_detailsView.findViewById(R.id.tv_7);
			button_8 = (Button) dialog_detailsView.findViewById(R.id.tv_8);
			button_9 = (Button) dialog_detailsView.findViewById(R.id.tv_9);
			button_0 = (Button) dialog_detailsView.findViewById(R.id.tv_0);

			amOrPmButton = (Button) dialog_detailsView.findViewById(R.id.tv_am);

			final Button final_amOrPmButton = amOrPmButton;
			// final_amOrPmButton.setAlpha(0);
			final_amOrPmButton.setText(":00");
			final_amOrPmButton.setEnabled(false);

			button_backspace = (Button) dialog_detailsView
					.findViewById(R.id.backspace);

			View.OnClickListener listener = new View.OnClickListener() {
				public void onClick(View v) {
					String currentText_zeroIndex_hours = final_hour_zeroth
							.getText().toString();
					String currentText_oneIndex_hours = final_hour_oneth
							.getText().toString();
					String currentText_zeroIndex_minutes = final_minutes_zeroth
							.getText().toString();
					String currentText_oneIndex_minutes = final_minutes_oneth
							.getText().toString();

					String pressed_key = (String) v.getTag();
					if (v.getTag() == "0" || v.getTag() == "1"
							|| v.getTag() == "2" || v.getTag() == "3"
							|| v.getTag() == "4" || v.getTag() == "5"
							|| v.getTag() == "6" || v.getTag() == "7"
							|| v.getTag() == "8" || v.getTag() == "9")
					// != "")
					{

						if (currentText_zeroIndex_hours.contains("0")
								&& currentText_oneIndex_hours.contains("0")
								&& currentText_oneIndex_minutes.contains("0")
								&& currentText_zeroIndex_minutes.contains("0")) {
							final_minutes_zeroth.setText(pressed_key);
						}

						else if (currentText_zeroIndex_hours.contains("0")
								&& currentText_oneIndex_hours.contains("0")
								&& currentText_oneIndex_minutes.contains("0")
								&& !currentText_zeroIndex_minutes.isEmpty()) {
							final_minutes_oneth
									.setText(currentText_zeroIndex_minutes);
							final_minutes_zeroth.setText(pressed_key);

						}

						else if (currentText_zeroIndex_hours.contains("0")
								&& currentText_oneIndex_hours.contains("0")
								&& !currentText_oneIndex_minutes.isEmpty()
								&& !currentText_zeroIndex_minutes.isEmpty())

						{
							final_hour_zeroth
									.setText(currentText_oneIndex_minutes);
							final_minutes_oneth
									.setText(currentText_zeroIndex_minutes);
							final_minutes_zeroth.setText(pressed_key);

						}

						else if (!currentText_zeroIndex_hours.isEmpty()
								&& currentText_oneIndex_hours.contains("0")
								&& !currentText_oneIndex_minutes.isEmpty()
								&& !currentText_zeroIndex_minutes.isEmpty())

						{
							final_hour_oneth
									.setText(currentText_zeroIndex_hours);
							final_hour_zeroth
									.setText(currentText_oneIndex_minutes);
							final_minutes_oneth
									.setText(currentText_zeroIndex_minutes);
							final_minutes_zeroth.setText(pressed_key);

						}
					}

					else if (v.getTag() == "bs") {
						if (!currentText_zeroIndex_hours.isEmpty()
								&& !currentText_oneIndex_hours.isEmpty()
								&& !currentText_oneIndex_minutes.isEmpty()
								&& !currentText_zeroIndex_minutes.isEmpty()) {

							final_hour_zeroth
									.setText(currentText_oneIndex_hours);
							final_minutes_zeroth
									.setText(currentText_oneIndex_minutes);
							final_minutes_oneth
									.setText(currentText_zeroIndex_hours);

							final_hour_oneth.setText("0");

						}
					}

				}
			};

			amOrPmButton.setOnClickListener(new OnClickListener() {

				boolean flag = false;

				@Override
				public void onClick(View v) {
					if (!flag) {
						final_amOrPmButton.setText("PM");
						tv_ampm.setText("AM");
						flag = true;

					} else {
						final_amOrPmButton.setText("AM");
						tv_ampm.setText("PM");
						flag = false;
					}

				}
			});

			button_1.setTag("1");
			button_1.setOnClickListener(listener);
			button_2.setTag("2");
			button_2.setOnClickListener(listener);
			button_3.setTag("3");
			button_3.setOnClickListener(listener);
			button_4.setTag("4");
			button_4.setOnClickListener(listener);
			button_5.setTag("5");
			button_5.setOnClickListener(listener);
			button_6.setTag("6");
			button_6.setOnClickListener(listener);
			button_7.setTag("7");
			button_7.setOnClickListener(listener);
			button_8.setTag("8");
			button_8.setOnClickListener(listener);
			button_9.setTag("9");
			button_9.setOnClickListener(listener);
			button_0.setTag("0");
			button_0.setOnClickListener(listener);
			button_backspace.setTag("bs");
			button_backspace.setOnClickListener(listener);

			DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					// save the new task title
				}
			};
			Common.CustomDialog.CustomDialog(this, dialog_detailsView,
					posListener, R.string.dialog_done);

		} else {
			View dialog_detailsView = this.getLayoutInflater().inflate(
					R.layout.rewamp_custom_time_picker, null);

			final TextView tv_ampm = (TextView) dialog_detailsView
					.findViewById(R.id.tv_chrnonometer_am_pm);

			TextView hour_zeroth = (TextView) dialog_detailsView
					.findViewById(R.id.tv_hours_zero_index);

			TextView hour_oneth = (TextView) dialog_detailsView
					.findViewById(R.id.tv_hours_one_index);

			TextView minutes_oneth = (TextView) dialog_detailsView
					.findViewById(R.id.tv_minutes_one_index);

			TextView minutes_zeroth = (TextView) dialog_detailsView
					.findViewById(R.id.tv_minutes_zero_index);

			final TextView final_hour_zeroth = hour_zeroth, final_hour_oneth = hour_oneth;

			final TextView final_minutes_zeroth = minutes_zeroth, final_minutes_oneth = minutes_oneth;

			final Button amOrPmButton, button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8, button_9, button_0, button_backspace;
			button_1 = (Button) dialog_detailsView.findViewById(R.id.tv_1);
			button_2 = (Button) dialog_detailsView.findViewById(R.id.tv_2);
			button_3 = (Button) dialog_detailsView.findViewById(R.id.tv_3);
			button_4 = (Button) dialog_detailsView.findViewById(R.id.tv_4);
			button_5 = (Button) dialog_detailsView.findViewById(R.id.tv_5);
			button_6 = (Button) dialog_detailsView.findViewById(R.id.tv_6);
			button_7 = (Button) dialog_detailsView.findViewById(R.id.tv_7);
			button_8 = (Button) dialog_detailsView.findViewById(R.id.tv_8);
			button_9 = (Button) dialog_detailsView.findViewById(R.id.tv_9);
			button_0 = (Button) dialog_detailsView.findViewById(R.id.tv_0);
			button_0.setEnabled(false);

			amOrPmButton = (Button) dialog_detailsView.findViewById(R.id.tv_am);

			final Button final_amOrPmButton = amOrPmButton;

			button_backspace = (Button) dialog_detailsView
					.findViewById(R.id.backspace);

			View.OnClickListener listener = new View.OnClickListener() {
				public void onClick(View v) {

					String currentText_zeroIndex_hours = final_hour_zeroth
							.getText().toString();

					String currentText_oneIndex_hours = final_hour_oneth
							.getText().toString();

					String currentText_zeroIndex_minutes = final_minutes_zeroth
							.getText().toString();

					String currentText_oneIndex_minutes = final_minutes_oneth
							.getText().toString();

					// ///////////////////////////////////////////////////////////////////////////////
					/*
					 * if (currentText_zeroIndex_hours == "-" &&
					 * currentText_oneIndex_hours == "-" &&
					 * currentText_oneIndex_minutes == "-" &&
					 * currentText_zeroIndex_minutes == "-") { // enable all
					 * keys button_0.setEnabled(false);
					 * button_1.setEnabled(true); button_2.setEnabled(true);
					 * button_3.setEnabled(true); button_4.setEnabled(true);
					 * button_5.setEnabled(true); button_6.setEnabled(true);
					 * button_7.setEnabled(true); button_8.setEnabled(true);
					 * button_9.setEnabled(true); } else if
					 * (currentText_oneIndex_hours == "-" &&
					 * currentText_zeroIndex_hours != "-" &&
					 * currentText_oneIndex_minutes != "-" &&
					 * currentText_zeroIndex_minutes != "-") {
					 * button_0.setEnabled(false); button_1.setEnabled(false);
					 * button_2.setEnabled(false); button_3.setEnabled(false);
					 * button_4.setEnabled(false); button_5.setEnabled(false);
					 * button_6.setEnabled(false); button_7.setEnabled(false);
					 * button_8.setEnabled(false); button_9.setEnabled(false);
					 * 
					 * }
					 * 
					 * else if (currentText_oneIndex_hours == "-" &&
					 * currentText_zeroIndex_hours == "-" &&
					 * currentText_oneIndex_minutes != "-" &&
					 * currentText_zeroIndex_minutes != "-") {
					 * button_0.setEnabled(true); button_1.setEnabled(true);
					 * button_2.setEnabled(true); button_3.setEnabled(true);
					 * button_4.setEnabled(true); button_5.setEnabled(true);
					 * button_6.setEnabled(true); button_7.setEnabled(true);
					 * button_8.setEnabled(true); button_9.setEnabled(true); }
					 * else if (currentText_oneIndex_hours == "-" &&
					 * currentText_zeroIndex_hours == "-" &&
					 * currentText_oneIndex_minutes == "-" &&
					 * currentText_zeroIndex_minutes != "-") {
					 * button_0.setEnabled(true); button_1.setEnabled(true);
					 * button_2.setEnabled(true); button_3.setEnabled(true);
					 * button_4.setEnabled(true); button_5.setEnabled(true);
					 * button_6.setEnabled(false); button_7.setEnabled(false);
					 * button_8.setEnabled(false); button_9.setEnabled(false); }
					 */
					// ////////////////////////////////////////////////////////////////////////////////////////////////

					String pressed_key = (String) v.getTag();
					if (v.getTag() == "0" || v.getTag() == "1"
							|| v.getTag() == "2" || v.getTag() == "3"
							|| v.getTag() == "4" || v.getTag() == "5"
							|| v.getTag() == "6" || v.getTag() == "7"
							|| v.getTag() == "8" || v.getTag() == "9")
					// != "")
					{

						if (currentText_zeroIndex_hours.contains("-")
								&& currentText_oneIndex_hours.contains("-")
								&& currentText_oneIndex_minutes.contains("-")
								&& currentText_zeroIndex_minutes.contains("-")) {
							final_minutes_zeroth.setText(pressed_key);
							button_0.setEnabled(true);
							button_1.setEnabled(true);
							button_2.setEnabled(true);
							button_3.setEnabled(true);
							button_4.setEnabled(true);
							button_5.setEnabled(true);
							button_6.setEnabled(false);
							button_7.setEnabled(false);
							button_8.setEnabled(false);
							button_9.setEnabled(false);

						}

						else if (currentText_zeroIndex_hours.contains("-")
								&& currentText_oneIndex_hours.contains("-")
								&& currentText_oneIndex_minutes.contains("-")
								&& !currentText_zeroIndex_minutes.isEmpty()) {
							final_minutes_oneth
									.setText(currentText_zeroIndex_minutes);
							final_minutes_zeroth.setText(pressed_key);
							button_0.setEnabled(true);
							button_1.setEnabled(true);
							button_2.setEnabled(true);
							button_3.setEnabled(true);
							button_4.setEnabled(true);
							button_5.setEnabled(true);
							button_6.setEnabled(true);
							button_7.setEnabled(true);
							button_8.setEnabled(true);
							button_9.setEnabled(true);

						}

						else if (currentText_zeroIndex_hours.contains("-")
								&& currentText_oneIndex_hours.contains("-")
								&& !currentText_oneIndex_minutes.isEmpty()
								&& !currentText_zeroIndex_minutes.isEmpty())

						{
							if (pressed_key == "0" || pressed_key == "1"
									|| pressed_key == "2" || pressed_key == "3"
									|| pressed_key == "4" || pressed_key == "5") {
								button_0.setEnabled(true);
								button_1.setEnabled(true);
								button_2.setEnabled(true);
								button_3.setEnabled(true);
								button_4.setEnabled(true);
								button_5.setEnabled(true);
								button_6.setEnabled(true);
								button_7.setEnabled(true);
								button_8.setEnabled(true);
								button_9.setEnabled(true);
							} else if (pressed_key == "6" || pressed_key == "7"
									|| pressed_key == "8" || pressed_key == "9") {
								button_0.setEnabled(false);
								button_1.setEnabled(false);
								button_2.setEnabled(false);
								button_3.setEnabled(false);
								button_4.setEnabled(false);
								button_5.setEnabled(false);
								button_6.setEnabled(false);
								button_7.setEnabled(false);
								button_8.setEnabled(false);
								button_9.setEnabled(false);
							}
							final_hour_zeroth
									.setText(currentText_oneIndex_minutes);
							final_minutes_oneth
									.setText(currentText_zeroIndex_minutes);
							final_minutes_zeroth.setText(pressed_key);
						}

						else if (!currentText_zeroIndex_hours.isEmpty()
								&& currentText_oneIndex_hours.contains("-")
								&& !currentText_oneIndex_minutes.isEmpty()
								&& !currentText_zeroIndex_minutes.isEmpty())

						{
							final_hour_oneth
									.setText(currentText_zeroIndex_hours);
							final_hour_zeroth
									.setText(currentText_oneIndex_minutes);
							final_minutes_oneth
									.setText(currentText_zeroIndex_minutes);
							final_minutes_zeroth.setText(pressed_key);

						}
					}

					else if (v.getTag() == "bs") {
						if (!currentText_zeroIndex_hours.isEmpty()
								&& !currentText_oneIndex_hours.isEmpty()
								&& !currentText_oneIndex_minutes.isEmpty()
								&& !currentText_zeroIndex_minutes.isEmpty()) {
							final_hour_zeroth
									.setText(currentText_oneIndex_hours);
							final_minutes_zeroth
									.setText(currentText_oneIndex_minutes);
							final_minutes_oneth
									.setText(currentText_zeroIndex_hours);

							final_hour_oneth.setText("-");

							// ////////////////////////////////////////////////////////////////////////////////
							if (currentText_zeroIndex_hours == "-"
									&& currentText_oneIndex_hours == "-"
									&& currentText_oneIndex_minutes == "-"
									&& currentText_zeroIndex_minutes == "-") {
								// enable all keys
								button_0.setEnabled(false);
								button_1.setEnabled(true);
								button_2.setEnabled(true);
								button_3.setEnabled(true);
								button_4.setEnabled(true);
								button_5.setEnabled(true);
								button_6.setEnabled(true);
								button_7.setEnabled(true);
								button_8.setEnabled(true);
								button_9.setEnabled(true);
							}

							else if (currentText_oneIndex_hours == "-"
									&& currentText_zeroIndex_hours != "-"
									&& currentText_oneIndex_minutes != "-"
									&& currentText_zeroIndex_minutes != "-") {
								// enable some and disable some on some basis
								button_0.setEnabled(false);
								button_1.setEnabled(false);
								button_2.setEnabled(false);
								button_3.setEnabled(false);
								button_4.setEnabled(false);
								button_5.setEnabled(false);
								button_6.setEnabled(false);
								button_7.setEnabled(false);
								button_8.setEnabled(false);
								button_9.setEnabled(false);

							}

							else if (currentText_oneIndex_hours == "-"
									&& currentText_zeroIndex_hours == "-"
									&& currentText_oneIndex_minutes != "-"
									&& currentText_zeroIndex_minutes != "-") {
								button_0.setEnabled(true);
								button_1.setEnabled(true);
								button_2.setEnabled(true);
								button_3.setEnabled(true);
								button_4.setEnabled(true);
								button_5.setEnabled(true);
								button_6.setEnabled(true);
								button_7.setEnabled(true);
								button_8.setEnabled(true);
								button_9.setEnabled(true);
							} else if (currentText_oneIndex_hours == "-"
									&& currentText_zeroIndex_hours == "-"
									&& currentText_oneIndex_minutes == "-"
									&& currentText_zeroIndex_minutes != "-") {
								button_0.setEnabled(true);
								button_1.setEnabled(true);
								button_2.setEnabled(true);
								button_3.setEnabled(true);
								button_4.setEnabled(true);
								button_5.setEnabled(true);
								button_6.setEnabled(false);
								button_7.setEnabled(false);
								button_8.setEnabled(false);
								button_9.setEnabled(false);
							}
							// /////////////////////////////////////////////////////////////////////////////////////////////////

						}
					}

				}
			};

			amOrPmButton.setOnClickListener(new OnClickListener() {

				boolean flag = false;

				@Override
				public void onClick(View v) {
					if (!flag) {
						final_amOrPmButton.setText("PM");
						tv_ampm.setText("AM");
						flag = true;

					} else {
						final_amOrPmButton.setText("AM");
						tv_ampm.setText("PM");
						flag = false;
					}

				}
			});

			button_1.setTag("1");
			button_1.setOnClickListener(listener);
			button_2.setTag("2");
			button_2.setOnClickListener(listener);
			button_3.setTag("3");
			button_3.setOnClickListener(listener);
			button_4.setTag("4");
			button_4.setOnClickListener(listener);
			button_5.setTag("5");
			button_5.setOnClickListener(listener);
			button_6.setTag("6");
			button_6.setOnClickListener(listener);
			button_7.setTag("7");
			button_7.setOnClickListener(listener);
			button_8.setTag("8");
			button_8.setOnClickListener(listener);
			button_9.setTag("9");
			button_9.setOnClickListener(listener);
			button_0.setTag("0");
			button_0.setOnClickListener(listener);
			button_backspace.setTag("bs");
			button_backspace.setOnClickListener(listener);

			DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					// save the new task title
				}
			};
			Common.CustomDialog.CustomDialog(this, dialog_detailsView,
					posListener, R.string.dialog_done);
		}

	}

	@SuppressLint("NewApi")
	public void openCustomTimepickerDialog12() {
		View dialog_detailsView = this.getLayoutInflater().inflate(
				R.layout.dialog_custom_timepicker, null);

		final TextView tv_ampm = (TextView) dialog_detailsView
				.findViewById(R.id.tv_chrnonometer_am_pm);

		TextView hour_zeroth = (TextView) dialog_detailsView
				.findViewById(R.id.tv_hours_zero_index);

		TextView hour_oneth = (TextView) dialog_detailsView
				.findViewById(R.id.tv_hours_one_index);

		TextView minutes_oneth = (TextView) dialog_detailsView
				.findViewById(R.id.tv_minutes_one_index);

		TextView minutes_zeroth = (TextView) dialog_detailsView
				.findViewById(R.id.tv_minutes_zero_index);

		final TextView final_hour_zeroth = hour_zeroth, final_hour_oneth = hour_oneth;

		final TextView final_minutes_zeroth = minutes_zeroth, final_minutes_oneth = minutes_oneth;

		Button amOrPmButton, button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8, button_9, button_0, button_backspace;
		button_1 = (Button) dialog_detailsView.findViewById(R.id.tv_1);
		button_2 = (Button) dialog_detailsView.findViewById(R.id.tv_2);
		button_3 = (Button) dialog_detailsView.findViewById(R.id.tv_3);
		button_4 = (Button) dialog_detailsView.findViewById(R.id.tv_4);
		button_5 = (Button) dialog_detailsView.findViewById(R.id.tv_5);
		button_6 = (Button) dialog_detailsView.findViewById(R.id.tv_6);
		button_7 = (Button) dialog_detailsView.findViewById(R.id.tv_7);
		button_8 = (Button) dialog_detailsView.findViewById(R.id.tv_8);
		button_9 = (Button) dialog_detailsView.findViewById(R.id.tv_9);
		button_0 = (Button) dialog_detailsView.findViewById(R.id.tv_0);

		amOrPmButton = (Button) dialog_detailsView.findViewById(R.id.tv_am);

		final Button final_amOrPmButton = amOrPmButton;

		button_backspace = (Button) dialog_detailsView
				.findViewById(R.id.backspace);

		View.OnClickListener listener = new View.OnClickListener() {
			public void onClick(View v) {
				String currentText_zeroIndex_hours = final_hour_zeroth
						.getText().toString();
				String currentText_oneIndex_hours = final_hour_oneth.getText()
						.toString();
				String currentText_zeroIndex_minutes = final_minutes_zeroth
						.getText().toString();
				String currentText_oneIndex_minutes = final_minutes_oneth
						.getText().toString();

				String pressed_key = (String) v.getTag();
				if (v.getTag() == "0" || v.getTag() == "1" || v.getTag() == "2"
						|| v.getTag() == "3" || v.getTag() == "4"
						|| v.getTag() == "5" || v.getTag() == "6"
						|| v.getTag() == "7" || v.getTag() == "8"
						|| v.getTag() == "9")
				// != "")
				{

					if (currentText_zeroIndex_hours.contains("0")
							&& currentText_oneIndex_hours.contains("0")
							&& currentText_oneIndex_minutes.contains("0")
							&& currentText_zeroIndex_minutes.contains("0")) {
						final_minutes_zeroth.setText(pressed_key);
					}

					else if (currentText_zeroIndex_hours.contains("0")
							&& currentText_oneIndex_hours.contains("0")
							&& currentText_oneIndex_minutes.contains("0")
							&& !currentText_zeroIndex_minutes.isEmpty()) {
						final_minutes_oneth
								.setText(currentText_zeroIndex_minutes);
						final_minutes_zeroth.setText(pressed_key);

					}

					else if (currentText_zeroIndex_hours.contains("0")
							&& currentText_oneIndex_hours.contains("0")
							&& !currentText_oneIndex_minutes.isEmpty()
							&& !currentText_zeroIndex_minutes.isEmpty())

					{
						final_hour_zeroth.setText(currentText_oneIndex_minutes);
						final_minutes_oneth
								.setText(currentText_zeroIndex_minutes);
						final_minutes_zeroth.setText(pressed_key);

					}

					else if (!currentText_zeroIndex_hours.isEmpty()
							&& currentText_oneIndex_hours.contains("0")
							&& !currentText_oneIndex_minutes.isEmpty()
							&& !currentText_zeroIndex_minutes.isEmpty())

					{
						final_hour_oneth.setText(currentText_zeroIndex_hours);
						final_hour_zeroth.setText(currentText_oneIndex_minutes);
						final_minutes_oneth
								.setText(currentText_zeroIndex_minutes);
						final_minutes_zeroth.setText(pressed_key);

					}
				}

				else if (v.getTag() == "bs") {
					if (!currentText_zeroIndex_hours.isEmpty()
							&& !currentText_oneIndex_hours.isEmpty()
							&& !currentText_oneIndex_minutes.isEmpty()
							&& !currentText_zeroIndex_minutes.isEmpty()) {

						final_hour_zeroth.setText(currentText_oneIndex_hours);
						final_minutes_zeroth
								.setText(currentText_oneIndex_minutes);
						final_minutes_oneth
								.setText(currentText_zeroIndex_hours);

						final_hour_oneth.setText("0");

					}
				}

			}
		};

		amOrPmButton.setOnClickListener(new OnClickListener() {

			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					final_amOrPmButton.setText("PM");
					tv_ampm.setText("AM");
					flag = true;

				} else {
					final_amOrPmButton.setText("AM");
					tv_ampm.setText("PM");
					flag = false;
				}

			}
		});

		button_1.setTag("1");
		button_1.setOnClickListener(listener);
		button_2.setTag("2");
		button_2.setOnClickListener(listener);
		button_3.setTag("3");
		button_3.setOnClickListener(listener);
		button_4.setTag("4");
		button_4.setOnClickListener(listener);
		button_5.setTag("5");
		button_5.setOnClickListener(listener);
		button_6.setTag("6");
		button_6.setOnClickListener(listener);
		button_7.setTag("7");
		button_7.setOnClickListener(listener);
		button_8.setTag("8");
		button_8.setOnClickListener(listener);
		button_9.setTag("9");
		button_9.setOnClickListener(listener);
		button_0.setTag("0");
		button_0.setOnClickListener(listener);
		button_backspace.setTag("bs");
		button_backspace.setOnClickListener(listener);

		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				// save the new task title
			}
		};
		Common.CustomDialog.CustomDialog(this, dialog_detailsView, posListener,
				R.string.dialog_done);
	}

	// public void listViewInflate(){
	// final ListView listView = (ListView)
	// findViewById(R.id.add_notificaiton_list);
	// final ArrayList<String> notificaitonList = new ArrayList<String>();
	// notificaitonList.add("Add Notification");
	// notificaitonList.add("Add Another");
	// notificaitonList.add("Add Another");
	//
	// final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	// android.R.layout.simple_list_item_1, notificaitonList);
	// listView.setScrollContainer(false);
	// listView.setAdapter(adapter);
	//
	// listView.setTextFilterEnabled(true);
	// listView.setOnItemClickListener(new OnItemClickListener() {
	// public void onItemClick(AdapterView<?> parent, View view,
	// int position, long id) {
	// int notificationArraySize = notificaitonList.size();
	// if (position == notificaitonList.size()-1)
	// notificaitonList.add("XXXXXXXXXXX");
	//
	// listView.setAdapter(adapter);
	// }
	// });
	// }

	public void radioDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] list_items = { "No notification", "At time of event",
				"10 minutes before", "30 minutes before", "1 hour before",
				"Custom..." };
		builder.setItems(list_items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Toast.makeText(getApplicationContext(),
				// "RepeatDialog " + which, Toast.LENGTH_LONG).show();
				if (which == 0) {
					tv_notification.setText("No Notification");
				} else if (which == 1) {
					tv_notification.setText("At time of event");
				} else if (which == 2) {
					tv_notification.setText("10 minutes before");
				} else if (which == 3) {
					tv_notification.setText("30 minutes before");
				} else if (which == 4) {
					tv_notification.setText("1 hour before");
				} else if (which == 5) {
					notificationDialog();// it should return a string that shall
											// be used as setText
				}
			}
		});
		builder.show();
	}

	public void repeatDialog(final TextView tv) {// final TaskListModel
													// tasklist, final TaskModel
		// temp) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] list_items = { "Does not repeat", "Every day", "Every week",
				"Every month", "Every year", "Custom..." };
		builder.setItems(list_items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					tv.setText("Does not repeat");
					tv.setTag("0");
				} else if (which == 1) {
					tv.setText("Every day");
					tv.setTag("1");
				} else if (which == 2) {
					tv.setText("Every week");
					tv.setTag("2");
				} else if (which == 3) {
					tv.setText("Every month");
					tv.setTag("3");
				} else if (which == 4) {
					tv.setText("Every year");
					tv.setTag("4");
				} else if (which == 5) {
					repeatDetailsDialog();
					tv.setTag("5");
				}

			}
		});
		builder.setTitle("Reminder");
		builder.show();

		// final ListView modeList = new ListView(mContext);
		//
		// String[] list_item = { "Once", "Once a Day", "Once a Week",
		// "Once a Month", "Once a Year" };
		//
		// ArrayAdapter<String> itemsAdapter = new
		// ArrayAdapter<String>(mContext,
		// android.R.layout.simple_list_item_1, list_item);
		//
		// modeList.setAdapter(itemsAdapter);
		// builder.setView(modeList);
		// builder.setCancelable(false);
		// final Dialog dialog = builder.create();
		// dialog.show();
		//
		// modeList.setOnItemClickListener(new OnItemClickListener() {
		// public void onItemClick(AdapterView<?> myAdapter, View myView,
		// int myItemInt, long mylng) {
		// // selectRepeatDialogItemId(tasklist, myItemInt, temp);//
		// // listitemid
		// dialog.dismiss();
		// }
		// });
	}

	public void addItemsToSpinner(Spinner spinner) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.repeatTill_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(adapter);

	}

	public void addItemsToHeaderSpinner(Spinner spinner) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.repeatInterval_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(adapter);

	}

	@SuppressLint("NewApi")
	public void repeatDetailsDialog() {
		View dialog_detailsView = this.getLayoutInflater().inflate(
				R.layout.dialog_repeat_details, null);
		final View final_dialog_detailsView = dialog_detailsView;
		View dialog_headerView = this.getLayoutInflater().inflate(
				R.layout.dialog_repeat_details_header, null);

		// Days Buttons
		Button sunday_btn = (Button) dialog_detailsView
				.findViewById(R.id.btn_sunday);
		Button monday_btn = (Button) dialog_detailsView
				.findViewById(R.id.btn_monday);
		Button tuesday_btn = (Button) dialog_detailsView
				.findViewById(R.id.btn_tuesday);
		Button wednesday_btn = (Button) dialog_detailsView
				.findViewById(R.id.btn_wednesday);
		Button thursday_btn = (Button) dialog_detailsView
				.findViewById(R.id.btn_thursday);
		Button friday_btn = (Button) dialog_detailsView
				.findViewById(R.id.btn_friday);
		Button saturday_btn = (Button) dialog_detailsView
				.findViewById(R.id.btn_saturday);

		Button openCalender_btn = (Button) dialog_detailsView
				.findViewById(R.id.tv_open_calender);

		final Button final_sunday_btn = sunday_btn;
		final Button final_monday_btn = monday_btn;
		final Button final_tuesday_btn = tuesday_btn;
		final Button final_wednesday_btn = wednesday_btn;
		final Button final_thursday_btn = thursday_btn;
		final Button final_friday_btn = friday_btn;
		final Button final_saturday_btn = saturday_btn;
		final Button final_openCalender_btn = openCalender_btn;
		// Days Buttons

		// View
		View viewline = (View) dialog_detailsView
				.findViewById(R.id.line_et_spinner_number_of_events);
		final View final_viewline = viewline;
		// View

		// EditTexts
		EditText spinnerValue_et = (EditText) dialog_detailsView
				.findViewById(R.id.et_spinner_number_of_events);

		EditText et1 = (EditText) dialog_detailsView.findViewById(R.id.et1);

		final EditText final_spinnerValue_et = spinnerValue_et;
		final EditText final_et1 = et1;
		// EditTexts

		// TextViews
		TextView spinnerValue_tv = (TextView) dialog_detailsView
				.findViewById(R.id.tv_spinnerValue);
		final TextView final_spinnerValue_tv = spinnerValue_tv;
		// TextViews

		// Dialog HeaderView

		Spinner spinner_header = (Spinner) dialog_headerView
				.findViewById(R.id.repeatInterval_spinner);
		addItemsToHeaderSpinner(spinner_header);
		final Spinner final_spinner_header = spinner_header;

		spinner_header.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// String selectedItem =
				// parent.getItemAtPosition(position).toString();
				if (position == 0) {
					// daily
					final_sunday_btn.setVisibility(View.GONE);
					final_monday_btn.setVisibility(View.GONE);
					final_tuesday_btn.setVisibility(View.GONE);
					final_wednesday_btn.setVisibility(View.GONE);
					final_thursday_btn.setVisibility(View.GONE);
					final_friday_btn.setVisibility(View.GONE);
					final_saturday_btn.setVisibility(View.GONE);
				} else if (position == 1) {
					// weekly
					final_sunday_btn.setVisibility(View.VISIBLE);
					final_monday_btn.setVisibility(View.VISIBLE);
					final_tuesday_btn.setVisibility(View.VISIBLE);
					final_wednesday_btn.setVisibility(View.VISIBLE);
					final_thursday_btn.setVisibility(View.VISIBLE);
					final_friday_btn.setVisibility(View.VISIBLE);
					final_saturday_btn.setVisibility(View.VISIBLE);
				} else if (position == 2) {
					// monthly
					final_sunday_btn.setVisibility(View.GONE);
					final_monday_btn.setVisibility(View.GONE);
					final_tuesday_btn.setVisibility(View.GONE);
					final_wednesday_btn.setVisibility(View.GONE);
					final_thursday_btn.setVisibility(View.GONE);
					final_friday_btn.setVisibility(View.GONE);
					final_saturday_btn.setVisibility(View.GONE);
				} else if (position == 3) {
					// yearly
					final_sunday_btn.setVisibility(View.GONE);
					final_monday_btn.setVisibility(View.GONE);
					final_tuesday_btn.setVisibility(View.GONE);
					final_wednesday_btn.setVisibility(View.GONE);
					final_thursday_btn.setVisibility(View.GONE);
					final_friday_btn.setVisibility(View.GONE);
					final_saturday_btn.setVisibility(View.GONE);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		// Dialog HeaderView

		// Spinner dialog_detailsView
		Spinner spinner = (Spinner) dialog_detailsView
				.findViewById(R.id.spinner_repeatTimes);
		addItemsToSpinner(spinner);
		final Spinner final_spinner = spinner;
		// addListenerOnSpinnerItemSelection(spinner);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// String selectedItem =
				// parent.getItemAtPosition(position).toString();

				if (position == 0) {
					final_openCalender_btn.setVisibility(View.GONE);
					final_viewline.setVisibility(View.GONE);
					final_spinnerValue_tv.setVisibility(View.GONE);
					final_spinnerValue_et.setVisibility(View.GONE);
				} else if (position == 1) {
					final_openCalender_btn.setVisibility(View.VISIBLE);
					final_viewline.setVisibility(View.GONE);
					final_spinnerValue_tv.setVisibility(View.GONE);
					final_spinnerValue_et.setVisibility(View.GONE);
				} else if (position == 2) {
					final_openCalender_btn.setVisibility(View.GONE);
					final_viewline.setVisibility(View.VISIBLE);
					final_spinnerValue_tv.setVisibility(View.VISIBLE);
					final_spinnerValue_et.setVisibility(View.VISIBLE);
				}

			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		// Spinner dialog_detailsView

		// Buttons and Listeners
		openCalender_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "What",
						Toast.LENGTH_SHORT).show();

			}
		});

		sunday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					final_sunday_btn
							.setBackgroundResource(R.drawable.circle_blue_full);
					flag = true;

				} else {
					final_sunday_btn
							.setBackgroundResource(R.drawable.circle_blue);
					flag = false;
				}

			}
		});
		monday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					final_monday_btn
							.setBackgroundResource(R.drawable.circle_blue_full);
					flag = true;

				} else {
					final_monday_btn
							.setBackgroundResource(R.drawable.circle_blue);
					flag = false;
				}

			}
		});
		tuesday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					final_tuesday_btn
							.setBackgroundResource(R.drawable.circle_blue_full);
					flag = true;

				} else {
					final_tuesday_btn
							.setBackgroundResource(R.drawable.circle_blue);
					flag = false;
				}
			}
		});
		wednesday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					final_wednesday_btn
							.setBackgroundResource(R.drawable.circle_blue_full);
					flag = true;

				} else {
					final_wednesday_btn
							.setBackgroundResource(R.drawable.circle_blue);
					flag = false;
				}
			}
		});
		thursday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					final_thursday_btn
							.setBackgroundResource(R.drawable.circle_blue_full);
					flag = true;

				} else {
					final_thursday_btn
							.setBackgroundResource(R.drawable.circle_blue);
					flag = false;
				}
			}
		});
		friday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					final_friday_btn
							.setBackgroundResource(R.drawable.circle_blue_full);
					flag = true;

				} else {
					final_friday_btn
							.setBackgroundResource(R.drawable.circle_blue);
					flag = false;
				}
			}
		});
		saturday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					final_saturday_btn
							.setBackgroundResource(R.drawable.circle_blue_full);
					flag = true;

				} else {
					final_saturday_btn
							.setBackgroundResource(R.drawable.circle_blue);
					flag = false;
				}
			}
		});
		// Buttons and Listeners

		// Switch header
		Switch switchOn = (Switch) dialog_headerView.findViewById(R.id.onOFF);
		switchOn.setChecked(true);

		switchOn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					final_dialog_detailsView.setAlpha(1);
					// rootView2.setBackgroundColor(Color.TRANSPARENT);
					// rootView2.setEnabled(true);
					final_sunday_btn.setEnabled(true);
					final_monday_btn.setEnabled(true);
					final_tuesday_btn.setEnabled(true);
					final_wednesday_btn.setEnabled(true);
					final_thursday_btn.setEnabled(true);
					final_friday_btn.setEnabled(true);
					final_saturday_btn.setEnabled(true);
					// spinnerValue.setEnabled(true);
					final_spinnerValue_tv.setEnabled(true);
					final_spinnerValue_et.setEnabled(true);
					final_et1.setEnabled(true);
					final_spinner.setEnabled(true);
					final_spinner_header.setEnabled(true);
					final_openCalender_btn.setEnabled(true);

				} else {
					final_dialog_detailsView.setAlpha((float) 0.5); // 0 = un
																	// visible
					final_sunday_btn.setEnabled(false);
					final_monday_btn.setEnabled(false);
					final_tuesday_btn.setEnabled(false);
					final_wednesday_btn.setEnabled(false);
					final_thursday_btn.setEnabled(false);
					final_friday_btn.setEnabled(false);
					final_saturday_btn.setEnabled(false);
					// spinnerValue.setEnabled(false);
					final_spinnerValue_tv.setEnabled(false);
					final_spinnerValue_et.setEnabled(false);
					final_et1.setEnabled(false);
					final_spinner.setEnabled(false);
					final_spinner_header.setEnabled(false);
					final_openCalender_btn.setEnabled(false);
				}

			}
		});
		// Switch header

		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				// save the new task title
			}
		};
		Common.CustomDialog.CustomDialog(this, dialog_detailsView,
				dialog_headerView, posListener, R.string.dialog_done);
	}

	// @SuppressLint("NewApi")
	// public void repeatDetailsDialogOLD() {
	// View rootView = this.getLayoutInflater().inflate(
	// R.layout.dialog_repeat_details, null);
	//
	// final Button openCalender_btn = (Button) rootView
	// .findViewById(R.id.tv_open_calender);
	//
	// final View viewline = (View) rootView
	// .findViewById(R.id.line_et_spinner_number_of_events);
	//
	// final TextView spinnerValue_tv = (TextView) rootView
	// .findViewById(R.id.tv_spinnerValue);
	//
	// final EditText spinnerValue_et = (EditText) rootView
	// .findViewById(R.id.et_spinner_number_of_events);
	//
	// final EditText et1 = (EditText) rootView.findViewById(R.id.et1);
	//
	// openCalender_btn.setVisibility(View.GONE);
	// viewline.setVisibility(View.GONE);
	// spinnerValue_tv.setVisibility(View.GONE);
	// spinnerValue_et.setVisibility(View.GONE);
	//
	// final Button sunday_btn = (Button) rootView
	// .findViewById(R.id.btn_sunday);
	// final Button monday_btn = (Button) rootView
	// .findViewById(R.id.btn_monday);
	// final Button tuesday_btn = (Button) rootView
	// .findViewById(R.id.btn_tuesday);
	// final Button wednesday_btn = (Button) rootView
	// .findViewById(R.id.btn_wednesday);
	// final Button thursday_btn = (Button) rootView
	// .findViewById(R.id.btn_thursday);
	// final Button friday_btn = (Button) rootView
	// .findViewById(R.id.btn_friday);
	// final Button saturday_btn = (Button) rootView
	// .findViewById(R.id.btn_saturday);
	//
	// openCalender_btn.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// Toast.makeText(getApplicationContext(), "What",
	// Toast.LENGTH_SHORT).show();
	//
	// }
	// });
	//
	// sunday_btn.setOnClickListener(new View.OnClickListener() {
	// boolean flag = false;
	//
	// @Override
	// public void onClick(View v) {
	// if (!flag) {
	// sunday_btn
	// .setBackgroundResource(R.drawable.circle_blue_full);
	// flag = true;
	//
	// } else {
	// sunday_btn.setBackgroundResource(R.drawable.circle_blue);
	// flag = false;
	// }
	//
	// }
	// });
	// monday_btn.setOnClickListener(new View.OnClickListener() {
	// boolean flag = false;
	//
	// @Override
	// public void onClick(View v) {
	// if (!flag) {
	// monday_btn
	// .setBackgroundResource(R.drawable.circle_blue_full);
	// flag = true;
	//
	// } else {
	// monday_btn.setBackgroundResource(R.drawable.circle_blue);
	// flag = false;
	// }
	//
	// }
	// });
	// tuesday_btn.setOnClickListener(new View.OnClickListener() {
	// boolean flag = false;
	//
	// @Override
	// public void onClick(View v) {
	// if (!flag) {
	// tuesday_btn
	// .setBackgroundResource(R.drawable.circle_blue_full);
	// flag = true;
	//
	// } else {
	// tuesday_btn.setBackgroundResource(R.drawable.circle_blue);
	// flag = false;
	// }
	// }
	// });
	// wednesday_btn.setOnClickListener(new View.OnClickListener() {
	// boolean flag = false;
	//
	// @Override
	// public void onClick(View v) {
	// if (!flag) {
	// wednesday_btn
	// .setBackgroundResource(R.drawable.circle_blue_full);
	// flag = true;
	//
	// } else {
	// wednesday_btn.setBackgroundResource(R.drawable.circle_blue);
	// flag = false;
	// }
	// }
	// });
	// thursday_btn.setOnClickListener(new View.OnClickListener() {
	// boolean flag = false;
	//
	// @Override
	// public void onClick(View v) {
	// if (!flag) {
	// thursday_btn
	// .setBackgroundResource(R.drawable.circle_blue_full);
	// flag = true;
	//
	// } else {
	// thursday_btn.setBackgroundResource(R.drawable.circle_blue);
	// flag = false;
	// }
	// }
	// });
	// friday_btn.setOnClickListener(new View.OnClickListener() {
	// boolean flag = false;
	//
	// @Override
	// public void onClick(View v) {
	// if (!flag) {
	// friday_btn
	// .setBackgroundResource(R.drawable.circle_blue_full);
	// flag = true;
	//
	// } else {
	// friday_btn.setBackgroundResource(R.drawable.circle_blue);
	// flag = false;
	// }
	// }
	// });
	// saturday_btn.setOnClickListener(new View.OnClickListener() {
	// boolean flag = false;
	//
	// @Override
	// public void onClick(View v) {
	// if (!flag) {
	// saturday_btn
	// .setBackgroundResource(R.drawable.circle_blue_full);
	// flag = true;
	//
	// } else {
	// saturday_btn.setBackgroundResource(R.drawable.circle_blue);
	// flag = false;
	// }
	// }
	// });
	//
	// // Dialog HeaderView
	// View headerView = this.getLayoutInflater().inflate(
	// R.layout.dialog_repeat_details_header, null);
	// Spinner spinnerHeader = (Spinner) headerView
	// .findViewById(R.id.repeatInterval_spinner);
	// addItemsToHeaderSpinner(spinnerHeader);
	// Switch switchOn = (Switch) headerView.findViewById(R.id.onOFF);
	// switchOn.setChecked(true);
	// // Dialog HeaderView
	//
	// final View rootView2 = rootView;
	// switchOn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	// @Override
	// public void onCheckedChanged(CompoundButton buttonView,
	// boolean isChecked) {
	// if (isChecked) {
	// rootView2.setAlpha(1);
	// // rootView2.setBackgroundColor(Color.TRANSPARENT);
	// // rootView2.setEnabled(true);
	// sunday_btn.setEnabled(true);
	// monday_btn.setEnabled(true);
	// tuesday_btn.setEnabled(true);
	// wednesday_btn.setEnabled(true);
	// thursday_btn.setEnabled(true);
	// friday_btn.setEnabled(true);
	// saturday_btn.setEnabled(true);
	// // spinnerValue.setEnabled(true);
	//
	// } else {
	// rootView2.setAlpha((float) 0.5); // 0 = un visible
	// sunday_btn.setEnabled(false);
	// monday_btn.setEnabled(false);
	// tuesday_btn.setEnabled(false);
	// wednesday_btn.setEnabled(false);
	// thursday_btn.setEnabled(false);
	// friday_btn.setEnabled(false);
	// saturday_btn.setEnabled(false);
	// // spinnerValue.setEnabled(false);
	// //
	// rootView2.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
	// }
	//
	// }
	// });
	// // Dialog HeaderView
	// Spinner spinner = (Spinner) rootView
	// .findViewById(R.id.spinner_repeatTimes);
	// addItemsToSpinner(spinner);
	// // addListenerOnSpinnerItemSelection(spinner);
	// spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	// public void onItemSelected(AdapterView<?> parent, View view,
	// int position, long id) {
	// // String selectedItem =
	// // parent.getItemAtPosition(position).toString();
	//
	// if (position == 0) {
	// openCalender_btn.setVisibility(View.GONE);
	// viewline.setVisibility(View.GONE);
	// spinnerValue_tv.setVisibility(View.GONE);
	// spinnerValue_et.setVisibility(View.GONE);
	// } else if (position == 1) {
	// openCalender_btn.setVisibility(View.VISIBLE);
	// viewline.setVisibility(View.GONE);
	// spinnerValue_tv.setVisibility(View.GONE);
	// spinnerValue_et.setVisibility(View.GONE);
	// } else if (position == 2) {
	// openCalender_btn.setVisibility(View.GONE);
	// viewline.setVisibility(View.VISIBLE);
	// spinnerValue_tv.setVisibility(View.VISIBLE);
	// spinnerValue_et.setVisibility(View.VISIBLE);
	// }
	//
	// }
	//
	// public void onNothingSelected(AdapterView<?> parent) {
	//
	// }
	// });
	//
	// DialogInterface.OnClickListener posListener = new
	// DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.cancel();
	// // save the new task title
	// }
	// };
	//
	// Common.CustomDialog.CustomDialog(this, rootView, headerView,
	// posListener, R.string.dialog_done);
	//
	// }

	// private void addListenerOnSpinnerItemSelection(Spinner spinner) {
	//
	// spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// decide what to show in the action bar.
		getMenuInflater().inflate(R.menu.save, menu);
		restoreActionBar();
		// return true;

		return super.onCreateOptionsMenu(menu);
	}

	public void restoreActionBar() {
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);

		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(list.fragmentColor)));

		actionBar.setTitle("Task Details");
		actionBar.show();
		/***********/
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		manuallySelectOptionMenuItem(id);
		return super.onOptionsItemSelected(item);
	}

	public void manuallySelectOptionMenuItem(int id) {
		if (id == R.id.action_save) {
			updateDatabase();
			finish();
		}
	}

	public void updateDatabase() {

		DatabaseHelper db = new DatabaseHelper(mContext);

		task.title = et_title.getText().toString();
		task.details = et_details.getText().toString();
		task.notes = et_notes.getText().toString();
		db.tasks.Edit(task);

		RepeatTaskModel repeatModel = new RepeatTaskModel();
		repeatModel.task_id = task._id;

		String getTag = (String) tv_repeat.getTag();
		repeatModel.interval_type = Integer.parseInt(getTag);
		// interval_type returns 0-5
		// doesnotRepeat/daily/weekly/monthly/custom

		// SwitchOff=withTime else withoutTime
		if (!isSwitchChecked) { // if switch is OFF
			repeatModel.allDay = 0;
			repeatModel.startDateTime = tv_dateStart.getText().toString() + " "
					+ tv_timeStart.getText().toString();
			// format this date time if needed
			repeatModel.endDateTime = tv_dateEnd.getText().toString() + " "
					+ tv_timeStart.getText().toString();
			// format this date time if needed
			// we need to add time as well we are missing time at moment

		} else if (isSwitchChecked) {// if switch is ON
			repeatModel.allDay = 1;
			repeatModel.startDate = tv_dateStart.getText().toString();
			// format this date time if needed
			repeatModel.endDate = tv_dateEnd.getText().toString();
			// format this date time if needed
		}
		db.taskRepeat.Add(repeatModel);
	}

	public void notificationDialog() {
		View view = this.getLayoutInflater().inflate(
				R.layout.dialog_notifications_details, null);
		final String myString = null;
		final EditText nTimes = (EditText) view
				.findViewById(R.id.et_numberOfNotifications);
		final RadioGroup radioGroup = (RadioGroup) view
				.findViewById(R.id.myRadioGroup);
		final RadioGroup radioGroup2 = (RadioGroup) view
				.findViewById(R.id.myRadioGroup2);

		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				String nTimesString = nTimes.getText().toString();
				if (radioGroup.getCheckedRadioButtonId() != -1) {
					int id = radioGroup.getCheckedRadioButtonId();
					View radioButton = radioGroup.findViewById(id);
					int radioId = radioGroup.indexOfChild(radioButton);
					RadioButton btn = (RadioButton) radioGroup
							.getChildAt(radioId);
					String selection = (String) btn.getText();
					v = " " + selection;
				}
				if (radioGroup2.getCheckedRadioButtonId() != -1) {
					int id = radioGroup2.getCheckedRadioButtonId();
					View radioButton = radioGroup2.findViewById(id);
					int radioId = radioGroup2.indexOfChild(radioButton);
					RadioButton btn = (RadioButton) radioGroup2
							.getChildAt(radioId);
					String selection = (String) btn.getText();
					nE = " " + selection;
				}

				tv_notification.setText(nTimesString + v + " before" + nE);
			}
		};

		Common.CustomDialog.CustomDialogWithRadio(this, view, posListener,
				R.string.dialog_done);
	}

	// {// final TaskListModel tasklist, final
	// TaskModel
	// temp) {
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// String[] list_items = { "No Notification", "At defined time",
	// "10 minutes before", "30 minutes before", "1 hour before",
	// "Custom..." };
	// builder.setItems(list_items, new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// // TODO Auto-generated method stub
	// }
	// });
	// builder.setTitle("Reminder");
	// builder.show();
	// public class CustomOnItemSelectedListener implements
	// OnItemSelectedListener {
	//
	// public void onItemSelected(AdapterView<?> parent, View view, int pos,
	// long id) {
	// // Toast.makeText(parent.getContext(),
	// // "OnItemSelectedListener : " +
	// // parent.getItemAtPosition(pos).toString(),
	// // Toast.LENGTH_SHORT).show();
	//
	// }
	//
	// @Override
	// public void onNothingSelected(AdapterView<?> arg0) {
	// // TODO Auto-generated method stub
	// }
	//
	// }

	// public void titleDialog(final TaskModel task) {
	// View view = this.getLayoutInflater().inflate(R.layout.edittext, null);
	// final EditText et_title = (EditText) view.findViewById(R.id.edit_text);
	// et_title.setText(task.title);
	// DialogInterface.OnClickListener posListener = new
	// DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.cancel();
	// // save the new task title
	// String title = et_title.getText().toString();
	// task.title = title;
	// }
	// };
	// DialogInterface.OnClickListener negListener = new
	// DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.cancel();
	// }
	// };
	// Common.CustomDialog.CustomDialog(this, view, negListener, posListener,
	// R.string.dialog_save, R.string.dialog_cancel);
	//
	// // Common.CustomDialog.CustomDialog(this, view);
	// // AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// // builder.setTitle("Reminder");
	// // builder.show();
	// }

}
