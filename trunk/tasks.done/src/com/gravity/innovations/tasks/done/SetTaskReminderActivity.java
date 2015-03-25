package com.gravity.innovations.tasks.done;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TimePicker.OnTimeChangedListener;

public class SetTaskReminderActivity extends ActionBarActivity {
	private TaskModel task;
	private TaskListModel list;
	private Switch _switch;
	private boolean isSwitchChecked = false, is24HourFormat = false;
	private Context mContext;
	private ActionBar mActionBar;
	private EditText et_title, et_details, et_notes;
	private DatabaseHelper db;
	private SimpleDateFormat formatter;
	private TextView tv_timeStart, tv_timeEnd, tv_dateStart, tv_dateEnd,
			tv_repeat, tv_notification2, tv_notification3, tv_notification4,
			tv_notification, tv_notification_addAnother;
	private int doesNotRepeat = 0, daily = 1, weekly = 2, monthly = 3,
			yearly = 4, custom = 5;
	private int globalInterval;
	private String globalRepeatValueString, globalNumberOfEvents;
	private Calendar calendar_StartDateOnly, calendar_EndDateOnly,
			calendar_StartTimeOnly, calendar_EndTimeOnly, calendar_DateTime,
			calendar_RepeatDialogDateOnly;
	private int resourceID_tvDateStart, resourceID_tvDateEnd,
			resourceID_tvTimeStart, resourceID_tvTimeEnd,
			resourceID_tvDateRepeatDialog;

	private boolean isMonday = false, isTuesday = false, isWednesday = false,
			isThursday = false, isFriday = false, isSaturday = false,
			isSunday = false, isDaily = true/* default */, isWeekly = false,
			isMonthly = false, isYearly = false, isForever = true/* default */,
			isUntilAtime = false, isForAFixedNumberOfEvents = false;

	private String weekdays;

	private ArrayList<TextView> arrayList_TextView = new ArrayList<TextView>();
	// get Custom String and assign and update db when needed
	private ArrayList<String> notificationDetailsList = new ArrayList<String>();
	// id for the new textViews
	private int textViewTag = 2;// was 3 before
	// textView stoping condition
	private int textViewFlag = 5;

