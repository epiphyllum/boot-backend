/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot;

import io.boot.commons.tools.config.ModuleConfig;
import org.springframework.stereotype.Service;

/**
 * 模块配置信息
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Service
public class ModuleConfigImpl implements ModuleConfig {
    @Override
    public String getName() {
        return "omni";
    }
}