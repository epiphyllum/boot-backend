/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot;

import io.boot.commons.magic.config.ApiJsonConfigProperties;
import io.boot.commons.magic.config.MySQLConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

/**
 * @author epiphyllum.zhou@gmail.com
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(ApiJsonConfigProperties.class)
public class MonoApp {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(MonoApp.class, args);
        MySQLConfig.setConfig(applicationContext);
    }
}