package logic;

import java.io.IOException;

/** глобальные переменные и функции */
public class Vars {

	public final static String PROG_VER = "0.2";
	
	private static long processWait = 15*1000; // 5*60*1000 = 5 минут
	
	/** время ожидания повтора процесса пинга в миллисекундах */
	public static long getProcessWait() { return processWait; }
	
	/** время ожидания повтора процесса пинга в секундах (строка) */
	public static String getWaitInSeconds() {
		long sec = processWait / 1000;
		return (sec >= 60) ? sec / 60 + "m" + (sec - (sec / 60 * 60)) + "s" : sec + "s"; 
	}
	
	/** задержка в миллисекундах */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {}
	}
	
	/** нажатие кнопки "q" */
	public static boolean isKeyQuitPressed() {
		char code = 0;
		try {
			if (System.in.available() == 0 )
				return false;
			code = (char) System.in.read();
		} catch (IOException e) {
			return false;
		}
		return (code == 'q' || code == 'Q');
	}
	
	/** создает строку с количеством пробелов len */
	public static String spaces(int len) {
		return (len <= 0) ? "" : String.format("%" + len + "s", "");
	}
	
	/** создаем строку символов char количеством len */
	public static String dummyString(char c, int len) {
		if (len < 1) return "";
		StringBuilder sb = new StringBuilder(len).append(c);
		int remnant = len - sb.length();
		while (remnant > 0) {
			if (remnant >= sb.length())	sb.append(sb);
			else sb.append(sb.subSequence(0, remnant));
			remnant = len - sb.length();
		}
		return sb.toString();
	}
	
}
