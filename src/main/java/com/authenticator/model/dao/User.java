package com.authenticator.model.dao;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", indexes = @Index(name = "idx_email", columnList = "email", unique = true))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    private String email;
}
