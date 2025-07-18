/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.commons.log.enums;

/**
 * 登录操作枚举
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
public enum LoginOperationEnum {
    /**
     * 登录成功
     */
    SUCCESS(0),
    /**
     * 登录失败
     */
    FAIL(1),
    /**
     * 用户退出
     */
    LOGOUT(2);


    private final int value;

    LoginOperationEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}