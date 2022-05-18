import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.DimensionUIResource;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;

public class GUICountWords extends JFrame {
    // --------------------------------------------------
    // UI-Elemente
    // --------------------------------------------------
    private JPanel mainPanel;
    private JPanel panelDownloadWebsites;
    private JPanel panelEvaluation;
    private JPanel panelChooseWebsiteFile;
    private JPanel panelHTTrackExe;
    private JPanel panelHTTrackOutputFolder;
    private JPanel panelHTTrackOptions;
    private JPanel panelDownloadWebsitesButtons;
    private TitledBorder titledBorderDW;
    private TitledBorder titledBorderEval;
    private JLabel labelChooseWebsiteFile;
    private JLabel labelHTTrackExe;
    private JLabel labelHTTrackOutputFolder;
    private JLabel labelStartCycleIn;
    private JLabel labelHours;
    private JLabel labelMinutes; 
    private JLabel labelNextDownloadAt;
    private JTextField textFieldWebsiteFile;
    private JTextField textFieldHTTrackExe;
    private JTextField textFieldHTTrackOutputFolder;
    private JButton buttonChooseWebsiteFile;
    private JButton buttonChooseHTTrackExe;
    private JButton buttonChooseHTTrackOutputFolder;
    private JButton buttonStartHTTrack;
    private JButton buttonStartDownloadCycle;
    private JButton buttonStopDownloadCycle;
    private JRadioButton radioButtonOnlyHTMLFiles;
    private JRadioButton radioButtonAllFiles;
    private ButtonGroup buttonGroupTypeOfFiles;
    private JSpinner spinnerDownloadInHours;
    private JSpinner spinnerDownloadInMinutes;
    private JFileChooser chooseHTTrackExe; 
    private JFileChooser chooseWebsiteFile;
    private JFileChooser chooseHTTrackOutputFolder;
    
    public static void main(String[] args) throws Exception {
        new GUICountWords();
    }

    public GUICountWords(){
        setTitle("Word Counter");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024,768);
        
        initComponentMainPanelAndOthers();
        addWindowEvents();

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void initComponentMainPanelAndOthers(){
        mainPanel = new JPanel();
        panelDownloadWebsites = new JPanel();
        panelEvaluation = new JPanel();
        titledBorderDW = new TitledBorder("Website-Download (mittels HTTrack)");
        titledBorderEval = new TitledBorder("Auswertungen");
        
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        panelDownloadWebsites.setBorder(titledBorderDW);
        panelEvaluation.setBorder(titledBorderEval);

        initComponentHTTrackExe(panelDownloadWebsites);
        initComponentChooseWebSiteFile(panelDownloadWebsites);
        initComponentChooseHTTrackOutputFolder(panelDownloadWebsites);
        initComponentHTTrackOptions(panelDownloadWebsites);
        initComponentStartWebsiteDownload(panelDownloadWebsites);

        mainPanel.add(panelDownloadWebsites);
        panelEvaluation.setPreferredSize(new DimensionUIResource(panelEvaluation.getWidth(), 310));
        mainPanel.add(panelEvaluation);
        add(mainPanel);
    }
    
    // --------------------------------------------------
    // Pfad zur HTTrack-Exe festlegen
    // --------------------------------------------------
    private void initComponentHTTrackExe(JPanel addToPanel){
        panelHTTrackExe = new JPanel();
        labelHTTrackExe = new JLabel("HTTrack-exe-Datei:", SwingConstants.RIGHT);
        textFieldHTTrackExe = new JTextField(65);
        buttonChooseHTTrackExe = new JButton("Datei selektieren...");
        chooseHTTrackExe = new JFileChooser();

        panelHTTrackExe.setLayout(new FlowLayout());
        labelHTTrackExe.setPreferredSize(new DimensionUIResource(150, 30));
        panelHTTrackExe.add(labelHTTrackExe);
        textFieldHTTrackExe.setEditable(false);
        panelHTTrackExe.add(textFieldHTTrackExe);
        buttonChooseHTTrackExe.addActionListener(new MyActionListener());
        buttonChooseHTTrackExe.setPreferredSize(new DimensionUIResource(150, 25));
        panelHTTrackExe.add(buttonChooseHTTrackExe);

        addToPanel.add(panelHTTrackExe);
    }

