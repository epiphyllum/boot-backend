/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.commons.mybatis.aspect;

import cn.hutool.core.collection.CollUtil;
import io.boot.commons.mybatis.annotation.DataFilter;
import io.boot.commons.mybatis.entity.DataScope;
import io.boot.commons.mybatis.enums.UserTypeEnum;
import io.boot.commons.security.user.SecurityUser;
import io.boot.commons.security.user.UserDetail;
import io.boot.commons.tools.enums.SuperAdminEnum;
import io.boot.commons.tools.constant.Constant;
import io.boot.commons.tools.exception.ErrorCode;
import io.boot.commons.tools.exception.BootException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 数据过滤，切面处理类
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
@Aspect
@Component
public class DataFilterAspect {
    @Pointcut("@annotation(io.boot.commons.mybatis.annotation.DataFilter)")
    public void dataFilterCut() {

    }

    @Before("dataFilterCut()")
    public void dataFilter(JoinPoint point) {
        Object params = point.getArgs()[0];
        if (params != null && params instanceof Map) {
            UserDetail user = SecurityUser.getUser();

            //如果是超级管理员，则不进行数据过滤
            if (user.getSuperAdmin() == SuperAdminEnum.YES.value()) {
                return;
            }
            // 运营也不过滤数据
            if (user.getUserType() == UserTypeEnum.OPERATION.value()) {
                return;
            }

            try {
                //否则进行数据过滤
                Map map = (Map) params;
                String sqlFilter = getSqlFilter(user, point);
                map.put(Constant.SQL_FILTER, new DataScope(sqlFilter));
            } catch (Exception e) {

            }

            return;
        }

        throw new BootException(ErrorCode.DATA_SCOPE_PARAMS_ERROR);
    }

    /**
     * 表上有  rid, aid, sid, mid
     * 区域经理->代理->业务员->商户
     * 获取数据过滤的SQL:  user.getUserType:  operation, region, agent, salesman, merchant
     * operation: 不限制添加
     * region:    rid == user.getDeptId();  // 用户是哪个区域代理
     * agent:     aid == user.getDeptId();  // 用户是哪个代理
     * salesman:  sid == user.getDeptId();  // 用户是哪个销售员
     * merchant:  mid == user.getDeptId();  // 用户是哪个商户
     */
    private String getSqlFilterCustom(UserDetail user, JoinPoint point) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = point.getTarget().getClass().getDeclaredMethod(signature.getName(), signature.getParameterTypes());
        DataFilter dataFilter = method.getAnnotation(DataFilter.class);

        //获取表的别名
        String tableAlias = dataFilter.tableAlias();
        if (StringUtils.isNotBlank(tableAlias)) {
            tableAlias += ".";
        }

        StringBuilder sqlFilter = new StringBuilder();
        sqlFilter.append(" (");

        // 区域经理, 代理, 销售员
        if (user.getUserType().equals(UserTypeEnum.REGION)) {
            sqlFilter.append(tableAlias).append("rid").append("=").append(user.getDeptId());
        } else if (user.getUserType().equals(UserTypeEnum.AGENT)) {
            sqlFilter.append(tableAlias).append("aid").append("=").append(user.getDeptId());
        } else if (user.getUserType().equals(UserTypeEnum.SALESMAN)) {
            sqlFilter.append(tableAlias).append("sid").append("=").append(user.getDeptId());
        } else {
            // todo i18n
            throw new BootException("invalid user");
        }

        sqlFilter.append(")");
        return sqlFilter.toString();
    }

    /**
     *
     */
    private String getSqlFilter(UserDetail user, JoinPoint point) throws Exception {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = point.getTarget().getClass().getDeclaredMethod(signature.getName(), signature.getParameterTypes());
        DataFilter dataFilter = method.getAnnotation(DataFilter.class);

        //获取表的别名
        String tableAlias = dataFilter.tableAlias();
        if (StringUtils.isNotBlank(tableAlias)) {
            tableAlias += ".";
        }

        StringBuilder sqlFilter = new StringBuilder();
        sqlFilter.append(" (");

        //部门ID列表
        List<Long> deptIdList = user.getDeptIdList();
        if (CollUtil.isNotEmpty(deptIdList)) {
            sqlFilter.append(tableAlias).append(dataFilter.deptId());
            sqlFilter.append(" in(").append(StringUtils.join(deptIdList, ",")).append(")");
        }
        // x.dept_id in ( 1, 2, 3)

        //查询本人数据
        if (CollUtil.isNotEmpty(deptIdList)) {
            sqlFilter.append(" or ");
        }
        sqlFilter.append(tableAlias).append(dataFilter.userId()).append("=").append(user.getId());
        // x.user_id = 9

        sqlFilter.append(")");
        return sqlFilter.toString();

    }
}