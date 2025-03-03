package com.example.MyGreetingApp.controller;

import com.example.MyGreetingApp.model.Greeting;
import com.example.MyGreetingApp.service.GreetingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/greetings")
public class GreetingController {

    // UC1: Return JSON for different HTTP Methods
    @RequestMapping(value = {"/", "/hello"})
    public Map<String, String> sayHello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello, how are you?");
        return response;
    }

    @RequestMapping(value = "/goodbye", method = RequestMethod.GET)
    public Map<String, String> sayGoodbye(@RequestParam(value = "name") String name) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Goodbye " + name + "!");
        return response;
    }

    @GetMapping("/param/{message}")
    public Map<String, String> sayHelloMessage(@PathVariable String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }

    private final GreetingService greetingService;

    @Autowired
    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    // UC2: Return JSON for different HTTP Methods

    @GetMapping("/simple")
    public ResponseEntity<Map<String, String>> getSimpleGreeting() {
        String message = greetingService.getSimpleGreeting();
        return ResponseEntity.ok(Map.of("message", message));
    }

}
