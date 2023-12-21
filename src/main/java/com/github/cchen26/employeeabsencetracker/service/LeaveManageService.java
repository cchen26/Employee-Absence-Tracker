package com.github.cchen26.employeeabsencetracker.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.github.cchen26.employeeabsencetracker.model.LeaveDetails;
import com.github.cchen26.employeeabsencetracker.repository.LeaveManageRepository;

@Service
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

    public Optional<LeaveDetails> getLeaveDetailsById(Integer id) {
        return leaveManageRepository.findById(id);
    }

    public void updateLeaveDetails(LeaveDetails leaveDetails) {
        leaveManageRepository.save(leaveDetails);
    }

    public List<LeaveDetails> getAllActiveLeaves() {
        return leaveManageRepository.getAllActiveLeaves();
    }

    public List<LeaveDetails> getAllLeavesOfUser(Integer userId) {
        return leaveManageRepository.getAllLeavesOfUser(userId);
    }

    // Refactor or remove this method to use safer query building techniques
    public List<LeaveDetails> getAllLeavesOnStatus(boolean pending, boolean accepted, boolean rejected) {
        return leaveManageRepository.findLeavesByStatus(pending, accepted, rejected);
    }

}
