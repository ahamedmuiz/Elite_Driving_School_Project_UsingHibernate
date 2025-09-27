package com.driving_school_hibernate.dao.custom;

import com.driving_school_hibernate.dao.CrudDAO;
import com.driving_school_hibernate.entity.PaymentEntity;

public interface PaymentDAO extends CrudDAO<PaymentEntity> {

    boolean existsByStudentAndCourse(String studentId, String courseId) throws Exception;

}