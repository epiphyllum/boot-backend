/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot;

import io.boot.commons.magic.config.MySQLConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;

/**
 * @author epiphyllum.zhou@gmail.com
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MonoApp {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(MonoApp.class, args);
        MySQLConfig.setConfig(MySQLConfig.DATABASE_MYSQL, "bootdb", new HashMap<>(), applicationContext);
    }
}