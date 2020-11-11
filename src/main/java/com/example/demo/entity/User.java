package com.example.demo.entity;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails
{
    //    @Column(name = "id", columnDefinition = "uuid")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String  password;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    private Date birthdate;

    private String description;

    //нужно будет учесть update  в бд
    @OneToMany(mappedBy = "user")
    private Set<Event> eventsIds;

    @OneToMany
    private Set<Event> subscribeTo;

    @Transient //не имеет отображения в БД
    private String passwordConfirm;


    //методы интерфейса UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    //геттеры-сеттеры

    public Long getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Set<Event> getEvents()
    {
        return eventsIds;
    }

    public void setEvents(Set<Event> eventsIds)
    {
        this.eventsIds = eventsIds;
    }

    public Set<Event> getSubscribeTo()
    {
        return subscribeTo;
    }

    public void setSubscribeTo(Set<Event> subscribeTo)
    {
        this.subscribeTo = subscribeTo;
    }

}