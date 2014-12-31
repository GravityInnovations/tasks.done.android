package com.gravity.innovations.tasks.done.view.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
	private Context _context;
	public static int totalPage = 5;

	public ViewPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		_context = context;

	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = new Fragment();
		switch (position) {
		case 0:
			fragment = LayoutOneViewPager.newInstance(_context);
			break;
		case 1:
			fragment = LayoutTwoViewPager.newInstance(_context);
			break;
		case 2:
			fragment = LayoutOneViewPager.newInstance(_context);
			break;
		case 3:
			fragment = LayoutTwoViewPager.newInstance(_context);
			break;
		case 4:
			fragment = LayoutOneViewPager.newInstance(_context);
			break;

		}
		return fragment;
	}

	@Override
	public int getCount() {
		return totalPage;
	}

}
