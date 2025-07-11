package io.boot.magicext;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.ssssssss.script.annotation.Comment;
import org.ssssssss.script.functions.ExtensionMethod;

@Component  //注入到Spring容器中，之后会被自动注册
public class StringExtension implements ExtensionMethod {

    /**
     * 对什么类进行扩展
      * @return
     */
    @Override
    public Class<?> support() {
        return String.class;    //为String类型扩展方法
    }
    /**
     * 类似于scala/kotlin的扩展
     */
    @Comment("将字符串转为Integer(方法名的提示)")
    public static Integer toInt(String str){  // 第一个参数无需使用@Comment注解
        return NumberUtils.toInt(str);
    }

}