    // --------------------------------------------------
    // Komponente Webseiten-Datei auswählen
    // --------------------------------------------------
    private void initComponentChooseWebSiteFile(JPanel addToPanel){
        panelChooseWebsiteFile = new JPanel();
        labelChooseWebsiteFile = new JLabel("Datei mit Webseiten:", SwingConstants.RIGHT);
        textFieldWebsiteFile = new JTextField(65);
        buttonChooseWebsiteFile = new JButton("Datei selektieren...");
        chooseWebsiteFile = new JFileChooser();

        panelChooseWebsiteFile.setLayout(new FlowLayout());
        labelChooseWebsiteFile.setPreferredSize(new DimensionUIResource(150, 30));
        panelChooseWebsiteFile.add(labelChooseWebsiteFile);
        textFieldWebsiteFile.setEditable(false);
        panelChooseWebsiteFile.add(textFieldWebsiteFile);
        buttonChooseWebsiteFile.addActionListener(new MyActionListener());
        buttonChooseWebsiteFile.setPreferredSize(new DimensionUIResource(150, 25));
        panelChooseWebsiteFile.add(buttonChooseWebsiteFile);

        addToPanel.add(panelChooseWebsiteFile);
    }

    // --------------------------------------------------
    // HTTrack Output Folder festlegen
    // --------------------------------------------------
    private void initComponentChooseHTTrackOutputFolder(JPanel addToPanel){
        panelHTTrackOutputFolder = new JPanel();
        labelHTTrackOutputFolder = new JLabel("HTTrack Ausgabeordner:", SwingConstants.RIGHT);
        textFieldHTTrackOutputFolder = new JTextField(65);
        buttonChooseHTTrackOutputFolder = new JButton("Ordner selektieren...");
        chooseHTTrackOutputFolder = new JFileChooser();
        chooseHTTrackOutputFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        panelHTTrackOutputFolder.setLayout(new FlowLayout());
        labelHTTrackOutputFolder.setPreferredSize(new DimensionUIResource(150, 30));
        panelHTTrackOutputFolder.add(labelHTTrackOutputFolder);
        textFieldHTTrackOutputFolder.setEditable(false);
        panelHTTrackOutputFolder.add(textFieldHTTrackOutputFolder);
        buttonChooseHTTrackOutputFolder.addActionListener(new MyActionListener());
        buttonChooseHTTrackOutputFolder.setPreferredSize(new DimensionUIResource(150, 25));
        panelHTTrackOutputFolder.add(buttonChooseHTTrackOutputFolder);

        addToPanel.add(panelHTTrackOutputFolder);
    }

    // --------------------------------------------------
    // HTTrack Optionen
    // --------------------------------------------------
    private void initComponentHTTrackOptions(JPanel addToPanel){
        panelHTTrackOptions = new JPanel(); 
        radioButtonOnlyHTMLFiles = new JRadioButton("Nur HTML-Dateien");
        radioButtonOnlyHTMLFiles.setSelected(true);
        radioButtonAllFiles = new JRadioButton("Alle Dateien");
        buttonGroupTypeOfFiles = new ButtonGroup();
        buttonGroupTypeOfFiles.add(radioButtonOnlyHTMLFiles);
        buttonGroupTypeOfFiles.add(radioButtonAllFiles);

        panelHTTrackOptions.setLayout(new FlowLayout());
        panelHTTrackOptions.setPreferredSize(new DimensionUIResource(800, 30));
        panelHTTrackOptions.add(radioButtonOnlyHTMLFiles);
        panelHTTrackOptions.add(radioButtonAllFiles);

        addToPanel.add(panelHTTrackOptions);
    }

