package com.gravity.innovations.tasks.done;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gravity.innovations.tasks.done.DemoHelper.TaskListDemo;
import com.gravity.innovations.tasks.done.DemoHelper.TaskOperations;

public class TaskListFragment extends Fragment implements TaskListDemo,
		TaskOperations {
	private TaskListModel data;
	private RecyclerView mRecyclerView;
	private TextView tv_listTitle, tv_footerSyncedTime;
	private ImageView iv_listIcon, iv_isSynced;
	private ImageButton btn_toggleNavDrawer, btn_deleteList, btn_editList,
			btn_shareList, btn_listSharedWith;
	public TaskAdapter mTaskAdapter;
	NavigationDrawerFragment mNavigationDrawerFragment;
	Activity mActivity;
	public int selectedTaskID;
	private BitmapAdapter mUsersAdapter;
	private GridView mGridView;
	private AppHandlerService mService;
	private ImageView mTextView_ownerImage;
	private TextView mTextView_ownerName;
	private String TAG = "TasklistFragment";
	private ImageButton floatingBtn;

	public TaskListFragment() {
	}

	public void newInstance(TaskListModel temp, int _selectTaskId,
			NavigationDrawerFragment mNavigationDrawerFragment,
			AppHandlerService mService) {
		this.data = temp;
		// updateRelativeTime();
		this.mNavigationDrawerFragment = mNavigationDrawerFragment;
		this.selectedTaskID = _selectTaskId;
		this.mService = mService;
	}

	public Fragment getFragment() {
		return this;
	}

	public void updateRelativeTime() {
		long currentTimeMills = System.currentTimeMillis();
		for (TaskModel m : this.data.tasks)
			m.updateRelativeTime(currentTimeMills);
		mTaskAdapter.notifyDataSetChanged();
	}

	public void updateCurrentTask() {
		int id = mTaskAdapter.getPosition();
		mTaskAdapter.notifyItemChanged(mTaskAdapter.getPosition());
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		try {
			// Assign color to headerLayout
			RelativeLayout headerLayout = (RelativeLayout) rootView
					.findViewById(R.id.header);
			String hex = data.fragmentColor;
			headerLayout.setBackgroundColor(Color.parseColor(hex));
			// floating button
			floatingBtn = (ImageButton) rootView
					.findViewById(R.id.floating_button);
			floatingBtn.setBackground(Common.ShapesAndGraphics
					.getfloatingButton(hex));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

				ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
					@Override
					public void getOutline(View view, Outline outline) {
						int size = getResources().getDimensionPixelSize(
								R.dimen.round_button_diameter);
						outline.setOval(0, 0, size, size);
					}
				};
				floatingBtn.setOutlineProvider(viewOutlineProvider);
			}
			floatingBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						mNavigationDrawerFragment.setReminder(data,
								new TaskModel());
					} catch (Exception e) {
						Log.e("TLF: onClick new Task", e.getLocalizedMessage());

					}
				}
			});

		} catch (Exception e) {
			Log.e("TaskListFragment", "Header&FloatingBtnColor");
		}

		try {
			btn_toggleNavDrawer = ((ImageButton) rootView
					.findViewById(R.id.iv_drawer_toggle));

			btn_toggleNavDrawer.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mNavigationDrawerFragment.openNavigationDrawer(-1);
					// to open navigation drawer
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		btn_shareList = ((ImageButton) rootView
				.findViewById(R.id.btn_share_list));
		btn_editList = ((ImageButton) rootView.findViewById(R.id.btn_edit_list));
		btn_deleteList = ((ImageButton) rootView
				.findViewById(R.id.btn_delete_list));

		btn_listSharedWith = ((ImageButton) rootView
				.findViewById(R.id.btn_showShared));
		if (true) {// (data.owner_id == mService.user_data._id) {

			btn_shareList.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mActivity = getActivity();
					openShareDialog(mActivity);
				}
			});

			btn_editList.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mActivity = getActivity();
					((MainActivity) mActivity)
							.manuallySelectOptionMenuItem(R.id.action_edit);
				}
			});
			btn_deleteList.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mActivity = getActivity();

					((MainActivity) mActivity)
							.manuallySelectOptionMenuItem(R.id.action_delete);
				}
			});
		} else {
			btn_shareList.setVisibility(View.GONE);
			btn_editList.setVisibility(View.GONE);
			btn_deleteList.setVisibility(View.GONE);
		}
		if (this.data.users.size() <= 0)
			btn_listSharedWith.setVisibility(View.GONE);
		btn_listSharedWith.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mActivity = getActivity();
				openSharedViewDialog();
			}
		});

		if (true) {
			// header on each fragment
			RelativeLayout header = (RelativeLayout) rootView
					.findViewById(R.id.header);
			tv_listTitle = (TextView) rootView.findViewById(R.id.tasklist_name);
			try {
				tv_listTitle.setText(data.title);
			} catch (Exception e) {
				String msg = "listTitle";
				Log.e(TAG, msg);
			}

			iv_listIcon = (ImageView) rootView.findViewById(R.id.tasklist_icon);
			iv_isSynced = (ImageView) rootView.findViewById(R.id.img_sync);
			try {
				mActivity = getActivity();
				iv_listIcon
						.setImageDrawable(mActivity.getResources().getDrawable(
								Common.DrawableResouces
										.compareDrawable(data.icon_identifier)));

			} catch (Exception e) {
				iv_listIcon.setImageDrawable(mActivity.getResources()
						.getDrawable(R.drawable.ic_assignment_white_24dp));
				String msg = "defaul listIconSetResource set";
				Log.e(TAG, msg);
			}

			View lv_footer = inflater.inflate(R.layout.fragment_main_footer,
					null);

			if (this.data.syncStatus != null
					&& (this.data.syncStatus == "Synced" || this.data.syncStatus
							.equals("Synced")))
				iv_isSynced.setImageResource(R.drawable.ic_launcher);
			else
				iv_isSynced.setImageResource(R.drawable.ic_unsynced);

			tv_footerSyncedTime = (TextView) lv_footer
					.findViewById(R.id.time_sync);

			View lv_owner = lv_footer.findViewById(R.id.ownerinfo);
			mTextView_ownerImage = (ImageView) lv_owner
					.findViewById(R.id.grid_item_image);
			mTextView_ownerName = (TextView) lv_owner
					.findViewById(R.id.grid_item_name);

			if (data.owner_id == mService.user_data._id) {
				if (mService.user_data.image != null) {
					Bitmap b = ImageGridAdapter.getRoundedCornerBitmap(Bitmap
							.createScaledBitmap(mService.user_data.image, 40,
									40, true));
					mTextView_ownerImage.setImageBitmap(b);
				} else {
					Bitmap b = BitmapFactory.decodeResource(
							mActivity.getResources(),
							R.drawable.ic_account_circle_grey600_24dp);
					mTextView_ownerImage.setImageBitmap(b);
				}
				if (mService.user_data.name != null)
					mTextView_ownerName.setText(mService.user_data.name);
			} else {
				try {
					Bitmap b = ImageGridAdapter
							.getRoundedCornerBitmap(getUserImage(data.owner));
					mTextView_ownerImage.setImageBitmap(b);
					mTextView_ownerName.setText(data.owner.name);
				} catch (Exception e) {
					Log.e("TLF: getUserImage: data.owener",
							e.getLocalizedMessage());
				}
			}

			try {
				if (data.syncStatusTimeStamp != null) {
					String syncTime = data.syncStatusTimeStamp;
					long time = Long.parseLong(syncTime);
					SimpleDateFormat simpledateformat = new SimpleDateFormat(
							"yyyy-MM-dd " + "hh:mm:ss");
					String DateTime = simpledateformat.format(new Date(time));
					tv_footerSyncedTime.setText(DateTime);
				} else {
					tv_footerSyncedTime.setText("Never");
				}
			} catch (Exception e) {
				Log.e("TasklistFragment", "sycTimeStampAssignment");
			}
			mGridView = (GridView) lv_footer.findViewById(R.id.gridView1);
			mActivity = getActivity();

			ArrayList<Bitmap> users_images = getUsersImages(this.data.users);

			mUsersAdapter = new BitmapAdapter(mActivity, mGridView,
					R.layout.grid_cell, this.data.users, mActivity);

			mGridView.setAdapter(mUsersAdapter);

			mRecyclerView = (RecyclerView) rootView
					.findViewById(R.id.recyclerView);
			LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(
					mActivity);
			mRecyclerView.setLayoutManager(mLinearLayoutManager);

			mTaskAdapter = new TaskAdapter(getActivity(),
					R.layout.row_task_listview, data,
					mNavigationDrawerFragment, /* data.tasks, */
					selectedTaskID, lv_footer, mRecyclerView);
			mRecyclerView.setAdapter(mTaskAdapter);
			try {
				mTaskAdapter.createViewHolder(mRecyclerView, 1);
			} catch (Exception e) {
				String s = e.getLocalizedMessage();
			}
			mRecyclerView.setItemAnimator(mTaskAdapter.anim);
			
			//mRecyclerView.setItemAnimator(new DefaultItemAnimator());

			registerForContextMenu(mRecyclerView);
			mGridView.requestDisallowInterceptTouchEvent(true);
			mTaskAdapter.notifyDataSetChanged();

		}
		SharedPreferences prefs = mActivity.getSharedPreferences(
				Common.PREFS_DEMO, Context.MODE_PRIVATE);
		Boolean demoFlag = prefs.getBoolean(
				Common.PREFS_KEY_DEMO_TASKLIST_FRAG, false);
		if (!demoFlag) {
			demo1_delBtn();
		}
		// below will work in cass when task is added
		if (Common.flag_demoTaskOp == true) {
			Boolean skipFlag_operations = prefs.getBoolean(
					Common.PREFS_KEY_DEMO_SKIP_TASK_OPERATIONS, false);
			if (skipFlag_operations) {
				demo10_openNavDrawer();
			} else {
				Boolean demoFlag_operations = prefs.getBoolean(
						Common.PREFS_KEY_DEMO_TASK_OPERATIONS, false);
				if (!demoFlag_operations) {
					demoTaskOp1_openDetails();
				}
			}

		}
		return rootView;
	}

	public boolean onContextItemSelected(MenuItem item) {
		try {
			// onLongClick it shows options(edit and delete) for a task
			int position = -1;

			position = mTaskAdapter.getPosition();
			// position of recycle view item

			switch (item.getItemId()) {
			case R.id.item_view:
				// TaskModel t = mTaskAdapter.getItem(position);
				mNavigationDrawerFragment.openTaskDetailsDialog(data,
						mTaskAdapter.getItem(position));
				break;
			case R.id.item_delete:
				// TaskModel t = mTaskAdapter.getItem(position);
				mNavigationDrawerFragment.deleteTask(data,
						mTaskAdapter.getItem(position));
				break;
			case R.id.item_edit:
				// ZZ**ZZ mNavigationDrawerFragment.addOrEditTask(data,
				// mTaskAdapter.getItem(position));
				mNavigationDrawerFragment.setReminder(data,
						mTaskAdapter.getItem(position));
				break;
			}
		} catch (Exception e) {
			Log.d("onContextITemSelect", e.getLocalizedMessage(), e);
			return super.onContextItemSelected(item);
		}
		return super.onContextItemSelected(item);
	}

	public void openShareDialog(Activity mActivity) {
		ArrayList<UserModel> temp_users = ((MainActivity) mActivity).getUsers();

		// boolean flag = false;//temp_users.removeAll(this.data.users);//false;
		// for(UserModel m: temp_users)
		// {
		// if(this.data.users.)
		// }
		// for(UserModel m1: this.data.users)
		// {
		// for(UserModel m2: temp_users)
		// {
		// if(m1._id == m2._id){
		// temp_users.remove(m2);
		// break;
		// }
		// }
		// //flag = temp_users.contains(m);
		// //temp_users.remove();
		// }
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

			// Bitmap bmp = BitmapFactory.decodeByteArray(temp.image, 0,
			// temp.image.length);
			// ImageView image = (ImageView) findViewById(R.id.imageView1);

			// user.iconRes.setImageBitmap(bmp);
			// byte[] byteArray = getBlob(temp.image);
			// Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0
			// ,byteArray.length);
			S.add(temp.displayName);
			user.iconRes = temp.image;
			if (temp.server_id != "" && temp.server_id != null)
				user.iconResId = R.drawable.ic_launcher;
			else
				user.iconResId = -1;
			users_lv.add(user);
			for (UserModel m1 : this.data.users) {
				if (m1._id == temp._id)
					users_lv_selected.add(user);
			}
		}

		// CharSequence[] cs = S.toArray(new CharSequence[S.size()]);
		//
		// AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		// builder.setIcon(android.R.drawable.ic_popup_reminder);
		// builder.setTitle("share");
		// builder.setItems(cs, null);
		// builder.create().show();
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
		DialogInterface.OnClickListener negListener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		};

		DialogInterface.OnClickListener posListener = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				ArrayList<Integer> sel = adapter.getSelected();
				ArrayList<UserModel> sel_users = new ArrayList<UserModel>();
				for (Integer i : sel) {
					UserModel m = users.get(i);
					m.image_alpha = 0.8;
					sel_users.add(m);
				}
				data.users.clear();
				data.users.addAll(sel_users);
				if (data.users.size() <= 0)
					btn_listSharedWith.setVisibility(View.GONE);
				else
					btn_listSharedWith.setVisibility(View.VISIBLE);
				mUsersAdapter.clear();
				mUsersAdapter.addAll(sel_users);// (getUsersImages(sel_users));
				mUsersAdapter.notifyDataSetChanged();// = new ImageGridAdapter(,
														// mActivity);
				mNavigationDrawerFragment.addUserToTaskList(data, sel_users);

				// add sel_users in table_users_tasklist and update grid adapter
				// in header

				// TaskModel tempModel = null;
				// String temp = tempModel.associated_usermodels;
				// temp = temp + ", " + which;
				// tempModel.associated_usermodels = temp;
				// db.Task_Edit(tempModel);

			}
		};
		Common.CustomDialog.MultiChoiceDialog(mActivity, adapter,
				onItemClickListener, negListener, posListener, R.string.ok,
				R.string.cancel, "Share");

	}

	private void openSharedViewDialog() {
		ArrayList<UserModel> users = this.data.users;

		ArrayList<Common.CustomViewsData.MultiSelectRowData> users_lv = new ArrayList<Common.CustomViewsData.MultiSelectRowData>();

		ArrayList<String> S = new ArrayList<String>();
		for (UserModel temp : users) {
			Common.CustomViewsData.MultiSelectRowData user = new Common.CustomViewsData.MultiSelectRowData();
			if (temp.name == null || temp.name == "" || temp.name.isEmpty())
				user.text1 = temp.displayName;
			else
				user.text1 = temp.name;
			user.text2 = temp.email;

			if (temp.server_id != "" && temp.server_id != null)
				user.iconResId = R.drawable.ic_launcher;
			else
				user.iconResId = -1;

			S.add(temp.displayName);
			user.iconRes = temp.image;
			users_lv.add(user);

		}

		final MultiSelectListAdapter adapter = new MultiSelectListAdapter(
				mActivity, R.layout.row_multiselectlist, users_lv);
		OnItemClickListener onItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// adapter.setOrRemoveSelection(view,position);

			}
		};
		DialogInterface.OnClickListener posListener = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();

			}
		};
		Common.CustomDialog.MultiChoiceDialog(mActivity, adapter,
				onItemClickListener, null, posListener, R.string.ok,
				R.string.cancel, "Sharing With");

	}

	public ArrayList<Bitmap> getUsersImages(ArrayList<UserModel> users) {
		ArrayList<Bitmap> bmps = new ArrayList<Bitmap>();
		for (UserModel user : users) {
			try {
				bmps.add(getUserImage(user));
			} catch (Exception e) {
				Log.e("TLF: getUserImage", e.getLocalizedMessage());
			}
		}
		return bmps;
	}

	public Bitmap getUserImage(UserModel user) {
		Bitmap bmp = null;
		if (user.image != null) {

			Bitmap b = BitmapFactory.decodeByteArray(user.image, 0,
					user.image.length);
			b = ImageGridAdapter.getRoundedCornerBitmap(b, user.image_alpha);

			bmp = b;

		} else {
			Bitmap b = BitmapFactory.decodeResource(mActivity.getResources(),
					R.drawable.ic_account_circle_grey600_24dp);
			b = ImageGridAdapter.getRoundedCornerBitmap(b, user.image_alpha);
			bmp = b;
		}

		return bmp;
	}

	@Override
	public void demo1_delBtn() {
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText(R.string.demo_deletelist);

		btn_deleteList.startAnimation(Common.Demo.getInfiniteDemoAnim());

		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				btn_deleteList.clearAnimation();
				dialog.dismiss();
				demo2_editBtn();
			}
		});

		dialog.show();
	}

	@Override
	public void demo2_editBtn() {
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText(R.string.demo_editlist);

		btn_editList.startAnimation(Common.Demo.getInfiniteDemoAnim());

		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				btn_editList.clearAnimation();
				dialog.dismiss();
				demo3_shareBtn();
			}
		});

		dialog.show();

	}

	@Override
	public void demo3_shareBtn() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText(R.string.demo_sharelist);

		btn_shareList.startAnimation(Common.Demo.getInfiniteDemoAnim());

		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				btn_shareList.clearAnimation();
				dialog.dismiss();
				demo4_sharedWithBtn();
			}
		});

		dialog.show();
	}

	@Override
	public void demo4_sharedWithBtn() {
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText(R.string.demo_watchshared);

		btn_listSharedWith.setVisibility(View.VISIBLE);
		btn_listSharedWith.startAnimation(Common.Demo.getInfiniteDemoAnim());

		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				btn_listSharedWith.clearAnimation();
				btn_listSharedWith.setVisibility(View.GONE);
				dialog.dismiss();
				demo5_listTitle();
			}
		});

		dialog.show();
	}

	@Override
	public void demo5_listTitle() {
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);
		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText(R.string.demo_listTitle);

		tv_listTitle.startAnimation(Common.Demo.getInfiniteDemoAnim());
		iv_listIcon.startAnimation(Common.Demo.getInfiniteDemoAnim());
		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				tv_listTitle.clearAnimation();
				iv_listIcon.clearAnimation();
				dialog.dismiss();
				demo6_navToggle();
			}
		});

		dialog.show();

	}

	@Override
	public void demo6_navToggle() {
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText(R.string.demo_navToggle);

		btn_toggleNavDrawer.startAnimation(Common.Demo.getInfiniteDemoAnim());

		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				btn_toggleNavDrawer.clearAnimation();
				dialog.dismiss();
				demo7_syncImage();
			}
		});

		dialog.show();
	}

	@Override
	public void demo7_syncImage() {
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText(R.string.demo_syncedToggle);

		iv_isSynced.startAnimation(Common.Demo.getInfiniteDemoAnim());

		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				iv_isSynced.clearAnimation();
				dialog.dismiss();
				demo8_fabBtn();
			}
		});

		dialog.show();
	}

	@Override
	public void demo8_fabBtn() {
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText(R.string.demo_fab);

		floatingBtn.startAnimation(Common.Demo.getInfiniteDemoAnim());

		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				floatingBtn.clearAnimation();
				dialog.dismiss();
				floatingBtn.performClick();

				SharedPreferences settings = mActivity.getSharedPreferences(
						Common.PREFS_DEMO, Context.MODE_PRIVATE);
				Editor editor = settings.edit();
				editor.putBoolean(Common.PREFS_KEY_DEMO_TASKLIST_FRAG, true);
				editor.commit();
			}
		});

		dialog.show();
	}

	// ////////////////////////////////////////////////////////
	@Override
	public void demoTaskOp1_openDetails() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText(R.string.demo_task_operation_details);

		// View v = getActivity().getLayoutInflater().inflate(
		// R.layout.row_task_listview, null);
		//
		// final ImageView item = (ImageView) v.findViewById(R.id.done_toggle);

		mTaskAdapter.setBlinkingPosition(0, Common.Demo.getDemoListAnimation());

		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				mTaskAdapter.setBlinkingPosition(-1,
						Common.Demo.getDemoListAnimation());
				demoTaskOp2_taskLongClick();
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	@Override
	public void demoTaskOp2_taskLongClick() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText(R.string.demo_task_operation_longClick);

		// View v = getActivity().getLayoutInflater().inflate(
		// R.layout.row_task_listview, null);
		// final ImageView item = (ImageView) v.findViewById(R.id.done_toggle);
		mTaskAdapter.setBlinkingPosition(0, Common.Demo.getDemoListAnimation());

		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				mTaskAdapter.setBlinkingPosition(-1,
						Common.Demo.getDemoListAnimation());
				demoTaskOp3_markDoneInstructions();
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	@Override
	public void demoTaskOp3_markDoneInstructions() {

		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText(R.string.demo_task_operation_toggle_instructions);

		mTaskAdapter.getToggleBtn().startAnimation(
				Common.Demo.getInfiniteDemoAnim());
		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				mTaskAdapter.getToggleBtn().clearAnimation();
				demoTaskOp4_markDone();
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	@Override
	public void demoTaskOp4_markDone() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText(R.string.demo_task_operation_markedDone);

		mTaskAdapter.getToggleBtn().performClick();

		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				Common.flag_demoTaskOp = false;
				dialog.dismiss();
				SharedPreferences settings = mActivity.getSharedPreferences(
						Common.PREFS_DEMO, Context.MODE_PRIVATE);
				Editor editor = settings.edit();
				editor.putBoolean(Common.PREFS_KEY_DEMO_TASK_OPERATIONS, true);
				editor.commit();

//				try {
//					mNavigationDrawerFragment.demo1_openDashInstructions();
//				} catch (Exception e) {
//					e.getLocalizedMessage();
//				}
				demo9_addAnotherTask();

			}
		});
		dialog.show();
	}

	@Override
	public void demo9_addAnotherTask() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText("Lets Add another Task");

		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				dialog.dismiss();
				SharedPreferences settings = mActivity.getSharedPreferences(
						Common.PREFS_DEMO, Context.MODE_PRIVATE);
				Editor editor = settings.edit();
				editor.putBoolean(Common.PREFS_KEY_DEMO_SKIP_TASK_OPERATIONS, true);
				editor.putBoolean(Common.PREFS_KEY_DEMO_TASK_ACTIVITY, false);// again
				editor.commit();
				floatingBtn.performClick();
			}
		});
		dialog.show();
	}
	@Override
	public void demo10_openNavDrawer() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		
		instructions.setText("You can add multiple tasks this way");

		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				Common.flag_demoTaskOp = false;
				dialog.dismiss();
				SharedPreferences settings = mActivity.getSharedPreferences(
						Common.PREFS_DEMO, Context.MODE_PRIVATE);
				Editor editor = settings.edit();
				editor.putBoolean(Common.PREFS_KEY_DEMO_TASK_OPERATIONS, true);
				editor.putBoolean(Common.PREFS_KEY_DEMO_TASK_ACTIVITY, true);
				editor.putBoolean(Common.PREFS_KEY_DEMO_SKIP_TASK_OPERATIONS, false);
				editor.commit();

				try {
					mNavigationDrawerFragment.demo1_openDashInstructions();
				} catch (Exception e) {
					e.getLocalizedMessage();
				}

			}
		});
		dialog.show();
	}

	
}
