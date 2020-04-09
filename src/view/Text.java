package view;

import static java.lang.System.out;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import logic.Logger;
import logic.Ping;
import logic.Ping.PingValue;
import logic.Vars;

public class Text {

	/** показать справку по программе (help) */
	public static void doViewHelp() {
		out.println("Usage: JavaPingLogger [-c] [-w:<wait>] [<host>] [<host>]\n\nOptions:");
		out.printf("%4s %-12s Checking hosts without logging.\n", "", "-c");
		out.printf("%4s %-12s Timeout before re-checking hosts (in seconds).\n", "", "-w:<wait>");
		out.printf("%4s %-12s The name of the host to be checked.\n", "", "<host>");
	}
	
	/** показать изменения и пинги */
	public static void view() {
		if (Logger.getLogFile() == null)
			Logger.init();
		clear();
		out.printf("Program: JavaPingLogger v%-22s Date: %-10.10s Time: %-8.8s%n",
				Vars.PROG_VER, LocalDate.now(), LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		out.printf("    Log: %-38s Wait: %-10.10s Rows: %-8.8s%n",
				Logger.getLogFile(), Vars.getWaitInSeconds(), Logger.getRowsInLog());
		// хосты
		out.println(Vars.dummyString('-', 79));
		out.printf("%" + 47 + "s host: %10s ping:%n", "", "");
		for(PingValue pv : Ping.getHostList())
			out.printf("%" + 47 + "s %-16.15s %.3s (%.3s:%.4s)%n",
					"", pv.getHost(), pv.getPing(), pv.getMinPing(), pv.getMaxPing());
		out.println("\nPress \"Q\" key and Enter to Quit.");
	}
	
	/** очистка консоли, если терминал поддерживает коды удаления ANSI */
	static void clearScreen() {
	    out.print("\033[H\033[2J");
	    out.flush();
	}
		
	/** очистка консоли */
	static void clear() {
		final boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
		try {
			if (isWindows)
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			else
				//Runtime.getRuntime().exec("clear");
				clearScreen();
		} catch (InterruptedException | IOException e) {}
	}

}
