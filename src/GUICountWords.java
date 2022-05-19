import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
import javax.swing.JTabbedPane;
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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;

public class GUICountWords extends JFrame {
    // --------------------------------------------------
    // GUI Start
    // --------------------------------------------------
    // --------------------------------------------------
    // UI-Elemente
    // --------------------------------------------------
    private JTabbedPane countWordsRegisters;
    private JPanel mainPanel;
    private JPanel searchPanel;
    private JPanel panelDownloadWebsites;
    private JPanel panelEvaluation;
    private JPanel panelChooseWebsiteFile;
    private JPanel panelHTTrackExe;
    private JPanel panelHTTrackOutputFolder;
    private JPanel panelHTTrackOptions;
    private JPanel panelDownloadWebsitesButtons;
    private JPanel panelEvaluationWebsites;
    private JPanel panelChooseTermsFile;
    private JPanel panelEvaluationOutputFolder;
    private JPanel panelEvaluationButtons;
    private JPanel panelFilterCriteria;
    private JPanel panelSearchResults;
    private TitledBorder titledBorderDW;
    private TitledBorder titledBorderEval;
    private TitledBorder titledBorderFilterCriteria;
    private TitledBorder titledBorderSearchResult;
    private JLabel labelChooseWebsiteFile;
    private JLabel labelHTTrackExe;
    private JLabel labelHTTrackOutputFolder;
    private JLabel labelStartDownloadCycleIn;
    private JLabel labelDownloadHours;
    private JLabel labelDownloadMinutes; 
    private JLabel labelNextDownloadAt;
    private JLabel labelEvaluationWebsites; 
    private JLabel labelChooseTermsFile;
    private JLabel labelEvaluationOutputFolder;
    private JLabel labelEvaluationHours;
    private JLabel labelEvaluationMinutes; 
    private JLabel labelStartEvaluationCycleIn;
    private JLabel labelNextEvaluationAt;
    private JTextField textFieldWebsiteFile;
    private JTextField textFieldHTTrackExe;
    private JTextField textFieldHTTrackOutputFolder;
    private JTextField textFieldEvaluationWebsites;
    private JTextField textFieldTermsFile;
    private JTextField textFieldEvaluationOutputFolder;
    private JButton buttonChooseWebsiteFile;
    private JButton buttonChooseHTTrackExe;
    private JButton buttonChooseHTTrackOutputFolder;
    private JButton buttonStartHTTrack;
    private JButton buttonStartDownloadCycle;
    private JButton buttonStopDownloadCycle;
    private JButton buttonEvaluationWebsites;
    private JButton buttonChooseTermsFile;
    private JButton buttonEvaluationOutputFolder;
    private JButton buttonStartEvaluation;
    private JButton buttonStartEvaluationCycle;
    private JButton buttonStopEvaluationCycle;
    private JRadioButton radioButtonOnlyHTMLFiles;
    private JRadioButton radioButtonAllFiles;
    private ButtonGroup buttonGroupTypeOfFiles;
    private JSpinner spinnerDownloadInHours;
    private JSpinner spinnerDownloadInMinutes;
    private JSpinner spinnerEvaluateInHours;
    private JSpinner spinnerEvaluateInMinutes;
    private JFileChooser chooseHTTrackExe; 
    private JFileChooser chooseWebsiteFile;
    private JFileChooser chooseHTTrackOutputFolder;
    private JFileChooser chooseEvaluationWebsites; 
    private JFileChooser chooseTermsFile;
    private JFileChooser chooseEvaluationOutputFolder;
    
    public static void main(String[] args) throws Exception {
        new GUICountWords();
    }

    public GUICountWords(){
        setTitle("Word Counter");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024,512);
        
