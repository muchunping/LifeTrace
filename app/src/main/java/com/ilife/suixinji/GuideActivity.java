package com.ilife.suixinji;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends Activity implements
		ViewPager.OnPageChangeListener , Runnable{
	private LinearLayout mTabIconLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		mTabIconLayout = (LinearLayout) findViewById(R.id.linearLayout1);
		ViewPager pager = (ViewPager) findViewById(R.id.viewPager1);
		pager.setAdapter(new ViewPagerAdapter());
		pager.setOffscreenPageLimit(guideImageResourceIds.length);
		pager.setOnPageChangeListener(this);
		mTabIconLayout.getChildAt(mCurrentPageIndex).setSelected(true);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

	}

	private int mCurrentPageIndex = 0;
	@Override
	public void onPageSelected(int position) {
		mTabIconLayout.getChildAt(position).setSelected(true);
		mTabIconLayout.getChildAt(mCurrentPageIndex).setSelected(false);
		mCurrentPageIndex = position;
		
		if(position == guideImageResourceIds.length - 1){
			new Handler().postDelayed(this, 5000);
		}else{
			new Handler().removeCallbacks(this);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	private final int[] guideImageResourceIds = {
			R.drawable.ic_launcher,
			R.drawable.ic_launcher,
			R.drawable.ic_launcher
	};

	private class ViewPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return guideImageResourceIds.length;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View child = getView(position);
			container.addView(child, position);
			return child;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
	
	public View getView(int position){
		if(position > guideImageResourceIds.length - 1)
			return null;
		ImageView view = new ImageView(this);
		view.setImageResource(guideImageResourceIds[position]);
		return view; 
		
	}

	@Override
	public void run() {
		SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = spf.edit();
		editor.putBoolean("firstRun", false);
		editor.commit();
		
		startActivity(new Intent(GuideActivity.this, MainActivity.class));
		finish();
	}
}
