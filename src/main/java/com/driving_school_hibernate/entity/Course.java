package com.driving_school_hibernate.entity;

import javax.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    private String courseId;  // e.g., C1001, C1002...

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String duration;

    @Column(nullable = false)
    private Double fee;

    // Many-to-Many with students
    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();

    // One-to-Many: course ↔ lessons
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Lesson> lessons = new HashSet<>();
}
