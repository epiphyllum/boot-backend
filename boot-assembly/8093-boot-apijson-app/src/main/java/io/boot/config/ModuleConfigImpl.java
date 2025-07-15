package io.boot.config;

import io.boot.commons.tools.config.ModuleConfig;
import org.springframework.stereotype.Component;

@Component
public class ModuleConfigImpl implements ModuleConfig {
    @Override
    public String getName() {
        return "apijson";
    }
}
