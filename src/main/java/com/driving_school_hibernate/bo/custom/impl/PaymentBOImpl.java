package com.driving_school_hibernate.bo.custom.impl;

import com.driving_school_hibernate.bo.custom.PaymentBO;
import com.driving_school_hibernate.bo.util.EntityDTOConvertor;
import com.driving_school_hibernate.dao.DAOFactory;
import com.driving_school_hibernate.dao.custom.PaymentDAO;
import com.driving_school_hibernate.dto.PaymentDTO;
import com.driving_school_hibernate.entity.CourseEntity;
import com.driving_school_hibernate.entity.PaymentEntity;
import com.driving_school_hibernate.entity.StudentEntity;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentBOImpl implements PaymentBO {

    private final PaymentDAO paymentDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PAYMENT);

    @Override
    public boolean savePayment(PaymentDTO dto) throws SQLException {
        Session session = com.driving_school_hibernate.config.FactoryConfiguration.getInstance().getSession();
        StudentEntity student = session.get(StudentEntity.class, dto.getStudentId());
        CourseEntity course = session.get(CourseEntity.class, dto.getCourseId());
        session.close();
        return paymentDAO.save(EntityDTOConvertor.toEntity(dto, student, course));
    }

    @Override
    public boolean updatePayment(PaymentDTO dto) throws SQLException {
        Session session = com.driving_school_hibernate.config.FactoryConfiguration.getInstance().getSession();
        StudentEntity student = session.get(StudentEntity.class, dto.getStudentId());
        CourseEntity course = session.get(CourseEntity.class, dto.getCourseId());
        session.close();
        return paymentDAO.update(EntityDTOConvertor.toEntity(dto, student, course));
    }

    @Override
    public boolean deletePayment(String id) throws SQLException {
        return paymentDAO.delete(id);
    }

    @Override
    public PaymentDTO findPaymentById(String id) throws SQLException {
        return paymentDAO.findById(id).map(EntityDTOConvertor::fromEntity).orElse(null);
    }

    @Override
    public List<PaymentDTO> findAllPayments() throws SQLException {
        return paymentDAO.findAll().stream()
                .map(EntityDTOConvertor::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> searchPaymentsByStudentName(String name) throws SQLException {
        return paymentDAO.searchByName(name).stream()
                .map(EntityDTOConvertor::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public String generateNewPaymentId() throws SQLException {
        String lastId = paymentDAO.findLastId().orElse(null);
        if (lastId != null) {
            int num = Integer.parseInt(lastId.substring(1)) + 1;
            return String.format("P%03d", num);
        } else {
            return "P001";
        }
    }
}
