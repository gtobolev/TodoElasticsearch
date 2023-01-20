package com.example.baseServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class CustomSuccessResponse<T> {

    private T data;
    private Integer statusCode;
    private Boolean success;

    public static <T> CustomSuccessResponse<T> okData(T data) {
        return new CustomSuccessResponse(data, 1, true);
    }
}
