/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.novo.exception;

import io.boot.commons.tools.exception.ErrorCode;

/**
 * 模块错误编码，由9位数字组成，前6位为模块编码，后3位为业务编码
 * <p>
 * 如：100001001（100001代表模块，001代表业务代码）
 * </p>
 *
 * @author epiphyllum.zhou@gmail.com
 */
public interface ModuleErrorCode extends ErrorCode {

}
