package com.lmoder.tennisclub.repositories;

import com.lmoder.tennisclub.models.Customer;
import com.lmoder.tennisclub.models.Reservation;
import com.lmoder.tennisclub.models.TennisCourt;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.Serializable;
import java.util.List;

public abstract class HibernateDAO<T extends Serializable> implements DAOInterface<T, Long>{
    private Class<T> clazz;

    private Transaction currentTransaction;

    protected Session currentSession;

    public final void setClazz(final Class<T> clazzToSet) {
        clazz = clazzToSet;
    }

    public Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }

    public Session openCurrentSessionWithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    public void closeCurrentSession() {
        currentSession.close();
    }

    public void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    private static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(TennisCourt.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Reservation.class);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());

        return configuration.buildSessionFactory(builder.build());
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    @Override
    public void save(T entity) {
        getCurrentSession().persist(entity);
    }
    @Override
    public void update(T entity) {
        getCurrentSession().merge(entity);
    }

    @Override
    public T findById(Long id) {
        return getCurrentSession().get(clazz, id);
    }
    @Override
    public void deleteById(Long id) {
        T entity = findById(id);
        if (entity != null) {
            getCurrentSession().remove(entity);
        }
    }

    @Override
    public List<T> findAll() {
        return getCurrentSession()
                .createQuery("from " + clazz.getName(), clazz).list();
    }
}
