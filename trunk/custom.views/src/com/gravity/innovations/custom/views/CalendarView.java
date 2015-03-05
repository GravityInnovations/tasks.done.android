package com.gravity.innovations.custom.views;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;


public class CalendarView extends RelativeLayout{

	private RecyclerView body;
	private CalendarAdapter mBodyAdapter;
	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
//		RelativeLayout.LayoutParams params = 
//		(RelativeLayout.LayoutParams)this.getLayoutParams();
//		params.addRule(RelativeLayout., anchor)
		// TODO Auto-generated constructor stub
	}
	private void init(Context mContext)
	{
		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.calendarview, this);
		body = (RecyclerView)findViewById(R.id.cal);
		
		StaggeredGridLayoutManager mLinearLayoutManager =
				
				new StaggeredGridLayoutManager(
				7,StaggeredGridLayoutManager.VERTICAL);
		body.setLayoutManager(mLinearLayoutManager);
		ArrayList<Calendar> markDates = new ArrayList<Calendar>();
		
		 mBodyAdapter = new CalendarAdapter(body,markDates);
		boolean b = mLinearLayoutManager.supportsPredictiveItemAnimations();
		//body.addStatesFromChildren();
		body.setAddStatesFromChildren(true);
		body.setAdapter(mBodyAdapter);
		
		//body.setItemAnimator(null);
		//body.scrollToPosition(500);
		//mLinearLayoutManager.assertInLayoutOrScroll("message");
	}
	
	public void ScrollToToday()
	{
		mBodyAdapter.scrollToToday();
	}
	
}
