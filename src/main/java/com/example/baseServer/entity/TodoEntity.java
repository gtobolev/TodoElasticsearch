package com.example.baseServer.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "todo")
public class TodoEntity {
    @Id
    private String id;

    @Field(type = FieldType.Auto)
    private String createdAt;

    @Field(type = FieldType.Boolean)
    private Boolean status = false;

    @Field(type = FieldType.Auto)
    private String text;

    @Field(type = FieldType.Auto)
    private String updatedAt;
}
