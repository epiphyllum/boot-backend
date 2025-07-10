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
public enum UserTypeEnum {
    OPERATION(0),
    REGION(1),
    AGENT(2),
    SALESMAN(2);

    private int value;

    UserTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
