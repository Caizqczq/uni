package com.unilife.service;

import com.unilife.model.dto.TopicDto;
import java.util.List;

public interface TopicService {
    TopicDto createTopic(TopicDto topicDto);
    TopicDto getTopicById(Long id);
    TopicDto getTopicByName(String name); // Added for convenience
    List<TopicDto> getAllTopics();
    TopicDto updateTopic(Long id, TopicDto topicDto);
    void deleteTopic(Long id);
}
