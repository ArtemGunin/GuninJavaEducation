package com.repository.mongoDB;

import com.config.MongoDBConfig;
import com.google.gson.*;
import com.model.product.TV;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.repository.CrudRepository;
import javassist.Modifier;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class TVRepositoryDBMongo implements CrudRepository<TV> {

    private static final MongoDatabase DATABASE = MongoDBConfig.getMongoDatabase();
    private static Gson gson = new Gson();
    private static TVRepositoryDBMongo instance;
    private final MongoCollection<Document> collection;

    public TVRepositoryDBMongo() {
        this.collection = DATABASE.getCollection(TV.class.getSimpleName());
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (localDateTime, type, jsonSerializationContext)
                                -> new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext)
                                -> LocalDateTime.parse(json.getAsString() + " 00:00",
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withLocale(Locale.ENGLISH)))
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .create();
    }

    public static TVRepositoryDBMongo getInstance() {
        if (instance == null) {
            instance = new TVRepositoryDBMongo();
        }
        return instance;
    }

    @Override
    public void save(TV tv) {
        collection.insertOne(Document.parse(gson.toJson(tv)));
    }

    @Override
    public void saveAll(List<TV> tvs) {
        List<Document> documents = tvs.stream()
                .map(tv -> Document.parse(gson.toJson(tv))).toList();
        collection.insertMany(documents);
    }

    @Override
    public boolean update(TV tv) {
        UpdateResult result = collection.replaceOne(
                eq("id", tv.getId()),
                Document.parse(gson.toJson(tv)));
        return result.getModifiedCount() > 0;
    }

    @Override
    public boolean delete(String id) {
        DeleteResult result = collection.deleteOne(eq("id", id));
        return result.getDeletedCount() > 0;
    }

    @Override
    public List<TV> getAll() {
        return collection.find()
                .map(document -> gson.fromJson(document.toJson(), TV.class)).into(new ArrayList<>());
    }

    @Override
    public Optional<TV> getById(String id) {
        return Optional.ofNullable(collection.find(eq("id", id))
                .map(document -> gson.fromJson(document.toJson(), TV.class)).first());
    }

    @Override
    public Optional<TV> getByIndex(int index) {
        if (index <= 0 || index > collection.countDocuments()) {
            return Optional.empty();
        }
        return Optional.ofNullable(collection.find().skip(index - 1)
                .map(document -> gson.fromJson(document.toJson(), TV.class)).first());
    }

    @Override
    public boolean hasProduct(String id) {
        return collection.find(eq("id", id)).first() != null;
    }
}
