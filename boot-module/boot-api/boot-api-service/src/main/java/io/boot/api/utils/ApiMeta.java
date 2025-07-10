package io.boot.api.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
public class ApiMeta {
    private Object instance;
    private Method method;
    private Class<?> reqClass;
}

