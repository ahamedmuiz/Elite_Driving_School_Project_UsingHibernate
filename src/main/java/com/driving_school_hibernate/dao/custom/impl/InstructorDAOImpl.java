package com.driving_school_hibernate.dao.custom.impl;

import com.driving_school_hibernate.config.FactoryConfiguration;
import com.driving_school_hibernate.dao.custom.InstructorDAO;
import com.driving_school_hibernate.entity.InstructorEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class InstructorDAOImpl implements InstructorDAO {

    @Override
    public boolean save(InstructorEntity entity) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.persist(entity);
        tx.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(InstructorEntity entity) throws SQLException {
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
        InstructorEntity instructor = session.get(InstructorEntity.class, id);
        if (instructor != null) {
            session.remove(instructor);
            tx.commit();
            session.close();
            return true;
        }
        tx.rollback();
        session.close();
        return false;
    }

    @Override
    public Optional<InstructorEntity> findById(String id) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        InstructorEntity entity = session.get(InstructorEntity.class, id);
        session.close();
        return Optional.ofNullable(entity);
    }

    @Override
    public List<InstructorEntity> findAll() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<InstructorEntity> list = session.createQuery("FROM InstructorEntity", InstructorEntity.class).list();
        session.close();
        return list;
    }

    @Override
    public List<String> findAllIds() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<String> ids = session.createQuery("SELECT i.instructorId FROM InstructorEntity i", String.class).list();
        session.close();
        return ids;
    }

    @Override
    public Optional<String> findLastId() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Query<String> query = session.createQuery(
                "SELECT i.instructorId FROM InstructorEntity i ORDER BY i.instructorId DESC", String.class
        ).setMaxResults(1);
        String lastId = query.uniqueResult();
        session.close();
        return Optional.ofNullable(lastId);
    }

    @Override
    public List<InstructorEntity> searchByName(String name) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<InstructorEntity> results = session.createQuery(
                "FROM InstructorEntity i WHERE i.firstName LIKE :name OR i.lastName LIKE :name", InstructorEntity.class
        ).setParameter("name", "%" + name + "%").list();
        session.close();
        return results;
    }
}
