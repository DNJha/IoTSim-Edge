package org.edge.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class LogUtil {

	public enum Level {
		DEBUG("debug"), INFOR("infor"), ERROR("error"), WARNING("warning");

		private String value;

		Level(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	private static Level level = Level.DEBUG;
	private static String path;
	private static boolean saveLogToFile;
	private static BufferedWriter bufferedWriter;
	private static boolean append=true;
	public static  void initLog(Level level,String path,boolean saveLogToFile,boolean append) {
		LogUtil.append=append;
		initLog(level, path, saveLogToFile);
	
	}
	/**
	 * must be called in initialization part if needing to write log into file
	 */
	
	public static  void initLog(Level level,String path,boolean saveLogToFile) {
	
		LogUtil.path=path;
		LogUtil.saveLogToFile=saveLogToFile;
		LogUtil.level=level;
		if(saveLogToFile) {
			FileWriter fileWriter;
			try {
				fileWriter = new FileWriter(path, append);
				bufferedWriter = new BufferedWriter(fileWriter);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void info(String msg) {
		if (level.ordinal() <= 1) {
			System.out.println(msg);
			if (saveLogToFile) {
				appendTextToFile(msg);
			}

		}

	}

public static void simulationFinished() {
	if(saveLogToFile && bufferedWriter!=null) {
		try {
			bufferedWriter.flush();
			bufferedWriter.close();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
	public static void appendTextToFile(String text) {
		try {
			bufferedWriter.write(text);
			bufferedWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
