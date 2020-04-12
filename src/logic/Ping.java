package logic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

/** главный пингонатор */
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
        for(PingValue pv : hostList)
            pv.setPing(InetHostReachable(pv.getHost(), 80, 2000));
    }
    
    /** пингуем хост по порту, возвращает время ответа,
     *  если возврат -1, то нет ответа или время истекло.
     *  порт 7:echo, 80:http  */
    public static int InetHostReachable(String host, int port, int timeout) {
        Socket socket = new Socket();
        try {
            long timeStart = System.currentTimeMillis();
            socket.connect(new InetSocketAddress(host, port), timeout);
            if (socket.isConnected()) {
                socket.close();
                return (int)(System.currentTimeMillis() - timeStart);
            }
        } catch (IOException e) {}
        return -1;
    }
    
    /** структура для запоминания отклика хостов */
    public static class PingValue {
        private String host;
        private LocalDateTime datetime = LocalDateTime.now();
        private int ping = -1;
        private int minPing = -1;
        private int maxPing = 0;
        
        /** добавляем название хоста */
        PingValue (String host) {
            this.host = host;
        }
        
        /** запись пинга и установка min : max */
        public void setPing(int  ping) {
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
        public int getPing() { return this.ping; }
        
        /** минимальный пинг */
        public int getMinPing() { return this.minPing; }
        
        /** миксимальный пинг */
        public int getMaxPing() { return this.maxPing; }
        
    }

}
