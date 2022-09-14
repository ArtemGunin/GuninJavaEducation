package com.repository.hibernate;

import com.config.HibernateFactoryUtils;
import com.context.Singleton;
import com.model.product.Toaster;
import com.repository.CrudRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@Singleton
public class ToasterRepositoryDBHibernate implements CrudRepository<Toaster> {

    private static final SessionFactory SESSION_FACTORY = HibernateFactoryUtils.getSessionFactory();

    private static ToasterRepositoryDBHibernate instance;

    public static ToasterRepositoryDBHibernate getInstance() {
        if (instance == null) {
            instance = new ToasterRepositoryDBHibernate();
        }
        return instance;
    }

    @Override
    public void save(Toaster toaster) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        session.save(toaster);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void saveAll(List<Toaster> toasters) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        for (Toaster toaster : toasters) {
            session.save(toaster);
        }
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public boolean update(Toaster toaster) {
        Session session = SESSION_FACTORY.openSession();
        boolean toasterAbsent = checkProduct(toaster.getId(), session);
        if (!toasterAbsent) {
            session.beginTransaction();
            session.update(toaster);
            session.getTransaction().commit();
        }
        session.close();
        return !toasterAbsent;
    }

    private boolean checkProduct(String id, Session session) {
        return session.createQuery("select id from Toaster t where id = :value")
                .setParameter("value", id)
                .list()
                .isEmpty();
    }

    @Override
    public boolean delete(String id) {
        Session session = SESSION_FACTORY.openSession();
        boolean toasterAbsent = checkProduct(id, session);
        if (!toasterAbsent) {
            session.beginTransaction();
            session.createQuery("delete from Toaster where id = :value")
                    .setParameter("value", id)
                    .executeUpdate();
            session.getTransaction().commit();
        }
        session.close();
        return !toasterAbsent;
    }

    @Override
    public List<Toaster> getAll() {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        List<Toaster> toasters = session
                .createQuery("select toaster from Toaster toaster", Toaster.class)
                .list();
        session.getTransaction().commit();
        session.close();
        return toasters;
    }

    @Override
    public Optional<Toaster> getById(String id) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        Optional<Toaster> toaster = Optional.ofNullable(session.get(Toaster.class, id));
        session.getTransaction().commit();
        session.close();
        return toaster;
    }

    @Override
    public Optional<Toaster> getByIndex(int index) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        List<String> id = session.createQuery("select id from Toaster toaster", String.class).list();
        Optional<Toaster> toaster = Optional.empty();
        try {
            toaster = Optional.ofNullable(session.get(Toaster.class, id.get(id.size() - index)));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        session.getTransaction().commit();
        session.close();
        return toaster;
    }

    @Override
    public boolean hasProduct(String id) {
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        boolean productPresent = !session.createQuery("select id from Toaster toaster where id = :value")
                .setParameter("value", id)
                .list()
                .isEmpty();
        session.getTransaction().commit();
        session.close();
        return productPresent;
    }
}
