package com.driving_school_hibernate.bo.custom.impl;

import com.driving_school_hibernate.bo.custom.StudentBO;
import com.driving_school_hibernate.bo.util.EntityDTOConvertor;
import com.driving_school_hibernate.dao.DAOFactory;
import com.driving_school_hibernate.dao.custom.StudentDAO;
import com.driving_school_hibernate.dto.StudentDTO;
import com.driving_school_hibernate.entity.StudentEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentBOImpl implements StudentBO {

    private final StudentDAO studentDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.STUDENT);

    @Override
    public boolean saveStudent(StudentDTO dto) throws SQLException {
        return studentDAO.save(EntityDTOConvertor.toEntity(dto));
    }

    @Override
    public boolean updateStudent(StudentDTO dto) throws SQLException {
        return studentDAO.update(EntityDTOConvertor.toEntity(dto));
    }

    @Override
    public boolean deleteStudent(String id) throws SQLException {
        return studentDAO.delete(id);
    }

    @Override
    public Optional<StudentDTO> findStudentById(String id) throws SQLException {
        return studentDAO.findById(id).map(EntityDTOConvertor::fromEntity);
    }

    @Override
    public List<StudentDTO> findAllStudents() throws SQLException {
        return studentDAO.findAll().stream()
                .map(EntityDTOConvertor::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllStudentIds() throws SQLException {
        return studentDAO.findAllIds();
    }

    @Override
    public Optional<String> getLastStudentId() throws SQLException {
        return studentDAO.findLastId();
    }

    @Override
    public List<StudentDTO> searchStudentsByName(String name) throws SQLException {
        return studentDAO.searchByName(name).stream()
                .map(EntityDTOConvertor::fromEntity)
                .collect(Collectors.toList());
    }
}
