package com.example.MyGreetingApp.service;


import com.example.MyGreetingApp.model.Greeting;
import com.example.MyGreetingApp.repository.GreetingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        if (firstName != null && lastName != null) {
            return "Hello " + firstName + " " + lastName;
        } else if (firstName != null) {
            return "Hello " + firstName;
        } else if (lastName != null) {
            return "Hello " + lastName;
        } else {
            return getSimpleGreeting();
        }
    }

    @Override
    public Greeting saveGreeting(@org.jetbrains.annotations.NotNull Greeting greeting) {
        if (greeting.getMessage() == null) {
            greeting.setMessage(getPersonalizedGreeting(greeting.getFirstName(), greeting.getLastName()));
        }
        return greetingRepository.save(greeting);
    }

    @Override
    public Optional<Greeting> findGreetingById(Long id) {
        return greetingRepository.findById(id);
    }

    @Override
    public List<Greeting> getAllGreetings() {
        return greetingRepository.findAll();
    }
}