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

public class MongoDBJDBC {

   public static void main( String args[] ) {
	
      try{
		
         // To connect to mongodb server
         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
			
         // Now connect to your databases
         MongoDatabase db = mongoClient.getDatabase("test");
         System.out.println("Connect to database successfully");
         // boolean auth = db.authenticate(myUserName, myPassword);
         // System.out.println("Authentication: "+auth);
 
         MongoCollection<Document> coll = db.getCollection("Economist");
         System.out.println("Collection created successfull");

         Document document = new Document("website", "www.125.com")
         .append("saveTo", "1.txt");
//         document.put("www.123.com", "1.txt");
//         document.put("www.125.com", "1.txt");
//         document.put("www.127.com", "2.txt");
         
        Document findDoc = coll.find(new Document("website", new Document("$eq", "www.123.com"))).first();
        if (findDoc == null) {
            coll.insertOne(document);
            System.out.println("insert economist map successfully");
        } else {
            System.out.println("exist!");
        }
           
        // coll.deleteOne(Filters.eq("website", "www.125.com"));
        FindIterable<Document> findIterable = coll.find(); // new Document("website", new Document("$eq", "www.123.com")));
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while (mongoCursor.hasNext()) {
            System.out.println(mongoCursor.next());
        }
        
//         DBCursor cursor = coll.find();
//         int i = 1;
//         while (cursor.hasNext()) {
//             System.out.println("Inserted Document: " + i);
//             System.out.println(cursor.next());
//             i++;
//         }

//         BasicDBObject doc = new BasicDBObject("title", "MongoDB").
//            append("description", "database").
//            append("likes", 100).
//            append("url", "http://www.tutorialspoint.com/mongodb/").
//            append("by", "tutorials point");
//				
//         coll.insert(doc);
//         System.out.println("Document inserted successfully");
			
      }catch(Exception e){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      }
   }
}
