/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.utils;

import lombok.Data;

/**
 * 模板管理
 *
 * @author epiphyllum.zhou@gmail.com
 */

@Data
public class TemplateContent {
    /**
     * 模板名称
     */
    private String name;
    /**
     * 模板内容
     */
    private String content;
    /**
     * 生成代码的路径
     */
    private String path;

}