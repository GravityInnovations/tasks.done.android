package com.gravity.innovations.tasks.done;

import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.mn;
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
	private View oldSelection = null;
	AlarmBroadcastReciever mAlarmBroadcastReciever = new AlarmBroadcastReciever();

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
	// private Builder mDialogBuilder;
	public TaskListModel opt_Tasklist;// =null;
	public TaskModel opt_Task;// =null;
	LayoutInflater inflaterr;

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

	public ArrayList<TaskModel> getPendingTasks() {
		ArrayList<TaskModel> temp = new ArrayList<TaskModel>();
		for (TaskListModel tasklist : this.data) {
			temp.addAll(tasklist.getPendingTasks());
		}
		return temp;
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
		inflaterr = inflater;
		mDrawerListView = (ListView) p.findViewById(R.id.nav_drawer_listview);
		View header = inflater.inflate(
				R.layout.fragment_navigation_drawer_header, null);

		EditText search = (EditText) p.findViewById(R.id.search);
		mDrawerListView.addHeaderView(header);
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
		mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
		return p;
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
		this.data = db.tasklists.Get();// .TaskList_List();
		mAdapter = new TaskListAdapter(getActivity(),
				R.layout.row_tasklist_listview, data);
		mDrawerListView.setAdapter(mAdapter);

		Resources res = getResources();

		ArrayList<CustomIconListAdapter.OptionsModel> options = new ArrayList<CustomIconListAdapter.OptionsModel>();
		options.add(new OptionsModel(R.drawable.ic_add_grey600_18dp,
				"New Catagory"));
		options.add(new OptionsModel(R.drawable.ic_info_outline_grey600_18dp,
				"Get More Apps"));
		options.add(new OptionsModel(R.drawable.ic_group_grey600_18dp,
				"About Developers"));
		options.add(new OptionsModel(R.drawable.ic_settings_grey600_18dp,
				"Settings"));
		options.add(new OptionsModel(R.drawable.ic_web_grey600_18dp,
				"Dashboard"));
		options.add(new OptionsModel(R.drawable.ic_router_grey600_18dp, String
				.valueOf(getResources().getString(R.string.make_ad_free))));
		final CustomIconListAdapter opt_adapter = new CustomIconListAdapter(
				getActivity(), R.layout.row_tasklist_listview, options);
		// new TaskListAdapter(getActivity(),
		// R.layout.tasklist_listview_row, options);
		if (tasklistid != -1 && taskid != -1)
			/*
			 * selectItem(tasklistid, taskid);
			 */
			getItemsPositions(tasklistid, taskid);
		else {
			try {
				((MainActivity) mActivity).onDashboardSelected();
			} catch (Exception e) {
				selectItem(tasklistid, -1);
			}
		}

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
			int imageresource = R.drawable.ic_expand_more_white_36dp;

			@Override
			public void onClick(View v) {
				RelativeLayout search_layout = (RelativeLayout) mDrawerLayout
						.findViewById(R.id.search_layout);
				if (imageresource == R.drawable.ic_expand_less_white_36dp) {
					mDrawerListView.setAdapter(mAdapter);
					search_layout.setVisibility(View.VISIBLE);
					mDrawerListView
							.setOnItemClickListener(TaskListItemListener);
					imageresource = R.drawable.ic_expand_more_white_36dp;

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
										options_toggle.performClick();
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
									case 5:
										((MainActivity) mActivity)
												.manuallySelectOptionMenuItem(R.id.action_dashboard);
										options_toggle.performClick();
										break;
									case 6:
										((MainActivity) mActivity)
												.manuallySelectOptionMenuItem(R.id.action_refer_friend);
										break;

									}
								}
							});
					imageresource = R.drawable.ic_expand_less_white_36dp;
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

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		// }
	}

	/*
	 * @Override public void onPause() { super.onPause();
	 * mContext.unregisterReceiver(mTimeReciever); }
	 * 
	 * @Override public void ume() { super.ume();
	 * mContext.registerReceiver(mTimeReciever, new IntentFilter(
	 * "android.intent.action.TIME_TICK")); }
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	public void openNavigationDrawer(int id) {
		mDrawerLayout.openDrawer(mFragmentContainerView);
			// to open the drawer on every start up
			// and when there is no list
	}

	public void shutNavigationDrawer() {
		mDrawerLayout.closeDrawer(mFragmentContainerView);
		// to close the drawer
	}

	public void getItemsPositions(int listID, int taskID) {
		int list_pos = 0;
		int task_pos = 0;

		for (TaskListModel _list : data) {
			if (_list._id == listID) {
				list_pos = data.indexOf(_list);
				for (TaskModel _task : _list.tasks) {
					if (_task._id == taskID) {
						task_pos = _list.tasks.indexOf(_task);
					}
				}
			}
		}
		selectItem(++list_pos, task_pos);
	}

	public void selectItem(TaskModel temp) {
		int list_pos = this.data.indexOf(temp.parent);
		int task_pos = this.data.get(list_pos).tasks.indexOf(temp);
		selectItem(++list_pos, task_pos);
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
			Common.SoftKeyboard.hide(mActivity);
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

	public void addTaskList(TaskListModel temp) {
		data.add(temp);
		// this.mAdapter.add(temp);
		this.mAdapter.notifyDataSetChanged();
		int position = this.mAdapter.getPosition(temp);
		selectItem(++position, -1);

		// if (user_data.is_sync_type && Common.hasInternet(mActivity)) {
		// GravityController.post_tasklist(mActivity, user_data, temp,
		// Common.RequestCodes.GRAVITY_SEND_TASKLIST);
		// }
	}

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public void clearSelection() {
		if (oldSelection != null) {
			oldSelection.setBackgroundColor(getResources().getColor(
					android.R.color.transparent));
		}
	}

	@SuppressLint("NewApi")
	private void setColorBackgroundResouce(GridView mGridView, int position,
			EditText et_hexCode, View view_catagoryColor) {
		View view = mGridView.getChildAt(position);
		clearSelection();
		oldSelection = view;
		String _colorHex = Common.ColorHex.taskDoneBlue;
		if (position == 0) {
			_colorHex = Common.ColorHex.taskDoneBlue;
		} else if (position == 1) {
			_colorHex = Common.ColorHex.moonLightBlue;
		} else if (position == 2) {
			_colorHex = Common.ColorHex.orangeJazz;
		} else if (position == 3) {
			_colorHex = Common.ColorHex.pink;
		} else if (position == 4) {
			_colorHex = Common.ColorHex.seaGreen;
		} else if (position == 5) {
			_colorHex = Common.ColorHex.yellow;
		} else if (position == 6) {
			_colorHex = Common.ColorHex.caramel;
		} else if (position == 7) {
			_colorHex = Common.ColorHex.teal;
		} else if (position == 8) {
			_colorHex = Common.ColorHex.lightGreen;
		} else if (position == 9) {
			_colorHex = Common.ColorHex.grey;
		} else if (position == 10) {
			_colorHex = Common.ColorHex.blueGrey;
		} else if (position == 11) {
			_colorHex = Common.ColorHex.depOrange;
		}

		et_hexCode.setText(_colorHex);
		view_catagoryColor.setBackgroundColor(Color.parseColor(_colorHex));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackgroundDrawable(Common.ShapesAndGraphics
					.getCircularShape(_colorHex));
		} else {
			view.setBackground(Common.ShapesAndGraphics
					.getCircularShape(_colorHex));
		}
	}

	Button temp_btn;

	public void addOrEditTaskList(final TaskListModel tasklist) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		View view_dialog = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_add_edit_tasklist, null);

		final EditText et_title = (EditText) view_dialog
				.findViewById(R.id.et_title);

		final View view_catagoryColor = (View) view_dialog
				.findViewById(R.id.view_catagoryColor);

		final EditText et_hexCode = (EditText) view_dialog
				.findViewById(R.id.et_hex);

		AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String v = parent.getTag().toString();
				if (v == "icon" || v.contains("icon")) {
					tasklist.icon_identifier = Common.DrawableResouces
							.getDrawableResouce(position);
				}
				if (v == "color" || v.contains("color")) {
					setColorBackgroundResouce(((GridView) parent), position,
							et_hexCode, view_catagoryColor);
					tasklist.fragmentColor = Common.DrawableResouces
							.getHexCode(position);
				}
			}
		};
		GridView iconsGrid = (GridView) view_dialog
				.findViewById(R.id.gv_catagoryIcon);
		// iconsGrid.setVerticalScrollBarEnabled(false);
		iconsGrid.setAdapter(new IconGridAdapter(mActivity,
				Common.Arrays.resource_icons));
		iconsGrid.setOnItemClickListener(listener);
		// iconsGrid.performItemClick(iconsGrid, 5, 5);
		// iconsGrid.setSelection(5);
		final GridView colorsGrid = (GridView) view_dialog
				.findViewById(R.id.gv_catagoryColor);
		colorsGrid.setVerticalScrollBarEnabled(false);
		colorsGrid.setAdapter(new CustomBaseAdapter_ColorsGrid(mActivity,
				Common.Arrays.colorsDrawables));
		colorsGrid.setOnItemClickListener(listener);
		/*
		 * default Selection setItemBackgroundResouce_NEW( ((
		 * GridView)colorsGrid), 0, et_hexCode, view_catagoryColor);
		 */

		et_hexCode.addTextChangedListener(new TextWatcher() {
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
				String str = et_hexCode.getText().toString();
				if (str.startsWith("#", 0)) {
					if ((str.length() >= 7)) {
						try {
							view_catagoryColor.setBackgroundColor(Color
									.parseColor(s.toString()));
						} catch (Exception e) {
							e.getLocalizedMessage();
						}
					}
				}
			}
		});
	
		
		//
		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		};
		builder.setView(view_dialog);
		builder.setPositiveButton(R.string.save, posListener);
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
		Button theButton = alertDialog
				.getButton(DialogInterface.BUTTON_POSITIVE);
		temp_btn = theButton;
		theButton.setEnabled(false);
		if (tasklist._id != -1) {
			et_title.setText(tasklist.title);
			view_catagoryColor.setBackgroundColor(Color
					.parseColor(tasklist.fragmentColor));
			temp_btn.setEnabled(true);
		}
		//
		et_title.addTextChangedListener(new TextWatcher() {

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
				if (s.length() > 0) {
					temp_btn.setEnabled(true);
				} else {
					temp_btn.setEnabled(false);
				}
			}
		});


		OnClickListener listerner = new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					String _title = et_title.getText().toString();
					_title = _title.replaceAll("\t", " ");// remove tabs
					_title = _title.replaceAll("\n", " ");// remove new line
					_title = _title.trim();// to remove spaces
					tasklist.title = _title;
				} catch (Exception e) {
					e.getLocalizedMessage();
				}
				String hex;
				try {
					hex = et_hexCode.getText().toString();
					if (hex.startsWith("#", 0) && hex.length() == 7) {
						tasklist.fragmentColor = et_hexCode.getText()
								.toString();
					}
					if (hex == null || hex.isEmpty() || hex.length() == 0
							|| hex == " " || hex == "") {
						tasklist.fragmentColor = Common.ColorHex.taskDoneBlue;
					}
				} catch (Exception e) {
					tasklist.fragmentColor = Common.ColorHex.taskDoneBlue;
					e.getLocalizedMessage();
				}

				try {
					String str = String.valueOf(tasklist.icon_identifier);
					if (str == "0") {
						tasklist.icon_identifier = R.drawable.ic_assignment_grey600_24dp;
					}
				} catch (Exception e) {
					e.getLocalizedMessage();
					tasklist.icon_identifier = R.drawable.ic_assignment_grey600_24dp;
				}

				if (tasklist._id == -1) {
					tasklist.owner_id = user_data._id;
					if (tasklist.title.length() != 0) {
						tasklist._id = db.tasklists.Add(tasklist);// .TaskList_New(temp);
						if (tasklist._id != -1) {
							addTaskList(tasklist);
						} else {
							Toast.makeText(mContext,
									"List not created try again",
									Toast.LENGTH_LONG).show();
						}
					} else if (tasklist.title.length() == 0) {
						Toast.makeText(mContext,
								"Try again with a valid title",
								Toast.LENGTH_LONG).show();
					}
					mAdapter.updateData(data); // update data
					int position = mAdapter.getCount();
					mAdapter.setSelection(position - 1);

				} else {
					if (tasklist.title.length() != 0) {
						
						int nRows = db.tasklists.Edit(tasklist,false);
						if (nRows > 0) {
							editTaskList(tasklist);
						}
					} else if (tasklist.title.length() == 0) {
						Toast.makeText(mContext,
								"Try again with a valid title",
								Toast.LENGTH_LONG).show();
					}
					mAdapter.updateData(data); // update data
					int position = mAdapter.getPosition(tasklist);
					mAdapter.setSelection(position);

				}
				alertDialog.dismiss();
			}
		};
		alertDialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(
				listerner);

	}

	private void editTaskList(TaskListModel Old) {
		this.mAdapter.notifyDataSetChanged();
		int position = this.mAdapter.getPosition(Old);
		selectItem(++position, -1);
	}

	public void editTaskListInAdapter(TaskListModel m) {
		for (int i = 0; i < mAdapter.getCount(); i++)
		// TaskListModel temp:this.data)
		{
			TaskListModel temp = mAdapter.getItem(i);
			if (temp._id == m._id) {
				// temp.gravity_id = m.gravity_id;
				temp.icon_identifier = m.icon_identifier;
				temp.server_id = m.server_id;
				temp.syncStatus = m.syncStatus;
				temp.syncStatusTimeStamp = "";
				temp.title = temp.title;
				temp.DateUpdated = temp.DateUpdated;
				temp.owner_id = temp.owner_id;
				this.mAdapter.notifyDataSetChanged();
				// ((MainActivity)mActivity).mTaskListFragment.
				int position = this.mAdapter.getPosition(temp);
				selectItem(++position, -1);// select if selected
				break;
			}
		}
	}
	
	public void addUserShareInAdapter(String tasklistId, String userids,
			boolean updateUsersAdapter) {

		for (int i = 0; i < mAdapter.getCount(); i++)
		// TaskListModel temp:this.data)
		{
			TaskListModel temp = mAdapter.getItem(i);
			if (temp.server_id == tasklistId
					|| temp.server_id.equals(tasklistId)) {
				if (updateUsersAdapter) {
					temp.users = db.users.Get(temp);
				}
				for (UserModel user : temp.users) {
					if (user.server_id != null
							&& userids.contains(user.server_id)) {
						user.image_alpha = 1.0;
					}
				}
				this.mAdapter.notifyDataSetChanged();
				// ((MainActivity)mActivity).mTaskListFragment.
				int position = this.mAdapter.getPosition(temp);
				selectItem(++position, -1);// select if selected
				break;
			}
		}

	}

	public void removeUserShareInAdapter(String tasklistId, String userids) {
		for (int i = 0; i < mAdapter.getCount(); i++)
		// TaskListModel temp:this.data)
		{
			TaskListModel temp = mAdapter.getItem(i);
			if (temp.server_id == tasklistId
					|| temp.server_id.equals(tasklistId)) {
				for (UserModel user : temp.users) {
					if (user.server_id != null
							&& userids.contains(user.server_id)) {
						mAdapter.getItem(i).users.remove(user);
					}
				}
				this.mAdapter.notifyDataSetChanged();
				// ((MainActivity)mActivity).mTaskListFragment.
				int position = this.mAdapter.getPosition(temp);
				selectItem(++position, -1);// select if selected
				break;
			}
		}
	}

	// full details of the tasks
	public void openTaskDetailsDialog(final TaskListModel parent,
			final TaskModel current) {
		try {
			DialogViewFragment dialog = new DialogViewFragment(parent, current,
					this, mActivity);
			dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
			dialog.show(getChildFragmentManager(), null);
			// send null or any string because override
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		if (db.tasks.Edit(temp) <= 0) {
			return false;
		} else {
			int position = mAdapter.getPosition(parent);
			this.mAdapter.getItem(position).GetTask(temp._id).set(temp);
			this.mAdapter.notifyDataSetChanged();
			((MainActivity) mActivity).updateCurrentTask();
			return true;
		}
	}

	public void deleteTaskList(final TaskListModel tasklist) {
		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					db.tasklists.Delete(tasklist._id);
					delete_tasklist_on_ui(tasklist);
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
		Common.CustomDialog.set(mContext, R.string.delete, R.string.cancel,
				negListener, posListener, R.string.delete_message_list);
	}
	public void delete_tasklist_on_ui(TaskListModel tasklist)
	{
		removeTaskList(tasklist);
		/**
		 * update data
		 */
		mAdapter.updateData(data); // update data
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

	public void deleteTask(final TaskListModel tasklist, final TaskModel task) {
		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					int position = mAdapter.getPosition(tasklist);
					// db.tasks.Delete(task._id);

					TaskModel tempTask = mService.db.tasks.Get(task._id);
					for (TaskNotificationsModel temp : tempTask.notifications) {
						mAlarmBroadcastReciever.cancelAlarm(mContext, temp._id);
					}

					if (db.tasks.Delete(task._id) == true) {
						// conditional handle true false
						mAdapter.getItem(position).RemoveTask(task._id);
						// cancel every alarm associated with multiple task
						// deletion
						// mAlarmBroadcastReciever.cancelAlarm(mContext,
						// position);
						// ??????????????????????????????????????????????????????????????
					} else {
						Log.e("NDF deleteTask", "bool if condition error");
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
		Common.CustomDialog.set(mContext, R.string.delete, R.string.cancel,
				negListener, posListener, R.string.delete_message_task);
	}

	/**
	 * Call backs interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks {
		// Called when an item in the navigation drawer is selected.
		void onNavigationDrawerItemSelected(TaskListModel temp, int selectTaskId);

		void onDashboardSelected();
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
					e.printStackTrace();
				}
			} else {
			}
			break;
		}
	}

	@Override
	public void onTimeReceive(Context mContext, Intent intent) {
		Log.e("NDF ", "onTimeReciever");
	}

	public void addUserToTaskList(TaskListModel mTaskList,
			ArrayList<UserModel> sel_users) {
		// TODO Auto-generated method stub
		ArrayList<UserModel> db_users = db.users.Get(mTaskList);// db.UserList_List(mTaskList._id);
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
		// for (UserModel user : final_users)
		if (final_users.size() > 0)
			db.users.Share(mTaskList, final_users);// .UserList_New(mTaskList,
													// final_users);
		// for (UserModel user : del_users)
		if (del_users.size() > 0)
			db.users.Share_Remove(mTaskList, del_users);// del
	}

	public void setReminder(final TaskListModel list, final TaskModel task) {
		this.opt_Tasklist = list;
		this.opt_Task = task;
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					Intent intent = new Intent(mActivity, TaskActivity.class);
					Serializable l = (Serializable) list;
					intent.putExtra(Common.KEY_EXTRAS_LIST, l);
					Serializable t = (Serializable) task;
					intent.putExtra(Common.KEY_EXTRAS_TASK, t);
					mActivity.startActivityForResult(intent, 12345/* 123 */);
				} catch (Exception e) {
					Log.e("NDF: setReminder", e.getLocalizedMessage());
				}
			}
		});
	}

	public void updateTask(TaskListModel list, TaskModel task) {

		for (TaskListModel tempList : data) {
			if (tempList._id == opt_Tasklist._id) {
				opt_Tasklist = data.get(data.indexOf(tempList));
				// getting reference
			}
		}

		if (task._id >= 0) {//means it is an old task
			for (TaskNotificationsModel tempNotificaiton : opt_Task.notifications) {
				if (tempNotificaiton._id != -1) {
					//for canceling all alarms
					mAlarmBroadcastReciever.cancelAlarm(mContext, tempNotificaiton._id);
				}
			}
			for (TaskNotificationsModel newNoifications : task.notifications) {
				if (newNoifications.interval_type != 0) {
					// definition that the notificationModel are (!Empty && Set)
					if (newNoifications._id == -1) {
						// Add Case
						mService.db.notification.Add(newNoifications, task._id);
						opt_Task.notifications.add(newNoifications);
					} else if (newNoifications._id != -1) {
						// Edit Case
						mService.db.notification.Edit(newNoifications);
						for (TaskNotificationsModel model : opt_Task.notifications) {
							if (newNoifications._id == model._id) {
								opt_Task.notifications.set(	opt_Task.notifications.indexOf(model), newNoifications);
							}
						}
					}
				} else if (newNoifications.interval_type == 0) {
					// remaining are all Delete Case
					mService.db.notification.Delete(newNoifications._id);
					opt_Task.notifications.remove(newNoifications);
				}
			}
			
			mService.db.tasks.Edit(task); //so that name title etc can be updated
			opt_Task = null;
			opt_Task = mService.db.tasks.Get(task._id);
			for (TaskModel temp : opt_Tasklist.tasks) {
				if (temp._id == opt_Task._id) {
					opt_Tasklist.tasks.set(opt_Tasklist.tasks.indexOf(temp), opt_Task);
				}
			}
			editTask(opt_Tasklist, opt_Task);
			// Set New Alarms
			//mAlarmBroadcastReciever.setAlarm(opt_Task, mContext);

		} else if (task._id == -1) {
			opt_Task = task;
			this.opt_Task.fk_tasklist_id = opt_Tasklist._id;
			this.opt_Task._id = mService.db.tasks.Add(opt_Task);
			addTask(opt_Tasklist, opt_Task);
			opt_Task = mService.db.tasks.Get(opt_Task._id);// redundant probably
			// Set New Alarms
			//mAlarmBroadcastReciever.setAlarm(opt_Task, mContext);
		}
	}

	public void referAFriend(int _userDataId) {
		shutNavigationDrawer();
		openAdFreeDialog(_userDataId);
	}

	public void openAdFreeDialog(final int _userDataId) {
		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// dialog.cancel();
				if (which == 0) {
					// Log.e("Refer", "Refer");
					openShareDialog(_userDataId);
				} else if (which == 1) {
					// Log.e("Video", "Video");
				}
			}
		};
		String[] items = { " Refer A Friend ", " Watch a video " };
		Common.CustomDialog.listDialog(mContext, items, posListener);
	}

	public void openShareDialog(final int _userDataId) {
		ArrayList<UserModel> temp_users = ((MainActivity) mActivity).getUsers();
		final ArrayList<UserModel> users = temp_users;
		ArrayList<Common.CustomViewsData.MultiSelectRowData> users_lv = new ArrayList<Common.CustomViewsData.MultiSelectRowData>();
		ArrayList<Common.CustomViewsData.MultiSelectRowData> users_lv_selected = new ArrayList<Common.CustomViewsData.MultiSelectRowData>();
		ArrayList<String> S = new ArrayList<String>();
		for (UserModel temp : users) {
			Common.CustomViewsData.MultiSelectRowData user = new Common.CustomViewsData.MultiSelectRowData();

			if (temp.name == null || temp.name == "" || temp.name.isEmpty())

				user.text1 = temp.displayName;
			else
				user.text1 = temp.name;

			user.text2 = temp.email;
			S.add(temp.displayName);
			user.iconRes = temp.image;
			if (temp.server_id != "" && temp.server_id != null)
				user.iconResId = R.drawable.ic_launcher;
			else
				user.iconResId = -1;
			users_lv.add(user);
			// for (UserModel m1 : this.data.users) {
			// if (m1._id == temp._id)
			// users_lv_selected.add(user);
			// }
		}

		final MultiSelectListAdapter adapter = new MultiSelectListAdapter(
				mActivity, R.layout.row_multiselectlist, users_lv);
		adapter.setNewSelection(users_lv_selected);
		OnItemClickListener onItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.setOrRemoveSelection(view, position);
			}
		};

		DialogInterface.OnClickListener negListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		};

		DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				ArrayList<Integer> sel = adapter.getSelected();
				ArrayList<UserModel> sel_users = new ArrayList<UserModel>();
				ArrayList<String> emailList = new ArrayList<String>();
				for (Integer i : sel) {
					UserModel m = users.get(i);
					m.image_alpha = 0.8;
					sel_users.add(m);
					emailList.add(m.email);
				}
				// emailList = new ArrayList<String>();
				// emailList.add("abbasi.abdullah72@gmail.com");
				// emailList.add("qazi.danial.ak@gmail.com");
				// emailList.add("mushahid.hassan@yahoo.com");
				// emailList.add("hassan_mushahid@live.co.uk");
				// emailList.add("mushahidhassan110@gmail.com");
				SendEmail mail = new SendEmail(emailList, _userDataId);
			}
		};
		Common.CustomDialog.MultiChoiceDialog(mActivity, adapter,
				onItemClickListener, negListener, posListener, R.string.ok,
				R.string.cancel, "Share");
	}
}
