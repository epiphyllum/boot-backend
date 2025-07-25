/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.commons.tools.xss;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.util.PathMatcher;

import java.io.IOException;

/**
 * XSS过滤
 *
 * @author epiphyllum.zhou@gmail.com
 */
@AllArgsConstructor
public class XssFilter implements Filter {
    private final XssProperties properties;
    private final PathMatcher pathMatcher;

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // 放行
        if (shouldNotFilter(httpServletRequest)) {
            chain.doFilter(request, response);

            return;
        }

        chain.doFilter(new XssHttpServletRequestWrapper(httpServletRequest), response);
    }

    private boolean shouldNotFilter(HttpServletRequest request) {
        // 放行不过滤的URL
        return properties.getExcludeUrls().stream().anyMatch(excludeUrl -> pathMatcher.match(excludeUrl, request.getServletPath()));
    }

    @Override
    public void destroy() {
    }

}