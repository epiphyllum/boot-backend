/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关服务
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApp {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class, args);
    }

}