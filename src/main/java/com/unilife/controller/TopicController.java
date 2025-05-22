package com.unilife.controller;

import com.unilife.model.dto.TopicDto;
import com.unilife.service.TopicService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // For error responses

// @RestController
// @RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;

    // @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    // @PostMapping
    // @PreAuthorize("hasRole('ADMIN')") // Example: Admin only
    public /*ResponseEntity<?>*/ Object createTopic(/*@RequestBody*/ TopicDto topicDto) {
        try {
            TopicDto createdTopic = topicService.createTopic(topicDto);
            // return ResponseEntity.status(HttpStatus.CREATED).body(createdTopic);
            return createdTopic; // Simplified return for non-Spring Boot env
        } catch (Exception e) { // Catch more specific exceptions from service
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
             return Map.of("error", e.getMessage());
        }
    }

    // @GetMapping
    public /*ResponseEntity<List<TopicDto>>*/ Object getAllTopics() {
        List<TopicDto> topics = topicService.getAllTopics();
        // return ResponseEntity.ok(topics);
        return topics;
    }

    // @GetMapping("/{id}")
    public /*ResponseEntity<?>*/ Object getTopicById(/*@PathVariable*/ Long id) {
        try {
            TopicDto topic = topicService.getTopicById(id);
            // return ResponseEntity.ok(topic);
            return topic;
        } catch (Exception e) { // Catch ResourceNotFoundException
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')") // Example: Admin only
    public /*ResponseEntity<?>*/ Object updateTopic(/*@PathVariable*/ Long id, /*@RequestBody*/ TopicDto topicDto) {
        try {
            TopicDto updatedTopic = topicService.updateTopic(id, topicDto);
            // return ResponseEntity.ok(updatedTopic);
            return updatedTopic;
        } catch (Exception e) { // Catch ResourceNotFoundException, UserAlreadyExistsException (for name conflict)
            // if (e instanceof ResourceNotFoundException) {
            //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // }
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }

    // @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')") // Example: Admin only
    public /*ResponseEntity<?>*/ Object deleteTopic(/*@PathVariable*/ Long id) {
        try {
            topicService.deleteTopic(id);
            // return ResponseEntity.noContent().build();
            return Map.of("message", "Topic deleted successfully");
        } catch (Exception e) { // Catch ResourceNotFoundException, IllegalStateException (if topic has posts)
            // if (e instanceof ResourceNotFoundException) {
            //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            // }
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
            return Map.of("error", e.getMessage());
        }
    }
}
