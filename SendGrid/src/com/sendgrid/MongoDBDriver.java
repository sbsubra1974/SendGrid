package com.sendgrid;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoDBDriver {

public MongoDatabase getDBConnection(String DBName) {	
	
	/*//good for paid Atlas cluster version
	ConnectionString connString = new ConnectionString(
		    "mongodb://m001-student:m001-mongodb-basics@cluster0-jxeqq.mongodb.net/test"
			
		);
		MongoClientSettings settings = MongoClientSettings.builder()
		    .applyConnectionString(connString)
		    .retryWrites(true)
		    .build();
		MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase database = mongoClient.getDatabase("video");
	*/	
		
	//good for connecting to an Atlas M0 (Free Tier) cluster:- https://docs.atlas.mongodb.com/driver-connection/ :: JAVA(SYNC)
	MongoClientURI uri = new MongoClientURI(
		"mongodb://m001-student:m001-mongodb-basics@sandbox-shard-00-00-lf3ai.mongodb.net:27017,sandbox-shard-00-01-lf3ai.mongodb.net:27017,sandbox-shard-00-02-lf3ai.mongodb.net:27017/local?ssl=true&authSource=admin&retryWrites=true");
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase(DBName);	
	
	return database;
	}
}
