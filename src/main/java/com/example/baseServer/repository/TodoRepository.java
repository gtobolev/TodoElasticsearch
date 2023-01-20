package com.example.baseServer.repository;

import com.example.baseServer.entity.TodoEntity;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends ElasticsearchRepository<TodoEntity, String> {

    void deleteAllByStatus(Boolean status);
}
