package com.repository.mongoDB;

import com.config.MongoDBConfig;
import com.google.gson.*;
import com.model.product.Phone;
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

public class PhoneRepositoryDBMongo implements CrudRepository<Phone> {

    private static final MongoDatabase DATABASE = MongoDBConfig.getMongoDatabase();
    private static Gson gson = new Gson();
    private static PhoneRepositoryDBMongo instance;
    private final MongoCollection<Document> collection;

    public PhoneRepositoryDBMongo() {
        this.collection = DATABASE.getCollection(Phone.class.getSimpleName());
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

    public static PhoneRepositoryDBMongo getInstance() {
        if (instance == null) {
            instance = new PhoneRepositoryDBMongo();
        }
        return instance;
    }

    @Override
    public void save(Phone phone) {
        collection.insertOne(Document.parse(gson.toJson(phone)));
    }

    @Override
    public void saveAll(List<Phone> phones) {
        List<Document> documents = phones.stream()
                .map(phone -> Document.parse(gson.toJson(phone)))
                .toList();
        collection.insertMany(documents);
    }

    @Override
    public boolean update(Phone phone) {
        UpdateResult result = collection.replaceOne(
                eq("id", phone.getId()),
                Document.parse(gson.toJson(phone)));
        return result.getModifiedCount() > 0;
    }

    @Override
    public boolean delete(String id) {
        DeleteResult result = collection.deleteOne(eq("id", id));
        return result.getDeletedCount() > 0;
    }

    @Override
    public List<Phone> getAll() {
        return collection.find()
                .map(document -> gson.fromJson(document.toJson(), Phone.class)).into(new ArrayList<>());
    }

    @Override
    public Optional<Phone> getById(String id) {
        return Optional.ofNullable(collection.find(eq("id", id))
                .map(document -> gson.fromJson(document.toJson(), Phone.class)).first());
    }

    @Override
    public Optional<Phone> getByIndex(int index) {
        if (index <= 0 || index > collection.countDocuments()) {
            return Optional.empty();
        }
        return Optional.ofNullable(collection.find().skip(index - 1)
                .map(document -> gson.fromJson(document.toJson(), Phone.class)).first());
    }

    @Override
    public boolean hasProduct(String id) {
        return collection.find(eq("id", id)).first() != null;
    }
}
