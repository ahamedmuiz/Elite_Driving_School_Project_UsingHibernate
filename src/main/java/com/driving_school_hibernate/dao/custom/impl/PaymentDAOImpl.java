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
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(entity);
                transaction.commit();
                return true;
            } catch (Exception e) {
                transaction.rollback();
                throw new SQLException("Failed to save payment: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public boolean update(PaymentEntity entity) throws SQLException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(entity);
                transaction.commit();
                return true;
            } catch (Exception e) {
                transaction.rollback();
                throw new SQLException("Failed to update payment: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                PaymentEntity payment = session.get(PaymentEntity.class, id);
                if (payment != null) {
                    session.remove(payment);
                    transaction.commit();
                    return true;
                }
                transaction.commit();
                return false;
            } catch (Exception e) {
                transaction.rollback();
                throw new SQLException("Failed to delete payment: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public Optional<PaymentEntity> findById(String id) throws SQLException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            PaymentEntity payment = session.get(PaymentEntity.class, id);
            return Optional.ofNullable(payment);
        } catch (Exception e) {
            throw new SQLException("Failed to find payment by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PaymentEntity> findAll() throws SQLException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Query<PaymentEntity> query = session.createQuery("FROM PaymentEntity", PaymentEntity.class);
            return query.list();
        } catch (Exception e) {
            throw new SQLException("Failed to find all payments: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> findAllIds() throws SQLException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Query<String> query = session.createQuery("SELECT p.paymentId FROM PaymentEntity p", String.class);
            return query.list();
        } catch (Exception e) {
            throw new SQLException("Failed to find all payment IDs: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<String> findLastId() throws SQLException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Query<String> query = session.createQuery(
                    "SELECT p.paymentId FROM PaymentEntity p ORDER BY p.paymentId DESC", String.class);
            query.setMaxResults(1);
            return Optional.ofNullable(query.uniqueResult());
        } catch (Exception e) {
            throw new SQLException("Failed to find last payment ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PaymentEntity> searchByName(String name) throws SQLException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Query<PaymentEntity> query = session.createQuery(
                    "SELECT p FROM PaymentEntity p WHERE p.student.firstName LIKE :name OR p.student.lastName LIKE :name",
                    PaymentEntity.class);
            query.setParameter("name", "%" + name + "%");
            return query.list();
        } catch (Exception e) {
            throw new SQLException("Failed to search payments by name: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByStudentAndCourse(String studentId, String courseId) throws Exception {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(p) FROM PaymentEntity p WHERE p.student.studentId = :studentId AND p.course.courseId = :courseId",
                    Long.class);
            query.setParameter("studentId", studentId);
            query.setParameter("courseId", courseId);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            throw new Exception("Failed to check payment existence: " + e.getMessage(), e);
        }
    }
}