	private String timeUnit = null, notificationAsEmail = null,
			checkAsEmail = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_task_reminder);
		mContext = getApplicationContext();
		is24HourFormat = DateFormat.is24HourFormat(mContext);
		Intent intent = getIntent();
		Bundle mBundle = intent.getExtras();
		mBundle.getSerializable("key_list");
		mBundle.getSerializable("key_task");
		list = (TaskListModel) intent.getSerializableExtra("key_list");
		task = (TaskModel) intent.getSerializableExtra("key_task");
		// widgets declaration
		setUpViewsWithData();
	}

	@SuppressLint("NewApi")
	private void setUpViewsWithData() {
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

		resourceID_tvTimeStart = tv_timeStart.getId();
		resourceID_tvTimeEnd = tv_timeEnd.getId();
		resourceID_tvDateEnd = tv_dateEnd.getId();
		resourceID_tvDateStart = tv_dateStart.getId();

		long currentTimeMillis = System.currentTimeMillis();
		tv_dateStart.setText(getCurrentDate(currentTimeMillis));
		tv_dateEnd.setText(getCurrentDate(currentTimeMillis));
		tv_timeStart.setText(getCurrentTime(currentTimeMillis));
		tv_timeEnd.setText(getCurrentTimePlusOneHour(currentTimeMillis));

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
		tv_repeat = (TextView) findViewById(R.id.repeat_tv);

		tv_repeat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				repeatDialog(tv_repeat);
			}
		});

		tv_dateStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// open openCustomTimepickerDialog dialog
				// openCalenderViewDialogStartDate();
				getDate(tv_dateStart);// openGetSetDateDialog()

			}
		});
		tv_dateEnd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// openCalenderViewDialogEndDate();
				getDate(tv_dateEnd);// openGetSetDateDialog()
			}
		});

		tv_timeStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// open openCustomTimepickerDialog dialog
				openCustomTimepickerDialog(tv_timeStart);

			}
		});

		tv_timeEnd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// open openCustomTimepickerDialog dialog
				openCustomTimepickerDialog(tv_timeEnd);
			}
		});

		tv_notification = (TextView) findViewById(R.id.add_notification_btn);
		tv_notification_addAnother = (TextView) findViewById(R.id.add_another_notification_btn);

		tv_notification.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				notificationTimeSelectionDialog(0);
			}
		});

		arrayList_TextView = new ArrayList<TextView>();
		// tags are assigned in xml
		arrayList_TextView.add(tv_notification);
		arrayList_TextView.add(tv_notification_addAnother);

		tv_notification_addAnother
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (textViewFlag == 6) {
							notificationTimeSelectionDialog(1);
						}
						if (arrayList_TextView.size() < 5) {
							final TextView newTextView = new TextView(
									getApplicationContext());
							newTextView
									.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											notificationTimeSelectionDialog(Integer
													.valueOf(v.getTag()
															.toString()));
											/*
											 * newTextView.setText(
											 * radioDialog_GetText());
											 */
										}
									});
							addANewTextView(newTextView);
							arrayList_TextView.add(newTextView);
						}
						if (arrayList_TextView.size() == 5 && textViewFlag == 5) {
							tv_notification_addAnother
									.setText("Notification Time");
							textViewFlag++;
						}
					}
				});

	}

	private void notificationTimeSelectionDialog(final int tag) {
		// it has to be radio dialog try think on that as well
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] list_items = { "No notification", "At time of event",
				"10 minutes before", "30 minutes before", "1 hour before",
				"Custom..." };
		builder.setItems(list_items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					if (tag == 0) {
						tv_notification.setText("No Notification");
					} else if (tag == 1) {
						tv_notification_addAnother.setText("No Notification");
					} else if (tag == 2) {
						tv_notification2.setText("No Notification");
					} else if (tag == 3) {
						tv_notification3.setText("No Notification");
					} else if (tag == 4) {
						tv_notification4.setText("No Notification");
					}
				} else if (which == 1) {
					if (tag == 0) {
						tv_notification.setText("At time of event");
					} else if (tag == 1) {
						tv_notification_addAnother.setText("At time of event");
					} else if (tag == 2) {
						tv_notification2.setText("At time of event");
					} else if (tag == 3) {
						tv_notification3.setText("At time of event");
					} else if (tag == 4) {
						tv_notification4.setText("At time of event");
					}
				} else if (which == 2) {
					if (tag == 0) {
						tv_notification.setText("10 minutes before");
					} else if (tag == 1) {
						tv_notification_addAnother.setText("10 minutes before");
					} else if (tag == 2) {
						tv_notification2.setText("10 minutes before");
					} else if (tag == 3) {
						tv_notification3.setText("10 minutes before");
					} else if (tag == 4) {
						tv_notification4.setText("10 minutes before");
					}
					// tv_notification.setText("10 minutes before");
				} else if (which == 3) {
					if (tag == 0) {
						tv_notification.setText("30 minutes before");
					} else if (tag == 1) {
						tv_notification_addAnother.setText("30 minutes before");
					} else if (tag == 2) {
						tv_notification2.setText("30 minutes before");
					} else if (tag == 3) {
						tv_notification3.setText("30 minutes before");
					} else if (tag == 4) {
						tv_notification4.setText("30 minutes before");
					}
					// tv_notification.setText("30 minutes before");
				} else if (which == 4) {
					if (tag == 0) {
						tv_notification.setText("1 hour before");
					} else if (tag == 1) {
						tv_notification_addAnother.setText("1 hour before");
					} else if (tag == 2) {
						tv_notification2.setText("1 hour before");
					} else if (tag == 3) {
						tv_notification3.setText("1 hour before");
					} else if (tag == 4) {
						tv_notification4.setText("1 hour before");
					}
					// tv_notification.setText("1 hour before");
				} else if (which == 5) {
					// notificationDialog();
					// it should return a string that shall be used as setText
					customNotificationTimeSelectionDialog(tag);
				}
			}
		});
		builder.show();
	}

	private void customNotificationTimeSelectionDialog(final int tag) {
		View view = this.getLayoutInflater().inflate(
				R.layout.dialog_notifications_details, null);
		final EditText nTimes = (EditText) view
				.findViewById(R.id.et_numberOfNotifications);
		final RadioGroup rg_TimeUnit = (RadioGroup) view
				.findViewById(R.id.myRadioGroup);
		final RadioGroup rg_NotificationOrEmail = (RadioGroup) view
				.findViewById(R.id.myRadioGroup2);

		nTimes.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

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

					if (num == 0) {
						btn0.setText("Minute");
						btn1.setText("Hour");
						btn2.setText("Day");
						btn3.setText("Week");
					} else if (num == 1) {
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
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				String nTimesString = nTimes.getText().toString();
				if (rg_TimeUnit.getCheckedRadioButtonId() != -1) {
					int id = rg_TimeUnit.getCheckedRadioButtonId();
					View radioButton = rg_TimeUnit.findViewById(id);
					int radioId = rg_TimeUnit.indexOfChild(radioButton);
					RadioButton btn = (RadioButton) rg_TimeUnit
							.getChildAt(radioId);
					String selection = (String) btn.getText();
					timeUnit = " " + selection;
				}
				if (rg_NotificationOrEmail.getCheckedRadioButtonId() != -1) {
					int id = rg_NotificationOrEmail.getCheckedRadioButtonId();
					View radioButton = rg_NotificationOrEmail.findViewById(id);
					int radioId = rg_NotificationOrEmail
							.indexOfChild(radioButton);
					RadioButton btn = (RadioButton) rg_NotificationOrEmail
							.getChildAt(radioId);
					String selection = (String) btn.getText();
					notificationAsEmail = selection;
				}
				if (notificationAsEmail == "As Email"
						|| notificationAsEmail.contains("As Email")) {
					if (tag == 0) {
						tv_notification.setText(nTimesString + timeUnit
								+ " before " + "as email");
					} else if (tag == 1) {
						tv_notification_addAnother.setText(nTimesString
								+ timeUnit + " before " + "as email");
					} else if (tag == 2) {
						tv_notification2.setText(nTimesString + timeUnit
								+ " before " + "as email");
					} else if (tag == 3) {
						tv_notification3.setText(nTimesString + timeUnit
								+ " before " + "as email");
					} else if (tag == 4) {
						tv_notification4.setText(nTimesString + timeUnit
								+ " before " + "as email");
					}

				} else if (notificationAsEmail != "Email") {
					if (tag == 0) {
						tv_notification.setText(nTimesString + timeUnit
								+ " before");
					} else if (tag == 1) {
						tv_notification_addAnother.setText(nTimesString
								+ timeUnit + " before");
					} else if (tag == 2) {
						tv_notification2.setText(nTimesString + timeUnit
								+ " before");
					} else if (tag == 3) {
						tv_notification3.setText(nTimesString + timeUnit
								+ " before");
					} else if (tag == 4) {
						tv_notification4.setText(nTimesString + timeUnit
								+ " before");
					}
				}
			}
		};
		Common.CustomDialog.CustomDialogWithRadio(this, view, posListener,
				R.string.dialog_done);
	}

	private void repeatDialog(final TextView tv) {
		// final TaskListModel tasklist, final TaskModel temp) {
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
					tv.setTag("5");
					repeatDetailsDialog();
				}
			}
		});
		builder.setTitle("Reminder");
		builder.show();
	}

	@SuppressLint("NewApi")
	private void repeatDetailsDialog() {
		final View dialog_detailsView = this.getLayoutInflater().inflate(
				R.layout.dialog_repeat_details, null);
		View dialog_headerView = this.getLayoutInflater().inflate(
				R.layout.dialog_repeat_details_header, null);
		// Days Buttons
		final Button sunday_btn, monday_btn, tuesday_btn, wednesday_btn, thursday_btn, friday_btn, saturday_btn;
		final TextView openCalender_btn;

		sunday_btn = (Button) dialog_detailsView.findViewById(R.id.btn_sunday);
		monday_btn = (Button) dialog_detailsView.findViewById(R.id.btn_monday);
		tuesday_btn = (Button) dialog_detailsView
				.findViewById(R.id.btn_tuesday);
		wednesday_btn = (Button) dialog_detailsView
				.findViewById(R.id.btn_wednesday);
		thursday_btn = (Button) dialog_detailsView
				.findViewById(R.id.btn_thursday);
		friday_btn = (Button) dialog_detailsView.findViewById(R.id.btn_friday);
		saturday_btn = (Button) dialog_detailsView
				.findViewById(R.id.btn_saturday);

		openCalender_btn = (TextView) dialog_detailsView
				.findViewById(R.id.tv_open_calender);
		resourceID_tvDateRepeatDialog = openCalender_btn.getId();
		openCalender_btn.setText(getDate(System.currentTimeMillis()));
		calendar_RepeatDialogDateOnly = Calendar.getInstance();
		calendar_RepeatDialogDateOnly.setTimeInMillis(System
				.currentTimeMillis());// if the user saves with selecting a date

		// repeatDialogDate = getCurrentDate(System.currentTimeMillis());

		final View viewline = (View) dialog_detailsView
				.findViewById(R.id.line_et_spinner_number_of_events);

		final EditText spinnerValue_et = (EditText) dialog_detailsView
				.findViewById(R.id.et_spinner_number_of_events);

		final EditText et1 = (EditText) dialog_detailsView
				.findViewById(R.id.et1);

		final TextView DoWoMoY = (TextView) dialog_detailsView
				.findViewById(R.id.tv2); // DaysorWeekorMonths/Years

		et1.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if ((et1.getText().toString()).isEmpty()
						|| (et1.getText().toString()) == ""
						|| (et1.getText().toString()) == " ") {
					DoWoMoY.setText("Day");
				} else if (!(et1.getText().toString()).isEmpty()) {
					int num = Integer.valueOf((et1.getText().toString()));

					if (num == 0) {
						DoWoMoY.setText("Day");
					} else if (num == 1) {
						DoWoMoY.setText("Day");
					} else if (num > 1) {
						DoWoMoY.setText("Days");
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
				// TODO Auto-generated method stub
			}
		});

		// final EditText final_spinnerValue_et = spinnerValue_et;
		// final EditText final_et1 = et1;

		final TextView spinnerValue_tv = (TextView) dialog_detailsView
				.findViewById(R.id.tv_spinnerValue);

		spinnerValue_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if ((spinnerValue_et.getText().toString()).isEmpty()
						|| (spinnerValue_et.getText().toString()) == ""
						|| (spinnerValue_et.getText().toString()) == " ") {
					spinnerValue_tv.setText("event");
				} else if (!(spinnerValue_et.getText().toString()).isEmpty()) {
					int num = Integer.valueOf((spinnerValue_et.getText()
							.toString()));

					if (num == 0) {
						spinnerValue_tv.setText("event");
					} else if (num == 1) {
						spinnerValue_tv.setText("event");
					} else if (num > 1) {
						spinnerValue_tv.setText("events");
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
				// TODO Auto-generated method stub

			}
		});

		final Spinner spinner_header = (Spinner) dialog_headerView
				.findViewById(R.id.repeatInterval_spinner);
		addItemsToHeaderSpinner(spinner_header);

		spinner_header.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// String selectedItem =
				// parent.getItemAtPosition(position).toString();
				if (position == 0) {
					// daily
					sunday_btn.setVisibility(View.GONE);
					monday_btn.setVisibility(View.GONE);
					tuesday_btn.setVisibility(View.GONE);
					wednesday_btn.setVisibility(View.GONE);
					thursday_btn.setVisibility(View.GONE);
					friday_btn.setVisibility(View.GONE);
					saturday_btn.setVisibility(View.GONE);
					// DoWoMoY.setText("Day(s)");
					isDaily = true;
					isWeekly = false;
					isMonthly = false;
					isYearly = false;

				} else if (position == 1) {
					// weekly
					sunday_btn.setVisibility(View.VISIBLE);
					monday_btn.setVisibility(View.VISIBLE);
					tuesday_btn.setVisibility(View.VISIBLE);
					wednesday_btn.setVisibility(View.VISIBLE);
					thursday_btn.setVisibility(View.VISIBLE);
					friday_btn.setVisibility(View.VISIBLE);
					saturday_btn.setVisibility(View.VISIBLE);
					// DoWoMoY.setText("Week(s)");

					isDaily = false;
					isWeekly = true;
					isMonthly = false;
					isYearly = false;
				} else if (position == 2) {
					// monthly
					sunday_btn.setVisibility(View.GONE);
					monday_btn.setVisibility(View.GONE);
					tuesday_btn.setVisibility(View.GONE);
					wednesday_btn.setVisibility(View.GONE);
					thursday_btn.setVisibility(View.GONE);
					friday_btn.setVisibility(View.GONE);
					saturday_btn.setVisibility(View.GONE);
					// DoWoMoY.setText("Month(s)");
					isDaily = false;
					isWeekly = false;
					isMonthly = true;
					isYearly = false;
				} else if (position == 3) {
					// yearly
					sunday_btn.setVisibility(View.GONE);
					monday_btn.setVisibility(View.GONE);
					tuesday_btn.setVisibility(View.GONE);
					wednesday_btn.setVisibility(View.GONE);
					thursday_btn.setVisibility(View.GONE);
					friday_btn.setVisibility(View.GONE);
					saturday_btn.setVisibility(View.GONE);
					// DoWoMoY.setText("Year(s)");
					isDaily = false;
					isWeekly = false;
					isMonthly = false;
					isYearly = true;
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		// Dialog HeaderView

		// Spinner dialog_detailsView
		final Spinner spinner = (Spinner) dialog_detailsView
				.findViewById(R.id.spinner_repeatTimes);
		addItemsToSpinner(spinner);
		// final Spinner final_spinner = spinner;
		// addListenerOnSpinnerItemSelection(spinner);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// String selectedItem =
				// parent.getItemAtPosition(position).toString();

				if (position == 0) {
					openCalender_btn.setVisibility(View.GONE);
					viewline.setVisibility(View.GONE);
					spinnerValue_tv.setVisibility(View.GONE);
					spinnerValue_et.setVisibility(View.GONE);
					isForever = true;
					isUntilAtime = false;
					isForAFixedNumberOfEvents = false;
				} else if (position == 1) {
					openCalender_btn.setVisibility(View.VISIBLE);
					viewline.setVisibility(View.GONE);
					spinnerValue_tv.setVisibility(View.GONE);
					spinnerValue_et.setVisibility(View.GONE);
					isForever = false;
					isUntilAtime = true;
					isForAFixedNumberOfEvents = false;
				} else if (position == 2) {
					openCalender_btn.setVisibility(View.GONE);
					viewline.setVisibility(View.VISIBLE);
					spinnerValue_tv.setVisibility(View.VISIBLE);
					spinnerValue_et.setVisibility(View.VISIBLE);
					isForever = false;
					isUntilAtime = false;
					isForAFixedNumberOfEvents = true;
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
				// Toast.makeText(getApplicationContext(),
				// "open CalView here again", Toast.LENGTH_SHORT).show();
				getDate(openCalender_btn);

			}
		});

		sunday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					sunday_btn
							.setBackgroundResource(R.drawable.circle_blue_full);
					flag = true;
					isSunday = true;

				} else {
					sunday_btn.setBackgroundResource(R.drawable.circle_blue);
					flag = false;
					isSunday = false;
				}

			}
		});
		monday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					monday_btn
							.setBackgroundResource(R.drawable.circle_blue_full);
					flag = true;
					isMonday = true;

				} else {
					monday_btn.setBackgroundResource(R.drawable.circle_blue);
					flag = false;
					isMonday = false;
				}

			}
		});
		tuesday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					tuesday_btn
							.setBackgroundResource(R.drawable.circle_blue_full);
					flag = true;
					isTuesday = true;

				} else {
					tuesday_btn.setBackgroundResource(R.drawable.circle_blue);
					flag = false;
					isTuesday = false;
				}
			}
		});
		wednesday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					wednesday_btn
							.setBackgroundResource(R.drawable.circle_blue_full);
					flag = true;
					isWednesday = true;

				} else {
					wednesday_btn.setBackgroundResource(R.drawable.circle_blue);
					flag = false;
					isWednesday = false;
				}
			}
		});
		thursday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					thursday_btn
							.setBackgroundResource(R.drawable.circle_blue_full);
					flag = true;
					isThursday = true;

				} else {
					thursday_btn.setBackgroundResource(R.drawable.circle_blue);
					flag = false;
					isThursday = false;
				}
			}
		});
		friday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					friday_btn
							.setBackgroundResource(R.drawable.circle_blue_full);
					flag = true;
					isFriday = true;

				} else {
					friday_btn.setBackgroundResource(R.drawable.circle_blue);
					flag = false;
					isFriday = false;
				}
			}
		});
		saturday_btn.setOnClickListener(new View.OnClickListener() {
			boolean flag = false;

			@Override
			public void onClick(View v) {
				if (!flag) {
					saturday_btn
							.setBackgroundResource(R.drawable.circle_blue_full);
					flag = true;
					isSaturday = true;

				} else {
					saturday_btn.setBackgroundResource(R.drawable.circle_blue);
					flag = false;
					isSaturday = false;
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
					dialog_detailsView.setAlpha(1);
					// rootView2.setBackgroundColor(Color.TRANSPARENT);
					// rootView2.setEnabled(true);
					sunday_btn.setEnabled(true);
					monday_btn.setEnabled(true);
					tuesday_btn.setEnabled(true);
					wednesday_btn.setEnabled(true);
					thursday_btn.setEnabled(true);
					friday_btn.setEnabled(true);
					saturday_btn.setEnabled(true);
					// spinnerValue.setEnabled(true);
					spinnerValue_tv.setEnabled(true);
					spinnerValue_et.setEnabled(true);
					et1.setEnabled(true);
					spinner.setEnabled(true);
					spinner_header.setEnabled(true);
					openCalender_btn.setEnabled(true);

				} else {
					dialog_detailsView.setAlpha((float) 0.5);// 0=unvisible
					sunday_btn.setEnabled(false);
					monday_btn.setEnabled(false);
					tuesday_btn.setEnabled(false);
					wednesday_btn.setEnabled(false);
					thursday_btn.setEnabled(false);
					friday_btn.setEnabled(false);
					saturday_btn.setEnabled(false);
					// spinnerValue.setEnabled(false);
					spinnerValue_tv.setEnabled(false);
					spinnerValue_et.setEnabled(false);
					et1.setEnabled(false);
					spinner.setEnabled(false);
					spinner_header.setEnabled(false);
					openCalender_btn.setEnabled(false);
				}
			}
		});
		// Switch header

		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				int interval;
				if (et1.getText().toString() == " "
						|| et1.getText().toString() == ""
						|| et1.getText().toString().isEmpty()
						|| et1.getText().toString() == "0"
						|| et1.getText().toString() == "00"
						|| et1.getText().toString() == "01") {
					// error handling
					interval = 1;
					globalInterval = interval;
				} else {
					interval = Integer.valueOf(et1.getText().toString());
					globalInterval = interval;
				}
				if (isDaily) {
					if (isForever) {
						// make a golbal String
						if (interval == 1) {
							globalRepeatValueString = "Every day";
						} else if (interval > 1) {
							globalRepeatValueString = "Repeat every "
									+ interval + " days";
						}
					} else if (isUntilAtime) {
						if (interval == 1) {
							globalRepeatValueString = "Repeat every day, until "
									+ openCalender_btn.getText().toString();
						} else {
							globalRepeatValueString = "Repeat every "
									+ interval + " days, until "
									+ openCalender_btn.getText().toString();
						}
					} else if (isForAFixedNumberOfEvents) {
						int numOfEvents = Integer.valueOf(spinnerValue_et
								.getText().toString());
						globalNumberOfEvents = String.valueOf(numOfEvents);
						if (interval == 1 && numOfEvents == 1) {
							globalRepeatValueString = "Repeat every day, for one time";
						} else if (interval > 1) {
							globalRepeatValueString = "Repeat every "
									+ interval + " days, for " + numOfEvents
									+ " events";
						}
					}
				} else if (isWeekly) {
					ArrayList<String> weekdaysSelected = new ArrayList<String>();
					if (isMonday) {
						weekdaysSelected.add("Mon");
					}
					if (isTuesday) {
						weekdaysSelected.add("Tue");
					}
					if (isWednesday) {
						weekdaysSelected.add("Wed");
					}
					if (isThursday) {
						weekdaysSelected.add("Thu");
					}
					if (isFriday) {
						weekdaysSelected.add("Fri");
					}
					if (isSaturday) {
						weekdaysSelected.add("Sat");
					}
					if (isSunday) {
						weekdaysSelected.add("Sun");
					}

					if (weekdaysSelected.toString() != null
							|| !weekdaysSelected.toString().isEmpty()) {
						StringTokenizer tokens = new StringTokenizer(
								weekdaysSelected.toString(), "[");
						String weekdays1 = tokens.nextToken();
						StringTokenizer tokens2 = new StringTokenizer(
								weekdays1, "]");
						weekdays = tokens2.nextToken();
						// keep one day as selected default
					}
					if (isForever) {
						if (interval == 1) {
							globalRepeatValueString = "Repeats weekly on "
									+ weekdays;
						} else {
							globalRepeatValueString = "Repeats every "
									+ globalInterval + " weeks on " + weekdays;
						}
					}
					if (isUntilAtime) {
						if (interval == 1) {
							globalRepeatValueString = "Repeats weekly until "
									+ openCalender_btn.getText().toString();
						} else {
							globalRepeatValueString = "Repeats every "
									+ globalInterval + "weeks until "
									+ openCalender_btn.getText().toString();
						}
					}
					if (isForAFixedNumberOfEvents) {
						if (interval == 1) {
							globalRepeatValueString = "Repeat every week, for one time";
						} else {
							int numOfEvents = Integer.valueOf(spinnerValue_et
									.getText().toString());
							globalRepeatValueString = "Repeat every "
									+ interval + " weeks, for " + numOfEvents
									+ " events";
						}
					}

				} else if (isMonthly) {
					if (isForever) {
						if (interval == 1) {
							globalRepeatValueString = "Repeat every month";
						} else {
							globalRepeatValueString = "Repeat every "
									+ interval + " months";
						}
					} else if (isUntilAtime) {
						if (interval == 1) {
							globalRepeatValueString = "Repeat every month, until "
									+ openCalender_btn.getText().toString();
						} else {
							globalRepeatValueString = "Repeat every "
									+ interval + " months, until "
									+ openCalender_btn.getText().toString();
						}
					} else if (isForAFixedNumberOfEvents) {
						if (interval == 1) {
							globalRepeatValueString = "Repeat every month, for one time";
						} else {
							int numOfEvents = Integer.valueOf(spinnerValue_et
									.getText().toString());
							globalRepeatValueString = "Repeat every "
									+ interval + " months, for " + numOfEvents
									+ " events";
						}
					}
				} else if (isYearly) {
					if (isForever) {
						if (interval == 1) {
							globalRepeatValueString = "Repeat every year";
						} else {
							globalRepeatValueString = "Repeat every "
									+ interval + " years";
						}

					} else if (isUntilAtime) {
						if (interval == 1) {
							globalRepeatValueString = "Repeat every year, until "
									+ openCalender_btn.getText().toString();
						} else {
							globalRepeatValueString = "Repeat every "
									+ interval + " years, until "
									+ openCalender_btn.getText().toString();
						}
					} else if (isForAFixedNumberOfEvents) {
						if (interval == 1) {
							globalRepeatValueString = "Repeat every year, for one time";
						} else {
							int numOfEvents = Integer.valueOf(spinnerValue_et
									.getText().toString());
							globalRepeatValueString = "Repeat every "
									+ interval + " years, for " + numOfEvents
									+ " events";
						}
					}
				}

				tv_repeat.setText(globalRepeatValueString);
			}
		};
		Common.CustomDialog.CustomDialog(this, dialog_detailsView,
				dialog_headerView, posListener, R.string.dialog_done);
	}

	private void addItemsToSpinner(Spinner spinner) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.repeatTill_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	private void addItemsToHeaderSpinner(Spinner spinner) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.repeatInterval_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	@SuppressLint("NewApi")
	public void openCustomTimepickerDialog(final TextView currentTextView) {

		View dialog_detailsView = this.getLayoutInflater().inflate(
				R.layout.dialog_timepicker, null);
		final TimePicker timePicker = (TimePicker) dialog_detailsView
				.findViewById(R.id.timepicker);
		// c_StartTimeOnly = Calendar.getInstance();
		// c_EndTimeOnly = Calendar.getInstance();
		if (is24HourFormat) {
			timePicker.setIs24HourView(true);
		}
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

				if (currentTextView.getId() == resourceID_tvTimeStart) {
					calendar_StartTimeOnly.set(Calendar.HOUR_OF_DAY, hourOfDay);
					calendar_StartTimeOnly.set(Calendar.MINUTE, minute);
				} else if (currentTextView.getId() == resourceID_tvTimeEnd) {
					calendar_EndTimeOnly.set(Calendar.HOUR_OF_DAY, hourOfDay);
					calendar_EndTimeOnly.set(Calendar.MINUTE, minute);
				}

			}
		});
		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				if (currentTextView.getId() == resourceID_tvTimeStart) {
					currentTextView.setText(geTime(calendar_StartTimeOnly
							.getTimeInMillis()));
				} else if (currentTextView.getId() == resourceID_tvTimeEnd) {
					currentTextView.setText(geTime(calendar_EndTimeOnly
							.getTimeInMillis()));
				}

			}
		};
		Common.CustomDialog.CustomDialog(this, dialog_detailsView, posListener,
				R.string.dialog_done);
	}

	@SuppressLint("NewApi")
	private void getDate(final TextView currentTextView) {
		View dialog_headerView = this.getLayoutInflater().inflate(
				R.layout.dialog_calenderview_header, null);
		View dialog_detailsView = this.getLayoutInflater().inflate(
				R.layout.dialog_calenderview, null);
		// "EEEE, MMM dd, yyyy"
		StringTokenizer tokens = new StringTokenizer(
				getDate(System.currentTimeMillis()), ",");
		String weekday = tokens.nextToken();
		String monthAndDay = tokens.nextToken();
		String year = tokens.nextToken();
		StringTokenizer token2 = new StringTokenizer(monthAndDay, " ");
		String month = token2.nextToken();
		String day = token2.nextToken();
		final TextView tv_year, tv_date, tv_month, tv_weekday;

		tv_weekday = (TextView) dialog_headerView.findViewById(R.id.tv_day);
		tv_weekday.setText(weekday);
		tv_date = (TextView) dialog_headerView.findViewById(R.id.tv_date);
		tv_date.setText(day);
		tv_month = (TextView) dialog_headerView.findViewById(R.id.tv_month);
		tv_month.setText(month);
		tv_year = (TextView) dialog_headerView.findViewById(R.id.tv_year);
		tv_year.setText(year);

		Long today = System.currentTimeMillis();
		CalendarView calendarView = (CalendarView) dialog_detailsView
				.findViewById(R.id.calenderView);
		calendar_RepeatDialogDateOnly = Calendar.getInstance();
		calendarView.setDate(today, true, true);// we can set cal object here
		calendarView
				.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
					public void onSelectedDayChange(CalendarView view,
							int year, int month, int dayOfMonth) {

						if (currentTextView.getId() == resourceID_tvDateStart) {
							calendar_StartDateOnly.set(Calendar.YEAR, year);
							calendar_StartDateOnly.set(Calendar.MONTH, month);
							calendar_StartDateOnly.set(Calendar.DAY_OF_MONTH,
									dayOfMonth);
							calendar_StartDateOnly.set(Calendar.HOUR, 00);
							calendar_StartDateOnly.set(Calendar.MINUTE, 00);
							// c_StartDateOnly = c;

							String monthString = calendar_StartDateOnly.getDisplayName(
									Calendar.MONTH, Calendar.SHORT,
									Locale.getDefault());
							tv_year.setText(String.valueOf(year));
							tv_date.setText(String.valueOf(dayOfMonth));

							tv_month.setText(monthString);

							String weekdayString = calendar_StartDateOnly
									.getDisplayName(Calendar.DAY_OF_WEEK,
											Calendar.LONG, Locale.getDefault());

							tv_weekday.setText(weekdayString);

						} else if (currentTextView.getId() == resourceID_tvDateEnd) {
							calendar_EndDateOnly.set(Calendar.YEAR, year);
							calendar_EndDateOnly.set(Calendar.MONTH, month);
							calendar_EndDateOnly.set(Calendar.DAY_OF_MONTH,
									dayOfMonth);
							calendar_EndDateOnly.set(Calendar.HOUR, 00);
							calendar_EndDateOnly.set(Calendar.MINUTE, 00);

							// c_EndDateOnly = c;
							String monthString = calendar_EndDateOnly.getDisplayName(
									Calendar.MONTH, Calendar.SHORT,
									Locale.getDefault());
							tv_year.setText(String.valueOf(year));
							tv_date.setText(String.valueOf(dayOfMonth));

							tv_month.setText(monthString);

							String weekdayString = calendar_EndDateOnly
									.getDisplayName(Calendar.DAY_OF_WEEK,
											Calendar.LONG, Locale.getDefault());

							tv_weekday.setText(weekdayString);

						} else if (currentTextView.getId() == resourceID_tvDateRepeatDialog) {
							calendar_RepeatDialogDateOnly.set(Calendar.YEAR,
									year);
							calendar_RepeatDialogDateOnly.set(Calendar.MONTH,
									month);
							calendar_RepeatDialogDateOnly.set(
									Calendar.DAY_OF_MONTH, dayOfMonth);
							calendar_RepeatDialogDateOnly
									.set(Calendar.HOUR, 00);
							calendar_RepeatDialogDateOnly.set(Calendar.MINUTE,
									00);

							String monthString = calendar_RepeatDialogDateOnly.getDisplayName(
									Calendar.MONTH, Calendar.SHORT,
									Locale.getDefault());
							tv_year.setText(String.valueOf(year));
							tv_date.setText(String.valueOf(dayOfMonth));
							tv_month.setText(monthString);
							String weekdayString = calendar_RepeatDialogDateOnly
									.getDisplayName(Calendar.DAY_OF_WEEK,
											Calendar.LONG, Locale.getDefault());

							tv_weekday.setText(weekdayString);
						}

					}
				});
		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				if (currentTextView.getId() == resourceID_tvDateStart) {
					calendar_StartDateOnly
							.setTimeInMillis(calendar_StartDateOnly
									.getTimeInMillis());
					currentTextView.setText(getDate(calendar_StartDateOnly
							.getTimeInMillis()));
				} else if (currentTextView.getId() == resourceID_tvDateEnd) {
					calendar_EndDateOnly.setTimeInMillis(calendar_EndDateOnly
							.getTimeInMillis());
					currentTextView.setText(getDate(calendar_EndDateOnly
							.getTimeInMillis()));
				} else if (currentTextView.getId() == resourceID_tvDateRepeatDialog) {
					currentTextView
							.setText(getDate(calendar_RepeatDialogDateOnly
									.getTimeInMillis()));
				}

			}
		};
		Common.CustomDialog.CustomDialog(this, dialog_detailsView,
				dialog_headerView, posListener, R.string.dialog_done);
		// long longDate = c_RepeatDialogDateOnly.getTimeInMillis();
		// return Long.toString(longDate);
	}

	private String geTime(long millis) {
		String currentTime = null, formateStyle = null;
		if (is24HourFormat) {
			formateStyle = "HH:mm";// 24 hours
		} else if (!is24HourFormat) {
			formateStyle = "hh:mm aa";// 12 hours
		}
		formatter = new SimpleDateFormat(formateStyle);
		currentTime = formatter.format(millis);
		return currentTime;
	}

	private String getDate(long millis) {
		String currentDate = null;
		formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy");
		currentDate = formatter.format(millis);
		return currentDate;
	}

	// Setting up initial time ontextViews//
	private String getCurrentDate(long millis) {
		// this will run only first time
		String currentDate = null;
		formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy");
		currentDate = formatter.format(millis);
		// initializing because if the user directly saves then
		calendar_StartDateOnly = Calendar.getInstance();
		calendar_StartDateOnly.setTimeInMillis(millis);
		calendar_EndDateOnly = Calendar.getInstance();
		calendar_EndDateOnly.setTimeInMillis(millis);
		// initializing because if the user directly saves then
		return currentDate;
	}

	private String getCurrentTime(long millis) {
		String currentTime = null, formateStyle = null;
		if (is24HourFormat) {
			formateStyle = "HH:mm";// 24 hours
		} else if (!is24HourFormat) {
			formateStyle = "hh:mm aa";// 12 hours
		}
		formatter = new SimpleDateFormat(formateStyle);
		currentTime = formatter.format(millis);

		// initializing because if the user directly saves then
		calendar_StartTimeOnly = Calendar.getInstance();
		calendar_StartTimeOnly.setTimeInMillis(millis);
		// initializing because if the user directly saves then

		return currentTime;
	}

	private String getCurrentTimePlusOneHour(long millis) {
		millis = millis + (3600000); // currentTime plusOneHour
		String currentTime = null, formateStyle = null;
		if (is24HourFormat) {
			formateStyle = "HH:mm";// 24 hours
		} else if (!is24HourFormat) {
			formateStyle = "hh:mm aa";// 12 hours
		}
		formatter = new SimpleDateFormat(formateStyle);
		currentTime = formatter.format(millis);
		// initializing because if the user directly saves then
		calendar_EndTimeOnly = Calendar.getInstance();
		calendar_EndTimeOnly.setTimeInMillis(millis);
		// initializing because if the user directly saves then
		return currentTime;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// decide what to show in the action bar.
		getMenuInflater().inflate(R.menu.save, menu);
		restoreActionBar();
		// return true;

		return super.onCreateOptionsMenu(menu);
	}

	public void restoreActionBar() {
		mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		mActionBar.setDisplayShowTitleEnabled(true);

		mActionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(list.fragmentColor)));

		mActionBar.setTitle("Task Details");
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
			updateDatabase();
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateDatabase() {
		db = new DatabaseHelper(mContext);
		db.tasks.Edit(getTaskModel());
		/******** Task edit ************/
		db.taskRepeat.Add(getRepeatTaskModel());
		/******** RepeatModel ************/
		ArrayList<RepeatTaskNotificationsModel> list = getNotificaitonsModelsList(task._id);
		for (RepeatTaskNotificationsModel temp : list) {
			db.taskRepeat_Notifications.Add(temp);
		}
		/******** RepeatTaskNotificationsModel ************/
	}

	private TaskModel getTaskModel() {
		task.title = et_title.getText().toString();
		task.details = et_details.getText().toString();
		task.notes = et_notes.getText().toString();
		return task;
	}

	private RepeatTaskModel getRepeatTaskModel() {
		int tag = getTag();
		RepeatTaskModel repeatModel = new RepeatTaskModel();
		repeatModel.task_id = task._id;
		if (tag == doesNotRepeat) {
			repeatModel.interval_type = doesNotRepeat;
			repeatModel.interval = doesNotRepeat;
			// remaing all null
		} else if (tag == daily) {
			repeatModel.interval = 1;
			repeatModel.interval_type = daily;
			// remaing all null
		} else if (tag == weekly) {
			repeatModel.interval = 1;
			repeatModel.interval_type = weekly;
			// remaing all null
		} else if (tag == monthly) {
			repeatModel.interval = 1;
			repeatModel.interval_type = monthly;
			// remaing all null
		} else if (tag == yearly) {
			repeatModel.interval = 1;
			repeatModel.interval_type = yearly;
			// remaing all null
		} else if (tag == custom) {
			if (globalInterval == 1) {
				if (isDaily) {
					repeatModel.interval = 1;
					repeatModel.interval_type = daily;
					if (isForever) {
						repeatModel.interval_expiration = null;// forever
					} else if (isUntilAtime) {
						repeatModel.interval_expiration = (String
								.valueOf(calendar_RepeatDialogDateOnly
										.getTimeInMillis()));// date
					} else if (isForAFixedNumberOfEvents) {
						repeatModel.interval_expiration = globalNumberOfEvents;
						// no. of events
					}
				} else if (isWeekly) {
					repeatModel.interval = 1;
					repeatModel.interval_type = weekly;
					repeatModel.interval_week = weekdays.toLowerCase();
					if (isForever) {
						repeatModel.interval_expiration = null;// forever
					} else if (isUntilAtime) {
						repeatModel.interval_expiration = (String
								.valueOf(calendar_RepeatDialogDateOnly
										.getTimeInMillis()));// date
					} else if (isForAFixedNumberOfEvents) {
						repeatModel.interval_expiration = globalNumberOfEvents;
						// no. of events
					}
				} else if (isMonthly) {
					repeatModel.interval = 1;
					repeatModel.interval_type = monthly;
					if (isForever) {
						repeatModel.interval_expiration = null;// forever
					} else if (isUntilAtime) {
						repeatModel.interval_expiration = (String
								.valueOf(calendar_RepeatDialogDateOnly
										.getTimeInMillis()));// date
					} else if (isForAFixedNumberOfEvents) {
						repeatModel.interval_expiration = globalNumberOfEvents;
						// no. of events
					}
				} else if (isYearly) {
					repeatModel.interval = 1;
					repeatModel.interval_type = yearly;
					if (isForever) {
						repeatModel.interval_expiration = null;// forever
					} else if (isUntilAtime) {
						repeatModel.interval_expiration = (String
								.valueOf(calendar_RepeatDialogDateOnly
										.getTimeInMillis()));// date
					} else if (isForAFixedNumberOfEvents) {
						repeatModel.interval_expiration = globalNumberOfEvents;
						// no. of events
					}
				}
			} else if (globalInterval > 1) {
				repeatModel.interval = globalInterval;
				if (isDaily) {
					repeatModel.interval_type = daily;
					if (isForever) {
						repeatModel.interval_expiration = null;// forever
					} else if (isUntilAtime) {
						repeatModel.interval_expiration = (String
								.valueOf(calendar_RepeatDialogDateOnly
										.getTimeInMillis()));// date
					} else if (isForAFixedNumberOfEvents) {
						repeatModel.interval_expiration = globalNumberOfEvents;
						// no. of events
					}
				} else if (isWeekly) {
					repeatModel.interval_type = weekly;

				} else if (isMonthly) {
					repeatModel.interval_type = monthly;
					if (isForever) {
						repeatModel.interval_expiration = null;// forever
					} else if (isUntilAtime) {
						repeatModel.interval_expiration = (String
								.valueOf(calendar_RepeatDialogDateOnly
										.getTimeInMillis()));// date
					} else if (isForAFixedNumberOfEvents) {
						repeatModel.interval_expiration = globalNumberOfEvents;
						// no. of events
					}
				} else if (isYearly) {
					repeatModel.interval_type = yearly;
					if (isForever) {
						repeatModel.interval_expiration = null;// forever
					} else if (isUntilAtime) {
						repeatModel.interval_expiration = (String
								.valueOf(calendar_RepeatDialogDateOnly
										.getTimeInMillis()));// date
					} else if (isForAFixedNumberOfEvents) {
						repeatModel.interval_expiration = globalNumberOfEvents;
						// no. of events
					}
				}
			}

		}

		// SwitchOff=withTime else withoutTime
		if (!isSwitchChecked) { // if switch is OFF
			repeatModel.allDay = doesNotRepeat;
			repeatModel.startDateTime = getLongDateTime(calendar_StartDateOnly,
					calendar_StartTimeOnly);
			repeatModel.endDateTime = getLongDateTime(calendar_EndDateOnly,
					calendar_EndTimeOnly);
		} else if (isSwitchChecked) {// if switch is ON
			repeatModel.allDay = daily;
			repeatModel.startDateTime = getLongDate(calendar_StartDateOnly);
			repeatModel.endDateTime = getLongDate(calendar_EndDateOnly);
		}
		return repeatModel;
	}

	private int getTag() {
		// interval_type returns 0-5
		// doesnotRepeat/daily/weekly/monthly/custom
		String getTag = (String) tv_repeat.getTag();
		return Integer.parseInt(getTag);
	}

	private String getLongDate(Calendar calendar) {
		long time = calendar.getTimeInMillis();
		return String.valueOf(time);
	}

	private String getLongDateTime(Calendar date, Calendar time) {
		int c_startDate = date.get(Calendar.DAY_OF_MONTH);
		int c_startMonth = date.get(Calendar.MONTH);
		int c_startYear = date.get(Calendar.YEAR);

		int c_startHour = time.get(Calendar.HOUR);
		int c_startMinute = time.get(Calendar.MINUTE);

		calendar_DateTime = Calendar.getInstance();
		calendar_DateTime.set(c_startYear, c_startMonth, c_startDate,
				c_startHour, c_startMinute);

		long timeLong = calendar_DateTime.getTimeInMillis();
		return String.valueOf(timeLong);

	}

	private ArrayList<RepeatTaskNotificationsModel> getNotificaitonsModelsList(
			int taskID) {
		ArrayList<RepeatTaskNotificationsModel> arrayList_RepeatTaskNotificationsModel = new ArrayList<RepeatTaskNotificationsModel>();
		notificationDetailsList = getNotifications();
		String intervalString, intervalTypeString;
		Boolean isEmail = false, isNotification = false, isMinute = false, isHour = false, isDay = false, isWeek = false;
		for (String notification : notificationDetailsList) {
			if (notification.contains("as email")) {
				// if the string contains as email
				StringTokenizer tokens = new StringTokenizer(notification, " ");
				String before, as, email; // use less
				intervalString = tokens.nextToken();
				intervalTypeString = tokens.nextToken().toLowerCase();
				if (intervalTypeString.contains("minute")
						|| intervalTypeString.contains("minutes")) {
					isMinute = true;
					isDay = false;
					isHour = false;
					isWeek = false;
				} else if (intervalTypeString.contains("hour")
						|| intervalTypeString.contains("hours")) {
					isMinute = false;
					isDay = false;
					isHour = true;
					isWeek = false;
				} else if (intervalTypeString.contains("day")
						|| intervalTypeString.contains("days")) {
					isMinute = false;
					isDay = true;
					isHour = false;
					isWeek = false;
				} else if (intervalTypeString.contains("week")
						|| intervalTypeString.contains("weeks")) {
					isMinute = false;
					isDay = false;
					isHour = false;
					isWeek = true;
				}

				before = tokens.nextToken();// use less
				as = tokens.nextToken();
				email = tokens.nextToken();
				isEmail = true;
				isNotification = false;
			} else {
				// if the string does not contain as email
				StringTokenizer tokens = new StringTokenizer(notification, " ");
				String before;// use less
				intervalString = tokens.nextToken();
				intervalTypeString = tokens.nextToken().toLowerCase();
				if (intervalTypeString.contains("minute")
						|| intervalTypeString.contains("minutes")) {
					isMinute = true;
					isDay = false;
					isHour = false;
					isWeek = false;
				} else if (intervalTypeString.contains("hour")
						|| intervalTypeString.contains("hours")) {
					isMinute = false;
					isDay = false;
					isHour = true;
					isWeek = false;
				} else if (intervalTypeString.contains("day")
						|| intervalTypeString.contains("days")) {
					isMinute = false;
					isDay = true;
					isHour = false;
					isWeek = false;
				} else if (intervalTypeString.contains("week")
						|| intervalTypeString.contains("weeks")) {
					isMinute = false;
					isDay = false;
					isHour = false;
					isWeek = true;
				}
				before = tokens.nextToken();// use lessF
				isNotification = true;
				isEmail = false;
			}
			RepeatTaskNotificationsModel model = new RepeatTaskNotificationsModel();
			model.task_id = taskID;
			model.interval = Integer.valueOf(intervalString);
			if (isMinute) {
				model.interval_type = 0;
			} else if (isHour) {
				model.interval_type = 1;
			} else if (isDay) {
				model.interval_type = 2;
			} else if (isWeek) {
				model.interval_type = 3;
			}
			if (isEmail) {
				model.send_notificaion_as_email = 1; // for email
			} else if (isNotification) {
				model.send_notificaion_as_email = 0; // default notification

			}
			arrayList_RepeatTaskNotificationsModel.add(model);
		}
		return arrayList_RepeatTaskNotificationsModel;

	}

	private ArrayList<String> getNotifications() {
		int size = arrayList_TextView.size();
		for (TextView textView : arrayList_TextView) {
			String check = textView.getText().toString().toLowerCase();
			if (check == "add another notification"
					|| check.contains("add another notification")
					|| check == "no notification"
					|| check.contains("no notification")
					|| check == "notifications"
					|| check.contains("notifications")
					|| check == "notification time"
					|| check.contains("notification time")) {
				// if any of the above string are found
				// Do Nothing
			} else
				notificationDetailsList.add(textView.getText().toString());
		}
		return notificationDetailsList;
	}

	@SuppressLint({ "ResourceAsColor", "NewApi" })
	private void addANewTextView(TextView textView) {
		try {

			LinearLayout layout = (LinearLayout) findViewById(R.id.append_btn_linearlayout);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			textView.setText("Notification Time");
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
			if (textViewTag == 2) {
				tv_notification2 = textView;
			} else if (textViewTag == 3) {
				tv_notification3 = textView;
			} else if (textViewTag == 4) {
				tv_notification4 = textView;
			}
			textViewTag++;
		} catch (Exception e) {
			Log.e("New TextView createion error", e.getLocalizedMessage());
		}
	}

}
