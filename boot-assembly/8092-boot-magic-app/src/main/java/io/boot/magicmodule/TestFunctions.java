package io.boot.magicmodule;
import io.boot.commons.tools.utils.HttpContextUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.annotation.MagicModule;
import org.ssssssss.script.annotation.Comment;

import java.util.Map;

@Component  //注入到Spring容器中
@MagicModule("test")    // 模块名称:  脚本里可以: import test;
public class TestFunctions {


    /**
     *   调用打印方法
     */
    @Comment("方法名的注释(用于提示)")
    public void println(@Comment("参数名的提示(用于提示)")String value) {
        System.out.println(value);
    }

    @Comment("call api json")
    public void callApiJSON(String body, Map<String, Object> params) {
        HttpSession session = HttpContextUtils.getHttpServletRequest().getSession();
        // 去调用APIJSON: todo
    }

    // 没法做权限
    @Comment("call api")
    public void callApi(String apiName, Map<String, Object> params) {
        HttpSession session = HttpContextUtils.getHttpServletRequest().getSession();
        params.forEach((k, v) -> {
            System.out.println(k + " -> " + v);
        });
        System.out.println(session);
        // 去调用APIJSON: todo
    }

}
