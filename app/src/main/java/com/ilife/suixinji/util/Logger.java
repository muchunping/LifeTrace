package com.ilife.suixinji.util;

import android.util.Log;

import com.ilife.suixinji.data.Defines;

public class Logger {

	public static void logI(String info) {
		if (Defines.SHOW_LOG) {
			Log.i(Defines.LOG_TAG, Defines.LOG_INFO_HEAD + info);
		}
	}
}
