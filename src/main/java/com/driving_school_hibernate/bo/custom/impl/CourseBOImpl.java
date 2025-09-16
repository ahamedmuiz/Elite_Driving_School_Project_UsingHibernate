package com.driving_school_hibernate.bo.custom.impl;

import com.driving_school_hibernate.bo.custom.CourseBO;
import com.driving_school_hibernate.dao.DAOFactory;
import com.driving_school_hibernate.dao.custom.CourseDAO;
import com.driving_school_hibernate.dto.CourseDTO;
import com.driving_school_hibernate.entity.CourseEntity;
import com.driving_school_hibernate.bo.util.EntityDTOConvertor;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourseBOImpl implements CourseBO {

    private final CourseDAO courseDAO = DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.COURSE);

    @Override
    public boolean saveCourse(CourseDTO dto) throws SQLException {
        return courseDAO.save(EntityDTOConvertor.toEntity(dto));
    }

    @Override
    public boolean updateCourse(CourseDTO dto) throws SQLException {
        return courseDAO.update(EntityDTOConvertor.toEntity(dto));
    }

    @Override
    public boolean deleteCourse(String id) throws SQLException {
        return courseDAO.delete(id);
    }

    @Override
    public Optional<CourseDTO> findCourseById(String id) throws SQLException {
        return courseDAO.findById(id).map(EntityDTOConvertor::fromEntity);
    }

    @Override
    public List<CourseDTO> findAllCourses() throws SQLException {
        return courseDAO.findAll().stream()
                .map(EntityDTOConvertor::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> searchCoursesByName(String name) throws SQLException {
        return courseDAO.searchByName(name).stream()
                .map(EntityDTOConvertor::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public String generateNewCourseId() throws SQLException {
        Optional<String> lastIdOpt = courseDAO.findLastId();
        if (lastIdOpt.isPresent()) {
            String lastId = lastIdOpt.get(); // e.g. "C5"
            int num = Integer.parseInt(lastId.substring(1));
            return "C00" + (num + 1);
        } else {
            return "C001";
        }
    }
}
