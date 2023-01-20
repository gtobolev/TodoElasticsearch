package com.example.baseServer.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import com.example.baseServer.dto.BaseSuccessResponse;
import com.example.baseServer.dto.ChangeStatusTodoDto;
import com.example.baseServer.dto.ChangeTextTodoDto;
import com.example.baseServer.dto.CreateTodoDto;
import com.example.baseServer.dto.CustomSuccessResponse;
import com.example.baseServer.dto.GetNewsDto;
import com.example.baseServer.entity.TodoEntity;
import com.example.baseServer.repository.TodoRepository;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final RestHighLevelClient esClient;

    public CustomSuccessResponse<TodoEntity> create(CreateTodoDto dto) {

        TodoEntity entity = new TodoEntity();
        entity.setText(dto.getText());
        entity.setId(UUID.randomUUID().toString());
        entity.setCreatedAt(LocalDateTime.now().toString());
        entity.setUpdatedAt(LocalDateTime.now().toString());
        todoRepository.save(entity);

        return CustomSuccessResponse.okData(entity);
    }

    @Transactional
    public BaseSuccessResponse delete(String id) {

        todoRepository.deleteById(id);

        return BaseSuccessResponse.ok();
    }

    @Transactional
    public BaseSuccessResponse patch(ChangeStatusTodoDto statusDto) throws IOException {

        SearchRequest searchRequest = new SearchRequest("todo");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery()).size(10000);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);


        List<TodoEntity> articles = new ArrayList<>();

        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String text = (String) sourceAsMap.get("text");
            String id = (String) sourceAsMap.get("id");
            String createdAt = (String) sourceAsMap.get("createdAt");
            String updatedAt = (String) sourceAsMap.get("updatedAt");

            TodoEntity article = new TodoEntity();
            article.setText(text);
            article.setStatus(statusDto.getStatus());
            article.setId(id);
            article.setCreatedAt(createdAt);
            article.setUpdatedAt(updatedAt);
            articles.add(article);
        }

        todoRepository.saveAll(articles);

        return BaseSuccessResponse.ok();
    }

    @Transactional
    public BaseSuccessResponse patchStatus(ChangeStatusTodoDto statusTodoDto, String id) {

        TodoEntity entity =  todoRepository.findById(id).orElseThrow();
        entity.setStatus(statusTodoDto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now().toString());
        todoRepository.save(entity);

        return BaseSuccessResponse.ok();
    }

    @Transactional
    public BaseSuccessResponse patchText(ChangeTextTodoDto textTodoDto, String id) {

        TodoEntity entity =  todoRepository.findById(id).orElseThrow();
        entity.setText(textTodoDto.getText());
        entity.setUpdatedAt(LocalDateTime.now().toString());
        todoRepository.save(entity);

        return BaseSuccessResponse.ok();
    }

    @Transactional
    public BaseSuccessResponse deleteAllReady() throws IOException {

        todoRepository.deleteAllByStatus(true);

        return BaseSuccessResponse.ok();
    }

    public CustomSuccessResponse getPaginated(Integer page, Integer perPage, Boolean statusFromRequest) throws Exception {

        SearchRequest searchRequest = new SearchRequest("todo");
        CountRequest countRequest = new CountRequest("todo");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        countRequest.source(searchSourceBuilder.query(QueryBuilders.matchAllQuery()));
        Long countAll = esClient.count(countRequest, RequestOptions.DEFAULT).getCount();

        countRequest.source(searchSourceBuilder.query(QueryBuilders.matchQuery("status", "true")));
        Long countReady = esClient.count(countRequest, RequestOptions.DEFAULT).getCount();

        countRequest.source(searchSourceBuilder.query(QueryBuilders.matchQuery("status", "false")));
        Long countNotReady = esClient.count(countRequest, RequestOptions.DEFAULT).getCount();

        if (statusFromRequest == null) {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        } else {
            searchSourceBuilder.query(QueryBuilders.matchQuery("status", statusFromRequest));
        }

        var offset =  (page - 1) * perPage;

        searchSourceBuilder
                    .from(offset)
                    .size(perPage)
                    .trackTotalHits(true)
                    .sort("createdAt", SortOrder.DESC);

            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

            List<TodoEntity> articles = new ArrayList<>();

            for (SearchHit hit : searchResponse.getHits().getHits()) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                String text = (String) sourceAsMap.get("text");
                Boolean status = (Boolean) sourceAsMap.get("status");
                String id = (String) sourceAsMap.get("id");
                String createdAt = (String) sourceAsMap.get("createdAt");
                String updatedAt = (String) sourceAsMap.get("updatedAt");

                TodoEntity article = new TodoEntity();
                article.setText(text);
                article.setStatus(status);
                article.setId(id);
                article.setCreatedAt(createdAt);
                article.setUpdatedAt(updatedAt);
                articles.add(article);
            }

        return CustomSuccessResponse.okData(
                        new GetNewsDto()
                                .setContent(articles)
                                .setReady(countReady.intValue())
                                .setNotReady(countNotReady.intValue())
                                .setNumberOfElements(countAll.intValue()));
    }
}
