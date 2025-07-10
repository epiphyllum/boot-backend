/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.commons.dynamic.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.boot.commons.dynamic.datasource.annotation.DataSource;
import io.boot.commons.dynamic.datasource.properties.DataSourceProperties;
import io.boot.commons.dynamic.datasource.properties.DynamicDataSourceProperties;
import jakarta.annotation.Resource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置多数据源
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DynamicDataSourceConfig {

    // 动态数据源配置:
    // dynamic:
    //    slave1:
    //      username:
    //    slave2
    //      username:
    @Resource
    private DynamicDataSourceProperties properties;

    // 默认数据源配置:  spring.datasource.druid
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }


    /**
     * 本身也是一个DataSource
     * DynamicDataSource < AbstractRoutingDataSource < AbstractDataSource < DataSource
     * 如果没有配置DynamicDataSource, 那么最终也会使用默认的那个spring.datasource.druid 这个datasource
    */
    @Bean
    public DynamicDataSource dynamicDataSource(DataSourceProperties dataSourceProperties) {

        // 动态数据源: dynamic: ds1, ds2
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(getDynamicDataSource());

        //默认数据源: spring.datasource.druid
        DruidDataSource defaultDataSource = DynamicDataSourceFactory.buildDruidDataSource(dataSourceProperties);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);

        return dynamicDataSource;
    }

    public Map<Object, Object> getDynamicDataSource() {

        // 获取动态数据源配置 map: k -> v: DataSourceProperties
        Map<String, DataSourceProperties> dataSourcePropertiesMap = properties.getDatasource();

        // 创建动态数据源: k -> v: DataSource
        Map<Object, Object> targetDataSources = new HashMap<>(dataSourcePropertiesMap.size());
        dataSourcePropertiesMap.forEach((k, v) -> {
            DruidDataSource druidDataSource = DynamicDataSourceFactory.buildDruidDataSource(v);
            targetDataSources.put(k, druidDataSource);
        });

        return targetDataSources;
    }

}