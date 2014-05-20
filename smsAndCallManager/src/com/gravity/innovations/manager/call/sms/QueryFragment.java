package com.gravity.innovations.manager.call.sms;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public  class QueryFragment extends Fragment {
	private MainMenuItem data;
	private static final String ARG_SECTION_QUERY = "section_query";
public static QueryFragment newInstance(String query) {
		QueryFragment fragment = new QueryFragment();
		Bundle args = new Bundle();
		args.putString(ARG_SECTION_QUERY, query);
		fragment.setArguments(args);
		return fragment;
	}
	
	public QueryFragment() {
		//this.data = data;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_query, container,
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
		//((FragmentActivity) activity).onSectionAttached(getArguments().getString(ARG_SECTION_QUERY));
	}
}

