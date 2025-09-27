package com.driving_school_hibernate.bo.custom;

import com.driving_school_hibernate.bo.SuperBO;
import com.driving_school_hibernate.dto.PaymentDTO;

import java.sql.SQLException;
import java.util.List;

public interface PaymentBO extends SuperBO {

    boolean savePayment(PaymentDTO paymentDTO) throws SQLException;

    boolean updatePayment(PaymentDTO paymentDTO) throws SQLException;

    boolean deletePayment(String paymentId) throws SQLException;

    PaymentDTO findPaymentById(String paymentId) throws SQLException;

    List<PaymentDTO> findAllPayments() throws SQLException;

    List<PaymentDTO> searchPaymentsByStudentName(String name) throws SQLException;

    String generateNextPaymentId() throws SQLException;

    boolean isDuplicatePayment(String studentId, String courseId) throws SQLException;

}