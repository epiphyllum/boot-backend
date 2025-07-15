package io.boot.controller;

import apijson.RequestMethod;
import apijson.StringUtil;
import io.boot.config.DemoParser;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

/**请求路由入口控制器，包括通用增删改查接口等，转交给 APIJSON 的 Parser 来处理
 * 具体见 SpringBoot 文档
 * https://www.springcloud.cc/spring-boot.html#boot-features-spring-mvc
 * 以及 APIJSON 通用文档 3.设计规范 3.1 操作方法  
 * https://github.com/Tencent/APIJSON/blob/master/Document.md#3.1
 * <br > 建议全通过HTTP POST来请求:
 * <br > 1.减少代码 - 客户端无需写HTTP GET,PUT等各种方式的请求代码
 * <br > 2.提高性能 - 无需URL encode和decode
 * <br > 3.调试方便 - 建议使用 APIAuto(http://apijson.cn/api) 或 Postman
 * @author Lemon
 */
@RestController
@RequestMapping("")
public class DemoController {

	public String parse(RequestMethod method, String request, HttpSession session) {
		return new DemoParser(method).setSession(session).setNeedVerify(false).parse(request);
	}

	@PostMapping(value = "{apiName}")
	public String apiHandle(@RequestBody String request, HttpSession session) {
		return parse(RequestMethod.CRUD, request, session);
	}

}