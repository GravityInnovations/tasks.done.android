package com.gravity.innovations.manager.call.sms;


import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


public  class Form_smsBlockerFragment extends Fragment {
	private MainMenuItem data;
	private Activity activity;
	public static Form_smsBlockerFragment newInstance() {
		Form_smsBlockerFragment fragment = new Form_smsBlockerFragment();
		Bundle args = new Bundle();
		return fragment;
	}
	
	public Form_smsBlockerFragment() {
		//this.data = data;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_form_smsblocker, container,
				false);
		AutoCompleteTextView textView = (AutoCompleteTextView)rootView.findViewById(R.id.multiAutoCompleteTextView1);
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
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		//((FormActivity) activity).onSectionAttached(getArguments().getString(ARG_SECTION_TITLE));
	}
}

