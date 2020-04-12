package logic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;

import logic.Ping.PingValue;

/** линия времени изменения отклика хостов */
public class TimeLine {

    final static int LINE_SIZE = 39;
    
    static long howDeleted = 0;
    
    static LinkedList<TimeLineValue> timeList = new LinkedList<>();
    
    /** сколько было удалено (при переполнении) */
    public static long getHowDeleted() { return howDeleted; }
    
    /** размер линии времени */
    public static int getSize() { return timeList.size();  }
    
    /** линия времени измерения отклика хостов */
    public static LinkedList<TimeLineValue> getList() { return timeList; }
    
    /** взять значение времени измерения отклика хостов
     * @return LinkedList<TimeLineValue> */
    public static TimeLineValue get(int index) { return timeList.get(index); }
    
    /** добавляем измерения отклика хостов в линию времени */
    public static void addPingsToLine() {
        timeList.addLast(new TimeLineValue(Ping.getHostList()));
        if (timeList.size() > LINE_SIZE) {
            timeList.removeFirst();
            howDeleted++;
        }
    }
    
    /** структура для запоминания времени отклика хостов */
    public static class TimeLineValue {
        private LocalDateTime datetime = LocalDateTime.now();
        private ArrayList<Integer> pinglist = new ArrayList<>();
        
        /** запись времени и отклика хостов */
        public TimeLineValue(ArrayList<PingValue> pv) {
            for(PingValue p : pv)
                this.pinglist.add(p.getPing());
        }
        
        /** дата и время добавления списка пингов */
        public LocalDateTime getDateTime() { return this.datetime; }
        
        /** время добавления списка пингов (форматированно HH:mm) */
        public String getTimeFormatted() { return this.datetime.format(DateTimeFormatter.ofPattern("HH:mm:ss")); }
        
        /** список пингов */
        public ArrayList<Integer> getPingList() { return this.pinglist; }
        
    }

}
