/**
 * Copyright (c) 2021 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.payment.controller;

import com.alipay.api.AlipayApiException;
import com.ijpay.alipay.AliPayApiConfig;

/**
 * 涉及支付宝接口调用的Controller，需要继承 AbstractAliPayApiController
 *
 * @author epiphyllum.zhou@gmail.com
 */
public abstract class AbstractAliPayApiController {
    /**
     * 获取支付宝配置
     *
     * @return {@link AliPayApiConfig} 支付宝配置
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public abstract AliPayApiConfig getApiConfig() throws AlipayApiException;
}
