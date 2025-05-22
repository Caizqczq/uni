package com.unilife.model.entity;

import java.time.LocalTime;
// import javax.persistence.*; // JPA annotations for reference

public class Course {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(nullable = false)
    private Long userId; // FK to User

    // @Column(nullable = false)
    private String courseName;

    private String teacherName;
    private String classroom;

    // @Column(nullable = false)
    private int dayOfWeek; // 1 for Monday, 7 for Sunday

    // @Column(nullable = false)
    private LocalTime startTime;

    // @Column(nullable = false)
    private LocalTime endTime;

    private int weekType = 0; // 0 for all weeks, 1 for odd, 2 for even
    private int startWeek;
    private int endWeek;
    private String notes;
    private String color; // Hex code for frontend display
    private boolean importedFromApi = false;

    public Course() {
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getWeekType() {
        return weekType;
    }

    public void setWeekType(int weekType) {
        this.weekType = weekType;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isImportedFromApi() {
        return importedFromApi;
    }

    public void setImportedFromApi(boolean importedFromApi) {
        this.importedFromApi = importedFromApi;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", userId=" + userId +
                ", courseName='" + courseName + '\'' +
                ", dayOfWeek=" + dayOfWeek +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", weekType=" + weekType +
                '}';
    }
}
