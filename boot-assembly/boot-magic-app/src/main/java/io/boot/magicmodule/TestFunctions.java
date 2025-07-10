package io.boot.magicmodule;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.annotation.MagicModule;
import org.ssssssss.script.annotation.Comment;

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
}
