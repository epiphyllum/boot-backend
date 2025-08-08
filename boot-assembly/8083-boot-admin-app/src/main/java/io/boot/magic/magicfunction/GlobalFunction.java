package io.boot.magic.magicfunction;

import apijson.JSONObject;
import apijson.RequestMethod;
import io.boot.commons.security.user.SecurityUser;
import io.boot.magic.config.MyParser;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.config.MagicFunction;
import org.ssssssss.script.annotation.Comment;
import org.ssssssss.script.annotation.Function;
import org.ssssssss.script.functions.DateExtension;

import java.util.Date;

@Component  //注入到Spring容器中
public class GlobalFunction implements MagicFunction {

    // 脚本中直接使用 now();
    @Function
    @Comment("取当前时间")
    public static Date now() {
        return new Date();
    }
    // 脚本中使用 date_format(now())
    @Function
    @Comment("日期格式化")
    public static String date_format(@Comment("目标日期") Date target) {
        return target == null ? null : DateExtension.format(target, "yyyy-MM-dd HH:mm:ss");
    }

    // 脚本中使用 date_format(now(),'yyyy-MM-dd')
    @Function
    @Comment("日期格式化")
    public static String date_format(@Comment("目标日期") Date target, @Comment("格式") String pattern) {
        return target == null ? null : DateExtension.format(target, pattern);
    }

    // 脚本中直接使用ifnull() 调用
    @Function
    @Comment("判断值是否为空")
    public static Object ifnull(@Comment("目标值") Object target, @Comment("为空的值") Object trueValue, @Comment("不为空的值") Object falseValue) {
        return target == null ? trueValue : falseValue;
    }

    // 拿到当登录用户信息
    @Function
    @Comment("当前登录用户")
    public static Object securityUser(){
        return SecurityUser.getUser();
    }

    // 通过String调用
    @Function
    @Comment("调用APIJSON By String")
    public static Object apiGet(@Comment("查询体") Object body){
        return new MyParser(RequestMethod.GET).setNeedVerify(false).parseResponse((String)body);
    }

    // 通过map调用
    @Function
    @Comment("调用APIJSON By JSON")
    public static Object apiFetch(@Comment("查询体") Object body){
        String bodyStr = JSONObject.toJSONString(body);
        return new MyParser(RequestMethod.GET).setNeedVerify(false).parseResponse(bodyStr);
    }

}