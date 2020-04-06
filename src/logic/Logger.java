package logic;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import logic.Ping.PingValue;

/** ведение записи измерений пинга */
public class Logger {

	/** загловок для CSV файла */
	final static String LOG_COLS_NAMES = "host;time;ping\n";

	static LocalDateTime dateTimeStart;
	static long rowsInLog;
	static String logFile;
		
	/** имя лог файла для записи измерений пинга */
	public static String getLogFile() { return logFile; }
	
	/** записано строк в лог файл */
	public static long getRowsInLog() { return rowsInLog; }
	
	/** инициализация переменных */
	public static void init() {
		rowsInLog = 0;
		dateTimeStart = LocalDateTime.now();
		logFile = "ping_" 
				+ dateTimeStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))
				+ ".csv";
	}
	
	/** начало записи в лог (создание файла и шапка) */
	public static void begin() {
		if (logFile == null)
			init();
		try (FileWriter fw = new FileWriter(logFile, false)) {
			fw.write(LOG_COLS_NAMES);
			fw.flush();
    		rowsInLog = 1;
        }
        catch (IOException ex) {} 
	}

	/** сохранение всех хостов в лог */
	public static void saveAllPings() {
		if (rowsInLog == 0)
			begin();
		try (FileWriter fw = new FileWriter(logFile, true)) {
			for(PingValue pv : Ping.getHostList()) {
				fw.write(pv.getHost() + ";"
						+ pv.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + ";"
						+ pv.getPing() + "\n");
	    		rowsInLog++;
			}
			fw.flush();
        }
        catch (IOException ex) {} 
	}

}
