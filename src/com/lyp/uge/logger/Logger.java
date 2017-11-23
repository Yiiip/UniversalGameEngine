package com.lyp.uge.logger;

import java.io.File;
import java.io.IOException;

/**
 * 日志级别（1级最低）：
 * 1 DEBUG - 调试时随意使用，实际运行不输出
 * 2 INFO - 实际运行时输出对用户有意义的信息
 * 3 WARN - 警告
 * 4 ERROR - 错误
 * @author LYP
 */
public class Logger {
	
	private final static String TAG_DEFAULT = "GameEngine";
	
	private static PrinterImpl mPrinter = new PrinterImpl();
	
	private Logger() {}
	
	public static void d(String message) {
		d(TAG_DEFAULT, message);
	}
	public static void d(String tag, String message) {
		mPrinter.debug(tag, message);
	}
	
	public static void i(String message) {
		i(TAG_DEFAULT, message);
	}
	public static void i(String tag, String message) {
		mPrinter.info(tag, message);
	}
	
	public static void w(String message) {
		w(TAG_DEFAULT, message);
	}
	public static void w(String tag, String message) {
		mPrinter.warn(tag, message);
	}

	public static void e(String message) {
		e(TAG_DEFAULT, message);
	}
	public static void e(Exception e) {
		e(TAG_DEFAULT, e);
	}
	public static void e(String tag, String message) {
		mPrinter.error(tag, message);
	}
	public static void e(String tag, Exception e) {
		mPrinter.error(tag, e);
	}
	
	public static void setLogOutLevel(Level level) {
		mPrinter.setLogOutLevel(level);
	}
	
	public static void setLogOutFile(File file) {
		try {
			mPrinter.setLogOutFile(file);
		} catch (IOException e) {
			e(e);
		}
	}
	
	public static void setLogMode(boolean logToConsole, boolean logToFile) {
		mPrinter.setLogOutMode(logToConsole, logToFile);
	}
	
	public static enum Level {
        DEBUG	("D", 1),
        INFO	("I", 2),
        WARN	("W", 3),
        ERROR	("E", 4);

        private String levelTag;
        private int levelValue;
        
        private Level(String tag, int levelValue) {
            this.levelTag = tag;
            this.levelValue = levelValue;
        }
        
        public String getLevelTag() {
            return levelTag;
        }
        
        public int getLevelValue() {
            return levelValue;
        }
    }
}
