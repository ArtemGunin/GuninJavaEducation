package com.repository.mongoDB;

import com.config.MongoDBConfig;
import com.context.Singleton;
import com.google.gson.Gson;
import com.model.product.TV;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.repository.CrudRepository;
import org.bson.Document;

import java.util.List;
import java.util.Optional;

@Singleton
public class TVRepositoryDBMongo implements CrudRepository<TV> {

    private static final MongoDatabase DATABASE = MongoDBConfig.getMongoDatabase();
    private static final Gson GSON = new Gson();
    private final MongoCollection<Document> collection;
    private static TVRepositoryDBMongo instance;

    public TVRepositoryDBMongo() {
        this.collection = DATABASE.getCollection(TVRepositoryDBMongo.class.getSimpleName());
    }

    public static TVRepositoryDBMongo getInstance() {
        if (instance == null) {
            instance = new TVRepositoryDBMongo();
        }
        return instance;
    }

    @Override
    public void save(TV tv) {

    }

    @Override
    public void saveAll(List<TV> products) {

    }

    @Override
    public boolean update(TV tv) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public List<TV> getAll() {
        return null;
    }

    @Override
    public Optional<TV> getById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<TV> getByIndex(int index) {
        return Optional.empty();
    }

    @Override
    public boolean hasProduct(String id) {
        return false;
    }
}
