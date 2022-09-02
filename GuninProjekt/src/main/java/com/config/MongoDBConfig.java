package com.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBConfig {

    private static final String URL = "localhost";
    private static final int PORT = 27017;
    private static final String DATABASE = "storage";
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;

    private MongoDBConfig() {
    }

    public static MongoDatabase getMongoDatabase() {
        if (mongoClient == null) {
            mongoClient = new MongoClient(URL, PORT);
            mongoDatabase = mongoClient.getDatabase(DATABASE);
            mongoDatabase.drop();
        }

        return mongoDatabase;
    }
}
