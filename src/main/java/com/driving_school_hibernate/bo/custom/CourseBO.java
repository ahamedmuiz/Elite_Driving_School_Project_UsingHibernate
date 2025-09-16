package com.driving_school_hibernate.bo.custom;

import com.driving_school_hibernate.bo.SuperBO;
import com.driving_school_hibernate.dto.CourseDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CourseBO extends SuperBO {
    boolean saveCourse(CourseDTO dto) throws SQLException;
    boolean updateCourse(CourseDTO dto) throws SQLException;
    boolean deleteCourse(String id) throws SQLException;
    Optional<CourseDTO> findCourseById(String id) throws SQLException;
    List<CourseDTO> findAllCourses() throws SQLException;
    List<CourseDTO> searchCoursesByName(String name) throws SQLException;
    String generateNewCourseId() throws SQLException;
}
