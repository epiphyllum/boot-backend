package io.boot.magicmodule;

import io.boot.commons.tools.utils.HttpContextUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.annotation.MagicModule;
import org.ssssssss.script.annotation.Comment;

import java.util.Map;

@Component  //注入到Spring容器中
@MagicModule("apijson")    // 模块名称:  脚本里可以: import test;
public class ApiJson {

    @Comment("调用APIJSON")
    public String executeRaw(String body) {
        HttpSession session = HttpContextUtils.getHttpServletRequest().getSession();
        // 去调用APIJSON: todo
        String rtnBody = "{}";
        // rtnBody = new DemoParser("CRUD").setSession(session).setNeedVerify(false).parse(body);
        return rtnBody;
    }

    @Comment("调用APIJSON")
    public String executeTemplate(String template, Map<String, Object> params) {
        // template + params --->  body
        String body = template + params.toString();
        HttpSession session = HttpContextUtils.getHttpServletRequest().getSession();
        // 去调用APIJSON: todo
        String rtnBody = "{}";
        // rtnBody = new DemoParser("CRUD").setSession(session).setNeedVerify(false).parse(body);
        return rtnBody;
    }

}
