/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.commons.security.enums;

/**
 * 用户状态
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
public enum UserStatusEnum {
    DISABLE(0),
    ENABLED(1);

    private final int value;

    UserStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
