package io.boot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("api-client")
public class ClientConfig {
    private String apiUrl;
    private String clientId;
    private String platformKey;
    private String privateKey;
}
