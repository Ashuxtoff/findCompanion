package com.example.demo.web.rest;

import com.example.demo.entity.Event;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EventService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.User;
import com.example.demo.service.SecurityService;
import com.example.demo.service.UserService;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.html.parser.Entity;
import java.util.*;

@Controller
public class EventController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventValidator eventValidator;

    @GetMapping("/lookForEvent")
    public String getEventsList()
    {
        return "lookForEvent";
    }

    @GetMapping("/addEvent")
    public String getAddEventForm(Model model)
    {
        model.addAttribute("eventForm", new Event());
        return "addEvent";
    }

    @PostMapping("/addEvent")
    public String addEvent(@ModelAttribute("eventForm") Event eventForm, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            return "addEvent";
        }

        eventValidator.validate(eventForm, bindingResult);

        if (eventValidator.getHasErrors()) {
            return "addEvent";
        }

        String username = securityService.findLoggedInUsername();
        User user = userRepository.findByUsername(username);
        Set<Event> userEvents = user.getEvents();

        Set<User> subscribers = new HashSet<User>();
        subscribers.add(user);
        Event event = new Event(eventForm.getTitle(), eventForm.getDescription(), eventForm.getDatetime(), eventForm.getAddress(), user, subscribers);

        userEvents.add(event);
        user.setEvents(userEvents);

        Set<Event> userSubscriptions = user.getSubscribeTo();
        userSubscriptions.add(event);
        user.setSubscribeTo(userSubscriptions);

        userRepository.save(user);
        eventRepository.save(event);


        return "redirect:/event" + event.getId().toString();
    }


    @GetMapping("/event{Id}")
    public String getCurrentEvent(@PathVariable String Id, Model model) {
        var longId = Long.parseLong(Id);
        Event event = eventRepository.findById(longId).get();
        model.addAttribute("eventForm", event);
        return "event";
    }

    @GetMapping("/myEvents")
    public String getMyEventsList(Model model) {
        String username = securityService.findLoggedInUsername();
        User user = userRepository.findByUsername(username);
//        ArrayList<String> myEventsIds = new ArrayList<String>();
        Set<Event> myEvents = user.getEvents();
        Event [] myEventsArray = new Event[myEvents.size()];
        var index = 0;
        for (Event event : myEvents) {
            myEventsArray[index] = event;
            index++;
        }
        Arrays.sort(myEventsArray);
//        for (Event event : myEvents) {
//            myEventsIds.add(event.getId().toString());
//        }
        ArrayList<Event> myEventsList = new ArrayList<Event>(Arrays.asList(myEventsArray));
        model.addAttribute("myEventsList", myEventsList);
        return "myEvents";
    }



}
