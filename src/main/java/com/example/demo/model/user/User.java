package com.example.demo.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "USER")
@AllArgsConstructor
@Builder
@Data
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @NotBlank
    @Column(name = "username", nullable = false)
    private String username;
    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;
    @NotBlank
    @Column(name = "merchantUuid", nullable = false)
    private String merchantUuid;

    public User() {

    }
}
