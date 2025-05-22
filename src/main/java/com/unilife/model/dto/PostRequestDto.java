package com.unilife.model.dto;

// import javax.validation.constraints.NotBlank;
// import javax.validation.constraints.NotNull;
// import javax.validation.constraints.Size;

public class PostRequestDto {

    // @NotBlank(message = "Title cannot be blank")
    // @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    // @NotBlank(message = "Content cannot be blank")
    private String content;

    // @NotNull(message = "Topic ID cannot be null")
    private Long topicId;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    @Override
    public String toString() {
        return "PostRequestDto{" +
                "title='" + title + '\'' +
                ", content='" + (content != null ? content.substring(0, Math.min(content.length(), 50)) + "..." : "null") + '\'' +
                ", topicId=" + topicId +
                '}';
    }
}
