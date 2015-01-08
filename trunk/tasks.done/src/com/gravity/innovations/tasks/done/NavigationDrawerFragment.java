package com.gravity.innovations.tasks.done;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gravity.innovations.tasks.done.Common.User;
import com.gravity.innovations.tasks.done.Common.userData;
import com.gravity.innovations.tasks.done.CustomIconListAdapter.OptionsModel;

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
	int list_type = android.R.drawable.ic_menu_agenda; // default
	private View oldSelection = null;// for list icon selection
	// for storing update time in database
	long currTime = System.currentTimeMillis();
	final String currentDateTime = Long.toString(currTime);
	// alaram service
	AlarmBroadcastReciever mAlarmBroadcastReciever = new AlarmBroadcastReciever();
	int weekday_int_value;

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
	/**
	 * Per the design guidelines, you should show the drawer on launch until the
	 * user manually expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	private static final String PREF_USER_ACTION_BAR_COLOR = "actionbar_color";

	// orange else 1 for white
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
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	private View mFragmentContainerView;
	private Activity mActivity;
	private int mCurrentSelectedPosition;// = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;
	private int mUserActionBarColor;
	private OnItemClickListener TaskListItemListener;
	private AppHandlerService mService;

	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mActivity = getActivity();
		CustomActionBarColor();// customized action bar color
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
		selectItem(mCurrentSelectedPosition, -1);

	}

	public void CustomActionBarColor() {
		SharedPreferences settings = mContext.getSharedPreferences(
				"action_bar_color_settings", Context.MODE_PRIVATE);
		mUserActionBarColor = settings.getInt(PREF_USER_ACTION_BAR_COLOR, 0);
		if (mUserActionBarColor == 1) {
			ActionBar mActionBar = getActionBar();
			mActionBar.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#4072b4")));// blue
		} else if (mUserActionBarColor == 2) {
			ActionBar mActionBar = getActionBar();
			mActionBar.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#1abc9c")));// torquise
		} else if (mUserActionBarColor == 3) {
			ActionBar mActionBar = getActionBar();
			mActionBar.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#f39c12")));// orange
		} else if (mUserActionBarColor == 4) {
			ActionBar mActionBar = getActionBar();
			mActionBar.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#000000")));// black
		} else if (mUserActionBarColor == 5) {
			ActionBar mActionBar = getActionBar();
			mActionBar.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#cecece")));// cloud
		}

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

		EditText search = (EditText) p.findViewById(R.id.search);
		// EditText name = (EditText) header.findViewById(R.id.text_name);
		// EditText email = (EditText) header.findViewById(R.id.text_email);

		mDrawerListView.addHeaderView(header);

		/*
		 * We Dont need this anymore
		 */
		// View footer = inflater.inflate(
		// R.layout.fragment_navigation_drawer_footer, null);//
		// navigation_drawer_header,
		// // null);
		// Button btn_add_tasklist = (Button) footer.findViewById(R.id.btn_add);
		//
		// // mDrawerListView.addFooterView(footer);
		// btn_add_tasklist.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// addOrEditTaskList(new TaskListModel());
		// }
		// });
		/*
		 * We Dont need this anymore
		 */
		TaskListItemListener = new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				view.setSelected(true);
				mAdapter.selected = position - 1;
				selectItem(position, -1);
				// below section for showing ndf list item as selected
				// clearSelection();
				// oldSelection = view;

				// view.setBackgroundColor(getResources().getColor(
				// R.color.selection_blue));
			}
		};
		mDrawerListView.setOnItemClickListener(TaskListItemListener);

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

		// db = new DatabaseHelper(mContext);
		// this.data = db.TaskList_List();
		// mAdapter = new TaskListAdapter(getActivity(),
		// R.layout.tasklist_listview_row, data);
		// // mDrawerListView.setTextFilterEnabled(true);//
		// mDrawerListView.setAdapter(mAdapter);
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
			Context mContext, userData user_data, int tasklistid, int taskid,
			AppHandlerService service) {
		this.mService = service;
		db = this.mService.db;// new DatabaseHelper(mContext);
		this.user_data = user_data;
		this.data = db.TaskList_List();
		mAdapter = new TaskListAdapter(getActivity(),
				R.layout.tasklist_listview_row, data);
		mDrawerListView.setAdapter(mAdapter);

		Resources res = getResources();

		ArrayList<CustomIconListAdapter.OptionsModel> options = new ArrayList<CustomIconListAdapter.OptionsModel>();
		options.add(new OptionsModel(R.drawable.ic_action_new_dark,
				"New Catagory"));
		options.add(new OptionsModel(R.drawable.ic_action_about,
				"Get More Apps"));
		options.add(new OptionsModel(R.drawable.ic_action_group,
				"About Developers"));
		options.add(new OptionsModel(R.drawable.ic_action_settings, "Settings"));
		final CustomIconListAdapter opt_adapter = new CustomIconListAdapter(
				getActivity(), R.layout.tasklist_listview_row, options);
		// new TaskListAdapter(getActivity(),
		// R.layout.tasklist_listview_row, options);
		//
		// automating the selection on selected
		if (tasklistid != -1 && taskid != -1)
			selectItem(tasklistid, taskid);
		else
			selectItem(1, -1);
		// SharedPreferences tasklistID = mContext.getSharedPreferences(
		// PREF_TASKLIST_ID_LAST_CLICKED, Context.MODE_PRIVATE);
		// mCurrentSelectedPosition =
		// tasklistID.getInt(PREF_TASKLIST_ID_LAST_CLICKED, 0);
		// if (mCurrentSelectedPosition != 0){
		//
		// selectItem(mCurrentSelectedPosition); //(1);
		// Log.e("Hello", "Mister");
		// }
		//

		// automating the selection on selected

		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;
		// set a custom shadow that overlays the main content when the drawer
		// opens
		// getActionBar().show();
		// user_data.name = "faik";
		// user_data.email = "faik.malik89@gmail.com";
		TextView name = (TextView) mDrawerLayout.findViewById(R.id.text_name);
		name.setText(user_data.name);
		TextView email = (TextView) mDrawerLayout.findViewById(R.id.text_email);
		email.setText(user_data.email);
		ImageView profile = (ImageView) mDrawerLayout
				.findViewById(R.id.profile_img);
		if (user_data.image != null) {
			Bitmap b = ImageGridAdapter.getRoundedCornerBitmap(Bitmap
					.createScaledBitmap(user_data.image, 175, 175, true));
			profile.setImageBitmap(b);
		}
		final ImageButton options_toggle = (ImageButton) mDrawerLayout
				.findViewById(R.id.options_toggle);

		options_toggle.setOnClickListener(new OnClickListener() {
			int imageresource = R.drawable.ic_action_expand;

			@Override
			public void onClick(View v) {
				RelativeLayout search_layout = (RelativeLayout) mDrawerLayout
						.findViewById(R.id.search_layout);
				if (imageresource == R.drawable.ic_action_collapse) {
					mDrawerListView.setAdapter(mAdapter);
					search_layout.setVisibility(View.VISIBLE);
					mDrawerListView
							.setOnItemClickListener(TaskListItemListener);
					imageresource = R.drawable.ic_action_expand;

				} else {
					mDrawerListView.setAdapter(opt_adapter);
					search_layout.setVisibility(View.GONE);
					mDrawerListView
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {

									switch (position) {
									case 1:
										((MainActivity) mActivity)
												.manuallySelectOptionMenuItem(R.id.action_add);
										break;
									case 2:
										((MainActivity) mActivity)
												.manuallySelectOptionMenuItem(R.id.action_store);
										break;
									case 3:
										((MainActivity) mActivity)
												.manuallySelectOptionMenuItem(R.id.action_developer);
										break;

									case 4:
										((MainActivity) mActivity)
												.manuallySelectOptionMenuItem(R.id.action_settings);
										break;

									}
								}
							});
					imageresource = R.drawable.ic_action_collapse;
				}
				options_toggle.setImageResource(imageresource);
				// v.setBackgroundColor(Color.parseColor("#34343434"));
				// TODO Auto-generated method stub

			}
		});
		// profile.setImageBitmap(user_data.image);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		ActionBar actionBar = getActionBar();
		// actionBar.hide();
		actionBar.setDisplayHomeAsUpEnabled(true);
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
				// getActionBar().show();
				if (!isAdded()) {
					return;
				}
				getActivity().supportInvalidateOptionsMenu(); // calls
				// onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				// TODO Auto-generated method stub
				super.onDrawerSlide(drawerView, slideOffset);
				// getActionBar()..hide();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				// getActionBar().hide();
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
		try {
			mTimeReciever = new TimeReciever();
			mContext.registerReceiver(mTimeReciever, new IntentFilter(
					"android.intent.action.TIME_TICK"));
		} catch (Exception e) {
			Log.e("ERROR(FIX ME)", "Address:TIME_TICK, setUp, NDF");
		}
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		// }
	}

	@Override
	public void onPause() {
		super.onPause();
		mContext.unregisterReceiver(mTimeReciever);
	}

	@Override
	public void onResume() {
		super.onResume();
		mContext.registerReceiver(mTimeReciever, new IntentFilter(
				"android.intent.action.TIME_TICK"));
	}

	public void onMinusOne(int id) {
		if (id == -1) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
			// to keep the drawer open on every start up
		}
	}

	public void selectItem(int position, int selectTaskId) {
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
			mCallbacks.onNavigationDrawerItemSelected(temp, selectTaskId);
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
		selectItem(++position, -1);

		if (user_data.is_sync_type && Common.hasInternet(mActivity)) {
			GravityController.post_tasklist(mActivity, user_data, temp,
					Common.RequestCodes.GRAVITY_SEND_TASKLIST);
		}
	}

	/*
	 * later do check this one may be a redundant function DO CHECK
	 */
	/*
	 * private void editTaskList(TaskListModel Old, String Title) { //
	 * this.mAdapter.add(temp); // this.mAdapter.getPosition(old)
	 * this.mAdapter.notifyDataSetChanged(); int position =
	 * this.mAdapter.getPosition(Old); selectItem(++position); }// later do
	 * check this one may be a redundant function
	 */
	/*
	 * later do check this one may be a redundant function
	 */

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public void clearSelection() {
		// if (oldSelection != null) {
		// oldSelection.setBackgroundColor(getResources().getColor(
		// android.R.color.transparent));
		// }
	}

	public void addOrEditTaskList(final TaskListModel tasklist) {
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.addoredit_tasklist_dialog, null);
		final EditText et_title = (EditText) view.findViewById(R.id.et_title);
		// new add//
		final GridView list_image_grid = (GridView) view
				.findViewById(R.id.gridview);

		list_image_grid.setAdapter(new IconGridAdapter(mActivity));
		// list_image_grid.setVerticalScrollBarEnabled(false);
		list_image_grid
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						clearSelection();
						oldSelection = view;

						view.setBackgroundColor(getResources().getColor(
								R.color.selection_blue));
						if (position == 0) {
							list_type = android.R.drawable.ic_menu_agenda;
						} else if (position == 1) {
							list_type = android.R.drawable.ic_menu_call;
						} else if (position == 2) {
							list_type = android.R.drawable.ic_menu_compass;
						} else if (position == 3) {
							list_type = android.R.drawable.ic_menu_day;
						} else if (position == 4) {
							list_type = android.R.drawable.ic_menu_directions;
						} else if (position == 5) {
							list_type = android.R.drawable.ic_menu_edit;
						} else if (position == 6) {
							list_type = android.R.drawable.ic_menu_gallery;
						} else if (position == 7) {
							list_type = android.R.drawable.ic_menu_info_details;
						} else if (position == 8) {
							list_type = android.R.drawable.ic_menu_manage;
						} else if (position == 9) {
							list_type = android.R.drawable.ic_menu_mapmode;
						} else if (position == 10) {
							list_type = android.R.drawable.ic_menu_mylocation;
						} else if (position == 11) {
							list_type = android.R.drawable.ic_menu_preferences;
						} else if (position == 12) {
							list_type = android.R.drawable.ic_menu_my_calendar;
						} else if (position == 13) {
							list_type = android.R.drawable.ic_menu_search;
						} else if (position == 14) {
							list_type = android.R.drawable.ic_menu_recent_history;
						} else if (position == 15) {
							list_type = android.R.drawable.ic_menu_slideshow;
						} else if (position == 16) {
							list_type = R.drawable.catag_social;
						} else if (position == 17) {
							list_type = R.drawable.catag_personal;
						} else if (position == 18) {
							list_type = R.drawable.catag_home;
						} else if (position == 19) {
							list_type = R.drawable.catag_work;
						}
					}
				});
		// new add//
		et_title.setText(tasklist.title);
		String dialogTitle = "";
		if (tasklist._id == -1) {
			dialogTitle = "Select Catagory";
		} else {
			dialogTitle = "Edit Catagory";
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
								TaskListModel temp = new TaskListModel(title,
										list_type);
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
									tasklist._id, title, list_type));
							if (nRows > 0) {
								tasklist.title = title;
								tasklist.icon_identifier = list_type;
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
		selectItem(++position, -1);
	}

	// full details of the tasks
	public void openTaskDetailsDialog(final TaskListModel parent , final TaskModel current) {

		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_task_full_details, null);

		TextView tv_title, tv_details, tv_notes, tv_updated, tv_sync_time;

		final ImageView doneToggle = (ImageView) view
				.findViewById(R.id.detail_done_toggle);

		tv_title = (TextView) view.findViewById(R.id.txt_task_name);
		tv_details = (TextView) view.findViewById(R.id.txt_details);
		tv_notes = (TextView) view.findViewById(R.id.txt_notes);
		tv_updated = (TextView) view.findViewById(R.id.txt_time_updated);
		// TextView tv_due = (TextView) view.findViewById(R.id.);
		tv_sync_time = (TextView) view.findViewById(R.id.txt_time_synced);

		// assigning title
		tv_title.setText(current.title.toString());
		// assigning details
		String details = current.details.toString();
		if (details.isEmpty()) {
			details = "no details yet";
			tv_details.setText(details);
		} else {
			tv_details.setText(details);
		}
		// assigning notes
		String notes = current.notes.toString();
		if (notes.isEmpty()) {
			notes = "no details yet";
			tv_notes.setText(notes);
		} else {
			tv_notes.setText(notes);
		}
		// long to string time formatting
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd " + " "
				+ "hh:MM:ss");
		// assigning updated time
		if (current.updated == null) {
			tv_updated.setText("Last Updated: not updated yet");
		} else {
			long update = Long.parseLong(current.updated.toString());
			String dateString = formatter.format(new Date(update));
			tv_updated.setText("Last Updated: " + dateString);
		}
		// assigning sync time
		try {
			if (current.syncStatusTimeStamp == null) {
				tv_sync_time.setText("Last Synced: not synced yet");
			} else {
				String lastSyced = (current.syncStatusTimeStamp).toString();
				long lastSyncTime = Long.parseLong(lastSyced);
				String syncString = formatter.format(new Date(lastSyncTime));
				tv_sync_time.setText("Last Synced: " + syncString);
			}
		} catch (Exception e) {
			Log.e("AssigningSycedTimeStamp", "NDF openTaskDetailsDialog");
		}

		// tv_created.setText(dateString);

		// String dueDate = current.due_at;
		// String dueDateString = formatter.format(dueDate );
		// String due = current.remind_at;// "well this needs to be fixed";//
		// current.due_at.toString();
		/*
		 * tv_due.setText(due);
		 * 
		 * String interval = null; if (current.remind_interval == 1) { interval
		 * = "once"; tv_interval.setText(interval); } else if
		 * (current.remind_interval == 2) { interval = "Daily";
		 * tv_interval.setText(interval); } else if (current.remind_interval ==
		 * 3) { interval = "Weekly"; tv_interval.setText(interval); } else if
		 * (current.remind_interval == 4) { interval = "Monthly";
		 * tv_interval.setText(interval); } else if (current.remind_interval ==
		 * 5) { interval = "Yearly"; tv_interval.setText(interval); } else {
		 * interval = "none"; tv_interval.setText(interval); }
		 */
		if (current.completed == 1) {
			doneToggle.setBackgroundResource(R.drawable.task_row_done_bg);
		} else if (current.completed == 0) {
			doneToggle.setBackgroundResource(R.drawable.task_row_bg);
		}

		doneToggle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (current.completed == 1) {
					doneToggle.setBackgroundResource(R.drawable.task_row_bg);
					current.completed = 0;
					MarkDoneTask(parent, current);
				} else if (current.completed == 0) {
					current.completed = 1;
					MarkDoneTask(parent, current);
					doneToggle
							.setBackgroundResource(R.drawable.task_row_done_bg);
				}
			}
		});
		Common.CustomDialog.CustomDialog(mContext, view);
	}

	// @SuppressLint("NewApi")
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

		String remind_at = task.remind_at;
		int remind_interval = task.remind_interval;
		int alarm_id = task.alarm_id;

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
										notes, tasklist._id);
								/*
								 * Trying this for new db model and constructor
								 */

								openReminderListDialog(tasklist, temp);

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
							// TaskModel temp = new TaskModel(task._id, title,
							// details, notes, tasklist._id);
							task.title = title;
							task.details = details;
							task.notes = notes;
							openReminderListDialog(tasklist, task);// testing
																	// now
							int nRows = db.Task_Edit(task);
							if (nRows > 0) {
								// tasklist.title = title;
								editTask(tasklist, task); // task and temp
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

	public void openReminderListDialog(final TaskListModel tasklist,
			final TaskModel temp) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Reminder Details");
		final ListView modeList = new ListView(mContext);
		Integer[] imageId = { R.drawable.ic_action_keyboard,
				R.drawable.ic_action_keyboard, R.drawable.ic_action_keyboard };
		// image array
		String[] list_item = { "Repeat: none", "Cancel Alarm", "Set Location" };

		if (temp.remind_interval == 1 || temp.remind_interval == 2
				|| temp.remind_interval == 3 || temp.remind_interval == 4
				|| temp.remind_interval == 5) {
			if (temp.remind_interval == 1) {
				String remindAt = temp.remind_at;
				// helping class Common.FormateTimeStrings used to get formated
				// dates
				String newFormatedDate = Common.FormateTimeStrings
						.getFormatedDateTimeString(remindAt);
				list_item[0] = "Repeat once at " + newFormatedDate;
			} else if (temp.remind_interval == 2) {
				String remindAt = temp.remind_at;
				String newFormatedDate = Common.FormateTimeStrings
						.getFormatedTimeString(remindAt);
				list_item[0] = "Repeat daily at " + newFormatedDate;
				// + temp.remind_at + " "; //
				// String.valueOf(temp.remind_interval);
			} else if (temp.remind_interval == 3) {
				String remindAt = temp.remind_at;
				int weekday = temp.weekday;
				String newFormatedDate = Common.FormateTimeStrings
						.getFormatedWeeklyTimeString(weekday, remindAt);
				list_item[0] = "Repeat weekly at: " + newFormatedDate;
				// temp.remind_at + " "; //
				// String.valueOf(temp.remind_interval);
			} else if (temp.remind_interval == 4) {
				String remindAt = temp.remind_at;
				String newFormatedDate = Common.FormateTimeStrings
						.getFormatedMonthlyDateTimeString(remindAt);
				list_item[0] = "Repeat monthly at " + newFormatedDate;
				// + temp.remind_at + " "; //
				// String.valueOf(temp.remind_interval);
			} else if (temp.remind_interval == 5) {
				String remindAt = temp.remind_at;
				String newFormatedDate = Common.FormateTimeStrings
						.getFormatedYearlyDateTimeString(remindAt);
				list_item[0] = "Repeat yearly at " + newFormatedDate;
				// temp.remind_at + " "; //
				// String.valueOf(temp.remind_interval);
			}
		}

		// if (temp.remind_at != null) {
		// list_item[1] = temp.remind_at; //shows reminder dateTime
		// }

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
						mAlarmBroadcastReciever.cancelAlarm(mContext,
								temp.alarm_id);
						temp.alarm_id = 0;
						temp.remind_at = null;
						temp.remind_interval = 0;
						temp.alarm_status = 0;
						temp.weekday = 0;
						long currTime = System.currentTimeMillis();
						String currentDateTime = Long.toString(currTime);
						temp.updated = currentDateTime;
						db.Task_Edit(temp);
						editTask(tasklist, temp);
						// mAdapter.notifyDataSetChanged();
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
				SelectDialogItemId(tasklist, myItemInt, temp);// listitemid
				dialog.dismiss();
			}
		});
	}

	public void SelectDialogItemId(TaskListModel tasklist, int id,
			TaskModel temp) {
		if (id == 0) {
			// reminderListDialogActionOne(temp);// datetime picker dialog
			repeatDialog(tasklist, temp);// showing optins like once,
											// daily...yearly
		} else if (id == 1) {
			// reminderListDialogActionTwo(temp);//temporary because we dont
			// need it anymore
			// repeat dialog values to displayed later only should be
			// implemented as unopenable later
			cancelAlarm(tasklist, temp);
		} else if (id == 2) {
			reminderListDialogActionThree(tasklist, temp);// set location dialog
		}
	}

	public void repeatDialog(final TaskListModel tasklist, final TaskModel temp) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Reminder");
		final ListView modeList = new ListView(mContext);

		String[] list_item = { "Once", "Once a Day", "Once a Week",
				"Once a Month", "Once a Year" };

		ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_list_item_1, list_item);

		modeList.setAdapter(itemsAdapter);
		builder.setView(modeList);

		// builder.setPositiveButton(R.string.dialog_finish,
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		// }
		// });
		// commented these lines for a reason
		// builder.setNegativeButton(R.string.dialog_dont_remind,
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		// mAdapter.notifyDataSetChanged();
		// dialog.cancel();
		// openReminderListDialog(temp);
		// }
		// });

		builder.setCancelable(false);
		final Dialog dialog = builder.create();
		dialog.show();

		modeList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView,
					int myItemInt, long mylng) {
				// String selectedFromList = (String) (modeList
				// .getItemAtPosition(myItemInt));
				selectRepeatDialogItemId(tasklist, myItemInt, temp);// listitemid
				dialog.dismiss();
			}
		});
	}

	public void cancelAlarm(final TaskListModel tasklist, final TaskModel temp) {

		mAlarmBroadcastReciever.cancelAlarm(mContext, temp.alarm_id);
		temp.alarm_id = 0;
		temp.remind_at = null;
		temp.remind_interval = 0;
		temp.alarm_status = 0;
		temp.weekday = 0;
		temp.updated = currentDateTime;
		db.Task_Edit(temp);
		editTask(tasklist, temp);
	}

	public void selectRepeatDialogItemId(TaskListModel tasklist, int id,
			TaskModel temp) {
		if (id == 0) {
			openPickerDialog_Once(tasklist, temp);// once
		} else if (id == 1) {
			openPickerDialog_Daily(tasklist, temp);// daily
		} else if (id == 2) {
			openPickerDialog_Weekly(tasklist, temp);// weekly
		} else if (id == 3) {
			openPickerDialog_Monthly(tasklist, temp);// month
		} else if (id == 4) {
			openPickerDialog_Yearly(tasklist, temp);// year
		}
	}

	public void openPickerDialog_Once(final TaskListModel tasklist,
			final TaskModel temp) {
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.datetimepicker_dialog, null);
		String dialogTitle = "Remind At Date Time";

		final DatePicker datepicker = (DatePicker) view
				.findViewById(R.id.datepicker);
		final TimePicker timepicker = (TimePicker) view
				.findViewById(R.id.timepicker);

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
				String month = Integer.toString(month_int);
				int date_int = datepicker.getDayOfMonth();
				String date = Integer.toString(date_int);

				String remind_DateTime = (year + "/" + month + "/" + date + "/"
						+ hour + "/" + minute);

				temp.remind_interval = 1;// remind_interval_once;
				temp.remind_at = remind_DateTime;
				temp.alarm_status = 1;// alarm_status_active;
				db.Task_Edit(temp);
				editTask(tasklist, temp);// for alarm icon adapter refresh
				// dialog.cancel();
				try {
					mAlarmBroadcastReciever.setAlarm_RepeatOnce(mContext,
							year_int, month_int, date_int, hour_int,
							minute_int, temp);
				} catch (Exception e) {
					Log.e("NDF",
							"openPickerDialog_Once:mAlarmBroadcastReciever");
				}
				dialog.dismiss();
				// openReminderListDialog(temp);
			}
		};

		DialogInterface.OnClickListener negListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				repeatDialog(tasklist, temp);
				dialog.cancel();
			}
		};
		Common.CustomDialog.CustomDialog(mContext, view, negListener,
				posListener, R.string.dialog_save, R.string.dialog_back,
				dialogTitle);

	}

	public void openPickerDialog_Daily(final TaskListModel tasklist,
			final TaskModel temp) {
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.datetimepicker_dialog /* timepicker_dialog */, null);
		String dialogTitle = "Remind At Time";

		final TimePicker timepicker = (TimePicker) view
				.findViewById(R.id.timepicker);
		final DatePicker datepicker = (DatePicker) view
				.findViewById(R.id.datepicker);

		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				int year_int = datepicker.getYear();
				String year = Integer.toString(year_int);
				int month_int = datepicker.getMonth();
				String month = Integer.toString(month_int);
				int date_int = datepicker.getDayOfMonth();
				String date = Integer.toString(date_int);

				int hour_int = timepicker.getCurrentHour();
				String hour = Integer.toString(hour_int);
				int minute_int = timepicker.getCurrentMinute();
				String minute = Integer.toString(minute_int);

				// String remind_DateTime = ( hour + ":" + minute);
				String remind_DateTime = (year + "/" + month + "/" + date + "/"
						+ hour + "/" + minute);

				temp.remind_interval = 2;// 2_remind_interval_daily;
				temp.remind_at = remind_DateTime;
				temp.alarm_status = 1;// alarm_status_active;
				db.Task_Edit(temp);

				editTask(tasklist, temp);

				try {
					mAlarmBroadcastReciever.setAlarm_repeatDaily(mContext,
							year_int, month_int, date_int, hour_int,
							minute_int, temp);
				} catch (Exception e) {
					Log.e("NDF",
							"openTimePickerDialog_Daily:mAlarmBroadcastReciever");
				}
				// dialog.cancel();
				dialog.dismiss();
				// openReminderListDialog(tasklist, temp);
			}
		};

		DialogInterface.OnClickListener negListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				repeatDialog(tasklist, temp);
				dialog.cancel();
			}
		};
		Common.CustomDialog.CustomDialog(mContext, view, negListener,
				posListener, R.string.dialog_save, R.string.dialog_back,
				dialogTitle);
	}

	public void openPickerDialog_Weekly(final TaskListModel tasklist,
			final TaskModel temp) {
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.weekday_timepicker_dialog, null);

		String dialogTitle = "Remind At Day and Time";

		final TimePicker timepicker = (TimePicker) view
				.findViewById(R.id.timepicker);
		final DatePicker datepicker = (DatePicker) view
				.findViewById(R.id.datepicker);

		Spinner spinner = (Spinner) view.findViewById(R.id.spinner_week);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				mContext, R.array.weekdays_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		final Spinner weekday_picker = (Spinner) view
				.findViewById(R.id.spinner_week);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				Object item = parent.getItemAtPosition(pos);
				weekday_int_value = pos; // global value of the weekday
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				int year_int = datepicker.getYear();
				String year = Integer.toString(year_int);
				int month_int = datepicker.getMonth();
				String month = Integer.toString(month_int);
				int date_int = datepicker.getDayOfMonth();
				String date = Integer.toString(date_int);

				int hour_int = timepicker.getCurrentHour();
				String hour = Integer.toString(hour_int);

				int minute_int = timepicker.getCurrentMinute();
				String minute = Integer.toString(minute_int);

				// String remind_DateTime =(hour + ":" + minute);
				String remind_DateTime = (year + "/" + month + "/" + date + "/"
						+ hour + "/" + minute);
				temp.remind_interval = 3; // repeat_weekly_remind_interval;
				temp.remind_at = remind_DateTime;
				temp.weekday = weekday_int_value;
				temp.alarm_status = 1;// alarm_status_active;
				db.Task_Edit(temp);
				editTask(tasklist, temp);
				try {
					mAlarmBroadcastReciever.setAlarm_RepeatWeekly(mContext,
							year_int, month_int, date_int, hour_int,
							minute_int, temp);

					String weekday = null;
					if (weekday_int_value == 0) {
						weekday = "Sunday";
					} else if (weekday_int_value == 1) {
						weekday = "Monday";
					} else if (weekday_int_value == 2) {
						weekday = "Tuesday";
					} else if (weekday_int_value == 3) {
						weekday = "Wednesday";
					} else if (weekday_int_value == 4) {
						weekday = "Thursday";
					} else if (weekday_int_value == 5) {
						weekday = "Friday";
					} else if (weekday_int_value == 6) {
						weekday = "Saturday";
					}
				} catch (Exception e) {
					Log.e("NDF",
							"openPickerDialog_Weekly:mAlarmBroadcastReciever");
				}
				// dialog.cancel();
				dialog.dismiss();
				// openReminderListDialog(tasklist, temp);
			}
		};

		DialogInterface.OnClickListener negListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				repeatDialog(tasklist, temp);
				dialog.cancel();
			}
		};
		Common.CustomDialog.CustomDialog(mContext, view, negListener,
				posListener, R.string.dialog_save, R.string.dialog_back,
				dialogTitle);
	}

	public void openPickerDialog_Monthly(final TaskListModel tasklist,
			final TaskModel temp) {
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.datetimepicker_dialog, null);
		String dialogTitle = "Remind At Time";

		final DatePicker datepicker = (DatePicker) view
				.findViewById(R.id.datepicker);

		final TimePicker timepicker = (TimePicker) view
				.findViewById(R.id.timepicker);

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
				String month = Integer.toString(month_int);
				int date_int = datepicker.getDayOfMonth();
				String date = Integer.toString(date_int);
				// String remind_DateTime = (year + "/" + month + "/" + date +
				// " " + hour + ":" + minute);
				String remind_DateTime = (year + "/" + month + "/" + date + "/"
						+ hour + "/" + minute);

				temp.remind_interval = 4; // repeat_monthly_remind_interval;
				temp.remind_at = remind_DateTime;
				temp.alarm_status = 1;// alarm_status_active;
				db.Task_Edit(temp);
				editTask(tasklist, temp);
				try {
					mAlarmBroadcastReciever.setAlarm_RepeatMonthly(mContext,
							year_int, month_int, date_int, hour_int,
							minute_int, temp);
				} catch (Exception e) {
					Log.e("NDF",
							"openPickerDialog_Monthly:mAlarmBroadcastReciever");
				}
				// dialog.cancel();
				dialog.dismiss();
				// openReminderListDialog(tasklist, temp);
			}
		};

		DialogInterface.OnClickListener negListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				repeatDialog(tasklist, temp);
				dialog.cancel();
			}
		};
		Common.CustomDialog.CustomDialog(mContext, view, negListener,
				posListener, R.string.dialog_save, R.string.dialog_back,
				dialogTitle);
	}

	public void openPickerDialog_Yearly(final TaskListModel tasklist,
			final TaskModel temp) {
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.datetimepicker_dialog, null);
		String dialogTitle = "Remind At Time";

		final DatePicker datepicker = (DatePicker) view
				.findViewById(R.id.datepicker);

		final TimePicker timepicker = (TimePicker) view
				.findViewById(R.id.timepicker);

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
				String month = Integer.toString(month_int);
				int date_int = datepicker.getDayOfMonth();
				String date = Integer.toString(date_int);
				// String remind_DateTime = (year + "/" + month + "/" + date +
				// " " + hour + ":" + minute);
				String remind_DateTime = (year + "/" + month + "/" + date + "/"
						+ hour + "/" + minute);
				temp.remind_interval = 5; // repeat_yearly_remind_interval;
				temp.remind_at = remind_DateTime;
				temp.alarm_status = 1;// alarm_status_active;
				db.Task_Edit(temp);
				editTask(tasklist, temp);
				try {
					mAlarmBroadcastReciever.setAlarm_RepeatYearly(mContext,
							year_int, month_int, date_int, hour_int,
							minute_int, temp);
				} catch (Exception e) {
					Log.e("NDF",
							"openDateTimePickerDialog:mAlarmBroadcastReciever");
				}
				// dialog.cancel();
				dialog.dismiss();
				// openReminderListDialog(tasklist, temp);
			}
		};

		DialogInterface.OnClickListener negListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				repeatDialog(tasklist, temp);
				dialog.cancel();
			}
		};
		Common.CustomDialog.CustomDialog(mContext, view, negListener,
				posListener, R.string.dialog_save, R.string.dialog_back,
				dialogTitle);
	}

	public void reminderListDialogActionThree(final TaskListModel tasklist,
			final TaskModel temp) {

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Location");
		builder.setMessage("Go Premium");
		builder.setPositiveButton(R.string.dialog_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						openReminderListDialog(tasklist, temp);
					}
				});
		builder.setNegativeButton(R.string.dialog_back,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						openReminderListDialog(tasklist, temp);
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
		selectItem(++position, -1);
	}

	private void editTask(TaskListModel parent, TaskModel temp) {
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
		selectItem(++position, -1);
	}

	public boolean MarkDoneTask(TaskListModel parent, TaskModel temp) {
		if (temp.completed == 1) {
			// temp.completed = 1;
			if (db.Task_Edit(temp) != -1) {
				int position = mAdapter.getPosition(parent);
				this.mAdapter.getItem(position).GetTask(temp._id).set(temp);
				this.mAdapter.notifyDataSetChanged();
				return true;
			} else
				return false;
		} else {
			// temp.completed = 0;

			// temp.updated = currentDateTime;
			if (db.Task_Edit(temp) != -1) {
				int position = mAdapter.getPosition(parent);
				this.mAdapter.getItem(position).GetTask(temp._id).set(temp);
				this.mAdapter.notifyDataSetChanged();
				return true;
			} else
				return false;
		}
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
		selectItem(position, -1);
	}

	public void deleteTask(final TaskListModel parent,
			final ArrayList<Integer> arrayList) {
		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					int position = mAdapter.getPosition(parent);
					for (int temp : arrayList) {
						// cancel every alarm associated with multiple task
						// deletion
						mAlarmBroadcastReciever.cancelAlarm(mContext, temp);
						if (db.Task_Delete(temp) == true) {
							// conditional handle true false
							mAdapter.getItem(position).RemoveTask(temp);
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
					selectItem(++position, -1);
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

		void onNavigationDrawerItemSelected(TaskListModel temp, int selectTaskId);
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

		public void run() {
			try {
				mAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				Log.e("TimerTask", "ERROR");
			}
		}

	}

	@Override
	public void onTimeReceive(Context mContext, Intent intent) {
		// TODO Auto-generated method stub
		// mAdapter.notifyDataSetChanged();

	}

	public void addUserToTaskList(TaskListModel mTaskList,
			ArrayList<UserModel> sel_users) {
		// TODO Auto-generated method stub
		ArrayList<UserModel> db_users = db.UserList_List(mTaskList._id);
		ArrayList<UserModel> final_users = new ArrayList<UserModel>();
		ArrayList<UserModel> del_users = new ArrayList<UserModel>();
		for (UserModel m1 : sel_users) {
			boolean flag = true;
			for (UserModel m2 : db_users) {
				if (m1._id == m2._id) {
					flag = false;
					break;
				}
			}
			if (flag) {
				final_users.add(m1);
			}
		}
		for (UserModel m1 : db_users) {
			boolean flag = false;
			for (UserModel m2 : sel_users) {
				if (m1._id == m2._id) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				del_users.add(m1);
			}
		}
		for (UserModel user : final_users)
			db.UserList_New(mTaskList, user);
		for (UserModel user : del_users)
			db.UserList_Delete(mTaskList._id, user._id);// del
	}

}