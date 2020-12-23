package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl  implements UserService
{
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityService securityService;

    @Override
    public boolean save(User user)
    {
        var userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null)
        {
            return false;
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean editProfile(User user)
    {
        var newUsername = user.getUsername();
        if (userRepository.findByUsername(newUsername) != null)
        {
            var currentUsername = securityService.findLoggedInUsername();
            var userFromDb = userRepository.findByUsername(currentUsername);
            userFromDb.setDescription(user.getDescription());
            userFromDb.setBirthdate(user.getBirthdate());
            userRepository.save(userFromDb);
            return false;
        }
        var currentUsername = securityService.findLoggedInUsername();
        var userFromDb = userRepository.findByUsername(currentUsername);
        userFromDb.setDescription(user.getDescription());
        userFromDb.setBirthdate(user.getBirthdate());
        userFromDb.setUsername(user.getUsername());
        userRepository.save(userFromDb);
        return true;
    }


    @Override
    public User findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }
}
