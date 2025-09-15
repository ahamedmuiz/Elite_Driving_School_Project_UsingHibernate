package com.driving_school_hibernate.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PaymentEntity {

    @Id
    @Column(name = "payment_id", length = 20)
    private String paymentId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "method", nullable = false, length = 50)
    private String method;  // Cash | Card

    @Column(name = "status", nullable = false, length = 20)
    private String status;  // Pending | Completed
}