    // --------------------------------------------------
    // Buttons für den WebsiteDownload initialisieren
    // --------------------------------------------------
    private void initComponentStartWebsiteDownload(JPanel addToPanel){
        panelDownloadWebsitesButtons = new JPanel();
        buttonStartHTTrack = new JButton("Download jetzt starten");
        labelStartCycleIn = new JLabel("Download alle: ");
        spinnerDownloadInHours = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        labelHours = new JLabel("Stunden");
        spinnerDownloadInMinutes = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        labelMinutes = new JLabel("Minuten");
        buttonStartDownloadCycle = new JButton("Zyklus starten");
        buttonStopDownloadCycle = new JButton("Zyklus stoppen");
        labelNextDownloadAt = new JLabel();
        
        panelDownloadWebsitesButtons.setLayout(new FlowLayout());

        buttonStartHTTrack.addActionListener(new MyActionListener());
        panelDownloadWebsitesButtons.add(buttonStartHTTrack);
        panelDownloadWebsitesButtons.add(labelStartCycleIn);
        panelDownloadWebsitesButtons.add(spinnerDownloadInHours);
        panelDownloadWebsitesButtons.add(labelHours);
        panelDownloadWebsitesButtons.add(spinnerDownloadInMinutes);
        panelDownloadWebsitesButtons.add(labelMinutes);
        buttonStartDownloadCycle.addActionListener(new MyActionListener());
        panelDownloadWebsitesButtons.add(buttonStartDownloadCycle);
        buttonStopDownloadCycle.addActionListener(new MyActionListener());
        buttonStopDownloadCycle.setEnabled(false);
        panelDownloadWebsitesButtons.add(buttonStopDownloadCycle);
        panelDownloadWebsitesButtons.add(labelNextDownloadAt);
        
        addToPanel.add(panelDownloadWebsitesButtons);
    }

