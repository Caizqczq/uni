package com.unilife.model.enums;

public enum PointActionType {
    CREATE_POST(10, "Created a new post"),
    CREATE_COMMENT(2, "Added a new comment"),
    POST_RECEIVE_LIKE(1, "Your post received a like"), // Awarded to the post author
    UPLOAD_SHARED_FILE(15, "Uploaded a shared file"),
    CREATE_SHARED_DOCUMENT(5, "Created a shared document"),
    DAILY_LOGIN(1, "Daily login bonus");
    // Add more as needed, e.g., COMPLETE_STUDY_GOAL(20)

    private final int points;
    private final String defaultDescription;

    PointActionType(int points, String defaultDescription) {
        this.points = points;
        this.defaultDescription = defaultDescription;
    }

    public int getPoints() {
        return points;
    }

    public String getDefaultDescription() {
        return defaultDescription;
    }
}
