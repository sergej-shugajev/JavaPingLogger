package view;

import static java.lang.System.out;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import logic.Logger;
import logic.Ping;
import logic.TimeLine;
import logic.TimeLine.TimeLineValue;
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
        String[] diagm = createTextDiagram(7);
        int rowRead = 0;
        for(PingValue pv : Ping.getHostList()) {
            out.printf("%-47.47s %-16.16s %.3s (%.3s:%.4s)%n",
                    (rowRead >= diagm.length) ? "" : diagm[rowRead],
                            pv.getHost(), pv.getPing(), pv.getMinPing(), pv.getMaxPing());
            rowRead++;
        }
        for(int rowEnd = rowRead; rowEnd < diagm.length; rowEnd++) {
            out.printf("%-47.47s%n", diagm[rowEnd]);
        }
        out.println("\nPress \"Q\" key and Enter to Quit.");
    }
    
    /** создаем текстовую диаграмму */
    static String[] createTextDiagram(int rows) {
        String[] strRet = new String[rows];
        // проверки на малое количество строк
        if (rows < 3) {
            if (rows > 0) strRet[0] = "";
            if (rows > 1) strRet[1] = "";
            return strRet;
        }
        // ищем максимальный пинг
        int pingMax = 0;
        for(TimeLineValue tl : TimeLine.getList()) {
            for(Integer ping : tl.getPingList())
                if (ping > pingMax)
                    pingMax = ping;
        }
        // отрисовка линий пинга
        StringBuilder sbLine = new StringBuilder();
        int pingStep = pingMax / (rows - 2);
        for(int pingRow = 0; pingRow < (rows - 2); pingRow++) {
            sbLine.setLength(0);
            nextChar:
            for(TimeLineValue tl : TimeLine.getList()) {
                int pingIndex = 0;
                for(Integer ping : tl.getPingList()) {
                    // если пинг в диапазоне
                    if (ping <= pingMax && ping > pingMax - pingStep) {
                        sbLine.append((char)('0' + pingIndex));
                        continue nextChar;
                    }
                    pingIndex++;
                }
                sbLine.append(' ');
            }
            strRet[pingRow] = String.format(" %4.4s |%39.39s|", pingMax, sbLine.toString());
            pingMax = pingMax - pingStep;
        }
        // отрисовка временной шкалы
        final int TIME_STEP = 10;
        final int TIME_STR_LEN = 5+3;
        sbLine.setLength(0);
        StringBuilder sbTime = new StringBuilder();
        for(int i = 0; i < 39; i++) {
            if (i < TimeLine.getSize()) {
                int step_mod = (int)((TimeLine.getHowDeleted() + i) % TIME_STEP);
                if (step_mod == 0) {
                    sbLine.append('|');
                    sbTime.append(TimeLine.get(i).getTimeFormatted());
                } else {
                    sbLine.append('-');
                    if (i == 0 && TimeLine.getHowDeleted() > 0) {
                        // обработка случая, когда время слева обрезано
                        sbTime.append(Vars.spaces(TIME_STR_LEN - step_mod));
                    }
                    if (step_mod >= TIME_STR_LEN)
                        sbTime.append(' '); // отступ между
                }
            } else {
                sbLine.insert(0, '-');
                sbTime.insert(0, ' ');
            }
        }
        strRet[rows-2] = String.format("      |%39.39s|", sbLine.toString());        
        strRet[rows-1] = String.format("       %39.39s", sbTime.toString());
        return strRet;
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
