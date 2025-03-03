package com.example.MyGreetingApp.service;


import com.example.MyGreetingApp.model.Greeting;

import java.util.List;
import java.util.Optional;

public interface GreetingService {
    String getSimpleGreeting();
    String getPersonalizedGreeting(String firstName, String lastName);
    Greeting saveGreeting(Greeting greeting);
    Optional<Greeting> findGreetingById(Long id);
    List<Greeting> getAllGreetings();
    Greeting updateGreeting(Long id, Greeting greeting);
}
