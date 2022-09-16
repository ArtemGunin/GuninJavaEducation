package com.repository.mongoDB;

import com.config.MongoDBConfig;
import com.google.gson.Gson;
import com.model.product.Phone;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.repository.CrudRepository;
import org.bson.Document;

import java.util.List;
import java.util.Optional;

public class PhoneRepositoryDBMongo implements CrudRepository<Phone> {

    private static final MongoDatabase DATABASE = MongoDBConfig.getMongoDatabase();
    private static final Gson GSON = new Gson();
    private final MongoCollection<Document> collection;
    private static PhoneRepositoryDBMongo instance;

    public PhoneRepositoryDBMongo() {
        this.collection = DATABASE.getCollection(Phone.class.getSimpleName());
    }

    public static PhoneRepositoryDBMongo getInstance() {
        if (instance == null) {
            instance = new PhoneRepositoryDBMongo();
        }
        return instance;
    }

    @Override
    public void save(Phone phone) {
        collection.insertOne(Document.parse(GSON.toJson(phone)));
    }

    @Override
    public void saveAll(List<Phone> phones) {
        List<Document> documents = phones.stream()
                .map(phone -> Document.parse(GSON.toJson(phone)))
                .toList();
        collection.insertMany(documents);
    }

    @Override
    public boolean update(Phone phone) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public List<Phone> getAll() {
        return null;
    }

    @Override
    public Optional<Phone> getById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Phone> getByIndex(int index) {
        return Optional.empty();
    }

    @Override
    public boolean hasProduct(String id) {
        return false;
    }
}
