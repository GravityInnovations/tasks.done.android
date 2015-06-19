package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DashboardFragment extends Fragment {
	private RecyclerView mRecyclerView;
	public DashboardAdapter mTaskAdapter;
	NavigationDrawerFragment mNavigationDrawerFragment;
	Activity mActivity;
	// private AppHandlerService mService;
	private ArrayList<TaskModel> tasks;
	private ImageButton btn_toggleNavDrawer;
	ImageView iv_search, iv_cancel;
	EditText search_et;
	RelativeLayout search_layout_relative, search_layout_replica_relative;

	public void newInstance(ArrayList<TaskModel> _tasks,
			NavigationDrawerFragment mNavigationDrawerFragment,
			AppHandlerService mService) {
		Collections.sort(_tasks, new Comparator<TaskModel>() {
			public int compare(TaskModel obj1, TaskModel obj2) {
				return obj1.getDateTime(obj1).compareTo(obj2.getDateTime(obj2));
			}
		});
		this.tasks = _tasks;
		this.mNavigationDrawerFragment = mNavigationDrawerFragment;
		// this.mService = mService;
	}

	public Fragment getFragment() {
		return this;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	public TaskModel getItem(int position) {
		TaskModel task = tasks.get(position);
		return task;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_dashboard,
				container, false);

		if (true) {
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

			iv_search = (ImageView) header.findViewById(R.id.search_iv);
			iv_search.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						Animation anim = AnimationUtils.loadAnimation(
								mActivity, android.R.anim.fade_in);
						search_layout_relative.setVisibility(View.VISIBLE);
						search_layout_relative.startAnimation(anim);
						Common.SoftKeyboard.show(search_et, mActivity);
					} catch (Exception e) {
						Log.e("DashboardFragment:iv_search:SearchFadeIn",
								e.getLocalizedMessage());
					}
				}
			});

			iv_cancel = (ImageView) header.findViewById(R.id.cancel_iv);
			iv_cancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						Animation anim = AnimationUtils.loadAnimation(
								mActivity, android.R.anim.fade_out);
						search_et.setText("");
						search_layout_relative.setVisibility(View.GONE);
						search_layout_relative.startAnimation(anim);
						Common.SoftKeyboard.hide(mActivity);
					} catch (Exception e) {
						Log.e("DashboardFragment:iv_cancel:SearchFadeOut",
								e.getLocalizedMessage());
					}
				}
			});

			btn_toggleNavDrawer = ((ImageButton) rootView
					.findViewById(R.id.iv_drawer_toggle));
			btn_toggleNavDrawer.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mNavigationDrawerFragment.openNavigationDrawer(-1);

				}
			});
			mActivity = getActivity();

			mRecyclerView = (RecyclerView) rootView
					.findViewById(R.id.recyclerView_dashboard);
			LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(
					mActivity);
			mRecyclerView.setLayoutManager(mLinearLayoutManager);

			mTaskAdapter = new DashboardAdapter(getActivity(),
					mNavigationDrawerFragment, tasks);

			mRecyclerView.setAdapter(mTaskAdapter);

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
			int taskCount = 0;
			taskCount = mTaskAdapter.getItemCount();
			TextView tv_taskCount = (TextView) header
					.findViewById(R.id.tv_pending_tasks);
			tv_taskCount.setText(String.valueOf(taskCount) + " Tasks Pending");
		}
		return rootView;
	}

}