        initComponentMainPanelAndOthers();
        addWindowEvents();

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void initComponentMainPanelAndOthers(){
        countWordsRegisters = new JTabbedPane();
        mainPanel = new JPanel();
        searchPanel = new JPanel();
        panelDownloadWebsites = new JPanel();
        panelEvaluation = new JPanel();
        panelFilterCriteria = new JPanel();
        panelSearchResults = new JPanel();
        titledBorderDW = new TitledBorder("Website-Download (mittels HTTrack)");
        titledBorderEval = new TitledBorder("Auswertungen");
        titledBorderFilterCriteria = new TitledBorder("Filterkriterien");
        titledBorderSearchResult = new TitledBorder("Suchergebnis");
        
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        panelDownloadWebsites.setBorder(titledBorderDW);
        panelEvaluation.setBorder(titledBorderEval);
        panelFilterCriteria.setBorder(titledBorderFilterCriteria);
        panelSearchResults.setBorder(titledBorderSearchResult);

        initComponentHTTrackExe(panelDownloadWebsites);
        initComponentChooseWebSiteFile(panelDownloadWebsites);
        initComponentChooseHTTrackOutputFolder(panelDownloadWebsites);
        initComponentHTTrackOptions(panelDownloadWebsites);
        initComponentStartWebsiteDownload(panelDownloadWebsites);

        initComponentEvaluationWebsites(panelEvaluation);
        initComponentChooseTermsFile(panelEvaluation);
        initComponentChooseEvaluationOutputFolder(panelEvaluation);
        initComponentStartEvaluation(panelEvaluation);

        mainPanel.add(panelDownloadWebsites);
        panelEvaluation.setPreferredSize(new DimensionUIResource(panelEvaluation.getWidth(), 40));
        mainPanel.add(panelEvaluation);
        searchPanel.add(panelFilterCriteria);
        panelSearchResults.setPreferredSize(new DimensionUIResource(panelSearchResults.getWidth(), 200));
        searchPanel.add(panelSearchResults);

        countWordsRegisters.addTab("Programmsteuerung",mainPanel);
        countWordsRegisters.addTab("Suche",searchPanel);
        add(countWordsRegisters);
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
        labelStartDownloadCycleIn = new JLabel("Download alle: ");
        spinnerDownloadInHours = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        labelDownloadHours = new JLabel("Stunden");
        spinnerDownloadInMinutes = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        labelDownloadMinutes = new JLabel("Minuten");
        buttonStartDownloadCycle = new JButton("Zyklus starten");
        buttonStopDownloadCycle = new JButton("Zyklus stoppen");
        labelNextDownloadAt = new JLabel();
        
        panelDownloadWebsitesButtons.setLayout(new FlowLayout());

        buttonStartHTTrack.addActionListener(new MyActionListener());
        panelDownloadWebsitesButtons.add(buttonStartHTTrack);
        panelDownloadWebsitesButtons.add(labelStartDownloadCycleIn);
        panelDownloadWebsitesButtons.add(spinnerDownloadInHours);
        panelDownloadWebsitesButtons.add(labelDownloadHours);
        panelDownloadWebsitesButtons.add(spinnerDownloadInMinutes);
        panelDownloadWebsitesButtons.add(labelDownloadMinutes);
        buttonStartDownloadCycle.addActionListener(new MyActionListener());
        panelDownloadWebsitesButtons.add(buttonStartDownloadCycle);
        buttonStopDownloadCycle.addActionListener(new MyActionListener());
        buttonStopDownloadCycle.setEnabled(false);
        panelDownloadWebsitesButtons.add(buttonStopDownloadCycle);
        panelDownloadWebsitesButtons.add(labelNextDownloadAt);
        
        addToPanel.add(panelDownloadWebsitesButtons);
    }

