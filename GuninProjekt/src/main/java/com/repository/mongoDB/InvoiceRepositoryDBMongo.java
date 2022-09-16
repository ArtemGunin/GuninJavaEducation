package com.repository.mongoDB;

import com.config.MongoDBConfig;
import com.google.gson.Gson;
import com.model.Invoice;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.repository.InvoiceRepository;
import com.repository.hibernate.InvoiceRepositoryDBHibernate;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InvoiceRepositoryDBMongo implements InvoiceRepository {

    private static final MongoDatabase DATABASE = MongoDBConfig.getMongoDatabase();
    private static final Gson GSON = new Gson();
    private final MongoCollection<Document> collection;
    private static InvoiceRepositoryDBMongo instance;

    public InvoiceRepositoryDBMongo() {
        this.collection = DATABASE.getCollection(Invoice.class.getSimpleName());
    }

    public static InvoiceRepositoryDBMongo getInstance() {
        if (instance == null) {
            instance = new InvoiceRepositoryDBMongo();
        }
        return instance;
    }

    @Override
    public void save(Invoice invoice) {

    }

    @Override
    public void saveAll(List<Invoice> invoices) {

    }

    @Override
    public List<Invoice> getAll() {
        return null;
    }

    @Override
    public Optional<Invoice> getById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Invoice> getByIndex(int index) {
        return Optional.empty();
    }

    @Override
    public boolean hasInvoice(String id) {
        return false;
    }

    @Override
    public boolean update(Invoice invoice) {
        return false;
    }

    @Override
    public boolean updateTime(String id, LocalDateTime time) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public List<Invoice> getInvoiceListWithSumConditions(double sumCondition) {
        return null;
    }

    @Override
    public long getCount() {
        return 0;
    }

    @Override
    public Map<Double, List<Invoice>> groupInvoices() {
        return null;
    }
}
