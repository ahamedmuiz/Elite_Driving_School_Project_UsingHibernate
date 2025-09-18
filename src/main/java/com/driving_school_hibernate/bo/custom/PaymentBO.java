package com.driving_school_hibernate.bo.custom;

import com.driving_school_hibernate.bo.SuperBO;
import com.driving_school_hibernate.dto.PaymentDTO;

import java.sql.SQLException;
import java.util.List;

public interface PaymentBO extends SuperBO {
    boolean savePayment(PaymentDTO dto) throws SQLException;
    boolean updatePayment(PaymentDTO dto) throws SQLException;
    boolean deletePayment(String id) throws SQLException;
    PaymentDTO findPaymentById(String id) throws SQLException;
    List<PaymentDTO> findAllPayments() throws SQLException;
    List<PaymentDTO> searchPaymentsByStudentName(String name) throws SQLException;
    String generateNewPaymentId() throws SQLException;
}
