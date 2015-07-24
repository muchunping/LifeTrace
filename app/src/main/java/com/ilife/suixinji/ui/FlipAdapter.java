package com.ilife.suixinji.ui;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class FlipAdapter extends BaseAdapter {

	private int repeatCount = 1;
	private Activity mActivity = null;
	private List<String> mData;

	public FlipAdapter(Activity activity, List<String> datas) {
		mActivity = activity;
		mData = datas;
	}

	@Override
	public int getCount() {
		return mData.size() * repeatCount;
	}

	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new ImageView(mActivity);
			convertView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, -1));
			convertView.setBackgroundColor(Color.WHITE);
			((ImageView)convertView).setScaleType(ScaleType.FIT_CENTER);
		} 
		
		Bitmap bm =BitmapFactory.decodeFile(mData.get(position));
        int degree = NewRecordActivity.readPictureDegree(mData.get(position));
        bm = NewRecordActivity.rotaingBitmap(degree, bm);
		((ImageView)convertView).setImageBitmap(bm);
		return convertView;
	}
}
