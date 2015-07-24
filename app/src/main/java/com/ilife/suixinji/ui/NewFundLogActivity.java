package com.ilife.suixinji.ui;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.ilife.suixinji.R;
import com.ilife.suixinji.db.DatabaseHelper;
import com.ilife.suixinji.db.Fund;
import com.ilife.suixinji.db.Fund.Type;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

public class NewFundLogActivity extends Activity {
	private DatabaseHelper mDbHelper;
	private RadioGroup group;
	private EditText moneyView, timeView, dowhatView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = new DatabaseHelper(this);
		setContentView(R.layout.activity_newfundlog);

		group = (RadioGroup) findViewById(R.id.radioGroup1);
		moneyView = (EditText) findViewById(R.id.editText1);
		timeView = (EditText) findViewById(R.id.editText2);
		dowhatView = (EditText) findViewById(R.id.editText3);
		timeView.setText(new Date().toString());
	}
	
	public void save(View v){
		Fund f = new Fund();
		int id = group.getCheckedRadioButtonId();
		if(id > 0){
			if(id == R.id.radio0){
				f.setType(Type.REVENUE);
			}else{
				f.setType(Type.EXPENSES);
			}
		}
		f.setMoney(Integer.parseInt(moneyView.getText().toString()));
		f.setTime(timeView.getText().toString());
		f.setDowhat(dowhatView.getText().toString());
		
		long rowId = mDbHelper.addFundLog(f);
		if(rowId >= 0){
			finish();
		}
	}
	
	public void cancel(View v){
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mDbHelper.open();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mDbHelper.close();
	}
	
	@Override
	public File getDatabasePath(String name) {
		String dbDir;
		File externalStorage = Environment.getExternalStorageDirectory();
		if(!externalStorage.exists()){
			boolean sdExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
			if(!sdExist)
				Log.e("SD", "SD");
			dbDir = Environment.getDataDirectory().getAbsolutePath();
		}else{
			dbDir = externalStorage.getAbsolutePath();
		}
		dbDir += "/SuiXinJi";
		String dbPath = dbDir + "/" + name;
		Log.i("ss", dbPath);
		File dirFile = new File(dbDir);
		if (!dirFile.exists())
			if(!dirFile.mkdirs() && !dirFile.isDirectory()){
				Log.e("IOException", "" + dirFile.getAbsolutePath() + "");
				File file = getDir(name, Context.MODE_PRIVATE);
				dbPath = file.getPath() + "/" + name;
			}
		
		boolean isFileCreateSuccess = false;
		File dbFile = new File(dbPath);
		if (!dbFile.exists()) {
			try {
				isFileCreateSuccess = dbFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			isFileCreateSuccess = true;
        
        if(isFileCreateSuccess)
            return dbFile;
        else 
            return super.getDatabasePath(name);
	}
	
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory) {
		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory, DatabaseErrorHandler errorHandler) {
		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
	}
}
