package com.ilife.suixinji.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class IOOperation {
	private static String FILENAME_SAVEPICTURE_DIRECTORY = "SuiXinJi";
	public static final String FILENAME_SAVEPICTURE_FILENAME_PREFIX = "cache";

	private static boolean ensureSavegameDirectoryExists() {
		File root = Environment.getExternalStorageDirectory();
		File dir = new File(root, FILENAME_SAVEPICTURE_DIRECTORY);
		if (!dir.exists()) return dir.mkdir();
		return true;
	}
	
	private static File getSavegameDirectory() {
		File root = Environment.getExternalStorageDirectory();
		return new File(root, FILENAME_SAVEPICTURE_DIRECTORY);
	}
	
	public static FileInputStream getInputFile(Context c, String filename) throws FileNotFoundException{
		ensureSavegameDirectoryExists();
		File root = getSavegameDirectory();
		return new FileInputStream(new File(root, filename));
	}
	
	public static File getTempFile(){
		File root = getSavegameDirectory();
		return new File(root, "tempfile.jpg");
	}
	
	public static void saveToFile(){
		
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
	        return inSampleSize;
	}
	
	public static File getOutputFile() throws FileNotFoundException{
		ensureSavegameDirectoryExists();
		return new File(getSavegameDirectory(), System.currentTimeMillis() + ".jpg");
	}
}
