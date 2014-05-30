package com.gravity.innovations.tasks.done;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Add_Update_Task extends Activity {
    EditText add_title, add_details, add_notes;
    Button add_save_btn, add_view_all, update_btn, update_view_all;
    LinearLayout add_view, update_view;
    String valid_details = null, valid_notes = null, valid_title = null,
	    Toast_msg = null, valid_user_id = "";
    int USER_ID;
    DatabaseHandler dbHandler = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.add_update_screen);

	// set screen
	Set_Add_Update_Screen();

	// set visibility of view as per calling activity
	String called_from = getIntent().getStringExtra("called");

	if (called_from.equalsIgnoreCase("add")) {
	    add_view.setVisibility(View.VISIBLE);
	    update_view.setVisibility(View.GONE);
	} else {

	    update_view.setVisibility(View.VISIBLE);
	    add_view.setVisibility(View.GONE);
	    USER_ID = Integer.parseInt(getIntent().getStringExtra("USER_ID"));

	    Task c = dbHandler.Get_Task(USER_ID);

	    add_title.setText(c.getTitle());
	    add_details.setText(c.getDetails());
	    add_notes.setText(c.getNotes());
	    // dbHandler.close();
	}
	add_details.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before,
		    int count) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		// min lenth 10 and max lenth 12 (2 extra for - as per phone
		// matcher format)
		Is_Valid_Sign_Number_Validation(add_details);
	    }
	});
	add_details
		.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

	add_notes.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before,
		    int count) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		Is_Valid_Email(add_notes);
	    }
	});

	add_title.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before,
		    int count) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		Is_Valid_Person_Name(add_title);
	    }
	});

	add_save_btn.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		// check the value state is null or not
		if (valid_title != null && valid_details != null
			&& valid_notes != null && valid_title.length() != 0
			&& valid_details.length() != 0
			&& valid_notes.length() != 0) {

		    dbHandler.Add_Task(new Task(valid_title,
			    valid_details, valid_notes));
		    Toast_msg = "Data inserted successfully";
		    Show_Toast(Toast_msg);
		    Reset_Text();

		}

	    }
	});

	update_btn.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub

		valid_title = add_title.getText().toString();
		valid_details = add_details.getText().toString();
		valid_notes = add_notes.getText().toString();

		// check the value state is null or not
		if (valid_title != null && valid_details != null
			&& valid_notes != null && valid_title.length() != 0
			&& valid_details.length() != 0
			&& valid_notes.length() != 0) {

		    dbHandler.Update_Task(new Task(USER_ID, valid_title,
			    valid_details, valid_notes));
		    dbHandler.close();
		    Toast_msg = "Data Update successfully";
		    Show_Toast(Toast_msg);
		    Reset_Text();
		} else {
		    Toast_msg = "Sorry Some Fields are missing.\nPlease Fill up all.";
		    Show_Toast(Toast_msg);
		}

	    }
	});
	update_view_all.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent view_user = new Intent(Add_Update_Task.this,
			Main_Screen.class);
		view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
			| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(view_user);
		finish();
	    }
	});

	add_view_all.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent view_user = new Intent(Add_Update_Task.this,
			Main_Screen.class);
		view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
			| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(view_user);
		finish();
	    }
	});

    }

    public void Set_Add_Update_Screen() {

	add_title = (EditText) findViewById(R.id.add_title);
	add_details = (EditText) findViewById(R.id.add_details);
	add_notes = (EditText) findViewById(R.id.add_notes);

	add_save_btn = (Button) findViewById(R.id.add_save_btn);
	update_btn = (Button) findViewById(R.id.update_btn);
	add_view_all = (Button) findViewById(R.id.add_view_all);
	update_view_all = (Button) findViewById(R.id.update_view_all);

	add_view = (LinearLayout) findViewById(R.id.add_view);
	update_view = (LinearLayout) findViewById(R.id.update_view);

	add_view.setVisibility(View.GONE);
	update_view.setVisibility(View.GONE);

    }

    public void Is_Valid_Sign_Number_Validation(EditText edt) throws NumberFormatException {
	if (edt.getText().toString().length() <= 0) {
		edt.setError("Accept Alphabets Only.");
	    valid_details = null;
	} else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
	    edt.setError("Accept Alphabets Only.");
	    valid_details = null;
	} else {
	    valid_details = edt.getText().toString();

	}

    } // END OF Edittext validation

    public void Is_Valid_Email(EditText edt) throws NumberFormatException {
    	if (edt.getText().toString().length() <= 0) {
    	    edt.setError("Accept Alphabets Only.");
	    valid_notes = null;
    	} else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
    	    edt.setError("Accept Alphabets Only.");
	    valid_notes = null;
	} else {
	    valid_notes = edt.getText().toString();
	}
    }

    

    public void Is_Valid_Person_Name(EditText edt) throws NumberFormatException {
	if (edt.getText().toString().length() <= 0) {
	    edt.setError("Accept Alphabets Only.");
	    valid_title = null;
	} else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
	    edt.setError("Accept Alphabets Only.");
	    valid_title = null;
	} else {
	    valid_title = edt.getText().toString();
	}

    }

    public void Show_Toast(String msg) {
	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void Reset_Text() {

	add_title.getText().clear();
	add_details.getText().clear();
	add_notes.getText().clear();

    }

}