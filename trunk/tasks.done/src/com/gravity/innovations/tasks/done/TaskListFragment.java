package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.google.android.gms.internal.ad;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.res.Resources;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskListFragment extends Fragment {
	private TaskListModel data;
	private ListView mListView;
	private TextView mTextView_listName, mTextView_syncedTime;
	private ImageView mImageView;
	private ImageButton btn_share, btn_edit, btn_delete;
	public TaskAdapter mTaskAdapter;
	public int selCount = 0; // for CAB multi select count
	NavigationDrawerFragment mNavigationDrawerFragment;
	Activity mActivity;
	public int selectedTaskID;
	private BitmapAdapter mUsersAdapter;
	private ImageButton btn_shared;

	private GridView mGridView;

	public TaskListFragment() {

	}

	public void newInstance(TaskListModel temp, int _selectTaskId,
			NavigationDrawerFragment mNavigationDrawerFragment) {
		// TODO Auto-generated method stub
		this.data = temp;
		
		// updateRelativeTime();
		this.mNavigationDrawerFragment = mNavigationDrawerFragment;
		this.selectedTaskID = _selectTaskId;
		// this.mThumbIds= mThumbIds;
	}

	public void newInstance(TaskListModel temp,
			NavigationDrawerFragment navigationDrawerFragment) {
		this.data = temp;
		// updateRelativeTime();
		this.mNavigationDrawerFragment = navigationDrawerFragment;

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

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_main, container,

				false);
		btn_share = ((ImageButton) rootView
				.findViewById(R.id.btn_share_list));
		btn_edit = ((ImageButton) rootView
				.findViewById(R.id.btn_edit_list));
		btn_delete = ((ImageButton) rootView
				.findViewById(R.id.btn_delete_list));

		btn_shared = ((ImageButton)rootView.findViewById(R.id.btn_showShared));

		btn_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mActivity = getActivity();

				openShareDialog();
				// TODO Auto-generated method stub
				// Toast.makeText(mActivity, "txt",Toast.LENGTH_LONG).show();

			}
		});
		if(this.data.users.size()<=0)
			btn_shared.setVisibility(View.GONE);
		btn_shared.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mActivity = getActivity();

				openSharedViewDialog();
				
			}
		});
		if(this.data.users.size()<=0)
			btn_shared.setVisibility(View.GONE);
		btn_shared.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mActivity = getActivity();
				openSharedViewDialog();
				
			}
		});
		btn_edit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mActivity = getActivity();
				//((MainActivity) mActivity).listof_nameEmailPic();
				// TODO Auto-generated method stub
				//Toast.makeText(mActivity, "edit",Toast.LENGTH_LONG).show();
				((MainActivity) mActivity)
				.manuallySelectOptionMenuItem(R.id.action_edit);

			}
		});
		btn_delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mActivity = getActivity();
				//((MainActivity) mActivity).listof_nameEmailPic();
				// TODO Auto-generated method stub
				((MainActivity) mActivity)
				.manuallySelectOptionMenuItem(R.id.action_delete);
			}
		});

		if (true){//(data.tasks != null && data.tasks.size() > 0) {
			mListView = (ListView) rootView.findViewById(R.id.list);	

			//header on each fragment
			
			mImageView = (ImageView) rootView.findViewById(R.id.tasklist_icon);
			try {
				mActivity = getActivity();
				Resources resources = mActivity.getResources();
				mImageView.setImageDrawable(resources
						.getDrawable(data.icon_identifier));
			} catch (Exception e) {
				String tag = "TasklistFragment";
				String msg = "listIconSetResource";
				Log.e(tag, msg);
			}
			mTextView_listName = (TextView) rootView.findViewById(R.id.tasklist_name);
			mTextView_listName.setText(data.title);

			mTextView_syncedTime = (TextView) rootView.findViewById(R.id.time_sync);

			if(data.syncStatusTimeStamp!=null){
			mTextView_syncedTime.setText(data.syncStatusTimeStamp);
			}else{
				mTextView_syncedTime.setText("Not Synced Yet");
			}
			mGridView = (GridView) rootView
 					.findViewById(R.id.gridView1);
			mActivity= getActivity();
		
			ArrayList<Bitmap> users_images = getUsersImages(this.data.users);
					
					//new ArrayList<Bitmap>();// getUsersImages(((MainActivity)mActivity).getUsers());//new ArrayList<Bitmap>();//getUsersImages(this.data.users);
			mUsersAdapter =new BitmapAdapter(mActivity, R.layout.grid_cell, users_images, mActivity);// new ImageGridAdapter(users_images, getActivity().getApplicationContext());
			mGridView.setVerticalScrollBarEnabled(false);
			mGridView.setHorizontalScrollBarEnabled(false);
			mGridView.setOnTouchListener(new OnTouchListener(){

			    @Override
			    public boolean onTouch(View v, MotionEvent event) {
			        if(event.getAction() == MotionEvent.ACTION_MOVE){
			            return true;
			        }
			        return false;
			    }

			});
			mGridView.setAdapter(mUsersAdapter);
		
			mTaskAdapter = new TaskAdapter(getActivity(),
					R.layout.task_listview_row, data,
					mNavigationDrawerFragment, data.tasks, selectedTaskID);
			mListView.setAdapter(mTaskAdapter);

			mTaskAdapter.notifyDataSetChanged();

			// Swipe to delete task
			/*
			 * SwipeDismissListViewTouchListener touchListener = new
			 * SwipeDismissListViewTouchListener( mListView, new
			 * SwipeDismissListViewTouchListener.DismissCallbacks() {
			 * 
			 * @Override public boolean canDismiss(int position) { return true;
			 * }
			 * 
			 * @Override public void onDismiss(ListView listView, int[]
			 * reverseSortedPositions) { for (int position :
			 * reverseSortedPositions) {
			 * mTaskAdapter.remove(mTaskAdapter.getItem(position));
			 * 
			 * mNavigationDrawerFragment.deleteTask(data,
			 * mTaskAdapter.getSelectedTaskModels());
			 * 
			 * } mTaskAdapter.notifyDataSetChanged(); } });
			 * mListView.setOnTouchListener(touchListener);
			 */
			// swipe to delete task
			mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

			mListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				@Override
				public boolean onActionItemClicked(
						android.view.ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
					case R.id.item_delete:
						mNavigationDrawerFragment.deleteTask(data,
								mTaskAdapter.getSelectedTaskModels());
						mode.finish();
						break;
					case R.id.item_edit:
						mNavigationDrawerFragment.addOrEditTask(data,
								mTaskAdapter.getSingularSelectedTaskModel());
						mode.finish();
					case R.id.item_share:
						// listof_nameEmailPic();
						mode.finish();
						break;
					}
					return true;
				}

				@Override
				public boolean onCreateActionMode(android.view.ActionMode mode,
						Menu menu) {
					getActivity().getActionBar().show();
					android.view.MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.contextual_menu, menu);
					return true;
				}

				@Override
				public void onDestroyActionMode(android.view.ActionMode mode) {
					// TODO Auto-generated method stub
					getActivity().getActionBar().hide();
					selCount = 0;
					mode.invalidate();
					mTaskAdapter.removeAllSelection();
				}

				@Override
				public boolean onPrepareActionMode(
						android.view.ActionMode mode, Menu menu) {
					MenuItem item_edit, item_share;
					if (selCount == 1) {
						item_edit = menu.findItem(R.id.item_edit);
						item_share = menu.findItem(R.id.item_share);
						item_edit.setVisible(true);
						item_share.setVisible(true);
						return true;
					} else {
						// on multiple item selection hide below item
						item_edit = menu.findItem(R.id.item_edit);
						item_share = menu.findItem(R.id.item_share);
						item_edit.setVisible(false);
						item_share.setVisible(false);
						return true;
					}
				}

				@Override
				public void onItemCheckedStateChanged(
						android.view.ActionMode mode, int position, long id,
						boolean checked) {
					if (checked) {
						selCount++;
						mTaskAdapter.setNewSelection(position, checked);
					} else {
						selCount--;
						mTaskAdapter.removeSelection(position);
					}
					mode.setSubtitle(selCount + " selected");
					mode.setTitle(data.title);

					mode.invalidate();
				}
			});

			mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					mListView.setItemChecked(position,
							!mTaskAdapter.isPositionChecked(position));
					return false;
				}
			});

			mListView.setOnItemClickListener(new OnItemClickListener() {

				// @Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
					// mListView.setItemChecked(pos, true); //this is THE line
					mActivity = getActivity();
					// final Animation animationFadeIn = AnimationUtils
					// .loadAnimation(mActivity, R.anim.fade_in);

					// arg1.startAnimation(animationFadeIn);
					/*
					 * for fixing gridView problems
					 */
					//--pos;
					 mNavigationDrawerFragment
					 .openTaskDetailsDialog(data, mTaskAdapter.getItem(pos));

					// emId.getSingularSelectedTaskModel() );
					// mTaskAdapter.sellectThisThing(pos);

					// swapListItem(pos, data.tasks, arg1);

					// mListView.addView(arg1, 0);
					// mListView.add(0, mTaskAdapter.getItem(pos));
				}
			});
		}
		return rootView;
	}

	public void swapListItem(int position, ArrayList<TaskModel> task_data,
			View view) {

		TaskModel taskAtZeroIndex = task_data.get(0);
		String zeroIndex = taskAtZeroIndex.updated;
		long zero_index = Long.valueOf(zeroIndex).longValue();

		TaskModel taskAtCurrentIndex = mTaskAdapter.getItem(position);
		String currentIndex = taskAtCurrentIndex.updated;
		long current_index = Long.valueOf(currentIndex).longValue();

		Animation animation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_top_to_bottom);
		view.startAnimation(animation);

		mTaskAdapter.remove(taskAtCurrentIndex);
		// mTaskAdapter.notifyDataSetChanged();
		mTaskAdapter.insert(taskAtCurrentIndex, 0);
		mTaskAdapter.notifyDataSetChanged();

		// for (int i = 0; i<= task_data.size(); i++) {
		// compare and get final result id

		// }

	}
	
	private void openShareDialog()
	{
		ArrayList<UserModel> temp_users = ((MainActivity)mActivity).getUsers();
		
		//boolean flag = false;//temp_users.removeAll(this.data.users);//false;
//		for(UserModel m: temp_users)
//		{
//			if(this.data.users.)
//		}
//		for(UserModel m1: this.data.users)
//		{
//			for(UserModel m2: temp_users)
//			{
//				if(m1._id == m2._id){
//					temp_users.remove(m2);
//				break;	
//				}
//			}
//			//flag = temp_users.contains(m);
//			//temp_users.remove();
//		}
		final ArrayList<UserModel> users = temp_users;
		ArrayList<Common.CustomViewsData.MultiSelectRowData> users_lv = new ArrayList<Common.CustomViewsData.MultiSelectRowData>();
		ArrayList<Common.CustomViewsData.MultiSelectRowData> users_lv_selected = new ArrayList<Common.CustomViewsData.MultiSelectRowData>();
		ArrayList<String> S = new ArrayList<String>();
		for (UserModel temp : users) {
			Common.CustomViewsData.MultiSelectRowData user = new Common.CustomViewsData.MultiSelectRowData();
			user.text1 = temp.displayName;
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
			users_lv.add(user);
			for(UserModel m1: this.data.users)
			{
				if(m1._id == temp._id)
					users_lv_selected.add(user);
			}
		}
		
//		CharSequence[] cs = S.toArray(new CharSequence[S.size()]);
//		
//		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//		builder.setIcon(android.R.drawable.ic_popup_reminder);
//		builder.setTitle("share");
//		builder.setItems(cs, null);
//		builder.create().show();
		final MultiSelectListAdapter adapter = new MultiSelectListAdapter(mActivity,
				R.layout.multiselectlist_row, users_lv);
		adapter.setNewSelection(users_lv_selected);
		OnItemClickListener onItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.setOrRemoveSelection(view,position);
				
				
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
				for(Integer i:sel)
				{
					sel_users.add(users.get(i));
				}
				data.users.clear();
				data.users.addAll(sel_users);
				if(data.users.size()<=0)
					btn_shared.setVisibility(View.GONE);
				else
					btn_shared.setVisibility(View.VISIBLE);
				mUsersAdapter.clear();
				mUsersAdapter.addAll(getUsersImages(sel_users));
				mUsersAdapter.notifyDataSetChanged();// = new ImageGridAdapter(, mActivity);
				mNavigationDrawerFragment.addUserToTaskList(data,sel_users);
				
				
				//add  sel_users in table_users_tasklist and update grid adapter in header
				
//				TaskModel tempModel = null;
//				String temp = tempModel.associated_usermodels;
//				temp = temp + ", " + which;
//				tempModel.associated_usermodels = temp;
//				db.Task_Edit(tempModel);
				
			}
		};
		Common.CustomDialog.MultiChoiceDialog(mActivity, adapter,
				onItemClickListener, negListener, posListener,
				R.string.dialog_ok, R.string.dialog_cancel, "Share");
		
	}
	private void openSharedViewDialog()
	{
		ArrayList<UserModel> users = this.data.users;

		ArrayList<Common.CustomViewsData.MultiSelectRowData> users_lv = new ArrayList<Common.CustomViewsData.MultiSelectRowData>();

		ArrayList<String> S = new ArrayList<String>();
		for (UserModel temp : users) {
			Common.CustomViewsData.MultiSelectRowData user = new Common.CustomViewsData.MultiSelectRowData();
			user.text1 = temp.displayName;
			user.text2 = temp.email;
			
			
			S.add(temp.displayName);
			user.iconRes = temp.image;
			users_lv.add(user);
			
		}

		final MultiSelectListAdapter adapter = new MultiSelectListAdapter(mActivity,
				R.layout.multiselectlist_row, users_lv);
		OnItemClickListener onItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//adapter.setOrRemoveSelection(view,position);
				
				
			}
		};
