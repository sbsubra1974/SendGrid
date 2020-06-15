package com.sendgrid;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("inside");
		// TODO Auto-generated method stub		
		/*
		Enumeration<String> parameterNames = request.getParameterNames();
		int i =0;
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
           System.out.println("EventWebhook parameter"+i+++" "+paramName+":"+request.getParameter(paramName));
           }
        */ 	
		
		StringBuffer jb = new StringBuffer();
		JSONObject jsonObject;
		JSONArray jsonarray;
		  String line = null;
		  try {
		    BufferedReader reader = request.getReader(); 
		    while ((line = reader.readLine()) != null) //read POST request body parameters
		      jb.append(line);	//line by line
		  } catch (Exception e) { /*report an error*/ }

		  System.out.println("jb: "+jb.toString());
		 	  
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
		  System.out.println("jsonarray.getJSONObject(0).get(\"event\"):"+jsonarray.getJSONObject(0).get("event")); //fetch 'event' key of first document of JSON array(already converted to string)
		  
		  MongoDBDriver db = new MongoDBDriver();		  
		  MongoDatabase mdb = db.getDBConnection("video");		  
		  MongoCollection<Document> collection = mdb.getCollection("movieDetails");		  
		  System.out.println("collection.countDocuments(): "+collection.countDocuments());
		  
		  //insert document in collection SendGrid.EventWebhook steps:-		  
		  //first: switch to db SendGrid
		  //second: get the collection in which to insert [EventWebhook]
		  //third: create document with key/value pairs. append more key/value pairs to the document if required
		  //if the document is already available as jsonarray[like as in request body parameter], 
		  //convert it to document
		  //fourth: insert document into collection EventWebhook
		  mdb = db.getDBConnection("SendGrid");
		  collection = mdb.getCollection("EventWebhook");
		  
		  /*insert one document
		  Document doc = Document.parse( jsonarray.get(0).toString()); 
		  collection.insertOne(doc);
		  */		  
		  //insert many documents
		  List<Document> doclist = new ArrayList<Document>();
		  for (int i=0; i<jsonarray.length(); i++) {
			  doclist.add( Document.parse(jsonarray.get(i).toString()) );
		  }
		  collection.insertMany(doclist);
		 		  
  
		System.out.println("outside");
		response.setStatus(200);
		//doGet(request, response);
	}

}
