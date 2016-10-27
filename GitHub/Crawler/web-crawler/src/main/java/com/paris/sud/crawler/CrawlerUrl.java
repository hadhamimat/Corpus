package com.paris.sud.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

/**
 * Created by Hadhami on 26/10/2016.
 */
public class CrawlerUrl {

    private int depth = 0;
    private String urlString = null;
    private URL url = null;
    private boolean isAllowedToVisit;
    private boolean isCheckedForPermission = false;
    private boolean isVisited = false;

    public CrawlerUrl() {
    }

    public CrawlerUrl(String urlString, int depth) {
        this.depth = depth;
        this.urlString = urlString;
        computeURL();
    }

    public CrawlerUrl(String urlString) {
        this.urlString = urlString;
        computeURL();
    }

    private void computeURL() {
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            // petit probleme
        }
    }

    public URL getURL() {
        return this.url;
    }

    public int getDepth() {
        return this.depth;
    }

    public boolean isAllowedToVisit() {
        return isAllowedToVisit;
    }

    public void setAllowedToVisit(boolean isAllowedToVisit) {
        this.isAllowedToVisit = isAllowedToVisit;
        this.isCheckedForPermission = true;
    }

    public boolean isCheckedForPermission() {
        return isCheckedForPermission;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setIsVisited() {
        this.isVisited = true;
    }

    public String getUrlString() {
        return this.urlString;
    }

    // donnees et methodes concernant le contenu telecharge depuis l'URL

    private String       htmlText;
    private Document htmlJsoupDoc;
    private String       niceText;
    private String       title;
    private List<String> linkList;

    public String getNiceText() {
        return(niceText);
    }

    public String getTitle() {
        return(title);
    }

    public List<String> getLinks() {
        return(linkList);
    }


    public void setRawContent(String htmlText) {
        String baseURL  = getURL() . toExternalForm();
        this . htmlText = htmlText;
        htmlJsoupDoc    = Jsoup . parse(htmlText,baseURL);
        title           = htmlJsoupDoc . title();
        niceText        = htmlJsoupDoc . body() . text();
        linkList        = new ArrayList<String>();
        Elements hrefJsoupLinks = htmlJsoupDoc . select("a[href]");
        for (Element link : hrefJsoupLinks) {
            String thisLink = link.attr("abs:href");
            if(thisLink . startsWith("http://")) {
                System.out.println("JSOUP Found: " + thisLink);
                linkList . add(thisLink);
            }
        }
    }

    public Queue<CrawlerUrl> readURL(){
        BufferedReader reader = null;
        // on se prend la liste des URLs a parcourir
        Queue<CrawlerUrl> urlQueue = new LinkedList<CrawlerUrl>();
        try {
            File file = new File("/Users/Hadhami/aic/Recherche et extraction d'info/Projet/URLs");
            reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                urlQueue.add(new CrawlerUrl(line));
                reader.close();
                break;
            }
            return urlQueue;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
