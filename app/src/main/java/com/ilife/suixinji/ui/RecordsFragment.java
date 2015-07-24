package com.ilife.suixinji.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.text.Layout;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ilife.suixinji.R;
import com.ilife.suixinji.data.Defines;
import com.ilife.suixinji.db.DatabaseHelper;
import com.ilife.suixinji.db.Record;
import com.ilife.suixinji.util.BaseListAdapter;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;

public class RecordsFragment extends Fragment implements OnItemClickListener {
	
	public static final String APP_ID = "wx8c2407548a4165c5";
	private IWXAPI shareApi;
	
	private ListView listView = null;
	private BaseListAdapter<Record> adapter;
	private DatabaseHelper mDatabaseHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		listView = new ListView(getActivity());
		listView.setOnItemClickListener(this);
		listView.setAdapter(adapter = new BaseListAdapter<Record>() {
			@Override
			protected View newView() {
				final ViewHolder holder = new ViewHolder();
				View view = getActivity().getLayoutInflater().inflate(R.layout.layout_record_list, null);
				holder.dateView = (TextView) view.findViewById(R.id.textView_datetime);
				holder.weekView = (TextView) view.findViewById(R.id.textView_weekday);
				holder.contentView = (TextView) view.findViewById(R.id.textView_content);
				holder.contentLayout = (LinearLayout) view.findViewById(R.id.layout_content);
				holder.imageLayout = (LinearLayout) view.findViewById(R.id.layout_image);
				holder.symbolView = (ImageView) view.findViewById(R.id.view_symbol);
				holder.contentLayout.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						boolean b = holder.symbolView.isSelected();
						holder.symbolView.setSelected(!b);
						if(b){
							holder.contentView.setEllipsize(null);
							holder.contentView.setSingleLine(false);
						}else{
							holder.contentView.setEllipsize(TruncateAt.END);
							holder.contentView.setMaxLines(3);
						}
					}
				});
				view.setTag(holder);
				return view;
			}

			@Override
			protected void bindView(int position, View convertView, Record record) {
				final ViewHolder holder = (ViewHolder) convertView.getTag();
				holder.dateView.setText(record.getCreateTime());
				holder.weekView.setText(record.getWeekday());
				holder.contentView.setText(record.getContent());
				holder.imageLayout.removeAllViews();
				holder.symbolView.setVisibility(View.GONE);
				Layout textLayout = holder.contentView.getLayout();
				if(textLayout != null){
					int line = textLayout.getLineCount();
					if(textLayout.getEllipsisCount(line - 1) > 0){
						holder.symbolView.setVisibility(View.VISIBLE);
						holder.symbolView.setSelected(true);
					}
				}else{
					holder.contentView.getViewTreeObserver().addOnGlobalLayoutListener(
							new OnGlobalLayoutListener() {
						public void onGlobalLayout() {
							Layout textLayout = holder.contentView.getLayout();
							int line = textLayout.getLineCount();
							if(textLayout.getEllipsisCount(line - 1) > 0){
								holder.symbolView.setVisibility(View.VISIBLE);
								holder.symbolView.setSelected(true);
							}
//							holder.contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
						}
					});
				}
				ArrayList<String> imagePath = record.getPhotosPath();
				if (!imagePath.isEmpty()) {
					int size = imagePath.size();
					boolean moreThanMaxCount = size > Defines.MAX_IMAGE_TO_SHOW;
					int realShowImageCount = moreThanMaxCount ? Defines.MAX_IMAGE_TO_SHOW : size;
					for (int i = 0; i < realShowImageCount; i++) {
						String path = imagePath.get(i);
						if(path.length() <= 0) continue;
						PhotoImageView photoImageView = new PhotoImageView(getActivity(), i, imagePath);
						holder.imageLayout.addView(photoImageView);
						photoImageView.setDrawableByPath(path);
					}
					if(moreThanMaxCount){
						TextView textView = new TextView(getActivity());
						textView.setText(getActivity().getResources().getString(
								R.string.more_info));
						holder.imageLayout.addView(textView);
					}
				}
			}
		});
		return listView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDatabaseHelper = new DatabaseHelper(getActivity());
		setHasOptionsMenu(true);
		setMenuVisibility(true);
		shareApi = WXAPIFactory.createWXAPI(getActivity(), APP_ID, true);
		shareApi.registerApp(APP_ID);
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
			startActivity(new Intent(getActivity(), NewRecordActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mDatabaseHelper.open();
		adapter.changeData(mDatabaseHelper.getRecordsByWhere(null));
	}

	@Override
	public void onPause() {
		super.onPause();
		mDatabaseHelper.close();
	}
	
	public class ViewHolder {
		TextView dateView;
		TextView weekView;
		TextView contentView;
		LinearLayout contentLayout;
		LinearLayout imageLayout;
		ImageView symbolView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Record record = (Record) parent.getItemAtPosition(position);
		WXTextObject textObj = new WXTextObject();
		textObj.text = record.getContent();
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		msg.description = record.getCreateTime();
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(SystemClock.currentThreadTimeMillis());
		req.message = msg;
		
		shareApi.sendReq(req);
	}
}
