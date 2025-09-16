package com.driving_school_hibernate.bo.custom.impl;

import com.driving_school_hibernate.bo.custom.InstructorBO;
import com.driving_school_hibernate.bo.util.EntityDTOConvertor;
import com.driving_school_hibernate.dao.DAOFactory;
import com.driving_school_hibernate.dao.custom.InstructorDAO;
import com.driving_school_hibernate.dto.InstructorDTO;
import com.driving_school_hibernate.entity.InstructorEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InstructorBOImpl implements InstructorBO {

    private final InstructorDAO instructorDAO =
            DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.INSTRUCTOR);

    @Override
    public boolean saveInstructor(InstructorDTO dto) throws SQLException {
        return instructorDAO.save(EntityDTOConvertor.toEntity(dto));
    }

    @Override
    public boolean updateInstructor(InstructorDTO dto) throws SQLException {
        return instructorDAO.update(EntityDTOConvertor.toEntity(dto));
    }

    @Override
    public boolean deleteInstructor(String id) throws SQLException {
        return instructorDAO.delete(id);
    }

    @Override
    public Optional<InstructorDTO> findById(String id) throws SQLException {
        return instructorDAO.findById(id).map(EntityDTOConvertor::fromEntity);
    }

    @Override
    public List<InstructorDTO> findAllInstructors() throws SQLException {
        return instructorDAO.findAll().stream()
                .map(EntityDTOConvertor::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<InstructorDTO> searchInstructors(String name) throws SQLException {
        return instructorDAO.searchByName(name).stream()
                .map(EntityDTOConvertor::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public String generateNewInstructorId() throws SQLException {
        Optional<String> lastIdOpt = instructorDAO.findLastId();
        if (lastIdOpt.isEmpty()) return "INS001";

        String lastId = lastIdOpt.get();
        int num = Integer.parseInt(lastId.replace("INS00", "")) + 1;
        return String.format("INS%03d", num);
    }
}
