package com.driving_school_hibernate.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class LessonDTO {
    private String lessonId;
    private String studentId;
    private String courseId;
    private String instructorId;
    private LocalDate date;
    private String time;
    private String status;
}
