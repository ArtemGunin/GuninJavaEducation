package com.repository.mongoDB;

import com.config.MongoDBConfig;
import com.google.gson.*;
import com.model.product.Toaster;
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

public class ToasterRepositoryDBMongo implements CrudRepository<Toaster> {

    private static final MongoDatabase DATABASE = MongoDBConfig.getMongoDatabase();
    private static Gson gson = new Gson();
    private static ToasterRepositoryDBMongo instance;
    private final MongoCollection<Document> collection;

    public ToasterRepositoryDBMongo() {
        this.collection = DATABASE.getCollection(Toaster.class.getSimpleName());
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

    public static ToasterRepositoryDBMongo getInstance() {
        if (instance == null) {
            instance = new ToasterRepositoryDBMongo();
        }
        return instance;
    }

    @Override
    public void save(Toaster toaster) {
        collection.insertOne(Document.parse(gson.toJson(toaster)));
    }

    @Override
    public void saveAll(List<Toaster> toasters) {
        List<Document> documents = toasters.stream()
                .map(toaster -> Document.parse(gson.toJson(toaster))).toList();
        collection.insertMany(documents);
    }

    @Override
    public boolean update(Toaster toaster) {
        UpdateResult result = collection.replaceOne(
                eq("id", toaster.getId()),
                Document.parse(gson.toJson(toaster)));
        return result.getModifiedCount() > 0;
    }

    @Override
    public boolean delete(String id) {
        DeleteResult result = collection.deleteOne(eq("id", id));
        return result.getDeletedCount() > 0;
    }

    @Override
    public List<Toaster> getAll() {
        return collection.find()
                .map(document -> gson.fromJson(document.toJson(), Toaster.class)).into(new ArrayList<>());
    }

    @Override
    public Optional<Toaster> getById(String id) {
        return Optional.ofNullable(collection.find(eq("id", id))
                .map(document -> gson.fromJson(document.toJson(), Toaster.class)).first());
    }

    @Override
    public Optional<Toaster> getByIndex(int index) {
        if (index <= 0 || index > collection.countDocuments()) {
            return Optional.empty();
        }
        return Optional.ofNullable(collection.find().skip(index - 1)
                .map(document -> gson.fromJson(document.toJson(), Toaster.class)).first());
    }

    @Override
    public boolean hasProduct(String id) {
        return collection.find(eq("id", id)).first() != null;
    }
}
