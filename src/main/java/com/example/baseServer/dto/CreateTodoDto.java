package com.example.baseServer.dto;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CreateTodoDto {

    @Size(min = 3, max = 160)
    private String text;
}
