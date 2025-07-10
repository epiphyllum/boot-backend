package io.boot.api.utils;

import org.springframework.core.ParameterizedTypeReference;

public abstract class BaseRequest<T> {
    public ParameterizedTypeReference<ApiResult<T>> resultClass() {
        return new ParameterizedTypeReference<ApiResult<T>>() {
        };
    }
}
