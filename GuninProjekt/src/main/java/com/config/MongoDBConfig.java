package com.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBConfig {

    public static final String DATABASE = "storage";
    private static final String URL = "localhost";
    private static final int PORT = 27017;
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