DialogInterface.OnClickListener posListener = new OnClickListener() {
			

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
		};
		Common.CustomDialog.MultiChoiceDialog(mActivity, adapter,
				onItemClickListener,null, posListener,
				R.string.dialog_ok, R.string.dialog_cancel, "Sharing With");
		
	}
	public ArrayList<Bitmap> getUsersImages(ArrayList<UserModel> users)
	{
		ArrayList<Bitmap> bmps = new ArrayList<Bitmap>();
		for(UserModel user: users)
		{
			if(user.image!= null)
			bmps.add(BitmapFactory.decodeByteArray(user.image, 0, user.image.length));
			else
				bmps.add(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.catag_personal));
		}
		return bmps;
	}
	
//	private void openShareDialog()
//	{
//		ArrayList<UserModel> temp_users = ((MainActivity)mActivity).getUsers();
//		
//		//boolean flag = false;//temp_users.removeAll(this.data.users);//false;
////		for(UserModel m: temp_users)
////		{
////			if(this.data.users.)
////		}
////		for(UserModel m1: this.data.users)
////		{
////			for(UserModel m2: temp_users)
////			{
////				if(m1._id == m2._id){
////					temp_users.remove(m2);
////				break;	
////				}
////			}
////			//flag = temp_users.contains(m);
////			//temp_users.remove();
////		}
//		final ArrayList<UserModel> users = temp_users;
//		ArrayList<Common.CustomViewsData.MultiSelectRowData> users_lv = new ArrayList<Common.CustomViewsData.MultiSelectRowData>();
//		ArrayList<Common.CustomViewsData.MultiSelectRowData> users_lv_selected = new ArrayList<Common.CustomViewsData.MultiSelectRowData>();
//		ArrayList<String> S = new ArrayList<String>();
//		for (UserModel temp : users) {
//			Common.CustomViewsData.MultiSelectRowData user = new Common.CustomViewsData.MultiSelectRowData();
//			user.text1 = temp.displayName;
//			user.text2 = temp.email;
//			
//			// Bitmap bmp = BitmapFactory.decodeByteArray(temp.image, 0,
//			// temp.image.length);
//			// ImageView image = (ImageView) findViewById(R.id.imageView1);
//
//			// user.iconRes.setImageBitmap(bmp);
//			// byte[] byteArray = getBlob(temp.image);
//			// Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0
//			// ,byteArray.length);
//			S.add(temp.displayName);
//			user.iconRes = temp.image;
//			users_lv.add(user);
//			for(UserModel m1: this.data.users)
//			{
//				if(m1._id == temp._id)
//					users_lv_selected.add(user);
//			}
//		}
//		
////		CharSequence[] cs = S.toArray(new CharSequence[S.size()]);
////		
////		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
////		builder.setIcon(android.R.drawable.ic_popup_reminder);
////		builder.setTitle("share");
////		builder.setItems(cs, null);
////		builder.create().show();
//		final MultiSelectListAdapter adapter = new MultiSelectListAdapter(mActivity,
//				R.layout.multiselectlist_row, users_lv);
//		adapter.setNewSelection(users_lv_selected);
//		OnItemClickListener onItemClickListener = new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				adapter.setOrRemoveSelection(view,position);
//				
//				
//			}
//		};
//		DialogInterface.OnClickListener negListener = new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.cancel();
//			}
//		};
//
//		DialogInterface.OnClickListener posListener = new OnClickListener() {
//			
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.cancel();
//				ArrayList<Integer> sel = adapter.getSelected();
//				ArrayList<UserModel> sel_users = new ArrayList<UserModel>();
//				for(Integer i:sel)
//				{
//					sel_users.add(users.get(i));
//				}
//				data.users.clear();
//				data.users.addAll(sel_users);
//				if(data.users.size()<=0)
//					btn_shared.setVisibility(View.GONE);
//				else
//					btn_shared.setVisibility(View.VISIBLE);
//				mUsersAdapter.clear();
//				mUsersAdapter.addAll(getUsersImages(sel_users));
//				mUsersAdapter.notifyDataSetChanged();// = new ImageGridAdapter(, mActivity);
//				
//				
//				
//				//add  sel_users in table_users_tasklist and update grid adapter in header
//				
////				TaskModel tempModel = null;
////				String temp = tempModel.associated_usermodels;
////				temp = temp + ", " + which;
////				tempModel.associated_usermodels = temp;
////				db.Task_Edit(tempModel);
//				
//			}
//		};
//		Common.CustomDialog.MultiChoiceDialog(mActivity, adapter,
//				onItemClickListener, negListener, posListener,
//				R.string.dialog_ok, R.string.dialog_cancel, "Share");
//		
//	}
//	private void openSharedViewDialog()
//	{
//		ArrayList<UserModel> users = this.data.users;
//
//		ArrayList<Common.CustomViewsData.MultiSelectRowData> users_lv = new ArrayList<Common.CustomViewsData.MultiSelectRowData>();
//
//		ArrayList<String> S = new ArrayList<String>();
//		for (UserModel temp : users) {
//			Common.CustomViewsData.MultiSelectRowData user = new Common.CustomViewsData.MultiSelectRowData();
//			user.text1 = temp.displayName;
//			user.text2 = temp.email;
//			
//			
//			S.add(temp.displayName);
//			user.iconRes = temp.image;
//			users_lv.add(user);
//			
//		}
//
//		final MultiSelectListAdapter adapter = new MultiSelectListAdapter(mActivity,
//				R.layout.multiselectlist_row, users_lv);
//		OnItemClickListener onItemClickListener = new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				//adapter.setOrRemoveSelection(view,position);
//				
//				
//			}
//		};
//DialogInterface.OnClickListener posListener = new OnClickListener() {
//			
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.cancel();
//				
//			}
//		};
//		Common.CustomDialog.MultiChoiceDialog(mActivity, adapter,
//				onItemClickListener,null, posListener,
//				R.string.dialog_ok, R.string.dialog_cancel, "Sharing With");
//		
//	}
//	public ArrayList<Bitmap> getUsersImages(ArrayList<UserModel> users)
//	{
//		ArrayList<Bitmap> bmps = new ArrayList<Bitmap>();
//		for(UserModel user: users)
//		{
//			if(user.image!= null)
//			bmps.add(BitmapFactory.decodeByteArray(user.image, 0, user.image.length));
//			else
//				bmps.add(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.catag_personal));
//		}
//		return bmps;
//	}

}
