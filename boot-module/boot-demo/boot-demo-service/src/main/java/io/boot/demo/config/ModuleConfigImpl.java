/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.demo.config;

import io.boot.commons.tools.config.ModuleConfig;
import org.springframework.stereotype.Service;

/**
 * 模块配置信息
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
@Service
public class ModuleConfigImpl implements ModuleConfig {
    @Override
    public String getName() {
        return "api";
    }
}