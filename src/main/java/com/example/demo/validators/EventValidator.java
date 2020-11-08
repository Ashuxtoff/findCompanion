package com.example.demo.validators;

import com.example.demo.entity.Event;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EventValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Event.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
