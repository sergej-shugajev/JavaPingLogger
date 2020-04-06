package logic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Ping {

	static ArrayList<PingValue> hostList = new ArrayList<>();
	
	/** список всех пингуемых хостов */
	public static ArrayList<PingValue> getHostList() { return hostList; }
	
	/** добавляем хост для проверки пингом */
	public static void addHost(String host) {
		hostList.add(new PingValue(host));
	}
	
	/** пингуем все добавленные хосты */
	public static void process() {
		if (hostList.isEmpty())
			addHost("google.com");
		for(PingValue pv : hostList)
			pv.setPing(InetHostReachable(pv.getHost(), 80, 2000));
	}
	
	/** пингуем хост по порту, возвращает время ответа,
	 *  если возврат -1, то нет ответа или время истекло.
	 *  порт 7:echo, 80:http  */
	public static long InetHostReachable(String host, int port, int timeout) {
		Socket socket = new Socket();
		try {
			long timeStart = System.currentTimeMillis();
			socket.connect(new InetSocketAddress(host, port), timeout);
			if (socket.isConnected()) {
				socket.close();
				return System.currentTimeMillis() - timeStart;
			}
		} catch (IOException e) {}
		return -1;
	}
	
	/** структура для заполнения массива хостов */
	public static class PingValue {
		private String host;
		private LocalDateTime datetime;
		private long ping;
		private long minPing;
		private long maxPing;
		
		/** добавляем название хоста */
		PingValue (String host) {
			this.host = host;
			this.datetime = LocalDateTime.now();
			this.ping = -1;
			this.minPing = -1;
			this.maxPing = 0;
		}
		
		/** запись пинга и установка min : max */
		public void setPing(long  ping) {
			this.ping = ping;
			this.datetime = LocalDateTime.now();
			if (this.minPing > ping | this.minPing == -1) this.minPing = ping;
			if (this.maxPing < ping) this.maxPing = ping;
		}
		
		/** имя хоста */
		public String getHost() { return this.host; }
		/** дата и вмеря действия */
		public LocalDateTime getDateTime() { return this.datetime; }
		/** время пинга */
		public long getPing() { return this.ping; }
		/** минимальный пинг */
		public long getMinPing() { return this.minPing; }
		/** миксимальный пинг */
		public long getMaxPing() { return this.maxPing; }
		
	}

}