    // --------------------------------------------------
    // Was soll passieren wenn sich das Fenster schließt
    // --------------------------------------------------
    private void addWindowEvents(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                connectToDB();
                LoadAllSettings();
            }
            @Override
            public void windowClosing(WindowEvent e) {
                SaveAllSettings();
                disconnectFromDB();
                System.exit(0);
            }
        });
    }

    // --------------------------------------------------
    // Innere Klasse um auf Benutzeraktionen zu reagieren
    // --------------------------------------------------
    class MyActionListener implements ActionListener {
        private boolean checkIfDownloadIsPossible(){
            if(textFieldHTTrackExe.getText().isEmpty()){
                JOptionPane.showMessageDialog(GUICountWords.this, "Keine HTTrack-Exe selektiert", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(textFieldWebsiteFile.getText().isEmpty()){
                JOptionPane.showMessageDialog(GUICountWords.this, "Keine Webseiten-Datei selektiert", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(textFieldHTTrackOutputFolder.getText().isEmpty()){
                JOptionPane.showMessageDialog(GUICountWords.this, "Keine Ausgabeordner angegeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true; 
        }
        private boolean checkIfCycleIsPossible(){
            if(((int)spinnerDownloadInHours.getValue() < 1) && ((int)spinnerDownloadInMinutes.getValue() < 1)){
                JOptionPane.showMessageDialog(GUICountWords.this, "Zykluswert muss größer 0 sein", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true; 
        }
        private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        private Runnable runnableDownloadCycle = new Runnable() {
            public void run() {
                File tempFileDownloadRunning = new File(".\\DownloadIsRunning"); 
                File tempFileEvaluationRunning = new File(".\\EvaluationIsRunning"); 
                if(tempFileDownloadRunning.exists()){
                    System.out.println("Download läuft bereits, warten auf den nächsten Zyklus");
                }
                if(tempFileEvaluationRunning.exists()){
                    System.out.println("Es läuft gerade eine Auswertung, warten auf den nächsten Zyklus");
                }
                if((!tempFileDownloadRunning.exists()) && (!tempFileEvaluationRunning.exists())){
                    startHTTrackDownload();
                }
                String nextCycleDateTime = getNextCycleDateTime(
                    (int)spinnerDownloadInHours.getValue(), 
                    (int)spinnerDownloadInMinutes.getValue()
                );
                labelNextDownloadAt.setText("Download startet um: " + nextCycleDateTime);
            }
        };
        private ScheduledFuture<?> threadDownloadCycle;
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == buttonChooseHTTrackExe){
                int result = chooseHTTrackExe.showOpenDialog(GUICountWords.this);
                if(result == JFileChooser.APPROVE_OPTION){
                    textFieldHTTrackExe.setText(chooseHTTrackExe.getSelectedFile().toString());
                }
            }
            if(e.getSource() == buttonChooseWebsiteFile){
                int result = chooseWebsiteFile.showOpenDialog(GUICountWords.this);
                if(result == JFileChooser.APPROVE_OPTION){
                    textFieldWebsiteFile.setText(chooseWebsiteFile.getSelectedFile().toString());
                }
            }
            if(e.getSource() == buttonChooseHTTrackOutputFolder){
                int result = chooseHTTrackOutputFolder.showOpenDialog(GUICountWords.this);
                if(result == JFileChooser.APPROVE_OPTION){
                    textFieldHTTrackOutputFolder.setText(chooseHTTrackOutputFolder.getSelectedFile().toString());
                }
            }
            if(e.getSource() == buttonStartHTTrack){
                if(!checkIfDownloadIsPossible()) return;
                startHTTrackDownload();
            }
            if(e.getSource() == buttonStartDownloadCycle){
                if(!checkIfDownloadIsPossible()) return;
                if(!checkIfCycleIsPossible()) return;
                int nextCycleInHours = (int)spinnerDownloadInHours.getValue();
                int nextCycleInMinutes = (int)spinnerDownloadInMinutes.getValue();
                int nextCylceCombinedInMinutes = (nextCycleInHours * 60) + nextCycleInMinutes;
                // if(threadDownloadCycle != null) threadDownloadCycle.cancel(false);
                threadDownloadCycle = executorService.scheduleAtFixedRate(runnableDownloadCycle, 0, nextCylceCombinedInMinutes, TimeUnit.MINUTES);
                spinnerDownloadInHours.setEnabled(false);
                spinnerDownloadInMinutes.setEnabled(false);
                buttonStartDownloadCycle.setEnabled(false);
                buttonStopDownloadCycle.setEnabled(true);
            }
            if(e.getSource() == buttonStopDownloadCycle){
                if(threadDownloadCycle != null) threadDownloadCycle.cancel(false);
                labelNextDownloadAt.setText("");
                spinnerDownloadInHours.setEnabled(true);
                spinnerDownloadInMinutes.setEnabled(true);
                buttonStartDownloadCycle.setEnabled(true);
                buttonStopDownloadCycle.setEnabled(false);
            }
        }
        private void Show_Output(Process process) throws IOException {
            BufferedReader output_reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = "";
            while ((output = output_reader.readLine()) != null) {
                System.out.println(output);
            }
        }
        private String getNextCycleDateTime(int inHours, int inMinutes){
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            Calendar cal = Calendar.getInstance(); 
            cal.setTime(new Date());
            cal.add(Calendar.HOUR_OF_DAY, inHours);    
            cal.add(Calendar.MINUTE, inMinutes);  
            return dateTimeFormat.format(cal.getTime());
        }
        private void startHTTrackDownload(){
            try{
                String fileTypes = (radioButtonOnlyHTMLFiles.isSelected() ? "-p1" : "-p3");
                String command = "\"" + textFieldHTTrackExe.getText() + "\" -%L " +
                    "\"" + textFieldWebsiteFile.getText() + "\" -O " + 
                    "\"" + textFieldHTTrackOutputFolder.getText() + "\" " + 
                    "-w -c8 -f0 -s0 " + fileTypes + " -A100000000 -q -%v";
                File tempFileDownloadRunning = new File(".\\DownloadIsRunning"); 
                tempFileDownloadRunning.createNewFile();
                Process runtime = Runtime.getRuntime().exec(command);
                Show_Output(runtime);
                tempFileDownloadRunning.delete();
            } catch (Exception ex) { System.out.println(ex.getMessage()); } 
        }
    }
    
    // --------------------------------------------------
    // Datenbank-Operationen
    // --------------------------------------------------
    private Connection conn = null;
    private void connectToDB() {  
        if(conn != null) return;
        try {  
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:config.db");    
        } catch (Exception e) {  System.out.println(e.getMessage()); }    
    }

    private void disconnectFromDB() {  
        try { if (conn != null) conn.close(); }
        catch (SQLException e) { System.out.println(e.getMessage()); }
    }  

    private void LoadAllSettings(){
        try {  
            Statement stmt = conn.createStatement(); 
            ResultSet rs = stmt.executeQuery("SELECT * FROM settings");  
            String key; String value;
            while(rs.next()){
                key = rs.getString("key");
                value = rs.getString("value");
                if(key.equals("HTTrackEXE")) {
                    textFieldHTTrackExe.setText(value);
                    chooseHTTrackExe.setSelectedFile(new File(value));
                }
                if(key.equals("FileWithWebsites")) {
                    textFieldWebsiteFile.setText(value);
                    chooseWebsiteFile.setSelectedFile(new File(value));
                }
                if(key.equals("HTTrackOutputFolder")) {
                    textFieldHTTrackOutputFolder.setText(value);
                    chooseHTTrackOutputFolder.setSelectedFile(new File(value));
                }
            }
        } catch (SQLException e) {  System.out.println(e.getMessage()); }  
    }

    private void SaveAllSettings(){

    }

}