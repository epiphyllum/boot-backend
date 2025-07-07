/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.boot.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.rbac.redis;

import io.boot.commons.tools.redis.RedisKeys;
import io.boot.commons.tools.redis.RedisUtils;
import io.boot.commons.tools.utils.HttpContextUtils;
import io.boot.rbac.dto.SysMenuDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 菜单管理
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Component
public class SysMenuRedis {
    @Resource
    private RedisUtils redisUtils;

    public void delete(Long userId) {
        //清空菜单导航、权限标识
        redisUtils.deleteByPattern(RedisKeys.getUserMenuNavKey(userId));
        redisUtils.delete(RedisKeys.getUserPermissionsKey(userId));
    }

    public void setUserMenuNavList(Long userId, List<SysMenuDTO> menuList) {
        String key = RedisKeys.getUserMenuNavKey(userId, HttpContextUtils.getLanguage());
        redisUtils.set(key, menuList);
    }

    public List<SysMenuDTO> getUserMenuNavList(Long userId) {
        String key = RedisKeys.getUserMenuNavKey(userId, HttpContextUtils.getLanguage());
        return (List<SysMenuDTO>) redisUtils.get(key);
    }

    public void setUserPermissions(Long userId, Set<String> permsSet) {
        String key = RedisKeys.getUserPermissionsKey(userId);
        redisUtils.set(key, permsSet);
    }

    public Set<String> getUserPermissions(Long userId) {
        String key = RedisKeys.getUserPermissionsKey(userId);
        return (Set<String>) redisUtils.get(key);
    }

}
