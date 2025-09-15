package com.driving_school_hibernate.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CourseDTO {
    private String courseId;
    private String name;
    private String duration;
    private double fee;
}
