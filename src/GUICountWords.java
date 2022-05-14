import java.io.Console;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;

public class GUICountWords extends JFrame {
    // --------------------------------------------------
    // UI-Elemente
    // --------------------------------------------------
    private JPanel mainPanel;
    private JPanel panelDownloadWebsites;
    private JPanel panelEvaluation;
    private TitledBorder titledBorderDW;
    private TitledBorder titledBorderEval;

    public static void main(String[] args) throws Exception {
        new GUICountWords();
    }

    public GUICountWords(){
        setTitle("Word Counter");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024,768);
        
        initComponents();
        add(mainPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents(){
        mainPanel = new JPanel();
        panelDownloadWebsites = new JPanel();
        panelEvaluation = new JPanel();
        titledBorderDW = new TitledBorder("Website-Download");
        titledBorderEval = new TitledBorder("Auswertungen");

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        panelDownloadWebsites.setBorder(titledBorderDW);
        panelEvaluation.setBorder(titledBorderEval);

        mainPanel.add(panelDownloadWebsites);
        mainPanel.add(panelEvaluation);
    }
}