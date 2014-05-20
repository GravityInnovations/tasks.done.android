package com.gravity.innovations.manager.call.sms;


import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;

import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public  class Form_selectionFragment extends Fragment {
	private MainMenuItem data;
	private Activity activity;
	public ArrayList<SelectionCondition> list_senders_data;
	public ArrayList<SelectionCondition> list_content_data;
	public ArrayList<SelectionCondition> list_frequency_data;
	
	public ListView list_senders;
	public ListView list_contentCondition;
	public ListView list_sendingFrequency;
	
	public SelectionListViewAdapter list_senders_adapter;
	public SelectionListViewAdapter list_content_adapter;
	public SelectionListViewAdapter list_frequency_adapter;
	public static Form_selectionFragment newInstance() {
		Form_selectionFragment fragment = new Form_selectionFragment();
		Bundle args = new Bundle();
		return fragment;
	}
	
	public Form_selectionFragment() {
		//this.data = data;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_form_selection, container,
				false);
		final Activity mActivity = getActivity();
		list_senders_data = new ArrayList<SelectionCondition>();
		list_content_data = new ArrayList<SelectionCondition>();
		list_frequency_data = new ArrayList<SelectionCondition>();
		setHasOptionsMenu(true);
		ImageButton addSender = (ImageButton)rootView.findViewById(R.id.btn_add_sender);
		ImageButton addContentCondition = (ImageButton)rootView.findViewById(R.id.btn_add_contentCondition);
		ImageButton addFrequency = (ImageButton)rootView.findViewById(R.id.btn_add_sendingFrequency);
		
		/*list_senders = (ListView)inflater.inflate(
                R.id.list_senders, container, false);
		*/list_senders = (ListView)rootView.findViewById(R.id.list_senders);
		list_contentCondition = (ListView)rootView.findViewById(R.id.list_contentCondition);
		list_sendingFrequency = (ListView)rootView.findViewById(R.id.list_sendingFrequency);
		list_senders_adapter = new SelectionListViewAdapter((Context)getActivity().getApplicationContext(), list_senders_data);
		list_content_adapter = new SelectionListViewAdapter((Context)getActivity().getApplicationContext(), list_content_data);
		list_frequency_adapter = new SelectionListViewAdapter((Context)getActivity().getApplicationContext(), list_frequency_data);
		
		
		list_senders.setAdapter(list_senders_adapter);
		list_contentCondition.setAdapter(list_content_adapter);
		list_sendingFrequency.setAdapter(list_frequency_adapter);
		/*list_senders.setAdapter(new ArrayAdapter<String>(
                activity,
                android.R.layout.simple_list_item_checked,
                android.R.id.text1,
                new String[]{
                        getString(R.string.title_section1),
                        getString(R.string.title_section2),
                        getString(R.string.title_section3),
                }));
		*/
		
		
		addSender.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String options[] = {"Contact", "Unsaved Number","Series"};
				AlertDialog.Builder mDialog = new AlertDialog.Builder(mActivity);
				  mDialog.setTitle("Please select a condition type");
				  //mDialog.setMessage("");
				  mDialog.setItems(options, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int which) {
		            	   
		            	   final EditText data1 = new EditText(activity);
		            	   switch(which){
		            	  	case 0://contact
		            	  		 Intent i = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
		 		           		i.setType(Phone.CONTENT_TYPE);
		 		           		startActivityForResult(i,CommonKeys.DIALOG_PICK_CONTACT);
		            	  		break;
		            	  	case 1://number
		            	  		AlertDialog.Builder mDialog_number = new AlertDialog.Builder(mActivity);
		            	  		mDialog_number.setTitle("Input the Mobile Number");
		            	  		data1.setInputType(InputType.TYPE_CLASS_PHONE);
		            	  		mDialog_number.setView(data1);
		            	  		mDialog_number.setPositiveButton("Add", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
											
										Intent IntentData = new Intent();
										String num = data1.getText().toString();
										IntentData.putExtra(CommonKeys.NUMBER,
												 num);
										onActivityResult(CommonKeys.DIALOG_ADD_NUMBER,
												CommonKeys.DUMMY_RESULT_CODE,
												IntentData);
										//data.putExtra("requestCode", dialog);
									}
								});
		            	  		
		            	  		mDialog_number.create().show();
		            	  		
		            	  		break;
		            	  	case 2://series
		            	  		LinearLayout mLayout= new LinearLayout(activity);
		            	  		data1.setInputType(InputType.TYPE_CLASS_PHONE);
		            	  		mLayout.setOrientation(LinearLayout.VERTICAL);
		            	  		AlertDialog.Builder mDialog_series = new AlertDialog.Builder(mActivity);
		            	  		mDialog_series.setTitle("Input Series");
		            	  		
		            	  		mLayout.addView(data1);
		            	  		//mLayout.addView(data1);
		            	  		
		            	  		final String choices[] = {"In the beganing","In Middle", "In the End"};
		            	  		final ListView options = new ListView(activity);
		            	  		
		            	  		options.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		            	  		options.setAdapter(new ArrayAdapter<String>(activity, 
		            	  				android.R.layout.simple_list_item_multiple_choice
		            	  				,choices));
		            	  		mLayout.addView(options);
		            	  		mDialog_series.setView(mLayout);
		            	  		mDialog_series.setPositiveButton("Add", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										SparseBooleanArray checked = options.getCheckedItemPositions();
										for (int i = 0; i < 3 ; i++)
										 if (checked.get(i)) {
											 data1.getText().toString();
											 Intent IntentData = new Intent();
												String series = data1.getText().toString();
												String number = series;
												if(i == 0)
													number =  series+"*";
												else if(i == 1)
													number = "*"+ series+"*";
												else if(i == 2)
													number =  "*"+series;
												IntentData.putExtra(CommonKeys.SERIES,
														 number);
												
												onActivityResult(CommonKeys.DIALOG_ADD_SERIES,
														CommonKeys.DUMMY_RESULT_CODE,
														IntentData);
										 }
										
										//data.putExtra("requestCode", dialog);
									}
								});
		            	  		
		            	  		mDialog_series.create().show();
		            	  		
		            	  		break;
		            	  }
		            	  
		               }
		        });
				  
				  mDialog.create().show();
				// TODO Auto-generated method stub
				
			}
		});
		addContentCondition.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final EditText data1 = new EditText(activity);
         	   
				LinearLayout mLayout= new LinearLayout(activity);
    	  		data1.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
				mLayout.setOrientation(LinearLayout.VERTICAL);
    	  		AlertDialog.Builder mDialog_content = new AlertDialog.Builder(mActivity);
    	  		mDialog_content.setTitle("Input Text Content");
    	  		
    	  		mLayout.addView(data1);
    	  		//mLayout.addView(data1);
    	  		
    	  		final String choices[] = {"In the beganing","In Middle", "In the End"};
    	  		final ListView options = new ListView(activity);
    	  		
    	  		options.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    	  		options.setAdapter(new ArrayAdapter<String>(activity, 
    	  				android.R.layout.simple_list_item_multiple_choice
    	  				,choices));
    	  		mLayout.addView(options);
    	  		mDialog_content.setView(mLayout);
    	  		mDialog_content.setPositiveButton("Add", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						SparseBooleanArray checked = options.getCheckedItemPositions();
						for (int i = 0; i < 3 ; i++)
						 if (checked.get(i)) {
							 data1.getText().toString();
							 Intent IntentData = new Intent();
								String series = data1.getText().toString();
								String number = series;
								if(i == 0)
									number =  series+"*";
								else if(i == 1)
									number = "*"+ series+"*";
								else if(i == 2)
									number =  "*"+series;
								IntentData.putExtra(CommonKeys.CONTENT,
										 number);
								onActivityResult(CommonKeys.DIALOG_ADD_CONTENT,
										CommonKeys.DUMMY_RESULT_CODE,
										IntentData);
						 }
						
						//data.putExtra("requestCode", dialog);
					}
				});
    	  		mDialog_content.create().show();
				
			}
		});
		addFrequency.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final EditText data1 = new EditText(activity);
				final EditText data2 = new EditText(activity);
	         	
				LinearLayout mLayout= new LinearLayout(activity);
    	  		data1.setInputType(InputType.TYPE_CLASS_NUMBER);
    	  		data2.setInputType(InputType.TYPE_CLASS_NUMBER);
				
    	  		mLayout.setOrientation(LinearLayout.VERTICAL);
    	  		AlertDialog.Builder mDialog_content = new AlertDialog.Builder(mActivity);
    	  		mDialog_content.setTitle("Frequency");
    	  		data1.setHint("Number of sms/call");
    	  		data2.setHint("Interval in minute");
    	  		
    	  		mLayout.addView(data1);
    	  		mLayout.addView(data2);
    	  		//mLayout.addView(data1);
    	  		
    	  		
    	  		mDialog_content.setView(mLayout);
    	  		mDialog_content.setPositiveButton("Add", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						
						Intent IntentData = new Intent();
								IntentData.putExtra(CommonKeys.FREQUENCY,
										data1.getText().toString());
								IntentData.putExtra(CommonKeys.PERIOD,
										data2.getText().toString());
								onActivityResult(CommonKeys.DIALOG_ADD_FREQUENCY,
										CommonKeys.DUMMY_RESULT_CODE,
										IntentData);
						
						//data.putExtra("requestCode", dialog);
					}
				});
    	  		mDialog_content.create().show();
				
			}
		});
		
		/*AutoCompleteTextView textView = (AutoCompleteTextView)rootView.findViewById(R.id.multiAutoCompleteTextView1);
		// Get the string array
		String contactName = null;
		String contactNumber = null;
		ArrayList<String> contacts = new ArrayList<String>();
		Context context = getActivity().getApplicationContext();
		final String[] projection = { Phone._ID, Phone.CONTENT_TYPE, Phone.NUMBER };
		
		Cursor cursor = context.getContentResolver().query(Phone.CONTENT_URI, null, Phone.HAS_PHONE_NUMBER, null, null);
		while (cursor.moveToNext())
		{
		    contactName  = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME)); 
		    contactNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
		    contacts.add(contactName + " ("+contactNumber+")"); 
		}

		// Create the adapter and set it to the AutoCompleteTextView 
		if(getActivity() == activity){
		ArrayAdapter<String> adapter = 
		        new ArrayAdapter<String>(this.activity, android.R.layout.select_dialog_item,contacts);
		
		try{
		textView.setAdapter(adapter);
		}
		catch(Exception ex)
		{
			String as = ex.getMessage();
		}
		}
		/*TextView textView = (TextView) rootView
				.findViewById(R.id.textView1);
		textView.setText(getArguments().getString(ARG_SECTION_DESCRIPTION));
		*/
		return rootView;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_save) {
			ArrayList<String> test = new ArrayList<String>();
			test.add("Faik");
			test.add("malik");
			test.add("some");
			AlertDialog.Builder mDialog = new AlertDialog.Builder(activity);
			  mDialog.setTitle("JSON DATA");
			  String x;
				try {
					x = customJSONSerializer.getJSONObject(list_senders_data,"Senders").toString(1);
					mDialog.setMessage(x);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			  //customJSONSerializer.getJSONGroups(list_senders_data).toString();
			  
			  mDialog.create().show();
			return true;
		}
		return false;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		SelectionCondition temp = new SelectionCondition();
		Bundle extras;
		switch(requestCode)
		{
			case CommonKeys.DIALOG_PICK_CONTACT:
				ContentResolver cr = activity.getContentResolver();
			    Cursor cursor = activity.getContentResolver().query(data.getData(), null, null, null, null);
			    cursor.moveToFirst();
			    int column = cursor.getColumnIndex(Phone.DISPLAY_NAME);
	            String number = cursor.getString(column);
	          
	            temp.parentProperty = CommonKeys.SENDERS;
	            temp.Values.add(new SelectionCondition.value(
	            		Phone._ID,
	            		cursor.getString(cursor.getColumnIndex(Phone._ID))
	            		));
	            temp.Values.add(new SelectionCondition.value(
	            		CommonKeys.PROPRETY_TYPE,
	            		CommonKeys.CONTACT
	            		));
	            temp.Values.add(new SelectionCondition.value(
	            		CommonKeys.DATA1,
	            		cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME))
	            		));
	            temp.Values.add(new SelectionCondition.value(
	            		CommonKeys.DATA2,
	            		cursor.getString(cursor.getColumnIndex(Phone.NORMALIZED_NUMBER))
	            		));
	            list_senders_data.add(temp);
	            
	    		//list.setText(getArguments().getString(ARG_SECTION_DESCRIPTION));
				break;
			case CommonKeys.DIALOG_ADD_NUMBER:
				temp.parentProperty = CommonKeys.SENDERS;
				extras = data.getExtras();
				String num = extras.getString(CommonKeys.NUMBER);
	            temp.Values.add(new SelectionCondition.value(
	            		CommonKeys.DATA1,
	            		num));
	            temp.Values.add(new SelectionCondition.value(
	            		CommonKeys.PROPRETY_TYPE,
	            		CommonKeys.NUMBER
	            		));
				list_senders_data.add(temp);
				break;
			case CommonKeys.DIALOG_ADD_SERIES:
				temp.parentProperty = CommonKeys.SENDERS;
				extras = data.getExtras();
				
	            temp.Values.add(new SelectionCondition.value(
	            		CommonKeys.DATA1,
	            		extras.getString(CommonKeys.SERIES)));
	            temp.Values.add(new SelectionCondition.value(
	            		CommonKeys.PROPRETY_TYPE,
	            		CommonKeys.SERIES
	            		));
				list_senders_data.add(temp);
				break;
			case CommonKeys.DIALOG_ADD_CONTENT:
				temp.parentProperty = CommonKeys.CONTENT;
				extras = data.getExtras();
				
	            temp.Values.add(new SelectionCondition.value(
	            		CommonKeys.DATA1,
	            		extras.getString(CommonKeys.CONTENT)));
	            
	            temp.Values.add(new SelectionCondition.value(
	            		CommonKeys.PROPRETY_TYPE,
	            		CommonKeys.CONTENT
	            		));
	           
				list_content_data.add(temp);
				break;	
			case CommonKeys.DIALOG_ADD_FREQUENCY:
				temp.parentProperty = CommonKeys.FREQUENCY;
				extras = data.getExtras();
				
	            temp.Values.add(new SelectionCondition.value(
	            		CommonKeys.DATA1,
	            		extras.getString(CommonKeys.FREQUENCY)));
	            temp.Values.add(new SelectionCondition.value(
	            		CommonKeys.DATA2,
	            		extras.getString(CommonKeys.PERIOD)));
	            
	            temp.Values.add(new SelectionCondition.value(
	            		CommonKeys.PROPRETY_TYPE,
	            		CommonKeys.PERIOD
	            		));
				list_frequency_data.add(temp);
				break;	
			
		}
		list_senders_adapter.notifyDataSetChanged();
		list_frequency_adapter.notifyDataSetChanged();
		list_content_adapter.notifyDataSetChanged();
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		//((FormActivity) activity).onSectionAttached(getArguments().getString(ARG_SECTION_TITLE));
	}
}

