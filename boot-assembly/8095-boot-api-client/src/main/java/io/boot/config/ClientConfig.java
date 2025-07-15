package io.boot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// 客户端接入配置
@Data
@ConfigurationProperties("api-client")
@Component
public class ClientConfig {
    private String apiUrl;
    private String clientId;
    private String platformKey;
    private String privateKey;
}
