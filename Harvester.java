import java.util.*;
import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Harvester {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) throw new IOException("input file format wrong !! -> java Harvester file.in file.out");
        
	/*--  using BufferedReader will accelerate the speed of input and output  --*/
	
	BufferedReader f = new BufferedReader(new FileReader(args[0]));
	PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(args[1])));
	
	String web = f.readLine();
	StringBuilder sb = new StringBuilder();
	
	Document doc;   /*-  Varable defined in Jsoup  -*/
	int numOfPages = 2;    
	for (int i = 1; i < numOfPages; i++) {
	    String website = web;
	    System.out.println("page : " + i + " -> " + website);
	    int num = 1;
	    try {
	        doc = Jsoup.connect(website).get();
                System.out.println("connect to the website");
		Elements contents = doc.select("font[data-iceapw]");  
		for (Element content : contents) {
                    sb.append(content.text());
		}
	    } catch (IOException e) { e.printStackTrace(); }
	}

	out.println(sb.toString());
        out.close();
    }
}
