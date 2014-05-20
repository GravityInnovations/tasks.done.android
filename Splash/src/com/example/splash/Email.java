package com.example.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Email extends Activity implements View.OnClickListener {
	EditText personalEmail, intro, stupidThings, personName, hatefulAction, outro;
	String emailAdd, beginning, name, stupidAction, hatefulAct, out;
	Button sendEmail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email);
		initializeVars();
		sendEmail.setOnClickListener(this);
	}
	private void initializeVars() {
		// TODO Auto-generated method stub
		personalEmail = (EditText) findViewById(R.id.etEmails);
		intro = (EditText) findViewById(R.id.etIntro);
		personName =(EditText) findViewById(R.id.etName);
		stupidThings =(EditText) findViewById(R.id.etThings);
		hatefulAction =(EditText) findViewById(R.id.etAction);
		outro =(EditText) findViewById(R.id.etOutro);
		sendEmail =(Button) findViewById(R.id.bSentEmail);
		
				
	}
	public void onClick(View v){
		convertEditTextVarsIntoString();
		String emailaddress[]={emailAdd};
		String message = "Well Hello! "
				+ name 
				+ " I just wanted to say "
				+ beginning 
				+ ". Not only that but i hate when you do "
				+ stupidAction
				+ " that just really makes me crazy. I just want to make  "
				+ hatefulAct
				+ " Welp, hat all i wanted "
				+ out 
				+  '\n' + "P.S Fuck";
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailaddress);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello Son");
		emailIntent.setType("palin/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
		startActivity(emailIntent);
	}
	private void convertEditTextVarsIntoString() {
		// TODO Auto-generated method stub
		emailAdd = personalEmail.getText().toString();
		beginning = intro.getText().toString();
		name = personName.getText().toString();
		stupidAction = stupidThings.getText().toString();
		hatefulAct = hatefulAction.getText().toString();
		out = outro.getText().toString();
	}
	protected void onPause(){
		super.onPause();finish();
	}
	

}
