/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.commons.tools.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Http工具类
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
public class HttpContextUtils {

	public static HttpServletRequest getHttpServletRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if(requestAttributes == null){
			return null;
		}

		return ((ServletRequestAttributes) requestAttributes).getRequest();
	}

	public static Map<String, String> getParameterMap(HttpServletRequest request) {
		Enumeration<String> parameters = request.getParameterNames();

		Map<String, String> params = new HashMap<>();
		while (parameters.hasMoreElements()) {
			String parameter = parameters.nextElement();
			String value = request.getParameter(parameter);
			if (StringUtils.isNotBlank(value)) {
				params.put(parameter, value);
			}
		}

		return params;
	}

	public static String getLanguage() {
		//默认语言
		String defaultLanguage = "zh-CN";
		//request
		HttpServletRequest request = getHttpServletRequest();
		if(request == null){
			return defaultLanguage;
		}

		//请求语言
		defaultLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);

		return defaultLanguage;
	}
}