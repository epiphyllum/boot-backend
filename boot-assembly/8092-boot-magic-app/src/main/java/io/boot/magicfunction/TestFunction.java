package io.boot.magicfunction;

import io.boot.commons.security.user.SecurityUser;
import io.boot.commons.tools.utils.HttpContextUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.config.MagicFunction;
import org.ssssssss.script.annotation.Comment;
import org.ssssssss.script.annotation.Function;
import org.ssssssss.script.functions.DateExtension;

import java.util.Date;

@Component  //注入到Spring容器中
public class TestFunction implements MagicFunction {

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

    @Function
    @Comment("当前登录用户")
    public static Object securityUser(){
        return SecurityUser.getUser();
    }

    @Function
    @Comment("调用APIJSON")
    public static Object executeApi(@Comment("查询体") Object body){
        HttpSession session = HttpContextUtils.getHttpServletRequest().getSession();
        System.out.println("session:" + session);
        String rtn = "api return";
        return rtn;
    }

    @Function
    @Comment("调用APIJSON Template")
    public static Object executeApi(@Comment("查询体") Object template, @Comment("参数") Object params){
        HttpSession session = HttpContextUtils.getHttpServletRequest().getSession();
        System.out.println("session:" + session);
        String rtn = "api template return";
        return rtn;
    }

}