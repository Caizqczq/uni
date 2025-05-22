package com.unilife.model.entity;

import java.time.LocalDateTime;
// import javax.persistence.*; // JPA annotations for reference

public class PersonalEvent {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(nullable = false)
    private Long userId; // FK to User

    // @Column(nullable = false)
    private String title;

    // @Lob
    private String description;

    // @Column(nullable = false)
    private LocalDateTime startTime;

    // @Column(nullable = false)
    private LocalDateTime endTime;

    private String location;

    // @Column(nullable = false)
    private boolean isAllDay = false;

    private String color; // Hex code for frontend display

    // @Column(nullable = false)
    private boolean reminderEnabled = false;

    private LocalDateTime reminderTime; // When to send reminder

    // @Column(nullable = false)
    private boolean reminderSent = false; // New field

    public PersonalEvent() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAllDay() {
        return isAllDay;
    }

    public void setAllDay(boolean allDay) {
        isAllDay = allDay;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isReminderEnabled() {
        return reminderEnabled;
    }

    public void setReminderEnabled(boolean reminderEnabled) {
        this.reminderEnabled = reminderEnabled;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }

    @Override
    public String toString() {
        return "PersonalEvent{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", isAllDay=" + isAllDay +
                '}';
    }
}
