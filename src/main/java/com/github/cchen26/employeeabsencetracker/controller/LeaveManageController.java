package com.github.cchen26.employeeabsencetracker.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import jakarta.validation.Valid;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.github.cchen26.employeeabsencetracker.model.LeaveDetails;
import com.github.cchen26.employeeabsencetracker.service.LeaveManageService;
import com.github.cchen26.employeeabsencetracker.service.UserInfoService;


@Controller
public class LeaveManageController {

    private final LeaveManageService leaveManageService;
    private final UserInfoService userInfoService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LeaveManageController(LeaveManageService leaveManageService, UserInfoService userInfoService) {
        this.leaveManageService = leaveManageService;
        this.userInfoService = userInfoService;
    }

    // Other methods with similar improvements

    @GetMapping("/user/get-all-leaves")
    @ResponseBody
    public String getAllLeaves(@RequestParam(value = "pending", defaultValue = "false") boolean pending,
                               @RequestParam(value = "accepted", defaultValue = "false") boolean accepted,
                               @RequestParam(value = "rejected", defaultValue = "false") boolean rejected) {

        List<LeaveDetails> leaves = leaveManageService.getAllLeaves();
        if (pending || accepted || rejected) {
            leaves = leaveManageService.getAllLeavesOnStatus(pending, accepted, rejected);
        }

        JSONArray jsonArr = new JSONArray();
        leaves.forEach(leave -> {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("title", leave.getEmployeeName());
            jsonObj.put("start", DATE_FORMATTER.format(leave.getFromDate()));
            jsonObj.put("end", DATE_FORMATTER.format(leave.getToDate().plusDays(1)));
            jsonObj.put("color", determineColor(leave));
            jsonArr.put(jsonObj);
        });

        return jsonArr.toString();
    }

    private String determineColor(LeaveDetails leave) {
        if (leave.isActive()) return "#0878af";
        return leave.isAcceptRejectFlag() ? "green" : "red";
    }

    // Rest of the methods
}
