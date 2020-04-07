package main;

import logic.Logger;
import logic.Ping;
import logic.Vars;
import view.Text;

/**
 *  Программа: JavaPingLogger v0.2b
 *  Автор: Сергей Шугаев
 */
public class PingLogger {
	
	public static void main(String[] args) {
		Vars.readArgs(args);
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
			while (System.currentTimeMillis() < timeWaitStop) {
				if (Vars.isKeyQuitPressed())
					break loop;
				Vars.sleep(200);
			}
		}
		System.exit(0);
	}
	
}