    // --------------------------------------------------
    // Auszuwertende Webseiten(-Ordner) festlegen
    // --------------------------------------------------
    private void initComponentEvaluationWebsites(JPanel addToPanel){
        panelEvaluationWebsites = new JPanel();
        labelEvaluationWebsites = new JLabel("Datei mit Webseitordner:", SwingConstants.RIGHT);
        textFieldEvaluationWebsites = new JTextField(65);
        buttonEvaluationWebsites = new JButton("Datei selektieren...");
        chooseEvaluationWebsites = new JFileChooser();
        
        panelEvaluationWebsites.setLayout(new FlowLayout());
        labelEvaluationWebsites.setPreferredSize(new DimensionUIResource(150, 30));
        panelEvaluationWebsites.add(labelEvaluationWebsites);
        textFieldEvaluationWebsites.setEditable(false);
        panelEvaluationWebsites.add(textFieldEvaluationWebsites);
        buttonEvaluationWebsites.addActionListener(new MyActionListener());
        buttonEvaluationWebsites.setPreferredSize(new DimensionUIResource(150, 25));
        panelEvaluationWebsites.add(buttonEvaluationWebsites);

        addToPanel.add(panelEvaluationWebsites);
    }

    // --------------------------------------------------
    // Datei mit auszuwertenden Begriffen auswählen
    // --------------------------------------------------
    private void initComponentChooseTermsFile(JPanel addToPanel){
        panelChooseTermsFile = new JPanel();
        labelChooseTermsFile = new JLabel("Datei mit Begriffen:", SwingConstants.RIGHT);
        textFieldTermsFile = new JTextField(65);
        buttonChooseTermsFile = new JButton("Datei selektieren...");
        chooseTermsFile = new JFileChooser();

        panelChooseTermsFile.setLayout(new FlowLayout());
        labelChooseTermsFile.setPreferredSize(new DimensionUIResource(150, 30));
        panelChooseTermsFile.add(labelChooseTermsFile);
        textFieldTermsFile.setEditable(false);
        panelChooseTermsFile.add(textFieldTermsFile);
        buttonChooseTermsFile.addActionListener(new MyActionListener());
        buttonChooseTermsFile.setPreferredSize(new DimensionUIResource(150, 25));
        panelChooseTermsFile.add(buttonChooseTermsFile);

        addToPanel.add(panelChooseTermsFile);
    }

    // --------------------------------------------------
    // Auswertungs Output Folder festlegen
    // --------------------------------------------------
    private void initComponentChooseEvaluationOutputFolder(JPanel addToPanel){
        panelEvaluationOutputFolder = new JPanel();
        labelEvaluationOutputFolder = new JLabel("Ausgabeordner:", SwingConstants.RIGHT);
        textFieldEvaluationOutputFolder = new JTextField(65);
        buttonEvaluationOutputFolder = new JButton("Ordner selektieren...");
        chooseEvaluationOutputFolder = new JFileChooser();
        chooseEvaluationOutputFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        panelEvaluationOutputFolder.setLayout(new FlowLayout());
        labelEvaluationOutputFolder.setPreferredSize(new DimensionUIResource(150, 30));
        panelEvaluationOutputFolder.add(labelEvaluationOutputFolder);
        textFieldEvaluationOutputFolder.setEditable(false);
        panelEvaluationOutputFolder.add(textFieldEvaluationOutputFolder);
        buttonEvaluationOutputFolder.addActionListener(new MyActionListener());
        buttonEvaluationOutputFolder.setPreferredSize(new DimensionUIResource(150, 25));
        panelEvaluationOutputFolder.add(buttonEvaluationOutputFolder);

        addToPanel.add(panelEvaluationOutputFolder);
    }

    // --------------------------------------------------
    // Buttons für die Auswertung initialisieren
    // --------------------------------------------------
    private void initComponentStartEvaluation(JPanel addToPanel){
        panelEvaluationButtons = new JPanel();
        buttonStartEvaluation = new JButton("Auswertung jetzt starten");
        labelStartEvaluationCycleIn = new JLabel("Auswertung alle: ");
        spinnerEvaluateInHours = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        labelEvaluationHours = new JLabel("Stunden");
        spinnerEvaluateInMinutes = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        labelEvaluationMinutes = new JLabel("Minuten");
        buttonStartEvaluationCycle = new JButton("Zyklus starten");
        buttonStopEvaluationCycle = new JButton("Zyklus stoppen");
        labelNextEvaluationAt = new JLabel();
        
        panelEvaluationButtons.setLayout(new FlowLayout());

        buttonStartEvaluation.addActionListener(new MyActionListener());
        panelEvaluationButtons.add(buttonStartEvaluation);
        panelEvaluationButtons.add(labelStartEvaluationCycleIn);
        panelEvaluationButtons.add(spinnerEvaluateInHours);
        panelEvaluationButtons.add(labelEvaluationHours);
        panelEvaluationButtons.add(spinnerEvaluateInMinutes);
        panelEvaluationButtons.add(labelEvaluationMinutes);
        buttonStartEvaluationCycle.addActionListener(new MyActionListener());
        panelEvaluationButtons.add(buttonStartEvaluationCycle);
        buttonStopEvaluationCycle.addActionListener(new MyActionListener());
        buttonStopEvaluationCycle.setEnabled(false);
        panelEvaluationButtons.add(buttonStopEvaluationCycle);
        panelEvaluationButtons.add(labelNextEvaluationAt);
        
        addToPanel.add(panelEvaluationButtons);
    }

