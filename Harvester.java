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
	
        String web = null;
	StringBuilder sb = new StringBuilder();
        while ((web = f.readLine()) != null) {
            for (int i = 1; i < 26; i++) {
	    Document doc;
	    String website = web + Integer.toString(i) + ".html";
	    int num = 1;
	    try {
	        doc = Jsoup.connect(website).get();
                System.out.println("connect to the website" + website);
		// Elements contents = doc.select("span[id]");  
		Elements contents = doc.select("table[style]");  
		for (Element content : contents) {
                    String text = content.text();
                    // System.out.println(text);
                    out.println(text);
                    // sb.append(text);
                    // sb.append('\n');
		}
	    } catch (IOException e) { e.printStackTrace(); }
            }
	}

	// out.println(sb.toString());
        out.close();
    }
}
