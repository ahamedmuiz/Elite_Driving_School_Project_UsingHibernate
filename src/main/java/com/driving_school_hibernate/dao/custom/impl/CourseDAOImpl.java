package com.driving_school_hibernate.dao.custom.impl;

import com.driving_school_hibernate.config.FactoryConfiguration;
import com.driving_school_hibernate.dao.custom.CourseDAO;
import com.driving_school_hibernate.entity.CourseEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CourseDAOImpl implements CourseDAO {

    @Override
    public boolean save(CourseEntity entity) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.save(entity);
        tx.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(CourseEntity entity) throws SQLException {
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
        CourseEntity entity = session.get(CourseEntity.class, id);
        if (entity != null) {
            session.delete(entity);
            tx.commit();
            session.close();
            return true;
        }
        session.close();
        return false;
    }

    @Override
    public Optional<CourseEntity> findById(String id) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        CourseEntity entity = session.get(CourseEntity.class, id);
        session.close();
        return Optional.ofNullable(entity);
    }

    @Override
    public List<CourseEntity> findAll() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<CourseEntity> list = session.createQuery("FROM CourseEntity", CourseEntity.class).list();
        session.close();
        return list;
    }

    @Override
    public List<String> findAllIds() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<String> ids = session.createQuery("SELECT c.courseId FROM CourseEntity c", String.class).list();
        session.close();
        return ids;
    }

    @Override
    public Optional<String> findLastId() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        String lastId = session.createQuery("SELECT c.courseId FROM CourseEntity c ORDER BY c.courseId DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
        session.close();
        return Optional.ofNullable(lastId);
    }

    @Override
    public List<CourseEntity> searchByName(String name) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<CourseEntity> list = session.createQuery(
                        "FROM CourseEntity c WHERE lower(c.name) LIKE :name", CourseEntity.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .list();
        session.close();
        return list;
    }
}
