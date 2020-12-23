package com.example.demo.web.rest;

import com.example.demo.entity.Event;
import com.example.demo.entity.Message;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.validators.EventValidator;
import com.example.demo.validators.MessageValidator;
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
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Controller
public class EventController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventValidator eventValidator;

    @Autowired
    private MessageValidator messageValidator;

    @Autowired
    private MessageRepository messageRepository;

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
        model.addAttribute("Id", Id);

        if (user.getEvents().contains(event)) {
            return "myEvent";
        }

        if (user.getSubscribeTo().contains(event)) {
            return "notMyEventSubscribed";
        }

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


    @PostMapping("/event{Id}")
    public String postProcessing(@PathVariable String Id, HttpServletRequest request) throws IOException {
        String actionType = getRequestBody(request);
        if (actionType.equals("delete")) {
            var longId = Long.parseLong(Id);
            Event event = eventRepository.findById(longId).get();
            eventRepository.delete(event);
            return "redirect:/menu";
        }

        var username = securityService.findLoggedInUsername();
        User user = userRepository.findByUsername(username);

        var longId = Long.parseLong(Id);
        Event event = eventRepository.findById(longId).get();

        if (actionType.equals("subscribe")) {
            user.getSubscribeTo().add(event);
            event.getSubscribers().add(user);
            userRepository.save(user);
            eventRepository.save(event);
            return "notMyEventSubscribed";
        }
        else {
            user.getSubscribeTo().remove(event);
            event.getSubscribers().remove(user);
            userRepository.save(user);
            eventRepository.save(event);
            return "notMyEventUnsubscribed";
        }
    }

    private static String getRequestBody(HttpServletRequest request) throws IOException {  // этот метод просто скопировал из stackoverflow чтобы обрабатывать запросы с фронта

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

    //---------------------------------------------------------------------------------------------------------------------

    @GetMapping("/editEvent{Id}")
    public String getEditEventForm(@PathVariable String Id, Model model)
    {
        var longId = Long.parseLong(Id);
        Event event = eventRepository.findById(longId).get();

        model.addAttribute("editEventForm", event);
        return "editEvent";
    }


    @PostMapping("/editEvent{Id}")
    public String editEvent(@PathVariable String Id, @ModelAttribute("eventForm") Event eventForm, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            return "editEvent" + Id;
        }

        eventValidator.validate(eventForm, bindingResult);

        if (eventValidator.getHasErrors()) {
            return "editEvent" + Id;
        }

        var longId = Long.parseLong(Id);
        Event event = eventRepository.findById(longId).get();

        event.setAddress(eventForm.getAddress());
        event.setDescription(eventForm.getDescription());
        event.setDatetime(eventForm.getDatetime());
        event.setTitle(eventForm.getTitle());

        eventRepository.save(event);

        return "redirect:/event" + Id;
    }


    @GetMapping("/event{Id}/chat")
    public String getEventChat(@PathVariable String Id, Model model) {
        var longId = Long.parseLong(Id);
        Event event = eventRepository.findById(longId).get();
        Set<Message> messagesSet = event.getMessages();

        var messagesArray = new Message[messagesSet.size()];
        var index = 0;

        for (Message message : messagesSet) {
            messagesArray[index] = message;
            index ++;
        }


        Arrays.sort(messagesArray);
        ArrayList<Message> messagesList = new ArrayList<Message>(Arrays.asList(messagesArray));

        model.addAttribute("messagesList", messagesList);
        model.addAttribute("Id", Id);
        return "eventChat";
    }


    @GetMapping("/event{Id}/chat/addMessage")
    public String getMessageAddingForm(@PathVariable String Id, Model model) {
        model.addAttribute("messageForm", new Message());
        model.addAttribute("Id", Id);
        return "addMessage";
    }

    @PostMapping("/event{Id}/chat/addMessage")
    public String addMessage(@PathVariable String Id, @ModelAttribute("messageForm") Message messageForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "addMessage";
        }

        messageValidator.validate(messageForm, bindingResult);

        if (messageValidator.getHasErrors()) {
            return "addEvent";
        }

        var longId = Long.parseLong(Id);
        Event event = eventRepository.findById(longId).get();

        var username = securityService.findLoggedInUsername();
        User user = userRepository.findByUsername(username);

        Message message = new Message(messageForm.getText(), event, user, new Date());

        event.getMessages().add(message);

        messageRepository.save(message);
        eventRepository.save(event);

        return "redirect:/event" + Id +"/chat/";
    }




}
