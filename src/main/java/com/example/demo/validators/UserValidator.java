package com.example.demo.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import java.util.Date;

@Component
public class UserValidator implements Validator {
    @Autowired
    private UserService userService;


    @Override
    public boolean supports(Class<?> aClass)
    {
        return User.class.equals(aClass);
    }

    private String context;

    private boolean hasErrors;

    public boolean getHasErrors() {
        return  hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public void validate(Object o, Errors errors)
    {
        User user = (User) o;

        setHasErrors(false);

        if (userService.findByUsername(user.getUsername()) != null && this.context.equals("registration")) {
            errors.rejectValue("username", "ThisUsernameIsAlreadyUsed", "This username is already used");
            setHasErrors(true);
        }

        if (user.getUsername() == null) {
            errors.rejectValue("username", "NotEmpty", "Username is required!");
            setHasErrors(true);
        }

        if (this.context.equals("registration") && !user.getPassword().equals(user.getPasswordConfirm()))
        {
            errors.rejectValue("password", "PasswordsAreNotEqual", "Passwords aren't equal");
            setHasErrors(true);
        }

        if (user.getBirthdate() != null) {
            var currentDate = new Date();
            if (user.getBirthdate().after(currentDate) || user.getBirthdate().equals(currentDate))
            {
                errors.rejectValue("birthdate", "IncorrectBirthdate", "Birthdate must be earlier then current date");
                setHasErrors(true);
            }
        }

        if (this.context.equals("registration") && !userService.save(user))
        {
            errors.rejectValue("passwordConfirm", "UserHasAlreadyBeenCreated", "User has already been created");
            setHasErrors(true);
        }
    }
}
