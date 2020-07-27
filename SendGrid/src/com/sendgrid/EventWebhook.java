package com.sendgrid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.Filters;

/**
 * Servlet implementation class EventWebhook
 */
@WebServlet("/EventWebhook")
public class EventWebhook extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventWebhook() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    MongoDBDriver db;
    MongoDatabase mdb;
    MongoCollection<Document> collection;
    FindIterable<Document> docs;
    MongoCursor<Document> cursor;
    PrintWriter out;
    List<String> list;
    Date date;
    DateFormat dateFormat;
    String strDate;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		//loop through all GET query parameters
		list = new ArrayList<String>();
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			//print query parameter names 
            String paramName = parameterNames.nextElement();
            list.add(paramName+"\n");
            //out.write(paramName);
            //out.write("n");
            //print query parameter values
            String[] paramValues = request.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
                list.add(paramValue+"\n");
                //out.write("t" + paramValue);
                //out.write("n");
            }
		}
		
		//include date/time stamp in response for easy debugging
		date = Calendar.getInstance().getTime();  
        dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");  
        strDate = dateFormat.format(date);        
		
         System.out.println("list: "+list.toString()+strDate);   
         response.setContentType("application/json");
   		 response.setCharacterEncoding("utf-8");
   		 out = response.getWriter();		 
   		 //send the entire serialized list as response
   		 //Notes about status codes:- : https://www.javamex.com/tutorials/servlets/http_status_code.shtml
   		 	//i. servlet will send 200OK BY DEFAULT-thats why you see this status code appearing on Postman EVEN WHEN YOU DO NOT SET 200OK exclusively
   		 	//ii. you should set the status code before sending any output. This is because the status code always comes before any output in the data returned to the client
   		 response.setStatus(200); 
   		 out.print(list+strDate);
        
		
		/* Retrieve event data from MongoDB's SendGrid DB's EventWebHook collection based on GET request parameter 'event' 
		//retrieve filter criteria from GET request
		String eventVal = request.getParameter("event");
		
		//connect to DB
		//get collection
	    db = new MongoDBDriver();		  
   	    mdb = db.getDBConnection("SendGrid");
		collection = mdb.getCollection("EventWebhook");
		
		//retrieve all documents in the collection based upon filter criteria
		//create mongodb cursor
		//setup an arraylist of String type
		//loop through cursor
		//parse cursor content to json, serialize parsed content to string(for response)
		//add as list element
		docs=collection.find(Filters.eq("event", eventVal));		    
		cursor = docs.iterator();
		list = new ArrayList<String>();
		while(cursor.hasNext())
			list.add(cursor.next().toJson().toString()); //parse cursor content to json, serialize parsed content to string(for response)
		    
		 response.setContentType("application/json");
		 response.setCharacterEncoding("utf-8");
		 out = response.getWriter();		 
		 //send the entire serialized list as response
		 //Notes about status codes:- : https://www.javamex.com/tutorials/servlets/http_status_code.shtml
		 	//i. servlet will send 200OK BY DEFAULT-thats why you see this status code appearing on Postman EVEN WHEN YOU DO NOT SET 200OK exclusively
		 	//ii. you should set the status code before sending any output. This is because the status code always comes before any output in the data returned to the client
		 response.setStatus(200); 
		 out.print(list);
	*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("inside");
		StringBuffer jb = new StringBuffer();
		JSONObject jsonObject;
		JSONArray jsonarray;
		MongoDBDriver db = new MongoDBDriver();		  
		MongoDatabase mdb;
		MongoCollection<Document> collection;
		  String line = null;
		  try {
		    BufferedReader reader = request.getReader(); 
		    while ((line = reader.readLine()) != null) //read POST request body parameters
		      jb.append(line);	//line by line
		  } catch (Exception e) { /*report an error*/ }		  
		 
		  System.out.println("jb :"+jb.toString());
		  
		  try {
		    //jsonObject = new JSONObject(jb.toString()); // https://stleary.github.io/JSON-java/
		     jsonarray = new JSONArray(jb.toString()); //concert request string to JSON array
		     
		  } catch (JSONException e) {
		    // crash and burn
		    throw new IOException("Error parsing JSON request string");
		  }
		
		  /* send with Postman:JSON POST parameter used:-
		   * {
			      "email":"example@test.com",
			      "timestamp":1513299569,
			      "pool": {
			            "name": "new_MY_test",
			            "id": 210
			        },
			      "smtp-id":"<14c5d75ce93.dfd.64b469@ismtpd-555>",
			      "event":"processed",
			      "category":"cat facts",
			      "sg_event_id":"rbtnWrG1DVDGGGFHFyun0A==",
			      "sg_message_id":"14c5d75ce93.dfd.64b469.filter0001.16648.5515E0B88.000000000000000000000"
			  }
		   */		  
		 /*
		 System.out.println("jsonObject pretty:"+jsonObject.toString(2)); 
		 System.out.println("key value:"+jsonObject.get("email"));
		 System.out.println("embedded key value:"+jsonObject.getJSONObject("pool").get("name"));
		 */
		 
		  /* send with Postman:JSON POST parameter used [array]:- Refer: https://sendgrid.com/docs/for-developers/tracking-events/event/
		    [
			  {
			    "email": "example@test.com",
			    "timestamp": 1513299569,
			    "smtp-id": "<14c5d75ce93.dfd.64b469@ismtpd-555>",
			    "event": "processed",
			    "category": "cat facts",
			    "sg_event_id": "sg_event_id",
			    "sg_message_id": "sg_message_id"
			  },
			  {
			    "email": "example@test.com",
			    "timestamp": 1513299569,
			    "smtp-id": "<14c5d75ce93.dfd.64b469@ismtpd-555>",
			    "event": "deferred",
			    "category": "cat facts",
			    "sg_event_id": "sg_event_id",
			    "sg_message_id": "sg_message_id",
			    "response": "400 try again later",
			    "attempt": "5"
			  },
			  {
			    "email": "example@test.com",
			    "timestamp": 1513299569,
			    "smtp-id": "<14c5d75ce93.dfd.64b469@ismtpd-555>",
			    "event": "delivered",
			    "category": "cat facts",
			    "sg_event_id": "sg_event_id",
			    "sg_message_id": "sg_message_id",
			    "response": "250 OK"
			  },
			  {
			    "email": "example@test.com",
			    "timestamp": 1513299569,
			    "smtp-id": "<14c5d75ce93.dfd.64b469@ismtpd-555>",
			    "event": "open",
			    "category": "cat facts",
			    "sg_event_id": "sg_event_id",
			    "sg_message_id": "sg_message_id",
			    "useragent": "Mozilla/4.0 (compatible; MSIE 6.1; Windows XP; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
			    "ip": "255.255.255.255"
			  },
			  {
			    "email": "example@test.com",
			    "timestamp": 1513299569,
			    "smtp-id": "<14c5d75ce93.dfd.64b469@ismtpd-555>",
			    "event": "click",
			    "category": "cat facts",
			    "sg_event_id": "sg_event_id",
			    "sg_message_id": "sg_message_id",
			    "useragent": "Mozilla/4.0 (compatible; MSIE 6.1; Windows XP; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
			    "ip": "255.255.255.255",
			    "url": "http://www.sendgrid.com/"
			  },
			  {
			    "email": "example@test.com",
			    "timestamp": 1513299569,
			    "smtp-id": "<14c5d75ce93.dfd.64b469@ismtpd-555>",
			    "event": "bounce",
			    "category": "cat facts",
			    "sg_event_id": "sg_event_id",
			    "sg_message_id": "sg_message_id",
			    "reason": "500 unknown recipient",
			    "status": "5.0.0"
			  },
			  {
			    "email": "example@test.com",
			    "timestamp": 1513299569,
			    "smtp-id": "<14c5d75ce93.dfd.64b469@ismtpd-555>",
			    "event": "dropped",
			    "category": "cat facts",
			    "sg_event_id": "sg_event_id",
			    "sg_message_id": "sg_message_id",
			    "reason": "Bounced Address",
			    "status": "5.0.0"
			  },
			  {
			    "email": "example@test.com",
			    "timestamp": 1513299569,
			    "smtp-id": "<14c5d75ce93.dfd.64b469@ismtpd-555>",
			    "event": "spamreport",
			    "category": "cat facts",
			    "sg_event_id": "sg_event_id",
			    "sg_message_id": "sg_message_id"
			  },
			  {
			    "email": "example@test.com",
			    "timestamp": 1513299569,
			    "smtp-id": "<14c5d75ce93.dfd.64b469@ismtpd-555>",
			    "event": "unsubscribe",
			    "category": "cat facts",
			    "sg_event_id": "sg_event_id",
			    "sg_message_id": "sg_message_id"
			  },
			  {
			    "email": "example@test.com",
			    "timestamp": 1513299569,
			    "smtp-id": "<14c5d75ce93.dfd.64b469@ismtpd-555>",
			    "event": "group_unsubscribe",
			    "category": "cat facts",
			    "sg_event_id": "sg_event_id",
			    "sg_message_id": "sg_message_id",
			    "useragent": "Mozilla/4.0 (compatible; MSIE 6.1; Windows XP; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
			    "ip": "255.255.255.255",
			    "url": "http://www.sendgrid.com/",
			    "asm_group_id": 10
			  },
			  {
			    "email": "example@test.com",
			    "timestamp": 1513299569,
			    "smtp-id": "<14c5d75ce93.dfd.64b469@ismtpd-555>",
			    "event": "group_resubscribe",
			    "category": "cat facts",
			    "sg_event_id": "sg_event_id",
			    "sg_message_id": "sg_message_id",
			    "useragent": "Mozilla/4.0 (compatible; MSIE 6.1; Windows XP; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
			    "ip": "000.000.000.000",
			    "url": "http://www.sendgrid.com/",
			    "asm_group_id": 10
			  }
			]		   
		   */
		  System.out.println("jsonarray.get(0).toString():"+jsonarray.get(0).toString()); //fetch first document of the JSON array,convert it to string
		  //System.out.println("jsonarray.getJSONObject(0).get(\"event\"):"+jsonarray.getJSONObject(0).get("event")); //fetch 'event' key of first document of JSON array(already converted to string)
		  
		  /*mdb = db.getDBConnection("video");		  
		  collection = mdb.getCollection("movieDetails");*/		  
		  		  
		  //insert document in collection SendGrid.EventWebhook steps:-		  
		  //first: switch to db SendGrid
		  //second: get the collection in which to insert [EventWebhook]
		  //third: create document with key/value pairs. append more key/value pairs to the document if required
		  //if the document is already available as jsonarray[like as in request body parameter], 
		  //convert it to document
		  //fourth: insert document into collection EventWebhook
		  mdb = db.getDBConnection("SendGrid");
		  collection = mdb.getCollection("EventWebhook");		  
		  /*//insert one document
		  Document doc = Document.parse( jsonarray.get(0).toString()); 
		  collection.insertOne(doc);
		  */		  
		  //insert many documents
		  List<Document> doclist = new ArrayList<Document>();
		  for (int i=0; i<jsonarray.length(); i++) {
			  doclist.add( Document.parse(jsonarray.get(i).toString()) );
		  }
		  collection.insertMany(doclist);		 		  
		
		System.out.println("collection.countDocuments(): "+collection.countDocuments());
		System.out.println("outside");		
		response.setStatus(200);
		//doGet(request, response);
	}

}
