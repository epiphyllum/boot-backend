package io.boot.monolith.dto;

import lombok.Data;

@Data
public class DemoRequest extends BaseRequest<DemoResponse> {
    private Integer id;
}
