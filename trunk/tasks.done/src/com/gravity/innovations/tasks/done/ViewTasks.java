package com.gravity.innovations.tasks.done;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ViewTasks extends Activity{// implements OnClickListener {
	Button getTasksData;
	TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_tasks);
		getTasksData = (Button) findViewById(R.id.getTaskBtn);
		tv = (TextView)findViewById(R.id.tvSQLinfo);
	//	getTasksData.setOnClickListener(new OnClickListener() {
//  comment this portion  
		Tasks task = new Tasks(this);
		task.open();
		String data = task.getData();
		task.close();
		tv.setText(data);	

//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Tasks task = new Tasks(this);
//				task.open();
//				String data = task.getData();
//				task.close();
//				tv.setText(data);
//				
//			}
//		});
	}

	
}
