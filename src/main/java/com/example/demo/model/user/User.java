package com.example.demo.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USER")
@AllArgsConstructor
@Builder
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "roles")
    @ManyToMany
    private List<UserRole> roles;

    public User() {

    }
}
