package com.example.demo.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService // обязательная спринговая вещь для аутентификации и определения метода нахождения пользователя. User Details - носитель инфы об аутентификации пользователя
{
    private static final String ROLE_NAME = "SIMPLE_ROLE";  // нет ограничения по ролям
    protected static final Set<GrantedAuthority> GRANTED_AUTHORITIES = Set.of(new SimpleGrantedAuthority(ROLE_NAME));  // нужно для создания пользователя в loadByUsername

    @Autowired
    private UserRepository userRepository;  // для работы с БД (findByUsername)

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException // возвращает пользователя по юзернейму
    {
        User user = userRepository.findByUsername(username);
        if (user == null)
        {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), GRANTED_AUTHORITIES);
    }
}
