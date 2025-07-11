package io.boot.config;


import io.boot.commons.security.user.SecurityUser;
import io.boot.commons.security.user.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.convert.ValueConverter;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.interceptor.RequestInterceptor;
import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.magicapi.core.model.JsonBean;
import org.ssssssss.magicapi.core.servlet.MagicHttpServletRequest;
import org.ssssssss.magicapi.core.servlet.MagicHttpServletResponse;
import org.ssssssss.script.MagicScriptContext;

import java.util.Set;

/**
 * magic-api 接口鉴权
 */
@Component
@Slf4j
public class PermissionInterceptor implements RequestInterceptor {

//    @Value("server.servlet.context-path")
//    private String contextPath;
//    @Value("magic-api.prefix")
//    private String apiPath;

    // 部分API接口可用户对外对接: 需要验证签名: todo

    /**
     * 接口请求之前
     * @param info    接口信息
     * @param context 脚本变量信息
     */
    @Override
    public Object preHandle(ApiInfo info, MagicScriptContext context, MagicHttpServletRequest request, MagicHttpServletResponse response) throws Exception {

         // info.getPath().startsWith()
        log.info("permission interceptor preHandle: {}", info);


        String requireLogin = info.getOptionValue("require_login");

        // 接口不需要登录
        if (StringUtils.isBlank(requireLogin) || "false".equals(requireLogin)) {
            log.debug("接口{}无需登录", info.getName());
            return null;
        }

        // 接口选项配置了需要登录
        UserDetail user = null;
        if ("true".equals(requireLogin)) {
            user = SecurityUser.getUser();
            if (user.getId() == null) {
                log.error("接口{}需登录, 没有登录信息", info.getName());
                return new JsonBean<>(401, "用户未登录");
            }
        } else {
            log.debug("接口{}无需登录", info.getName());
            return null;
        }

        // 接口权限配置
        String apiPermission = info.getOptionValue("permission");

        // 接口没有权限要求
        if (StringUtils.isBlank(apiPermission)) {
            log.debug("接口{}无权限限制", info.getName());
            return null;
        }

        // 用户权限集合
        Set<String> authoritySet = user.getAuthoritySet();

        // 用户的权限集合没有这个权限
        if (!authoritySet.contains(apiPermission)) {
            log.error("无权访问, {}请求接口{}, 接口权限:{}, 用户权限:{} ", user.getUsername(), info.getName(), apiPermission, authoritySet);
            return new JsonBean<>(403, "用户权限不足");
        }

        return null;
    }

    /**
     * 接口执行之后
     *
     * @param info    接口信息
     * @param context 变量信息
     * @param value   即将要返回到页面的值
     */
    @Override
    public Object postHandle(ApiInfo info, MagicScriptContext context, Object value, MagicHttpServletRequest request, MagicHttpServletResponse response) throws Exception {
        log.info("{} 执行完毕，返回结果:{}", info.getName(), value);
        return null;
    }
}