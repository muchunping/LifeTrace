package com.ilife.suixinji.db;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class Fund implements Parcelable {
	public enum Type {
		REVENUE(0), EXPENSES(1);
		
		public int value;
		Type(int value){
			this.value = value;
		}
		
		public int getValue(){
			return value;
		}
		
		public static Type parseValue(int value){
			return value == 0 ? REVENUE : EXPENSES;
		}
	}
	
	public int id;
	public int money;
	public String time;
	public Type type;
	public String dowhat;
	
	public Fund() {
	}
	
	public void createFromContentValues(ContentValues values){
		Integer tem_i = null;
		String tem_s = null;

		tem_i = values.getAsInteger(FIELD_ID);
		if(tem_i != null) id = tem_i;
		
		tem_i = values.getAsInteger(FIELD_MONEY);
		if(tem_i != null) money = tem_i;
		
		tem_s = values.getAsString(FIELD_TIME);
		if(tem_s != null) time = tem_s;
		
		tem_i = values.getAsInteger(FIELD_TYPE);
		if(tem_i != null) type = Type.parseValue(tem_i);
		
		tem_s = values.getAsString(FIELD_DOWHAT);
		if(tem_s != null) dowhat = tem_s;
	}
	
	public ContentValues getContentValues(){
		ContentValues values = new ContentValues();
		values.put(FIELD_MONEY, money);
		values.put(FIELD_TIME, time);
		values.put(FIELD_TYPE, type.getValue());
		values.put(FIELD_DOWHAT, dowhat);
		return values;
	}
	
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getDowhat() {
		return dowhat;
	}

	public void setDowhat(String dowhat) {
		this.dowhat = dowhat;
	}


	public static final Creator<Fund> CREATOR = new Creator<Fund>() {
		public Fund createFromParcel(Parcel in) {
			return new Fund(in);
		}

		public Fund[] newArray(int size) {
			return new Fund[size];
		}
	};

	private Fund(Parcel in) {

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}

	public static final String TABLE_NAME ="fund";
	public static final String FIELD_ID ="id";
	public static final String FIELD_MONEY ="money";
	public static final String FIELD_TIME ="time";
	public static final String FIELD_TYPE ="type";
	public static final String FIELD_DOWHAT ="dowhat";
}
