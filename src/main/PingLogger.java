package main;

import logic.Logger;
import logic.Ping;
import logic.Vars;
import view.Text;

/**
 *  Программа: JavaPingLogger v0.2
 *  Автор: Сергей Шугаев
 */
public class PingLogger {
	
	public static void main(String[] args) {
		Ping.addHost("amazon.com");
		Ping.addHost("google.com");
		Ping.addHost("yandex.ru");
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
