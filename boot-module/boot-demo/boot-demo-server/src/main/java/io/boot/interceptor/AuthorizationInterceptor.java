/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.interceptor;

import io.boot.annotation.Login;
import io.boot.commons.tools.exception.RenException;
import io.boot.entity.TokenEntity;
import io.boot.exception.ModuleErrorCode;
import io.boot.service.TokenService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 权限(Token)验证
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Resource
    private TokenService tokenService;

    public static final String USER_KEY = "userId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Login annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
        } else {
            return true;
        }

        if (annotation == null) {
            return true;
        }

        //从header中获取token
        String token = request.getHeader("token");
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }

        //token为空
        if (StringUtils.isBlank(token)) {
            throw new RenException(ModuleErrorCode.TOKEN_NOT_EMPTY);
        }

        //查询token信息
        TokenEntity tokenEntity = tokenService.getByToken(token);
        if (tokenEntity == null || tokenEntity.getExpireDate().getTime() < System.currentTimeMillis()) {
            throw new RenException(ModuleErrorCode.TOKEN_INVALID);
        }

        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(USER_KEY, tokenEntity.getUserId());

        return true;
    }
}
