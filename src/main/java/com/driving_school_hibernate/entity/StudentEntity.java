package com.driving_school_hibernate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class StudentEntity {
    @Id
    @Column(name = "student_id", length = 20)
    private String studentId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "nic", nullable = false, unique = true, length = 20)
    private String nic;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "address", length = 255)
    private String address;
}