    // --------------------------------------------------
    // GUI Ende
    // --------------------------------------------------
    // --------------------------------------------------
    // Event Handling Start
    // --------------------------------------------------

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
                JOptionPane.showMessageDialog(GUICountWords.this, "Kein HTTrack Ausgabeordner angegeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true; 
        }
        private boolean checkIfDownloadCycleIsPossible(){
            if(((int)spinnerDownloadInHours.getValue() < 1) && ((int)spinnerDownloadInMinutes.getValue() < 1)){
                JOptionPane.showMessageDialog(GUICountWords.this, "Zykluswert muss größer 0 sein", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true; 
        }
        private boolean checkIfEvaluationIsPossible(){
            if(textFieldHTTrackOutputFolder.getText().isEmpty()){
                JOptionPane.showMessageDialog(GUICountWords.this, "Kein HTTrack Ausgabeordner angegeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(textFieldEvaluationWebsites.getText().isEmpty()){
                JOptionPane.showMessageDialog(GUICountWords.this, "Kein Webseiten-Ordner-Datei angegeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(textFieldTermsFile.getText().isEmpty()){
                JOptionPane.showMessageDialog(GUICountWords.this, "Kein Suchbegriff-Datei angegeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(textFieldEvaluationOutputFolder.getText().isEmpty()){
                JOptionPane.showMessageDialog(GUICountWords.this, "Kein Auswertungs-Ausgabeordner angegeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }
        private boolean checkIfEvaluationCycleIsPossible(){
            if(((int)spinnerEvaluateInHours.getValue() < 1) && ((int)spinnerEvaluateInMinutes.getValue() < 1)){
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
        private Runnable runnableEvaluationCycle = new Runnable() {
            public void run() {
                File tempFileDownloadRunning = new File(".\\DownloadIsRunning"); 
                File tempFileEvaluationRunning = new File(".\\EvaluationIsRunning"); 
                if(tempFileDownloadRunning.exists()){
                    System.out.println("Es läuft gerade ein Download, warten auf den nächsten Zyklus");
                }
                if(tempFileEvaluationRunning.exists()){
                    System.out.println("Auswertung läuft bereits, warten auf den nächsten Zyklus");
                }
                if((!tempFileDownloadRunning.exists()) && (!tempFileEvaluationRunning.exists())){
                    startEvaluation();
                }
                String nextEvaluationDateTime = getNextCycleDateTime(
                    (int)spinnerEvaluateInHours.getValue(), 
                    (int)spinnerEvaluateInMinutes.getValue()
                );
                labelNextEvaluationAt.setText("Auswertung startet um: " + nextEvaluationDateTime);
            }
        };
        private ScheduledFuture<?> threadDownloadCycle;
        private ScheduledFuture<?> threadEvaluationCycle;
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
                if(!checkIfDownloadCycleIsPossible()) return;
                int nextCycleInHours = (int)spinnerDownloadInHours.getValue();
                int nextCycleInMinutes = (int)spinnerDownloadInMinutes.getValue();
                int nextCylceCombinedInMinutes = (nextCycleInHours * 60) + nextCycleInMinutes;
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
            if(e.getSource() == buttonEvaluationWebsites){
                int result = chooseEvaluationWebsites.showOpenDialog(GUICountWords.this);
                if(result == JFileChooser.APPROVE_OPTION){
                    textFieldEvaluationWebsites.setText(chooseEvaluationWebsites.getSelectedFile().toString());
                }
            }
            if(e.getSource() == buttonChooseTermsFile){
                int result = chooseTermsFile.showOpenDialog(GUICountWords.this);
                if(result == JFileChooser.APPROVE_OPTION){
                    textFieldTermsFile.setText(chooseTermsFile.getSelectedFile().toString());
                }
            }
            if(e.getSource() == buttonEvaluationOutputFolder){
                int result = chooseEvaluationOutputFolder.showOpenDialog(GUICountWords.this);
                if(result == JFileChooser.APPROVE_OPTION){
                    textFieldEvaluationOutputFolder.setText(chooseEvaluationOutputFolder.getSelectedFile().toString());
                }
            }
            if(e.getSource() == buttonStartEvaluation){
                if(!checkIfEvaluationIsPossible()) return;
                startEvaluation();
            }
            if(e.getSource() == buttonStartEvaluationCycle){
                if(!checkIfEvaluationIsPossible()) return;
                if(!checkIfEvaluationCycleIsPossible()) return;
                int nextEvaluationInHours = (int)spinnerEvaluateInHours.getValue();
                int nextEvaluationInMinutes = (int)spinnerEvaluateInMinutes.getValue();
                int nextEvaluationCombinedInMinutes = (nextEvaluationInHours * 60) + nextEvaluationInMinutes;
                threadEvaluationCycle = executorService.scheduleAtFixedRate(runnableEvaluationCycle, 0, nextEvaluationCombinedInMinutes, TimeUnit.MINUTES);
                spinnerEvaluateInHours.setEnabled(false);
                spinnerEvaluateInMinutes.setEnabled(false);
                buttonStartEvaluationCycle.setEnabled(false);
                buttonStopEvaluationCycle.setEnabled(true);
            }
            if(e.getSource() == buttonStopEvaluationCycle){
                if(threadEvaluationCycle != null) threadEvaluationCycle.cancel(false);
                labelNextEvaluationAt.setText("");
                spinnerEvaluateInHours.setEnabled(true);
                spinnerEvaluateInMinutes.setEnabled(true);
                buttonStartEvaluationCycle.setEnabled(true);
                buttonStopEvaluationCycle.setEnabled(false);
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
            File tempFileDownloadRunning = new File(".\\DownloadIsRunning"); 
            try{
                String fileTypes = (radioButtonOnlyHTMLFiles.isSelected() ? "-p1" : "-p3");
                String command = "\"" + textFieldHTTrackExe.getText() + "\" -%L " +
                    "\"" + textFieldWebsiteFile.getText() + "\" -O " + 
                    "\"" + textFieldHTTrackOutputFolder.getText() + "\" " + 
                    "-w -c8 -f0 -s0 " + fileTypes + " -A100000000 -q -%v";
                tempFileDownloadRunning.createNewFile();
                Process runtime = Runtime.getRuntime().exec(command);
                Show_Output(runtime);
            } catch (Exception ex) { 
                System.out.println(ex.getMessage()); 
            } finally { 
                if(tempFileDownloadRunning.exists()) tempFileDownloadRunning.delete();
            } 
        }
        private void writeSettingsIntoConfigTextfile(){
            try {
                FileWriter myWriter = new FileWriter("config.txt");
                myWriter.write("AuszuwertenderOrdner=" + textFieldHTTrackOutputFolder.getText() + "\r\n");
                myWriter.write("DateiMitWebseitordnern=" + textFieldEvaluationWebsites.getText() + "\r\n");
                myWriter.write("DateiMitBegriffen=" + textFieldTermsFile.getText() + "\r\n");
                myWriter.write("AuswertungsOrdner=" + textFieldEvaluationOutputFolder.getText() + "\r\n"); 
                myWriter.write("InDatenbankSchreiben=Ja" + "\r\n");
                myWriter.write("NullWerteInErgebnisMitaufnehmen=Ja" + "\r\n");
                myWriter.close();
            } catch (IOException e) { e.printStackTrace(); }
        }
        private void startEvaluation(){
            File tempFileEvaluationRunning = new File(".\\EvaluationIsRunning");
            try{
                writeSettingsIntoConfigTextfile();
                String command = "\"" + System.getenv("JAVA_HOME") + "\\bin\\java.exe\" -cp \".\\lib\\*\" \".\\src\\CountWords.java\"";
                tempFileEvaluationRunning.createNewFile();
                Process runtime = Runtime.getRuntime().exec(command);
                Show_Output(runtime);
            } catch (Exception ex) { 
                System.out.println(ex.getMessage()); 
            } finally { 
                if(tempFileEvaluationRunning.exists()) tempFileEvaluationRunning.delete();
            }
        }
    }
    
    // --------------------------------------------------
    // Event Handling Ende
    // --------------------------------------------------
    // --------------------------------------------------
    // Datenbank-Operationen Start
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
                if(key.equals("DownloadOnlyHTMLFiles")){
                    if(value.toLowerCase().equals("yes")){
                        radioButtonOnlyHTMLFiles.setSelected(true);
                    } else {
                        radioButtonAllFiles.setSelected(true);
                    }
                }
                if(key.equals("DownloadCycleHours")){
                    spinnerDownloadInHours.setValue(Integer.parseInt(value));
                }
                if(key.equals("DownloadCycleMinutes")){
                    spinnerDownloadInMinutes.setValue(Integer.parseInt(value));
                }
                if(key.equals("FileWithWebsiteFolders")) {
                    textFieldEvaluationWebsites.setText(value);
                    chooseEvaluationWebsites.setSelectedFile(new File(value));
                }
                if(key.equals("FileWithTerms")) {
                    textFieldTermsFile.setText(value);
                    chooseTermsFile.setSelectedFile(new File(value));
                }
                if(key.equals("EvaluationOutputFolder")) {
                    textFieldEvaluationOutputFolder.setText(value);
                    chooseEvaluationOutputFolder.setSelectedFile(new File(value));
                }
                if(key.equals("EvaluationCycleHours")){
                    spinnerEvaluateInHours.setValue(Integer.parseInt(value));
                }
                if(key.equals("EvaluationCycleMinutes")){
                    spinnerEvaluateInMinutes.setValue(Integer.parseInt(value));
                }
            }
        } catch (SQLException e) {  System.out.println(e.getMessage()); }  
    }

    private void SaveAllSettings(){
        try {
            PreparedStatement pstmt; 
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "HTTrackEXE");
            pstmt.setString(1, textFieldHTTrackExe.getText());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "FileWithWebsites");
            pstmt.setString(1, textFieldWebsiteFile.getText());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "HTTrackOutputFolder");
            pstmt.setString(1, textFieldHTTrackOutputFolder.getText());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "DownloadOnlyHTMLFiles");
            pstmt.setString(1, radioButtonOnlyHTMLFiles.isSelected() ? "yes" : "no");
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "DownloadCycleHours");
            pstmt.setString(1, spinnerDownloadInHours.getValue().toString());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "DownloadCycleMinutes");
            pstmt.setString(1, spinnerDownloadInMinutes.getValue().toString());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "FileWithWebsiteFolders");
            pstmt.setString(1, textFieldEvaluationWebsites.getText());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "FileWithTerms");
            pstmt.setString(1, textFieldTermsFile.getText());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "EvaluationOutputFolder");
            pstmt.setString(1, textFieldEvaluationOutputFolder.getText());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "EvaluationCycleHours");
            pstmt.setString(1, spinnerEvaluateInHours.getValue().toString());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "EvaluationCycleMinutes");
            pstmt.setString(1, spinnerEvaluateInMinutes.getValue().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {  System.out.println(e.getMessage()); }
    }

    // --------------------------------------------------
    // Datenbank-Operationen Ende
    // --------------------------------------------------

}