package io.boot.monolith.dto;

import lombok.Data;

@Data
public class BaseResponse {
    Integer code;
    String message;
}
