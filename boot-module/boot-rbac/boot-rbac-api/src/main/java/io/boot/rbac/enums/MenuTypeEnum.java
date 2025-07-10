/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.rbac.enums;

/**
 * 菜单类型枚举
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
public enum MenuTypeEnum {
    /**
     * 菜单
     */
    MENU(0),
    /**
     * 按钮
     */
    BUTTON(1);

    private final int value;

    MenuTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
