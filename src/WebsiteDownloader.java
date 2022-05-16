/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebsiteDownloader {
    public static void main(String[] args) throws Exception {
        // DownloadWholeWebsite("https://accuro.at/de/");
        test("https://accuro.at/de/");
    }
    private static void getLinks(String url, Set<String> urls) {
        if (urls.contains(url)) {
            return;
        }
        urls.add(url);
    
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select("a");
            for(Element element : elements){
                System.out.println(element.absUrl("href"));
                getLinks(element.absUrl("href"), urls);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void DownloadWholeWebsite(String mainURL){
        Set<String> allURLs = new HashSet<>();
        getLinks(mainURL, allURLs);
        for(String url: allURLs){
            System.out.println(url);
        }
    }

    public static void test(String mainURL){
        try {
            String inputLine = "";
            BufferedReader in=new BufferedReader(new FileReader(mainURL));
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                String pattern = "https?://([-\\w\\.]+)+(:\\d+)?(/([\\w/_\\.]*(\\?\\S+)?)?)?";

                // Create a Pattern object
                Pattern r = Pattern.compile(pattern);
                // Now create matcher object.
                Matcher m = r.matcher(inputLine.replaceAll("http://", "\nhttp://"));
                while (!m.hitEnd()) {
                    if (m.find()) {
                        System.out.println("Found value: " + m.group(0));
                    } else {
                        //System.out.println("NO MATCH");
                    }
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/