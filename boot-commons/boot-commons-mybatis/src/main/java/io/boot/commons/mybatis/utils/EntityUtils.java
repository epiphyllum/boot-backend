/**
 * Copyright (c) 2018 BootCloud All rights reserved.
 *
 * https://www.bootcloud.io
 *
 * 版权所有，侵权必究！
 */

package io.boot.commons.mybatis.utils;

import io.boot.commons.mybatis.enums.DelFlagEnum;
import io.boot.commons.security.user.SecurityUser;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 实体工具类
 *
 * @author epiphyllum.zhou@gmail.com
 * @since 1.0.0
 */
public class EntityUtils {

    /**
     * 设置删除信息
     * @param ids     ids
     * @param entity  实体
     */
    public static <T> List<T> deletedBy(Long[] ids, Class<T> entity) {
        List<T> entityList = new ArrayList<>(ids.length);
        for(Long id : ids){
            T entityObject = deletedBy(id, entity);
            entityList.add(entityObject);
        }

        return entityList;
    }

    /**
     * 设置删除信息
     * @param id      id
     * @param entity  实体
     */
    public static <T> T deletedBy(Long id, Class<T> entity) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("id", id);
        map.put("updater", SecurityUser.getUserId());
        map.put("updateDate", new Date());
        map.put("delFlag", DelFlagEnum.DEL.value());

        T entityObject = null;
        try {
            entityObject = entity.newInstance();
        } catch (Exception e) {

        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            setValue(entityObject, entry.getKey(), entry.getValue());
        }

        return entityObject;
    }

    private static <T> void setValue(T entity, String key, Object value) {
        Class<?> clazz = entity.getClass();

        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                if (field.getName().equalsIgnoreCase(key)) {
                    try {
                        field.set(entity, value);
                    } catch (IllegalAccessException e) {
                        continue;
                    }
                    return;
                }
            }
        }
    }
}
