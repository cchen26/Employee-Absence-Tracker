package com.github.cchen26.employeeabsencetracker.DTO;

import com.github.cchen26.employeeabsencetracker.model.LeaveDetails;
import com.github.cchen26.employeeabsencetracker.model.UserInfo;
import java.time.LocalDateTime;

public class LeaveDetailsDTO {

    private Integer id;
    private String username;
    private String employeeName;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String leaveType;
    private String reason;
    private int duration;
    private boolean acceptRejectFlag;
    private boolean active;

    // Default Constructor
    public LeaveDetailsDTO() {
    }

    // Constructor using fields
    public LeaveDetailsDTO(LeaveDetails leaveDetails) {
        this.id = leaveDetails.getId();
        this.username = leaveDetails.getUser().getEmail();
        this.employeeName = leaveDetails.getUser().getFirstName() + " " + leaveDetails.getUser().getLastName();
        this.fromDate = leaveDetails.getFromDate();
        this.toDate = leaveDetails.getToDate();
        this.leaveType = leaveDetails.getLeaveType();
        this.reason = leaveDetails.getReason();
        this.duration = leaveDetails.getDuration();
        this.acceptRejectFlag = leaveDetails.isAcceptRejectFlag();
        this.active = leaveDetails.isActive();
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public String getReason() {
        return reason;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isAcceptRejectFlag() {
        return acceptRejectFlag;
    }

    public boolean isActive() {
        return active;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setAcceptRejectFlag(boolean acceptRejectFlag) {
        this.acceptRejectFlag = acceptRejectFlag;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Method to convert DTO to Entity
    public LeaveDetails toEntity(UserInfo userInfo) {
        LeaveDetails leaveDetails = new LeaveDetails();
        leaveDetails.setId(this.id);
        leaveDetails.setUser(userInfo); // Setting the UserInfo object
        leaveDetails.setFromDate(this.fromDate);
        leaveDetails.setToDate(this.toDate);
        leaveDetails.setLeaveType(this.leaveType);
        leaveDetails.setReason(this.reason);
        leaveDetails.setDuration(this.duration);
        leaveDetails.setAcceptRejectFlag(this.acceptRejectFlag);
        leaveDetails.setActive(this.active);
        return leaveDetails;
    }

}
