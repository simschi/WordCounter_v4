import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;

public class CountWords {
    // --------------------------------------------------
    // Datumsformate 
    // --------------------------------------------------
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private static String currentDate; 
    private static String currentDateTime; 
        
    // --------------------------------------------------
    // Config-Datei einlesen 
    // --------------------------------------------------
    private static String fileWithWebsiteFolders;
    private static String fileWithTerms;
    private static String writeInDatabase;
    private static String backupFolder;
    private static String downloadedWebsitesFolder;
    private static String zeroValuesInResult;
    private static void getDataFromConfigFile() throws Exception{
        File configFile = new File("config.txt");
        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        String setting;
        String settingName;
        String settingValue;
        while ((setting = reader.readLine()) != null) {
            settingName = setting.split("=")[0];
            settingValue = setting.split("=")[1];
            if(settingName.equals("AuszuwertenderOrdner")) downloadedWebsitesFolder = settingValue;
            if(settingName.equals("DateiMitWebseitordnern")) fileWithWebsiteFolders = settingValue;
            if(settingName.equals("DateiMitBegriffen")) fileWithTerms = settingValue; 
            if(settingName.equals("AuswertungsOrdner")) backupFolder = settingValue;
            if(settingName.equals("InDatenbankSchreiben")) writeInDatabase = settingValue;
            if(settingName.equals("NullWerteInErgebnisMitaufnehmen")) zeroValuesInResult = settingValue;
        }
        reader.close();
    } 

    // --------------------------------------------------
    // Zu durchsuchende Webseitordner einlesen
    // --------------------------------------------------
    private static List<String> getWebsiteFoldersToSearch() throws Exception{
        List<String> websiteFolders = new ArrayList<String>();
        File websiteFoldersFileName = new File(fileWithWebsiteFolders);
        BufferedReader reader = new BufferedReader(new FileReader(websiteFoldersFileName));
        String websiteFolder;
        while ((websiteFolder = reader.readLine()) != null) {
            websiteFolders.add(websiteFolder);
        }
        reader.close();
        copyFileInCurrentBackupDir(fileWithWebsiteFolders);
        return websiteFolders;
    }

