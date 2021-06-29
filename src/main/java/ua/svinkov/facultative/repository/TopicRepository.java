package ua.svinkov.facultative.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.svinkov.facultative.entity.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {

}
