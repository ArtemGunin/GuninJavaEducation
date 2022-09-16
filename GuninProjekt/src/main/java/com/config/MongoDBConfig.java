package com.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBConfig {

    private static final String URL = "ac-qvvnmad-shard-00-00.fmbcqln.mongodb.net";
    private static final int PORT = 27017;
    public static final String DATABASE = "storage";
    static MongoClient mongoClient;
    static MongoDatabase mongoDatabase;

    public static MongoDatabase getMongoDatabase() {
        if (mongoClient == null) {
            mongoClient = new MongoClient(URL, PORT);
            mongoDatabase = mongoClient.getDatabase(DATABASE);
            mongoDatabase.drop();
        }
        return mongoDatabase;
    }
}
