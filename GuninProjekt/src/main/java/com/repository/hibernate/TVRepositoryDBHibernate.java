package com.repository.hibernate;

import com.config.HibernateFactoryUtils;
import com.context.Singleton;
import com.model.product.TV;
import com.repository.CrudRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@Singleton
public class TVRepositoryDBHibernate implements CrudRepository<TV> {

    private static final SessionFactory SESSION_FACTORY = HibernateFactoryUtils.getSessionFactory();

    private static TVRepositoryDBHibernate instance;

    public static TVRepositoryDBHibernate getInstance() {
        if (instance == null) {
            instance = new TVRepositoryDBHibernate();
        }
        return instance;
    }

    @Override
    public void save(TV tv) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        session.save(tv);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void saveAll(List<TV> tvs) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        for (TV tv : tvs) {
            session.save(tv);
        }
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public boolean update(TV tv) {
        Session session = SESSION_FACTORY.openSession();
        boolean tvAbsent = checkProduct(tv.getId(), session);
        if (!tvAbsent) {
            session.beginTransaction();
            session.update(tv);
            session.getTransaction().commit();
        }
        session.close();
        return !tvAbsent;
    }

    private boolean checkProduct(String id, Session session) {
        return session.createQuery("select id from TV tv where id = :value")
                .setParameter("value", id)
                .list()
                .isEmpty();
    }

    @Override
    public boolean delete(String id) {
        Session session = SESSION_FACTORY.openSession();
        boolean tvAbsent = checkProduct(id, session);
        if (!tvAbsent) {
            session.beginTransaction();
            session.createQuery("delete from TV where id = :value")
                    .setParameter("value", id)
                    .executeUpdate();
            session.getTransaction().commit();
        }
        session.close();
        return !tvAbsent;
    }

    @Override
    public List<TV> getAll() {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        List<TV> tvs = session
                .createQuery("select tv from TV tv", TV.class)
                .list();
        session.getTransaction().commit();
        session.close();
        return tvs;
    }

    @Override
    public Optional<TV> getById(String id) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        Optional<TV> tv = Optional.ofNullable(session.get(TV.class, id));
        session.getTransaction().commit();
        session.close();
        return tv;
    }

    @Override
    public Optional<TV> getByIndex(int index) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        List<String> id = session.createQuery("select id from TV tv", String.class).list();
        Optional<TV> tv = Optional.empty();
        try {
            tv = Optional.ofNullable(session.get(TV.class, id.get(id.size() - index)));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        session.getTransaction().commit();
        session.close();
        return tv;
    }

    @Override
    public boolean hasProduct(String id) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        boolean productPresent = !session.createQuery("select id from TV tv where id = :value")
                .setParameter("value", id)
                .list()
                .isEmpty();
        session.getTransaction().commit();
        session.close();
        return productPresent;
    }
}
