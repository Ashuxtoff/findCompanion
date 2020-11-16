package com.example.demo.repository;

import com.example.demo.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long>
{
    User findByUsername(String username);


}
