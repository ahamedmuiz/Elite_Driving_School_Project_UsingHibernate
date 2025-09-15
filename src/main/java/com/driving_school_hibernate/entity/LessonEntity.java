package com.driving_school_hibernate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "lessons")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class LessonEntity {
    @Id
    @Column(name = "lesson_id", length = 20)
    private String lessonId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private InstructorEntity instructor;

    @Column(name = "lesson_date", nullable = false)
    private LocalDate date;

    @Column(name = "time", length = 20)
    private String time;

    @Column(name = "status", length = 20)
    private String status;
}
