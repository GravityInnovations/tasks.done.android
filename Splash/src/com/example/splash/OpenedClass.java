package com.example.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class OpenedClass extends Activity implements OnClickListener,
		OnCheckedChangeListener {
	TextView question, test;
	Button returnData;
	RadioGroup selectionList;
	String gotBread, setData;
	Bundle backpack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send);
		initialize();
		
		SharedPreferences getData = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String et = getData.getString("name", "M is . .. .");
		String values = getData.getString("list", "4");
		if(values.contentEquals("1")){
			question.setText(et);
		}
		// Bundle gotBasket = getIntent().getExtras();
		// gotBread = gotBasket.getString("key");
		// question.setText(gotBread);
	}

	private void initialize() {
		// TODO Auto-generated method stub
		question = (TextView) findViewById(R.id.tvQuestions);
		test = (TextView) findViewById(R.id.tvText);
		returnData = (Button) findViewById(R.id.bReturn);
		returnData.setOnClickListener(this);
		selectionList = (RadioGroup) findViewById(R.id.rgAnswers);
		selectionList.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent person = new Intent();

		backpack.putString("answer", setData);
		person.putExtras(backpack);
		setResult(RESULT_OK, person);
		finish();

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		// TODO Auto-generated method stub

		switch (checkedId) {
		case R.id.rCrazy:
			setData = "Yeah Right";
			break;
		case R.id.rSuper:
			setData = "Yeah Right too";
			break;
		case R.id.rBoth:
			setData = "what!";
			break;
		}
		test.setText(setData);
	}
}
