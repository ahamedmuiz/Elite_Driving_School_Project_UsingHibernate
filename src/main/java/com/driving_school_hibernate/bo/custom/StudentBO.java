package com.driving_school_hibernate.bo.custom;

import com.driving_school_hibernate.bo.SuperBO;
import com.driving_school_hibernate.dto.StudentDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface StudentBO extends SuperBO {
    boolean saveStudent(StudentDTO dto) throws SQLException;

    boolean updateStudent(StudentDTO dto) throws SQLException;

    boolean deleteStudent(String id) throws SQLException;

    Optional<StudentDTO> findStudentById(String id) throws SQLException;

    List<StudentDTO> findAllStudents() throws SQLException;

    List<String> getAllStudentIds() throws SQLException;

    Optional<String> getLastStudentId() throws SQLException;

    List<StudentDTO> searchStudentsByName(String name) throws SQLException;
}
