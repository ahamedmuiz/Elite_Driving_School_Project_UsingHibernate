package com.driving_school_hibernate.bo.custom.impl;

import com.driving_school_hibernate.bo.custom.PaymentBO;
import com.driving_school_hibernate.bo.util.EntityDTOConvertor;
import com.driving_school_hibernate.config.FactoryConfiguration;
import com.driving_school_hibernate.dao.DAOFactory;
import com.driving_school_hibernate.dao.custom.CourseDAO;
import com.driving_school_hibernate.dao.custom.PaymentDAO;
import com.driving_school_hibernate.dao.custom.StudentDAO;
import com.driving_school_hibernate.dto.PaymentDTO;
import com.driving_school_hibernate.entity.CourseEntity;
import com.driving_school_hibernate.entity.PaymentEntity;
import com.driving_school_hibernate.entity.StudentEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaymentBOImpl implements PaymentBO {

    private final PaymentDAO paymentDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PAYMENT);
    private final StudentDAO studentDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.STUDENT);
    private final CourseDAO courseDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.COURSE);

    @Override
    public boolean savePayment(PaymentDTO paymentDTO) throws SQLException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.beginTransaction();
            try {

                Optional<StudentEntity> student = studentDAO.findById(paymentDTO.getStudentId());
                if (student.isEmpty()) {
                    throw new SQLException("Student not found with ID: " + paymentDTO.getStudentId());
                }

                Optional<CourseEntity> course = courseDAO.findById(paymentDTO.getCourseId());
                if (course.isEmpty()) {
                    throw new SQLException("Course not found with ID: " + paymentDTO.getCourseId());
                }

                if (paymentDAO.existsByStudentAndCourse(paymentDTO.getStudentId(), paymentDTO.getCourseId())) {
                    throw new SQLException("Payment already exists for this student and course");
                }

                PaymentEntity paymentEntity = EntityDTOConvertor.toEntity(paymentDTO, student.get(), course.get());
                boolean saved = paymentDAO.save(paymentEntity);

                if (saved) {
                    transaction.commit();
                } else {
                    transaction.rollback();
                }
                return saved;
            } catch (Exception e) {
                transaction.rollback();
                throw new SQLException("Failed to save payment: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public boolean updatePayment(PaymentDTO paymentDTO) throws SQLException {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Optional<PaymentEntity> existingPayment = paymentDAO.findById(paymentDTO.getPaymentId());
                if (existingPayment.isEmpty()) {
                    throw new SQLException("Payment not found with ID: " + paymentDTO.getPaymentId());
                }

                Optional<StudentEntity> student = studentDAO.findById(paymentDTO.getStudentId());
                if (student.isEmpty()) {
                    throw new SQLException("Student not found with ID: " + paymentDTO.getStudentId());
                }

                Optional<CourseEntity> course = courseDAO.findById(paymentDTO.getCourseId());
                if (course.isEmpty()) {
                    throw new SQLException("Course not found with ID: " + paymentDTO.getCourseId());
                }

                PaymentEntity paymentEntity = EntityDTOConvertor.toEntity(paymentDTO, student.get(), course.get());
                boolean updated = paymentDAO.update(paymentEntity);

                if (updated) {
                    transaction.commit();
                } else {
                    transaction.rollback();
                }
                return updated;
            } catch (Exception e) {
                transaction.rollback();
                throw new SQLException("Failed to update payment: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public boolean deletePayment(String paymentId) throws SQLException {
        return paymentDAO.delete(paymentId);
    }

    @Override
    public PaymentDTO findPaymentById(String paymentId) throws SQLException {
        Optional<PaymentEntity> paymentEntity = paymentDAO.findById(paymentId);
        return paymentEntity.map(EntityDTOConvertor::fromEntity).orElse(null);
    }

    @Override
    public List<PaymentDTO> findAllPayments() throws SQLException {
        List<PaymentEntity> paymentEntities = paymentDAO.findAll();
        return paymentEntities.stream()
                .map(EntityDTOConvertor::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> searchPaymentsByStudentName(String name) throws SQLException {
        List<PaymentEntity> paymentEntities = paymentDAO.searchByName(name);
        return paymentEntities.stream()
                .map(EntityDTOConvertor::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public String generateNextPaymentId() throws SQLException {
        Optional<String> lastId = paymentDAO.findLastId();
        if (lastId.isPresent()) {
            String lastPaymentId = lastId.get();
            try {
                int number = Integer.parseInt(lastPaymentId.substring(1));
                return String.format("P%03d", number + 1);
            } catch (NumberFormatException e) {
                throw new SQLException("Invalid payment ID format: " + lastPaymentId);
            }
        }
        return "P001";
    }

    @Override
    public boolean isDuplicatePayment(String studentId, String courseId) throws SQLException {
        try {
            return paymentDAO.existsByStudentAndCourse(studentId, courseId);
        } catch (Exception e) {
            throw new SQLException("Failed to check duplicate payment: " + e.getMessage(), e);
        }
    }
}