/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package io.boot.controller;

import apijson.RequestMethod;
import apijson.StringUtil;
import io.boot.config.MyParser;
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
@RequestMapping("dev")
public class DevAPISONController {

	// TODO 这里关闭校验，方便新手快速测试，实际线上项目建议开启
	public String parse(RequestMethod method, String request, HttpSession session) {
		return new MyParser(method).setSession(session).setNeedVerify(false).parse(request);
	}


	//通用接口，非事务型操作 和 简单事务型操作 都可通过这些接口自动化实现<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**获取
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GET}
	 */
	@PostMapping(value = "get")
	public String get(@RequestBody String request, HttpSession session) {
		return parse(RequestMethod.GET, request, session);
	}


	/**增删改查统一接口，这个一个接口可替代 7 个万能通用接口，牺牲一些路由解析性能来提升一点开发效率
	 * @param method
	 * @param request
	 * @param session
	 * @return
	 */
	@PostMapping(value = "{method}")  // 如果和其它的接口 URL 冲突，可以加前缀，例如改为 crud/{method} 或 Controller 注解 @RequestMapping("crud")
	public String crud(@PathVariable String method, @RequestBody String request, HttpSession session) {
		return parse(RequestMethod.valueOf(StringUtil.toUpperCase(method)), request, session);
	}

	/**获取
	 * 只为兼容HTTP GET请求，推荐用HTTP POST，可删除
	 * @param request 只用String，避免encode后未decode
	 * @param session
	 * @return
	 * @see {@link RequestMethod#GET}
	 */
	@GetMapping("get/{request}")
	public String openGet(@PathVariable String request, HttpSession session) {
		try {
			request = URLDecoder.decode(request, StringUtil.UTF_8);
		} catch (Exception e) {
			// Parser 会报错
		}
		return get(request, session);
	}

}