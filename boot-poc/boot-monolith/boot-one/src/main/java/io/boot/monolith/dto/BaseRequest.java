package io.boot.monolith.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import io.boot.commons.tools.utils.Result;

public abstract class BaseRequest<T> {
    public TypeReference<Result<T>> resultClass() {
        return new TypeReference<Result<T>>() {
        };
    }
}
