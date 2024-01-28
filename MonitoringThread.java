import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MonitoringThread extends Thread {

    private static final String LOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String LOG_FILE_PATH = "ping_log.txt";

    private final String zielAdresse;
    private final int ueberwachungsIntervall;
    private final JTextArea logTextArea;
    private volatile boolean isMonitoring;

    public MonitoringThread(String zielAdresse, int ueberwachungsIntervall, JTextArea logTextArea) {
        this.zielAdresse = zielAdresse;
        this.ueberwachungsIntervall = ueberwachungsIntervall;
        this.logTextArea = logTextArea;
        this.isMonitoring = true;
    }

    @Override
    public void run() {
        while (isMonitoring) {
            boolean istVerbindungOk = ueberwacheVerbindung(zielAdresse);

            if (!istVerbindungOk) {
                logTextArea.append("Verbindungsprobleme erkannt!\n");
            }

            try {
                Thread.sleep(ueberwachungsIntervall);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopMonitoring() {
        isMonitoring = false;
    }

    private boolean ueberwacheVerbindung(String zielAdresse) {
        try {
            InetAddress address = InetAddress.getByName(zielAdresse);
            long startTime = System.currentTimeMillis();
            boolean isReachable = address.isReachable(5000);
            long endTime = System.currentTimeMillis();
            long pingZeit = endTime - startTime;

            logTextArea.append("Ping-Zeit: " + pingZeit + " ms\n");

            if (!isReachable || pingZeit > 100) {
                loggePingWert(pingZeit);
            }

            return isReachable && pingZeit <= 100;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void loggePingWert(long pingZeit) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            String formattedDate = new SimpleDateFormat(LOG_DATE_FORMAT).format(new Date());
            writer.write(formattedDate + " - Ping-Zeit: " + pingZeit + " ms\n");
            logTextArea.append("Ping-Wert über 100ms wurde geloggt.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
