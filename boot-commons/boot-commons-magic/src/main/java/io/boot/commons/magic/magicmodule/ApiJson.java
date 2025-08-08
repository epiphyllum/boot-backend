package io.boot.magic.magicmodule;

import apijson.RequestMethod;
import com.alibaba.fastjson.JSONObject;
import io.boot.commons.magic.config.MyParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.annotation.MagicModule;
import org.ssssssss.script.annotation.Comment;

import java.util.LinkedHashMap;

@Component  //注入到Spring容器中
@MagicModule("apijson")    // 模块名称:  脚本里可以: import test;
@Slf4j
public class ApiJson {

    // string body
    @Comment("调用apijson: query")
    public JSONObject get(String body) {
        return new MyParser(RequestMethod.GET).setNeedVerify(false).parseResponse(body);
    }

    // map body
    @Comment("调用apijson: query")
    public JSONObject get(LinkedHashMap<String, Object> body) {
        String bodyStr = JSONObject.toJSONString(body);
        return new MyParser(RequestMethod.GET)
                .setNeedVerify(false)
                .parseResponse(bodyStr);
    }

    // string body
    @Comment("调用apijson: 创建")
    public String create(String body) {
        return new MyParser(RequestMethod.POST).setNeedVerify(false).parse(body);
    }

    // map body
    @Comment("调用apijson: 创建")
    public String create(LinkedHashMap<String, Object> body) {
        String bodyStr = JSONObject.toJSONString(body);
        return new MyParser(RequestMethod.POST).setNeedVerify(false).parse(bodyStr);
    }

    // string body
    @Comment("调用apijson: 更新")
    public String update(String body) {
        return new MyParser(RequestMethod.PUT).setNeedVerify(false).parse(body);
    }

    // may body
    @Comment("调用apijson: 更新")
    public String update(LinkedHashMap<String, Object> body) {
        String bodyStr = JSONObject.toJSONString(body);
        return new MyParser(RequestMethod.PUT).setNeedVerify(false).parse(bodyStr);
    }
}
