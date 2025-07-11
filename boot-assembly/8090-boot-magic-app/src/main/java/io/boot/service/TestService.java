package io.boot.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.FutureTask;

@Service
public class TestService {
    public String echo(String msg) {
        FutureTask t = null;
        return "hello " + msg;
    }
}
