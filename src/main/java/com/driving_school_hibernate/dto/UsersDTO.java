package com.driving_school_hibernate.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UsersDTO {
    private String userId;
    private String username;
    private String email;
    private String password;
    private String role;
}
