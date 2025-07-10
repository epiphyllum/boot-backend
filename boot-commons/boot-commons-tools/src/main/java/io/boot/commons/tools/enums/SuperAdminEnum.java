/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.commons.tools.enums;

/**
 * 超级管理员枚举
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
public enum SuperAdminEnum {
    YES(1),
    NO(0);

    private int value;

    SuperAdminEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}