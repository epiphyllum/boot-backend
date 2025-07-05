/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 监控中心
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
public class MonitorApp {

    public static void main(String[] args) {
        SpringApplication.run(MonitorApp.class, args);
    }

}