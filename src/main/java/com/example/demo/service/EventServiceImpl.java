package com.example.demo.service;

import javax.inject.Inject;
import com.example.demo.entity.Event;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventServiceImpl implements EventService {

//    @Inject
//    private SessionFactory sessionFactory;
//
//    @Override
//    @Transactional
//    public void create(Event event) {
//        Session session = sessionFactory.getCurrentSession();
//        session.save(event);
//        session.flush();
//    }
//
//    @Override
//    @Transactional
//    public void update(Event event) {
//        Session session = sessionFactory.getCurrentSession();
//        session.update(event);
//        session.flush();
//    }
//
//    @Override
//    @Transactional
//    public void delete(Event event) {
//        Session session = sessionFactory.getCurrentSession();
//        session.delete(event);
//        session.flush();
//    }
//
//    @Override
//    @Transactional
//    public Event findById(Long id) {
//        Session session = sessionFactory.getCurrentSession();
//        Event event = session.get(Event.class, id);
//        return event;
//    }
}
