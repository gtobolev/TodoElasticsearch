package com.example.baseServer.dto;

import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ChangeTextTodoDto {

    @Size(min = 3, max = 160)
    String text;
}
