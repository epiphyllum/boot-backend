package io.boot.novo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class NovoController {

    @GetMapping("test")
    public String test() {
        return "test";
    }
}
