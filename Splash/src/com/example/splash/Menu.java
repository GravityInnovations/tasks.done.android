package com.example.splash;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity {

	 String classes[]={"SplashActivity","MainActivity","Email",
			 "Camera","SplashActivity","Data","TextPlay","Gfx", "Grid"};
	
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//removes the tiles etc full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//removes the titles etc full screen
		setListAdapter(new ArrayAdapter<String>(Menu.this, android.R.layout.simple_list_item_1, classes));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String che = classes[position];
		try{
		Class ourClass = Class.forName("com.example.splash." + che);
		Intent ourIntent = new Intent(Menu.this, ourClass);
		startActivity(ourIntent);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		//return super.onCreateOptionsMenu(menu);
	MenuInflater blowUp = getMenuInflater();
	blowUp.inflate(R.menu.cool_menu, menu);
	return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//return super.onOptionsItemSelected(item);
		switch (item.getItemId()){
		case R.id.aboutus:
			Intent i = new Intent("com.splash.ABOUT");
			startActivity(i);
			break;
		case R.id.prefrences:
			Intent i2 = new Intent("com.splash.PREFRENCE");
			startActivity(i2);
			break;
		case R.id.exit:
			finish();
			break;
			
		}
		return false;
	}
	

}
