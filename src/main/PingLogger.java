package main;

import logic.Logger;
import logic.Ping;
import logic.Vars;
import view.Text;

/**
 *  JavaPingLogger
 *   - Программа для проверки сайтов на соединение с логированием. 
 *  @version v0.2e
 *  @author Sergej Shugajev
 */
public class PingLogger {
	
	public static void main(String[] args) {
		Vars.readArgs(args);
//		Vars.readArgs(new String[] {"/w:0", "google.com", "google.com", "google.com"});
//		Vars.readArgs(new String[] {"/c", "/w:1", "google.com", "yandex.ru"});
		if (Vars.isHelpCommand()) {
			Text.doViewHelp();
			System.exit(0);
		}
		// обычная работа программы
		Text.view();
		Logger.begin();
		loop:
		while (true) {
			Ping.process();
			Logger.saveAllPings();
			Text.view();
			long timeWaitStop = System.currentTimeMillis() + Vars.getProcessWait();
			// ожидаем нажатия "q" или окончания задержки
			while (System.currentTimeMillis() <= timeWaitStop) {
				if (Vars.isKeyQuitPressed())
					break loop;
				Vars.sleep(200);
			}
		}
		System.exit(0);
	}
	
}
