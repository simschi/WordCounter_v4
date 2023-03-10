import org.apache.commons.io.FileUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.JSpinner.DateEditor;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.Desktop;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class GUICountWords extends JFrame {
    // --------------------------------------------------
    // GUI Start
    // --------------------------------------------------
    // --------------------------------------------------
    // UI-Elemente
    // --------------------------------------------------
    private JTabbedPane countWordsRegisters;
    private JScrollPane scrollPaneTableResults;
    private JPanel mainPanel;
    private JPanel searchPanel;
    private JPanel panelDownloadWebsites;
    private JPanel panelEvaluation;
    private JPanel panelChooseWebsiteFile;
    private JPanel panelHTTrackExe;
    private JPanel panelHTTrackOutputFolder;
    private JPanel panelHTTrackOptions;
    private JPanel panelDownloadWebsitesButtons;
    private JPanel panelDownloadProgressBar;
    private JPanel panelEvaluationWebsites;
    private JPanel panelChooseTermsFile;
    private JPanel panelEvaluationOutputFolder;
    private JPanel panelEvaluationButtons;
    private JPanel panelFilterCriteria;
    private JPanel panelFilterSearchTerms;
    private JPanel panelSearchResults;
    private JPanel panelSearchResultsTable;
    private JPanel panelEvaluationOptions;
    private TitledBorder titledBorderDW;
    private TitledBorder titledBorderEval;
    private TitledBorder titledBorderFilterCriteria;
    private TitledBorder titledBorderSearchResult;
    private JLabel labelChooseWebsiteFile;
    private JLabel labelHTTrackExe;
    private JLabel labelHTTrackOutputFolder;
    private JLabel labelStartDownloadCycleIn;
    private JLabel labelDownloadDays;
    private JLabel labelDownloadHours;
    private JLabel labelNextDownloadAt;
    private JLabel labelEvaluationWebsites;
    private JLabel labelChooseTermsFile;
    private JLabel labelEvaluationOutputFolder;
    private JLabel labelEvaluationDays;
    private JLabel labelEvaluationHours;
    private JLabel labelStartEvaluationCycleIn;
    private JLabel labelNextEvaluationAt;
    private JLabel labelTrendAnalysisName;
    private JLabel labelFilterDateFrom;
    private JLabel labelFilterDateTo;
    private JLabel labelFilterWebsites;
    private JLabel labelFilterSearchTerm;
    private JTextField textFieldWebsiteFile;
    private JTextField textFieldHTTrackExe;
    private JTextField textFieldHTTrackOutputFolder;
    private JTextField textFieldEvaluationWebsites;
    private JTextField textFieldTermsFile;
    private JTextField textFieldEvaluationOutputFolder;
    private JTextField textFieldTrendAnalysisName;
    private JTextField textFieldFilterWebsites;
    private JTextField textFieldFilterSearchTerm;
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
    private JButton buttonOpenWebsiteFile;
    private JButton buttonOpenWebsiteFolderFile;
    private JButton buttonOpenTermsFile;
    private JButton buttonSearch;
    private JRadioButton radioButtonOnlyHTMLFiles;
    private JRadioButton radioButtonIncludeImages;
    private JRadioButton radioButtonIncludeImagesAndVideos;
    private JRadioButton radioButtonAllFiles;
    private ButtonGroup buttonGroupTypeOfFiles;
    private JCheckBox checkBoxZeroValuesInExcel;
    private JCheckBox checkBoxTrendAnalysis;
    private JSpinner spinnerDownloadInDays;
    private JSpinner spinnerDownloadInHours;
    private JSpinner spinnerEvaluateInDays;
    private JSpinner spinnerEvaluateInHours;
    private JSpinner spinnerFilterDateFrom;
    private JSpinner spinnerFilterDateTo;
    private DefaultTableModel modelSearchResults;
    private JTable tableSearchResults;
    private JFileChooser chooseHTTrackExe;
    private JFileChooser chooseWebsiteFile;
    private JFileChooser chooseHTTrackOutputFolder;
    private JFileChooser chooseEvaluationWebsites;
    private JFileChooser chooseTermsFile;
    private JFileChooser chooseEvaluationOutputFolder;

    private JProgressBar downloadProgressBar;

    public static void main(String[] args) throws Exception {
        new GUICountWords();
    }

    public GUICountWords() {
        setTitle("Word Counter");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 576);

        initComponentMainPanelAndOthers();
        addWindowEvents();

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void initComponentMainPanelAndOthers() {
        downloadProgressBar = new JProgressBar();
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
        initComponentDownloadProgressBar(panelDownloadWebsites);

        initComponentEvaluationWebsites(panelEvaluation);
        initComponentChooseTermsFile(panelEvaluation);
        initComponentChooseEvaluationOutputFolder(panelEvaluation);
        initComponentEvaluationOptions(panelEvaluation);
        initComponentStartEvaluation(panelEvaluation);

        initComponentFilterCriteria(panelFilterCriteria);
        initComponentSearchResults(panelSearchResults);

        mainPanel.add(panelDownloadWebsites);
        panelEvaluation.setPreferredSize(new DimensionUIResource(panelEvaluation.getWidth(), 65));
        mainPanel.add(panelEvaluation);
        searchPanel.add(panelFilterCriteria);
        panelSearchResults.setPreferredSize(new DimensionUIResource(panelSearchResults.getWidth(), 450));
        searchPanel.add(panelSearchResults);

        countWordsRegisters.addTab("Programmsteuerung", mainPanel);
        countWordsRegisters.addTab("Suche", searchPanel);
        add(countWordsRegisters);
    }

    // --------------------------------------------------
    // Pfad zur HTTrack-Exe festlegen
    // --------------------------------------------------
    private void initComponentHTTrackExe(JPanel addToPanel) {
        panelHTTrackExe = new JPanel();
        labelHTTrackExe = new JLabel("HTTrack-exe-Datei:", SwingConstants.RIGHT);
        textFieldHTTrackExe = new JTextField(56);
        buttonChooseHTTrackExe = new JButton("Datei selektieren...");
        chooseHTTrackExe = new JFileChooser();

        panelHTTrackExe.setLayout(new FlowLayout());
        labelHTTrackExe.setPreferredSize(new DimensionUIResource(160, 30));
        panelHTTrackExe.add(labelHTTrackExe);
        textFieldHTTrackExe.setEditable(false);
        panelHTTrackExe.add(textFieldHTTrackExe);
        buttonChooseHTTrackExe.addActionListener(new MyActionListener());
        buttonChooseHTTrackExe.setPreferredSize(new DimensionUIResource(150, 25));
        panelHTTrackExe.add(buttonChooseHTTrackExe);

        addToPanel.add(panelHTTrackExe);
    }

    // --------------------------------------------------
    // Komponente Webseiten-Datei ausw??hlen
    // --------------------------------------------------
    private void initComponentChooseWebSiteFile(JPanel addToPanel) {
        panelChooseWebsiteFile = new JPanel();
        labelChooseWebsiteFile = new JLabel("Datei mit Webseiten:", SwingConstants.RIGHT);
        textFieldWebsiteFile = new JTextField(40);
        buttonChooseWebsiteFile = new JButton("Datei selektieren...");
        chooseWebsiteFile = new JFileChooser();
        buttonOpenWebsiteFile = new JButton("Datei bearbeiten...");

        panelChooseWebsiteFile.setLayout(new FlowLayout());
        labelChooseWebsiteFile.setPreferredSize(new DimensionUIResource(160, 30));
        panelChooseWebsiteFile.add(labelChooseWebsiteFile);
        textFieldWebsiteFile.setEditable(false);
        panelChooseWebsiteFile.add(textFieldWebsiteFile);
        buttonChooseWebsiteFile.addActionListener(new MyActionListener());
        buttonChooseWebsiteFile.setPreferredSize(new DimensionUIResource(150, 25));
        panelChooseWebsiteFile.add(buttonChooseWebsiteFile);
        buttonOpenWebsiteFile.addActionListener(new MyActionListener());
        buttonOpenWebsiteFile.setPreferredSize(new DimensionUIResource(150, 25));
        panelChooseWebsiteFile.add(buttonOpenWebsiteFile);

        addToPanel.add(panelChooseWebsiteFile);
    }

    // --------------------------------------------------
    // HTTrack Output Folder festlegen
    // --------------------------------------------------
    private void initComponentChooseHTTrackOutputFolder(JPanel addToPanel) {
        panelHTTrackOutputFolder = new JPanel();
        labelHTTrackOutputFolder = new JLabel("HTTrack Ausgabeordner:", SwingConstants.RIGHT);
        textFieldHTTrackOutputFolder = new JTextField(56);
        buttonChooseHTTrackOutputFolder = new JButton("Ordner selektieren...");
        chooseHTTrackOutputFolder = new JFileChooser();
        chooseHTTrackOutputFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        panelHTTrackOutputFolder.setLayout(new FlowLayout());
        labelHTTrackOutputFolder.setPreferredSize(new DimensionUIResource(160, 30));
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
    private void initComponentHTTrackOptions(JPanel addToPanel) {
        panelHTTrackOptions = new JPanel();
        radioButtonOnlyHTMLFiles = new JRadioButton("Nur HTML-Dateien");
        radioButtonOnlyHTMLFiles.setSelected(true);
        radioButtonIncludeImages = new JRadioButton("HTML-Dateien mit Bildern");
        radioButtonIncludeImagesAndVideos = new JRadioButton("HTML-Dateien mit Bildern und Videos");
        radioButtonAllFiles = new JRadioButton("Alle Dateien");
        buttonGroupTypeOfFiles = new ButtonGroup();
        buttonGroupTypeOfFiles.add(radioButtonOnlyHTMLFiles);
        buttonGroupTypeOfFiles.add(radioButtonIncludeImages);
        buttonGroupTypeOfFiles.add(radioButtonIncludeImagesAndVideos);
        buttonGroupTypeOfFiles.add(radioButtonAllFiles);

        panelHTTrackOptions.setLayout(new FlowLayout());
        panelHTTrackOptions.setPreferredSize(new DimensionUIResource(800, 30));
        panelHTTrackOptions.add(radioButtonOnlyHTMLFiles);
        panelHTTrackOptions.add(radioButtonIncludeImages);
        panelHTTrackOptions.add(radioButtonIncludeImagesAndVideos);
        panelHTTrackOptions.add(radioButtonAllFiles);

        addToPanel.add(panelHTTrackOptions);
    }

    // --------------------------------------------------
    // Buttons f??r den WebsiteDownload initialisieren
    // --------------------------------------------------
    private void initComponentStartWebsiteDownload(JPanel addToPanel) {
        panelDownloadWebsitesButtons = new JPanel();
        buttonStartHTTrack = new JButton("Download jetzt starten");
        labelStartDownloadCycleIn = new JLabel("Download alle: ");
        spinnerDownloadInDays = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        labelDownloadDays = new JLabel("Tage");
        spinnerDownloadInHours = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        labelDownloadHours = new JLabel("Stunden");
        buttonStartDownloadCycle = new JButton("Zyklus starten");
        buttonStopDownloadCycle = new JButton("Zyklus stoppen");
        labelNextDownloadAt = new JLabel();

        panelDownloadWebsitesButtons.setLayout(new FlowLayout());

        buttonStartHTTrack.addActionListener(new MyActionListener());
        panelDownloadWebsitesButtons.add(buttonStartHTTrack);
        panelDownloadWebsitesButtons.add(labelStartDownloadCycleIn);
        panelDownloadWebsitesButtons.add(spinnerDownloadInDays);
        panelDownloadWebsitesButtons.add(labelDownloadDays);
        panelDownloadWebsitesButtons.add(spinnerDownloadInHours);
        panelDownloadWebsitesButtons.add(labelDownloadHours);
        buttonStartDownloadCycle.addActionListener(new MyActionListener());
        panelDownloadWebsitesButtons.add(buttonStartDownloadCycle);
        buttonStopDownloadCycle.addActionListener(new MyActionListener());
        buttonStopDownloadCycle.setEnabled(false);
        panelDownloadWebsitesButtons.add(buttonStopDownloadCycle);
        panelDownloadWebsitesButtons.add(labelNextDownloadAt);

        addToPanel.add(panelDownloadWebsitesButtons);
    }

    // ------------------------------------------------------
    // Fortschrittsbalken f??r  WebsiteDownload initialisieren
    // ------------------------------------------------------
    private void initComponentDownloadProgressBar(JPanel addToPanel) {
        panelDownloadProgressBar = new JPanel();

        downloadProgressBar.setVisible(false);
        downloadProgressBar.setValue(0);
        downloadProgressBar.setStringPainted(true);
        panelDownloadProgressBar.add(downloadProgressBar);

        addToPanel.add(panelDownloadProgressBar);
    }

    // --------------------------------------------------
    // Auszuwertende Webseiten(-Ordner) festlegen
    // --------------------------------------------------
    private void initComponentEvaluationWebsites(JPanel addToPanel) {
        panelEvaluationWebsites = new JPanel();
        labelEvaluationWebsites = new JLabel("Datei mit Webseitenordner:", SwingConstants.RIGHT);
        textFieldEvaluationWebsites = new JTextField(40);
        buttonEvaluationWebsites = new JButton("Datei selektieren...");
        chooseEvaluationWebsites = new JFileChooser();
        buttonOpenWebsiteFolderFile = new JButton("Datei bearbeiten...");

        panelEvaluationWebsites.setLayout(new FlowLayout());
        labelEvaluationWebsites.setPreferredSize(new DimensionUIResource(160, 30));
        panelEvaluationWebsites.add(labelEvaluationWebsites);
        textFieldEvaluationWebsites.setEditable(false);
        panelEvaluationWebsites.add(textFieldEvaluationWebsites);
        buttonEvaluationWebsites.addActionListener(new MyActionListener());
        buttonEvaluationWebsites.setPreferredSize(new DimensionUIResource(150, 25));
        panelEvaluationWebsites.add(buttonEvaluationWebsites);
        buttonOpenWebsiteFolderFile.addActionListener(new MyActionListener());
        buttonOpenWebsiteFolderFile.setPreferredSize(new DimensionUIResource(150, 25));
        panelEvaluationWebsites.add(buttonOpenWebsiteFolderFile);

        addToPanel.add(panelEvaluationWebsites);
    }

    // --------------------------------------------------
    // Datei mit auszuwertenden Begriffen ausw??hlen
    // --------------------------------------------------
    private void initComponentChooseTermsFile(JPanel addToPanel) {
        panelChooseTermsFile = new JPanel();
        labelChooseTermsFile = new JLabel("Datei mit Begriffen:", SwingConstants.RIGHT);
        textFieldTermsFile = new JTextField(40);
        buttonChooseTermsFile = new JButton("Datei selektieren...");
        chooseTermsFile = new JFileChooser();
        buttonOpenTermsFile = new JButton("Datei bearbeiten...");

        panelChooseTermsFile.setLayout(new FlowLayout());
        labelChooseTermsFile.setPreferredSize(new DimensionUIResource(160, 30));
        panelChooseTermsFile.add(labelChooseTermsFile);
        textFieldTermsFile.setEditable(false);
        panelChooseTermsFile.add(textFieldTermsFile);
        buttonChooseTermsFile.addActionListener(new MyActionListener());
        buttonChooseTermsFile.setPreferredSize(new DimensionUIResource(150, 25));
        panelChooseTermsFile.add(buttonChooseTermsFile);
        buttonOpenTermsFile.addActionListener(new MyActionListener());
        buttonOpenTermsFile.setPreferredSize(new DimensionUIResource(150, 25));
        panelChooseTermsFile.add(buttonOpenTermsFile);

        addToPanel.add(panelChooseTermsFile);
    }

    // --------------------------------------------------
    // Auswertungs Output Folder festlegen
    // --------------------------------------------------
    private void initComponentChooseEvaluationOutputFolder(JPanel addToPanel) {
        panelEvaluationOutputFolder = new JPanel();
        labelEvaluationOutputFolder = new JLabel("Ausgabeordner:", SwingConstants.RIGHT);
        textFieldEvaluationOutputFolder = new JTextField(56);
        buttonEvaluationOutputFolder = new JButton("Ordner selektieren...");
        chooseEvaluationOutputFolder = new JFileChooser();
        chooseEvaluationOutputFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        panelEvaluationOutputFolder.setLayout(new FlowLayout());
        labelEvaluationOutputFolder.setPreferredSize(new DimensionUIResource(160, 30));
        panelEvaluationOutputFolder.add(labelEvaluationOutputFolder);
        textFieldEvaluationOutputFolder.setEditable(false);
        panelEvaluationOutputFolder.add(textFieldEvaluationOutputFolder);
        buttonEvaluationOutputFolder.addActionListener(new MyActionListener());
        buttonEvaluationOutputFolder.setPreferredSize(new DimensionUIResource(150, 25));
        panelEvaluationOutputFolder.add(buttonEvaluationOutputFolder);

        addToPanel.add(panelEvaluationOutputFolder);
    }

    // --------------------------------------------------
    // Komponente f??r Auswertungsoptionen 
    // --------------------------------------------------
    private void initComponentEvaluationOptions(JPanel addToPanel) {
        panelEvaluationOptions = new JPanel();
        checkBoxZeroValuesInExcel = new JCheckBox("Nullwerte in Exceldatei ausgeben?");
        checkBoxTrendAnalysis = new JCheckBox("Daten in Trendanalyse importieren?");
        labelTrendAnalysisName = new JLabel("Trendanalyse-Name: ");
        textFieldTrendAnalysisName = new JTextField(20);

        panelEvaluationOptions.add(checkBoxZeroValuesInExcel);
        panelEvaluationOptions.add(checkBoxTrendAnalysis);
        panelEvaluationOptions.add(labelTrendAnalysisName);
        panelEvaluationOptions.add(textFieldTrendAnalysisName);

        addToPanel.add(panelEvaluationOptions);
    }

    // --------------------------------------------------
    // Buttons f??r die Auswertung initialisieren
    // --------------------------------------------------
    private void initComponentStartEvaluation(JPanel addToPanel) {
        panelEvaluationButtons = new JPanel();
        buttonStartEvaluation = new JButton("Auswertung jetzt starten");
        labelStartEvaluationCycleIn = new JLabel("Auswertung alle: ");
        spinnerEvaluateInDays = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        labelEvaluationDays = new JLabel("Tage");
        spinnerEvaluateInHours = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        labelEvaluationHours = new JLabel("Stunden");
        buttonStartEvaluationCycle = new JButton("Zyklus starten");
        buttonStopEvaluationCycle = new JButton("Zyklus stoppen");
        labelNextEvaluationAt = new JLabel();

        panelEvaluationButtons.setLayout(new FlowLayout());

        buttonStartEvaluation.addActionListener(new MyActionListener());
        panelEvaluationButtons.add(buttonStartEvaluation);
        panelEvaluationButtons.add(labelStartEvaluationCycleIn);
        panelEvaluationButtons.add(spinnerEvaluateInDays);
        panelEvaluationButtons.add(labelEvaluationDays);
        panelEvaluationButtons.add(spinnerEvaluateInHours);
        panelEvaluationButtons.add(labelEvaluationHours);
        buttonStartEvaluationCycle.addActionListener(new MyActionListener());
        panelEvaluationButtons.add(buttonStartEvaluationCycle);
        buttonStopEvaluationCycle.addActionListener(new MyActionListener());
        buttonStopEvaluationCycle.setEnabled(false);
        panelEvaluationButtons.add(buttonStopEvaluationCycle);
        panelEvaluationButtons.add(labelNextEvaluationAt);

        addToPanel.add(panelEvaluationButtons);
    }

    // --------------------------------------------------
    // Komponente f??r Filterkriterien ??ber Suche
    // --------------------------------------------------
    private void initComponentFilterCriteria(JPanel addToPanel) {
        panelFilterSearchTerms = new JPanel();
        labelFilterWebsites = new JLabel("Webseite: ");
        labelFilterSearchTerm = new JLabel("Wort: ");
        labelFilterDateFrom = new JLabel("Datum von: ");
        labelFilterDateTo = new JLabel("Datum bis: ");
        textFieldFilterWebsites = new JTextField(20);
        textFieldFilterSearchTerm = new JTextField(20);
        spinnerFilterDateFrom = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MONTH));
        DateEditor editorFilterDateFrom = new DateEditor(spinnerFilterDateFrom, "dd.MM.yyyy");
        spinnerFilterDateFrom.setEditor(editorFilterDateFrom);
        spinnerFilterDateTo = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MONTH));
        DateEditor editorFilterDateTo = new DateEditor(spinnerFilterDateTo, "dd.MM.yyyy");
        spinnerFilterDateTo.setEditor(editorFilterDateTo);
        buttonSearch = new JButton("Suchen");

        panelFilterSearchTerms.setLayout(new FlowLayout());
        panelFilterSearchTerms.add(labelFilterSearchTerm);
        panelFilterSearchTerms.add(textFieldFilterSearchTerm);
        panelFilterSearchTerms.add(labelFilterWebsites);
        panelFilterSearchTerms.add(textFieldFilterWebsites);
        panelFilterSearchTerms.add(labelFilterDateFrom);
        panelFilterSearchTerms.add(spinnerFilterDateFrom);
        panelFilterSearchTerms.add(labelFilterDateTo);
        panelFilterSearchTerms.add(spinnerFilterDateTo);
        buttonSearch.addActionListener(new MyActionListener());
        panelFilterSearchTerms.add(buttonSearch);

        addToPanel.add(panelFilterSearchTerms);
    }

    // --------------------------------------------------
    // Komponente f??r Suchergebnisse
    // --------------------------------------------------
    private void initComponentSearchResults(JPanel addToPanel) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        panelSearchResultsTable = new JPanel();
        String columnNames[] = {"Wort", "Anzahl", "Webseite", "Datum"};
        modelSearchResults = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        tableSearchResults = new JTable(modelSearchResults);
        scrollPaneTableResults = new JScrollPane(tableSearchResults);

        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableSearchResults.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableSearchResults.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tableSearchResults.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableSearchResults.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        scrollPaneTableResults.setPreferredSize(new DimensionUIResource(950, 400));
        panelSearchResultsTable.add(scrollPaneTableResults);

        addToPanel.add(panelSearchResultsTable);
    }

    // --------------------------------------------------
    // GUI Ende
    // --------------------------------------------------
    // --------------------------------------------------
    // Event Handling Start
    // --------------------------------------------------

    // --------------------------------------------------
    // Was soll passieren wenn sich das Fenster schlie??t
    // --------------------------------------------------
    private void addWindowEvents() {
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
        private boolean checkIfDownloadIsPossible() {
            if (textFieldHTTrackExe.getText().isEmpty()) {
                JOptionPane.showMessageDialog(GUICountWords.this, "Keine HTTrack-Exe selektiert", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (textFieldWebsiteFile.getText().isEmpty()) {
                JOptionPane.showMessageDialog(GUICountWords.this, "Keine Webseiten-Datei selektiert", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (textFieldHTTrackOutputFolder.getText().isEmpty()) {
                JOptionPane.showMessageDialog(GUICountWords.this, "Kein HTTrack Ausgabeordner angegeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }

        private boolean checkIfDownloadCycleIsPossible() {
            if (((int) spinnerDownloadInDays.getValue() < 1) && ((int) spinnerDownloadInHours.getValue() < 1)) {
                JOptionPane.showMessageDialog(GUICountWords.this, "Zykluswert muss gr????er 0 sein", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }

        private boolean checkIfEvaluationIsPossible() {
            if (textFieldHTTrackOutputFolder.getText().isEmpty()) {
                JOptionPane.showMessageDialog(GUICountWords.this, "Kein HTTrack Ausgabeordner angegeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (textFieldEvaluationWebsites.getText().isEmpty()) {
                JOptionPane.showMessageDialog(GUICountWords.this, "Kein Webseiten-Ordner-Datei angegeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (textFieldTermsFile.getText().isEmpty()) {
                JOptionPane.showMessageDialog(GUICountWords.this, "Kein Suchbegriff-Datei angegeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (textFieldEvaluationOutputFolder.getText().isEmpty()) {
                JOptionPane.showMessageDialog(GUICountWords.this, "Kein Auswertungs-Ausgabeordner angegeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }

        private boolean checkIfEvaluationCycleIsPossible() {
            if (((int) spinnerEvaluateInDays.getValue() < 1) && ((int) spinnerEvaluateInHours.getValue() < 1)) {
                JOptionPane.showMessageDialog(GUICountWords.this, "Zykluswert muss gr????er 0 sein", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }

        private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        private Runnable runnableDownloadCycle = new Runnable() {
            public void run() {
                File tempFileDownloadRunning = new File(".\\DownloadIsRunning");
                File tempFileEvaluationRunning = new File(".\\EvaluationIsRunning");
                if (tempFileDownloadRunning.exists()) {
                    System.out.println("Download l??uft bereits, warten auf den n??chsten Zyklus");
                }
                if (tempFileEvaluationRunning.exists()) {
                    System.out.println("Es l??uft gerade eine Auswertung, warten auf den n??chsten Zyklus");
                }
                if ((!tempFileDownloadRunning.exists()) && (!tempFileEvaluationRunning.exists())) {
                    startHTTrackDownload();
                }
                String nextCycleDateTime = getNextCycleDateTime(
                        (int) spinnerDownloadInDays.getValue(),
                        (int) spinnerDownloadInHours.getValue()
                );
                labelNextDownloadAt.setText("Download startet um: " + nextCycleDateTime);
            }
        };
        private Runnable runnableEvaluationCycle = new Runnable() {
            public void run() {
                File tempFileDownloadRunning = new File(".\\DownloadIsRunning");
                File tempFileEvaluationRunning = new File(".\\EvaluationIsRunning");
                if (tempFileDownloadRunning.exists()) {
                    System.out.println("Es l??uft gerade ein Download, warten auf den n??chsten Zyklus");
                }
                if (tempFileEvaluationRunning.exists()) {
                    System.out.println("Auswertung l??uft bereits, warten auf den n??chsten Zyklus");
                }
                if ((!tempFileDownloadRunning.exists()) && (!tempFileEvaluationRunning.exists())) {
                    startEvaluation();
                }
                String nextEvaluationDateTime = getNextCycleDateTime(
                        (int) spinnerEvaluateInDays.getValue(),
                        (int) spinnerEvaluateInHours.getValue()
                );
                labelNextEvaluationAt.setText("Auswertung startet um: " + nextEvaluationDateTime);
            }
        };
        private ScheduledFuture<?> threadDownloadCycle;
        private ScheduledFuture<?> threadEvaluationCycle;

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buttonChooseHTTrackExe) {
                int result = chooseHTTrackExe.showOpenDialog(GUICountWords.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    textFieldHTTrackExe.setText(chooseHTTrackExe.getSelectedFile().toString());
                }
            }
            if (e.getSource() == buttonChooseWebsiteFile) {
                int result = chooseWebsiteFile.showOpenDialog(GUICountWords.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    textFieldWebsiteFile.setText(chooseWebsiteFile.getSelectedFile().toString());
                }
            }
            if (e.getSource() == buttonChooseHTTrackOutputFolder) {
                int result = chooseHTTrackOutputFolder.showOpenDialog(GUICountWords.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    textFieldHTTrackOutputFolder.setText(chooseHTTrackOutputFolder.getSelectedFile().toString());
                }
            }
            if (e.getSource() == buttonStartHTTrack) {
                if (!checkIfDownloadIsPossible()) return;
                startHTTrackDownload();
            }
            if (e.getSource() == buttonStartDownloadCycle) {
                if (!checkIfDownloadIsPossible()) return;
                if (!checkIfDownloadCycleIsPossible()) return;
                int nextCycleInDays = (int) spinnerDownloadInDays.getValue();
                int nextCycleInHours = (int) spinnerDownloadInHours.getValue();
                int nextCycleCombinedInHours = (nextCycleInDays * 24) + nextCycleInHours;
                threadDownloadCycle = executorService.scheduleAtFixedRate(runnableDownloadCycle, 0, nextCycleCombinedInHours, TimeUnit.HOURS);
                spinnerDownloadInDays.setEnabled(false);
                spinnerDownloadInHours.setEnabled(false);
                buttonStartDownloadCycle.setEnabled(false);
                buttonStopDownloadCycle.setEnabled(true);
            }
            if (e.getSource() == buttonStopDownloadCycle) {
                if (threadDownloadCycle != null) threadDownloadCycle.cancel(false);
                labelNextDownloadAt.setText("");
                spinnerDownloadInDays.setEnabled(true);
                spinnerDownloadInHours.setEnabled(true);
                buttonStartDownloadCycle.setEnabled(true);
                buttonStopDownloadCycle.setEnabled(false);
            }
            if (e.getSource() == buttonEvaluationWebsites) {
                int result = chooseEvaluationWebsites.showOpenDialog(GUICountWords.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    textFieldEvaluationWebsites.setText(chooseEvaluationWebsites.getSelectedFile().toString());
                }
            }
            if (e.getSource() == buttonChooseTermsFile) {
                int result = chooseTermsFile.showOpenDialog(GUICountWords.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    textFieldTermsFile.setText(chooseTermsFile.getSelectedFile().toString());
                }
            }
            if (e.getSource() == buttonEvaluationOutputFolder) {
                int result = chooseEvaluationOutputFolder.showOpenDialog(GUICountWords.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    textFieldEvaluationOutputFolder.setText(chooseEvaluationOutputFolder.getSelectedFile().toString());
                }
            }
            if (e.getSource() == buttonStartEvaluation) {
                if (!checkIfEvaluationIsPossible()) return;
                startEvaluation();
            }
            if (e.getSource() == buttonStartEvaluationCycle) {
                if (!checkIfEvaluationIsPossible()) return;
                if (!checkIfEvaluationCycleIsPossible()) return;
                int nextEvaluationInDays = (int) spinnerEvaluateInDays.getValue();
                int nextEvaluationInHours = (int) spinnerEvaluateInHours.getValue();
                int nextEvaluationCombinedInHours = (nextEvaluationInDays * 24) + nextEvaluationInHours;
                threadEvaluationCycle = executorService.scheduleAtFixedRate(runnableEvaluationCycle, 0, nextEvaluationCombinedInHours, TimeUnit.HOURS);
                spinnerEvaluateInDays.setEnabled(false);
                spinnerEvaluateInHours.setEnabled(false);
                buttonStartEvaluationCycle.setEnabled(false);
                buttonStopEvaluationCycle.setEnabled(true);
            }
            if (e.getSource() == buttonStopEvaluationCycle) {
                if (threadEvaluationCycle != null) threadEvaluationCycle.cancel(false);
                labelNextEvaluationAt.setText("");
                spinnerEvaluateInDays.setEnabled(true);
                spinnerEvaluateInHours.setEnabled(true);
                buttonStartEvaluationCycle.setEnabled(true);
                buttonStopEvaluationCycle.setEnabled(false);
            }
            if (e.getSource() == buttonOpenWebsiteFile) {
                try {
                    File websiteFile = new File(textFieldWebsiteFile.getText());
                    if (textFieldWebsiteFile.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(GUICountWords.this, "Kein Webseitendatei angegeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!websiteFile.exists()) {
                        JOptionPane.showMessageDialog(GUICountWords.this, "Die Webseitendatei existiert nicht", "Fehler", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Desktop.getDesktop().edit(websiteFile);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (e.getSource() == buttonOpenWebsiteFolderFile) {
                try {
                    File websiteFolderFile = new File(textFieldEvaluationWebsites.getText());
                    if (textFieldEvaluationWebsites.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(GUICountWords.this, "Kein Webseitenordnerdatei angegeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!websiteFolderFile.exists()) {
                        JOptionPane.showMessageDialog(GUICountWords.this, "Die Webseitenordnerdatei existiert nicht", "Fehler", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Desktop.getDesktop().edit(websiteFolderFile);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (e.getSource() == buttonOpenTermsFile) {
                try {
                    File termsFile = new File(textFieldTermsFile.getText());
                    if (textFieldTermsFile.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(GUICountWords.this, "Kein Begriffe-Datei angegeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!termsFile.exists()) {
                        JOptionPane.showMessageDialog(GUICountWords.this, "Die Begriffe-Datei existiert nicht", "Fehler", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Desktop.getDesktop().edit(termsFile);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            if (e.getSource() == buttonSearch) {
                File tempFileEvaluationRunning = new File(".\\EvaluationIsRunning");
                if (tempFileEvaluationRunning.exists()) {
                    System.out.println("Es wird gerade eine Auswertung gemacht");
                    return;
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                modelSearchResults.setRowCount(0);
                searchThroughWordTable(
                        textFieldFilterSearchTerm.getText(),
                        textFieldFilterWebsites.getText(),
                        dateFormat.format((Date) spinnerFilterDateFrom.getValue()),
                        dateFormat.format((Date) spinnerFilterDateTo.getValue())
                );
            }
        }

        private void Show_Output(Process process) throws IOException {
            BufferedReader output_reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = "";
            while ((output = output_reader.readLine()) != null) {
                System.out.println(output);
            }
        }

        private String getNextCycleDateTime(int inDays, int inHours) {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, inDays);
            cal.add(Calendar.HOUR_OF_DAY, inHours);
            return dateTimeFormat.format(cal.getTime());
        }

        private void  setGUIInputEnabled(boolean enabled){

            /*Website Download */
            buttonChooseHTTrackExe.setEnabled(enabled);
            buttonChooseWebsiteFile.setEnabled(enabled);
            buttonOpenWebsiteFile.setEnabled(enabled);
            buttonChooseHTTrackOutputFolder.setEnabled(enabled);
            radioButtonOnlyHTMLFiles.setEnabled(enabled);
            radioButtonIncludeImages.setEnabled(enabled);
            radioButtonIncludeImagesAndVideos.setEnabled(enabled);
            radioButtonAllFiles.setEnabled(enabled);
            buttonStartHTTrack.setEnabled(enabled);
            buttonStartDownloadCycle.setEnabled(enabled);
            spinnerDownloadInDays.setEnabled(enabled);
            spinnerDownloadInHours.setEnabled(enabled);

            /*Auswertung*/
            buttonEvaluationWebsites.setEnabled(enabled);
            buttonOpenWebsiteFolderFile.setEnabled(enabled);
            buttonChooseTermsFile.setEnabled(enabled);
            buttonOpenTermsFile.setEnabled(enabled);
            buttonEvaluationOutputFolder.setEnabled(enabled);
            buttonStartEvaluation.setEnabled(enabled);
            buttonStartEvaluationCycle.setEnabled(enabled);
            spinnerEvaluateInDays.setEnabled(enabled);
            spinnerEvaluateInHours.setEnabled(enabled);
            checkBoxTrendAnalysis.setEnabled(enabled);
            checkBoxTrendAnalysis.setEnabled(enabled);
            textFieldTrendAnalysisName.setEnabled(enabled);

            /*Registerkarten*/
            countWordsRegisters.setEnabled(enabled);
        }

        /* Methode bekommt den allgemeinen Zielordner und die aktuelle URL,
        *  zur Ermittlung des Ausgabeordners der aktuellen URL, ??bermittelt. Damit kann
        *  die Ordnergr????e und Anzahl Subordnern und Dateien ermittelt werden.*/
        private HashMap<String, Double> getDirectoryDetails(String superDir, String currUrl ){
            HashMap<String, Double> dirDetails = new HashMap<>();
            String targetFolder = null;
            String path = null;

            if (currUrl.contains("www.")){
                String subString = currUrl.substring(currUrl.indexOf("www."));
                String subString2 = subString.substring(0,subString.indexOf("/"));
                targetFolder = subString2;
            } else if(currUrl.contains("http:") || currUrl.contains("https:")) {
                String subString = currUrl.substring(currUrl.indexOf("://") + 3);
                String subString2 = subString.substring(0,subString.indexOf("/"));
                targetFolder = subString2;
            }

            path = superDir + "\\" + targetFolder;
            File dir = new File(path);

            if (dir.exists()){
                double size = FileUtils.sizeOfDirectory(dir);
                double sizeKB = size / 1024;
                double sizeMB = sizeKB / 1024;
                double sizeGB = sizeMB / 1024;

                if (sizeGB > 1) {
                    double tmp = Math.round(sizeGB * 100);
                    sizeGB = tmp / 100;
                    dirDetails.put("dirSizeGB", sizeGB);
                } else {
                    double tmp = Math.round(sizeMB * 100);
                    sizeMB = tmp / 100;
                    dirDetails.put("dirSizeMB", sizeMB);
                }

                int[] nrFilesAndDir = getNrOfFilesAndDirectoriesInt(dir);

                if(nrFilesAndDir != null) {
                    if (nrFilesAndDir.length == 2) {
                        dirDetails.put("nrFiles", (double) nrFilesAndDir[0]);
                        dirDetails.put("nrDirs", (double) nrFilesAndDir[1]);
                    }
                }


                return dirDetails;
            } else {
                return null;
            }
        }

        private int[] getNrOfFilesAndDirectoriesInt(File dir){
            int[] cnt = new int[2];
            int[] tmp;
            if (dir.exists()) {
                for (File file : dir.listFiles()) {
                    if (file.isFile()){
                        cnt[0] = cnt[0] + 1;
                    }
                    if (file.isDirectory()){
                        tmp = getNrOfFilesAndDirectoriesInt(file);
                        cnt[0] = cnt[0] + tmp[0];
                        cnt[1] = cnt[1] + tmp[1] +1;
                    }
                }
                return cnt;
            }
            return null;
        }

        private void startHTTrackDownload() {
            File tempFileDownloadRunning = new File(".\\DownloadIsRunning");
            String outputFolder = textFieldHTTrackOutputFolder.getText().replace("\\", "\\\\");
            String wordCounterLogFilePath = outputFolder + "\\WordCounterLog.txt";
            File wordCounterLogFile = new File(wordCounterLogFilePath);
            final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            try {
                new Thread(() -> {
                    try (Stream<String> websiteStream = Files.lines(Paths.get(textFieldWebsiteFile.getText()))) {

                        setGUIInputEnabled(false);
                        int maxURLsTmp = 0;
                        AtomicInteger cnt= new AtomicInteger(0);

                        try(Stream<String> websiteStreamLines = Files.lines(Paths.get(textFieldWebsiteFile.getText()))) {
                             maxURLsTmp = (int) websiteStreamLines.count();
                        }catch (IOException ioException1){
                            System.out.println(ioException1);
                        }

                        final int maxURLs = maxURLsTmp;
                        boolean logFileCreatedTmp = false;

                        try {
                            logFileCreatedTmp = wordCounterLogFile.createNewFile();
                        }catch (IOException ioException3){
                            System.out.println("WordCounter-Logdatei nicht erstellt: " + ioException3.getMessage());
                        }
                        final boolean logFileCreated = logFileCreatedTmp;

                        downloadProgressBar.setVisible(false);
                        downloadProgressBar.setValue(0);
                        downloadProgressBar.setStringPainted(true);
                        downloadProgressBar.setMaximum(maxURLs);
                        downloadProgressBar.setString(cnt + "/" + maxURLs);
                        downloadProgressBar.setIndeterminate(true);
                        downloadProgressBar.setVisible(true);

                        websiteStream.forEach(url -> {
                            try {
                                // Build file filter
                                String filter = null;
                                if (radioButtonOnlyHTMLFiles.isSelected()) {
                                    // Only HTML- and HTM-files downloaded
                                    filter = "-*.jpg -*.png -*.gif -*.mp4 -*.ogg -*.webm"; //exclude images and videos explicitly
                                } else if (radioButtonIncludeImages.isSelected()) {
                                    // HTML-, HTM-, JPG- and PNG-files downloaded
                                    filter = "+*.jpg +*.png +*.gif -*.mp4 -*.ogg -*.webm "; //include images and exclude videos explicitly
                                } else if (radioButtonIncludeImagesAndVideos.isSelected()) {
                                    // HTML-, HTM-, JPG-, PNG-, MP4-, OGG- and WEBM-files downloaded
                                    filter = "+*.jpg +*.png +*.gif +*.mp4 +*.ogg +*.webm "; //include images and videos explicitly
                                }

                                String command = "\"" + textFieldHTTrackExe.getText() + "\""
                                        //+ " -%L " + "\"" + textFieldWebsiteFile.getText() + "\""
                                        + " --mirror " + url
                                        + " -O "
                                        + "\"" + textFieldHTTrackOutputFolder.getText() + "\" "
                                        + "-w -c8 -f0 -s0 "
                                        + "--debug-log " // Logging in Debug-Mode
                                        + (radioButtonAllFiles.isSelected() ? "p7 " : "p1 ") // When AllFiles is selected, then download HTML-files first, then other files (p7) else only HTML-Files (p1)
                                        + "-A100000000 -q -%v "
                                        + "-E3600 " // Timeout for mirroring one URL => 3600 seconds
                                        + filter; // File filters

                                tempFileDownloadRunning.createNewFile();

                                // Write download start to WordCounterLog
                                if (logFileCreated) {
                                    FileWriter logWriter = new FileWriter(wordCounterLogFilePath, true);
                                    String timestamp = ZonedDateTime.now().format(timestampFormatter);
                                    logWriter.write(timestamp + " - Download der Website " + url + " gestartet!\n");
                                    logWriter.close();
                                }

                                Process runtime = Runtime.getRuntime().exec(command);
                                Show_Output(runtime);

                                // Write download end to WordCounterLog
                                if (logFileCreated) {
                                    FileWriter logWriter = new FileWriter(wordCounterLogFilePath, true);
                                    String timestamp = ZonedDateTime.now().format(timestampFormatter);

                                    HashMap<String, Double> dirDetails = getDirectoryDetails(outputFolder, url);

                                    String dirSize = dirDetails.containsKey("dirSizeGB") ? dirDetails.get("dirSizeGB") + "GB" : dirDetails.get("dirSizeMB") + "MB";
                                    int nrFiles = dirDetails.get("nrFiles").intValue();
                                    int nrDirs = dirDetails.get("nrDirs").intValue();

                                    logWriter.write(timestamp + " - Download der Website " + url + " abgeschlossen!\n");
                                    logWriter.write("\tGr????e: " + dirSize + "; Dateien: " + nrFiles +"; Ordner: " + nrDirs + "\n");
                                    logWriter.close();
                                }

                                // Copy hts-log.txt for future analysis
                                try {
                                    String url4LogFile = url.replace(":", "").replace("//", "-").replace("/", "-");
                                    Files.copy(new File(outputFolder + "\\hts-log.txt").toPath(),
                                            new File(outputFolder + "\\hts-log_" + url4LogFile + ".txt").toPath(),
                                            StandardCopyOption.REPLACE_EXISTING);
                                } catch (IOException ioException2) {
                                    System.out.println("Fehler beim Kopieren der Logdatei: " + ioException2.getMessage());
                                }

                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }

                            cnt.getAndIncrement();
                            downloadProgressBar.setValue(cnt.get());
                            downloadProgressBar.setIndeterminate(false);
                            downloadProgressBar.setString(cnt + "/" + maxURLs);
                        });

                        setGUIInputEnabled(true);

                    } catch (IOException ioException) {
                        System.out.println(ioException.getMessage());
                    }

                }).start();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            } finally {
                if (tempFileDownloadRunning.exists()) tempFileDownloadRunning.delete();
            }
        }

        private void writeSettingsIntoConfigTextfile() {
            try {
                FileWriter myWriter = new FileWriter("config.txt");
                myWriter.write("AuszuwertenderOrdner=" + textFieldHTTrackOutputFolder.getText() + "\r\n");
                myWriter.write("DateiMitWebseitordnern=" + textFieldEvaluationWebsites.getText() + "\r\n");
                myWriter.write("DateiMitBegriffen=" + textFieldTermsFile.getText() + "\r\n");
                myWriter.write("AuswertungsOrdner=" + textFieldEvaluationOutputFolder.getText() + "\r\n");
                String zeroValuesInResult = checkBoxZeroValuesInExcel.isSelected() ? "Ja" : "Nein";
                myWriter.write("NullWerteInErgebnisMitaufnehmen=" + zeroValuesInResult + "\r\n");
                String doTrendAnalysis = checkBoxTrendAnalysis.isSelected() ? "Ja" : "Nein";
                myWriter.write("InDatenbankSchreiben=" + doTrendAnalysis + "\r\n");
                myWriter.write("TrendAnalysisName=" + textFieldTrendAnalysisName.getText() + "\r\n");
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void startEvaluation() {
            File tempFileEvaluationRunning = new File(".\\EvaluationIsRunning");
            try {
                writeSettingsIntoConfigTextfile();
                String command = "\"" + System.getenv("JAVA_HOME") + "\\bin\\java.exe\" -cp \".\\lib\\*\" \".\\src\\CountWords.java\"";
                tempFileEvaluationRunning.createNewFile();
                Process runtime = Runtime.getRuntime().exec(command);
                Show_Output(runtime);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            } finally {
                if (tempFileEvaluationRunning.exists()) tempFileEvaluationRunning.delete();
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
    private Connection connWords = null;

    private void connectToDB() {
        if (conn != null) return;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:config.db");
            connWords = DriverManager.getConnection("jdbc:sqlite:Words.db");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void disconnectFromDB() {
        try {
            if (conn != null) conn.close();
            if (connWords != null) connWords.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void LoadAllSettings() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM settings");
            String key;
            String value;
            while (rs.next()) {
                key = rs.getString("key");
                value = rs.getString("value");
                if (key.equals("HTTrackEXE")) {
                    textFieldHTTrackExe.setText(value);
                    chooseHTTrackExe.setSelectedFile(new File(value));
                }
                if (key.equals("FileWithWebsites")) {
                    textFieldWebsiteFile.setText(value);
                    chooseWebsiteFile.setSelectedFile(new File(value));
                }
                if (key.equals("HTTrackOutputFolder")) {
                    textFieldHTTrackOutputFolder.setText(value);
                    chooseHTTrackOutputFolder.setSelectedFile(new File(value));
                }
                if (key.equals("DownloadOnlyHTMLFiles")) {
                    if (value.toLowerCase().equals("yes")) {
                        radioButtonOnlyHTMLFiles.setSelected(true);
                    } else {
                        radioButtonAllFiles.setSelected(true);
                    }
                }
                if (key.equals("DownloadCycleDays")) {
                    spinnerDownloadInDays.setValue(Integer.parseInt(value));
                }
                if (key.equals("DownloadCycleHours")) {
                    spinnerDownloadInHours.setValue(Integer.parseInt(value));
                }
                if (key.equals("FileWithWebsiteFolders")) {
                    textFieldEvaluationWebsites.setText(value);
                    chooseEvaluationWebsites.setSelectedFile(new File(value));
                }
                if (key.equals("FileWithTerms")) {
                    textFieldTermsFile.setText(value);
                    chooseTermsFile.setSelectedFile(new File(value));
                }
                if (key.equals("ZeroValuesInExcel")) {
                    checkBoxZeroValuesInExcel.setSelected(value.toLowerCase().equals("yes"));
                }
                if (key.equals("DoTrendAnalysis")) {
                    checkBoxTrendAnalysis.setSelected(value.toLowerCase().equals("yes"));
                }
                if (key.equals("TrendAnalysisName")) {
                    textFieldTrendAnalysisName.setText(value);
                }
                if (key.equals("EvaluationOutputFolder")) {
                    textFieldEvaluationOutputFolder.setText(value);
                    chooseEvaluationOutputFolder.setSelectedFile(new File(value));
                }
                if (key.equals("EvaluationCycleDays")) {
                    spinnerEvaluateInDays.setValue(Integer.parseInt(value));
                }
                if (key.equals("EvaluationCycleHours")) {
                    spinnerEvaluateInHours.setValue(Integer.parseInt(value));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void SaveAllSettings() {
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
            pstmt.setString(2, "DownloadCycleDays");
            pstmt.setString(1, spinnerDownloadInDays.getValue().toString());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "DownloadCycleHours");
            pstmt.setString(1, spinnerDownloadInHours.getValue().toString());
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
            pstmt.setString(2, "ZeroValuesInExcel");
            pstmt.setString(1, checkBoxZeroValuesInExcel.isSelected() ? "yes" : "no");
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "DoTrendAnalysis");
            pstmt.setString(1, checkBoxTrendAnalysis.isSelected() ? "yes" : "no");
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "TrendAnalysisName");
            pstmt.setString(1, textFieldTrendAnalysisName.getText());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "EvaluationOutputFolder");
            pstmt.setString(1, textFieldEvaluationOutputFolder.getText());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "EvaluationCycleDays");
            pstmt.setString(1, spinnerEvaluateInDays.getValue().toString());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement("UPDATE Settings SET value=? WHERE key=?");
            pstmt.setString(2, "EvaluationCycleHours");
            pstmt.setString(1, spinnerEvaluateInHours.getValue().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void searchThroughWordTable(String searchTerm, String websiteTerm, String dateFrom, String dateTo) {
        try {
            PreparedStatement pstmt = connWords.prepareStatement("SELECT word,number,website,date FROM Word " +
                    "WHERE word LIKE ? AND website LIKE ? " +
                    "AND substr(date,7)||substr(date,4,2)||substr(date,1,2) BETWEEN ? AND ?");
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + websiteTerm + "%");
            pstmt.setString(3, dateFrom);
            pstmt.setString(4, dateTo);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String rowData[] = {
                        rs.getString("word"),
                        Integer.toString(rs.getInt("number")),
                        rs.getString("website"),
                        rs.getString("date")};
                modelSearchResults.addRow(rowData);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // --------------------------------------------------
    // Datenbank-Operationen Ende
    // --------------------------------------------------

}
