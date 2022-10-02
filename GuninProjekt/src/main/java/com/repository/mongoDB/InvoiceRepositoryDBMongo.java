package com.repository.mongoDB;

import com.config.MongoDBConfig;
import com.google.gson.*;
import com.model.Invoice;
import com.model.product.Phone;
import com.model.product.TV;
import com.model.product.Toaster;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.repository.InvoiceRepository;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

public class InvoiceRepositoryDBMongo implements InvoiceRepository {

    private static final MongoDatabase DATABASE = MongoDBConfig.getMongoDatabase();
    private static InvoiceRepositoryDBMongo instance;
    private final Gson gson;
    private final MongoCollection<Document> collection;

    public InvoiceRepositoryDBMongo() {
        this.collection = DATABASE.getCollection(Invoice.class.getSimpleName());
        gson = new GsonBuilder()
                .registerTypeAdapter(
                        LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (localDateTime, type, jsonSerializationContext)
                                -> new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .registerTypeAdapter(
                        LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                                LocalDateTime.parse(
                                        json.getAsString() + " 00:00",
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withLocale(Locale.ENGLISH)))
                .create();

    }

    public static InvoiceRepositoryDBMongo getInstance() {
        if (instance == null) {
            instance = new InvoiceRepositoryDBMongo();
        }
        return instance;
    }

    private void setProductsToInvoice(Invoice invoice) {
        invoice.setProducts(invoice.getProductIds().stream()
                .map(productId -> {
                    Optional<Phone> phone = PhoneRepositoryDBMongo.getInstance().getById(productId);
                    if (phone.isPresent()) {
                        return phone.get();
                    }
                    Optional<Toaster> toaster = ToasterRepositoryDBMongo.getInstance().getById(productId);
                    if (toaster.isPresent()) {
                        return toaster.get();
                    }
                    Optional<TV> tv = TVRepositoryDBMongo.getInstance().getById(productId);
                    if (tv.isPresent()) {
                        return tv.get();
                    }
                    throw new IllegalArgumentException("Product is absent");
                })
                .toList());
    }

    @Override
    public void save(Invoice invoice) {
        collection.insertOne(Document.parse(gson.toJson(invoice)));
    }

    @Override
    public void saveAll(List<Invoice> invoices) {
        List<Document> documents = invoices.stream()
                .map(invoice -> Document.parse(gson.toJson(invoice))).toList();
        collection.insertMany(documents);
    }

    @Override
    public List<Invoice> getAll() {
        List<Invoice> invoiceList = collection.find()
                .map(document -> gson.fromJson(document.toJson(), Invoice.class))
                .into(new ArrayList<>());
        invoiceList.forEach(this::setProductsToInvoice);
        return invoiceList;
    }

    @Override
    public Optional<Invoice> getById(String id) {
        Optional<Invoice> invoice = Optional.ofNullable(collection.find(eq("id", id))
                .map(document -> gson.fromJson(document.toJson(), Invoice.class)).first());
        setProductsToInvoice(invoice.orElseThrow());
        return invoice;
    }

    @Override
    public Optional<Invoice> getByIndex(int index) {
        if (index <= 0 || index > collection.countDocuments()) {
            return Optional.empty();
        }
        Optional<Invoice> invoice = Optional.ofNullable(collection.find().skip(index - 1)
                .map(document -> gson.fromJson(document.toJson(), Invoice.class)).first());
        setProductsToInvoice(invoice.orElseThrow());
        return invoice;
    }

    @Override
    public boolean hasInvoice(String id) {
        return collection.find(eq("id", id)).first() != null;
    }

    @Override
    public boolean update(Invoice invoice) {
        UpdateResult result = collection.replaceOne(
                eq("id", invoice.getId()),
                Document.parse(gson.toJson(invoice)));
        return result.getModifiedCount() > 0;
    }

    @Override
    public boolean updateTime(String id, LocalDateTime time) {
        collection.findOneAndUpdate(eq("_id", id), Updates.set("time", time));
        return true;
    }

    @Override
    public boolean delete(String id) {
        DeleteResult result = collection.deleteOne(eq("id", id));
        return result.getDeletedCount() > 0;
    }

    @Override
    public List<Invoice> getInvoiceListWithSumConditions(double sumCondition) {
        List<Invoice> invoices = collection.find(gt("sum", sumCondition))
                .map(document -> gson.fromJson(document.toJson(), Invoice.class))
                .into(new ArrayList<>());
        invoices.forEach(this::setProductsToInvoice);
        return invoices;
    }

    @Override
    public long getCount() {
        return collection.countDocuments();
    }

    @Override
    public Map<Double, List<Invoice>> groupInvoices() {
        Map<Double, List<Invoice>> result = new TreeMap<>();
        collection.aggregate(List.of(Aggregates.group("$sum", Accumulators.sum("count", 1))))
                .map(document -> gson.fromJson(document.toJson(), JsonObject.class))
                .forEach((Consumer<? super JsonObject>) jsonObject -> {
                    double sum = jsonObject.get("_id").getAsDouble();
                    List<Invoice> invoices = collection.find(eq("sum", sum))
                            .map(document -> gson.fromJson(document.toJson(), Invoice.class))
                            .into(new ArrayList<>());
                    result.put(sum, invoices);
                });
        return result;
    }
}
