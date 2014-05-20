package com.gravity.innovations.manager.call.sms;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	public String query;
	public SectionsPagerAdapter(FragmentManager fm, String query) {
		
		super(fm);
		this.query = query;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class
		// below).
		switch(position)
		{
			case 0:
				return Form_selectionFragment.newInstance();
			case 1:
				return QueryFragment.newInstance("some query");
		}
		return Form_smsBlockerFragment.newInstance();
	}
	@Override
	public int getCount() {
		// Show 3 total pages.
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return "Form";
		case 1:
			return "Query Editor";
		}
		return null;
	}
}
