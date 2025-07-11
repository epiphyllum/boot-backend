package io.boot.config;

import io.boot.commons.dynamic.datasource.config.DynamicDataSourceConfig;
import io.boot.commons.dynamic.datasource.config.DynamicDataSourceFactory;
import io.boot.commons.dynamic.datasource.properties.DataSourceProperties;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ssssssss.magicapi.datasource.model.MagicDynamicDataSource;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class MagicDataSourceConfig {
    // 动态数据源配置
    @Resource
    private DynamicDataSourceConfig dynamicDataSourceConfig;

    // 默认数据源配置
    @Resource
    private DataSourceProperties dataSourceProperties;

    @Bean
    public MagicDynamicDataSource magicDynamicDataSource() {
        // magic-api数据源
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
}
