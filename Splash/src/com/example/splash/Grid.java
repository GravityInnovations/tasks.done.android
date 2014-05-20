package com.example.splash;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

public class Grid extends Activity implements OnClickListener{
	ArrayAdapter myStringArray;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_activity);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
	    
		gridview.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1));
//	    gridview.setOnItemClickListener(new OnItemClickListener() {
//	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//	            Toast.makeText(Grid.this, "" + position, Toast.LENGTH_SHORT).show();
//	        }
//	    });
	  
	    
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
