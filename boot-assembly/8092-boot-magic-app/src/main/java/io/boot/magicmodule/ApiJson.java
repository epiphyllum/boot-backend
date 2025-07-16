package io.boot.magicmodule;

import com.alibaba.fastjson.JSONObject;
import io.boot.commons.tools.utils.HttpContextUtils;
import io.boot.config.MyParser;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.annotation.MagicModule;
import org.ssssssss.script.annotation.Comment;
import apijson.RequestMethod;

import java.util.Map;

@Component  //注入到Spring容器中
@MagicModule("apijson")    // 模块名称:  脚本里可以: import test;
@Slf4j
public class ApiJson {

    @Comment("调用apijson")
    public JSONObject get(String body) {
        log.info("get: {} {}", body.getClass(),body);
        return new MyParser(RequestMethod.GET).setNeedVerify(false).parseResponse(body);
    }

    // 新增加
    @Comment("调用apijson")
    public String post(String body) {
        return new MyParser(RequestMethod.POST).setNeedVerify(false).parse(body);
    }

    // 更新
    @Comment("调用apijson")
    public String put(String body) {
        return new MyParser(RequestMethod.PUT).setNeedVerify(false).parse(body);
    }

    @Comment("调用apijson")
    public String get(String template, Map<String, Object> params) {
        // todo: 模板 + 参数 拿到最终的的request
        return null;
    }

}
