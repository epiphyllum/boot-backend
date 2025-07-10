/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot;

import io.boot.commons.dynamic.datasource.config.DynamicDataSourceConfig;
import io.boot.commons.dynamic.datasource.config.DynamicDataSourceFactory;
import io.boot.commons.dynamic.datasource.properties.DataSourceProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.datasource.model.MagicDynamicDataSource;
import org.ssssssss.magicapi.modules.db.provider.ColumnMapperProvider;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 新模块
 *
 * @author epiphyllum.zhou@gmail.com
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@Slf4j
public class MagicApp {


    // 动态数据源配置
    @Resource
    private DynamicDataSourceConfig dynamicDataSourceConfig;

    // 默认数据源配置
    @Resource
    private DataSourceProperties dataSourceProperties;

    @Bean
    public MagicDynamicDataSource magicDynamicDataSource() {
        MagicDynamicDataSource magicDynamicDataSource = new MagicDynamicDataSource();

        // 设置默认数据源
        DataSource defaultDatasource = DynamicDataSourceFactory.buildDruidDataSource(dataSourceProperties);
        magicDynamicDataSource.setDefault(defaultDatasource);

        // 设置动态数据源
        Map<Object, Object> dynamicDataSource = dynamicDataSourceConfig.getDynamicDataSource();
        dynamicDataSource.forEach((k, v) -> {
            magicDynamicDataSource.add((String) k, (DataSource) v);
        });

        return magicDynamicDataSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(MagicApp.class, args);
    }
}