package com.gravity.innovations.tasks.done.view.pager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.gravity.innovations.tasks.done.R;

public class ViewPagerActivity extends FragmentActivity {
	private ViewPager _mViewPager;
	private ViewPagerAdapter _adapter;
	private Button _btn1, _btn2, _btn3, _btn4, _btn5;
	View view;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_main);
		setUpView();
		setTab();

	}

	private void setUpView() {
		_mViewPager = (ViewPager) findViewById(R.id.viewPager);
		_adapter = new ViewPagerAdapter(getApplicationContext(),
				getSupportFragmentManager());
		_mViewPager.setAdapter(_adapter);
		_mViewPager.setCurrentItem(0);
		initButton();
	}

	private void setTab() {
		_mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int position) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				btnAction(position);
				if (position == 4) {
					Toast.makeText(getApplicationContext(), "Fourth position",
							Toast.LENGTH_LONG).show();
					Animation animationFadeIn = AnimationUtils.loadAnimation(
							getApplicationContext(), android.R.anim.fade_out);
					_mViewPager.startAnimation(animationFadeIn);
				}
			}

		});

	}

	private void btnAction(int action) {
		switch (action) {
		case 0:
			// current
			setButton(_btn1, "", 40, 40);// 1
			// others
			setButton(_btn2, "", 20, 20);// 2
			setButton(_btn3, "", 20, 20);// 3
			setButton(_btn4, "", 20, 20);// 4
			setButton(_btn5, "", 20, 20);// 5

			break;
		case 1:
			// current
			setButton(_btn2, "", 40, 40);// 2
			// others
			setButton(_btn1, "", 20, 20);// 1
			setButton(_btn3, "", 20, 20);// 3
			setButton(_btn4, "", 20, 20);// 4
			setButton(_btn5, "", 20, 20);// 5

			break;
		case 2:
			// current
			setButton(_btn3, "", 40, 40);// 3
			// others
			setButton(_btn1, "", 20, 20);// 1
			setButton(_btn2, "", 20, 20);// 2
			setButton(_btn4, "", 20, 20);// 4
			setButton(_btn5, "", 20, 20);// 5
			break;
		case 3:
			// current
			setButton(_btn4, "", 40, 40);// 4
			// others
			setButton(_btn1, "", 20, 20);// 1
			setButton(_btn2, "", 20, 20);// 2
			setButton(_btn3, "", 20, 20);// 3
			setButton(_btn5, "", 20, 20);// 5
			break;
		case 4:
			// current
			setButton(_btn5, "", 40, 40);// 5
			// others
			setButton(_btn1, "", 20, 20);// 1
			setButton(_btn2, "", 20, 20);// 2
			setButton(_btn3, "", 20, 20);// 3
			setButton(_btn4, "", 20, 20);// 4
			break;
		}
	}

	private void initButton() {
		_btn1 = (Button) findViewById(R.id.btn1);
		_btn2 = (Button) findViewById(R.id.btn2);
		_btn3 = (Button) findViewById(R.id.btn3);
		_btn4 = (Button) findViewById(R.id.btn4);
		_btn5 = (Button) findViewById(R.id.btn5);

		setButton(_btn1, "", 40, 40);// 1
		setButton(_btn2, "", 20, 20);
		setButton(_btn3, "", 20, 20);
		setButton(_btn4, "", 20, 20);
		setButton(_btn5, "", 20, 20);
	}

	private void setButton(Button btn, String text, int h, int w) {
		btn.setWidth(w);
		btn.setHeight(h);
		btn.setText(text);
	}
}