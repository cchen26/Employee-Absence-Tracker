package com.github.cchen26.employeeabsencetracker.controller;

import jakarta.validation.Valid;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.github.cchen26.employeeabsencetracker.model.UserInfo;
import com.github.cchen26.employeeabsencetracker.service.UserInfoService;

@Controller
public class LoginController {

    private final UserInfoService userInfoService;

    private static final String VIEW_HOME = "home";
    private static final String VIEW_LOGIN = "login";
    private static final String VIEW_REGISTRATION = "registration";

    public LoginController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping({ "/", "/login" })
    public ModelAndView login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return new ModelAndView("redirect:/" + VIEW_HOME);
        }

        return new ModelAndView(VIEW_LOGIN);
    }

    @GetMapping("/registration")
    public ModelAndView registration() {
        ModelAndView mav = new ModelAndView(VIEW_REGISTRATION);
        mav.addObject("userInfo", new UserInfo());
        return mav;
    }

    @PostMapping("/registration")
    public ModelAndView createNewUser(@Valid UserInfo userInfo, BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView(VIEW_REGISTRATION);

        if (userInfoService.findUserByEmail(userInfo.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "error.user", "User already exists with Email id");
        }

        if (bindingResult.hasErrors()) {
            return mav;
        }

        userInfoService.saveUser(userInfo);
        mav.addObject("successMessage", "User registered successfully! Awaiting for Manager approval!!");
        mav.addObject("userInfo", new UserInfo());
        return mav;
    }

    @GetMapping("/user/home")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView(VIEW_HOME);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        mav.addObject("userInfo", userInfoService.findUserByEmail(auth.getName()));
        return mav;
    }
}
