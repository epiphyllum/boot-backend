package io.boot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Data
@EnableConfigurationProperties
@ConfigurationProperties("api-client")
public class ClientConfig {
    private String apiUrl;
    private String clientId;
    private String platformKey;
    private String privateKey;
}
