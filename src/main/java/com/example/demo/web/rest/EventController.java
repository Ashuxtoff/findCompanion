package com.example.demo.web.rest;

import com.example.demo.validators.EventValidator;
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
public class EventController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private EventValidator eventValidator;

        @GetMapping("/lookForEvent")
    public String getEventsList()
    {
        return "lookForEvent";
    }


    @GetMapping("/addEvent")
    public String getAddEventForm()
    {
        return "addEvent";
    }

//    @PostMapping("/addEvent")
//    public String




}
