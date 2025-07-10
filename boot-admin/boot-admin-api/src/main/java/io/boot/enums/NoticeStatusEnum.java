/**
 * Copyright (c) 2016-2020 BootCloud All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.enums;

/**
 * 通知状态枚举
 *
 * @author Mark sunlightcs@gmail.com
 */
public enum NoticeStatusEnum {
    /**
     * 草稿
     */
    DRAFT(0),
    /**
     * 发送
     */
    SEND(1);

    private final int value;

    NoticeStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
