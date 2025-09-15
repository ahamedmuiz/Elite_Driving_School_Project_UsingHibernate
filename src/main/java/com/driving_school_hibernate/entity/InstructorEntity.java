package com.driving_school_hibernate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "instructors")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class InstructorEntity {

    @Id
    @Column(name = "instructor_id", length = 20)
    private String instructorId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "nic", unique = true, nullable = false, length = 20)
    private String nic;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "specialization", length = 100)
    private String specialization;

    @Column(name = "availability", length = 20)
    private String availability; // "Available" / "Not Available"
}
