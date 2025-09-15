package com.driving_school_hibernate.dao.custom.impl;

import com.driving_school_hibernate.config.FactoryConfiguration;
import com.driving_school_hibernate.dao.custom.StudentDAO;
import com.driving_school_hibernate.entity.StudentEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public boolean save(StudentEntity entity) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(entity);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(StudentEntity entity) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.merge(entity);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        StudentEntity student = session.get(StudentEntity.class, id);
        if (student != null) {
            session.remove(student);
            transaction.commit();
            session.close();
            return true;
        }
        session.close();
        return false;
    }

    @Override
    public Optional<StudentEntity> findById(String id) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        StudentEntity student = session.get(StudentEntity.class, id);
        session.close();
        return Optional.ofNullable(student);
    }

    @Override
    public List<StudentEntity> findAll() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<StudentEntity> list = session.createQuery("FROM StudentEntity", StudentEntity.class).list();
        session.close();
        return list;
    }

    @Override
    public List<String> findAllIds() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<String> ids = session.createQuery("SELECT s.studentId FROM StudentEntity s", String.class).list();
        session.close();
        return ids;
    }

    @Override
    public Optional<String> findLastId() throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        String lastId = session.createQuery("SELECT s.studentId FROM StudentEntity s ORDER BY s.studentId DESC", String.class)
                .setMaxResults(1)
                .uniqueResult();
        session.close();
        return Optional.ofNullable(lastId);
    }

    @Override
    public List<StudentEntity> searchByName(String name) throws SQLException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Query<StudentEntity> query = session.createQuery(
                "FROM StudentEntity s WHERE s.firstName LIKE :name OR s.lastName LIKE :name",
                StudentEntity.class
        );
        query.setParameter("name", "%" + name + "%");
        List<StudentEntity> result = query.list();
        session.close();
        return result;
    }
}
