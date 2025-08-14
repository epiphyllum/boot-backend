package io.boot;


import io.boot.commons.magic.config.ApiJsonConfigProperties;
import io.boot.commons.magic.config.MySQLConfig;
import jakarta.annotation.Resource;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApiJsonConfigListener {

    @Resource
    private ApiJsonConfigProperties apiJsonConfigProperties; // 注入刷新后的配置类

    // 监听配置刷新事件
    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onConfigRefresh(RefreshScopeRefreshedEvent event) {
        MySQLConfig.setConfig(apiJsonConfigProperties);
        System.out.println("refresh table map...");
        System.out.println("----------------------------");
        apiJsonConfigProperties.getTableMap().forEach((k, v) -> {
            System.out.println(k + " -> " + v);
        });
    }
}
