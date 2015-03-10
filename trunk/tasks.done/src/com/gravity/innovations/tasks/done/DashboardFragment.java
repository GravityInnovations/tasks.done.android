package com.gravity.innovations.tasks.done;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DashboardFragment extends Fragment {
	private RecyclerView mRecyclerView;
	private TextView // tv_listTitle,
	tv_footerSyncedTime;
	public DashboardAdapter mTaskAdapter;
	// DashboardAdapter mAdapter;
	NavigationDrawerFragment mNavigationDrawerFragment;
	Activity mActivity;
	private AppHandlerService mService;
	private ArrayList<TaskModel> tasks;
	ListView mListView;
	private ImageButton btn_toggleNavDrawer;
	ImageView iv_search, iv_cancel;
	EditText search_et;
	RelativeLayout search_layout_relative, search_layout_replica_relative;

	public void newInstance(ArrayList<TaskModel> _tasks,
			NavigationDrawerFragment mNavigationDrawerFragment,
			AppHandlerService mService
	// ,TaskListAdapter _mTaskListAdapter
	) {
		// TODO Auto-generated method stub
		this.tasks = _tasks;
		// updateRelativeTime();
		this.mNavigationDrawerFragment = mNavigationDrawerFragment;
		// this.mThumbIds= mThumbIds;
		this.mService = mService;
	}

	public Fragment getFragment() {
		return this;
	}

	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	// private ShapeDrawable returnButtonShape(String hex) {
	// ShapeDrawable circle;
	// circle = new ShapeDrawable(new OvalShape());
	// // circle.setBounds(10, 10, 20, 20);
	// // circle.setPadding(14, 15, 10, 10);// L,T,R,B
	// circle.getPaint().setColor(Color.parseColor(hex));
	// return circle;
	// }

	public TaskModel getItem(int position) {
		TaskModel task = tasks.get(position);
		return task;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_dashboard,
				container, false);

		if (true) {// (data.tasks != null && data.tasks.size() > 0) {
			// header on each fragment
			RelativeLayout header = (RelativeLayout) rootView
					.findViewById(R.id.header);

			search_et = (EditText) rootView.findViewById(R.id.search_et);

			search_layout_relative = (RelativeLayout) header
					.findViewById(R.id.search_layout_relative);

			search_layout_replica_relative = (RelativeLayout) header
					.findViewById(R.id.search_layout_relative_replica);
			// initial states of views
			search_layout_relative.setVisibility(View.GONE);
			search_layout_replica_relative.setVisibility(View.VISIBLE);

			/*
			 * code below opens soft keyboard on imageClick
			 */
			iv_search = (ImageView) header.findViewById(R.id.search_iv);
			// iv_search.setFocusableInTouchMode(true);
			iv_search.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					try{
					Animation anim = AnimationUtils
							.loadAnimation(mActivity, R.anim.fade_in);
					search_layout_relative.setVisibility(View.VISIBLE);
					search_layout_relative
							.startAnimation(anim);
					}catch(Exception e){
						Log.e("DashboardFragment:iv_search:SearchFadeIn",e.getLocalizedMessage());
					}
				}
			});

			iv_cancel = (ImageView) header.findViewById(R.id.cancel_iv);
			iv_cancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
				try{
					Animation anim = AnimationUtils
							.loadAnimation(mActivity, R.anim.fade_out);
					search_layout_relative.setVisibility(View.GONE);
					search_layout_relative
							.startAnimation(anim);
				}catch(Exception e){
					Log.e("DashboardFragment:iv_cancel:SearchFadeOut",e.getLocalizedMessage());
				}
				}
			});

			// View lv_footer = inflater.inflate(R.layout.fragment_main_footer,
			// null);

			btn_toggleNavDrawer = ((ImageButton) rootView
					.findViewById(R.id.iv_drawer_toggle));
			btn_toggleNavDrawer.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mNavigationDrawerFragment.openNavigationDrawer(-1);

				}
			});
			mActivity = getActivity();

			// ArrayList<Bitmap> users_images = getUsersImages(this.data.users);

			// mUsersAdapter = new BitmapAdapter(mActivity, mGridView,
			// R.layout.grid_cell, this.data.users, mActivity);

			// mGridView.setAdapter(mUsersAdapter);

			mRecyclerView = (RecyclerView) rootView
					.findViewById(R.id.recyclerView_dashboard);
			LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(
					mActivity);
			mRecyclerView.setLayoutManager(mLinearLayoutManager);

			// mTaskAdapter = new TaskAdapter
			// (getActivity(),
			// R.layout.task_listview_row, data,
			// mNavigationDrawerFragment, /* data.tasks, */
			// selectedTaskID, lv_footer, mRecyclerView);
			mTaskAdapter = new DashboardAdapter(getActivity(),
					R.layout.row_task_dashboard/* task_listview_row */,
					mNavigationDrawerFragment, /* data. */tasks);// , lv_footer, search_et);

			mRecyclerView.setAdapter(mTaskAdapter);
			try {
				// RecyclerViewAdapter.ViewHolder viewHolder = new
				// RecyclerViewAdapter.ViewHolder(lv_footer,1);
				// recyclerView.addView(viewHolder.itemView);
				/*********************
				 * mTaskAdapter.createViewHolder(mRecyclerView, 0);
				 **************/
				// (lv_footer,
				// 1);//.bindViewHoldeer(viewHolder,
				// 1);
				// m.addView(lv_footer, 0);
				// recyclerView//.addView(lv_footer);
			} catch (Exception e) {
				String s = e.getLocalizedMessage();
			}
			// mRecyclerView.addView(lv_footer);

			/**********/
			mRecyclerView.setItemAnimator(new DefaultItemAnimator());
			search_et.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					String text = search_et.getText().toString()
							.toLowerCase(Locale.getDefault());
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					mTaskAdapter.getFilter().filter(s.toString());
				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});

			int taskCount = mTaskAdapter.getItemCount();
			TextView tv_taskCount = (TextView) header
					.findViewById(R.id.tv_pending_tasks);
			tv_taskCount.setText(String.valueOf(taskCount) + " Tasks Pending");

		}
		return rootView;
	}

}
