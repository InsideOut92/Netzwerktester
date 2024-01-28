import javax.swing.*;
import java.awt.*;

public class NetzwerkueberwachungGUI extends JFrame {

    private static final String ZIEL_ADRESSE = "www.google.com";
    private static final int UEBERWACHUNGS_INTERVALL = 5000;

    private MonitoringThread monitoringThread;
    private JButton startButton;
    private JButton stopButton;
    private JTextArea logTextArea;

    public NetzwerkueberwachungGUI() {
        setTitle("Netzwerküberwachung");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridBagLayout());
        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        startButton = new JButton("Überwachung starten");
        startButton.addActionListener(e -> startMonitoring());
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(startButton, gbc);

        stopButton = new JButton("Überwachung stoppen");
        stopButton.setEnabled(false);
        stopButton.addActionListener(e -> stopMonitoring());
        gbc.gridx = 1;
        add(stopButton, gbc);

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(new JScrollPane(logTextArea), gbc);
    }

    private void startMonitoring() {
        monitoringThread = new MonitoringThread(ZIEL_ADRESSE, UEBERWACHUNGS_INTERVALL, logTextArea);
        monitoringThread.start();

        startButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    private void stopMonitoring() {
        if (monitoringThread != null) {
            monitoringThread.stopMonitoring();
            monitoringThread = null;
        }

        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NetzwerkueberwachungGUI().setVisible(true));
    }
}
