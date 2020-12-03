package com.example.demo.entity;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="messages")
public class Message implements Comparable<Message> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    private Event event;

    @ManyToOne
    private User author;

    private String username;

    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    private Date datetime;

    public Message() {
    }

    public Message(String text, Event event, User author, Date datetime) {
        this.text = text;
        this.event = event;
        this.author = author;
        this.datetime = datetime;
        this.username = author.getUsername();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    @Override
    public int compareTo(Message other) {
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
