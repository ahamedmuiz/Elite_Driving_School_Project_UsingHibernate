package com.driving_school_hibernate.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class InstructorDTO {
    private String instructorId;
    private String firstName;
    private String lastName;
    private String nic;
    private String phone;
    private String email;
    private String specialization;
    private String availability;
}
