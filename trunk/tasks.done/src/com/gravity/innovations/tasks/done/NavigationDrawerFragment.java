package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.sql.CommonDataSource;

import org.json.JSONException;
import org.json.JSONObject;

import com.gravity.innovations.tasks.done.Common.userData;
import com.gravity.innovations.tasks.done.MainActivity.Task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements
		Common.Callbacks.HttpCallback, Common.Callbacks.TimeCallBack {

	DialogListViewAdapter dialog_adapter;
	int resource;
	TimeReciever mTimeReciever;
	private ArrayList<TaskListModel> data = new ArrayList<TaskListModel>();
	private DatabaseHelper db;
	private Common.userData user_data;
	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
	/**
	 * Per the design guidelines, you should show the drawer on launch until the
	 * user manually expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;
	private Context mContext;
	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;
	public TaskListAdapter mAdapter;
	// TaskListFragment mTaskListFragment;// m
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	private View mFragmentContainerView;
	private Activity mActivity;
	private int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;
	Handler mHandler;
	Timer myTimer;
	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mActivity = getActivity();
		// Read in the flag indicating whether or not the user has demonstrated
		// awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState
					.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		}
		// Select either the default item (0) or the last selected item.
		selectItem(mCurrentSelectedPosition);
 
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View p = (View) inflater.inflate(R.layout.fragment_navigation_drawer,
				container, false);
		mDrawerListView = (ListView) p.findViewById(R.id.nav_drawer_listview);

		// int width = getResources().getDisplayMetrics().widthPixels/2;
		// RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
		// mDrawerListView.getLayoutParams();
		// params.width = width;
		// mDrawerListView.setLayoutParams(params);

		// ViewGroup.LayoutParams params = (ViewGroup.LayoutParams)
		// p.getLayoutParams();
		// params.width = getResources().getDisplayMetrics().widthPixels/2;
		// p.setLayoutParams(params);
		//

		// mDrawerListView = (ListView) inflater.inflate(
		// R.layout.fragment_navigation_drawer, container, false);
		View header = inflater.inflate(
				R.layout.fragment_navigation_drawer_header, null);// navigation_drawer_header,
																	// null);

		// ImageView image = (ImageView) header.findViewById(R.id.image);
		// EditText name = (EditText) header.findViewById(R.id.text_name);
		// EditText email = (EditText) header.findViewById(R.id.text_email);

		EditText search = (EditText) p.findViewById(R.id.search);
		mDrawerListView.addHeaderView(header);

		View footer = inflater.inflate(
				R.layout.fragment_navigation_drawer_footer, null);// navigation_drawer_header,
																	// null);
		Button btn_add_tasklist = (Button) footer.findViewById(R.id.btn_add);

		// mDrawerListView.addFooterView(footer);
		btn_add_tasklist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addOrEditTaskList(new TaskListModel());
			}
		});
		mDrawerListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						view.setSelected(true);
						selectItem(position);
					}
				});

		// start swipe
		/*
		 * SwipeDismissListViewTouchListener touchListener = new
		 * SwipeDismissListViewTouchListener( mDrawerListView, new
		 * SwipeDismissListViewTouchListener.DismissCallbacks() {
		 * 
		 * @Override public boolean canDismiss(int position) { return true; }
		 * 
		 * @Override public void onDismiss(ListView listView, int[]
		 * reverseSortedPositions) { for (int position : reverseSortedPositions)
		 * { mAdapter.remove(mAdapter.getItem(position)); }
		 * mAdapter.notifyDataSetChanged(); } });
		 * 
		 * mDrawerListView.setOnTouchListener(touchListener);
		 */
		// end swipe

		mDrawerListView.setTextFilterEnabled(true);

		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {
					mAdapter.getFilter().filter(s.toString());
				} catch (Exception e) {
					Log.e("onTextChanged", "NotWorking");
				} finally {
					// NavigationDrawerFragment.this.mAdapter.notifyDataSetChanged();
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

		db = new DatabaseHelper(mContext);
		this.data = db.TaskList_List();
		mAdapter = new TaskListAdapter(getActivity(),
				R.layout.tasklist_listview_row, data);
		mDrawerListView.setAdapter(mAdapter);
		mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

		return p;// mDrawerListView;
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 * @param user_data
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout,
			Context mContext, userData user_data) {
		db = new DatabaseHelper(mContext);
		this.user_data = user_data;
		this.data = db.TaskList_List();
		mAdapter = new TaskListAdapter(getActivity(),
				R.layout.tasklist_listview_row, data);
		mDrawerListView.setAdapter(mAdapter);
		selectItem(1);
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;
		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.navigation_drawer_open, /*
										 * "open drawer" description for
										 * accessibility
										 */
		R.string.navigation_drawer_close /*
										 * "close drawer" description for
										 * accessibility
										 */
		) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}
				getActivity().supportInvalidateOptionsMenu(); // calls
				// onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}
				if (!mUserLearnedDrawer) {
					// The user manually opened the drawer; store this flag to
					// prevent auto-showing
					// the navigation drawer automatically in the future.
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
							.apply();
				}
				getActivity().supportInvalidateOptionsMenu(); // calls
				// onPrepareOptionsMenu()
			}
		};
		// If the user hasn't 'learned' about the drawer, open it to introduce
		// them to the drawer,
		// per the navigation drawer design guidelines.
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}
		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});
		mTimeReciever = new TimeReciever();
		mContext.registerReceiver( mTimeReciever, new IntentFilter("android.intent.action.TIME_TICK"));
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mContext.unregisterReceiver(mTimeReciever);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mContext.registerReceiver( mTimeReciever, new IntentFilter("android.intent.action.TIME_TICK"));
	}

	public void onMinusOne(int id) {
		if (id == -1) {
			mDrawerLayout.openDrawer(mFragmentContainerView); // to keep the drawer open
		}
	}

	private void selectItem(int position) {
		position--;
		if (position < 0)
			position = 0;
		mCurrentSelectedPosition = position;
		if (mDrawerListView != null) {
			mDrawerListView.setItemChecked(position, true);
		}
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			TaskListModel temp;// = mAdapter.getItem(position+1);
			try {
				temp = mAdapter.getItem(position);
			} catch (Exception ex) {
				temp = new TaskListModel();
			}
			mCallbacks.onNavigationDrawerItemSelected(temp);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar.
		// See also
		// showGlobalContextActionBar, which controls the top-left area of the
		// action bar.
		if (mDrawerLayout != null && isDrawerOpen()) {
			inflater.inflate(R.menu.global, menu);
			showGlobalContextActionBar();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to
	 * show the global app 'context', rather than just what's in the current
	 * screen.
	 */
	private void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(R.string.app_name);
	}

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	private void addTaskList(TaskListModel temp) {
		data.add(temp);
		// this.mAdapter.add(temp);
		this.mAdapter.notifyDataSetChanged();
		int position = this.mAdapter.getPosition(temp);
		selectItem(++position);

		if (user_data.is_sync_type && Common.hasInternet(mActivity)) {
			GravityController.post_tasklist(mActivity, user_data, temp,
					Common.RequestCodes.GRAVITY_SEND_TASKLIST);
		}
	}

	private void editTaskList(TaskListModel Old, String Title) {
		// this.mAdapter.add(temp);
		// this.mAdapter.getPosition(old)
		this.mAdapter.notifyDataSetChanged();
		int position = this.mAdapter.getPosition(Old);
		selectItem(++position);
	}// later do check this one may be a redundant function

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */

	public void addOrEditTaskList(final TaskListModel tasklist) {
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.addoredit_tasklist_dialog, null);
		final EditText et_title = (EditText) view.findViewById(R.id.et_title);
		et_title.setText(tasklist.title);
		String dialogTitle = "";
		if (tasklist._id == -1) {
			dialogTitle = "New Task List";
		} else {
			dialogTitle = "Edit Task List";
		}
		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					if (tasklist._id == -1) {
						// create
						String title = et_title.getText().toString();
						if (title.length() != 0) {
							try {
								TaskListModel temp = new TaskListModel(title);
								// should retun a bool on true
								temp._id = db.TaskList_New(temp);
								if (temp._id != -1) {
									// toastMsg = "tasklist added";
									addTaskList(temp);
								} else {
									// toastMsg =
									// "Retry! \n tasklist not added";
								}
								// Common.CustomToast.CreateAToast(mContext,
								// toastMsg);

								/**
								 * update data
								 */
								mAdapter.updateData(data); // update data
								/**
								 * update data
								 */

							} catch (Exception e) {
								Log.e("MainActivity", "newOrEditTaskList");
							} finally {
								Log.e("MainActivitynewOrEditTaskList", "np");
							}// finally
						}
					} else {
						// update tasklist
						String title = et_title.getText().toString();
						if (title.length() != 0) {
							int nRows = db.TaskList_Edit(new TaskListModel(
									tasklist._id, title));
							if (nRows > 0) {
								tasklist.title = title;
								editTaskList(tasklist);
							}
						}
						/**
						 * update data
						 */
						mAdapter.updateData(data); // update data
						/**
						 * update data
						 */

					}
				} catch (Exception e) {
					Log.d("Exception on", "Positive Listener");
				}
			}
		};
		DialogInterface.OnClickListener negListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		};
		Common.CustomDialog.CustomDialog(mContext, view, negListener,
				posListener, R.string.dialog_save, R.string.dialog_cancel,
				dialogTitle);
	}

	private void editTaskList(TaskListModel Old) {
		this.mAdapter.notifyDataSetChanged();
		int position = this.mAdapter.getPosition(Old);
		selectItem(++position);
	}

	@SuppressLint("NewApi")
	public void addOrEditTask(final TaskListModel tasklist, final TaskModel task) {
		final View view = getActivity().getLayoutInflater().inflate(
				R.layout.addoredit_task_dialog, null);
		final EditText et_title = (EditText) view.findViewById(R.id.et_title);
		final EditText et_details = (EditText) view
				.findViewById(R.id.et_details);
		final EditText et_notes = (EditText) view.findViewById(R.id.et_notes);
		et_title.setText(task.title);
		et_details.setText(task.details);
		et_notes.setText(task.notes);

		String dialogTitle = "";
		if (task._id == -1) {
			dialogTitle = "New Task";
		} else {
			dialogTitle = "Edit Task";
		}
		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					String title = et_title.getText().toString();
					String details = et_details.getText().toString();
					String notes = et_notes.getText().toString();
					
					if (task._id == -1) {
						// create
						if (title.length() != 0) {
							try {
								TaskModel temp = new TaskModel(title, details,
										notes, null, // dueDateTime,
										tasklist._id);

								/*
								 * Trying this for new db model and constructor
								 */

								openListDialog(temp);

								/*
								 * Trying this for new db model and constructor
								 */

								// should retun a bool on true
								temp._id = db.Task_New(temp);
								if (temp._id != -1) {
									// toastMsg = "tasklist added";
									addTask(tasklist, temp);
								} else {
									// toastMsg =
									// "Retry! \n tasklist not added";
								}
								// Common.CustomToast.CreateAToast(mContext,
								// toastMsg);
							} catch (Exception e) {
								Log.e("MainActivity", "newOrEditTaskList");
							} finally {
								Log.e("MainActivitynewOrEditTaskList", "np");
								// open a list dialog
							}// finally
						}
					} else {
						if (title.length() != 0) {
							TaskModel temp = new TaskModel(
									// tasklist._id
									task._id, title, details, notes,
									tasklist._id);
							openListDialog(temp);// testing now
							int nRows = db.Task_Edit(temp);
							if (nRows > 0) {
								// tasklist.title = title;
								editTask(tasklist, temp); // task and temp
								// mTaskListFragment.editTask();
							}
							// toastMsg = "Data Updated successfully";
							// Common.CustomToast.CreateAToast(mContext,
							// toastMsg);
						}
					}
				} catch (Exception e) {
					Log.d("Exception on", "Positive Listener");
				}
			}
		};
		DialogInterface.OnClickListener negListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		};
		Common.CustomDialog.CustomDialog(mContext, view, negListener,
				posListener, R.string.dialog_save, R.string.dialog_cancel,
				dialogTitle);
	}

	public void openListDialog(final TaskModel temp) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Reminder");
		final ListView modeList = new ListView(mContext);

		Integer[] imageId = { R.drawable.ic_action_keyboard,
				R.drawable.ic_action_keyboard, R.drawable.ic_action_keyboard };// image
																				// array

		String[] list_item = { "Set Time and Date", "Repeat", "Set Location" };

		if (temp.remind_at != null) {
			list_item[0] = temp.remind_at;
		}
		if (temp.remind_interval == 1 || temp.remind_interval == 2
				|| temp.remind_interval == 3 || temp.remind_interval == 4) {
			if (temp.remind_interval == 1) {
				list_item[1] = "Once"; // String.valueOf(temp.remind_interval);
			} else if (temp.remind_interval == 2) {
				list_item[1] = "Daily"; // String.valueOf(temp.remind_interval);
			} else if (temp.remind_interval == 3) {
				list_item[1] = "Weekly"; // String.valueOf(temp.remind_interval);
			} else if (temp.remind_interval == 4) {
				list_item[1] = "Yearly"; // String.valueOf(temp.remind_interval);
			}
		}
		dialog_adapter = new DialogListViewAdapter(mActivity, resource,
				list_item, imageId);// sending data to adapter
		modeList.setAdapter(dialog_adapter);
		builder.setView(modeList);

		builder.setPositiveButton(R.string.dialog_finish,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		builder.setNegativeButton(R.string.dialog_dont_remind,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mAdapter.notifyDataSetChanged();
					}
				});

		builder.setCancelable(false);
		final Dialog dialog = builder.create();
		dialog.show();

		modeList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				// String selectedFromList = (String) (modeList
				// .getItemAtPosition(myItemInt));
				SelectDialogItemId(myItemInt, temp);// listitemid
				dialog.dismiss();
			}
		});
	}

	public void SelectDialogItemId(int id, TaskModel temp) {
		if (id == 0) {
			listDialogActionsOne(temp);// datetime picker dialog
		} else if (id == 1) {
			listDialogActionTwo(temp);// repeat dialog
		} else if (id == 2) {
			listDialogActionThree();// set location dialog
		}
	}

	public void listDialogActionsOne(final TaskModel temp) {
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.datetimepicker_dialog, null);
		String dialogTitle = "TimePicker";

		final DatePicker datepicker = (DatePicker) view
				.findViewById(R.id.dialog_datepicker);
		final TimePicker timepicker = (TimePicker) view
				.findViewById(R.id.dialog_timepicker);

		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				int hour_int = timepicker.getCurrentHour();
				String hour = Integer.toString(hour_int);
				int minute_int = timepicker.getCurrentMinute();
				String minute = Integer.toString(minute_int);

				int year_int = datepicker.getYear();
				String year = Integer.toString(year_int);
				int month_int = datepicker.getMonth();
				String month = Integer.toString(month_int + 1);
				int date_int = datepicker.getDayOfMonth();
				String date = Integer.toString(date_int);

				String remind_DateTime = (year + "/" + month + "/" + date + " "
						+ hour + ":" + minute);

				db = new DatabaseHelper(mContext);
				int recieved_id = temp._id;
				String recieved_title = temp.title;
				String recieved_details = temp.details;
				String recieved_notes = temp.notes;
				// remind_DateTime;
				int recieved_fk_id = temp.fk_tasklist_id;
				TaskModel temp = new TaskModel(recieved_id, recieved_title,
						recieved_details, recieved_notes, remind_DateTime,
						recieved_fk_id);
				db.Task_Edit(temp);
				dialog.cancel();
				dialog.dismiss();
				openListDialog(temp);
			}
		};

		DialogInterface.OnClickListener negListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		};
		Common.CustomDialog.CustomDialog(mContext, view, negListener,
				posListener, R.string.dialog_save, R.string.dialog_back,
				dialogTitle);
	}

	public void listDialogActionTwo(final TaskModel temp) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Repeat");
		final String[] items = { "Once", "Once a Day", "Once a Week",
				"Once a Year" };
		builder.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
					}
				});
		builder.setPositiveButton(R.string.dialog_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						int remind_interval = ((AlertDialog) dialog)
								.getListView().getCheckedItemPosition();// position
						remind_interval++; // position ++;

						db = new DatabaseHelper(mContext);
						int recieved_id = temp._id;
						String recieved_title = temp.title;
						String recieved_details = temp.details;
						String recieved_notes = temp.notes;
						String remind_DateTime = temp.remind_at;
						int recieved_fk_id = temp.fk_tasklist_id;
						TaskModel temp = new TaskModel(recieved_id,
								recieved_title, recieved_details,
								recieved_notes, remind_DateTime,
								remind_interval, recieved_fk_id);

						db.Task_Edit(temp);
						dialog.dismiss();
						openListDialog(temp);
					}
				});
		builder.setNegativeButton(R.string.dialog_back,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		builder.setCancelable(true);
		final Dialog dialog = builder.create();
		dialog.show();
	}

	public void listDialogActionThree() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Location");
		builder.setMessage("Go Premium");
		builder.setPositiveButton(R.string.dialog_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		builder.setNegativeButton(R.string.dialog_back,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		builder.setCancelable(false);
		final Dialog dialog = builder.create();
		dialog.show();
	}

	private void addTask(TaskListModel parent, TaskModel temp) {
		parent.tasks.add(temp);
		this.mAdapter.notifyDataSetChanged();
		/**
		 * update data
		 */
		mAdapter.updateData(data); // update data
		/**
		 * update data
		 */
		int position = this.mAdapter.getPosition(parent);
		selectItem(++position);
	}

	private void editTask(TaskListModel parent, TaskModel temp)// (TaskModel
																// task,
																// TaskModel
																// temp)
	{
		int position = mAdapter.getPosition(parent);
		this.mAdapter.getItem(position).GetTask(temp._id).set(temp);
		this.mAdapter.notifyDataSetChanged();
		/**
		 * update data
		 */
		mAdapter.updateData(data); // update data
		/**
		 * update data
		 */
		position = this.mAdapter.getPosition(parent);
		selectItem(++position);
	}

	public void deleteTaskList(final TaskListModel tasklist) {
		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					db.TaskList_Delete(tasklist._id);
					removeTaskList(tasklist);

					/**
					 * update data
					 */
					mAdapter.updateData(data); // update data
					/**
					 * update data
					 */

				} catch (Exception E) {
					Log.e("MainActivity", "Delete TaskList");
				} finally {
					// Update adapter
				}
			}
		};
		DialogInterface.OnClickListener negListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		};
		Common.CustomDialog.CustomDialog(mContext, R.string.delete,
				R.string.dialog_cancel, negListener, posListener);
	}

	private void removeTaskList(TaskListModel temp) {
		int position = 0;// this.mAdapter.getPosition(temp);
		boolean flag = false;
		for (TaskListModel i : this.data) {
			if (flag) {
				position = this.mAdapter.getPosition(i);
				break;
			}
			if (i._id == temp._id)
				flag = true;
		}
		data.remove(temp);
		this.mAdapter.notifyDataSetChanged();
		/**
		 * update data
		 */
		mAdapter.updateData(data); // update data
		/**
		 * update data
		 */
		selectItem(position);
	}

	public void deleteTask(final TaskListModel parent,
			final ArrayList<Integer> arrayList) {
		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					int position = mAdapter.getPosition(parent);
					for (int temp : arrayList) {
						if (db.Task_Delete(temp) == true) {// conditional
							mAdapter.getItem(position).RemoveTask(temp);// handle
																		// true
																		// false
						} else {
							Log.e("NDF deleteTask", "bool if condition error");
						}
					}
					mAdapter.notifyDataSetChanged();
					/**
					 * update data
					 */
					mAdapter.updateData(data); // update data
					/**
					 * update data
					 */
					selectItem(++position);
				} catch (Exception E) {
					Log.e("MainActivity", "Delete Task");
				} finally {
					// Update adapter
				}
			}
		};
		DialogInterface.OnClickListener negListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		};
		Common.CustomDialog.CustomDialog(mContext, R.string.delete,
				R.string.dialog_cancel, negListener, posListener);
	}

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(TaskListModel temp);
	}

	@Override
	public void httpResult(JSONObject data, int RequestCode, int ResultCode) {
		// TODO Auto-generated method stub
		switch (RequestCode) {
		case Common.RequestCodes.GRAVITY_SEND_TASKLIST:
			if (ResultCode == Common.HTTP_RESPONSE_OK) {
				try {
					data = data.getJSONObject("data");
					data.get("TaskListId");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

			}
			break;
		}
	}

    class UpdateTimeTask extends TimerTask {

        public void run() 
           {        
            try{
        	 mAdapter.notifyDataSetChanged();
            }catch(Exception e){
            	Log.e("TimerTask", "ERROR");
            }
           }

        }
  
 
	@Override
	public void onTimeReceive(Context mContext, Intent intent) {
		// TODO Auto-generated method stub
		    	//mAdapter.notifyDataSetChanged();       
				
		
 	}

}

 
