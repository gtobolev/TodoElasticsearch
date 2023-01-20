package com.example.baseServer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class GetNewsDto<R> {

    private R content;
    private Integer numberOfElements;
    private Integer ready;
    private Integer notReady;
}
