/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot;

import apijson.framework.APIJSONApplication;
import apijson.framework.APIJSONCreator;
import apijson.orm.SQLConfig;
import apijson.orm.SQLExecutor;
import io.boot.config.ApiJsonSQLConfig;
import io.boot.config.ApiJsonSQLExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

/**
 * @author epiphyllum.zhou@gmail.com
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@Slf4j
public class ApiJsonApp {

    // 全局 ApplicationContext 实例，方便 getBean 拿到 Spring/SpringBoot 注入的类实例
    private static ApplicationContext APPLICATION_CONTEXT;

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    static {
        // 使用本项目的自定义处理类
        APIJSONApplication.DEFAULT_APIJSON_CREATOR = new APIJSONCreator<Long>() {
            @Override
            public SQLConfig<Long> createSQLConfig() {
                return new ApiJsonSQLConfig();
            }
            @Override
            public SQLExecutor<Long> createSQLExecutor() {
                return new ApiJsonSQLExecutor();
            }
        };
    }

    public static void main(String[] args) throws Exception {
        APPLICATION_CONTEXT = SpringApplication.run(ApiJsonApp.class, args);
        APIJSONApplication.init(false);  // 4.4.0 以上需要这句来保证以上 static 代码块中给 DEFAULT_APIJSON_CREATOR 赋值会生效
    }
}