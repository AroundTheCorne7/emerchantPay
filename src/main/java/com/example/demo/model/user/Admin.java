package com.example.demo.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "ADMIN")
@AllArgsConstructor
@Builder
@Data
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "is_active")
    private boolean isActive;

    public Admin() {
    }
}
