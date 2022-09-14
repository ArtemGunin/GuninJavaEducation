package com.repository.hibernate;

import com.config.HibernateFactoryUtils;
import com.context.Singleton;
import com.model.product.Phone;
import com.repository.CrudRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@Singleton
public class PhoneRepositoryDBHibernate implements CrudRepository<Phone> {

    private static final SessionFactory SESSION_FACTORY = HibernateFactoryUtils.getSessionFactory();

    private static PhoneRepositoryDBHibernate instance;

    public static PhoneRepositoryDBHibernate getInstance() {
        if (instance == null) {
            instance = new PhoneRepositoryDBHibernate();
        }
        return instance;
    }

    @Override
    public void save(Phone phone) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        session.save(phone);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void saveAll(List<Phone> phones) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        for (Phone phone : phones) {
            session.save(phone);
        }
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public boolean update(Phone phone) {
        Session session = SESSION_FACTORY.openSession();
        boolean phoneAbsent = checkProduct(phone.getId(), session);
        if (!phoneAbsent) {
            session.beginTransaction();
            session.update(phone);
            session.getTransaction().commit();
        }
        session.close();
        return !phoneAbsent;
    }

    private boolean checkProduct(String id, Session session) {
        return session.createQuery("select id from Phone p where id = :value")
                .setParameter("value", id)
                .list()
                .isEmpty();
    }

    @Override
    public boolean delete(String id) {
        Session session = SESSION_FACTORY.openSession();
        boolean phoneAbsent = checkProduct(id, session);
        if (!phoneAbsent) {
            session.beginTransaction();
            session.createQuery("delete from Phone where id = :value")
                    .setParameter("value", id)
                    .executeUpdate();
            session.getTransaction().commit();
        }
        session.close();
        return !phoneAbsent;
    }

    @Override
    public List<Phone> getAll() {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        List<Phone> phones = session
                .createQuery("select phone from Phone phone", Phone.class)
                .list();
        session.getTransaction().commit();
        session.close();
        return phones;
    }

    @Override
    public Optional<Phone> getById(String id) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        Optional<Phone> phone = Optional.ofNullable(session.get(Phone.class, id));
        session.getTransaction().commit();
        session.close();
        return phone;
    }

    @Override
    public Optional<Phone> getByIndex(int index) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        List<String> id = session.createQuery("select id from Phone phone", String.class).list();
        Optional<Phone> phone = Optional.empty();
        try {
            phone = Optional.ofNullable(session.get(Phone.class, id.get(id.size() - index)));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        session.getTransaction().commit();
        session.close();
        return phone;
    }

    @Override
    public boolean hasProduct(String id) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        boolean productPresent = !session.createQuery("select id from Phone phone where id = :value")
                .setParameter("value", id)
                .list()
                .isEmpty();
        session.getTransaction().commit();
        session.close();
        return productPresent;
    }
}
