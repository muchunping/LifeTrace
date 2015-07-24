package com.ilife.suixinji.util;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseListAdapter<T> extends BaseAdapter {
	private List<T> mDatas;
	public BaseListAdapter(){
		mDatas = new ArrayList<T>();
	}
	public BaseListAdapter(List<T> list){
		mDatas = list;
		if(list == null) 
			mDatas = new ArrayList<T>();
	}
	
	public void changeData(List<T> list){
		mDatas = list;
		notifyDataSetChanged();
	}
	
	public void addDataList(List<T> newList){
		mDatas.addAll(newList);
		notifyDataSetChanged();
	}
	
	public void addData(T data, int location){
		if(location < 0)
			mDatas.add(data);
		mDatas.add(location, data);
		notifyDataSetChanged();
	}
	
	public void removeDataList(List<T> list){
		mDatas.removeAll(list);
		notifyDataSetChanged();
	}
	
	public void removeData(T data){
		mDatas.remove(data);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mDatas == null)
			return 0;
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		if(mDatas == null)
			return null;
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null)
			view = newView();
		bindView(position, view, mDatas.get(position));
		return view;
	}

	protected abstract View newView();
	protected abstract void bindView(int position, View convertView, T t);
}
