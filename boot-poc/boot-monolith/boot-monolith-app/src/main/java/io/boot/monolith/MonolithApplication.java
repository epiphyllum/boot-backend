/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 消息模块
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MonolithApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonolithApplication.class, args);
    }
}