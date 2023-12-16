package com.github.cchen26.employeeabsencetracker.controller;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import com.github.cchen26.employeeabsencetracker.model.UserInfo;
import com.github.cchen26.employeeabsencetracker.service.UserInfoService;

@Controller
public class AdminController {

    private final UserInfoService userInfoService;
    private final BCryptPasswordEncoder bCryptPassEncoder;

    private static final String REDIRECT_MANAGE_USERS = "redirect:/user/manage-users";
    private static final String VIEW_CHANGE_PASSWORD = "changePassword";
    private static final String VIEW_MANAGE_USERS = "manageUsers";
    private static final String VIEW_EDIT_USER = "editUser";

    public AdminController(UserInfoService userInfoService, BCryptPasswordEncoder bCryptPassEncoder) {
        this.userInfoService = userInfoService;
        this.bCryptPassEncoder = bCryptPassEncoder;
    }

    @GetMapping("/user/change-password")
    public ModelAndView changePasswordForm() {
        return new ModelAndView(VIEW_CHANGE_PASSWORD);
    }

    @PostMapping("/user/change-password")
    public ModelAndView changePasswordSubmit(@RequestParam("currentPassword") String currentPassword,
                                             @RequestParam("newPassword") String newPassword) {
        ModelAndView mav = new ModelAndView();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserInfo> userInfo = userInfoService.findUserByEmail(username);

        if (!bCryptPassEncoder.matches(currentPassword, userInfo.getPassword())) {
            mav.addObject("successMessage", "Current Password entered is wrong!!!");
            mav.setView(new RedirectView("../user/change-password"));
            return mav;
        }

        userInfo.setPassword(bCryptPassEncoder.encode(newPassword));
        userInfoService.saveUser(userInfo);
        mav.addObject("successMessage", "Password changed Successfully!!!");
        mav.setView(new RedirectView("../user/home"));
        return mav;
    }

    // Other methods with similar improvements
}
