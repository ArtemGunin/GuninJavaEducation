package com.repository.hibernate;

import com.config.HibernateFactoryUtils;
import com.context.Singleton;
import com.model.Invoice;
import com.repository.InvoiceRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class InvoiceRepositoryDBHibernate implements InvoiceRepository {

    private static final SessionFactory SESSION_FACTORY = HibernateFactoryUtils.getSessionFactory();

    private static InvoiceRepositoryDBHibernate instance;

    public static InvoiceRepositoryDBHibernate getInstance() {
        if (instance == null) {
            instance = new InvoiceRepositoryDBHibernate();
        }
        return instance;
    }

    @Override
    public void save(Invoice invoice) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        session.save(invoice);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void saveAll(List<Invoice> invoices) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        for (Invoice invoice : invoices) {
            session.save(invoice);
        }
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<Invoice> getAll() {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        List<Invoice> invoices = session
                .createQuery("select invoice from Invoice invoice", Invoice.class)
                .list();
        session.getTransaction().commit();
        session.close();
        return invoices;
    }

    @Override
    public Optional<Invoice> getById(String id) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        Optional<Invoice> invoice = Optional.ofNullable(session.get(Invoice.class, id));
        session.getTransaction().commit();
        session.close();
        return invoice;
    }

    @Override
    public Optional<Invoice> getByIndex(int index) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        List<String> id = session.createQuery("select id from Invoice invoice", String.class).list();
        Optional<Invoice> invoice = Optional.empty();
        try {
            invoice = Optional.ofNullable(session.get(Invoice.class, id.get(id.size() - index)));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        session.getTransaction().commit();
        session.close();
        return invoice;
    }

    @Override
    public boolean hasInvoice(String id) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        boolean productPresent = !session.createQuery("select id from Invoice invoice where id = :value")
                .setParameter("value", id)
                .list()
                .isEmpty();
        session.getTransaction().commit();
        session.close();
        return productPresent;
    }

    @Override
    public boolean update(Invoice invoice) {
        Session session = SESSION_FACTORY.openSession();
        boolean invoiceAbsent = checkProduct(invoice.getId(), session);
        if (!invoiceAbsent) {
            session.beginTransaction();
            session.update(invoice);
            session.getTransaction().commit();
        }
        session.close();
        return !invoiceAbsent;
    }

    private boolean checkProduct(String id, Session session) {
        return session.createQuery("select id from Invoice i where id = :value")
                .setParameter("value", id)
                .list()
                .isEmpty();
    }

    @Override
    public boolean delete(String id) {
        Session session = SESSION_FACTORY.openSession();
        boolean invoiceAbsent = checkProduct(id, session);
        if (!invoiceAbsent) {
            session.beginTransaction();
            session.createQuery("delete from Invoice where id = :value")
                    .setParameter("value", id)
                    .executeUpdate();
            session.getTransaction().commit();
        }
        session.close();
        return !invoiceAbsent;
    }

    @Override
    public boolean updateTime(String id, LocalDateTime time) {
        Session session = SESSION_FACTORY.openSession();
        boolean invoiceAbsent = checkProduct(id, session);
        if (!invoiceAbsent) {
            session.beginTransaction();
            session.createQuery("update Invoice set time = :value where id = :id")
                    .setParameter("value", time)
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
        }
        session.close();
        return !invoiceAbsent;
    }

    @Override
    public List<Invoice> getInvoiceListWithSumConditions(double sumCondition) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        List<Invoice> invoices = session
                .createQuery("select invoice from Invoice invoice where invoice.sum > :sum", Invoice.class)
                .setParameter("sum", sumCondition)
                .list();
        session.getTransaction().commit();
        session.close();
        return invoices;
    }

    @Override
    public long getCount() {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        long count = (long) session.createQuery("select count(id) from Invoice").getSingleResult();
        session.getTransaction().commit();
        session.close();
        return count;
    }

    @Override
    public Map<Double, List<Invoice>> groupInvoices() {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        String hqlGroupInvoiceBySum = "select invoice from Invoice invoice";
        Map<Double, List<Invoice>> groups = session.createQuery(hqlGroupInvoiceBySum, Invoice.class)
                .getResultList()
                .stream()
                .collect(Collectors.groupingBy(Invoice::getSum));
        session.getTransaction().commit();
        session.close();
        return groups;
    }
}
