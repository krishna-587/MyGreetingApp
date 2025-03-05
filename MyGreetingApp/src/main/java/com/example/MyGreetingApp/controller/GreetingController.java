package com.example.MyGreetingApp.controller;

import com.example.MyGreetingApp.model.Greeting;
import com.example.MyGreetingApp.service.GreetingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/greetings")
@Tag(name = "Greeting Controller", description = "Handles greeting messages")
public class GreetingController {

    private final GreetingService greetingService;

    @Autowired
    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    // UC1: Return JSON for different HTTP Methods
    @GetMapping("/simple")
    public ResponseEntity<Map<String, String>> getSimpleGreeting() {
        String message = greetingService.getSimpleGreeting();
        return ResponseEntity.ok(Map.of("message", message));
    }

    // UC3: Personalized greeting based on user attributes
    @GetMapping("/personalized")
    public ResponseEntity<Map<String, String>> getPersonalizedGreeting(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        String message = greetingService.getPersonalizedGreeting(firstName, lastName);
        return ResponseEntity.ok(Map.of("message", message));
    }

    // UC4: Save greeting message
    @PostMapping
    public ResponseEntity<Greeting> createGreeting(@RequestBody Greeting greeting) {
        Greeting savedGreeting = greetingService.saveGreeting(greeting);
        return new ResponseEntity<>(savedGreeting, HttpStatus.CREATED);
    }

    // UC5: Find greeting by ID
    @GetMapping("/{id}")
    public ResponseEntity<Greeting> getGreetingById(@PathVariable Long id) {
        return greetingService.findGreetingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UC6: List all greetings
    @GetMapping
    public ResponseEntity<List<Greeting>> getAllGreetings() {
        List<Greeting> greetings = greetingService.getAllGreetings();
        return ResponseEntity.ok(greetings);
    }

    // UC7: Update greeting
    @PutMapping("/{id}")
    public ResponseEntity<Greeting> updateGreeting(
            @PathVariable Long id,
            @RequestBody Greeting greeting) {
        try {
            Greeting updatedGreeting = greetingService.updateGreeting(id, greeting);
            return ResponseEntity.ok(updatedGreeting);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // UC8: Delete greeting
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGreeting(@PathVariable Long id) {
        try {
            greetingService.deleteGreeting(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}