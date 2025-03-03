package com.example.MyGreetingApp.service;


import com.example.MyGreetingApp.model.Greeting;

public interface GreetingService {
    String getSimpleGreeting();
    String getPersonalizedGreeting(String firstName, String lastName);
    Greeting saveGreeting(Greeting greeting);
}
