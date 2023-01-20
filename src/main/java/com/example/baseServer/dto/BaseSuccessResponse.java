package com.example.baseServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class BaseSuccessResponse {

    private Integer statusCode;
    private Boolean success;

    public static BaseSuccessResponse ok() {
        return new BaseSuccessResponse(1, true);
    }
}
