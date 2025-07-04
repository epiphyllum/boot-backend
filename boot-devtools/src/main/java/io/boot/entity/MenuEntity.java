/**
 * Copyright (c) 2020 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.entity;

import lombok.Data;

/**
 * 创建菜单
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Data
public class MenuEntity {
    private Long pid;
    private String name;
    private String icon;
    private String backendUrl;
    private String className;

}
