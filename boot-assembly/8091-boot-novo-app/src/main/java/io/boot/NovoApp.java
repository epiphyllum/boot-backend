/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 新模块
 *
 * @author epiphyllum.zhou@gmail.com
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class NovoApp {

    public static void main(String[] args) {
        SpringApplication.run(NovoApp.class, args);
    }

}