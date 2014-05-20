package com.example.splash;

import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

/*
 public class TextPlay extends Activity {

 Button chkCmd;
 ToggleButton passTogg;
 EditText input;
 TextView display;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
 // TODO Auto-generated method stub
 super.onCreate(savedInstanceState);
 setContentView(R.layout.text_activity);
 breakFast();
 passTogg.setOnClickListener(new View.OnClickListener() {

 @Override
 public void onClick(View v) {
 // TODO Auto-generated method stub
 if (passTogg.isChecked()) {
 input.setInputType(InputType.TYPE_CLASS_TEXT
 | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
 } else {
 input.setInputType(InputType.TYPE_CLASS_TEXT);
 }
 }
 });
 chkCmd.setOnClickListener(new View.OnClickListener() {

 @Override
 public void onClick(View v) {
 // TODO Auto-generated method stub
 String check = input.getText().toString();
 if (check.contentEquals("left")) {
 display.setGravity(Gravity.LEFT);
 } else if (check.contentEquals("center")) {
 display.setGravity(Gravity.CENTER);
 } else if (check.contentEquals("right")) {
 display.setGravity(Gravity.RIGHT);
 } else if (check.contentEquals("blue")) {
 display.setTextColor(Color.BLUE);
 } else if (check.contentEquals("WTF")) {
 Random crazy = new Random();
 display.setText("WTF!!!!");
 display.setTextSize(crazy.nextInt(75));
 display.setTextColor(Color.rgb((crazy.nextInt(265)),
 (crazy.nextInt(265)), (crazy.nextInt(265))));
 switch (crazy.nextInt(3)) {
 case 0:
 display.setGravity(Gravity.LEFT);
 break;
 case 1:
 display.setGravity(Gravity.CENTER);
 break;
 case 2:
 display.setGravity(Gravity.RIGHT);
 break;
 }
 } else {
 display.setText("invalid");
 display.setGravity(Gravity.CENTER);
 display.setTextColor(Color.WHITE);

 }

 }
 });
 }

 private void breakFast() {
 // TODO Auto-generated method stub
 chkCmd = (Button) findViewById(R.id.bResult);
 passTogg = (ToggleButton) findViewById(R.id.bToggle);
 input = (EditText) findViewById(R.id.etCmd);
 display = (TextView) findViewById(R.id.tvDisplay);	
 }

 }
 */
public class TextPlay extends Activity implements View.OnClickListener {

	Button chkCmd;
	ToggleButton passTogg;
	EditText input;
	TextView display;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_activity);
		breakFast();
		passTogg.setOnClickListener(this);
		chkCmd.setOnClickListener(this);
	}
	private void breakFast() {
		// TODO Auto-generated method stub
		chkCmd = (Button) findViewById(R.id.bResult);
		passTogg = (ToggleButton) findViewById(R.id.bToggle);
		input = (EditText) findViewById(R.id.etCmd);
		display = (TextView) findViewById(R.id.tvDisplay);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bResult:
			String check = input.getText().toString();
			if (check.contentEquals("left")) {
				display.setGravity(Gravity.LEFT);
			} else if (check.contentEquals("center")) {
				display.setGravity(Gravity.CENTER);
			} else if (check.contentEquals("right")) {
				display.setGravity(Gravity.RIGHT);
			} else if (check.contentEquals("blue")) {
				display.setTextColor(Color.BLUE);
			} else if (check.contentEquals("WTF")) {
				Random crazy = new Random();
				display.setText("WTF!!!!");
				display.setTextSize(crazy.nextInt(75));
				display.setTextColor(Color.rgb((crazy.nextInt(265)),
						(crazy.nextInt(265)), (crazy.nextInt(265))));
				switch (crazy.nextInt(3)) {
				case 0:
					display.setGravity(Gravity.LEFT);
					break;
				case 1:
					display.setGravity(Gravity.CENTER);
					break;
				case 2:
					display.setGravity(Gravity.RIGHT);
					break;
				}
			} else {
				display.setText("invalid");
				display.setGravity(Gravity.CENTER);
				display.setTextColor(Color.WHITE);

			}
			break;
		case R.id.bToggle:
			if (passTogg.isChecked()) {
				input.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			} else {
				input.setInputType(InputType.TYPE_CLASS_TEXT);
			}
			break;
		}
	}

}