package io.boot.config;

import apijson.RequestMethod;
import apijson.orm.AbstractFunctionParser;
import com.alibaba.fastjson.JSONObject;


/**可远程调用的函数类，用于自定义业务逻辑处理
 * 具体见 https://github.com/Tencent/APIJSON/issues/101
 * @author Lemon
 */
public class MyFunctionParser extends AbstractFunctionParser<Long> {
	public static final String TAG = "MyFunctionParser";

	public MyFunctionParser() {
		this(null, null, 0, null);
	}
	public MyFunctionParser(RequestMethod method, String tag, int version, JSONObject request) {
		super(method, tag, version, request);
	}
}