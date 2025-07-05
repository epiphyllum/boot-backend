package io.boot.controller;

import io.boot.commons.tools.exception.BootException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("health")
public class HealthCheckController {

    @GetMapping("message")
    public String message() {
        throw new BootException(403);
    }
}
