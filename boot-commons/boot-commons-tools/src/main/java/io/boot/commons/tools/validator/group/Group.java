/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.commons.tools.validator.group;


import jakarta.validation.GroupSequence;

/**
 * 定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {

}
