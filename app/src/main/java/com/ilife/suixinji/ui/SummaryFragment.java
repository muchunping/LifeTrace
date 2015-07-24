package com.ilife.suixinji.ui;

import com.ilife.suixinji.R;
import com.ilife.suixinji.db.DatabaseHelper;
import com.ilife.suixinji.db.Fund;
import com.ilife.suixinji.db.Fund.Type;
import com.ilife.suixinji.util.BaseListAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class SummaryFragment extends Fragment {
	private DatabaseHelper mDbHelper;
	private BaseListAdapter<Fund> adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ListView iv = new ListView(getActivity());
		iv.setAdapter(adapter = new BaseListAdapter<Fund>() {

			@Override
			protected View newView() {
				ViewHolder holder = new ViewHolder();
				View view = getActivity().getLayoutInflater().inflate(R.layout.layout_fundlog_list, null);
				holder.timeView = (TextView) view.findViewById(R.id.textView1);
				holder.moneyView = (TextView) view.findViewById(R.id.textView2);
				holder.dowhatView = (TextView) view.findViewById(R.id.textView3);
				view.setTag(holder);
				return view;
			}

			@Override
			protected void bindView(int position, View convertView, Fund t) {
				ViewHolder holder = (ViewHolder) convertView.getTag();
				holder.timeView.setText(t.getTime());
				String text = "  ";
				if(t.getType() == Type.REVENUE)
					text = "  ";
				holder.moneyView.setText(text + t.getMoney());
				holder.dowhatView.setText(t.getDowhat());
			}
		});
		return iv;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = new DatabaseHelper(getActivity());
		setHasOptionsMenu(true);
		setMenuVisibility(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.loading, menu);
       super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			startActivity(new Intent(getActivity(), NewFundLogActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mDbHelper.open();
		adapter.addDataList(mDbHelper.getFundLogsByWhere(null));
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mDbHelper.close();
	}
	
	private class ViewHolder{
		TextView timeView, moneyView, dowhatView;
	}
}
