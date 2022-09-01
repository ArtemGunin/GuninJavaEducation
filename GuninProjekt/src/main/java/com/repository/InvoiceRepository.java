package com.repository;

import com.model.Invoice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InvoiceRepository {

    void save(Invoice invoice);

    void saveAll(List<Invoice> invoices);

    List<Invoice> getAll();

    Optional<Invoice> getById(String id);

    Optional<Invoice> getByIndex(int index);

    boolean hasInvoice(String id);

    boolean update(Invoice invoice);

    boolean updateTime(String id, LocalDateTime time);

    boolean delete(String id);

    List<Invoice> getInvoiceListWithSumConditions(double sumCondition);

    long getCount();

    Map<Double, List<Invoice>> groupInvoices();
}
