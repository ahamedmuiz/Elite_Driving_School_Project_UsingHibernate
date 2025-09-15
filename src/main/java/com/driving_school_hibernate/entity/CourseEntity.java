package com.driving_school_hibernate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CourseEntity {
    @Id
    @Column(name = "course_id", length = 20)
    private String courseId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "duration", length = 50)
    private String duration;

    @Column(name = "fee", nullable = false)
    private double fee;
}
