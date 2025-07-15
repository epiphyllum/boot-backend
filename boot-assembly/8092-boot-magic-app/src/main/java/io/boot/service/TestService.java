package io.boot.service;

import io.boot.commons.dynamic.datasource.config.DynamicDataSource;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.FutureTask;

@Service
public class TestService {

    @Resource
    private DynamicDataSource ds;

    public String echo(String msg) {

        FutureTask t = null;
        return "hello " + msg;
    }
}
