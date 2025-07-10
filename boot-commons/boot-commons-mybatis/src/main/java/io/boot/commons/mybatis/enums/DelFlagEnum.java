/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.commons.mybatis.enums;

/**
 * 删除标识枚举类
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
public enum DelFlagEnum {
    NORMAL(0),
    DEL(1);

    private int value;

    DelFlagEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
