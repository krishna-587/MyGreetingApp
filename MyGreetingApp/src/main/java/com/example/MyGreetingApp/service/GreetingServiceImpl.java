package com.example.MyGreetingApp.service;

import com.example.MyGreetingApp.repository.GreetingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GreetingServiceImpl implements GreetingService {

    private final GreetingRepository greetingRepository;

    @Autowired
    public GreetingServiceImpl(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    @Override
    public String getSimpleGreeting() {
        return "Hello World";
    }

    @Override
    public String getPersonalizedGreeting(String firstName, String lastName) {
        return "";
    }

}