package com.unilife.service.impl;

import com.unilife.exception.ResourceNotFoundException;
import com.unilife.exception.UserAlreadyExistsException; // Placeholder for a more specific TopicAlreadyExistsException
import com.unilife.mapper.TopicMapper;
import com.unilife.model.dto.TopicDto;
import com.unilife.model.entity.Topic;
import com.unilife.service.TopicService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// @Service
public class TopicServiceImpl implements TopicService {

    private final TopicMapper topicMapper;

    // @Autowired
    public TopicServiceImpl(TopicMapper topicMapper) {
        this.topicMapper = topicMapper;
    }

    @Override
    // @Transactional
    public TopicDto createTopic(TopicDto topicDto) {
        if (topicMapper.findByName(topicDto.getName()) != null) {
            throw new UserAlreadyExistsException("Topic with name '" + topicDto.getName() + "' already exists.");
        }
        Topic topic = mapToEntity(topicDto);
        topicMapper.save(topic);
        return mapToDto(topic);
    }

    @Override
    public TopicDto getTopicById(Long id) {
        Topic topic = topicMapper.findById(id);
        if (topic == null) {
            throw new ResourceNotFoundException("Topic not found with id: " + id);
        }
        return mapToDto(topic);
    }

    @Override
    public TopicDto getTopicByName(String name) {
        Topic topic = topicMapper.findByName(name);
        if (topic == null) {
            throw new ResourceNotFoundException("Topic not found with name: " + name);
        }
        return mapToDto(topic);
    }

    @Override
    public List<TopicDto> getAllTopics() {
        List<Topic> topics = topicMapper.findAll();
        return topics.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    // @Transactional
    public TopicDto updateTopic(Long id, TopicDto topicDto) {
        Topic existingTopic = topicMapper.findById(id);
        if (existingTopic == null) {
            throw new ResourceNotFoundException("Topic not found with id: " + id);
        }

        // Check for name conflict if name is being changed
        if (topicDto.getName() != null && !topicDto.getName().equals(existingTopic.getName())) {
            if (topicMapper.findByName(topicDto.getName()) != null) {
                throw new UserAlreadyExistsException("Topic with name '" + topicDto.getName() + "' already exists.");
            }
            existingTopic.setName(topicDto.getName());
        }

        if (topicDto.getDescription() != null) {
            existingTopic.setDescription(topicDto.getDescription());
        }
        
        topicMapper.update(existingTopic);
        return mapToDto(existingTopic);
    }

    @Override
    // @Transactional
    public void deleteTopic(Long id) {
        Topic existingTopic = topicMapper.findById(id);
        if (existingTopic == null) {
            throw new ResourceNotFoundException("Topic not found with id: " + id);
        }
        // TODO: Consider implications: What happens to posts under this topic?
        // Option 1: Disallow deletion if posts exist.
        // Option 2: Set topic_id in posts to null.
        // Option 3: Delete all posts under this topic (cascade).
        // For now, simple deletion. This needs business logic decision.
        // Example check (requires PostMapper):
        // if (postMapper.countByTopicId(id) > 0) {
        //    throw new IllegalStateException("Cannot delete topic with id " + id + " as it has associated posts.");
        // }
        topicMapper.deleteById(id);
    }

    private TopicDto mapToDto(Topic topic) {
        if (topic == null) return null;
        return new TopicDto(topic.getId(), topic.getName(), topic.getDescription(), topic.getCreatedAt());
    }

    private Topic mapToEntity(TopicDto dto) {
        if (dto == null) return null;
        Topic topic = new Topic();
        // ID is not set from DTO for creation, but could be for update if DTO carries it
        topic.setName(dto.getName());
        topic.setDescription(dto.getDescription());
        // createdAt is set by entity constructor
        return topic;
    }
}
