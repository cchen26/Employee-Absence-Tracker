package com.github.cchen26.employeeabsencetracker.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.github.cchen26.employeeabsencetracker.model.UserInfo;
import com.github.cchen26.employeeabsencetracker.service.UserInfoService;

@Controller
public class AdminController {

    private final UserInfoService userInfoService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminController(UserInfoService userInfoService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userInfoService = userInfoService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/user/change-password")
    public ModelAndView changePasswordForm() {
        return new ModelAndView("changePassword");
    }

    @PostMapping("/user/change-password")
    public ModelAndView changePasswordSubmit(@RequestParam("currentPassword") String currentPassword,
                                             @RequestParam("newPassword") String newPassword) {
        ModelAndView mav = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserInfo> optionalUserInfo = userInfoService.findUserByEmail(auth.getName());

        if (optionalUserInfo.isEmpty()) {
            mav.addObject("errorMessage", "User not found!");
            mav.setViewName("changePassword");
            return mav;
        }

        UserInfo userInfo = optionalUserInfo.get();
        if (!bCryptPasswordEncoder.matches(currentPassword, userInfo.getPassword())) {
            mav.addObject("errorMessage", "Current Password entered is wrong!!!");
            mav.setViewName("changePassword");
            return mav;
        }

        userInfo.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userInfoService.saveUser(userInfo);
        mav.addObject("successMessage", "Password changed Successfully!!!");
        mav.setViewName("redirect:/user/home");
        return mav;
    }

    @GetMapping("/user/manage-users")
    public ModelAndView showManageUsers() {
        List<UserInfo> userList = userInfoService.getUsers();
        return new ModelAndView("manageUsers", "users", userList);
    }

    @GetMapping("/user/manage-users/{action}/{id}")
    public ModelAndView manageUsers(@PathVariable String action, @PathVariable Integer id) {
        ModelAndView mav = new ModelAndView("redirect:/leave-management-system/user/manage-users");
        Optional<UserInfo> optionalUserInfo = userInfoService.getUserById(id);

        if (optionalUserInfo.isEmpty()) {
            mav.addObject("errorMessage", "User not found!");
            return mav;
        }

        UserInfo userInfo = optionalUserInfo.get();
        switch (action) {
            case "edit":
                mav.addObject("userInfo", userInfo);
                mav.setViewName("editUser");
                break;
            case "delete":
                userInfoService.deleteUser(id);
                mav.addObject("successMessage", "User removed successfully!!");
                break;
            case "block":
                userInfoService.blockUser(id);
                mav.addObject("successMessage", "User blocked successfully!!");
                break;
            case "unblock":
                userInfoService.unBlockUser(id);
                mav.addObject("successMessage", "User is active now!!");
                break;
        }
        return mav;
    }

    @PostMapping("/user/manage-users/save-user-edit")
    public ModelAndView saveUserEdit(@Valid UserInfo editedUserInfo, BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();

        Optional<UserInfo> optionalExistingUser = userInfoService.getUserById(editedUserInfo.getId());
        if (optionalExistingUser.isEmpty()) {
            mav.addObject("errorMessage", "User not found!");
            mav.setViewName("editUser");
            return mav;
        }

        UserInfo existingUser = optionalExistingUser.get();
        if (!existingUser.getEmail().equals(editedUserInfo.getEmail()) &&
                userInfoService.findUserByEmail(editedUserInfo.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "error.user", "User already exists with Email id");
        }

        if (bindingResult.hasErrors()) {
            mav.addObject("errorField", Objects.requireNonNull(bindingResult.getFieldError()).getField());
            mav.addObject("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            mav.setViewName("editUser");
            return mav;
        }

        updateUserInfo(existingUser, editedUserInfo);
        userInfoService.saveUser(existingUser);
        mav.addObject("successMessage", "User Details updated successfully!!");
        mav.setViewName("redirect:/user/manage-users");
        return mav;
    }

    private void updateUserInfo(UserInfo existingUser, UserInfo editedUserInfo) {
        existingUser.setEmail(editedUserInfo.getEmail());
        existingUser.setFirstName(editedUserInfo.getFirstName());
        existingUser.setLastName(editedUserInfo.getLastName());
        existingUser.setRole(editedUserInfo.getRole());
        // Assuming password change is handled elsewhere. Add password update if needed.
    }
}

