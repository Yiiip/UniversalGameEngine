package com.lyp.uge.logger;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.lyp.uge.logger.Logger.Level;

public class PrinterImpl implements Printer {
	/** 每条 Log 的 tag 输出的最大长度, 超过部分将被截断 */
	private static final int TAG_MAX_LENGTH = 20;

	/** 每条 Log 的 message 输出的最大长度, 超过部分将被截断 */
	private static final int MESSAGE_MAX_LENGTH = 1024;

	/** 日期前缀格式化 */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

	/** 日志当前的输出级别, 默认为 INFO 级别 */
	private static Level logOutLevel = Level.INFO;

	/** 是否输出到控制台, 默认输出 */
	private static boolean isOutToConsole = true;

	/** 是否输出到文件 */
	private static boolean isOutToFile = false;

	/** 日志输出文件, 追加到文件尾 */
	private static File logOutFile;

	/** 日志文件输出流, 追加到文件尾 */
	private static RandomAccessFile logOutFileStream;

	@Override
	public void debug(String tag, String message) {
		printLog(Level.DEBUG, tag, message, false);
	}

	@Override
	public void info(String tag, String message) {
		printLog(Level.INFO, tag, message, false);
	}

	@Override
	public void warn(String tag, String message) {
		printLog(Level.WARN, tag, message, false);
	}

	@Override
	public void error(String tag, String message) {
		printLog(Level.ERROR, tag, message, true);
	}

	@Override
	public void error(String tag, Exception e) {
		if (e == null) {
			error(tag, (String) null);
			return;
		}

		PrintStream printOut = null;
		try {
			ByteArrayOutputStream bytesBufOut = new ByteArrayOutputStream();
			printOut = new PrintStream(bytesBufOut);
			e.printStackTrace(printOut);
			printOut.flush();
			error(tag, new String(bytesBufOut.toByteArray(), "UTF-8"));
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			closeStream(printOut);
		}
	}

	private void printLog(Level level, String tag, String message, boolean isOutToErr) {
		if (level.getLevelValue() >= logOutLevel.getLevelValue()) {
			String log = DATE_FORMAT.format(new Date()) + " " 
					+ "[" + level.getLevelTag() + "]"
					+ checkTextLengthLimit(tag, TAG_MAX_LENGTH) + ": "
					+ checkTextLengthLimit(message, MESSAGE_MAX_LENGTH);

			if (isOutToConsole) {
				outLogToConsole(isOutToErr, log);
			}
			if (isOutToFile) {
				outLogToFile(log);
			}
		}
	}
	
	public void setLogOutLevel(Level currentLevel) {
		logOutLevel = currentLevel == null ? Level.INFO : currentLevel;
	}

	public synchronized void setLogOutFile(File logOutFile) throws IOException {
		PrinterImpl.logOutFile = logOutFile;

		if (logOutFileStream != null) {
			closeStream(logOutFileStream);
			logOutFileStream = null;
		}

		if (PrinterImpl.logOutFile != null) {
			try {
				logOutFileStream = new RandomAccessFile(PrinterImpl.logOutFile, "rw");
				logOutFileStream.seek(PrinterImpl.logOutFile.length());
			} catch (IOException e) {
				closeStream(logOutFileStream);
				logOutFileStream = null;
				throw e;
			}
		}
	}

	public void setLogOutMode(boolean isOutToConsole, boolean isOutToFile) {
		PrinterImpl.isOutToConsole = isOutToConsole;
		PrinterImpl.isOutToFile = isOutToFile;
	}

	private synchronized void outLogToConsole(boolean isOutToErr, String log) {
		if (isOutToErr) {
			// System.err 和 System.out 是两个不同的输出流通道, 如果极短时间内连
			// 续输出 log 到 err 和 out, 控制台上的打印顺序可能会不完全按时序打印.
			System.err.println(log);
		} else {
			System.out.println(log);
		}
	}

	private synchronized void outLogToFile(String log) {
		if (logOutFileStream != null) {
			try {
				logOutFileStream.write((log + "\n").getBytes("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String checkTextLengthLimit(String text, int maxLength) {
		if ((text != null) && (text.length() > maxLength)) {
			text = text.substring(0, maxLength - 3) + "...";
		}
		return text;
	}

	private void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (Exception e) {
			}
		}
	}
}
