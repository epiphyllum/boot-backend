package io.boot.monolith.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import io.boot.commons.tools.utils.Result;
import lombok.Data;

@Data
public abstract class BaseRequest<T> {
    public TypeReference<Result<T>> resultClass() {
        return new TypeReference<Result<T>>() {
        };
    }
}
