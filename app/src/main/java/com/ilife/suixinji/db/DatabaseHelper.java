package com.ilife.suixinji.db;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class DatabaseHelper {
	private static final String DB_NAME = "suixinji";
	private static final int DB_VERSION = 3;
	
	private Datebase datebase;
	private SQLiteDatabase mDb;
	
	public DatabaseHelper(Context cxt){
		datebase = new Datebase(cxt);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void open(){
		try {
			mDb = datebase.getWritableDatabase();
		} catch (SQLiteCantOpenDatabaseException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		mDb = null;
		datebase.close();
	}
	
	public long addRecord(Record record){
		return mDb.insert(Record.TABLE_NAME, null, record.getContentValues());
	}
	
	public int deleteRecord(Record record){
		return deleteRecord(record.getId());
	}
	
	public int deleteRecord(int id){
		return mDb.delete(Record.TABLE_NAME, Record.FIELD_ID + "=" + id, null);
	}
	
	public List<Record> getRecordsByWhere(String where){
		Cursor cursor = mDb.query(Record.TABLE_NAME, null, where, null, null, null, Record.FIELD_CREATETIME + " desc");
		if(cursor == null || cursor.getCount() == 0)
			return new ArrayList<Record>();
		List<Record> records = new ArrayList<Record>(cursor.getCount());
		while (cursor.moveToNext()) {
			ContentValues values = new ContentValues();
			DatabaseUtils.cursorRowToContentValues(cursor, values);
			Record record = new Record();
			record.createFromContentValues(values);
			records.add(record);
		}
		return records;
	}
	
	public long addFundLog(Fund fund){
		return mDb.insert(Fund.TABLE_NAME, null, fund.getContentValues());
	}
	
	public List<Fund> getFundLogsByWhere(String where){
		Cursor cursor = mDb.query(Fund.TABLE_NAME, null, where, null, null, null, Fund.FIELD_TIME + " desc");
		if(cursor == null || cursor.getCount() == 0)
			return new ArrayList<Fund>();
		List<Fund> fundlogs = new ArrayList<Fund>(cursor.getCount());
		while (cursor.moveToNext()) {
			ContentValues values = new ContentValues();
			DatabaseUtils.cursorRowToContentValues(cursor, values);
			Fund fund = new Fund();
			fund.createFromContentValues(values);
			fundlogs.add(fund);
		}
		return fundlogs;
	}
	
	private class Datebase extends SQLiteOpenHelper{
		private static final String CREATE_TABLE_RECORD = "CREATE TABLE IF NOT EXISTS " +
				Record.TABLE_NAME 	+ " (" +
					Record.FIELD_ID 	+ " INTEGER PRIMARY KEY AUTOINCREMENT," + 
					Record.FIELD_CREATETIME 	+ " TEXT NOT NULL," +
					Record.FIELD_CONTENT 		+ " TEXT," +
					Record.FIELD_WEEKDAY 		+ " TEXT NOT NULL," +
					Record.FIELD_PHOTOSPATH 	+ " TEXT," +
					Record.FIELD_AUDIOSPATH 	+ " TEXT," +
					Record.FIELD_VIDEOSPATH 	+ " TEXT," +
					Record.FIELD_DOODLEPATH		+ " TEXT" +
				");";
		private static final String CREATE_TABLE_FUND = "CREATE TABLE IF NOT EXISTS " +
				Fund.TABLE_NAME 	+ " (" +
				Fund.FIELD_ID 	+ " INTEGER PRIMARY KEY AUTOINCREMENT," + 
				Fund.FIELD_MONEY 	+ " INTEGER NOT NULL," +
				Fund.FIELD_TIME 		+ " TEXT," +
				Fund.FIELD_TYPE 		+ " INTEGER NOT NULL," +
				Fund.FIELD_DOWHAT 		+ " TEXT" +
			");";

		public Datebase(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE_RECORD);
			db.execSQL(CREATE_TABLE_FUND);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(oldVersion < 3){
				db.execSQL(CREATE_TABLE_FUND);
			}
			if(oldVersion < 2){
				addColumn(db, Record.TABLE_NAME, Record.FIELD_WEEKDAY, "TEXT DEFAULT 'unkonwn' NOT NULL");
			}
		}
		
		@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion,
				int newVersion) {
		}
		
		private void addColumn(SQLiteDatabase db, String table, String field, String type) {
		    db.execSQL("ALTER TABLE " + table + " ADD "+ field + " " + type);
		}
		
	}
}
