package com.driving_school_hibernate.bo.custom;

import com.driving_school_hibernate.bo.SuperBO;
import com.driving_school_hibernate.dto.LessonDTO;

import java.util.List;

public interface LessonBO extends SuperBO {

    boolean saveLesson(LessonDTO dto) throws Exception;

    boolean updateLesson(LessonDTO dto) throws Exception;

    boolean deleteLesson(String id) throws Exception;

    List<LessonDTO> findAllLessons() throws Exception;

    List<LessonDTO> searchLessons(String studentName) throws Exception;

    String generateNewLessonId() throws Exception;

}
