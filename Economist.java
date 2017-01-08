import java.util.*;
import java.io.*;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.*;
import com.mongodb.ServerAddress;
import java.util.Arrays;
import java.util.HashMap;

import org.bson.Document;

import org.jsoup.Jsoup;
// import org.jsoup.nodes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Economist {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) throw new IOException("input file format wrong !! -> java Harvester file.in file.out");
        
	/*--  using BufferedReader will accelerate the speed of input and output  --*/
	
	BufferedReader f = new BufferedReader(new FileReader(args[0]));
	
        String web = null;
	// StringBuilder sb = new StringBuilder();
        List<String> links = new ArrayList<String>();
        org.jsoup.nodes.Document doc;
        while ((web = f.readLine()) != null) {
	    String website = web;
	    try {
	        doc = Jsoup.connect(website).userAgent("Mozilla").get();
                System.out.println("connect to the website" + website);
		// Elements contents = doc.select("span[id]");  
		Elements contents = doc.select("div[class=btn-group]");  
		for (Element content : contents) {
                    // System.out.println(content);
                    String link = content.select("a").attr("abs:href");
                    links.add(link);
                    System.out.println(link);
                    // out.println(link);
                    // sb.append(text);
                    // sb.append('\n');
		}
	    } catch (IOException e) { e.printStackTrace(); }
	}

        int i = 0;
        List<String> textLinks = new ArrayList<String>();
        for (String link : links) {
            i += 1;
            // if (i > 1) break;
            System.out.println(link);
            try {
                doc = Jsoup.connect(link).userAgent("Mozilla").get();
                System.out.println("connect to the website " + link);
                Elements contents = doc.select("ol");
                for (Element content : contents) {
                    Elements subLinks = content.select("li");
                    for (Element subLink : subLinks) {
                        if (subLink.text().matches(".*[a-zA-Z].*")) {
                            String textLink = subLink.select("a").attr("abs:href");
                            textLinks.add(textLink);
                            System.out.println(textLink);
                        }
                    }
                }
                System.out.println();
            } catch (IOException e) { e.printStackTrace(); }

        }

        System.out.println("total links = " + textLinks.size());

        // To connect to mongodb server
         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

         // Now connect to your databases
         MongoDatabase db = mongoClient.getDatabase("Text");
         System.out.println("Connect to database successfully");
         // boolean auth = db.authenticate(myUserName, myPassword);
         // System.out.println("Authentication: "+auth);

         MongoCollection<Document> coll = db.getCollection("Economist");
         System.out.println("Collection created successfull");

        Document document;
        Iterator<String> iter = textLinks.iterator();
	PrintWriter out;
        StringBuilder sb = new StringBuilder();
        int j = 1;
        int jump = 10;
        while (iter.hasNext()) {
            String link = iter.next();
            j += 1;
            String testFile = Integer.toString(j/jump + 1) + ".txt";
            document = new Document("website", link)
              .append("saveTo", testFile);
            Document findDoc = coll.find(new Document("website", new Document("$eq", link))).first();
        if (findDoc == null) {
            coll.insertOne(document);
        }

            File file = new File(args[1] + "/" + testFile);
            if (file.exists()) {
                System.out.println(testFile + " exists, omit");   
                continue;
            }
            if (j % jump == 0) {
                out = new PrintWriter(new BufferedWriter(new FileWriter(args[1] + "/" + Integer.toString(j/jump) + ".txt")));
                out.println(sb.toString());
                out.close();
                sb = new StringBuilder();
            }
            try {
                if (link.equals("")) continue;
                System.out.println("connect to the website i: " + j + " " + link);
                doc = Jsoup.connect(link).userAgent("Mozilla").get();
                Elements contents = doc.select("div[class=articlebody]");
                for (Element content : contents) {
                    String text = content.text();
                    sb.append(text);
                    // out.println(text);
                    System.out.println(text);
                }
            } catch (IOException e) { e.printStackTrace(); }
        }
    }
}
