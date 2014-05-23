package com.gravity.innovations.tasks.done;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

;

public class AddTask extends Activity implements OnClickListener {
	Button sqlUpdate;
	EditText sqlTitle, sqlDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_task);
		sqlUpdate = (Button) findViewById(R.id.bSQLUpdate);

		sqlTitle = (EditText) findViewById(R.id.etSQLTitle);
		sqlDetails = (EditText) findViewById(R.id.etSQLDetail);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.bSQLUpdate:

			String title = sqlTitle.getText().toString();
			String details = sqlDetails.getText().toString();
			Tasks entry = new Tasks(AddTask.this);
			entry.open();
			entry.createEntry(title, details);
			entry.close();
			break;
		}
	}
}