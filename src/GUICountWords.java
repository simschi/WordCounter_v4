import java.io.Console;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;

public class GUICountWords extends JFrame {
    // --------------------------------------------------
    // UI-Elemente
    // --------------------------------------------------
    private JPanel mainPanel;
    private JPanel panelDownloadWebsites;
    private JPanel panelEvaluation;
    private JPanel panelChooseWebsiteFile;
    private TitledBorder titledBorderDW;
    private TitledBorder titledBorderEval;
    private JLabel labelChooseWebsiteFile;
    private JTextField textFieldWebsiteFile;
    private JButton buttonChooseWebsiteFile;
    private JFileChooser chooseWebsiteFile;

    public static void main(String[] args) throws Exception {
        new GUICountWords();
    }

    public GUICountWords(){
        setTitle("Word Counter");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024,768);
        
        initComponentMainPanelAndOthers();
        

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponentMainPanelAndOthers(){
        mainPanel = new JPanel();
        panelDownloadWebsites = new JPanel();
        panelEvaluation = new JPanel();
        titledBorderDW = new TitledBorder("Website-Download");
        titledBorderEval = new TitledBorder("Auswertungen");
        
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        panelDownloadWebsites.setBorder(titledBorderDW);
        panelEvaluation.setBorder(titledBorderEval);

        initComponentChooseWebSiteFile(panelDownloadWebsites);

        mainPanel.add(panelDownloadWebsites);
        mainPanel.add(panelEvaluation);
        add(mainPanel);
    }
    
    // --------------------------------------------------
    // Komponente Webseiten-Datei auswählen
    // --------------------------------------------------
    private void initComponentChooseWebSiteFile(JPanel addToPanel){
        panelChooseWebsiteFile = new JPanel();
        labelChooseWebsiteFile = new JLabel("Datei mit Webseiten:");
        textFieldWebsiteFile = new JTextField(70);
        buttonChooseWebsiteFile = new JButton("Datei selektieren...");
        chooseWebsiteFile = new JFileChooser();

        panelChooseWebsiteFile.setLayout(new FlowLayout());
        panelChooseWebsiteFile.add(labelChooseWebsiteFile);
        textFieldWebsiteFile.setEditable(false);
        panelChooseWebsiteFile.add(textFieldWebsiteFile);
        buttonChooseWebsiteFile.addActionListener(new MyActionListener());
        panelChooseWebsiteFile.add(buttonChooseWebsiteFile);

        addToPanel.add(panelChooseWebsiteFile);
    }

    // --------------------------------------------------
    // Innere Klasse um auf Benutzeraktionen zu reagieren
    // --------------------------------------------------
    class MyActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == buttonChooseWebsiteFile){
                int result = chooseWebsiteFile.showOpenDialog(GUICountWords.this);
                if(result == JFileChooser.APPROVE_OPTION){
                    // chooseWebsiteFile.getSelectedFile();
                    textFieldWebsiteFile.setText(chooseWebsiteFile.getSelectedFile().toString());
                }
            }
        }
    }

    
}