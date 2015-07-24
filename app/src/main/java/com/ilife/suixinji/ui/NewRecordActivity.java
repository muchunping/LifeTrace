package com.ilife.suixinji.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ilife.suixinji.R;
import com.ilife.suixinji.db.DatabaseHelper;
import com.ilife.suixinji.db.Record;
import com.ilife.suixinji.util.IOOperation;

public class NewRecordActivity extends Activity {
	private DatabaseHelper mDatabaseHelper;
	private EditText mTextView;
	private LinearLayout mImageLayout, mAudioLayout, mVideoLayout, mDoodleLayout;
	private List<String> mImagesPath, mAudiosPath, mVideosPath, mDoodlesPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newrecord);
		
		mTextView = (EditText) findViewById(R.id.contentView);
		mImageLayout = (LinearLayout) findViewById(R.id.imagelayout);
		mAudioLayout = (LinearLayout) findViewById(R.id.audiolayout);
		mVideoLayout = (LinearLayout) findViewById(R.id.videolayout);
		mDoodleLayout = (LinearLayout) findViewById(R.id.doodlelayout);

		mImagesPath = new ArrayList<String>();
		mAudiosPath = new ArrayList<String>();
		mVideosPath = new ArrayList<String>();
		mDoodlesPath = new ArrayList<String>();
		
		mDatabaseHelper = new DatabaseHelper(this);
	}
	
	@Override
	public File getDatabasePath(String name) {
		String dbDir;
		File externalStorage = Environment.getExternalStorageDirectory();
		if(!externalStorage.exists()){
			boolean sdExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
			if(!sdExist)
				Log.e("SD�����?", "SD�������ڣ������SD��");
			dbDir = Environment.getDataDirectory().getAbsolutePath();
		}else{
			dbDir = externalStorage.getAbsolutePath();
		}
		dbDir += "/SuiXinJi";
		String dbPath = dbDir + "/" + name;
		Log.i("��ݿ�·��", dbPath);
		File dirFile = new File(dbDir);
		if (!dirFile.exists())
			if(!dirFile.mkdirs() && !dirFile.isDirectory()){
				Log.e("IOException", "�����ļ��� " + dirFile.getAbsolutePath() + " ʧ��");
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
	
	private boolean savedSuccessful = false;
	public void save(View v){
		mDatabaseHelper.open();
		Record newRecord = new Record();
		ContentValues cv = new ContentValues();
		cv.put(Record.FIELD_CONTENT, mTextView.getText().toString());
		cv.put(Record.FIELD_PHOTOSPATH, stringListToString(mImagesPath));
		newRecord.createFromContentValues(cv);
		long result = mDatabaseHelper.addRecord(newRecord);
		mDatabaseHelper.close();
		if(result == -1){
			savedSuccessful = false;
			Toast.makeText(this, "����ʧ��", Toast.LENGTH_SHORT).show();
		}else{
			savedSuccessful = true;
			Toast.makeText(this, "����ɹ�", Toast.LENGTH_SHORT).show();
		}
		finish();
	}
	
	@Override
	protected void onDestroy() {
		if(!savedSuccessful)
			for (String path : mImagesPath) {
				File file = new File(path);
				file.delete();
			}
		super.onDestroy();
	}
	
	private String stringListToString(List<String> list){
		StringBuilder builder = new StringBuilder();
		for (String string : list) {
			if(builder.length() > 0)
				builder.append(",");
			builder.append(string);
		}
		return builder.toString();
	}
	
	public void takephoto(View v){
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(IOOperation.getTempFile()));
		startActivityForResult(cameraIntent, LOADPIC1);
	}
	
	public void pickpicture(View v) {
		Intent pickIntent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(pickIntent, LOADPIC2);
	}

	public void addaudio(View v) {

	}

	public void adddoodle(View v) {

	}
	
	public void back(View v){
		onBackPressed();
	}

	public static final int LOADPIC1 = 1;
	public static final int LOADPIC2 = 2;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) return;
		switch (requestCode) {
		case LOADPIC1:
			final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(IOOperation.getTempFile().getPath(), options);
	        options.inSampleSize = IOOperation.calculateInSampleSize(options, 720, 720);
	        options.inJustDecodeBounds = false;
	        Bitmap bm =BitmapFactory.decodeFile(IOOperation.getTempFile().getPath(), options);
	        int degree = readPictureDegree(IOOperation.getTempFile().getPath());
	        bm = rotaingBitmap(degree, bm);
	        
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.JPEG, 60, baos);
				byte[] b = baos.toByteArray();
				File file = IOOperation.getOutputFile();
		        FileOutputStream fos = new FileOutputStream(file);
		        fos.write(b);
				fos.close();
		        mImageLayout.addView(createImageView(bm));
				mImagesPath.add(file.getPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case LOADPIC2:
			if (data == null) return;
			Uri originalUri = data.getData();
			try {
				Bitmap photo = Media.getBitmap(getContentResolver(), originalUri);
				if (photo == null) return;
				bm = /*zoomBitmap(photo, 480, 800);*/photo;
//				photo.recycle();
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.JPEG, 60, baos);
				byte[] b = baos.toByteArray();
				File file = IOOperation.getOutputFile();
		        FileOutputStream fos = new FileOutputStream(file);
		        fos.write(b);
				fos.close();
				degree = readPictureDegree(originalUri.getPath());
				Cursor cursor = Media.query(getContentResolver(), originalUri,
						new String[]{Media.ORIENTATION});
				if(cursor != null && cursor.moveToFirst()){
					degree = cursor.getInt(0);
					bm = rotaingBitmap(degree, bm);
				}
				mImageLayout.addView(createImageView(bm));
				mImagesPath.add(file.getPath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			break;
		default:
			break;
		}
	}
	
	/**
	 * ��ȡͼƬ���ԣ���ת�ĽǶ�
	 * @param path ͼƬ���·��
	 * @return degree��ת�ĽǶ�
	 */
    public static int readPictureDegree(String path) {
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			return readPictureDegree(orientation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
    }
    
    private static int readPictureDegree(int orientation){
    	int degree = 0;
    	switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_90:
			degree = 90;
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			degree = 180;
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			degree = 270;
			break;
		}
    	return degree;
    }
   /*
    * ��תͼƬ 
    * @param angle 
    * @param bitmap 
    * @return Bitmap 
    */  
   public static Bitmap rotaingBitmap(int angle , Bitmap bitmap) {  
	   if(angle == 0) return bitmap;
       Matrix matrix = new Matrix();
       matrix.postRotate(angle);  
       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
               bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
       return resizedBitmap;  
   }
	
	public Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);// ���þ���������Ų�������ڴ����
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }
	
	private View createImageView(Bitmap tembm){
		ImageView iv = new ImageView(this);
		iv.setBackgroundResource(R.drawable.frame_photo);
		iv.setPadding(2, 2, 2, 2);
		
		int width = tembm.getWidth();
		int height = tembm.getHeight();
		int scaleX = 150 / width;
		int scaleY = 150 / height;
		int scaleWidth = width;
		int scaleHeight = height;
		if (scaleX < scaleY && scaleY < 1){
			scaleWidth = 150;
			scaleHeight = height * scaleX;
		}else if (scaleY < scaleX && scaleX < 1){
			scaleWidth = width * scaleY;
			scaleHeight = 150;
		}
		Bitmap bm = Bitmap.createScaledBitmap(tembm, scaleWidth, scaleHeight, true);
		iv.setImageBitmap(bm);
		iv.setScaleType(ScaleType.CENTER_INSIDE);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(150, 150);
		llp.topMargin = 5;
		llp.leftMargin = 5;
		llp.rightMargin = 5;
		llp.bottomMargin = 5;
		iv.setLayoutParams(llp);
		
//		tembm.recycle();
		return iv;
	}
}
