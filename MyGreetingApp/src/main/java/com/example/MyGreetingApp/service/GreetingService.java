package com.example.MyGreetingApp.service;


public interface GreetingService {
    String getSimpleGreeting();
    String getPersonalizedGreeting(String firstName, String lastName);
}
