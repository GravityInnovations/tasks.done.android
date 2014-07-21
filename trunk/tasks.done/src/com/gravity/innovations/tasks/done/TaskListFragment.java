package com.gravity.innovations.tasks.done;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class TaskListFragment extends Fragment {
	private TaskListModel data;
	private ListView mListView;
	public TaskAdapter mTaskAdapter;
	private int selCount = 0; // for CAB multi select count
	NavigationDrawerFragment mNavigationDrawerFragment;
	DatabaseHelper db;
	
	public TaskListFragment()
	{
		
	}
	public void newInstance(TaskListModel temp, NavigationDrawerFragment navigationDrawerFragment)
	{
		this.data = temp;
		this.mNavigationDrawerFragment = navigationDrawerFragment;
	}
	public Fragment getFragment()
	{
		return this;
	}
	
	@Override
	public void onAttach(Activity activity)
	{
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
			if (data.tasks != null && data.tasks.size() > 0) {
				mListView = (ListView) rootView.findViewById(R.id.list);
				mTaskAdapter = new TaskAdapter(getActivity(),
						R.layout.task_listview_row, data.tasks);
				mListView.setAdapter(mTaskAdapter);
				mTaskAdapter.notifyDataSetChanged();
				mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
				mListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
							@Override
							public boolean onActionItemClicked(
									android.view.ActionMode mode, MenuItem item) {
								switch (item.getItemId()) {
								case R.id.item_delete:

//									db = new DatabaseHelper(getActivity().getApplicationContext());
//									for (Integer temp : mTaskAdapter.getSelectedTaskModels())
//										db.Task_Delete(temp);
								
									break;
								case R.id.item_edit:
								//	mNavigationDrawerFragment.addOrEditTask(data, mTaskAdapter.getSingularSelectedTaskModel());
									break;
								}
								return true;
							}

							@Override
							public boolean onCreateActionMode(
									android.view.ActionMode mode, Menu menu) {
								android.view.MenuInflater inflater = mode
										.getMenuInflater();
								inflater.inflate(R.menu.contextual_menu, menu);
								return true;
							}
							@Override
							public void onDestroyActionMode(
									android.view.ActionMode mode) {
								// TODO Auto-generated method stub
							}
							@Override
							public boolean onPrepareActionMode(
									android.view.ActionMode mode, Menu menu) {
								if (selCount == 1) {
									MenuItem item = menu.findItem(R.id.item_edit);
									item.setVisible(true);
									return true;
								} else {
									MenuItem item = menu.findItem(R.id.item_edit);
									item.setVisible(false);
									return true;
								}
							}
							@Override
							public void onItemCheckedStateChanged(
									android.view.ActionMode mode, int position,
									long id, boolean checked) {
								if (checked) {
									selCount++; // cab edit
								} else {
									selCount--; // cab edit
								}
								// cab edit
								mode.setTitle(selCount + " selected");
								mode.invalidate(); // Add this to Invalidate CAB
								// cab edit
							}
						});
 

				
				
				//				
//				MultiChoiceModeListener mModeListenerTemp = new MultiChoiceModeListener() {
//					
//					@Override
//					public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//						// TODO Auto-generated method stub
//						return false;
//					}
//					
//					@Override
//					public void onDestroyActionMode(ActionMode mode) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//						// TODO Auto-generated method stub
//						return false;
//					}
//					
//					@Override
//					public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//						// TODO Auto-generated method stub
//						return false;
//					}
//					
//					@Override
//					public void onItemCheckedStateChanged(ActionMode mode, int position,
//							long id, boolean checked) {
//						// TODO Auto-generated method stub
//					}
//				};
			}
			return rootView;
	}
	
	public void AddItem(TaskModel temp)
	{
		
	}
	
}
