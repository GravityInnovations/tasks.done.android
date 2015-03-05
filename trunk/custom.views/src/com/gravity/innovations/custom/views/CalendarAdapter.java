package com.gravity.innovations.custom.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

	
	private ArrayList<CalendarObject> data = new ArrayList<CalendarObject>();
	private RecyclerView mr;
	int minyear=2013;
	int maxyear=2016;
	String[] weeks = {"S","M","T","W","T","F","S"};
	// = null;
	int todaysPosition = 0;
	public CalendarAdapter(RecyclerView v, ArrayList<Calendar> markDates)
	{
		this.mr = v;
		Calendar cal1 = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		markDates.add(cal1);
		//Calendar today = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		cal.set(minyear, Calendar.JANUARY, 1);
		int d = 1,m=Calendar.JANUARY, y = minyear;
		while(cal.get(Calendar.YEAR) <= maxyear)
        {
			d = cal.get(Calendar.DAY_OF_MONTH);
			m = cal.get(Calendar.MONTH);
			y = cal.get(Calendar.YEAR);
			CalendarObject o = new CalendarObject();
			o.title = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)
					+ " "+ y;
			
			o.type = 1;
			data.add(o);
			if(y == year && 
					m == month)
				todaysPosition = data.size()-1;
			
			for(int k=0;k<7;k++)
    		{
        		o = new CalendarObject();
            	o.title = weeks[k];//cal.getDisplayName(Calendar..DAY_OF_WEEK, Calendar.SHORT, Locale.US);
        		o.type = 2;
            	data.add(o);
    		}
			int day = cal.get(Calendar.DAY_OF_WEEK);
			for(int i=1;i<day;i++)
	        {
	        	data.add(new CalendarObject());
	        }
			int x = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			for(int i=1;i<=x;i++)
			{
				o = new CalendarObject();
				o.title = i+"";
				for(Calendar date:markDates)
				{
					if(date.get(Calendar.YEAR) == y &&
							date.get(Calendar.MONTH)== m &&
							date.get(Calendar.DAY_OF_MONTH)== i)
					{
						o.mark = true;
						markDates.remove(date);
						break;
					}
						
				}
				data.add(o);
			}
			cal.roll(Calendar.MONTH, true);
			if(cal.get(Calendar.MONTH)==Calendar.JANUARY)
			{
				cal.roll(Calendar.YEAR, true);
			}
			
        }
		scrollToToday();
		//this.mr.requestLayout();
		//this.mr.scrollToPosition(todaysPosition);
		
		//this.mr.buildDrawingCache(true);
		//this.mr.smoothScrollToPosition(todaysPosition);
		
//		this.mr.buildDrawingCache(true);
		//this.mr.scrollToPosition(todaysPosition);
		//this.notifyDataSetChanged();
  
		
	}
	public void scrollToToday()
	{
		this.mr.scrollToPosition(todaysPosition);
	}
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return data.size()-1;
	}

	

	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO Auto-generated method stub
		if(viewType == 0)
		{
			View itemLayoutView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.calendarview_body_thumb, null);

			// create ViewHolder

			ItemViewHolder viewHolder = new ItemViewHolder(itemLayoutView);
			return viewHolder;
		}
		else if(viewType == 1)
		{
			View itemLayoutView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.calendarview_header, null);
			//int i = itemLayoutView.getLayoutParams().width;//.getWidth();
			// create ViewHolder

			HeadViewHolder viewHolder = new HeadViewHolder(itemLayoutView);
			return viewHolder;
		}
		else if(viewType == 2)
		{
			View itemLayoutView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.calendarview_header_thumb, null);
			//int i = itemLayoutView.getLayoutParams().width;//.getWidth();
			// create ViewHolder

			Item2ViewHolder viewHolder = new Item2ViewHolder(itemLayoutView);
			return viewHolder;
		}
		return null;
	}
	public static class ItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mMARK;
        public ItemViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            mTextView = (TextView)itemLayoutView.findViewById(R.id.txt_item);
            try{
            	mMARK = (ImageView)itemLayoutView.findViewById(R.id.mark);
            }
            catch(Exception e)
            {
            	
            }
        }
    }
	public static class Item2ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        
        public Item2ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            mTextView = (TextView)itemLayoutView.findViewById(R.id.txt_item);
                  }
    }
	public static class HeadViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public HeadViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            mTextView = (TextView)itemLayoutView.findViewById(R.id.header_month);
        }
    }
	@Override
	public int getItemViewType(int position) {
		CalendarObject o = data.get(position);
		 return o.type;
	}
	@Override
	public void onBindViewHolder(
			android.support.v7.widget.RecyclerView.ViewHolder arg0, int position) {
		if (getItemViewType(position) == 0) {
			try{
				ItemViewHolder viewHolder = (ItemViewHolder) arg0;
				CalendarObject o = data.get(position);
				viewHolder.mTextView.setText(o.title);
				if(o.mark == true)
				//viewHolder.mMARK.setBackgroundResource(R.drawable.cal_mark);
				viewHolder.mMARK.setBackgroundColor(Color.parseColor("#ff0000"));
				else
					viewHolder.mMARK.setBackgroundColor(Color.parseColor("#ffffff"));
			}
			catch(Exception e)
			{
				String s = "";
			}
		}
		else if (getItemViewType(position) == 1) 
		{
			try{
			HeadViewHolder viewHolder = (HeadViewHolder) arg0;
			CalendarObject o = data.get(position);
			//Object o = viewHolder.itemView.getLayoutParams();
			StaggeredGridLayoutManager.LayoutParams layoutParams1 = 
					new StaggeredGridLayoutManager.LayoutParams
					(mr.getLayoutParams());
					//((StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams());
		    layoutParams1.setFullSpan(true);
		    
		    viewHolder.itemView.setLayoutParams(layoutParams1);
		    viewHolder.mTextView.setText(o.title);//.setLayoutParams(layoutParams1);
			}
			catch(Exception e){}
		}
		if (getItemViewType(position) == 2) {
			try{
				Item2ViewHolder viewHolder = (Item2ViewHolder) arg0;
				CalendarObject o = data.get(position);
				
				viewHolder.mTextView.setText(o.title);
			}
			catch(Exception e)
			{
				
			}
		}
		
	}
}
