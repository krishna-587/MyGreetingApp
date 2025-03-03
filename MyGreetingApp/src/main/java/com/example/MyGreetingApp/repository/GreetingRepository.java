package com.example.MyGreetingApp.repository;


import com.example.MyGreetingApp.model.Greeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GreetingRepository extends JpaRepository<Greeting, Long> {
    // JpaRepository provides all CRUD operations by default
}
