package com.driving_school_hibernate.dao.custom.impl;

import com.driving_school_hibernate.config.FactoryConfiguration;
import com.driving_school_hibernate.dao.custom.LessonDAO;
import com.driving_school_hibernate.entity.LessonEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LessonDAOImpl implements LessonDAO {

    @Override
    public boolean save(LessonEntity entity) throws SQLException {
        Transaction tx = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new SQLException(e);
        }
    }

    @Override
    public boolean update(LessonEntity entity) throws SQLException {
        Transaction tx = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new SQLException(e);
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
        Transaction tx = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            tx = session.beginTransaction();
            LessonEntity e = session.get(LessonEntity.class, id);
            if (e != null) session.remove(e);
            tx.commit();
            return e != null;
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            throw new SQLException(ex);
        }
    }

    @Override
    public Optional<LessonEntity> findById(String id) throws SQLException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            LessonEntity e = session.get(LessonEntity.class, id);
            return Optional.ofNullable(e);
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public List<LessonEntity> findAll() throws SQLException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            return session.createQuery("FROM LessonEntity", LessonEntity.class).list();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public List<String> findAllIds() throws SQLException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            return session.createQuery("select l.lessonId from LessonEntity l", String.class).list();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public Optional<String> findLastId() throws SQLException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            List<String> list = session.createQuery("select l.lessonId from LessonEntity l order by l.lessonId desc", String.class)
                    .setMaxResults(1).list();
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    @Override
    public List<LessonEntity> searchByName(String name) throws SQLException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            String param = "%" + name.toLowerCase() + "%";
            return session.createQuery("select l from LessonEntity l " +
                            "where lower(l.student.firstName) like :nm or lower(l.student.lastName) like :nm", LessonEntity.class)
                    .setParameter("nm", param)
                    .list();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }
}
