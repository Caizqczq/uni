package com.unilife.controller;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.exception.UserAlreadyExistsException;
import com.unilife.model.dto.TopicDto;
import com.unilife.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // For error responses
import org.springframework.http.HttpStatus; // For ResponseEntity
import org.springframework.http.ResponseEntity; // For ResponseEntity

// @RestController
// @RequestMapping("/api/topics")
@Tag(name = "Forum Topic Management", description = "APIs for managing forum topics.")
public class TopicController {

    private final TopicService topicService;

    // @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @Operation(summary = "Create a new forum topic", description = "Allows administrators to create a new forum topic. Topic names must be unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Topic created successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., topic name already exists)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized (not authenticated)"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user is not an ADMIN)")
    })
    // @PostMapping
    // @PreAuthorize("hasRole('ADMIN')") // Example: Admin only
    public /*ResponseEntity<?>*/ Object createTopic(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Details of the topic to create.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = TopicDto.class)))
            /*@RequestBody*/ TopicDto topicDto) {
        try {
            TopicDto createdTopic = topicService.createTopic(topicDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTopic);
        } catch (UserAlreadyExistsException e) { // Name conflict
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get all forum topics", description = "Retrieves a list of all available forum topics.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all topics",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto[].class)))
    })
    // @GetMapping
    public /*ResponseEntity<List<TopicDto>>*/ Object getAllTopics() {
        List<TopicDto> topics = topicService.getAllTopics();
        return ResponseEntity.ok(topics);
    }

    @Operation(summary = "Get a specific topic by ID", description = "Retrieves details of a specific forum topic by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved topic",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "404", description = "Topic not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @GetMapping("/{id}")
    public /*ResponseEntity<?>*/ Object getTopicById(
            @Parameter(description = "ID of the topic to retrieve.", required = true, example = "1")
            /*@PathVariable*/ Long id) {
        try {
            TopicDto topic = topicService.getTopicById(id);
            return ResponseEntity.ok(topic);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Update an existing forum topic", description = "Allows administrators to update the details of an existing forum topic. Topic names must remain unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Topic updated successfully",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., topic name conflict)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user is not an ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Topic not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')") // Example: Admin only
    public /*ResponseEntity<?>*/ Object updateTopic(
            @Parameter(description = "ID of the topic to update.", required = true, example = "1")
            /*@PathVariable*/ Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated details for the topic.", required = true,
                                                                   content = @Content(schema = @Schema(implementation = TopicDto.class)))
            /*@RequestBody*/ TopicDto topicDto) {
        try {
            TopicDto updatedTopic = topicService.updateTopic(id, topicDto);
            return ResponseEntity.ok(updatedTopic);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (UserAlreadyExistsException e) { // Name conflict
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Delete a forum topic", description = "Allows administrators to delete a forum topic. May fail if topic has associated posts (depending on service logic).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Topic deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden (user is not an ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Topic not found",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "409", description = "Conflict (e.g., topic has posts and cannot be deleted)",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    // @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')") // Example: Admin only
    public /*ResponseEntity<?>*/ Object deleteTopic(
            @Parameter(description = "ID of the topic to delete.", required = true, example = "1")
            /*@PathVariable*/ Long id) {
        try {
            topicService.deleteTopic(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) { // For cases like "cannot delete topic with posts"
             return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }
}
