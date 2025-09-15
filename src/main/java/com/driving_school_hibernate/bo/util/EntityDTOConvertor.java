package com.driving_school_hibernate.bo.util;

import com.driving_school_hibernate.dto.*;
import com.driving_school_hibernate.entity.*;

public class EntityDTOConvertor {

    // ==================== Users ====================
    public static UsersDTO fromEntity(UsersEntity entity) {
        if (entity == null) return null;
        return new UsersDTO(
                entity.getUserId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole()
        );
    }

    public static UsersEntity toEntity(UsersDTO dto) {
        if (dto == null) return null;
        return new UsersEntity(
                dto.getUserId(),
                dto.getUsername(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getRole()
        );
    }

    // ==================== Student ====================
    public static StudentDTO fromEntity(StudentEntity entity) {
        if (entity == null) return null;
        return new StudentDTO(
                entity.getStudentId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getNic(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getAddress()
        );
    }

    public static StudentEntity toEntity(StudentDTO dto) {
        if (dto == null) return null;
        return new StudentEntity(
                dto.getStudentId(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getNic(),
                dto.getPhone(),
                dto.getEmail(),
                dto.getAddress()
        );
    }

    // ==================== Instructor ====================
    public static InstructorDTO fromEntity(InstructorEntity entity) {
        if (entity == null) return null;
        return new InstructorDTO(
                entity.getInstructorId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getNic(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getSpecialization(),
                entity.getAvailability()
        );
    }

    public static InstructorEntity toEntity(InstructorDTO dto) {
        if (dto == null) return null;
        return new InstructorEntity(
                dto.getInstructorId(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getNic(),
                dto.getPhone(),
                dto.getEmail(),
                dto.getSpecialization(),
                dto.getAvailability()
        );
    }

    // ==================== Course ====================
    public static CourseDTO fromEntity(CourseEntity entity) {
        if (entity == null) return null;
        return new CourseDTO(
                entity.getCourseId(),
                entity.getName(),
                entity.getDuration(),
                entity.getFee()
        );
    }

    public static CourseEntity toEntity(CourseDTO dto) {
        if (dto == null) return null;
        return new CourseEntity(
                dto.getCourseId(),
                dto.getName(),
                dto.getDuration(),
                dto.getFee()
        );
    }

    // ==================== Lesson ====================
    public static LessonDTO fromEntity(LessonEntity entity) {
        if (entity == null) return null;
        return new LessonDTO(
                entity.getLessonId(),
                entity.getStudent().getStudentId(),
                entity.getCourse().getCourseId(),
                entity.getInstructor().getInstructorId(),
                entity.getDate(),
                entity.getTime(),
                entity.getStatus()
        );
    }

    public static LessonEntity toEntity(LessonDTO dto, StudentEntity student,
                                        CourseEntity course, InstructorEntity instructor) {
        if (dto == null) return null;
        return new LessonEntity(
                dto.getLessonId(),
                student,
                course,
                instructor,
                dto.getDate(),
                dto.getTime(),
                dto.getStatus()
        );
    }

    // ==================== Payment ====================
    public static PaymentDTO fromEntity(PaymentEntity entity) {
        if (entity == null) return null;
        return new PaymentDTO(
                entity.getPaymentId(),
                entity.getStudent().getStudentId(),
                entity.getCourse().getCourseId(),
                entity.getAmount(),
                entity.getPaymentDate(),
                entity.getMethod(),
                entity.getStatus()
        );
    }

    public static PaymentEntity toEntity(PaymentDTO dto, StudentEntity student, CourseEntity course) {
        if (dto == null) return null;
        return new PaymentEntity(
                dto.getPaymentId(),
                student,
                course,
                dto.getAmount(),
                dto.getPaymentDate(),
                dto.getMethod(),
                dto.getStatus()
        );
    }
}
