package io.boot.controller;

import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.validator.ValidatorUtils;
import io.boot.commons.tools.validator.group.AddGroup;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequestMapping("health")
public class HealthCheckController {

    @GetMapping("check")
    public String check() {
        return "OK";
    }

    @GetMapping("codeEx")
    public String codeEx() {
        throw new BootException(500);
    }

    @GetMapping("messageEx")
    public String messageEx() {
        throw new BootException("字符串异常");
    }

    @GetMapping("validation")
    public String validation() {
        DemoDTO dto = new DemoDTO();
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        return "ok";
    }

    public static class DemoDTO implements Serializable {
        private static final long serialVersionUID = 1L;
        @NotNull(message = "{sysdept.pid.require}", groups = AddGroup.class)
        private Long id;
    }

}
