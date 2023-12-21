package com.github.cchen26.employeeabsencetracker.controller;

import com.github.cchen26.employeeabsencetracker.model.LeaveDetails;
import com.github.cchen26.employeeabsencetracker.model.UserInfo;
import com.github.cchen26.employeeabsencetracker.service.LeaveManageService;
import com.github.cchen26.employeeabsencetracker.service.UserInfoService;
import com.github.cchen26.employeeabsencetracker.DTO.LeaveDetailsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class LeaveManageController {

    private final LeaveManageService leaveManageService;
    private final UserInfoService userInfoService;

    public LeaveManageController(LeaveManageService leaveManageService, UserInfoService userInfoService) {
        this.leaveManageService = leaveManageService;
        this.userInfoService = userInfoService;
    }

    // Endpoint to initialize a form for applying for leave.
    @GetMapping("/apply-leave")
    public ResponseEntity<LeaveDetailsDTO> getLeaveApplicationForm() {
        return ResponseEntity.ok(new LeaveDetailsDTO());
    }

    // Endpoint to submit a leave application.
    @PostMapping("/apply-leave")
    public ResponseEntity<String> submitApplyLeave(@RequestBody LeaveDetailsDTO leaveDetailsDto) {
        Optional<UserInfo> optionalUserInfo = userInfoService.getUserInfo();
        if (optionalUserInfo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        UserInfo userInfo = optionalUserInfo.get();
        LeaveDetails leaveDetails = leaveDetailsDto.toEntity(userInfo);
        leaveDetails.setUser(userInfo);
        leaveManageService.applyLeave(leaveDetails);
        return ResponseEntity.ok("Your Leave Request is registered!");
    }

    // Get all leaves with optional status filters.
    @GetMapping("/get-all-leaves")
    public ResponseEntity<List<LeaveDetailsDTO>> getAllLeaves(
            @RequestParam(value = "pending", defaultValue = "false") boolean pending,
            @RequestParam(value = "accepted", defaultValue = "false") boolean accepted,
            @RequestParam(value = "rejected", defaultValue = "false") boolean rejected) {
        List<LeaveDetails> leaves = leaveManageService.getAllLeavesOnStatus(pending, accepted, rejected);
        return ResponseEntity.ok(leaves.stream().map(LeaveDetailsDTO::new).collect(Collectors.toList()));
    }

    // Manage existing leaves by accepting or rejecting them.
    @PutMapping("/manage-leaves/{action}/{id}")
    public ResponseEntity<String> manageLeaves(@PathVariable("action") String action, @PathVariable("id") Integer id) {
        Optional<LeaveDetails> optionalLeaveDetails = leaveManageService.getLeaveDetailsById(id);
        if (optionalLeaveDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Leave details not found");
        }
        LeaveDetails leaveDetails = optionalLeaveDetails.get();
        switch (action) {
            case "accept":
                leaveDetails.setAcceptRejectFlag(true);
                leaveDetails.setActive(false);
                break;
            case "reject":
                leaveDetails.setAcceptRejectFlag(false);
                leaveDetails.setActive(false);
                break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid action");
        }
        leaveManageService.updateLeaveDetails(leaveDetails);
        return ResponseEntity.ok("Leave updated successfully!");
    }

    // Display the leaves applied by the current user.
    @GetMapping("/my-leaves")
    public ResponseEntity<List<LeaveDetailsDTO>> showMyLeaves() {
        Optional<UserInfo> optionalUserInfo = userInfoService.getUserInfo();
        if (optionalUserInfo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        UserInfo userInfo = optionalUserInfo.get();
        List<LeaveDetails> leaves = leaveManageService.getAllLeavesOfUser(userInfo.getId());
        return ResponseEntity.ok(leaves.stream().map(LeaveDetailsDTO::new).collect(Collectors.toList()));
    }

}


