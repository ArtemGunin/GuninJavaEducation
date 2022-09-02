package com.repository.mongoDB;

import com.config.MongoDBConfig;
import com.google.gson.Gson;
import com.model.product.Toaster;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.repository.CrudRepository;
import org.bson.Document;

import java.util.List;
import java.util.Optional;

public class ToasterRepositoryDBMongo implements CrudRepository<Toaster> {

    private static final MongoDatabase DATABASE = MongoDBConfig.getMongoDatabase();
    private static final Gson GSON = new Gson();
    private final MongoCollection<Document> collection;
    private static ToasterRepositoryDBMongo instance;

    public ToasterRepositoryDBMongo() {
        this.collection = DATABASE.getCollection(ToasterRepositoryDBMongo.class.getSimpleName());
    }

    public static ToasterRepositoryDBMongo getInstance() {
        if (instance == null) {
            instance = new ToasterRepositoryDBMongo();
        }
        return instance;
    }

    @Override
    public void save(Toaster toaster) {

    }

    @Override
    public void saveAll(List<Toaster> products) {

    }

    @Override
    public boolean update(Toaster toaster) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public List<Toaster> getAll() {
        return null;
    }

    @Override
    public Optional<Toaster> getById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Toaster> getByIndex(int index) {
        return Optional.empty();
    }

    @Override
    public boolean hasProduct(String id) {
        return false;
    }
}
