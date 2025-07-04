/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * xxl-job 定时任务
 *
 * @author epiphyllum.zhou@gmail.com
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class JobApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }

}