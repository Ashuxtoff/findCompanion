package com.example.demo.validators;

import com.example.demo.entity.Event;
import com.example.demo.entity.Message;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class MessageValidator implements Validator {


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

        Message message = (Message) target;

        setHasErrors(false);

        if (message.getText().length() == 0) {
            errors.rejectValue("text", "MessageCodeIsEmpty", "Message cannot be empty");
            setHasErrors(true);
        }
    }
}
