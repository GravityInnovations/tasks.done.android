package com.gravity.innovations.tasks.done.view.pager;

import com.gravity.innovations.tasks.done.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LayoutOneViewPager extends Fragment {
	
	public static Fragment newInstance(Context context) {
		LayoutOneViewPager fragment = new LayoutOneViewPager();
		// TODO Auto-generated method stub
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.viewpager_layoutone, null);
		return rootView;//super.onCreateView(inflater, container, savedInstanceState);
	}

}
