package com.driving_school_hibernate.dao.custom.impl;

import com.driving_school_hibernate.config.FactoryConfiguration;
import com.driving_school_hibernate.dao.custom.PaymentDAO;
import com.driving_school_hibernate.entity.PaymentEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public boolean save(PaymentEntity entity) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.save(entity);
        tx.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(PaymentEntity entity) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.update(entity);
        tx.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        PaymentEntity payment = session.get(PaymentEntity.class, id);
        if (payment != null) {
            session.delete(payment);
            tx.commit();
            session.close();
            return true;
        }
        session.close();
        return false;
    }

    @Override
    public Optional<PaymentEntity> findById(String id) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        PaymentEntity payment = session.get(PaymentEntity.class, id);
        session.close();
        return Optional.ofNullable(payment);
    }

    @Override
    public List<PaymentEntity> findAll() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<PaymentEntity> list = session.createQuery("from PaymentEntity", PaymentEntity.class).list();
        session.close();
        return list;
    }

    @Override
    public List<String> findAllIds() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<String> list = session.createQuery("select p.paymentId from PaymentEntity p", String.class).list();
        session.close();
        return list;
    }

    @Override
    public Optional<String> findLastId() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Query<String> query = session.createQuery("select p.paymentId from PaymentEntity p order by p.paymentId desc", String.class);
        query.setMaxResults(1);
        String lastId = query.uniqueResult();
        session.close();
        return Optional.ofNullable(lastId);
    }

    @Override
    public List<PaymentEntity> searchByName(String name) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Query<PaymentEntity> query = session.createQuery(
                "from PaymentEntity p where lower(p.student.firstName) like :name or lower(p.student.lastName) like :name",
                PaymentEntity.class
        );
        query.setParameter("name", "%" + name.toLowerCase() + "%");
        List<PaymentEntity> list = query.list();
        session.close();
        return list;
    }
}
