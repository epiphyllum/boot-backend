/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.commons.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 数据过滤注解
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFilter {
    /**
     * 表的别名
     */
    String tableAlias() default "";

    /**
     * 用户ID
     */
    String userId() default "creator";

    /**
     * 部门ID
     */
    String deptId() default "dept_id";

    /*
    String rid() default "rid"; // 区域经理
    String aid() default "aid"; // 代理
    String sid() default "sid"; // 销售员
    String mid() default "mid"; // 商户
    */
}