/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.boot.service.impl;

import io.boot.commons.mybatis.service.impl.BaseServiceImpl;
import io.boot.commons.security.user.SecurityUser;
import io.boot.commons.security.user.UserDetail;
import io.boot.commons.tools.constant.Constant;
import io.boot.commons.tools.enums.SuperAdminEnum;
import io.boot.commons.tools.exception.ErrorCode;
import io.boot.commons.tools.exception.RenException;
import io.boot.commons.tools.utils.ConvertUtils;
import io.boot.commons.tools.utils.HttpContextUtils;
import io.boot.commons.tools.utils.TreeUtils;
import io.boot.dao.SysMenuDao;
import io.boot.dto.SysMenuDTO;
import io.boot.entity.SysMenuEntity;
import io.boot.enums.MenuTypeEnum;
import io.boot.redis.SysMenuRedis;
import io.boot.service.SysLanguageService;
import io.boot.service.SysMenuService;
import io.boot.service.SysRoleMenuService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 菜单管理
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
    @Resource
    private SysMenuRedis sysMenuRedis;
    @Resource
    private SysRoleMenuService sysRoleMenuService;
    @Resource
    private SysLanguageService sysLanguageService;

    @Override
    public SysMenuDTO get(Long id) {
        SysMenuEntity entity = baseDao.getById(id, HttpContextUtils.getLanguage());

        SysMenuDTO dto = ConvertUtils.sourceToTarget(entity, SysMenuDTO.class);

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysMenuDTO dto) {
        SysMenuEntity entity = ConvertUtils.sourceToTarget(dto, SysMenuEntity.class);

        //保存菜单
        insert(entity);
        saveLanguage(entity.getId(), "name", entity.getName());

        //清空当前用户，菜单导航、权限标识
        sysMenuRedis.delete(SecurityUser.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysMenuDTO dto) {
        SysMenuEntity entity = ConvertUtils.sourceToTarget(dto, SysMenuEntity.class);

        //上级菜单不能为自身
        if (entity.getId().equals(entity.getPid())) {
            throw new RenException(ErrorCode.SUPERIOR_MENU_ERROR);
        }

        //更新菜单
        updateById(entity);
        saveLanguage(entity.getId(), "name", entity.getName());

        //清空当前用户，菜单导航、权限标识
        sysMenuRedis.delete(SecurityUser.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        //逻辑删除
        logicDelete(new Long[]{id}, SysMenuEntity.class);

        //删除角色菜单关系
        sysRoleMenuService.deleteByMenuId(id);

        //清空当前用户，菜单导航、权限标识
        sysMenuRedis.delete(SecurityUser.getUserId());
    }

    @Override
    public List<SysMenuDTO> getMenuList(Integer type) {
        List<SysMenuEntity> menuList = baseDao.getMenuList(type, HttpContextUtils.getLanguage());

        List<SysMenuDTO> dtoList = ConvertUtils.sourceToTarget(menuList, SysMenuDTO.class);

        return TreeUtils.build(dtoList, Constant.MENU_ROOT);
    }

    @Override
    public List<SysMenuDTO> getUserMenuList(UserDetail userDetail, Integer type) {
        List<SysMenuEntity> menuList;

        //系统管理员，拥有最高权限
        if (userDetail.getSuperAdmin() == SuperAdminEnum.YES.value()) {
            menuList = baseDao.getMenuList(type, HttpContextUtils.getLanguage());
        } else {
            menuList = baseDao.getUserMenuList(userDetail.getId(), type, HttpContextUtils.getLanguage());
        }

        List<SysMenuDTO> dtoList = ConvertUtils.sourceToTarget(menuList, SysMenuDTO.class);

        return TreeUtils.build(dtoList);
    }

    @Override
    public List<SysMenuDTO> getUserMenuNavList(UserDetail userDetail) {
        List<SysMenuDTO> menuList = sysMenuRedis.getUserMenuNavList(userDetail.getId());
        if (menuList == null) {
            menuList = getUserMenuList(userDetail, MenuTypeEnum.MENU.value());

            sysMenuRedis.setUserMenuNavList(userDetail.getId(), menuList);
        }

        return menuList;
    }

    @Override
    public Set<String> getUserPermissions(UserDetail userDetail) {
        //用户权限列表
        Set<String> permsSet = sysMenuRedis.getUserPermissions(userDetail.getId());
        if (permsSet != null) {
            return permsSet;
        }

        //超级管理员，拥有最高权限
        List<SysMenuEntity> menuList;
        if (userDetail.getSuperAdmin() == SuperAdminEnum.YES.value()) {
            menuList = baseDao.getMenuList(null, HttpContextUtils.getLanguage());
        } else {
            menuList = baseDao.getUserMenuList(userDetail.getId(), null, HttpContextUtils.getLanguage());
        }

        permsSet = new HashSet<>();
        for (SysMenuEntity menu : menuList) {
            if (StringUtils.isNotBlank(menu.getPermissions())) {
                permsSet.addAll(Arrays.asList(menu.getPermissions().trim().split(",")));
            }
        }

        //保存到缓存
        sysMenuRedis.setUserPermissions(userDetail.getId(), permsSet);

        return permsSet;
    }

    @Override
    public List<SysMenuDTO> getListPid(Long pid) {
        List<SysMenuEntity> menuList = baseDao.getListPid(pid);

        return ConvertUtils.sourceToTarget(menuList, SysMenuDTO.class);
    }

    private void saveLanguage(Long tableId, String fieldName, String fieldValue) {
        sysLanguageService.saveOrUpdate("sys_menu", tableId, fieldName, fieldValue, HttpContextUtils.getLanguage());
    }

}