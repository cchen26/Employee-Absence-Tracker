package com.github.cchen26.employeeabsencetracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "userinfo")
@Getter @Setter @NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "E-mail cannot be empty!")
    @Email(message = "Please provide a valid email!")
    private String email;

    @NotBlank(message = "Password cannot be empty!")
    @Size(min = 5, message = "Choose at least five characters for password!")
    private String password;

    @NotBlank(message = "Please provide a role!")
    private String role;

    @NotBlank(message = "Please provide first name!")
    private String firstName;

    @NotBlank(message = "Please provide last name!")
    private String lastName;

    private boolean active;
}
