package com.ilife.suixinji.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class Record implements Parcelable{
	// for database table column name
	public static final String TABLE_NAME = "record";
	public static final String FIELD_ID = "_id";
	public static final String FIELD_CREATETIME = "createTime";
	public static final String FIELD_CONTENT = "content";
	public static final String FIELD_WEEKDAY = "weekday";
	public static final String FIELD_PHOTOSPATH = "photosPath";
	public static final String FIELD_AUDIOSPATH = "audiosPath";
	public static final String FIELD_VIDEOSPATH = "videosPath";
	public static final String FIELD_DOODLEPATH = "doodlePath";
	
	private int id;
	private String createTime ;
	private String content;
	private String weekday;
	private ArrayList<String> photosPath = new ArrayList<String>();
	private List<String> audiosPath = new ArrayList<String>();
	private List<String> videosPath = new ArrayList<String>();
	private List<String> doodlePath = new ArrayList<String>();
	
	public Record() {

	}
	
	public Record(boolean isDummy) {
		
	}
	
	public void createFromContentValues(ContentValues values){
		Integer tem_i = null;
		String tem_s = null;

		tem_i = values.getAsInteger("id");
		if(tem_i != null) id = tem_i;
		
		tem_s = values.getAsString("createTime");
		if(tem_s != null) createTime = tem_s;
		else createTime = generDefaultTime();
		
		tem_s = values.getAsString("content");
		if(tem_s != null) content = tem_s;
		
		tem_s = values.getAsString("weekday");
		if(tem_s != null) weekday = tem_s;
		else weekday = generDafaultWeekday();
		
		tem_s = values.getAsString("photosPath");
		if(tem_s != null) {
			String[] tem = tem_s.split(",");
			for (int i = 0; i < tem.length; i++) {
				photosPath.add(tem[i]);
			}
		}
		
		tem_s = values.getAsString("audiosPath");
		if(tem_s != null) {
			String[] tem = tem_s.split(",");
			for (int i = 0; i < tem.length; i++) {
				audiosPath.add(tem[i]);
			}
		}
		
		tem_s = values.getAsString("videosPath");
		if(tem_s != null) {
			String[] tem = tem_s.split(",");
			for (int i = 0; i < tem.length; i++) {
				videosPath.add(tem[i]);
			}
		}
		
		tem_s = values.getAsString("doodlePath");
		if(tem_s != null) {
			String[] tem = tem_s.split(",");
			for (int i = 0; i < tem.length; i++) {
				doodlePath.add(tem[i]);
			}
		}
	}
	
	private String generDafaultWeekday() {
		int day = Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_WEEK);
		System.out.println(day);
		switch (day) {
		case Calendar.SUNDAY:
			return "������";
		case Calendar.MONDAY:
			return "����һ";
		case Calendar.TUESDAY:
			return "���ڶ�";
		case Calendar.WEDNESDAY:
			return "������";
		case Calendar.THURSDAY:
			return "������";
		case Calendar.FRIDAY:
			return "������";
		case Calendar.SATURDAY:
			return "������";
		default:
			return "error(" + day + ")";
		}
	}

	private String generDefaultTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
		return sdf.format(new Date());
	}

	public ContentValues getContentValues(){
		ContentValues values = new ContentValues();
		values.put(FIELD_CREATETIME, createTime);
		values.put(FIELD_CONTENT, content);
		values.put(FIELD_WEEKDAY, weekday);
		values.put(FIELD_PHOTOSPATH, listToString(photosPath));
		values.put(FIELD_AUDIOSPATH, listToString(audiosPath));
		values.put(FIELD_VIDEOSPATH, listToString(videosPath));
		values.put(FIELD_DOODLEPATH, listToString(doodlePath));
		return values;
	}
	
	private String listToString(List<String> list){
		StringBuilder builder = new StringBuilder();
		for (String string : list) {
			if(builder.length() > 0)
				builder.append(",");
			builder.append(string);
		}
		return builder.toString();
	}

	public int getId() {
		return id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getContent() {
		return content;
	}

	public ArrayList<String> getPhotosPath() {
		return photosPath;
	}

	public List<String> getAudiosPath() {
		return audiosPath;
	}

	public List<String> getVideosPath() {
		return videosPath;
	}

	public List<String> getDoodlePath() {
		return doodlePath;
	}
	
	public String getWeekday() {
		return weekday;
	}

	public static final Creator<Record> CREATOR = new Creator<Record>() {
		public Record createFromParcel(Parcel in) {
			return new Record(in);
		}

		public Record[] newArray(int size) {
			return new Record[size];
		}
	};

	private Record(Parcel in) {

	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
	}
}
