package com.driving_school_hibernate.dao.custom.impl;

import com.driving_school_hibernate.config.FactoryConfiguration;
import com.driving_school_hibernate.dao.custom.UsersDAO;
import com.driving_school_hibernate.entity.UsersEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UsersDAOImpl implements UsersDAO {

    @Override
    public boolean save(UsersEntity entity) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.persist(entity);
        tx.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(UsersEntity entity) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.merge(entity);
        tx.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        UsersEntity user = session.get(UsersEntity.class, id);
        if (user != null) {
            session.remove(user);
            tx.commit();
            session.close();
            return true;
        }
        tx.rollback();
        session.close();
        return false;
    }

    @Override
    public Optional<UsersEntity> findById(String id) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        UsersEntity entity = session.get(UsersEntity.class, id);
        session.close();
        return Optional.ofNullable(entity);
    }

    @Override
    public List<UsersEntity> findAll() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<UsersEntity> list = session.createQuery("FROM UsersEntity", UsersEntity.class).list();
        session.close();
        return list;
    }

    @Override
    public List<String> findAllIds() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<String> ids = session.createQuery("SELECT u.userId FROM UsersEntity u", String.class).list();
        session.close();
        return ids;
    }

    @Override
    public Optional<String> findLastId() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Query<String> q = session.createQuery("SELECT u.userId FROM UsersEntity u ORDER BY u.userId DESC", String.class);
        q.setMaxResults(1);
        String lastId = q.uniqueResult();
        session.close();
        return Optional.ofNullable(lastId);
    }

    @Override
    public List<UsersEntity> searchByName(String name) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Query<UsersEntity> q = session.createQuery("FROM UsersEntity u WHERE u.username LIKE :name", UsersEntity.class);
        q.setParameter("name", "%" + name + "%");
        List<UsersEntity> result = q.list();
        session.close();
        return result;
    }
}
