package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gravity.innovations.tasks.done.DemoHelper.DashBoardDemo;

public class DashboardFragment extends Fragment implements DashBoardDemo {
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

	@SuppressLint("NewApi")
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
		if (Common.flag_demoDashboard) {
			SharedPreferences prefs = mActivity.getSharedPreferences(
					Common.PREFS_DEMO, Context.MODE_PRIVATE);
			Boolean demoFlag = prefs.getBoolean(
					Common.PREFS_KEY_DEMO_DASHBOARD, false);

			if (!demoFlag) {
				demo2_fragDashInstruction();
			}
		}
		return rootView;
	}

	@Override
	public void demo1_openDashInstructions() {
		// TODO Auto-generated method stub
		// This function is overridin in NDF
	}

	@Override
	public void demo2_fragDashInstruction() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText(R.string.demo_dashboard_instructions);

		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				dialog.dismiss();
				demo3_fragDashSearch();
			}
		});

		dialog.show();
	}

	@Override
	public void demo3_fragDashSearch() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);

		instructions.setText(R.string.demo_dashboard_search);
		iv_search.startAnimation(Common.Demo.getInfiniteDemoAnim());
		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				dialog.dismiss();
				iv_search.clearAnimation();
				demo4_fragDashSearch();
			}
		});

		dialog.show();
	}

	@Override
	public void demo4_fragDashSearch() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText(R.string.demo_dashboard_search_instructions);

		iv_search.performClick();

		Button btn = (Button) dialog.findViewById(R.id.got_it);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				dialog.dismiss();
				demo5_fragDashEnd();
			}
		});

		dialog.show();
	}

	@Override
	public void demo5_fragDashEnd() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(mActivity,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_demo_overlay);

		TextView instructions = (TextView) dialog
				.findViewById(R.id.instructions);
		instructions.setText(R.string.demo_end);

		Button btn = (Button) dialog.findViewById(R.id.got_it);
		iv_cancel.performClick();
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				dialog.dismiss();
				SharedPreferences settings = ((Context) mActivity)
						.getSharedPreferences(Common.PREFS_DEMO,
								Context.MODE_PRIVATE);
				Editor editor = settings.edit();
				editor.putBoolean(Common.PREFS_KEY_DEMO_DASHBOARD, true);
				editor.commit();
			}
		});

		dialog.show();
	}
}
