/**
 * Copyright (c) 2021 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.payment.Interceptor;

import com.alipay.api.AlipayApiException;
import com.ijpay.alipay.AliPayApiConfigKit;
import io.boot.payment.controller.AbstractAliPayApiController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 支付宝支付拦截器，需要在WebMvcConfig.class配置拦截路径[/pay/alipay/**]4
 *
 * @author epiphyllum.zhou@gmail.com
 */
public class AliPayInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws AlipayApiException {
        if (HandlerMethod.class.equals(handler.getClass())) {
            HandlerMethod method = (HandlerMethod) handler;
            Object controller = method.getBean();
            if (!(controller instanceof AbstractAliPayApiController)) {
                throw new RuntimeException("控制器需要继承 AbstractAliPayApiController");
            }
            AliPayApiConfigKit.setThreadLocalAliPayApiConfig(((AbstractAliPayApiController) controller).getApiConfig());
            return true;
        }
        return false;
    }
}