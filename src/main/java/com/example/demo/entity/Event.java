package com.example.demo.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="events")
public class Event implements Comparable<Event> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "event_id")
    private Long id;

    private String title;

    private String description;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    private Date datetime;

    private String address;

    @ManyToOne
    private User user;

    private String username;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "user_subscription",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private Set<User> subscribers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Date getDatetime() {
        return datetime;
    }

    public Event(String title, String description, Date datetime, String address, User user, Set<User> subscribers) {
        this.title = title;
        this.description = description;
        this.datetime = datetime;
        this.address = address;
        this.user = user;
        this.subscribers = subscribers;
        this.username = user.getUsername();
    }

    public Event() {

    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<User> subscribers) {
        this.subscribers = subscribers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int compareTo(Event other) {
        var currentDatetime = this.datetime;
        var otherDatetime = other.getDatetime();
        if (currentDatetime.before(otherDatetime)) {
            return -1;
        }
        else if (currentDatetime.after(otherDatetime)) {
            return 1;
        }
        return 0;
    }
}