package logic;

import java.io.IOException;

/** глобальные переменные и функции */
public class Vars {

    public final static String PROG_VER = "0.3.2";
    
    private static long processWait = 15*1000; // 15 секунд
    
    private static boolean isHelpCommand = false;
    private static boolean isCheckOnlyCommand = false;
    
    private static int connectPort = 80;
    
    /** надо ли показывать подсказку комманд программы (help) */
    public static boolean isHelpCommand() { return isHelpCommand; }
    
    /** только проверка хостов без записи в лог (check) */
    public static boolean isCheckOnlyCommand() { return isCheckOnlyCommand; }
    
    /** время ожидания повтора процесса пинга в миллисекундах */
    public static long getProcessWait() { return processWait; }
    
    /** порт для проверки подключения к соккету (80 http) */
    public static int getConnectPort() { return connectPort; }
    
    /** время ожидания повтора процесса пинга в секундах (строка) */
    public static String getWaitInSeconds() {
        long sec = processWait / 1000;
        return (sec >= 60) ? sec / 60 + "m" + (sec - (sec / 60 * 60)) + "s" : sec + "s"; 
    }
    
    /** чтение параметров коммандной строки */
    public static void readArgs(String[] args) {
        if (args.length > 0) {
            for (String p : args) {
                if (p.startsWith("-") | p.startsWith("/")) {
                    // это комманда, а не хост
                    p = p.substring(1).toLowerCase();
                    if (p.startsWith("h") | p.startsWith("?")) { // help
                        isHelpCommand = true;
                    }
                    if (p.startsWith("c")) { // check
                        isCheckOnlyCommand = true;
                    }
                    if (p.startsWith("p:")) { // port
                        try {
                        	connectPort = Integer.parseInt(p.substring(2)); 
                        } catch (NumberFormatException e) {}
                    }
                    if (p.startsWith("w:")) { // wait
                        try {
                            processWait = Long.parseLong(p.substring(2)) * 1000; 
                        } catch (NumberFormatException e) {}
                    }
                } else
                    Ping.addHost(p);
            }
        }
        if (Ping.getHostList().isEmpty()) {
            Ping.addHost("amazon.com");
            Ping.addHost("google.com");
            Ping.addHost("yandex.ru");
        }
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
            if (remnant >= sb.length()) sb.append(sb);
            else sb.append(sb.subSequence(0, remnant));
            remnant = len - sb.length();
        }
        return sb.toString();
    }
    
}
