package io.boot.commons.magic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties
public class ApiJsonConfigProperties {
    private String dbType;                // 什么数据库
    private String schema;                // 什么schema
    private Map<String, String> tableMap; // 表明映射
}

