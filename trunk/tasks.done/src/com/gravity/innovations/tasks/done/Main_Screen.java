package com.gravity.innovations.tasks.done;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Main_Screen extends Activity {
	Button add_btn;
	ListView Task_listview;
	ArrayList<Task> task_data = new ArrayList<Task>();
	TaskAdapter cAdapter;
	DatabaseHelper db;
	String Toast_msg;
	private Activity mActivity;
	private Context mContext = this;
	private int selCount = 0; // for CAB multi select count
								

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		try {
			Task_listview = (ListView) findViewById(R.id.list);
			Task_listview.setItemsCanFocus(true);
			// Addition started here
			mActivity = this;
			Task_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			Task_listview
					.setMultiChoiceModeListener(new MultiChoiceModeListener() {

						@SuppressLint("NewApi")
						@Override
						public boolean onActionItemClicked(
								android.view.ActionMode mode, MenuItem item) {
							// TODO Auto-generated method stub
							switch (item.getItemId()) {
							case R.id.item_delete:
								new AlertDialog.Builder(Main_Screen.this)
										.setIcon(
												android.R.drawable.ic_dialog_alert)
										.setTitle(R.string.delete)
										.setMessage(
												getString(R.string.delete_message_confirm))
										.setPositiveButton(
												R.string.delete,
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface di,
															int i) {

														for (Integer temp : cAdapter
																.getSelectedTasks())
															db.Delete_Task(temp);
														Toast_msg = "Data Deleted successfully";
														Show_Toast(Toast_msg);
														selCount = 0;
														Set_Referash_Data();

													}
												})
										.setNegativeButton(
												R.string.dialog_cancel,
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface di,
															int i) {
														selCount = 0;
													}
												}).show();

								// item delete with dialog

								mode.finish();
								break;
							// edit

							case R.id.item_edit:
								createADialog(cAdapter
										.getSingularSelectedTask());

								mode.finish();
								break;
							// edit
							}
							return true;
						}

						@Override
						public boolean onCreateActionMode(
								android.view.ActionMode mode, Menu menu) {
							// TODO Auto-generated method stub
							MenuInflater inflater = getMenuInflater();
							inflater.inflate(R.menu.contextual_menu, menu);
							return true;
						}

						@Override
						public void onDestroyActionMode(
								android.view.ActionMode mode) {
							// TODO Auto-generated method stub
							// cAdapter.clearSelection();
						}

						@Override
						public boolean onPrepareActionMode(
								android.view.ActionMode mode, Menu menu) {
							// TODO Auto-generated method stub
							if (selCount == 1) {
								MenuItem item = menu.findItem(R.id.item_edit);
								item.setVisible(true);
								return true;
							} else {
								MenuItem item = menu.findItem(R.id.item_edit);
								item.setVisible(false);
								return true;
							}
							// return false;
						}

						// @SuppressLint("NewApi")
						@Override
						public void onItemCheckedStateChanged(
								android.view.ActionMode mode, int position,
								long id, boolean checked) {
							if (checked) {
								selCount++; // cab edit
								cAdapter.setNewSelection(position, checked);
							} else {
								selCount--; // cab edit
								cAdapter.removeSelection(position);
							}

							// cab edit
							mode.setTitle(selCount + " selected");

							mode.invalidate(); // Add this to Invalidate CAB
							// cab edit

						}

					});

			Task_listview
					.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							// TODO Auto-generated method stub

							Task_listview.setItemChecked(position,
									!cAdapter.isPositionChecked(position));

							return false;
						}
					});
			Task_listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
					Task_listview.setItemChecked(pos, true);
				}
			});
			// Addition ends here
			Set_Referash_Data();

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("some error", "" + e);
		}

	}

	public void Set_Referash_Data() {
		task_data.clear();
		db = new DatabaseHelper(this);
		ArrayList<Task> contact_array_from_db = db.Get_Tasks();

		for (int i = 0; i < contact_array_from_db.size(); i++) {

			int tidno = contact_array_from_db.get(i).getID();
			String title = contact_array_from_db.get(i).getTitle();
			String details = contact_array_from_db.get(i).getDetails();
			String notes = contact_array_from_db.get(i).getNotes();
			Task cnt = new Task();
			cnt.setID(tidno);
			cnt.setTitle(title);
			cnt.setDetails(details);
			cnt.setNotes(notes);

			task_data.add(cnt);
		}
		db.close();
		cAdapter = new TaskAdapter(Main_Screen.this, R.layout.listview_row,
				task_data);
		Task_listview.setAdapter(cAdapter);
		cAdapter.notifyDataSetChanged();
	}

	public void Show_Toast(String msg) {

		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Set_Referash_Data();
	}

	public void createADialog(final Task task) {
		View view = getLayoutInflater().inflate(R.layout.add_dialog, null);
		final EditText add_title = (EditText) view.findViewById(R.id.add_title);
		final EditText add_details = (EditText) view
				.findViewById(R.id.add_details);
		final EditText add_notes = (EditText) view.findViewById(R.id.add_notes);
		add_details.setText(task._task_details);
		add_title.setText(task._task_title);
		add_notes.setText(task._task_notes);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setView(view);
		if (task._id == -1) {
			dialog.setTitle("New Task");
			// new task
		} else {
			dialog.setTitle("Edit Task");
			// update
		}
		dialog.setPositiveButton(R.string.dialog_save,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						try {

							if (task._id == -1) {
								// create
								String title = add_title.getText().toString();
								Log.d(title, "this is the title");
								String details = add_details.getText()
										.toString();
								String notes = add_notes.getText().toString();

								if (title.length() != 0
										&& details.length() != 0
										&& notes.length() != 0) {

									db.Add_Task(new Task(title, details, notes));
									Toast_msg = "Data inserted successfully";
									Show_Toast(Toast_msg);
									Set_Referash_Data();
								}
							} else {
								// update
								String title = add_title.getText().toString();
								Log.d(title, "this is the title");
								String details = add_details.getText()
										.toString();
								String notes = add_notes.getText().toString();
								if (title.length() != 0
										&& details.length() != 0
										&& notes.length() != 0) {

									// for(Task temp :
									// cAdapter.getSingularSelectedTask())//.getCurrentCheckedPosition());

									db.Update_Task(new Task(task._id, title,
											details, notes));
									db.close();
									Toast_msg = "Data Updated successfully";
									Show_Toast(Toast_msg);
									Set_Referash_Data();
								}

							}
							Toast_msg = "Data updated successfully";
							Show_Toast(Toast_msg);
							selCount = 0;// task count after action
							Set_Referash_Data();

						} catch (Exception e) {
							Log.d("Hey! got an exception", "some weird error");
						}
					}
				});

		dialog.setNegativeButton(R.string.dialog_cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
						selCount = 0;// task count after action
					}
				});
		dialog.show();

		// item edit with dialog

	} // eof createADialog

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.ic_action_new_btn:
			this.createADialog(new Task());
			return true;
		case R.id.action_settings:
			// openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
