package com.github.cchen26.employeeabsencetracker.service;

import java.util.List;
import java.util.Optional;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;
import com.github.cchen26.employeeabsencetracker.model.LeaveDetails;
import com.github.cchen26.employeeabsencetracker.repository.LeaveManageRepository;


@Service(value = "leaveManageService")
public class LeaveManageService {

    private final LeaveManageRepository leaveManageRepository;

    public LeaveManageService(LeaveManageRepository leaveManageRepository) {
        this.leaveManageRepository = leaveManageRepository;
    }

    public void applyLeave(LeaveDetails leaveDetails) {
        long duration = ChronoUnit.DAYS.between(leaveDetails.getFromDate(), leaveDetails.getToDate()) + 1;
        leaveDetails.setDuration((int) duration);
        leaveDetails.setActive(true);
        leaveManageRepository.save(leaveDetails);
    }

    public List<LeaveDetails> getAllLeaves() {
        return leaveManageRepository.findAll();
    }

    public Optional<LeaveDetails> getLeaveDetailsOnId(Long id) {
        return leaveManageRepository.findById(id);
    }

    public void updateLeaveDetails(LeaveDetails leaveDetails) {
        leaveManageRepository.save(leaveDetails);
    }

    public List<LeaveDetails> getAllActiveLeaves() {
        return leaveManageRepository.getAllActiveLeaves();
    }

    public List<LeaveDetails> getAllLeavesOfUser(String username) {
        return leaveManageRepository.getAllLeavesOfUser(username);
    }

    // Method for getAllLeavesOnStatus needs to be refactored or moved to the repository layer
}

