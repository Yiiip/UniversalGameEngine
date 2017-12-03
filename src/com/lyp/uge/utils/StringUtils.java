package com.lyp.uge.utils;

public class StringUtils {

	public static String formatTime(int num) {
		String retStr;
		if (num >= 0 && num < 10) {
			retStr = "0" + num;
		} else {
			retStr = "" + num;
		}
		return retStr;
	}
}
