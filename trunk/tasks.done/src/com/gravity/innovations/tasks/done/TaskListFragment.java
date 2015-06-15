package com.gravity.innovations.tasks.done;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskListFragment extends Fragment {
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
			final ImageButton floatingBtn = (ImageButton) rootView
					.findViewById(R.id.floating_button);
			floatingBtn.setBackground(Common.ShapesAndGraphics.getfloatingButton(hex));
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
					// * mNavigationDrawerFragment.addOrEditTask(data,
					// * new TaskModel());
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
		if (data.user_id == mService.user_data._id) {

			btn_shareList.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mActivity = getActivity();
					openShareDialog();
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
			tv_listTitle.setText(data.title);

			iv_listIcon = (ImageView) rootView.findViewById(R.id.tasklist_icon);
			iv_isSynced = (ImageView) rootView.findViewById(R.id.img_sync);
			try {
				mActivity = getActivity();
				iv_listIcon.setImageDrawable(mActivity.getResources().getDrawable(Common.DrawableResouces.compareDrawable(data.icon_identifier) ));

			} catch (Exception e) {
				String tag = "TasklistFragment";
				String msg = "listIconSetResource";
				Log.e(tag, msg);
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

			if (data.user_id == mService.user_data._id) {
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
			registerForContextMenu(mRecyclerView);
			mGridView.requestDisallowInterceptTouchEvent(true);
			mTaskAdapter.notifyDataSetChanged();

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

	private void openShareDialog() {
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
}
