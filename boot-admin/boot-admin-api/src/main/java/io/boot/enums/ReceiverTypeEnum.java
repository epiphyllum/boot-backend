/**
 * Copyright (c) 2016-2020 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.enums;

/**
 * 接受者类型枚举
 *
 * @author epiphyllum.zhou@gmail.com
 */
public enum ReceiverTypeEnum {
    /**
     * 全部
     */
    ALL(0),
    /**
     * 部门
     */
    DEPT(1);

    private final int value;

    ReceiverTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
