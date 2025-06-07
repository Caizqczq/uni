package com.unilife.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

@Schema(description = "Data Transfer Object for user's course schedule entries.")
public class CourseDto {

    @Schema(description = "Unique identifier of the course entry.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "User ID this course entry belongs to. Usually set by the system.", example = "100", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "Name of the course.", example = "Advanced Calculus", requiredMode = Schema.RequiredMode.REQUIRED)
    private String courseName;

    @Schema(description = "Name of the teacher or instructor.", example = "Prof. Einstein", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String teacherName;

    @Schema(description = "Classroom or location of the course.", example = "Room 301, Science Hall", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String classroom;

    @Schema(description = "Day of the week (1 for Monday, 7 for Sunday).", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private int dayOfWeek;

    @Schema(description = "Start time of the course.", example = "09:00:00", requiredMode = Schema.RequiredMode.REQUIRED, type = "string", format = "time")
    private LocalTime startTime;

    @Schema(description = "End time of the course.", example = "10:30:00", requiredMode = Schema.RequiredMode.REQUIRED, type = "string", format = "time")
    private LocalTime endTime;

    @Schema(description = "Week type (0 for all weeks, 1 for odd weeks, 2 for even weeks).", example = "0", defaultValue = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private int weekType;

    @Schema(description = "Starting week number for the course.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private int startWeek;

    @Schema(description = "Ending week number for the course.", example = "16", requiredMode = Schema.RequiredMode.REQUIRED)
    private int endWeek;

    @Schema(description = "Additional notes for the course.", example = "Bring calculator", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String notes;

    @Schema(description = "Color code (hex) for displaying the course in the schedule.", example = "#FF5733", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String color;

    @Schema(description = "Flag indicating if the course was imported from an external API.", example = "false", accessMode = Schema.AccessMode.READ_ONLY)
    private boolean importedFromApi;

    // Constructors
    public CourseDto() {
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
        return "CourseDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", courseName='" + courseName + '\'' +
                ", dayOfWeek=" + dayOfWeek +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
