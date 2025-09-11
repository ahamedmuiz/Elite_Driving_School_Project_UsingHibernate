package com.driving_school_hibernate.dto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data                 // generates getters/setters/toString/hashCode/equals
@NoArgsConstructor    // generates no-args constructor
@AllArgsConstructor   // generates all-args constructor
@Builder              // allows builder pattern
public class UserDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;  // BCrypt hash

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Roles role;
}
