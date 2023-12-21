package com.github.cchen26.employeeabsencetracker.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leave_details")
@Getter @Setter @NoArgsConstructor
public class LeaveDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Please specify the user!")
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserInfo user; // Assuming UserInfo is your user entity

    @NotNull(message = "Please provide start date!")
    private LocalDateTime fromDate;

    @NotNull(message = "Please provide end date!")
    private LocalDateTime toDate;

    @NotBlank(message = "Please select type of leave!")
    private String leaveType;

    @NotBlank(message = "Please provide a reason for the leave!")
    private String reason;

    private int duration;

    private boolean acceptRejectFlag;

    private boolean active;
}
