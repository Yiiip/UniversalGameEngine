package com.lyp.uge.logger;

public interface Printer {

	void debug(String tag, String message);

	void info(String tag, String message);

	void warn(String tag, String message);

	void error(String tag, String message);
	
	void error(String tag, Exception e);

}
