package com.example.demo.web.rest;


import com.example.demo.repository.UserRepository;
import com.example.demo.validators.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.demo.entity.User;
import com.example.demo.service.SecurityService;
import com.example.demo.service.UserService;


@Controller
public class UserController
{
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registration(Model model)
    {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") User userForm, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userValidator.setContext("registration");
        userValidator.validate(userForm, bindingResult);
        userValidator.setContext("");
        if (userValidator.getHasErrors()) {
            return "registration";
        }
        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        String username = securityService.findLoggedInUsername();
        User user = userRepository.findByUsername(username);
        return "redirect:/menu";
    }

    @GetMapping("/login") // модел - это биндинг (для ошибок валидации)
    public String login(Model model, String error, String logout)
    {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @GetMapping({"/", "/menu"})
    public String menu(Model model)
    {
        return "menu";
    }


    @GetMapping("/editProfile")
    public String editProfile(Model model)
    {
        var loggedInUserName = securityService.findLoggedInUsername();
        var user = userService.findByUsername(loggedInUserName);

        model.addAttribute("editProfileForm", user);
        return "editProfile";
    }

    @PostMapping("/editProfile")
    public String editUserProfile(@ModelAttribute("editProfileForm") User editProfileForm, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            return "editProfile";
        }

        userValidator.validate(editProfileForm, bindingResult);

        if (userValidator.getHasErrors()) {
            return "editProfile";
        }

        userService.editProfile(editProfileForm);
        securityService.autoLoginWithoutPassword(editProfileForm.getUsername());

        return "menu";
    }

    @GetMapping("/myProfile")
    public String getCurrentEvent(Model model) {
        String username = securityService.findLoggedInUsername();
        User user = userService.findByUsername(username);
        model.addAttribute("userForm", user);
        return "myProfile";
    }
}
