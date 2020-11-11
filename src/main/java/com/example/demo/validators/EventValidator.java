package com.example.demo.validators;

import com.example.demo.entity.Event;
import com.example.demo.entity.User;
import com.example.demo.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;

@Component
public class EventValidator implements Validator {

    private boolean hasErrors;

     public boolean getHasErrors() {
        return  hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Event.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Event event = (Event)target;

        setHasErrors(false);

        if (event.getTitle() == null) {
            errors.rejectValue("title", "NotEmptyTitle", "Title is required!");
            setHasErrors(true);
        }

        if (event.getDatetime() != null) {
            var currentDate = new Date();
            if (event.getDatetime().before(currentDate))
            {
                errors.rejectValue("datetime", "IncorrectDatetime", "Datetime must be later then current date");
                setHasErrors(true);
            }
        }

    }
}
