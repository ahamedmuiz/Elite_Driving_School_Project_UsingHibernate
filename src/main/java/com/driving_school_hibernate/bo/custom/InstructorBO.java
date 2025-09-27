package com.driving_school_hibernate.bo.custom;

import com.driving_school_hibernate.bo.SuperBO;
import com.driving_school_hibernate.dto.InstructorDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface InstructorBO extends SuperBO {

    boolean saveInstructor(InstructorDTO dto) throws SQLException;

    boolean updateInstructor(InstructorDTO dto) throws SQLException;

    boolean deleteInstructor(String id) throws SQLException;

    Optional<InstructorDTO> findById(String id) throws SQLException;

    List<InstructorDTO> findAllInstructors() throws SQLException;

    List<InstructorDTO> searchInstructors(String name) throws SQLException;

    String generateNewInstructorId() throws SQLException;

}
