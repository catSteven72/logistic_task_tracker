package com.task_helper.task_helper.controllers;

import com.task_helper.task_helper.DTO.PasswordChangeDTO;
import com.task_helper.task_helper.DTO.UserRegistrationDTO;
import com.task_helper.task_helper.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public ModelAndView showRegistrationForm() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("user", new UserRegistrationDTO());
        return modelAndView;
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDTO registrationDto) {
        userService.registerNewUser(registrationDto);
        return "redirect:/";
    }

    @GetMapping("/change-password")
    public ModelAndView showChangePasswordForm() {
        return new ModelAndView("change-password", "passwordChangeDto", new PasswordChangeDTO());
    }

    @PostMapping("/change-password")
    public String changeUserPassword(@ModelAttribute PasswordChangeDTO passwordChangeDto) {
        userService.changeUserPassword(passwordChangeDto);
        return "redirect:/";
    }
}
