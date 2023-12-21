package com.github.cchen26.employeeabsencetracker.repository;

import java.util.List;

import com.github.cchen26.employeeabsencetracker.model.LeaveDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository(value = "leaveManageRepository")
public interface LeaveManageRepository extends JpaRepository<LeaveDetails, Integer> {

    @Query("SELECT ld FROM LeaveDetails ld WHERE ld.active = true")
    List<LeaveDetails> getAllActiveLeaves();

    @Query("SELECT ld FROM LeaveDetails ld WHERE ld.user.id = :userId ORDER BY ld.id DESC")
    List<LeaveDetails> getAllLeavesOfUser(@Param("userId") Integer userId);


    // Add a method for dynamic query based on status parameters
    @Query("SELECT ld FROM LeaveDetails ld WHERE " +
            "(?1 = true AND ld.active = true) OR " +
            "(?2 = true AND ld.active = false AND ld.acceptRejectFlag = true) OR " +
            "(?3 = true AND ld.active = false AND ld.acceptRejectFlag = false)")
    List<LeaveDetails> findLeavesByStatus(boolean pending, boolean accepted, boolean rejected);


}
