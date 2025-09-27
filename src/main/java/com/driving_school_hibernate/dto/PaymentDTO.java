package com.driving_school_hibernate.dto;

import lombok.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PaymentDTO {
    private String paymentId;
    private String studentId;
    private String courseId;
    private double amount;
    private LocalDate paymentDate;
    private String method;
    private String status;
}
