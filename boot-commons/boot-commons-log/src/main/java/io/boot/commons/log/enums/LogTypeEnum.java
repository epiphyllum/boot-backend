/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.commons.log.enums;

/**
 * 日志类型枚举
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
public enum LogTypeEnum {
    /**
     * 登录日志
     */
    LOGIN(0),
    /**
     * 操作日志
     */
    OPERATION(1),
    /**
     * 异常日志
     */
    ERROR(2);

    private final int value;

    LogTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}