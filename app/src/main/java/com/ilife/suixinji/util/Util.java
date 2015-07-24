package com.ilife.suixinji.util;

import java.util.Random;

public class Util {
	
	private static Random random = new Random();
	
	public static String shortWithEllipsis(String input, int count) {
		if (input.length() <= count) {
			return input;
		}
		String output = input.substring(0, count-3);
		output = output.concat("...");
		return output;
	}
	
	public int getRandom(int max) {
		return Math.abs(random.nextInt()) % max;
	}
	
}