    // --------------------------------------------------
    // Zu suchende Begriffe von Excel-Datei einlesen
    // --------------------------------------------------
    private static List<String> getSearchTerms() throws Exception{
        // Excel-Datei einlesen
        FileInputStream file = new FileInputStream(new File(fileWithTerms));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<String> terms = new ArrayList<String>();

        // Daten aus Excel-Datei einlesen
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                terms.add(cell.getStringCellValue());
            }
        }
        workbook.close();
        file.close();
        copyFileInCurrentBackupDir(fileWithTerms);
        return terms;
    }

    // --------------------------------------------------
    // Alle Dateien aus Ordner samt Subordner auslesen
    // --------------------------------------------------
    private static List<String> filesInDir;
    private static void getAllFilesInDir(File dirPath){
        File filesList[] = dirPath.listFiles();
        for(File file : filesList) {
           if(file.isFile()) {
               filesInDir.add(file.getAbsolutePath());
           } else {
               getAllFilesInDir(file);
           }
        }
     }

    // --------------------------------------------------
    // HTML-Datei als Plain Text einlesen
    // --------------------------------------------------
    private static String getHTMLPlainText(String htmlFileName){
        try {
            Path filePath = Path.of(htmlFileName);
            String content = Files.readString(filePath);
            String htmlPlainText = Jsoup.parse(content).text();
            return htmlPlainText;
        } catch (Exception e) {
            // e.printStackTrace();
            return "";
        }
    }

    // --------------------------------------------------
    // Zählen wie oft Wörter in einem Text vorkommen
    // --------------------------------------------------
    private static int countWordInText(String text, String words){
        String convertedText = text.replace(" ", "").toLowerCase();
        String convertedWords = words.replace(" ", "").toLowerCase();
        return StringUtils.countMatches(convertedText, convertedWords);
    }

    // --------------------------------------------------
    // Eine Webseite mit Begriffen durchsuchen
    // --------------------------------------------------
    private static Map<String, Integer> foundWordsURLs;
    private static Map<String, Integer> foundWordsWebsites;
    private static void analyzeWebsite(String websiteFolder, List<String> searchTerms){
        // Alle Dateien aus Ordner samt Subordner auslesen
        String websiteFolderPath = downloadedWebsitesFolder + "\\" + websiteFolder;
        File websiteFolderFile = new File(websiteFolderPath);
        filesInDir = new ArrayList<String>();
        getAllFilesInDir(websiteFolderFile);

        // Jede Datei nach sämtlichen Begriffen durchsuchen
        for (String htmlFileName : filesInDir) {
            System.out.println(htmlFileName);
            String htmlPlainText = getHTMLPlainText(htmlFileName);
            for (String searchTerm : searchTerms) {
                String url = htmlFileName.replace(downloadedWebsitesFolder + "\\", "https:\\\\");
                String keyURLs = searchTerm + ";" + url;
                String keyWebsites = searchTerm + ";" + websiteFolder;
                int value = countWordInText(htmlPlainText, searchTerm);
                if((value > 0) || (zeroValuesInResult.toLowerCase().equals("ja"))) {
                    if(foundWordsURLs.containsKey(keyURLs)){
                        int oldValue = foundWordsURLs.get(keyURLs);
                        foundWordsURLs.put(keyURLs, oldValue + value);
                    } else {
                        foundWordsURLs.put(keyURLs, value);
                    }
                }
                if(foundWordsWebsites.containsKey(keyWebsites)){
                    int oldValue = foundWordsWebsites.get(keyWebsites);
                    foundWordsWebsites.put(keyWebsites, oldValue + value);
                } else {
                    foundWordsWebsites.put(keyWebsites, value);
                }
            }
        }
        copyWebsiteInCurrentBackupDir(websiteFolderPath);
    }

    // --------------------------------------------------
    // Webseite zu einer URL ermitteln
    // --------------------------------------------------
    private static String getWebsiteOfURL(String URL){
        String website = URL.replace("https:\\\\", "").replace("http:\\\\", "");
        website = website.substring(0, website.indexOf("\\"));
        return "https:\\\\"+website;
    }

    // --------------------------------------------------
    // Excel-Datei mit gefundenen Wörtern erstellen
    // --------------------------------------------------
    private static void writeFoundWordsExcel() throws Exception{
        // Workbook öffnen
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Ergebnis");
        XSSFRow row;
  
        // Daten
        Map<String, Object[]> wordData = new TreeMap<String, Object[]>();
        int counter = 2;
        wordData.put("1", new Object[] { "Wort", "Anzahl", "Webseite", "URL", "Datum" });
        for (Map.Entry<String, Integer> foundWord : foundWordsURLs.entrySet()) {
            wordData.put(Integer.toString(counter), new Object[] {
                foundWord.getKey().split(";")[0],
                Integer.toString(foundWord.getValue()),
                getWebsiteOfURL(foundWord.getKey().split(";")[1]),
                foundWord.getKey().split(";")[1],
                currentDate
            });
            counter++;
        }

        // Daten in Excel schreiben
        Set<String> keyid = wordData.keySet();
        int rowid = 0;
        for (String key : keyid) {
            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = wordData.get(key);
            int cellid = 0;
            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }
        String outputFile = currentBackupDirectory + "\\Ergebnis.xlsx"; 
        FileOutputStream out = new FileOutputStream(new File(outputFile));
        
        // Workbook schließen
        workbook.write(out);
        workbook.close();
        out.close();
    }

    // --------------------------------------------------
    // Datenbank um gefundenen Wörter erweitern
    // --------------------------------------------------
    private static void writeFoundWordsInDatabase() throws Exception {
        if(!writeInDatabase.toLowerCase().equals("ja")) return;
        String driver = "org.sqlite.JDBC";
        String url = "jdbc:sqlite:Words.db";

        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url);
        PreparedStatement prep = conn.prepareStatement("insert into word values (?, ?, ?, ?, ?);");
        for (Map.Entry<String, Integer> foundWord : foundWordsWebsites.entrySet()) {
            prep.setString(2, foundWord.getKey().split(";")[0]);
            prep.setInt(3, foundWord.getValue());
            prep.setString(4, foundWord.getKey().split(";")[1]);
            prep.setString(5, currentDate);
            prep.addBatch();
        }

        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);
        prep.close();
        conn.close();

        copyFileInCurrentBackupDir("Words.db");
    }

    // --------------------------------------------------
    // Auswertungsordner anlegen  
    // --------------------------------------------------
    private static String currentBackupDirectory;
    private static String analyzedWebsiteFolder;
    private static void createDateTimeFolder() {
        currentBackupDirectory = backupFolder + "\\" + currentDateTime;
        File dir = new File(currentBackupDirectory);
        dir.mkdir();
        analyzedWebsiteFolder = currentBackupDirectory + "\\AnalysierteWebseiten";
        dir = new File(analyzedWebsiteFolder);
        dir.mkdir();
    }

    // --------------------------------------------------
    // Mitgegeben Datei in Sicherungsordner kopieren 
    // --------------------------------------------------
    private static void copyFileInCurrentBackupDir(String fileName) {
        Path source = Path.of(fileName);
        Path target = Path.of(currentBackupDirectory + "\\" + source.getFileName());
        try { 
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e){e.printStackTrace();} 
    }

    // --------------------------------------------------
    // Mitgegeben Webseite in Sicherungsordner kopieren 
    // --------------------------------------------------
    private static void copyWebsiteInCurrentBackupDir(String websiteFolder) {        
        File srcDir = new File(websiteFolder);
        File destDir = new File(analyzedWebsiteFolder + "\\" + srcDir.getName());
        try{
            FileUtils.copyDirectory(srcDir, destDir);
        } catch (Exception e) {e.printStackTrace();}
    }

    // --------------------------------------------------
    // Start-Datum und -Zeit des Programms setzen
    // --------------------------------------------------    
    private static void setStartDateTime(){
        Date now = new Date();
        currentDate = dateFormat.format(now);
        currentDateTime = dateTimeFormat.format(now);
    }

    // --------------------------------------------------
    // Hauptprogramm
    // --------------------------------------------------    
    public static void main(String[] args) throws Exception {
        setStartDateTime();
        getDataFromConfigFile();
        createDateTimeFolder();
        copyFileInCurrentBackupDir("config.txt");
        List<String> websiteFoldersToSearch = getWebsiteFoldersToSearch();
        List<String> searchTerms = getSearchTerms();
        foundWordsURLs = new TreeMap<String, Integer>();
        foundWordsWebsites = new TreeMap<String, Integer>();
        for (String websiteFolderToSearch : websiteFoldersToSearch) {
            analyzeWebsite(websiteFolderToSearch, searchTerms);
        }
        writeFoundWordsExcel();
        writeFoundWordsInDatabase(); 
        System.out.println("Auswertung fertiggestellt");
    }
}
