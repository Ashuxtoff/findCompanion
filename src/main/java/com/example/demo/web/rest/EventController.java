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

    @GetMapping("/addEvent")
    public String getAddEventForm(Model model)
    {
        model.addAttribute("eventForm", new Event());
        return "addEvent";
    }

    @PostMapping("/addEvent")
    public String addEvent(Model model, @ModelAttribute("eventForm") Event eventForm, BindingResult bindingResult)
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

        eventRepository.save(event);
        userRepository.save(user);

        return "redirect:/event" + event.getId().toString();
    }


    @GetMapping("/event{Id}")
    public String getCurrentEvent(@PathVariable String Id, Model model) {
        var username = securityService.findLoggedInUsername();
        User user = userRepository.findByUsername(username);

        var longId = Long.parseLong(Id);
        Event event = eventRepository.findById(longId).get();
        model.addAttribute("eventForm", event);

//        if (user.getEvents().contains(event)) {
//            return "myEvent";
//        }
//
//        if (user.getSubscribeTo().contains(event)) {
//            return "notMyEventSubscribed";
//        }
//        else {
//            return "notMyEventUnsubscribed";
//        }
        if (user.getEvents().contains(event)) {
            //model.addAttribute("wasDeleted", false);
            return "myEvent";
        }

        if (user.getSubscribeTo().contains(event)) {
            //model.addAttribute("wasSubscribed", true);
            return "notMyEventSubscribed";
        }

        //model.addAttribute("wasSubscribed", false);
        return "notMyEventUnsubscribed";
    }

    @GetMapping("/myEvents")
    public String getMyEventsList(Model model) {
        String username = securityService.findLoggedInUsername();
        User user = userRepository.findByUsername(username);

        Set<Event> myEvents = user.getEvents();
        Event [] myEventsArray = new Event[myEvents.size()];
        var index = 0;
        for (Event event : myEvents) {
            myEventsArray[index] = event;
            index++;
        }
        Arrays.sort(myEventsArray);

        ArrayList<Event> myEventsList = new ArrayList<Event>(Arrays.asList(myEventsArray));
        model.addAttribute("myEventsList", myEventsList);
        return "myEvents";
    }

    @GetMapping("/mySubscriptions")
    public String getMySubscriptionsList(Model model) {
        String username = securityService.findLoggedInUsername();
        User user = userRepository.findByUsername(username);

        Set<Event> mySubscriptions = user.getSubscribeTo();
        Event [] mySubscriptionsArray = new Event[mySubscriptions.size()];
        var index = 0;
        for (Event event : mySubscriptions) {
            mySubscriptionsArray[index] = event;
            index++;
        }
        Arrays.sort(mySubscriptionsArray);

        ArrayList<Event> mySubscriptionsList = new ArrayList<Event>(Arrays.asList(mySubscriptionsArray));
        model.addAttribute("mySubscriptionsList", mySubscriptionsList);
        return "mySubscriptions";
    }

    @GetMapping("/othersEvents")
    public String getOthersEvents(Model model) {
        String username = securityService.findLoggedInUsername();

        List<Event> allEvents = eventRepository.findAll();
        ArrayList<Event> allEventsList = new ArrayList<Event>(allEvents);
        model.addAttribute("othersEventsList", allEventsList.stream().filter(event -> !(event.getUsername().equals(username))).toArray());
        return "othersEvents";
    }

    @GetMapping("/event{Id}/subscribers")
    public String getEventSubscribers(@PathVariable String Id, Model model) {
        var longId = Long.parseLong(Id);
        Event event = eventRepository.findById(longId).get();
        Set<User> subscribersSet = event.getSubscribers();

        ArrayList<User> subscribersList = new ArrayList<User>(subscribersSet);

        model.addAttribute("subscribersList", subscribersList);
        return "redirect:/eventSubscribers";
    }

    @GetMapping("/event{Id}/subscribe")
    public String subscribe(@PathVariable String Id, Model model) {
        String username = securityService.findLoggedInUsername();
        User user = userRepository.findByUsername(username);

        var longId = Long.parseLong(Id);
        Event event = eventRepository.findById(longId).get();
        user.getSubscribeTo().add(event); // проработать после того, как решится вопрос со связностью таблиц, нужно ли еще добавлять юзера в список для ивента
        userRepository.save(user); // Здесь падает ошибка, ошибка в промежуточной таблице "повторяющееся значение ключа нарушает ограничение уникальности "uk_vupek7odydvgxk6s0o5nvii8"".
        eventRepository.save(event);

        model.addAttribute("eventForm", event);
        return "redirect:/notMyEventSubscribed";
    }

    @GetMapping("/event{Id}/unsubscribe")
    public String unsubscribe(@PathVariable String Id, Model model) {
        String username = securityService.findLoggedInUsername();
        User user = userRepository.findByUsername(username);

        var longId = Long.parseLong(Id);
        Event event = eventRepository.findById(longId).get();
        user.getSubscribeTo().remove(event); // проработать после того, как решится вопрос со связностью таблиц, нужно ли еще добавлять юзера в список для ивента
        userRepository.save(user);
        eventRepository.save(event);

        model.addAttribute("eventForm", event);
        return "redirect:/notMyEventUnsubscribed";
    }

    @GetMapping("/event{Id}/delete")
    public String delete(@PathVariable String Id, Model model) {
        String username = securityService.findLoggedInUsername();
        User user = userRepository.findByUsername(username);

        var longId = Long.parseLong(Id);
        Event event = eventRepository.findById(longId).get();
        user.getSubscribeTo().remove(event); // проработать после того, как решится вопрос со связностью таблиц, нужно ли еще добавлять юзера в список для ивента
        userRepository.save(user);
        eventRepository.delete(event);

        return "menu";
    }

    @GetMapping("/editEvent{Id}")
    public String getEditEventForm(@PathVariable String Id, Model model)
    {
        var longId = Long.parseLong(Id);
        Event event = eventRepository.findById(longId).get();

        model.addAttribute("editEventForm", event);
        return "editProfile";
    }




//    @PostMapping("/event{Id}")
//    public String changeEvent(@PathVariable String Id, @ModelAttribute("wasDeleted") boolean wasDeleted) {
//        var username = securityService.findLoggedInUsername();
//        User user = userRepository.findByUsername(username);
//
//        var longId = Long.parseLong(Id);
//        Event event = eventRepository.findById(longId).get();
//
//        if (wasDeleted) {
//            user.getEvents().remove(event);
//            eventRepository.delete(event);
//        }
//        return "menu";
//
//
//    }

//    @PostMapping("/event{Id}")
//    public String changeEvent(@PathVariable String Id, Model model) {
//        var username = securityService.findLoggedInUsername();
//        User user = userRepository.findByUsername(username);
//
//        var longId = Long.parseLong(Id);
//        Event event = eventRepository.findById(longId).get();
//
//        if (user.getEvents().contains(event)) {
//            boolean wasDeleted = (boolean)model.getAttribute("wasDeleted");
//            if (wasDeleted) {
//                user.getEvents().remove(event);
//                eventRepository.delete(event);
//            }
//            return "menu";
//        }
//
//        boolean wasSubscribed = (boolean)model.getAttribute("wasSubscribed");
//        if (wasSubscribed) {
//            user.getEvents().add(event);
//            userRepository.save(user);
//            eventRepository.save(event);
//            return "notMyEventSubscribed";
//        }
//        else {
//            user.getEvents().remove(event);
//            userRepository.save(user);
//            eventRepository.save(event);
//            return "notMyEventUnsubscribed";
//        }
//    }
//




}
