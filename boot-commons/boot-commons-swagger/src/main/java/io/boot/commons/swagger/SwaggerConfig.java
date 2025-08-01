/**
 * Copyright (c) 2019 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.commons.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Swagger配置
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi userApi() {
        String[] paths = {"/**"};
        String[] packagedToMatch = {"io.boot"};
        return GroupedOpenApi.builder().group("default")
                .pathsToMatch(paths)
                .packagesToScan(packagedToMatch).build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact();
        contact.setName("BootCloud");

        return new OpenAPI().info(new Info()
                .title("BootCloud")
                .description("接口文档")
                .contact(contact)
                .version("4.0")
                .termsOfService("https://www.bootcloud.io/community"));
    }
}
