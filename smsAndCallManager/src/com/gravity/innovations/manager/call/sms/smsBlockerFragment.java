package com.gravity.innovations.manager.call.sms;


import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public  class smsBlockerFragment extends Fragment {
	private MainMenuItem data;
	private static final String ARG_SECTION_TITLE = "section_title";
	private static final String ARG_SECTION_DESCRIPTION = "section_desc";
public static smsBlockerFragment newInstance(MainMenuItem data) {
		smsBlockerFragment fragment = new smsBlockerFragment();
		Bundle args = new Bundle();
		args.putString(ARG_SECTION_DESCRIPTION, data.Description);
		args.putString(ARG_SECTION_TITLE, data.Title);
		args.putString(CommonKeys.SMS_BLOCKER_FRAGMENT_TOKEN, data.FragmentToken);
		fragment.setArguments(args);
		return fragment;
	}
	
	public smsBlockerFragment() {
		//this.data = data;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_smsblocker, container,
				false);
		/*TextView textView = (TextView) rootView
				.findViewById(R.id.textView1);
		textView.setText(getArguments().getString(ARG_SECTION_DESCRIPTION));
		*/
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getString(ARG_SECTION_TITLE));
	}
}

