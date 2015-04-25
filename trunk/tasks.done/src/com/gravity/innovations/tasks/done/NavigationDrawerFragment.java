package com.gravity.innovations.tasks.done;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
	// alaram service
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
		options.add(new OptionsModel(R.drawable.ic_list_default, "Dashboard"));
		final CustomIconListAdapter opt_adapter = new CustomIconListAdapter(
				getActivity(), R.layout.tasklist_listview_row, options);
		// new TaskListAdapter(getActivity(),
		// R.layout.tasklist_listview_row, options);
		//
		// automating the selection on selected
		if (tasklistid != -1 && taskid != -1)
			selectItem(tasklistid, taskid);
		else {
			try {
				((MainActivity) mActivity).onDashboardSelected();
			} catch (Exception e) {
				selectItem(tasklistid, -1);
			}
		}

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
		if (id == -1) {
			// NOTE: it does not need the
			// check and argument int remove them later
			mDrawerLayout.openDrawer(mFragmentContainerView);
			// to open the drawer on every start up
			// and when there is no list
		}
	}

	public void shutNavigationDrawer() {
		mDrawerLayout.closeDrawer(mFragmentContainerView);
		// to close the drawer
	}

	// public void magicButton(){
	// //mFragmentContainerView = getActivity().findViewById(fragmentId);
	// mDrawerLayout.openDrawer(mFragmentContainerView);
	// }
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
		if (oldSelection != null) {
			oldSelection.setBackgroundColor(getResources().getColor(
					android.R.color.transparent));
		}
	}

	private void setDrawableResouce(int position) {
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

						setDrawableResouce(position);

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
								String[] colorsArray = { "#7FFFD4", "#B0B3B6",
										"#F4D05E", "#F46C5E", "#34495e",
										"#e67e22", "#95a5a6", "#00A5A6",
										"#5AADAD", "#C1A79A", "#FF982F",
										"#FFC182" };
								Random rand = new Random();
								int fragment_color = rand
										.nextInt(colorsArray.length) + 1;
								String colorHEX = colorsArray[fragment_color];
								TaskListModel temp = new TaskListModel(title,
										list_type, colorHEX);
								temp.user_id = user_data._id;
								// should retun a bool on true
								temp._id = db.tasklists.Add(temp);// .TaskList_New(temp);
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
								Log.e("MainActivity", "addOrEditTaskList");
							} finally {
								int position = mAdapter.getCount();
								mAdapter.setSelection(position - 1);
								// for highlighting the selection
							}
						}
					} else {
						// update tasklist
						String title = et_title.getText().toString();
						if (title.length() != 0) {

							tasklist.title = title;
							tasklist.icon_identifier = list_type;

							int nRows = db.tasklists.Edit(tasklist);
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
						int position = mAdapter.getPosition(tasklist);
						mAdapter.setSelection(position);
						// setSelection for item
					}
				} catch (Exception e) {
					Log.d("MainActivity", "addOrEditTaskList");
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
				posListener, R.string.save, R.string.cancel, dialogTitle);
	}

	private void editTaskList(TaskListModel Old) {
		this.mAdapter.notifyDataSetChanged();
		int position = this.mAdapter.getPosition(Old);
		selectItem(++position, -1);
	}

	public void editTaskListInAdapter(TaskListModel m) {
		for (int i = 0; i < mAdapter.getCount(); i++)// TaskListModel
														// temp:this.data)
		{
			TaskListModel temp = mAdapter.getItem(i);
			if (temp._id == m._id) {
				temp.etag = m.etag;
				// temp.gravity_id = m.gravity_id;
				temp.icon_identifier = m.icon_identifier;
				temp.kind = m.kind;
				temp.self_link = m.self_link;
				temp.server_id = m.server_id;
				temp.syncStatus = m.syncStatus;
				temp.syncStatusTimeStamp = "";
				temp.title = temp.title;
				temp.updated = temp.updated;
				temp.user_id = temp.user_id;
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

		for (int i = 0; i < mAdapter.getCount(); i++)// TaskListModel
														// temp:this.data)
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
		for (int i = 0; i < mAdapter.getCount(); i++)// TaskListModel
														// temp:this.data)
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
			// send null or any string because @overide

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
		}

		else {
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
				R.string.cancel, negListener, posListener);
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
					if (db.tasks.Delete(task._id) == true) {
						// conditional handle true false
						mAdapter.getItem(position).RemoveTask(task._id);
						// cancel every alarm associated with multiple task
						// deletion
						mAlarmBroadcastReciever.cancelAlarm(mContext, position);
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
		Common.CustomDialog.CustomDialog(mContext, R.string.delete,
				R.string.cancel, negListener, posListener);
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

			}
			break;
		}
	}

	@Override
	public void onTimeReceive(Context mContext, Intent intent) {
		// TODO Auto-generated method stub
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
					Serializable s = (Serializable) list;
					intent.putExtra("key_list", s);
					Serializable t = (Serializable) task;
					intent.putExtra("key_task", t);
					mActivity.startActivityForResult(intent, 12345/* 123 */);
				} catch (Exception e) {
					Log.e("NDF: setReminder", e.getLocalizedMessage());

				}

			}
		});

	}

	public void addOrEditTaskDetails(TaskListModel list, TaskModel task) {
		//NOTE //PROBLEMS
		//in edit case when a notification is added two notification gets added 
		//TaskAdapter is not getting updated properly, problem can be viewed when editing Task 
		for (TaskListModel temp : data) {
			if (temp._id == opt_Tasklist._id) {
				opt_Tasklist = data.get(data.indexOf(temp));
				// opt_Tasklist = data.get(temp._id);
			}
		}// don't screw with this one

		// ///////////////////////////
		if (task._id != -1 || task._id >= 0) {

			// ArrayList<TaskNotificationsModel> refrenceList =
			// opt_Task.notifications;
			// ArrayList<TaskNotificationsModel> newList = task.notifications;
			for (TaskNotificationsModel newNoifications : task.notifications) {
				if (newNoifications._id == -1
						&& newNoifications.interval_type != 0) {
					// Add Case
					mService.db.notification.Add(newNoifications, task._id);
					opt_Task.notifications.add(newNoifications);
				} else if (newNoifications._id != -1
						&& newNoifications.interval_type != 0) {
					// Edit Case
					mService.db.notification.Edit(newNoifications);
					for (TaskNotificationsModel model : opt_Task.notifications) {
						if (newNoifications._id == model._id) {
							opt_Task.notifications.set(
									opt_Task.notifications.indexOf(model),
									newNoifications);
						}
					}

				} else {
					// Delete Case
					mService.db.notification.Delete(newNoifications._id);
					opt_Task.notifications.remove(newNoifications);
				}

			}
			// ArrayList<TaskNotificationsModel> newGeneratedrefrenceList =
			// refrenceList;

		 
			mService.db.tasks.Edit(task /* opt_Task */);
			opt_Task = null;// mService.db.tasks.Get(opt_Task._id);
			opt_Task = mService.db.tasks.Get(task._id);// so the adapter could
														// update properly
			editTask(opt_Tasklist, opt_Task);
			//MarkDoneTask(opt_Tasklist, opt_Task)
		} else if (task._id == -1) {
			opt_Task = task;
			this.opt_Task.fk_tasklist_id = opt_Tasklist._id;
			this.opt_Task._id = mService.db.tasks.Add(opt_Task);
			// opt_Task = mService.db.tasks.Get(opt_Task._id);
			addTask(opt_Tasklist, opt_Task);
			opt_Task = mService.db.tasks.Get(opt_Task._id);
			mAlarmBroadcastReciever.setAlarm(opt_Task, mContext);
		}
	}
}
